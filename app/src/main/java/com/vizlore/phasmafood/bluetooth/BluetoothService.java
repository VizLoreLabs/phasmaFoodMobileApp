package com.vizlore.phasmafood.bluetooth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.TestingUtils;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.model.DebugMeasurement;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.ui.CancelMeasurementActivity;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.utils.Config;
import com.vizlore.phasmafood.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.vizlore.phasmafood.utils.Config.BT_DEVICE_UUID_KEY;

/**
 * Created by smedic on 2/17/18.
 */

public class BluetoothService extends Service {

	private static final String TAG = "SMEDIC BS";

	public static final String DEVICE_OBJECT = "device_name";

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_OBJECT = 4;
	public static final int MESSAGE_TOAST = 5;
	private static final String CHANNEL_ID = "phasmaFoodId";

	private final IBinder mBinder = new LocalBinder();

	private CommunicationController bluetoothController;
	private BluetoothDevice connectingDevice;
	private CompositeDisposable disposable = new CompositeDisposable();

	//notification stuff
	private NotificationCompat.Builder notificationBuilder;
	private NotificationCompat.Builder completedNotificationBuilder;
	private NotificationCompat.Builder canceledNotificationBuilder;
	private NotificationManager notificationManager;

	private int notificationId = 10;
	private int completedNotificationId = 11;
	private int canceledNotificationId = 12;

	private boolean isMeasurementStarted;

	@Inject
	RxBluetooth rxBluetooth;

	@Inject
	DeviceApi deviceApi;

	@Inject
	MeasurementRepository measurementRepository;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case CommunicationController.STATE_CONNECTED:
							Log.d(TAG, "Connected to: " + connectingDevice.getName());

							registerBtDevice(connectingDevice);
							break;
						case CommunicationController.STATE_CONNECTING:
							Log.d(TAG, "Connecting...");
							break;
						case CommunicationController.STATE_LISTEN:
						case CommunicationController.STATE_NONE:
							Log.d(TAG, "Not connected");
							break;
					}
					break;
				case MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;
					String writeMessage = new String(writeBuf);
					break;
				case MESSAGE_READ:
					final int arg = msg.arg2;
					handleMessageRead(arg, msg.obj);
					break;
				case MESSAGE_DEVICE_OBJECT:
					connectingDevice = msg.getData().getParcelable(DEVICE_OBJECT);
					Toast.makeText(getApplicationContext(), "Connected to " + connectingDevice.getName(),
						Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(), msg.getData().getString("toast"),
						Toast.LENGTH_SHORT).show();
					break;
			}
			return false;
		}
	});

	/**
	 * Handle reading message received from Bluetooth device
	 *
	 * @param messageType 0 - measurement data
	 *                    1 - captured image
	 *                    3 - configuration
	 *                    5 - image received
	 *                    6 - measurement data received
	 */
	private void handleMessageRead(final int messageType, Object param) {

		switch (messageType) {
			case 0: // receiving measurement data
				int progressData = (int) param;
				notificationBuilder.setContentTitle("Receiving measurement data");
				notificationBuilder.setContentText(progressData + "%");
				notificationBuilder.setProgress(100, progressData, false);
				notificationManager.notify(notificationId, notificationBuilder.build());

				// workaround -> if stuck at 90, unlock for next measurement
				// TODO improve
				if (progressData >= 90) {
					isMeasurementStarted = false;
				}
				break;
			case 1: // receiving captured image
				int progressImage = (int) param;
				if (progressImage < 100) {
					notificationBuilder.setContentTitle("Receiving captured image");
					notificationBuilder.setContentText(progressImage + "%");
				} else {
					notificationBuilder.setContentTitle("Completed!");
					notificationBuilder.setContentText("");
				}
				notificationBuilder.setProgress(100, progressImage, false);
				notificationManager.notify(notificationId, notificationBuilder.build());
				break;
			case 2:
				//do nothing
				break;
			case 3: //receiving configuration
				switch ((String) param) {
					case "00":
						notificationBuilder.setContentTitle("(1/3) Getting VIS Data");
						break;
					case "01":
						notificationBuilder.setContentTitle("(2/3) Getting NIR Data");
						break;
					case "02":
						notificationBuilder.setContentTitle("(3/3) Getting FLUO Data");
						break;
					case "03":
						notificationBuilder.setContentTitle("Measurement done. Waiting for data");
						break;
					case "05":
						notificationBuilder.setContentTitle("White Reference (TODO)");
						break;
				}
				notificationBuilder.setProgress(100, 0, false);
				notificationManager.notify(notificationId, notificationBuilder.build());
				break;
			case 5: // Image received
				final byte[] readBufData = (byte[]) param;
				final Bitmap bitmap = BitmapFactory.decodeByteArray(readBufData, 0, readBufData.length);
				if (bitmap != null) {
					final String savedImagePath = Utils.tempFileImage(getApplicationContext(), bitmap, "captured_image");
					measurementRepository.saveMeasurementImagePath(savedImagePath);
					Log.d(TAG, "handleMessageRead: image path: " + savedImagePath);
					Log.e(TAG, "handleMessageRead: image size: " + readBufData.length);
				}

				//saveMeasurement(measurementDataResponse);
				//startResultsActivity();

				isMeasurementStarted = false;
				break;
			case 6:
				notifyFinishedStatus("Measurement completed!", "");
				Log.d(TAG, "handleMessageRead: Measurement data received");
				final byte[] readBufImage = (byte[]) param;
				String measurementDataResponse = new String(readBufImage);
				Log.d(TAG, "handleMessageRead: data: " + measurementDataResponse);
				if (measurementDataResponse.contains("End of Response")) {
					measurementDataResponse = measurementDataResponse.replace("End of Response", "");
				}
				saveMeasurement(measurementDataResponse);
				startResultsActivity();
				break;
			case 7:
				Log.d(TAG, "handleMessageRead: Cancel");
				notifyCanceledStatus("Measurement canceled!", "");
				break;
		}
	}

	private void saveMeasurement(@NonNull String data) {
		Log.d(TAG, "saveMeasurement - string size: " + data.length());
		//data = data.replace("End of Response", "");
		final Gson gson = new GsonBuilder().create();
		int fullSize = bluetoothController.getFullSize(); //debug only
		Measurement measurement;
		try {
			measurement = gson.fromJson(data, Measurement.class);
		} catch (IllegalStateException | JsonSyntaxException exception) {
			Log.e(TAG, "saveMeasurement: Wrong JSON format! Data: " + data);
			Toast.makeText(getApplicationContext(), "Parsing examination failed. Wrong JSON format", Toast.LENGTH_SHORT).show();
			notificationManager.cancel(notificationId);
			isMeasurementStarted = false;

			//DEBUG send string to server and report failure
			//postMeasurementString(data, false, fullSize, data.length());
			return;
		}

		//DEBUG send string to server and report success
		//postMeasurementString(data, true, fullSize, data.length());

		// received message parsed successfully
		if (measurement != null && measurement.getResponse() != null) {
			Log.d(TAG, "handleMessageRead: MEASUREMENT OK");
			//save measurement (too big to put in bundle or parcelable)
			measurementRepository.saveMeasurement(measurement);
			Log.d(TAG, "saveMeasurement: MEASUREMENT SAVED!");
		} else {
			Log.d(TAG, "saveMeasurement: ERROR SAVING MEASUREMENT!");
			Toast.makeText(getApplicationContext(), "Parsing examination failed", Toast.LENGTH_SHORT).show();
			notifyFinishedStatus("Parsing examination and saving failed!", measurement == null ? "Measurement is null!" :
				"Measurement response is null!");
		}
	}

	//DEBUG TODO remove SMEDIC
	public void postMeasurementString(@NonNull String rawData, boolean status, int expectedSize,
									  int receivedSize) {
		final DebugMeasurement debugMeasurement = new DebugMeasurement();
		debugMeasurement.setRawdata(rawData);
		debugMeasurement.setStatus(status);
		debugMeasurement.setExpectedSize(String.valueOf(expectedSize));
		debugMeasurement.setReceivedSize(String.valueOf(receivedSize));
		debugMeasurement.setPercentage(String.valueOf(100));
		disposable.add(measurementRepository.postMeasurementString(debugMeasurement)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> Log.d(TAG, "postMeasurementString: SUCCESS"),
				error -> Log.d(TAG, "postMeasurementString onError: " + error.toString())));
	}


	/**
	 * Notify when measurement is completed
	 */
	private void notifyFinishedStatus(@NonNull final String title, @NonNull final String message) {
		isMeasurementStarted = false;
		completedNotificationBuilder.setContentTitle(title);
		completedNotificationBuilder.setContentText(message);
		notificationManager.cancel(notificationId);
		notificationManager.notify(completedNotificationId, completedNotificationBuilder.build());
	}

	/**
	 * Notify when measurement is canceled
	 */
	private void notifyCanceledStatus(@NonNull final String title, @NonNull final String message) {
		isMeasurementStarted = false;
		canceledNotificationBuilder.setContentTitle(title);
		canceledNotificationBuilder.setContentText(message);
		notificationManager.cancel(notificationId);
		notificationManager.notify(canceledNotificationId, canceledNotificationBuilder.build());
	}

	private void startResultsActivity() {
		final Intent intent = new Intent(BluetoothService.this, MeasurementResultsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO: 2/17/18 check if device is there (shared prefs and getBondedDevices match)
		//connect();
		init();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	//returns the instance of the service
	public class LocalBinder extends Binder {
		public BluetoothService getServiceInstance() {
			return BluetoothService.this;
		}
	}

	private void init() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		createNotificationChannel();
		createInProgressNotification();
		createCompletedNotification();
		createCanceledNotification();
	}

	// mock sending a message (testing)
	public Single<Measurement> sendFakeMessage(@NonNull final String jsonFileName) {
		final Measurement measurement = new TestingUtils().readMeasurementFromJson(jsonFileName);

		return Single.just(measurement)
			.delay(2000, TimeUnit.MILLISECONDS);
	}

	//called multiple times
	public void sendMessage(final String data, boolean isCancelMessage) {
		Log.d(TAG, "sendData: SEND MESSAGE TO BT DEVICE. Is cancel: " + isCancelMessage);

		//unlock for new calls even if cancel fails
		if (isCancelMessage) {
			isMeasurementStarted = false;
		}

		if (!isCancelMessage && isMeasurementStarted) {
			Log.d(TAG, "sendData: measurement already in progress...");
			Toast.makeText(this, "Measurement in progress...", Toast.LENGTH_SHORT).show();
			return;
		}

		if (bluetoothController == null) {
			Log.e(TAG, "sendData: controller null");
			Toast.makeText(this, "Device not connected!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (bluetoothController.getState() != CommunicationController.STATE_CONNECTED) {
			Toast.makeText(this, "Connection was lost!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (data.length() > 0) {
			byte[] send = data.getBytes();
			bluetoothController.write(send);
			isMeasurementStarted = true;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.getComponent().inject(this);
		Log.d(TAG, "BluetoothService started! service: " + rxBluetooth);
		bluetoothController = new CommunicationController(handler);
	}

	@Override
	public void onDestroy() {
		if (bluetoothController != null) {
			bluetoothController.stop();
		}
		isMeasurementStarted = false;
	}

	public boolean isConnected() {
		return bluetoothController != null && bluetoothController.getState() == CommunicationController.STATE_CONNECTED;
	}

	public void openConnection(String deviceAddress) {
		rxBluetooth.cancelDiscovery();
		BluetoothDevice device = rxBluetooth.getRemoteDevice(deviceAddress);
		if (bluetoothController != null) {
			bluetoothController.connect(device);
		} else {
			bluetoothController = new CommunicationController(handler);
			bluetoothController.connect(device);
		}
		isMeasurementStarted = false;
	}

	public void closeConnection() {
		Log.d(TAG, "Disconnecting...");
		if (bluetoothController != null) {
			bluetoothController.stop();
			bluetoothController = null;
		}
		isMeasurementStarted = false;
	}

	private void registerBtDevice(final BluetoothDevice device) {
		disposable.add(deviceApi.readDevice(device.getAddress())
			.subscribeOn(Schedulers.computation())
			.subscribe(() -> { //completed
				},
				error -> createNewDevice(device)));
	}

	private void createNewDevice(final BluetoothDevice device) {
		final String deviceUuid;
		if (device != null && device.getUuids() != null && device.getUuids()[0] != null) {
			deviceUuid = device.getUuids()[0].getUuid().toString();
		} else {
			deviceUuid = "12345678910";
		}
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(Config.DEVICE_MAC, deviceUuid);
		requestBody.put(Config.DEVICE_UUID, "1234567890");
		disposable.add(deviceApi.createDevice(requestBody)
			.subscribeOn(Schedulers.computation())
			.subscribe(() -> {
					Log.d(TAG, "onComplete: device created");
					final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
					prefs.edit().putString(BT_DEVICE_UUID_KEY, deviceUuid).apply();
				},
				error -> Log.d(TAG, "onError: device not created:" + error.getMessage())));
	}

	/**
	 * Show this notification only when measurement is in progress
	 */
	private void createInProgressNotification() {
		notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
			.setPriority(Notification.PRIORITY_MAX)
			.setProgress(100, 0, false)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setChannelId(CHANNEL_ID)
			.addAction(createCancelAction())
			.setAutoCancel(true);
		//do not show it yet
	}

	/**
	 * Show this notification only when measurement is completed
	 */
	private void createCompletedNotification() {
		final Intent intent = new Intent(this, MeasurementResultsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
			intent, PendingIntent.FLAG_UPDATE_CURRENT);
		completedNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
			.setPriority(Notification.PRIORITY_MAX)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentIntent(pendingIntent)
			.setChannelId(CHANNEL_ID)
			.setAutoCancel(true);
		//do not show it yet
	}

	/**
	 * Show this notification only when measurement is completed
	 */
	private void createCanceledNotification() {
		canceledNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
			.setPriority(Notification.PRIORITY_MAX)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setChannelId(CHANNEL_ID)
			.setAutoCancel(true);
		//do not show it yet
	}

	private NotificationCompat.Action createCancelAction() {
		final Intent cancelMeasurementIntent = new Intent(this, CancelMeasurementActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
			cancelMeasurementIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		return new NotificationCompat.Action(android.R.drawable.ic_menu_close_clear_cancel, "Cancel", pendingIntent);
	}

	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			final CharSequence name = getString(R.string.channel_name);
			final String description = getString(R.string.channel_description);
			final int importance = NotificationManager.IMPORTANCE_DEFAULT;
			final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			notificationManager.createNotificationChannel(channel);
		}
	}
}
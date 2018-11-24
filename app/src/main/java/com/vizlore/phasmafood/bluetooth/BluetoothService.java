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
import android.os.Environment;
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
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.TestingUtils;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.utils.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
	private Disposable disposable = new CompositeDisposable();
	private String measurementDataResponse;

	//notification stuff
	private NotificationCompat.Builder notificationBuilder;
	private NotificationManager notificationManager;
	private int notificationId = 10;

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
					Log.e(TAG, "Me: " + writeMessage);
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
				break;
			case 1: // receiving captured image
				int progressImage = (int) param;
				notificationBuilder.setContentTitle("Receiving captured image");
				notificationBuilder.setContentText(progressImage + "%");
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
						notificationBuilder.setContentTitle("Measurement Finished. Waiting for data...");
						break;
					case "05":
						notificationBuilder.setContentTitle("White Reference (TODO)");
						break;
				}
				notificationBuilder.setProgress(100, 0, false);
				notificationManager.notify(notificationId, notificationBuilder.build());
				break;
			case 5: // Image received
				Log.d(TAG, "handleMessage: IMAGE RECEIVED");
				final byte[] readBufData = (byte[]) param;
				final Bitmap bitmap = BitmapFactory.decodeByteArray(readBufData, 0, readBufData.length);
				final String savedImagePath = tempFileImage(getApplicationContext(), bitmap, "captured_image");
				Log.d(TAG, "handleMessage: image path: " + savedImagePath);
				Log.e(TAG, "handleMessage: image size: " + readBufData.length);
				// TODO: 10/21/18 dismiss notification?

				final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
				final Measurement measurement = gson.fromJson(measurementDataResponse, Measurement.class);
				Log.d(TAG, "handleMessage: measurement: " + measurement);
				// TODO: 9/11/18 improve and move out of here
				if (measurement != null && measurement.getResponse() != null) {
					Log.d(TAG, "handleMessage: ok measurement");
					//save measurement (too big to put in bundle or parcelable)
					measurementRepository.saveMeasurement(measurement);
					measurementRepository.saveMeasurementImagePath(savedImagePath);

					// start activity where user can see measurement charts and captured image
					final Intent intent = new Intent(BluetoothService.this, MeasurementResultsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Parsing examination failed", Toast.LENGTH_SHORT).show();
				}
				break;
			case 6:
				Log.d(TAG, "handleMessage: Measurement data received");
				final byte[] readBufImage = (byte[]) param;
				measurementDataResponse = new String(readBufImage);
				Log.d(TAG, "handleMessage: data: " + measurementDataResponse);
				break;
		}
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
		createNotification();
	}

	public String tempFileImage(Context context, Bitmap bitmap, String name) {

		File outputDir = context.getCacheDir();
		File imageFile = new File(outputDir, name + ".jpg");

		OutputStream os;
		try {
			os = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.e(TAG, "Error writing file", e);
		}

		return imageFile.getAbsolutePath();
	}

	// dump measurement response data if needed
	private static String tempFile2(byte[] text) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/smedic");
		dir.mkdirs();
		File file = new File(dir, "output.txt");
		try {
			FileOutputStream f = new FileOutputStream(file);
			f.write(text);
			f.flush();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	//must be called just once from outside (activity for instance)
	public void sendMessage(final String message) {
		sendData(message);
	}

	// mock sending a message (testing)
	public Single<Measurement> sendFakeMessage(@NonNull final String jsonFileName) {
		final Measurement measurementJson = TestingUtils.readMeasurementFromJson(jsonFileName);
		return Single.just(measurementJson)
			.delay(2000, TimeUnit.MILLISECONDS);
	}

	//called multiple times
	private void sendData(final String data) {
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
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.getComponent().inject(this);
		Log.d(TAG, "BluetoothService started! service: " + rxBluetooth);

//		// check if bluetooth is currently enabled and ready for use
//		if (!rxBluetooth.isBluetoothEnabled()) {
//			Log.d(TAG, "Bluetooth should be enabled first!");
//			stopSelf();
//		}
		bluetoothController = new CommunicationController(handler);
	}

	@Override
	public void onDestroy() {
//		compositeDisposable.dispose();
//		disposable.dispose();
//		super.onDestroy();
//		Log.d(TAG, "BluetoothService stopped!");
		if (bluetoothController != null) {
			bluetoothController.stop();
		}
	}

	public void connectToCommunicationController(String deviceAddress) {
		rxBluetooth.cancelDiscovery();
		BluetoothDevice device = rxBluetooth.getRemoteDevice(deviceAddress);
		if (bluetoothController != null) {
			Log.d(TAG, "Going For Connection");
			bluetoothController.connect(device);
		} else {
			bluetoothController = new CommunicationController(handler);
			bluetoothController.connect(device);
		}
	}

	public void disconnectFromCommunicationController() {
		Log.d(TAG, "disconnect");
		if (bluetoothController != null) {
			Log.d(TAG, "Stopping Controller");
			bluetoothController.stop();
			bluetoothController = null;
		}
	}

	// TODO: 3/5/18 refactor in RxJava fashion
	private void registerBtDevice(final BluetoothDevice device) {
		deviceApi.readDevice(device.getAddress())
			.subscribeOn(Schedulers.computation())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					//device found - already registered
				}

				@Override
				public void onError(Throwable e) {
					// device did not find, register it

					// TODO: 9/8/18
					final String deviceUuid = device.getUuids()[0].getUuid().toString();
					Map<String, String> requestBody = new HashMap<>();
					requestBody.put(Config.DEVICE_MAC, deviceUuid);
					requestBody.put(Config.DEVICE_UUID, "1234567890");

					deviceApi.createDevice(requestBody)
						.subscribeOn(Schedulers.computation())
						.subscribe(new CompletableObserver() {
							@Override
							public void onSubscribe(Disposable d) {
							}

							@Override
							public void onComplete() {
								Log.d(TAG, "onComplete: device created");
								final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
								prefs.edit().putString(BT_DEVICE_UUID_KEY, deviceUuid).apply();
							}

							@Override
							public void onError(Throwable e) {
								Log.d(TAG, "onError: device not created:" + e.getMessage());
							}
						});

				}
			});
	}

	private void createNotification() {
		createNotificationChannel();

		final Intent intent = new Intent(BluetoothService.this, MeasurementResultsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
			intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
			.setPriority(Notification.PRIORITY_MAX)
			.setProgress(100, 0, false)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentIntent(pendingIntent)
			.setChannelId(CHANNEL_ID)
			.setAutoCancel(true);
		//do not show it yet
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

	public void closeConnection() {
		if (bluetoothController != null) {
			if (disposable != null) {
				disposable.dispose();
			}
			bluetoothController.stop();
			bluetoothController = null;
		}
	}

	public boolean isConnected() {
		return bluetoothController != null && bluetoothController.getState() == CommunicationController.STATE_CONNECTED;
	}
}
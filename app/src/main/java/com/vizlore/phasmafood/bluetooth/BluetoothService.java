package com.vizlore.phasmafood.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.TestingUtils;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

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

	private final IBinder mBinder = new LocalBinder();

	private CommunicationController bluetoothController;
	private BluetoothDevice connectingDevice;

	@Inject
	RxBluetooth rxBluetooth;

	@Inject
	DeviceApi deviceApi;

	private Disposable disposable = new CompositeDisposable();

	//test
	private String jsonReceived = " ";
	private int ack = 0;
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

					// message is here, parse it if it's last one
					// or append if not

					byte[] readBuf = (byte[]) msg.obj;
					final String readMessage = new String(readBuf, 0, msg.arg1);
					Log.e(TAG, "Received: " + connectingDevice.getName() + ":  " + readMessage);

					if (readMessage.equals("End of Response")) {
						Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_SHORT).show();

						final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
						final Measurement measurement = gson.fromJson(jsonReceived, Measurement.class);

						// TODO: 9/11/18 improve and move out of here
						if (measurement != null && measurement.getResponse() != null) {
							//save measurement (too big to put in bundle or parcelable)
							MyApplication.getInstance().saveMeasurement(measurement);
							final Intent intent = new Intent(BluetoothService.this, MeasurementResultsActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(), "Parsing examination failed", Toast.LENGTH_SHORT).show();
						}

					} else {
						//append next message
						jsonReceived += readMessage;
						ack++;
						sendAck(ack);
					}
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

	//must be called just once from outside (activity for instance)
	public void sendMessage(final String message) {
		//reset ack and received message
		ack = 0;
		jsonReceived = " ";
		sendData(message);
	}

	// mock sending a message (testing)
	public Single<Measurement> sendFakeMessage(@NonNull final String jsonFileName) {
		final Measurement measurementJson = TestingUtils.readMeasurementFromJson(jsonFileName);
		return Single.just(measurementJson)
			.delay(3000, TimeUnit.MILLISECONDS);
	}

	private void sendAck(final int ack) {
		JSONObject request = null;
		try {
			request = new JSONObject();
			JSONObject RequestBody = new JSONObject();
			RequestBody.put("Use cases", "ACK");
			RequestBody.put("ACK_NUM", String.valueOf(ack));
			request.put("Request", RequestBody);
		} catch (JSONException e) {
			Log.e(TAG, "unexpected JSON exception", e);
		}
		if (request.toString() != null) {
			sendData(request.toString());
		}
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
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO: 2/17/18 check if device is there (shared prefs and getBondedDevices match)
		//connect();
		return START_STICKY;
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
								final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
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
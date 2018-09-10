package com.vizlore.phasmafood.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.api.UserApi;
import com.vizlore.phasmafood.model.User;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.Sample;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.utils.Config;
import com.vizlore.phasmafood.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by smedic on 2/17/18.
 */

public class BluetoothService extends Service {

	private static final String TAG = "SMEDIC BS";

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_OBJECT = 4;
	public static final int MESSAGE_TOAST = 5;

	private final IBinder mBinder = new LocalBinder();

	private CommunicationController bluetoothController;
	private BluetoothDevice connectingDevice;
	public static final String DEVICE_OBJECT = "device_name";

	@Inject
	RxBluetooth rxBluetooth;

	@Inject
	DeviceApi deviceApi;

	@Inject
	MeasurementRepository measurementRepository;

	@Inject
	UserApi userApi;

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

						// TODO: 3/5/18 use measurement
						// TODO: 3/5/18 remove this fake
//						Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
//						String json = new JsonFileLoader().fromAsset("result1.json");
//						Examination examination = gson.fromJson(json, Examination.class);

						final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
						final Measurement measurement = gson.fromJson(jsonReceived, Measurement.class);

						//save examination
						MyApplication.getInstance().saveMeasurement(measurement);
						if (measurement != null) {
							sendMeasurementToServer(measurement.getResponse().getSample());
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
	public void sendMessage(String message) {
		//reset ack and received message
		ack = 0;
		jsonReceived = " ";
		sendData(message);
	}


	private void sendAck(int ack) {
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
	private void sendData(String data) {
		if (bluetoothController == null) {
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

	private void sendMeasurementToServer(@NonNull final Sample sample) {

		userApi.getCurrentProfile()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new SingleObserver<ResponseBody>() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onSuccess(ResponseBody responseBody) {
					try {
						Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
						User user = gson.fromJson(responseBody.string(), User.class);
						createExaminationRequest(sample, user.id());

					} catch (IOException e) {
						Toast.makeText(BluetoothService.this, "Error parsing response", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}

				@Override
				public void onError(Throwable e) {
					Toast.makeText(BluetoothService.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			});
	}

	private void createExaminationRequest(@NonNull Sample sample, @NonNull final String userId) {

		Log.d(TAG, "createExaminationRequest: DEVICE ID: " + connectingDevice.getAddress());

		String sampleId = String.valueOf(new Date().getTime() % 1000000000);
		sample.setSampleID(sampleId);
		sample.setUserID(userId);
		sample.setDeviceID(connectingDevice.getAddress());
		sample.setMobileID(Utils.getMobileUUID());

		// TODO: 9/4/18
		// add
		// laboratory required
		// foodType required
		// useCase required

		measurementRepository.createMeasurementRequest(sample)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				() -> Toast.makeText(BluetoothService.this, "Request created", Toast.LENGTH_SHORT).show(),
				e -> {
					Log.d(TAG, "onError error: " + e.toString());
					Toast.makeText(BluetoothService.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
				});
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
					requestBody.put(Config.DEVICE_MAC, "1234567890");
					requestBody.put(Config.DEVICE_UUID, deviceUuid);

					deviceApi.createDevice(requestBody)
						.subscribeOn(Schedulers.computation())
						.subscribe(new CompletableObserver() {
							@Override
							public void onSubscribe(Disposable d) {
							}

							@Override
							public void onComplete() {
								Log.d(TAG, "onComplete: device created");
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

	public boolean IsConnected() {
		if (bluetoothController != null) {
			return bluetoothController.getState() == CommunicationController.STATE_CONNECTED;
		} else {
			return false;
		}
	}

}
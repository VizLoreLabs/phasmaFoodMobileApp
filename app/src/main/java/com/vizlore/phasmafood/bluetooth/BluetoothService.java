package com.vizlore.phasmafood.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.bluetooth.exceptions.ConnectionErrorException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

	private CommunicationController chatController;
	private BluetoothDevice connectingDevice;
	public static final String DEVICE_OBJECT = "device_name";

	@Inject
	RxBluetooth rxBluetooth;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private Disposable disposable = new CompositeDisposable();

	private BluetoothSocket btSocket = null;
	private BluetoothConnection connection = null;

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
							//btnConnect.setEnabled(false);
							//btnDisConnect.setEnabled(true);
							break;
						case CommunicationController.STATE_CONNECTING:
							Log.d(TAG, "Connecting...");
							//btnConnect.setEnabled(false);
							break;
						case CommunicationController.STATE_LISTEN:
						case CommunicationController.STATE_NONE:
							Log.d(TAG, "Not connected");
							//btnConnect.setEnabled(true);
							//btnDisConnect.setEnabled(false);
							break;
					}
					break;
				case MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;

					String writeMessage = new String(writeBuf);
					// chatMessages.add("Me: " + writeMessage);
					// chatAdapter.notifyDataSetChanged();
					Log.e(TAG, "Me: " + writeMessage);
					break;
				case MESSAGE_READ:

					byte[] readBuf = (byte[]) msg.obj;
					final String readMessage = new String(readBuf, 0, msg.arg1);
					Log.e(TAG, "Received: " + connectingDevice.getName() + ":  " + readMessage);
					if (readMessage.equals("End of Response")) {
						Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_SHORT).show();
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
			sendMessage(request.toString());
		}
	}

	public void sendMessage(String message) {

		//reset ack and received message
		ack = 0;
		jsonReceived = " ";

		if (chatController.getState() != CommunicationController.STATE_CONNECTED) {
			Toast.makeText(this, "Connection was lost!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (message.length() > 0) {
			byte[] send = message.getBytes();
			chatController.write(send);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.getComponent().inject(this);

//		Log.d(TAG, "BluetoothService started! service: " + rxBluetooth);
//		if (!rxBluetooth.isBluetoothAvailable()) {
//			// handle the lack of bluetooth support
//			Log.d(TAG, "Bluetooth is not supported!");
//			stopSelf();
//		}
//		// check if bluetooth is currently enabled and ready for use
//		if (!rxBluetooth.isBluetoothEnabled()) {
//			Log.d(TAG, "Bluetooth should be enabled first!");
//			stopSelf();
//		}
		chatController = new CommunicationController(handler);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO: 2/17/18 check if device is there (shared prefs and getBondedDevices match)
		//connect();
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
//		compositeDisposable.dispose();
//		disposable.dispose();
//		super.onDestroy();
//		Log.d(TAG, "BluetoothService stopped!");
		if (chatController != null)
			chatController.stop();
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
		chatController.connect(device);
	}

	public void disconnectFromCommunicationController() {
		Log.d(TAG, "disconnect");
		chatController.stop();
	}

	public void connect() {
		rxBluetooth.cancelDiscovery();

		if (rxBluetooth.getBondedDevices().size() > 0) {
			// get first device - FIXME: 2/17/18 add selected
			BluetoothDevice device = rxBluetooth.getBondedDevices().get(0);
			Log.d(TAG, "connect: device: " + device);
			try {
				connectToDevice(device.getAddress());
				listenInputStream();
			} catch (ConnectionErrorException e) {
				Log.d(TAG, "ConnectionErrorException: " + e.getMessage());
				e.printStackTrace();
				try {
					btSocket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public void connectToDevice(final String deviceAddress) throws ConnectionErrorException {

		if (connection != null) {
			if (disposable != null) {
				disposable.dispose();
			}
			Log.d(TAG, "connectToDevice: close previous connection");
			connection.closeConnection();
		}

		if (rxBluetooth.isDiscovering()) {
			rxBluetooth.cancelDiscovery();
		}

		BluetoothDevice device = rxBluetooth.getRemoteDevice(deviceAddress);

		try {
			btSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
		} catch (IOException e2) {
			throw new ConnectionErrorException(e2.getMessage());
		}

		try {
			btSocket.connect();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionErrorException(e.getMessage());
		}
		try {
			connection = new BluetoothConnection(btSocket);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectionErrorException(e.getMessage());
		}
	}

	public void sendData(String data) {
		Log.d(TAG, "sendData: " + data);
		if (connection != null) {
			connection.send(data);
		}
	}

	private void listenInputStream() {
		connection.observeStringDataObservable()
			.observeOn(Schedulers.newThread())
			.subscribeOn(Schedulers.computation())
			.subscribe(new Observer<String>() {
				@Override
				public void onSubscribe(Disposable d) {

				}

				@Override
				public void onNext(String s) {
					Log.d(TAG, "onNext:" + s);
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError: " + e.toString());
				}

				@Override
				public void onComplete() {

				}
			});
	}

	public void closeConnection() {
		if (connection != null) {
			if (disposable != null) {
				disposable.dispose();
			}
			connection.closeConnection();
		}
	}
}
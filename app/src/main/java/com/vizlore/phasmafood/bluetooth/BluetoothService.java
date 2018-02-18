package com.vizlore.phasmafood.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.bluetooth.exceptions.ConnectionErrorException;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 2/17/18.
 */

public class BluetoothService extends Service {

	private static final String TAG = "SMEDIC BS";

	@Inject
	RxBluetooth rxBluetooth;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private Disposable disposable = new CompositeDisposable();

	private BluetoothSocket btSocket = null;
	private BluetoothConnection connection = null;

	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.getComponent().inject(this);

		Log.d(TAG, "BluetoothService started! service: " + rxBluetooth);
		if (!rxBluetooth.isBluetoothAvailable()) {
			// handle the lack of bluetooth support
			Log.d(TAG, "Bluetooth is not supported!");
			stopSelf();
		}
		// check if bluetooth is currently enabled and ready for use
		if (!rxBluetooth.isBluetoothEnabled()) {
			Log.d(TAG, "Bluetooth should be enabled first!");
			stopSelf();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null && intent.getExtras() != null) {

			final String data = intent.getStringExtra("DATA");
			Log.d(TAG, "onStartCommand: DATA TO SEND: " + data);

			// TODO: 2/17/18 check if device is there (shared prefs and getBondedDevices match)
			if (rxBluetooth.getBondedDevices().size() > 0) {
				// get first device - FIXME: 2/17/18 add selected
				BluetoothDevice device = rxBluetooth.getBondedDevices().get(0);
				try {
					connectToDevice(device.getAddress());
					sendData(data);

					if (!disposable.isDisposed()) {
						disposable = connection.observeString()
							.observeOn(Schedulers.newThread())
							.subscribeOn(Schedulers.computation())
							.subscribe(t -> Log.d(TAG, "connectToDevice: READ: " + t), e -> {
								e.printStackTrace();
								Log.d(TAG, "connectToDevice: error: " + e.getMessage());
							});
					}

				} catch (ConnectionErrorException e) {
					e.printStackTrace();
					try {
						btSocket.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		}

		return START_STICKY;
	}


	@Override
	public void onDestroy() {
		compositeDisposable.dispose();
		disposable.dispose();
		super.onDestroy();
		Log.d(TAG, "BluetoothService stopped!");
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
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
			btSocket.connect();
			try {
				connection = new BluetoothConnection(btSocket);
			} catch (Exception e) {
				Log.d(TAG, "connectToDevice socket error: " + e.getMessage());
				e.printStackTrace();
				throw new ConnectionErrorException(e.getMessage());
			}

		} catch (IOException e2) {
			//insert code to deal with this
			Log.d(TAG, "connectToDevice exception: " + e2.getMessage());
			throw new ConnectionErrorException(e2.getMessage());
		}
	}

	public void sendData(String data) {
		Log.d(TAG, "onCreate: SEND DATA");
		Log.d(TAG, "onCreate: bt socket: " + btSocket);
		Log.d(TAG, "onCreate: connection: " + connection);
		if (connection != null) {
			connection.send(data);
		}
	}
}
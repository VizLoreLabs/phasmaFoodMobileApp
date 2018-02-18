package com.vizlore.phasmafood.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.bluetooth.exceptions.ConnectionErrorException;

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

	private final IBinder mBinder = new LocalBinder();

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
		// TODO: 2/17/18 check if device is there (shared prefs and getBondedDevices match)
		connect();
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		compositeDisposable.dispose();
		disposable.dispose();
		super.onDestroy();
		Log.d(TAG, "BluetoothService stopped!");
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
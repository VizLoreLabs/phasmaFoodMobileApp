package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.bluetooth.BluetoothConnection;
import com.vizlore.phasmafood.bluetooth.BtPredicate;
import com.vizlore.phasmafood.bluetooth.RxBluetooth;
import com.vizlore.phasmafood.bluetooth.events.BondStateEvent;
import com.vizlore.phasmafood.bluetooth.events.ConnectionStateEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 1/14/18.
 */

public class BluetoothViewModel extends ViewModel {

	private static final String TAG = "SMEDIC BVM";

	@Inject
	RxBluetooth rxBluetooth;

	private MutableLiveData<Boolean> shouldEnableBluetooth;
	private MutableLiveData<BluetoothDevice> foundBluetoothDeviceLiveData;
	private MutableLiveData<String> discoveryEventLiveData;
	private MutableLiveData<ConnectionStateEvent> connectionStateLiveData;
	private MutableLiveData<List<BluetoothDevice>> bluetoothDevicesLiveData;
	private MutableLiveData<BondStateEvent> bondStateEventLiveData;
	private MutableLiveData<Integer> bluetoothStateLiveData;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private Disposable disposable = new CompositeDisposable();
	private BluetoothSocket btSocket = null;
	private BluetoothConnection connection = null;

	public BluetoothViewModel() {
		MyApplication.getComponent().inject(this);
	}

	public RxBluetooth getRxBluetooth() {
		return rxBluetooth;
	}

	public LiveData<Boolean> checkBluetooth() {

		if (shouldEnableBluetooth == null) {
			shouldEnableBluetooth = new MutableLiveData<>();
		}

		if (!rxBluetooth.isBluetoothAvailable()) {
			// handle the lack of bluetooth support
			Log.d(TAG, "Bluetooth is not supported!");
		} else {
			// check if bluetooth is currently enabled and ready for use
			if (!rxBluetooth.isBluetoothEnabled()) {
				Log.d(TAG, "Enabling Bluetooth");
				shouldEnableBluetooth.setValue(true);
			} else {
				// you are ready
				Log.d(TAG, "onCreate: ready");
				// TODO: 1/10/18 enable all bluetooth related buttons
				shouldEnableBluetooth.setValue(false);
			}
		}
		return shouldEnableBluetooth;
	}

	public void startDiscovery() {
		rxBluetooth.startDiscovery();
	}

	public void cancelDiscovery() {
		rxBluetooth.cancelDiscovery();
	}

	public void sendData(String data) {
		Log.d(TAG, "onCreate: SEND DATA");
		Log.d(TAG, "onCreate: bt socket: " + btSocket);
		Log.d(TAG, "onCreate: connection: " + connection);
		if (connection != null) {
			connection.send(data);
		}
	}

	public void createBond(final String deviceAddress) {
		BluetoothDevice device = rxBluetooth.getRemoteDevice(deviceAddress);
		if (device != null) {
			device.createBond();
		}
	}

	public void connectToDevice(final String deviceAddress) {

		if (connection != null) {
			if (disposable != null) {
				disposable.dispose();
			}
			connection.closeConnection();
		}

		rxBluetooth.cancelDiscovery();

		BluetoothDevice device = rxBluetooth.getRemoteDevice(deviceAddress);

		try {
			btSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
			btSocket.connect();
			try {
				connection = new BluetoothConnection(btSocket);
			} catch (Exception e) {
				Log.d(TAG, "onCreate: socket error: " + e.getMessage());
				e.printStackTrace();
			}

		} catch (IOException e2) {
			//insert code to deal with this
			Log.d(TAG, "Exception: " + e2.getMessage());
			try {
				btSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		disposable = connection.observeString()
				.observeOn(Schedulers.newThread())
				.subscribeOn(Schedulers.computation())
				.subscribe(t -> Log.d(TAG, "onCreate: READ: " + t), e -> {
					e.printStackTrace();
					Log.d(TAG, "onCreate: error: " + e.getMessage());
				});
	}

	public LiveData<BluetoothDevice> getFoundDevices() {

		if (foundBluetoothDeviceLiveData == null) {
			foundBluetoothDeviceLiveData = new MutableLiveData<>();
		}

		compositeDisposable.add(rxBluetooth.observeDevices()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.subscribe(bluetoothDevice -> {
					Log.d(TAG, "getFoundDevices - Device found: " + bluetoothDevice.getAddress() + " - " + bluetoothDevice.getName());
					foundBluetoothDeviceLiveData.setValue(bluetoothDevice);
				}));
		return foundBluetoothDeviceLiveData;
	}

	public LiveData<String> getDiscoveryEvents() {

		if (discoveryEventLiveData == null) {
			discoveryEventLiveData = new MutableLiveData<>();
		}

		compositeDisposable.add(rxBluetooth.observeDiscovery()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.ACTION_DISCOVERY_STARTED, BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
				.subscribe(action -> {
					//start.setText(R.string.button_searching);
					Log.d(TAG, "observerDiscovery EVENT: " + action);
					discoveryEventLiveData.setValue(action);
				}));
		return discoveryEventLiveData;
	}

	public LiveData<Integer> getBluetoothState() {

		if (bluetoothStateLiveData == null) {
			bluetoothStateLiveData = new MutableLiveData<>();
		}
		compositeDisposable.add(rxBluetooth.observeBluetoothState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.STATE_ON, BluetoothAdapter.STATE_OFF, BluetoothAdapter.STATE_TURNING_OFF,
						BluetoothAdapter.STATE_TURNING_ON))
				.subscribe(state -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorActive));
					Log.d(TAG, "observeBluetoothState EVENT: state on");
					bluetoothStateLiveData.setValue(state);
				}));
		return bluetoothStateLiveData;
	}

	public LiveData<ConnectionStateEvent> getConnectionState() {

		if (connectionStateLiveData == null) {
			connectionStateLiveData = new MutableLiveData<>();
		}

		compositeDisposable.add(rxBluetooth.observeConnectionState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.STATE_CONNECTED, BluetoothAdapter.STATE_DISCONNECTED))
				.subscribe(event -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorInactive));
					Log.d(TAG, "observeBondState EVENT: connection: " + event.toString());
					connectionStateLiveData.setValue(event);

				}));
		return connectionStateLiveData;
	}

	public LiveData<BondStateEvent> getBondState() {

		if (bondStateEventLiveData == null) {
			bondStateEventLiveData = new MutableLiveData<>();
		}

		compositeDisposable.add(rxBluetooth.observeBondState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
				.subscribe(event -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorInactive));
					bondStateEventLiveData.setValue(event);
				}));
		return bondStateEventLiveData;
	}

	/**
	 * Gets devices that we are bonded to
	 *
	 * @return list of devices
	 */
	public LiveData<List<BluetoothDevice>> getBondedDevices() {

		if (bluetoothDevicesLiveData == null) {
			bluetoothDevicesLiveData = new MutableLiveData<>();
		}

		List<BluetoothDevice> list = new ArrayList<>(rxBluetooth.getBondedDevices());
		bluetoothDevicesLiveData.setValue(list);
		return bluetoothDevicesLiveData;
	}

	@Override
	protected void onCleared() {

		if (rxBluetooth != null) {
			// Make sure we're not doing discovery anymore
			rxBluetooth.cancelDiscovery();
		}

		disposable.dispose();
		compositeDisposable.dispose();
		super.onCleared();
	}
}

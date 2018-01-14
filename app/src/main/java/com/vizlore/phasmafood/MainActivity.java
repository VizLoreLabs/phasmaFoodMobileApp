package com.vizlore.phasmafood;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vizlore.phasmafood.bluetooth.BluetoothConnection;
import com.vizlore.phasmafood.bluetooth.BtPredicate;
import com.vizlore.phasmafood.bluetooth.RxBluetooth;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "SMEDIC";

	private static final int REQUEST_PERMISSION_COARSE_LOCATION = 0;
	private static final int REQUEST_ENABLE_BT = 1;

	@BindView(R.id.found)
	ListView foundListView;

	@BindView(R.id.bonded)
	ListView bondedListView;

	@BindView(R.id.bondedNumber)
	TextView bondedNumber;

	@BindView(R.id.button)
	Button startDiscovery;

	@BindView(R.id.button2)
	Button cancelDiscovery;

	@BindView(R.id.perform)
	Button perform;

	private List<String> list = new ArrayList<>();
	private ArrayAdapter adapter;

	private List<String> bondedList = new ArrayList<>();
	private ArrayAdapter bondedAdapter;

	private RxBluetooth rxBluetooth;
	private List<BluetoothDevice> devices = new ArrayList<>();
	private Intent bluetoothServiceIntent;
	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	private Disposable disposable = new CompositeDisposable();
	private BluetoothSocket btSocket = null;
	private BluetoothConnection connection = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		rxBluetooth = new RxBluetooth(getApplicationContext());

		adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
		foundListView.setAdapter(adapter);

		bondedAdapter = new ArrayAdapter<>(this, R.layout.list_item, bondedList);
		bondedListView.setAdapter(bondedAdapter);

		UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
		userViewModel.hasSession().observe(this, hasSession -> {
			Log.d(TAG, "onCreate: has session: " + hasSession);
		});

		// bond to device on click
		foundListView.setOnItemClickListener((adapterView, view, i, l) -> {
			Log.d(TAG, "onCreate: bond to: " + list.get(i));
			BluetoothDevice device = rxBluetooth.getRemoteDevice(list.get(i));
			device.createBond();
		});

		// connect to device
		bondedListView.setOnItemClickListener((adapterView, view, i, l) -> {

			if (connection != null) {
				if (disposable != null) {
					disposable.dispose();
				}
				connection.closeConnection();
			}

			rxBluetooth.cancelDiscovery();
			BluetoothDevice device = rxBluetooth.getRemoteDevice(bondedList.get(i));

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
		});

		// start discovery
		startDiscovery.setOnClickListener(v -> {
			devices.clear();
			//setAdapter(devices);
			if (ContextCompat.checkSelfPermission(MainActivity.this,
					android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG, "onClick: request permissions");
				ActivityCompat.requestPermissions(MainActivity.this,
						new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
						REQUEST_PERMISSION_COARSE_LOCATION);
			} else {
				Log.d(TAG, "onClick: start discovery");
				rxBluetooth.startDiscovery();
			}
		});

		cancelDiscovery.setOnClickListener(v -> {
			Log.d(TAG, "onClick: cancel discovery");
			rxBluetooth.cancelDiscovery();
		});

		if (!rxBluetooth.isBluetoothAvailable()) {
			// handle the lack of bluetooth support
			Log.d(TAG, "Bluetooth is not supported!");
		} else {
			// check if bluetooth is currently enabled and ready for use
			if (!rxBluetooth.isBluetoothEnabled()) {
				// to enable bluetooth via startActivityForResult()
				Log.d(TAG, "Enabling Bluetooth");
				rxBluetooth.enableBluetooth(this, REQUEST_ENABLE_BT);
			} else {
				// you are ready
				Log.d(TAG, "onCreate: ready");
				// TODO: 1/10/18 enable all bluetooth related buttons
			}
		}

		perform.setOnClickListener(view -> {

			Log.d(TAG, "onCreate: SEND DATA");
			Log.d(TAG, "onCreate: bt socket: " + btSocket);
			Log.d(TAG, "onCreate: connection: " + connection);

			String textToSend = "Send example data to device!";
			if (connection != null) {
				connection.send(textToSend);
			}
		});

		compositeDisposable.add(rxBluetooth.observeDevices()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.subscribe(bluetoothDevice -> {
					Log.d(TAG, "observeDevices EVENT: Device found: " + bluetoothDevice.getAddress() + " - " + bluetoothDevice.getName());

					String deviceHardwareAddress = bluetoothDevice.getAddress(); // MAC address

					list.add(deviceHardwareAddress);
					adapter.notifyDataSetChanged();
				}));

		compositeDisposable.add(rxBluetooth.observeDiscovery()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
				.subscribe(action -> {
					//start.setText(R.string.button_searching);
					Log.d(TAG, "observerDiscovery EVENT: discovery started");
				}));

		compositeDisposable.add(rxBluetooth.observeDiscovery()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
				.subscribe(action -> {
					//start.setText(R.string.button_restart);
					Log.d(TAG, "observeDiscovery EVENT: discovery finished");
				}));

		compositeDisposable.add(rxBluetooth.observeBluetoothState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.STATE_ON))
				.subscribe(integer -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorActive));
					Log.d(TAG, "observeBluetoothState EVENT: state on");
				}));

		compositeDisposable.add(rxBluetooth.observeConnectionState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.STATE_CONNECTED, BluetoothAdapter.STATE_DISCONNECTED))
				.subscribe(event -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorInactive));
					Log.d(TAG, "observeBondState EVENT: connection: " + event.toString());

				}));

		compositeDisposable.add(rxBluetooth.observeBondState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
				.subscribe(event -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorInactive));
					Log.d(TAG, "observeBondState EVENT: bonded to: " + event.toString());

				}));

		compositeDisposable.add(rxBluetooth.observeBluetoothState()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.filter(BtPredicate.in(BluetoothAdapter.STATE_OFF, BluetoothAdapter.STATE_TURNING_OFF,
						BluetoothAdapter.STATE_TURNING_ON))
				.subscribe(state -> {
					//start.setBackgroundColor(getResources().getColor(R.color.colorInactive));
					Log.d(TAG, "observeBluetoothState EVENT: state " + state);

				}));

		getBondedDevices();

	}

	private BluetoothSocket createBluetoothSocket2(BluetoothDevice device) throws IOException {
		UUID uuid = device.getUuids()[0].getUuid();
		try {
			final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
			return (BluetoothSocket) m.invoke(device, uuid);
		} catch (Exception e) {
			Log.e(TAG, "Could not create Insecure RFComm Connection", e);
		}
		return device.createRfcommSocketToServiceRecord(uuid);
	}

	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
		ParcelUuid[] uuids = device.getUuids();
		Log.d(TAG, "createBluetoothSocket: get size: " + uuids.length);
		Log.d(TAG, "createBluetoothSocket: get uuid: " + uuids[0].getUuid());
		return device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
		//creates secure outgoing connecetion with BT device using UUID
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_COARSE_LOCATION) {
			for (String permission : permissions) {
				if (android.Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
					// Start discovery if permission granted
					rxBluetooth.startDiscovery();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (rxBluetooth != null) {
			// Make sure we're not doing discovery anymore
			rxBluetooth.cancelDiscovery();
		}
		compositeDisposable.dispose();
	}

	private void addDevice(BluetoothDevice device) {
		Log.d(TAG, "addDevice: add device: " + device.getName() + " - " + device.getAddress());
		devices.add(device);
		//setAdapter(devices);
	}

	public void getBondedDevices() {
		bondedList.clear();
		bondedNumber.setText(String.valueOf(rxBluetooth.getBondedDevices().size()));

		for (BluetoothDevice device : rxBluetooth.getBondedDevices()) {
			//bondedList.add(device.getName() + " - " + device.getAddress() + " - " + device.getBondState());
			bondedList.add(device.getAddress());
		}
		bondedAdapter.notifyDataSetChanged();
	}
}

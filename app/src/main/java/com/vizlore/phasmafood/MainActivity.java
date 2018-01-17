package com.vizlore.phasmafood;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vizlore.phasmafood.utils.Utils;
import com.vizlore.phasmafood.viewmodel.BluetoothViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

	private BluetoothViewModel bluetoothViewModel;

	private List<String> list = new ArrayList<>();
	private ArrayAdapter adapter;

	private List<String> bondedList = new ArrayList<>();
	private ArrayAdapter bondedAdapter;

	private List<BluetoothDevice> devices = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
		foundListView.setAdapter(adapter);

		bondedAdapter = new ArrayAdapter<>(this, R.layout.list_item, bondedList);
		bondedListView.setAdapter(bondedAdapter);

		UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

		// bluetooth view model
		bluetoothViewModel = ViewModelProviders.of(this).get(BluetoothViewModel.class);

		bluetoothViewModel.checkBluetooth().observe(this, shouldEnable -> {
			if (shouldEnable) {
				bluetoothViewModel.getRxBluetooth().enableBluetooth(this, REQUEST_ENABLE_BT);
			} else {
				//already enabled
			}
		});

		// bond to device on click
		foundListView.setOnItemClickListener((adapterView, view, i, l) -> {
			Log.d(TAG, "onCreate: bond to: " + list.get(i));
			bluetoothViewModel.createBond(list.get(i));
		});

		// connect to device
		bondedListView.setOnItemClickListener((adapterView, view, i, l) -> {
			bluetoothViewModel.connectToDevice(bondedList.get(i));

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
				bluetoothViewModel.startDiscovery();
			}
		});

		cancelDiscovery.setOnClickListener(v -> {
			Log.d(TAG, "onClick: cancel discovery");
			bluetoothViewModel.cancelDiscovery();
		});

		perform.setOnClickListener(view -> {
			bluetoothViewModel.sendData("Example data to be sent!");
		});

		bluetoothViewModel.getFoundDevices().observe(this, bluetoothDevice -> {
			if (bluetoothDevice != null) {
				list.add(bluetoothDevice.getAddress()); // MAC address
				adapter.notifyDataSetChanged();
			}
		});

		bluetoothViewModel.getDiscoveryEvents().observe(this, discoveryEvent -> Log.d(TAG, "onChanged: discoveryEvent: " + discoveryEvent));

		bluetoothViewModel.getBluetoothState().observe(this, bluetoothState -> Log.d(TAG, "onChanged: bluetoothState: " + bluetoothState));

		bluetoothViewModel.getConnectionState().observe(this, connectionStateEvent -> Log.d(TAG, "onChanged: connectionStateEvent: " + connectionStateEvent.toString()));

		bluetoothViewModel.getBondState().observe(this, bondStateEvent -> Log.d(TAG, "onChanged: bondStateEvent: " + bondStateEvent.toString()));

		bluetoothViewModel.getBondedDevices().observe(this, bluetoothDevices -> {

			bondedList.clear();
			bondedNumber.setText(String.valueOf(bluetoothDevices.size()));

			for (BluetoothDevice device : bluetoothDevices) {
				//bondedList.add(device.getName() + " - " + device.getAddress() + " - " + device.getBondState());
				bondedList.add(device.getAddress());
			}
			bondedAdapter.notifyDataSetChanged();

			Utils.prettyPrintDevices("Bonded", bluetoothDevices);
		});

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_COARSE_LOCATION) {
			for (String permission : permissions) {
				if (android.Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
					// Start discovery if permission granted
					bluetoothViewModel.startDiscovery();
				}
			}
		}
	}

	private void addDevice(BluetoothDevice device) {
		Log.d(TAG, "addDevice: add device: " + device.getName() + " - " + device.getAddress());
		devices.add(device);
		//setAdapter(devices);
	}
}

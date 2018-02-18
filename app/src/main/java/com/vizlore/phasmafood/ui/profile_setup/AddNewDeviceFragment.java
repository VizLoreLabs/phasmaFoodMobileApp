package com.vizlore.phasmafood.ui.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.ui.adapters.DevicesAdapter;
import com.vizlore.phasmafood.ui.profile_setup.viewmodel.ProfileSetupViewModel;
import com.vizlore.phasmafood.viewmodel.BluetoothViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/27/18.
 */

public class AddNewDeviceFragment extends ProfileBaseFragment {

	private static final String TAG = "SMEDIC";

	private static final int REQUEST_ENABLE_BT = 1;

	private BluetoothViewModel bluetoothViewModel;
	private UserViewModel userViewModel;
	private ProfileSetupViewModel profileSetupViewModel;
	private List<BluetoothDevice> devicesList = new ArrayList<>();
	private DevicesAdapter devicesAdapter;

	@BindView(R.id.search)
	Button search;

	@BindView(R.id.devicesList)
	RecyclerView devicesRecyclerView;

	@OnClick({R.id.search, R.id.backButton})
	void onClick(View view) {
		switch (view.getId()) {
			case R.id.search:
				// TODO: 1/27/18
				if (bluetoothViewModel.isDiscovering()) {
					bluetoothViewModel.cancelDiscovery();
				} else {
					bluetoothViewModel.startDiscovery();
					devicesList.clear();
					devicesAdapter.notifyDataSetChanged();
				}
				break;
			case R.id.backButton:
				profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
				break;
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		profileSetupViewModel = ViewModelProviders.of(getActivity()).get(ProfileSetupViewModel.class);
		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

		devicesAdapter = new DevicesAdapter(DevicesAdapter.AdapterListType.AVAILABLE_DEVICES, devicesList);
		devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		devicesRecyclerView.setAdapter(devicesAdapter);

		devicesAdapter.getClickEventObservable().observe(this, actionPair -> {
			if (actionPair.second == DevicesAdapter.AdapterAction.ACTION_PAIR) {
				BluetoothDevice device = devicesList.get(actionPair.first);
				Log.d(TAG, "onViewCreated: bond to: " + device.getAddress());
				bluetoothViewModel.createBond(device.getAddress());
			} else if (actionPair.second == DevicesAdapter.AdapterAction.ACTION_CONFIG) {
				// TODO: 1/28/18
			}
		});

		bluetoothViewModel = ViewModelProviders.of(getActivity()).get(BluetoothViewModel.class);

		bluetoothViewModel.checkBluetooth().observe(this, shouldEnable -> {
			if (shouldEnable) {
				bluetoothViewModel.getRxBluetooth().enableBluetooth(getActivity(), REQUEST_ENABLE_BT);
			} else {
				//already enabled
			}
		});

		bluetoothViewModel.getDiscoveryEvents().observe(this, discoveryEvent -> {
			Log.d(TAG, "onChanged: discoveryEvent: " + discoveryEvent);
			if (discoveryEvent.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
				search.setText("Stop search");
			} else if (discoveryEvent.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				search.setText("Start search");
			}
		});

		bluetoothViewModel.getFoundDevices().observe(this, bluetoothDevice -> {
			//if (!devicesList.contains(bluetoothDevice)) {
				devicesList.add(bluetoothDevice);
				devicesAdapter.notifyDataSetChanged();
			//}
		});

		bluetoothViewModel.getBondState().observe(this, bondStateEvent -> {
			Log.d(TAG, "onChanged: bondStateEvent: " + bondStateEvent.toString());
		});

		bluetoothViewModel.startDiscovery();
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_add_new_device;
	}
}

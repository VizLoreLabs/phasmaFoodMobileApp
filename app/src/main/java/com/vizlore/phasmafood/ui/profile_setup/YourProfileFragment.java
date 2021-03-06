package com.vizlore.phasmafood.ui.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
 * Created by smedic on 1/17/18.
 */

public class YourProfileFragment extends ProfileBaseFragment {

	private static final String TAG = "SMEDIC";

	private UserViewModel userViewModel;
	private ProfileSetupViewModel profileSetupViewModel;
	private List<BluetoothDevice> devicesList = new ArrayList<>();

	private OnConnect onConnectListener;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof ProfileSetupActivity) {
			onConnectListener = (ProfileSetupActivity) context;
		}
	}

	@BindView(R.id.devicesList)
	RecyclerView devicesRecyclerView;

	@OnClick({R.id.editProfile, R.id.addNewDevice, R.id.backButton, R.id.logOut})
	void onClick(View view) {
		switch (view.getId()) {
			case R.id.editProfile:
				profileSetupViewModel.setSelected(ProfileAction.EDIT_PROFILE);
				break;
			case R.id.addNewDevice:
				profileSetupViewModel.setSelected(ProfileAction.ADD_NEW_DEVICE_CLICKED);
				break;
			case R.id.backButton:
				profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
				break;
			case R.id.logOut:
				userViewModel.signOut();
				profileSetupViewModel.setSelected(ProfileAction.SIGN_OUT_CLICKED);
				break;
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		profileSetupViewModel = ViewModelProviders.of(getActivity()).get(ProfileSetupViewModel.class);
		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

		final DevicesAdapter devicesAdapter = new DevicesAdapter(devicesList);
		devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		devicesRecyclerView.setAdapter(devicesAdapter);

		final BluetoothViewModel bluetoothViewModel = ViewModelProviders.of(getActivity()).get(BluetoothViewModel.class);
		bluetoothViewModel.getBondedDevices().observe(this, devices -> {
			devicesList.clear();
			devicesList.addAll(devices);
		});

		devicesAdapter.getClickEventObservable().observe(this, actionPair -> {
			if (actionPair.second == DevicesAdapter.AdapterAction.ACTION_PAIR) {
				BluetoothDevice device = devicesList.get(actionPair.first);
				Log.d(TAG, "onViewCreated: connect to: " + device.getAddress());
				onConnectListener.onConnectClick(device);
			} else if (actionPair.second == DevicesAdapter.AdapterAction.ACTION_CONFIG) {
				Log.d(TAG, "onViewCreated: disconnect");
				onConnectListener.onDisconnectClick();
			}
		});
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_your_profile;
	}

	public interface OnConnect {
		void onConnectClick(BluetoothDevice device);

		void onDisconnectClick();
	}
}
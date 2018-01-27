package com.vizlore.phasmafood.ui.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
	private DevicesAdapter devicesAdapter;

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
				userViewModel.signOut().observe(this, signedOut -> {
					if (signedOut != null && signedOut) {
						profileSetupViewModel.setSelected(ProfileAction.SIGN_OUT_CLICKED);
						// TODO: 1/18/18 show dialog
					} else {
						Toast.makeText(getContext(), getString(R.string.signingOutError), Toast.LENGTH_SHORT).show();
					}
				});
				break;
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		profileSetupViewModel = ViewModelProviders.of(getActivity()).get(ProfileSetupViewModel.class);
		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

		devicesAdapter = new DevicesAdapter(devicesList);
		devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		devicesRecyclerView.setAdapter(devicesAdapter);

		BluetoothViewModel bluetoothViewModel = ViewModelProviders.of(getActivity()).get(BluetoothViewModel.class);
		bluetoothViewModel.getBondedDevices().observe(this, devices -> {
			devicesList.clear();
			devicesList.addAll(devices);
		});

	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_your_profile;
	}
}
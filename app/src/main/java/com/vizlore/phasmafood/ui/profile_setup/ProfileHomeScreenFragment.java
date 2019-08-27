package com.vizlore.phasmafood.ui.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.utils.ConnectivityChecker;
import com.vizlore.phasmafood.viewmodel.FcmMobileViewModel;

import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class ProfileHomeScreenFragment extends ProfileBaseFragment {

	private FcmMobileViewModel configViewModel;

//	@BindView(R.id.measurementHistory)
//	Button measurementHistory;

	@OnClick({R.id.startMeasurement, R.id.yourProfile})
	void onClick(Button button) {
		switch (button.getId()) {
			case R.id.startMeasurement:
				if (!ConnectivityChecker.isBluetoothEnabled(getActivity())) {
					Toast.makeText(getActivity(), getString(R.string.bluetoothNotEnabled), Toast.LENGTH_SHORT).show();
					return;
				}
				profileSetupViewModel.setSelected(ProfileAction.START_MEASUREMENT_CLICKED);
				break;
//			case R.id.measurementHistory:
//				if (!ConnectivityChecker.isNetworkEnabled(getActivity())) {
//					Toast.makeText(getActivity(), getString(R.string.networkNotEnabled), Toast.LENGTH_SHORT).show();
//					return;
//				}
//				profileSetupViewModel.setSelected(ProfileAction.MEASUREMENT_HISTORY_CLICKED);
//				break;
			case R.id.yourProfile:
				if (!ConnectivityChecker.isNetworkEnabled(getActivity())) {
					Toast.makeText(getActivity(), getString(R.string.networkNotEnabled), Toast.LENGTH_SHORT).show();
					return;
				}
				profileSetupViewModel.setSelected(ProfileAction.YOUR_PROFILE_CLICKED);
				break;
		}
	}

	@OnClick(R.id.learnMore)
	void onLearnMoreClicked() {
		if (!ConnectivityChecker.isNetworkEnabled(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.networkNotEnabled), Toast.LENGTH_SHORT).show();
			return;
		}
		profileSetupViewModel.setSelected(ProfileAction.LEARN_MORE_CLICKED);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		configViewModel = ViewModelProviders.of(getActivity()).get(FcmMobileViewModel.class);

		configViewModel.sendFcmToken().observe(this, sendResult -> {
			if (!sendResult) {
				Toast.makeText(getActivity(), "Token saved: " + sendResult, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//measurementHistory.setEnabled(false);
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_home_screen;
	}
}

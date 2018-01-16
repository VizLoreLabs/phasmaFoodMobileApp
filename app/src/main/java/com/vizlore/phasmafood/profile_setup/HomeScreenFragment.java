package com.vizlore.phasmafood.profile_setup;

import android.widget.Button;

import com.vizlore.phasmafood.R;

import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class HomeScreenFragment extends ProfileBaseFragment {

	@OnClick({R.id.startMeasurement, R.id.measurementHistory, R.id.yourProfile})
	void onClick(Button button) {
		switch (button.getId()) {
			case R.id.startMeasurement:
				profileSetupViewModel.setSelected(ProfileAction.START_MEASUREMENT_CLICKED);
				break;
			case R.id.measurementHistory:
				profileSetupViewModel.setSelected(ProfileAction.MEASUREMENT_HISTORY_CLICKED);
				break;
			case R.id.yourProfile:
				profileSetupViewModel.setSelected(ProfileAction.YOUR_PROFILE_CLICKED);
				break;
		}
	}

	@OnClick(R.id.back)
	void onBackClick() {
		profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
	}

	@OnClick(R.id.learnMore)
	void onLearnMoreClicked() {
		profileSetupViewModel.setSelected(ProfileAction.LEARN_MORE_CLICKED);
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_home_screen;
	}
}

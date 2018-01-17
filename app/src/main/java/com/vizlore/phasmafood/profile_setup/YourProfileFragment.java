package com.vizlore.phasmafood.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.profile_setup.viewmodel.ProfileSetupViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import butterknife.OnClick;

/**
 * Created by smedic on 1/17/18.
 */

public class YourProfileFragment extends ProfileBaseFragment {

	private UserViewModel userViewModel;
	private ProfileSetupViewModel profileSetupViewModel;

	private static final String TAG = "SMEDIC";

	@OnClick(R.id.logOut)
	void onLogInClicked() {
		userViewModel.signOut().observe(this, signedOut -> {
			if (signedOut != null && signedOut) {
				Log.d(TAG, "onLogInClicked: log out");
				profileSetupViewModel.setSelected(ProfileAction.SIGN_OUT_CLICKED);
			} else {
				Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_your_profile;
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		profileSetupViewModel = ViewModelProviders.of(getActivity()).get(ProfileSetupViewModel.class);
		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
	}
}
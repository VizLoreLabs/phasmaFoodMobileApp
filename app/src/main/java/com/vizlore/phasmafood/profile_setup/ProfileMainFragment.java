package com.vizlore.phasmafood.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class ProfileMainFragment extends ProfileBaseFragment {

	private UserViewModel userViewModel;

	private static final String TAG = "SMEDIC";

	@BindView(R.id.email)
	EditText email;

	@BindView(R.id.password)
	EditText password;

	@OnClick(R.id.logIn)
	void onLogInClicked() {
		Log.d(TAG, "onLogInClicked: ");
		// TODO: 1/16/18 add proper checks
		if (email.getText() != null && password.getText() != null) {
			userViewModel.signIn(email.getText().toString(), password.getText().toString()).observe(this,
					result -> {
						// TODO: 1/17/18 check if token is there
						profileSetupViewModel.setSelected(ProfileAction.SIGNED_IN);
					});
		}
	}

	@OnClick(R.id.forgotPassword)
	void onForgotPasswordClicked() {
		profileSetupViewModel.setSelected(ProfileAction.RECOVER_PASSWORD);
	}

	@OnClick(R.id.register)
	void onRegisterClicked() {
		profileSetupViewModel.setSelected(ProfileAction.CREATE_ACCOUNT_CLICKED);
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_profile_main;
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
	}
}

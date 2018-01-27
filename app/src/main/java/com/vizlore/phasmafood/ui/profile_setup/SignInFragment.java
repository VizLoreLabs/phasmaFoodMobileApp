package com.vizlore.phasmafood.ui.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.utils.Validator;
import com.vizlore.phasmafood.viewmodel.FcmMobileViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class SignInFragment extends ProfileBaseFragment {

	private UserViewModel userViewModel;
	private FcmMobileViewModel configViewModel;

	@BindView(R.id.email)
	EditText email;

	@BindView(R.id.password)
	EditText password;

	@OnClick(R.id.logIn)
	void onLogInClicked() {

		// TODO: 1/16/18 add proper checks
		if (Validator.validateFields(new EditText[]{email, password})) {

			final String emailText = email.getText().toString();
			final String passwordText = password.getText().toString();

			userViewModel.getToken(emailText, passwordText).observe(this, receivedToken -> {

				if (receivedToken != null) {
					configViewModel.readFcmToken().observe(this, isTokenValid -> {

						if (isTokenValid != null && isTokenValid) {
							profileSetupViewModel.setSelected(ProfileAction.SIGNED_IN);
						} else { //send FCM token to server
							configViewModel.sendFcmToken().observe(this, sendResult -> {
								if (sendResult != null && sendResult) {
									profileSetupViewModel.setSelected(ProfileAction.SIGNED_IN);
								} else {
									Toast.makeText(getContext(), getString(R.string.errorSendingToken), Toast.LENGTH_SHORT).show();
								}
							});
						}
					});
				} else {
					Toast.makeText(getContext(), getString(R.string.signingInError), Toast.LENGTH_SHORT).show();
				}
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

		configViewModel = ViewModelProviders.of(getActivity()).get(FcmMobileViewModel.class);

		// testing values
		email.setText("vanste25@gmail.com");
		password.setText("username123");
	}
}
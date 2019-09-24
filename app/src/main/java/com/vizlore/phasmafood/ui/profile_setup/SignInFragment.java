package com.vizlore.phasmafood.ui.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.utils.ConnectivityChecker;
import com.vizlore.phasmafood.utils.Resource;
import com.vizlore.phasmafood.utils.Validator;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class SignInFragment extends ProfileBaseFragment {

	private UserViewModel userViewModel;

	@BindView(R.id.email)
	EditText email;

	@BindView(R.id.password)
	EditText password;

	private static final String TAG = "SMEDIC";


	@OnClick(R.id.logIn)
	void onLogInClicked() {

		if (!ConnectivityChecker.isNetworkEnabled(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.networkNotEnabled), Toast.LENGTH_SHORT).show();
			return;
		}

		// TODO: 1/16/18 add proper checks
		if (Validator.validateFields(new EditText[]{email, password})) {

			final String emailText = email.getText().toString();
			final String passwordText = password.getText().toString();

			userViewModel.login(emailText, passwordText).observe(this, response -> {
				if (response.status == Resource.Status.SUCCESS) {
					profileSetupViewModel.setSelected(ProfileAction.SIGNED_IN);
				} else {
					Toast.makeText(getContext(), response.message, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@OnClick(R.id.forgotPassword)
	void onForgotPasswordClicked() {
		if (!ConnectivityChecker.isNetworkEnabled(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.networkNotEnabled), Toast.LENGTH_SHORT).show();
			return;
		}
		profileSetupViewModel.setSelected(ProfileAction.RECOVER_PASSWORD);
	}

	@OnClick(R.id.register)
	void onRegisterClicked() {
		if (!ConnectivityChecker.isNetworkEnabled(getActivity())) {
			Toast.makeText(getActivity(), getString(R.string.networkNotEnabled), Toast.LENGTH_SHORT).show();
			return;
		}
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

		// testing values
		email.setText("vanste25@gmail.com"); // TODO: 2019-08-27 Remove before uploading to Play
		password.setText("username123");
	}
}

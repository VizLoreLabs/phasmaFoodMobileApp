package com.vizlore.phasmafood.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class CreateAccountFragment extends ProfileBaseFragment {

	private UserViewModel userViewModel;

	private static final String TAG = "SMEDIC";

	@BindView(R.id.firstName)
	EditText firstNameEditText;
	@BindView(R.id.lastName)
	EditText lastNameEditText;
	@BindView(R.id.userName)
	EditText usernameEditText;
	@BindView(R.id.company)
	EditText companyEditText;
	@BindView(R.id.email)
	EditText emailEditText;
	@BindView(R.id.password)
	EditText passwordEditText;
	@BindView(R.id.confirmPassword)
	EditText confirmPasswordEditText;

	@OnClick(R.id.back)
	void onBackClicked() {
		profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
	}

	@OnClick(R.id.createAccount)
	void onCreateAccountClicked() {

		final String firstName = firstNameEditText.getText().toString();
		final String lastName = lastNameEditText.getText().toString();
		final String username = usernameEditText.getText().toString();
		final String company = companyEditText.getText().toString();
		final String email = emailEditText.getText().toString();
		final String password = passwordEditText.getText().toString();
		final String confirmPassword = confirmPasswordEditText.getText().toString();

		if (!username.isEmpty() && !company.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {

			if (!password.equals(confirmPassword)) {
				Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
				return;
			}
			userViewModel.createAccount(firstName, lastName, username, company, email, password)
					.observe(this, status -> Log.d(TAG, "onChanged: boolean: " + status));

		} else {
			Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_create_account;
	}
}

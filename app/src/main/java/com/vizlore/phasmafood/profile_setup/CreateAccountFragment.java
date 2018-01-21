package com.vizlore.phasmafood.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.utils.Validator;
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
	Spinner companySpinner;
	@BindView(R.id.email)
	EditText emailEditText;
	@BindView(R.id.password)
	EditText passwordEditText;
	@BindView(R.id.confirmPassword)
	EditText confirmPasswordEditText;

	@OnClick(R.id.backButton)
	void onBackClicked() {
		profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
	}

	@OnClick(R.id.createAccount)
	void onCreateAccountClicked() {

		if (Validator.validateFields(new EditText[]{firstNameEditText, lastNameEditText, usernameEditText,
			emailEditText, passwordEditText, confirmPasswordEditText})) {

			final String firstName = firstNameEditText.getText().toString();
			final String lastName = lastNameEditText.getText().toString();
			final String username = usernameEditText.getText().toString();
			final String company = companySpinner.getSelectedItem().toString();
			final String email = emailEditText.getText().toString();
			final String password = passwordEditText.getText().toString();
			final String confirmPassword = confirmPasswordEditText.getText().toString();

			if (!password.equals(confirmPassword)) { //check if passwords match
				Toast.makeText(getContext(), getString(R.string.passwordsDoNotMatch), Toast.LENGTH_SHORT).show();
				return;
			}
			userViewModel.createAccount(firstName, lastName, username, company, email, password)
				.observe(this, status -> {
					Toast.makeText(getContext(), getString(R.string.accountCreated), Toast.LENGTH_SHORT).show();
				});

		} else {
			Toast.makeText(getContext(), getString(R.string.fillAllFields), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

		// Creating adapter for spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.companies, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		companySpinner.setAdapter(adapter);
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_create_account;
	}

}

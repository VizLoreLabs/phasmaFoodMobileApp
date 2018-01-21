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
import com.vizlore.phasmafood.model.User;
import com.vizlore.phasmafood.utils.Validator;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/21/18.
 */

public class EditProfileFragment extends ProfileBaseFragment {

	private UserViewModel userViewModel;
	private User currentUser;

	@BindView(R.id.firstName)
	EditText firstName;

	@BindView(R.id.lastName)
	EditText lastName;

	@BindView(R.id.userName)
	EditText username;

	@BindView(R.id.company)
	Spinner company;

	@OnClick({R.id.backButton, R.id.save})
	void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
				break;
			case R.id.save:
				if (Validator.validateFields(new EditText[]{firstName, lastName, username})) {

					// create new user
					User user = User.builder()
						.company(company.getSelectedItem().toString())
						.firstName(firstName.getText().toString())
						.lastName(lastName.getText().toString())
						.username(username.getText().toString())
						.id(currentUser.id())
						.build();

					if (!user.equals(currentUser)) {
						userViewModel.updateProfile(user).observe(this, isSaved -> {
							if (isSaved != null && isSaved) {
								Toast.makeText(getContext(), getString(R.string.profileSaved), Toast.LENGTH_SHORT).show();
								profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
							} else {
								Toast.makeText(getContext(), getString(R.string.profileSavingError), Toast.LENGTH_SHORT).show();
							}
						});
					}
				} else {
					Toast.makeText(getContext(), getString(R.string.fillAllFields), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

		// Creating adapter for spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.companies, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		company.setAdapter(adapter);

		userViewModel.getUserProfile().observe(this, user -> {
			if (user != null) {
				firstName.setText(user.firstName());
				lastName.setText(user.lastName());
				username.setText(user.username());
				company.setSelection(getCurrentCompanyPosition(user.company()));

				//save user for later check if anything is changed
				currentUser = user;
			}
		});
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_edit_profile;
	}

	private int getCurrentCompanyPosition(String currentCompany) {
		String[] companies = getResources().getStringArray(R.array.companies);
		if (Arrays.asList(companies).contains(currentCompany)) {
			for (int i = 0; i < companies.length; i++) {
				if (companies[i].equals(currentCompany)) {
					return i;
				}
			}
		}
		return 0;
	}

}

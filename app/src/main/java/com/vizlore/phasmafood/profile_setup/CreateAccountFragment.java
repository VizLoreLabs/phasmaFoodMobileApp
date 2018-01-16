package com.vizlore.phasmafood.profile_setup;

import android.widget.EditText;

import com.vizlore.phasmafood.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by smedic on 1/16/18.
 */

public class CreateAccountFragment extends ProfileBaseFragment {

	@BindView(R.id.email)
	EditText email;

	@BindView(R.id.password)
	EditText password;

	@BindView(R.id.confirmPassword)
	EditText confirmPassword;

	@OnClick(R.id.back)
	void onBackClicked() {
		profileSetupViewModel.setSelected(ProfileAction.GO_BACK);
	}

	@OnClick(R.id.createAccount)
	void onCreateAccountClicked() {
		//// TODO: 1/16/18
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_create_account;
	}
}

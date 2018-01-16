package com.vizlore.phasmafood.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.analysis_wizard.WizardActivity;
import com.vizlore.phasmafood.profile_setup.viewmodel.ProfileSetupViewModel;

import butterknife.ButterKnife;

/**
 * Created by smedic on 1/16/18.
 */

public class ProfileSetupActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_setup);
		ButterKnife.bind(this);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.grey));

		ProfileSetupViewModel profileSetupViewModel = ViewModelProviders.of(this).get(ProfileSetupViewModel.class);
		profileSetupViewModel.getSelectedEvent().observe(this, actionSelection -> {
			if (actionSelection != null) {
				switch (actionSelection) {
					case SIGNED_IN:
						replaceFragment(new HomeScreenFragment());
						break;
					case RECOVER_PASSWORD:
						// TODO: 1/16/18
						break;
					case CREATE_ACCOUNT_CLICKED:
						replaceFragment(new CreateAccountFragment());
						break;
					case START_MEASUREMENT_CLICKED:
						// TODO: 1/16/18
						startActivity(new Intent(this, WizardActivity.class));
						break;
					case MEASUREMENT_HISTORY_CLICKED:
						// TODO: 1/16/18  
						break;
					case YOUR_PROFILE_CLICKED:
						// TODO: 1/16/18
						break;
					case LEARN_MORE_CLICKED:
						// TODO: 1/16/18
						break;
					case GO_BACK:
						if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
							getSupportFragmentManager().popBackStack();
						} else {
							finish();
						}
						break;
					default:
						break;
				}
			}
		});

		addFragment(new ProfileMainFragment());
	}

	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.add(R.id.fragmentContainer, fragment);
		fragmentTransaction.commit();

	}

	public void replaceFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left,
				R.anim.slide_from_left, R.anim.slide_to_right);
		transaction.addToBackStack(null);
		transaction.replace(R.id.fragmentContainer, fragment);
		transaction.commit();
	}
}

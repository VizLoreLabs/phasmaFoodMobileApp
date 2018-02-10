package com.vizlore.phasmafood.ui.profile_setup;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.ui.BaseActivity;
import com.vizlore.phasmafood.ui.profile_setup.viewmodel.ProfileSetupViewModel;
import com.vizlore.phasmafood.ui.wizard.WizardActivity;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import butterknife.ButterKnife;

/**
 * Created by smedic on 1/16/18.
 */

public class ProfileSetupActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";
	private static final int REQUEST_PERMISSION_COARSE_LOCATION = 0;

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

						if (ContextCompat.checkSelfPermission(this,
							android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							ActivityCompat.requestPermissions(this,
								new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
								REQUEST_PERMISSION_COARSE_LOCATION);
						} else {
							replaceBaseFragment2(new ProfileHomeScreenFragment());
						}
						break;
					case RECOVER_PASSWORD:
						// TODO: 1/16/18
						break;
					case CREATE_ACCOUNT_CLICKED:
						replaceFragment(new CreateAccountFragment());
						break;
					case START_MEASUREMENT_CLICKED:
						startActivity(new Intent(this, WizardActivity.class));
						break;
					case MEASUREMENT_HISTORY_CLICKED:
						// TODO: 1/16/18  
						break;
					case YOUR_PROFILE_CLICKED:
						replaceFragment(new YourProfileFragment());
						break;
					case LEARN_MORE_CLICKED:
						// TODO: 1/16/18
						break;
					case EDIT_PROFILE:
						replaceFragment(new EditProfileFragment());
						break;
					case SIGN_OUT_CLICKED:
						clearBackstack();
						replaceBaseFragment(new SignInFragment());
						break;
					case ADD_NEW_DEVICE_CLICKED:
						replaceFragment(new AddNewDeviceFragment());
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
		if (isLocationPermissionEnabled()) {
			checkUserStatus();
		}
	}

	public void checkUserStatus() {
		UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
		if (userViewModel.hasSession()) {
			addFragment(new ProfileHomeScreenFragment());
		} else {
			addFragment(new SignInFragment());
		}
	}

	public boolean isLocationPermissionEnabled() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// Request the permission.
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_COARSE_LOCATION);
			return false;
		} else {
			return true;
		}
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

	public void replaceBaseFragment2(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left,
			R.anim.slide_from_left, R.anim.slide_to_right);
		transaction.replace(R.id.fragmentContainer, fragment);
		transaction.commit();
	}

	public void replaceBaseFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right,
			R.anim.slide_from_right, R.anim.slide_to_left);
		transaction.replace(R.id.fragmentContainer, fragment);
		transaction.commit();
	}

	public void clearBackstack() {
		while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStackImmediate();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_COARSE_LOCATION) {
			for (String permission : permissions) {
				if (android.Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
					checkUserStatus();
				}
			}
		}
	}
}

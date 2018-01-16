package com.vizlore.phasmafood.profile_setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vizlore.phasmafood.R;

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

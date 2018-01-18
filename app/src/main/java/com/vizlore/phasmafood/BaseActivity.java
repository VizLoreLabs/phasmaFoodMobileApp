package com.vizlore.phasmafood;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * Created by smedic on 1/18/18.
 */

public abstract class BaseActivity extends FragmentActivity {

	@Override
	public void finish() {
		super.finish();
		overridePendingTransitionExit();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransitionEnter();
	}

	/**
	 * Overrides the pending Activity transition by performing the "Enter" animation.
	 */
	protected void overridePendingTransitionEnter() {
		overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
	}

	/**
	 * Overrides the pending Activity transition by performing the "Exit" animation.
	 */
	protected void overridePendingTransitionExit() {
		overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
	}
}

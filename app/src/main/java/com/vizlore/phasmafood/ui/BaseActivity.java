package com.vizlore.phasmafood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vizlore.phasmafood.R;

import butterknife.ButterKnife;

/**
 * Created by smedic on 1/18/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

	public abstract int getLayoutId();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		ButterKnife.bind(this);
	}

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

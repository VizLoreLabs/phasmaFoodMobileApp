package com.vizlore.phasmafood;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 1/15/18.
 */

public class WizardActivity extends FragmentActivity {

	@BindView(R.id.step1)
	ImageView firstStep;

	@BindView(R.id.step2)
	ImageView secondStep;

	@BindView(R.id.step3)
	ImageView thirdStep;

	@BindView(R.id.step4)
	ImageView fourthStep;

	@BindView(R.id.step5)
	ImageView fifthStep;

	@BindView(R.id.pb)
	ProgressBar percentage;

	private int stepSelected = 0;
	private View animatedView = null;

	@OnClick({R.id.step1, R.id.step2, R.id.step3, R.id.step4, R.id.step5})
	void onClick(View v) {
		switch (v.getId()) {
			case R.id.step1:
				percentage.setProgress(100 / 6);
				stepSelected = R.id.step1;
				break;
			case R.id.step2:
				percentage.setProgress(200 / 6);
				stepSelected = R.id.step2;
				break;
			case R.id.step3:
				percentage.setProgress(300 / 6);
				stepSelected = R.id.step3;
				break;
			case R.id.step4:
				percentage.setProgress(400 / 6);
				stepSelected = R.id.step4;
				break;
			case R.id.step5:
				percentage.setProgress(500 / 6);
				stepSelected = R.id.step5;
				break;
		}
		scaleUp(v);
	}

	private void scaleUp(View v) {
		scaleDown();
		v.animate().scaleX(1.4f).scaleY(1.4f).setDuration(200).start();
		animatedView = v;
	}

	private void scaleDown() {
		if (animatedView != null) {
			animatedView.animate().scaleX(1.0f).scaleY(1.0f).start();
		}
	}

	@OnClick(R.id.button_1)
	void onButtonClick() {
		if (stepSelected != 0) {
			((ImageView) findViewById(stepSelected)).setImageResource(R.drawable.ic_checkmark);
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wizard);
		ButterKnife.bind(this);

		// clear FLAG_TRANSLUCENT_STATUS flag:
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		// finally change the color
		getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.greyHeader));

	}
}

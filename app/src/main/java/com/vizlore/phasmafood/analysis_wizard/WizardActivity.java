package com.vizlore.phasmafood.analysis_wizard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vizlore.phasmafood.BaseActivity;
import com.vizlore.phasmafood.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 1/15/18.
 */

public class WizardActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";

	private int[] analysisType = {R.string.selectTypeOfAnalysis,
		R.string.selectTypeOfFood,
		R.string.specifyScanningConditions,
		R.string.scanningParamsSummary};

	@BindView(R.id.title)
	TextView title;

	private int currentStep = 0;
	private View animatedView = null;

	@OnClick(R.id.backButton)
	void onCloseClicked() {
		finish();
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

	@OnClick(R.id.nextButton)
	void onNextButtonClick() {
		((ImageView) findViewById(stepToViewId(currentStep))).setImageResource(R.drawable.ic_checkmark);
		if (currentStep + 1 <= 5) {
			if (currentStep == 0) {
				replaceFragment(new FragmentSecondStep());
				currentStep++;
				setTitle();
				setIndicator();
			} else if (currentStep == 1) {
				replaceFragment(new FragmentThirdStep());
				currentStep++;
				setTitle();
				setIndicator();
			} else if (currentStep == 2) {
				replaceFragment(new FragmentSummary());
				currentStep++;
				setTitle();
				setIndicator();

				((Button) findViewById(R.id.nextButton)).setText("START ANALYSIS");
			}
		}
	}

	@OnClick(R.id.previousButton)
	void onPrevButtonClick() {
		if (currentStep > 0) {
			getSupportFragmentManager().popBackStack();
			currentStep--;
			setTitle();
			setIndicator();

			((Button) findViewById(R.id.nextButton)).setText("NEXT");
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wizard);
		ButterKnife.bind(this);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.grey));

		addFragment(new FragmentFirstStep());
		setTitle();
		setIndicator();
	}

	private void setTitle() {
		title.setText(analysisType[currentStep]);
	}

	private void setIndicator() {
		scaleUp(findViewById(stepToViewId(currentStep)));
	}

	private int stepToViewId(int step) {
		switch (step) {
			case 0:
				return R.id.step1;
			case 1:
				return R.id.step2;
			case 2:
				return R.id.step3;
			case 3:
				return R.id.step4;
			default:
				throw new IllegalArgumentException("Wrong step provided");
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

}

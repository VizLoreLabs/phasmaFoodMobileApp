package com.vizlore.phasmafood;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 1/15/18.
 */

public class WizardActivity extends FragmentActivity {

	private static final String TAG = "SMEDIC";

	private int[] analysisType = {R.string.selectTypeOfAnalysis,
			R.string.selectTypeOfFood,
			R.string.specifyScanningConditions,
			R.string.measurementGuide,
			R.string.analysisResults};

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

	@BindView(R.id.viewPager)
	ViewPager viewPager;

	@BindView(R.id.step)
	TextView stepNumber;

	@BindView(R.id.title)
	TextView title;

	private int currentStep = 1;
	private View animatedView = null;

	private FragmentPagerAdapter adapterViewPager;

	@OnClick({R.id.step1, R.id.step2, R.id.step3, R.id.step4, R.id.step5})
	void onClick(ImageView v) {

		int step = viewIdToStep(v.getId());
		scaleUp(v);
		currentStep = step;
		setStepsProgress(step);
		viewPager.setCurrentItem(step - 1);

		stepNumber.setText("STEP " + step);
		title.setText(analysisType[step - 1]);
	}

	private void setStepsProgress(int step) {
		int newValue = (step * 100) / 6;
		percentage.setProgress(newValue);
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
			onClick(findViewById(stepToViewId(currentStep + 1)));
		}
	}

	@OnClick(R.id.previousButton)
	void onPrevButtonClick() {
		if (currentStep - 1 > 0) {
			onClick(findViewById(stepToViewId(currentStep - 1)));
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

		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapterViewPager);

		//set initial position
		onClick(firstStep);
	}

	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 5;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0: // Fragment # 0 - This will show FirstFragment
					return new FragmentFirstStep();
				case 1: // Fragment # 0 - This will show FirstFragment different title
					return new FragmentSecondStep();
				case 2: // Fragment # 1 - This will show SecondFragment
					return new FragmentThirdStep();
				case 3: // Fragment # 1 - This will show SecondFragment
					return new FragmentFourthStep();
				case 4: // Fragment # 1 - This will show SecondFragment
					return new FragmentFifthStep();
				default:
					return null;
			}
		}
	}

	private int viewIdToStep(final int viewId) {
		switch (viewId) {
			case R.id.step1:
				return 1;
			case R.id.step2:
				return 2;
			case R.id.step3:
				return 3;
			case R.id.step4:
				return 4;
			case R.id.step5:
				return 5;
			default:
				throw new IllegalArgumentException("Wrong view id provided!");
		}
	}

	private int stepToViewId(int step) {
		switch (step) {
			case 1:
				return R.id.step1;
			case 2:
				return R.id.step2;
			case 3:
				return R.id.step3;
			case 4:
				return R.id.step4;
			case 5:
				return R.id.step5;
			default:
				throw new IllegalArgumentException("Wrong step provided");
		}
	}
}

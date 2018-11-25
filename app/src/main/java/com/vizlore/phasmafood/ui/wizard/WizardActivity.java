package com.vizlore.phasmafood.ui.wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.ui.configuration.ConfigurationActivity;
import com.vizlore.phasmafood.utils.Constants;
import com.vizlore.phasmafood.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizardpager.model.AbstractWizardModel;
import wizardpager.model.ModelCallbacks;
import wizardpager.model.Page;
import wizardpager.model.ReviewItem;
import wizardpager.ui.PageFragmentCallbacks;
import wizardpager.ui.ReviewFragment;
import wizardpager.ui.StepPagerStrip;

public class WizardActivity extends AppCompatActivity implements PageFragmentCallbacks,
	ReviewFragment.Callbacks, ModelCallbacks {

	private static final String TAG = "SMEDIC";

	public static final String DEBUG_MODE_KEY = "debug_mode";

	private MyPagerAdapter pagerAdapter;
	private boolean editingAfterReview;
	private AbstractWizardModel wizardModel = new PhasmaFoodWizardModel(this);
	private boolean consumePageSelectedEvent;
	private List<Page> currentPageSequence;
	private boolean isRoot;

	@BindView(R.id.nextButton)
	Button nextButton;

	@BindView(R.id.previousButton)
	Button prevButton;

	@BindView(R.id.pager)
	ViewPager pager;

	@BindView(R.id.strip)
	StepPagerStrip stepPagerStrip;

	//for testing purposes
	@BindView(R.id.debugModeSwitch)
	Switch debugModeSwitch;

	@OnClick({R.id.backButton, R.id.nextButton, R.id.previousButton})
	void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.nextButton:
				if (pager.getCurrentItem() == currentPageSequence.size()) {
					sendToBluetoothDevice();
				} else {
					if (editingAfterReview) {
						pager.setCurrentItem(pagerAdapter.getCount() - 1);
					} else {
						pager.setCurrentItem(pager.getCurrentItem() + 1);
					}
				}
				break;
			case R.id.previousButton:
				pager.setCurrentItem(pager.getCurrentItem() - 1);
				break;
		}
	}

	/**
	 * Connect to bluetooth device and send data
	 */
	private void sendToBluetoothDevice() {
		if (pager.getAdapter() != null) {

			Object o = pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
			if (o instanceof ReviewFragment) {

				List<ReviewItem> items = ((ReviewFragment) o).getReviewItems();
				if (items != null) {
					JSONObject jsonObject = new JSONObject();
					for (ReviewItem item : items) {
						String title = Utils.removeMagicChar(item.getTitle());
						String value = Utils.removeMagicChar(item.getDisplayValue());
						Log.d(TAG, "Item: " + title + "\t" + value);
						try {
							jsonObject.put(title, value);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					final Intent intent = new Intent(this, ConfigurationActivity.class);
					intent.putExtra(Constants.WIZARD_DATA_KEY, jsonObject.toString());
					startActivity(intent);
				}
			}
		} else {
			throw new IllegalStateException("Pager adapter null");
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wizard);
		ButterKnife.bind(this);

		if (savedInstanceState != null) {
			wizardModel.load(savedInstanceState.getBundle("model"));
		}

		wizardModel.registerListener(this);

		pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		stepPagerStrip.setOnPageSelectedListener(position -> {
			position = Math.min(pagerAdapter.getCount() - 1, position);
			if (pager.getCurrentItem() != position) {
				pager.setCurrentItem(position);
			}
		});

		pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				stepPagerStrip.setCurrentPage(position);

				if (consumePageSelectedEvent) {
					consumePageSelectedEvent = false;
					return;
				}

				editingAfterReview = false;
				updateBottomBar();
			}
		});

		onPageTreeChanged();
		updateBottomBar();

		// just for testing (is debug mode or not)
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		debugModeSwitch.setChecked(prefs.getBoolean(DEBUG_MODE_KEY, false));

		debugModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) ->
			prefs.edit().putBoolean(DEBUG_MODE_KEY, isChecked).apply());
	}

	@Override
	public void onPageTreeChanged() {
		currentPageSequence = wizardModel.getCurrentPageSequence();
		recalculateCutOffPage();
		stepPagerStrip.setPageCount(currentPageSequence.size() + 1); // + 1 =
		// review
		// step
		pagerAdapter.notifyDataSetChanged();
		updateBottomBar();
	}

	private void updateBottomBar() {
		int position = pager.getCurrentItem();
		if (position == currentPageSequence.size()) {
			nextButton.setText(R.string.configureParams);
			//nextButton.setBackgroundColor(getResources().getColor(R.color.step_pager_selected_tab_color));
			nextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
		} else {
			nextButton.setText(editingAfterReview ? R.string.summary : R.string.next);
			//nextButton.setBackgroundResource(R.drawable.selectable_item_background);
			nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
		}

		prevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
		isRoot = position <= 0;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		wizardModel.unregisterListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("model", wizardModel.save());
	}

	@Override
	public AbstractWizardModel onGetModel() {
		return wizardModel;
	}

	@Override
	public void onEditScreenAfterReview(String key) {
		for (int i = currentPageSequence.size() - 1; i >= 0; i--) {
			if (currentPageSequence.get(i).getKey().equals(key)) {
				consumePageSelectedEvent = true;
				editingAfterReview = true;
				pager.setCurrentItem(i);
				updateBottomBar();
				break;
			}
		}
	}

	@Override
	public void onPageDataChanged(Page page) {
		if (page.isRequired()) {
			if (recalculateCutOffPage()) {
				pagerAdapter.notifyDataSetChanged();
				updateBottomBar();
			}
		}
	}

	@Override
	public Page onGetPage(String key) {
		return wizardModel.findByKey(key);
	}

	private boolean recalculateCutOffPage() {
		// Cut off the pager adapter at first required page that isn't completed
		int cutOffPage = currentPageSequence.size() + 1;
		for (int i = 0; i < currentPageSequence.size(); i++) {
			Page page = currentPageSequence.get(i);
			if (page.isRequired() && !page.isCompleted()) {
				cutOffPage = i;
				break;
			}
		}

		if (pagerAdapter.getCutOffPage() != cutOffPage) {
			pagerAdapter.setCutOffPage(cutOffPage);
			return true;
		}

		return false;
	}

	@Override
	public void onBackPressed() {
		if (!isRoot) {
			pager.setCurrentItem(pager.getCurrentItem() - 1);
		} else {
			finish();
		}
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		private int mCutOffPage;
		private Fragment mPrimaryItem;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			if (i >= currentPageSequence.size()) {
				return new ReviewFragment();
			}

			return currentPageSequence.get(i).createFragment();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO: be smarter about this
			if (object == mPrimaryItem) {
				// Re-use the current fragment (its position never changes)
				return POSITION_UNCHANGED;
			}

			return POSITION_NONE;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
								   Object object) {
			super.setPrimaryItem(container, position, object);
			mPrimaryItem = (Fragment) object;
		}

		@Override
		public int getCount() {
			return Math.min(mCutOffPage + 1, currentPageSequence == null ? 1
				: currentPageSequence.size() + 1);
		}

		public void setCutOffPage(int cutOffPage) {
			if (cutOffPage < 0) {
				cutOffPage = Integer.MAX_VALUE;
			}
			mCutOffPage = cutOffPage;
		}

		public int getCutOffPage() {
			return mCutOffPage;
		}
	}

}

package com.vizlore.phasmafood.ui.wizard;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.bluetooth.BluetoothService;
import com.vizlore.phasmafood.utils.Utils;

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

public class WizardActivity extends FragmentActivity implements PageFragmentCallbacks,
	ReviewFragment.Callbacks, ModelCallbacks {

	private static final String TAG = "SMEDIC";

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

	BluetoothService bluetoothService;

	@OnClick({R.id.backButton, R.id.nextButton, R.id.previousButton})
	void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.nextButton:
				if (pager.getCurrentItem() == currentPageSequence.size()) {
					AlertDialog alertDialog = new AlertDialog.Builder(WizardActivity.this).create();
					alertDialog.setMessage(getString(R.string.sendDataMessage));
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.yes), (dialog, which) -> {
						// TODO: 2/19/18 uncomment
						sendToBluetoothDevice();
						dialog.dismiss();
					});
					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.no), (dialog, which) -> dialog.dismiss());
					alertDialog.show();
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

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) iBinder;
			bluetoothService = binder.getServiceInstance(); //Get instance of your service!
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			bluetoothService = null;
		}
	};

	/**
	 * Connect to bluetooth device and send data
	 */
	private void sendToBluetoothDevice() {
		if (pager.getAdapter() != null) {

			Object o = pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
			if (o instanceof ReviewFragment) {

				List<ReviewItem> items = ((ReviewFragment) o).getReviewItems();
				if (items != null) {
					JsonObject jsonObject = new JsonObject();
					for (ReviewItem item : items) {
						String title = Utils.removeMagicChar(item.getTitle());
						String value = Utils.removeMagicChar(item.getDisplayValue());
						Log.d(TAG, "Item: " + title + "\t" + value);
						jsonObject.addProperty(title, value);
					}
					JsonObject jsonObjectRequest = new JsonObject();
					jsonObjectRequest.add("Request", jsonObject);
					bluetoothService.sendMessage(jsonObjectRequest.toString());
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

		Intent intent = new Intent(this, BluetoothService.class);
		//startService(intent); //Starting the service
		bindService(intent, connection, Context.BIND_AUTO_CREATE); //Binding to the service!

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
			nextButton.setText(R.string.finish);
			nextButton.setBackgroundColor(getResources().getColor(R.color.step_pager_selected_tab_color));
			nextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
		} else {
			nextButton.setText(editingAfterReview ? R.string.summary : R.string.next);
			nextButton.setBackgroundResource(R.drawable.selectable_item_background);
			nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
		}

		prevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
		isRoot = position <= 0;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		wizardModel.unregisterListener(this);

		//stopService(new Intent(this, BluetoothService.class));
		if (bluetoothService != null && connection != null) {
			bluetoothService.closeConnection();
		}
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

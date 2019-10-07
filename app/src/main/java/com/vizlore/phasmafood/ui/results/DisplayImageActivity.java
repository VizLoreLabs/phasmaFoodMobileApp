package com.vizlore.phasmafood.ui.results;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.helpers.BitmapHolder;
import com.vizlore.phasmafood.model.results.BitmapWrapper;
import com.vizlore.phasmafood.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class DisplayImageActivity extends BaseActivity {

	@BindView(R.id.image)
	ImageView image;

	@OnClick(R.id.backButton)
	void onClick() {
		finish();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_image_display;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final BitmapWrapper bitmapWrapper = BitmapHolder.getInstance().getBitmapWrapper();
		image.setImageBitmap(bitmapWrapper.getBitmap());
	}
}

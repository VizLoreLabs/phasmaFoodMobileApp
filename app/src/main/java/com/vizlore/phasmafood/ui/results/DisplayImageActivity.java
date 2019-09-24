package com.vizlore.phasmafood.ui.results;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.ui.BaseActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class DisplayImageActivity extends BaseActivity {

	public static final String IMAGE_PATH_EXTRA = "image_path";

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

		final String imagePath = getIntent().getStringExtra(IMAGE_PATH_EXTRA);
		Picasso.get().load(new File(imagePath)).into(image);
	}
}

package com.vizlore.phasmafood.model.results;

import android.graphics.Bitmap;

/**
 * @author Stevan Medic
 * <p>
 * Created on Oct 2019
 */
public class BitmapWrapper {

	private Bitmap bitmap;
	private String bitmapName;

	public BitmapWrapper(Bitmap bitmap, String bitmapName) {
		this.bitmap = bitmap;
		this.bitmapName = bitmapName;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getBitmapName() {
		return bitmapName;
	}

	public void setBitmapName(String bitmapName) {
		this.bitmapName = bitmapName;
	}
}

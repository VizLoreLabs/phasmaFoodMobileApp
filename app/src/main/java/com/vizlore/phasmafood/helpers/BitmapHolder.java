package com.vizlore.phasmafood.helpers;

import com.vizlore.phasmafood.model.results.BitmapWrapper;

/**
 * @author Stevan Medic
 * <p>
 * Created on Oct 2019
 */
public class BitmapHolder {

	private static final BitmapHolder instance = new BitmapHolder();

	private BitmapWrapper bitmapWrapper;

	private BitmapHolder() {
	}

	public static BitmapHolder getInstance() {
		return instance;
	}

	public BitmapWrapper getBitmapWrapper() {
		return bitmapWrapper;
	}

	public void setBitmapWrapper(BitmapWrapper bitmap) {
		this.bitmapWrapper = bitmap;
	}
}

package com.vizlore.phasmafood.api;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Created by smedic on 1/4/18.
 */

@GsonTypeAdapterFactory
public abstract class AutoValueGsonFactory implements TypeAdapterFactory {

	// Static factory method to access the package
	// private generated implementation
	public static TypeAdapterFactory create() {
		return new AutoValueGson_AutoValueGsonFactory();
	}
}
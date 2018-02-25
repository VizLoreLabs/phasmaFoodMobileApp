package com.vizlore.phasmafood.dagger.modules;

import com.vizlore.phasmafood.api.DeviceApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by smedic on 2/25/18.
 */

@Module
public class DeviceModule {

	@Provides
	@Singleton
	DeviceApi provideDeviceApi(Retrofit retrofit) {
		return retrofit.create(DeviceApi.class);
	}
}

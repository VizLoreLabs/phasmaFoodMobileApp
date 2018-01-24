package com.vizlore.phasmafood.dagger.modules;

import com.vizlore.phasmafood.api.FcmMobileApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by smedic on 1/23/18.
 */

@Module
public class MobileModule {

	@Provides
	@Singleton
	FcmMobileApi provideMobileApi(Retrofit retrofit) {
		return retrofit.create(FcmMobileApi.class);
	}

}

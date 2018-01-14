package com.vizlore.phasmafood.dagger.modules;

import com.vizlore.phasmafood.api.UserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by smedic on 1/4/18.
 */

@Module
public class UserModule {

	@Provides
	@Singleton
	UserApi provideUserApi(Retrofit retrofit) {
		return retrofit.create(UserApi.class);
	}

}
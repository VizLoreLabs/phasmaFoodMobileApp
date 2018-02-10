package com.vizlore.phasmafood.dagger.modules;

import com.vizlore.phasmafood.api.ExaminationApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by smedic on 2/10/18.
 */

@Module
public class ExaminationModule {

	@Provides
	@Singleton
	ExaminationApi provideExaminationApi(Retrofit retrofit) {
		return retrofit.create(ExaminationApi.class);
	}
}

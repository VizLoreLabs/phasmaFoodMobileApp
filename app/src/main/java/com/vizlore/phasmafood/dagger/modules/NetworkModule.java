package com.vizlore.phasmafood.dagger.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vizlore.phasmafood.Config.BASE_URL;

/**
 * Created by smedic on 1/4/18.
 */

@Module
public class NetworkModule {

	@Provides
	@Singleton
	Gson provideGson() {
		return new GsonBuilder().serializeNulls().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
	}

//	@Provides
//	@Singleton
//	OkHttpClient provideOkHttpClient() {
//		return new OkHttpClient.Builder()
//				//.addInterceptor(new RetrofitInterceptor()).build();
//				.build();
//	}

	@Provides
	@Singleton
	RxJava2CallAdapterFactory provideRxJava2CallAdapterFactory() {
		return RxJava2CallAdapterFactory.create();
	}

	@Provides
	@Singleton
	GsonConverterFactory provideGsonConverterFactory(Gson gson) {
		return GsonConverterFactory.create(gson);
	}

	@Provides
	@Singleton
	Retrofit provideRetrofit(RxJava2CallAdapterFactory rxJava2CallAdapterFactory, GsonConverterFactory factory) {
		return new Retrofit.Builder()
				.addCallAdapterFactory(rxJava2CallAdapterFactory)
				.addConverterFactory(factory)
				.baseUrl(BASE_URL)
				.build();
	}
}
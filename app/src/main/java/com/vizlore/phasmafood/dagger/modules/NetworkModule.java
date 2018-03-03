package com.vizlore.phasmafood.dagger.modules;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vizlore.phasmafood.utils.Config.BASE_URL;

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

	@Provides
	@Singleton
	HttpLoggingInterceptor provideHttpLoggingInterceptor() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("SMEDIC", "log: " + message));
		logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
		return logging;
	}

	@Provides
	@Singleton
	OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
		return new OkHttpClient.Builder()
			.writeTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.addInterceptor(loggingInterceptor)
			.build();
	}

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
	Retrofit provideRetrofit(RxJava2CallAdapterFactory rxJava2CallAdapterFactory, GsonConverterFactory factory,
							 OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
			.addCallAdapterFactory(rxJava2CallAdapterFactory)
			.addConverterFactory(factory)
			.client(okHttpClient)
			.baseUrl(BASE_URL)
			.build();
	}
}
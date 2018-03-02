package com.vizlore.phasmafood;

/**
 * Created by smedic on 1/4/18.
 */

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.vizlore.phasmafood.dagger.AppComponent;
import com.vizlore.phasmafood.dagger.DaggerAppComponent;
import com.vizlore.phasmafood.dagger.modules.AppModule;
import com.vizlore.phasmafood.dagger.modules.BluetoothModule;
import com.vizlore.phasmafood.dagger.modules.NetworkModule;
import com.vizlore.phasmafood.dagger.modules.UserModule;
import com.vizlore.phasmafood.model.results.Examination;

import timber.log.Timber;

/**
 * Main application class. Initializes dagger.
 */
public class MyApplication extends Application {

	private static AppComponent component;
	private static Context context;
	private static MyApplication instance = null;

	// temporary save examination
	private Examination examination;

	@Override
	public void onCreate() {
		super.onCreate();

		Timber.plant(new Timber.DebugTree());

		component = DaggerAppComponent.builder()
			.userModule(new UserModule())
			.appModule(new AppModule(this))
			.networkModule(new NetworkModule())
			.bluetoothModule(new BluetoothModule(this))
			.build();

		context = getApplicationContext();

		instance = this;
	}

	public static AppComponent getComponent() {
		return component;
	}

	public static Context getAppContext() {
		return context;
	}

	public static MyApplication getInstance() {
		return instance;
	}

	// temporary save examination
	public void saveExamination(@NonNull Examination examination) {
		this.examination = examination;
	}

	public Examination getExamination() {
		return examination;
	}
}
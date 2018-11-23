package com.vizlore.phasmafood;

/**
 * Created by smedic on 1/4/18.
 */

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.vizlore.phasmafood.dagger.AppComponent;
import com.vizlore.phasmafood.dagger.DaggerAppComponent;
import com.vizlore.phasmafood.dagger.modules.AppModule;
import com.vizlore.phasmafood.dagger.modules.BluetoothModule;
import com.vizlore.phasmafood.dagger.modules.NetworkModule;
import com.vizlore.phasmafood.dagger.modules.UserModule;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Main application class. Initializes Dagger and tools.
 */
public class MyApplication extends Application {

	private static AppComponent component;
	private static MyApplication instance = null;

	@Override
	public void onCreate() {
		super.onCreate();

		Fabric.with(this, new Crashlytics());
		Stetho.initializeWithDefaults(this);
		Timber.plant(new Timber.DebugTree());

		component = DaggerAppComponent.builder()
			.userModule(new UserModule())
			.appModule(new AppModule(this))
			.networkModule(new NetworkModule())
			.bluetoothModule(new BluetoothModule(this))
			.build();

		instance = this;
	}

	public static AppComponent getComponent() {
		return component;
	}

	public static MyApplication getInstance() {
		return instance;
	}
}
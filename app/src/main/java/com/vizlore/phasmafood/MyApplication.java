package com.vizlore.phasmafood;

/**
 * Created by smedic on 1/4/18.
 */

import android.app.Application;
import android.content.Context;

import com.vizlore.phasmafood.dagger.AppComponent;
import com.vizlore.phasmafood.dagger.DaggerAppComponent;
import com.vizlore.phasmafood.dagger.modules.AppModule;
import com.vizlore.phasmafood.dagger.modules.BluetoothModule;
import com.vizlore.phasmafood.dagger.modules.NetworkModule;
import com.vizlore.phasmafood.dagger.modules.UserModule;

/**
 * Main application class. Initializes dagger.
 */
public class MyApplication extends Application {

	private static AppComponent component;
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		component = DaggerAppComponent.builder()
			.userModule(new UserModule())
			.appModule(new AppModule(this))
			.networkModule(new NetworkModule())
			.bluetoothModule(new BluetoothModule(this))
			.build();
		context = getApplicationContext();
	}

	public static AppComponent getComponent() {
		return component;
	}

	public static Context getAppContext() {
		return context;
	}
}
package com.vizlore.phasmafood.dagger.modules;

import android.app.Application;

import com.vizlore.phasmafood.bluetooth.RxBluetooth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smedic on 1/9/18.
 */

@Module
public class BluetoothModule {

	private Application mApplication;

	public BluetoothModule(Application application) {
		mApplication = application;
	}

	@Provides
	@Singleton
	RxBluetooth provideRxBluetooth() {
		return new RxBluetooth(mApplication);
	}
}

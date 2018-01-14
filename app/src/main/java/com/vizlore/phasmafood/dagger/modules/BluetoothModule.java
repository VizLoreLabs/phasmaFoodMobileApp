package com.vizlore.phasmafood.dagger.modules;

import android.content.Context;

import com.vizlore.phasmafood.bluetooth.RxBluetooth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smedic on 1/9/18.
 */

@Module
public class BluetoothModule {

	@Provides
	@Singleton
	RxBluetooth provideRxBluetooth(Context context) {
		return new RxBluetooth(context);
	}
}

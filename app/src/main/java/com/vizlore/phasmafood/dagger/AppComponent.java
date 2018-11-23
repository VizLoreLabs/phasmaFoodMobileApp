package com.vizlore.phasmafood.dagger;

import com.vizlore.phasmafood.TestingUtils;
import com.vizlore.phasmafood.bluetooth.BluetoothService;
import com.vizlore.phasmafood.dagger.modules.AppModule;
import com.vizlore.phasmafood.dagger.modules.BluetoothModule;
import com.vizlore.phasmafood.dagger.modules.DeviceModule;
import com.vizlore.phasmafood.dagger.modules.MeasurementModule;
import com.vizlore.phasmafood.dagger.modules.MobileModule;
import com.vizlore.phasmafood.dagger.modules.NetworkModule;
import com.vizlore.phasmafood.dagger.modules.RepositoriesModule;
import com.vizlore.phasmafood.dagger.modules.UserModule;
import com.vizlore.phasmafood.services.MyFirebaseInstanceIDService;
import com.vizlore.phasmafood.ui.configuration.ConfigurationActivity;
import com.vizlore.phasmafood.viewmodel.BluetoothViewModel;
import com.vizlore.phasmafood.viewmodel.DeviceViewModel;
import com.vizlore.phasmafood.viewmodel.FcmMobileViewModel;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by smedic on 1/4/18.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, UserModule.class, BluetoothModule.class,
	MobileModule.class, MeasurementModule.class, DeviceModule.class, RepositoriesModule.class})
public interface AppComponent {

	void inject(UserViewModel userViewModel);

	void inject(BluetoothViewModel bluetoothViewModel);

	void inject(FcmMobileViewModel configViewModel);

	void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);

	void inject(MeasurementViewModel examinationViewModel);

	void inject(BluetoothService bluetoothService);

	void inject(DeviceViewModel deviceViewModel);

	void inject(ConfigurationActivity configurationActivity);

	void inject(TestingUtils testingUtils);
}
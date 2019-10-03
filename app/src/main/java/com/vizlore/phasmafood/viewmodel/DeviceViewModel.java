package com.vizlore.phasmafood.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.utils.Utils;

import javax.inject.Inject;

/**
 * Created by smedic on 2/28/18.
 */

public class DeviceViewModel extends AndroidViewModel {

	private String deviceId = null;

	@Inject
	DeviceApi deviceApi;

	public DeviceViewModel(@NonNull Application application) {
		super(application);
		MyApplication.getComponent().inject(this);
	}

	@Nullable
	public String getDeviceAddress() {
		if (deviceId == null) {
			deviceId = Utils.getBluetoothDeviceAddress();
		}
		return deviceId;
	}
}

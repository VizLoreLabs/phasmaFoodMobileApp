package com.vizlore.phasmafood.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
import com.vizlore.phasmafood.utils.Utils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by smedic on 2/28/18.
 */

public class DeviceViewModel extends AndroidViewModel {

	private SingleLiveEvent<Boolean> createDeviceLiveData;
	private SingleLiveEvent<Boolean> readDeviceLiveData;

	private Disposable disposable;

	private String deviceId = null;
	private String deviceMAC = null;

	@Inject
	DeviceApi deviceApi;

	public DeviceViewModel(@NonNull Application application) {
		super(application);
		MyApplication.getComponent().inject(this);
	}

	@Nullable
	public String getDeviceID() {
		if (deviceId == null) {
			deviceId = Utils.getBluetoothDeviceUUID();
		}
		return deviceId;
	}

	public String getDeviceMAC() {
		if (deviceMAC == null) {
			deviceMAC = "1234567890"; // TODO: 9/8/18
		}
		return deviceMAC;
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}

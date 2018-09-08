package com.vizlore.phasmafood.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.utils.Config;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
import com.vizlore.phasmafood.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 2/28/18.
 */

public class DeviceViewModel extends AndroidViewModel {

	private SingleLiveEvent<Boolean> createDeviceLiveData;
	private MutableLiveData<Boolean> readDeviceLiveData;

	private Disposable disposable;

	private String deviceId = null;
	private String deviceMAC = null;

	@Inject
	DeviceApi deviceApi;

	public DeviceViewModel(@NonNull Application application) {
		super(application);
		MyApplication.getComponent().inject(this);
	}

	public LiveData<Boolean> createDevice() {
		if (createDeviceLiveData == null) {
			createDeviceLiveData = new SingleLiveEvent<>();
		}

		final Map<String, String> requestBody = new HashMap<>();
		requestBody.put(Config.DEVICE_UUID, getDeviceMAC());
		requestBody.put(Config.DEVICE_MAC, getDeviceID());

		disposable = deviceApi.createDevice(requestBody)
			.subscribeOn(Schedulers.computation())
			.subscribe(
				() -> createDeviceLiveData.postValue(true),
				e -> createDeviceLiveData.postValue(false)
			);

		return createDeviceLiveData;
	}

	public LiveData<Boolean> readDevice() {
		if (readDeviceLiveData == null) {
			readDeviceLiveData = new MutableLiveData<>();
		}

		deviceApi.readDevice(getDeviceID())
			.subscribeOn(Schedulers.computation())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					readDeviceLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					readDeviceLiveData.postValue(false);
				}
			});
		return readDeviceLiveData;
	}

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

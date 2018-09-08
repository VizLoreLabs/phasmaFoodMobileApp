package com.vizlore.phasmafood.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.DeviceApi;
import com.vizlore.phasmafood.utils.Config;
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

	private static final String TAG = "SMEDIC DVM";

	private MutableLiveData<Boolean> createDeviceLiveData;
	private MutableLiveData<Boolean> readDeviceLiveData;

	@Inject
	DeviceApi deviceApi;

	public DeviceViewModel(@NonNull Application application) {
		super(application);
		MyApplication.getComponent().inject(this);
	}

	public LiveData<Boolean> createDevice() {
		if (createDeviceLiveData == null) {
			createDeviceLiveData = new MutableLiveData<>();
		}

		final Map<String, String> requestBody = new HashMap<>();
		requestBody.put(Config.DEVICE_UUID, "1234567890");
		requestBody.put(Config.DEVICE_MAC, Utils.getBluetoothDeviceUUID());

		deviceApi.createDevice(requestBody)
			.subscribeOn(Schedulers.computation())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					createDeviceLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					createDeviceLiveData.postValue(false);
				}
			});

		return createDeviceLiveData;
	}

	public LiveData<Boolean> readDevice() {
		if (readDeviceLiveData == null) {
			readDeviceLiveData = new MutableLiveData<>();
		}

		deviceApi.readDevice(Utils.getBluetoothDeviceUUID())
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
}

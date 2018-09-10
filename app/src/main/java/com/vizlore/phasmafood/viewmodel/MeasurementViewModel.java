package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.model.results.Sample;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
import com.vizlore.phasmafood.utils.Utils;

import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 2/10/18.
 */

public class MeasurementViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";

	private static final String EXAMPLE_DEVICE_ID = "d946a1ce-af55-41d2-9aa0-e";

	private Disposable disposable;

	private SingleLiveEvent<Boolean> measurementLiveData;

	@Inject
	MeasurementRepository measurementRepository;

	public MeasurementViewModel() {
		MyApplication.getComponent().inject(this);
	}

	public LiveData<Boolean> createMeasurementRequest(final String userId, final Sample sample,
													  final String deviceId) {

		if (measurementLiveData == null) {
			measurementLiveData = new SingleLiveEvent<>();
		}

		Log.d(TAG, "createMeasurementRequest: User ID: " + userId);

		final Random rand = new Random();
		final int randomValue = rand.nextInt(1000000);

		final String sampleId = String.valueOf(randomValue);
		sample.setSampleID(sampleId);
		sample.setUserID(userId);
		sample.setDeviceID(deviceId);
		sample.setMobileID(Utils.getMobileUUID());

		disposable = measurementRepository.createMeasurementRequest(sample)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> measurementLiveData.postValue(true),
				error -> {
					Log.d(TAG, "onError error: " + error.toString());
					measurementLiveData.postValue(false);
				});
		return measurementLiveData;
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}
}
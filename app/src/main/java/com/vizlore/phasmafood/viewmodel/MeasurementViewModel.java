package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.model.DebugError;
import com.vizlore.phasmafood.model.configuration.Configuration;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.Sample;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.utils.Resource;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
import com.vizlore.phasmafood.utils.Utils;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by smedic on 2/10/18.
 */

public class MeasurementViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";

	private static final String EXAMPLE_DEVICE_ID = "d946a1ce-af55-41d2-9aa0-e";

	private Disposable disposable;

	private SingleLiveEvent<Resource<String>> measurementLiveData;

	@Inject
	MeasurementRepository measurementRepository;

	public MeasurementViewModel() {
		MyApplication.getComponent().inject(this);
	}

	public LiveData<Resource<String>> createMeasurementRequest(final String userId, final Sample sample,
															   final String deviceId, boolean shouldAnalyze) {
		if (measurementLiveData == null) {
			measurementLiveData = new SingleLiveEvent<>();
		}

		final Random rand = new Random();
		final int randomValue = rand.nextInt(1000000);


		final String sampleId = String.valueOf(randomValue);
		sample.setSampleID(sampleId);
		sample.setUserID(userId);
		sample.setDeviceID(deviceId);
		sample.setMobileID(Utils.getMobileUUID());
		sample.setConfiguration(measurementRepository.getConfiguration());

		disposable = measurementRepository.createMeasurementRequest(sample, shouldAnalyze)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> measurementLiveData.postValue(Resource.success("Success!")),
				error -> {
					Log.d(TAG, "onError: " + error.toString());
					//reportError(error);
					measurementLiveData.postValue(Resource.error(error.toString(), null));
				});
		return measurementLiveData;
	}

	private void reportError(Throwable throwable) {
		if (throwable instanceof HttpException) {
			HttpException exception = (HttpException) throwable;

			String body = "";
			if (exception.response() != null && exception.response().errorBody() != null) {
				try {
					body = exception.response().errorBody().string();
				} catch (IOException e) {
					body = "";
					e.printStackTrace();
				}
			}

			disposable = measurementRepository.postError(new DebugError(exception.code() + " - " + exception.message() +
				body))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(() -> Log.d(TAG, "reportError: REPORTED!!!"),
					error -> Log.d(TAG, "ERROR REPORTING?!?! " + error.toString()));
		}
	}

	@Override
	protected void onCleared() {
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
		super.onCleared();
	}

	public void saveMeasurement(@NonNull final Measurement measurement) {
		measurementRepository.saveMeasurement(measurement);
	}

	public Measurement getSavedMeasurement() {
		return measurementRepository.getMeasurement();
	}

	public String getSuccessfulMeasurementTime() {
		final Date date = measurementRepository.getMeasurementCompletedTime();
		if (date != null) {
			return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date);
		}
		return "";
	}

	public String getMeasurementImagePath() {
		return measurementRepository.getMeasurementImagePath();
	}

	public void saveConfigurationParams(@NonNull final Configuration configuration) {
		measurementRepository.saveConfiguration(configuration);
	}

	public void saveProcessingRequestType(MeasurementRepository.ProcessingRequestType type) {
		measurementRepository.saveProcessingRequestType(type);
	}

	public MeasurementRepository.ProcessingRequestType getProcessingRequestType() {
		return measurementRepository.getProcessingRequestType();
	}
}

package com.vizlore.phasmafood.repositories;

import android.support.annotation.NonNull;

import com.vizlore.phasmafood.api.MeasurementApi;
import com.vizlore.phasmafood.model.DebugError;
import com.vizlore.phasmafood.model.DebugMeasurement;
import com.vizlore.phasmafood.model.configuration.Configuration;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.Sample;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class MeasurementRepositoryImpl implements MeasurementRepository {

	final MeasurementApi measurementApi;

	public MeasurementRepositoryImpl(@NonNull final MeasurementApi measurementApi) {
		this.measurementApi = measurementApi;
	}

	@Override
	public Completable createMeasurementRequest(@NonNull final Sample measurement, boolean shouldAnalyze) {
		String action = shouldAnalyze ? "analyze" : "store";
		return measurementApi.createMeasurementRequest(measurement, action)
			.subscribeOn(Schedulers.computation());
	}

	@Override
	public Completable postMeasurementString(@NonNull DebugMeasurement debugMeasurement) {
		return measurementApi.postMeasurementString(debugMeasurement)
			.subscribeOn(Schedulers.computation());
	}

	@Override
	public Completable postError(@NonNull DebugError debugError) {
		return measurementApi.postError(debugError)
			.subscribeOn(Schedulers.computation());
	}

	// temporary save examination and image path
	private Measurement measurement;
	private Configuration configuration;
	private String measurementImagePath;
	private Date successfulMeasurementTime;

	public void saveMeasurement(@NonNull final Measurement measurement) {
		this.measurement = measurement;
		this.successfulMeasurementTime = new Date();
	}

	@Override
	public Measurement getMeasurement() {
		return measurement;
	}

	@Override
	public Date getMeasurementCompletedTime() {
		return successfulMeasurementTime;
	}

	@Override
	public String getMeasurementImagePath() {
		return measurementImagePath;
	}

	@Override
	public void saveMeasurementImagePath(@NonNull final String measurementImagePath) {
		this.measurementImagePath = measurementImagePath;
	}

	@Override
	public void saveConfiguration(@NonNull Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
}

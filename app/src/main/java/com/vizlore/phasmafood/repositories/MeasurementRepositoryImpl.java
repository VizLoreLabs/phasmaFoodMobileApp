package com.vizlore.phasmafood.repositories;

import android.support.annotation.NonNull;

import com.vizlore.phasmafood.api.MeasurementApi;
import com.vizlore.phasmafood.model.configuration.Configuration;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.Sample;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class MeasurementRepositoryImpl implements MeasurementRepository {

	final MeasurementApi measurementApi;

	public MeasurementRepositoryImpl(@NonNull final MeasurementApi measurementApi) {
		this.measurementApi = measurementApi;
	}

	@Override
	public Completable createMeasurementRequest(Sample measurement) {
		return measurementApi.createMeasurementRequest(measurement)
			.subscribeOn(Schedulers.computation());
	}

	// temporary save examination and image path
	private Measurement measurement;
	private Configuration configuration;
	private String measurementImagePath;

	public void saveMeasurement(@NonNull final Measurement measurement) {
		this.measurement = measurement;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public String getMeasurementImagePath() {
		return measurementImagePath;
	}

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

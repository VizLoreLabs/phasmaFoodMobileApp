package com.vizlore.phasmafood.repositories;

import android.support.annotation.NonNull;

import com.vizlore.phasmafood.api.MeasurementApi;
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
}

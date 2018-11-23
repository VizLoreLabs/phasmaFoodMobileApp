package com.vizlore.phasmafood.repositories;

import android.support.annotation.NonNull;

import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.Sample;

import io.reactivex.Completable;
import retrofit2.http.Body;

public interface MeasurementRepository {

	Completable createMeasurementRequest(@Body Sample measurement);

	//save locally for now
	// TODO: 11/23/18 implement Room db
	void saveMeasurement(@NonNull Measurement measurement);

	Measurement getMeasurement();

	String getMeasurementImagePath();

	void saveMeasurementImagePath(@NonNull String measurementImagePath);
}

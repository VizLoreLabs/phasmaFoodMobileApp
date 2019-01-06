package com.vizlore.phasmafood.repositories;

import android.support.annotation.NonNull;

import com.vizlore.phasmafood.model.configuration.Configuration;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.Sample;

import java.util.Date;

import io.reactivex.Completable;

public interface MeasurementRepository {

	Completable createMeasurementRequest(@NonNull Sample measurement, boolean shouldAnalyze);

	//save locally for now
	// TODO: 11/23/18 implement Room db
	void saveMeasurement(@NonNull Measurement measurement);

	Measurement getMeasurement();

	Date getMeasurementCompletedTime();

	void saveMeasurementImagePath(@NonNull String measurementImagePath);

	String getMeasurementImagePath();

	void saveConfiguration(@NonNull Configuration configuration);

	Configuration getConfiguration();
}

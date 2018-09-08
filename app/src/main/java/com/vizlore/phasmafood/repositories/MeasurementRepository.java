package com.vizlore.phasmafood.repositories;

import com.vizlore.phasmafood.model.results.Sample;

import io.reactivex.Completable;
import retrofit2.http.Body;

public interface MeasurementRepository {

	Completable createMeasurementRequest(@Body Sample measurement);

}

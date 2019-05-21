package com.vizlore.phasmafood.api;

import com.vizlore.phasmafood.model.DebugError;
import com.vizlore.phasmafood.model.DebugMeasurement;
import com.vizlore.phasmafood.model.results.Sample;

import io.reactivex.Completable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by smedic on 2/10/18.
 */

public interface MeasurementApi {

	@POST("/api/v1/data/measurement/")
	Completable createMeasurementRequest(@Body Sample measurement, @Query("operation") String operation);

	@POST("/api/v1/data/measurement/debug")
	Completable postMeasurementString(@Body DebugMeasurement debugMeasurement);

	@POST("/api/v1/data/measurement/response")
	Completable postError(@Body DebugError debugError);
}

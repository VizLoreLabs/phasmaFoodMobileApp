package com.vizlore.phasmafood.api;

import com.vizlore.phasmafood.model.results.Sample;

import io.reactivex.Completable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by smedic on 2/10/18.
 */

public interface MeasurementApi {

	@POST("/api/v1/dashboard/measurement/")
	Completable createMeasurementRequest(@Body Sample measurement);
}

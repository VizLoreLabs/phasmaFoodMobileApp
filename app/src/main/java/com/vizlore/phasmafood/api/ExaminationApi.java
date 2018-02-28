package com.vizlore.phasmafood.api;

import com.vizlore.phasmafood.model.results.Examination;

import io.reactivex.Completable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by smedic on 2/10/18.
 */

public interface ExaminationApi {

	@POST("/api/v1/requests/")
	Completable createExaminationRequest(@Header("Authorization") String token, @Body Examination examination);
}

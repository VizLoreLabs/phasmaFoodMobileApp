package com.vizlore.phasmafood.api;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by smedic on 1/23/18.
 */

public interface FcmMobileApi {

	@POST("/api/v1/mobile/")
	Completable sendFcmData(@Body Map<String, String> body);

	@GET("/api/v1/mobile/{registration_id}")
	Single<ResponseBody> readFcmToken(@Path("registration_id") String regId);

	@PUT("/api/v1/mobile/{registration_id}")
	Completable updateFcmData(@Path("registration_id") String regId, @Body Map<String, String> body);

	@PATCH("/api/v1/mobile/{registration_id}")
	Completable partialUpdateFcmData(@Path("registration_id") String regId, @Body Map<String, String> body);

	@DELETE("/api/v1/mobile/{registration_id}")
	Completable deleteFcmToken(@Path("registration_id") String regId);
}

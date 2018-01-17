package com.vizlore.phasmafood.api;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by smedic on 12/29/17.
 */

public interface UserApi {

	@POST("/api/v1/auth/users/create/")
	Completable createAccount(@Body Map<String, String> body);

	@POST("/api/v1/auth/jwt/create/")
	Single<ResponseBody> getToken(@Body Map<String, String> body);

	@POST("/api/v1/auth/jwt/refresh/")
	Single<ResponseBody> getRefreshToken(@Body Map<String, String> body);

	@POST("/api/v1/auth/jwt/verify/")
	Single<ResponseBody> verifyToken(@Body Map<String, String> body);

	@GET("/api/v1/auth/me/")
	Single<ResponseBody> getProfile(@Header("Authorization") String token);
}

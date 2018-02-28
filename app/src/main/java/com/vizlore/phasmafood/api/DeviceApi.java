package com.vizlore.phasmafood.api;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by smedic on 2/25/18.
 */

public interface DeviceApi {

	@GET("/api/v1/device/")
	Single<ResponseBody> listDevice(@Header("Authorization") String token);

	@POST("/api/v1/device/")
	Completable createDevice(@Header("Authorization") String token, @Body Map<String, String> body);

	@GET("/api/v1/device/{serial_cpu}")
	Completable readDevice(@Header("Authorization") String token, @Path("serial_cpu") String serialCpu);

	@PUT("/api/v1/device/{serial_cpu}")
	Completable updateDevice(@Header("Authorization") String token, @Path("serial_cpu") String serialCpu);

	@PATCH("/api/v1/device/{serial_cpu}/")
	Completable partialUpdateDevice(@Header("Authorization") String token, @Path("serial_cpu") String serialCpu, @Body Map<String, String> body);

	@DELETE("/api/v1/device/{serial_cpu}")
	Completable deleteDevice(@Header("Authorization") String token, @Path("serial_cpu") String serialCpu);
}

package com.vizlore.phasmafood.api;

import com.vizlore.phasmafood.model.User;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by smedic on 12/29/17.
 */

public interface UserApi {

	@GET("/bins/eey2r")
	Single<User> hasSession();

	@POST("/api/v1/auth/users/create/")
	Completable createAccount(@Body Map<String, String> body);

	@POST("/api/v1/auth/jwt/create/")
	Single<Object> getToken(@Body Map<String, String> body);

	// TODO: 1/4/18
	@GET("/bins/eey2r")
	Completable signIn(final String username, final String password);

	// TODO: 1/4/18
	@GET("/bins/eey2r")
	Completable signOut();

	// TODO: 1/4/18
	@GET("/bins/eey2r")
	Completable changePassword(final String newPassword);

	// TODO: 1/4/18
	@GET("/bins/eey2r")
	Completable resetPassword(final String email);
}

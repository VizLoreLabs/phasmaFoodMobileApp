package com.vizlore.phasmafood.api;

import com.vizlore.phasmafood.model.User;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by smedic on 12/29/17.
 */

public interface UserApi {

	@GET("/bins/eey2r")
	Single<User> hasSession();

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

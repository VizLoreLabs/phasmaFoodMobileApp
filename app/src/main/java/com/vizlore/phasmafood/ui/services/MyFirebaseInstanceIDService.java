package com.vizlore.phasmafood.ui.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.FcmMobileApi;
import com.vizlore.phasmafood.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 1/23/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

	private static final String TAG = "SMEDIC FCM Service";

	@Inject
	SharedPreferences sharedPreferences;

	@Inject
	FcmMobileApi mobileApi;

	public MyFirebaseInstanceIDService() {
		super();
		MyApplication.getComponent().inject(this);
	}

	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. Note that this is called when the InstanceID token
	 * is initially generated so this is where you would retrieve the token.
	 */
	// [START refresh_token]
	@Override
	public void onTokenRefresh() {
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Refreshed token: " + refreshedToken);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
		sendRegistrationToServer(refreshedToken);
	}
	// [END refresh_token]

	/**
	 * Persist token to third-party servers.
	 * <p>
	 * Modify this method to associate the user's FCM InstanceID token with any server-side account
	 * maintained by your application.
	 *
	 * @param token The new token.
	 */
	private void sendRegistrationToServer(String token) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung A5");
		requestBody.put("registration_id", token);
		requestBody.put("type", "android");

		mobileApi.sendFcmData(Utils.getHeader(sharedPreferences), requestBody).observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
			@Override
			public void onSubscribe(Disposable d) {
				Log.d(TAG, "onSubscribe: ");
			}

			@Override
			public void onComplete() {
				Log.d(TAG, "onComplete: ");
			}

			@Override
			public void onError(Throwable e) {
				Log.d(TAG, "onError: " + e.toString());
			}
		});
	}
}

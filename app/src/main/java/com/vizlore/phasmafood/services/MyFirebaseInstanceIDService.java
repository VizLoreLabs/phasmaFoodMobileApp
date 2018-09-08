package com.vizlore.phasmafood.services;

import android.content.SharedPreferences;
import android.os.Build;
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

import static com.vizlore.phasmafood.viewmodel.UserViewModel.TOKEN_KEY;

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
		Log.d(TAG, "Refreshed FCM token: " + refreshedToken);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
		if (hasSession()) {
			sendRegistrationToServer(refreshedToken);
		}
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

		//example: Samsung Note 4 6.0 MARSHMALLOW
		final String deviceDetails = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE
			+ " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

		final Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", deviceDetails);
		requestBody.put("registration_id", FirebaseInstanceId.getInstance().getToken());
		requestBody.put("device_id", Utils.getMobileUUID());
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.sendFcmData(requestBody).observeOn(AndroidSchedulers.mainThread())
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

	public boolean hasSession() {
		return sharedPreferences.contains(TOKEN_KEY);
	}
}

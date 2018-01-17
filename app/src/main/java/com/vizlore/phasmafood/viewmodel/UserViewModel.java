package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by smedic on 1/4/18.
 */

public class UserViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";

	@Inject
	Retrofit retrofit;

	@Inject
	UserApi userApi;

	// testing
	private boolean hasSession;

	private MutableLiveData<Boolean> hasSessionLiveData;
	private MutableLiveData<Boolean> signInLiveData;
	private MutableLiveData<Boolean> signOutLiveData;
	private MutableLiveData<Boolean> createAccountLiveData;

	public UserViewModel() {
		MyApplication.getComponent().inject(this);
	}

	public LiveData<Boolean> hasSession() {

		if (hasSessionLiveData == null) {
			hasSessionLiveData = new MutableLiveData<>();
		}

		new Handler().postDelayed(() -> {

			if (hasSession) {
				hasSessionLiveData.setValue(false);
				hasSession = false;
			} else {
				hasSessionLiveData.setValue(true);
				hasSession = true;
			}

		}, 1500);

//		userApi.hasSession()
//				.subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread()).
//				subscribe(new SingleObserver<User>() {
//					@Override
//					public void onSubscribe(Disposable d) {
//						Log.d(TAG, "onSubscribe: ");
//					}
//
//					@Override
//					public void onSuccess(User user) {
//						Log.d(TAG, "onSuccess: ");
//						hasSessionLiveData.setValue(true);
//					}
//
//					@Override
//					public void onError(Throwable e) {
//						Log.d(TAG, "onError: " + e.toString());
//						hasSessionLiveData.setValue(false);
//
//					}
//				});

		return hasSessionLiveData;
	}

	public LiveData<Boolean> createAccount(String firstName, String lastName, String username, String company,
										   String email, String password) {
		if (createAccountLiveData == null) {
			createAccountLiveData = new MutableLiveData<>();
		}

		JSONObject paramObject = new JSONObject();
		try {
			if (!firstName.isEmpty()) {
				paramObject.put("first_name", firstName);
			}
			if (!lastName.isEmpty()) {
				paramObject.put("last_name", lastName);
			}
			paramObject.put("username", username);
			paramObject.put("company", company);
			paramObject.put("email", email);
			paramObject.put("company", company);
			paramObject.put("password", password);

			Log.d(TAG, "onCreateAccountClicked: json: " + paramObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		userApi.createAccount(paramObject.toString())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new CompletableObserver() {
					@Override
					public void onSubscribe(Disposable d) {
					}

					@Override
					public void onComplete() {
						Log.d(TAG, "onComplete: ");
						createAccountLiveData.postValue(true);
					}

					@Override
					public void onError(Throwable e) {
						Log.d(TAG, "onError: e: " + e.getMessage());
						createAccountLiveData.postValue(false);
					}
				});

		return createAccountLiveData;
	}

	public LiveData<Boolean> signIn(final String email, final String password) {

		if (signInLiveData == null) {
			signInLiveData = new MutableLiveData<>();
		}

		// TODO: 1/5/18 call sign in
		new Handler().postDelayed(() -> signInLiveData.setValue(true), 1500);

		return signInLiveData;
	}

	public LiveData<Boolean> signOut() {

		if (signOutLiveData == null) {
			signOutLiveData = new MutableLiveData<>();
		}

		// TODO: 1/5/18 call sign in
		new Handler().postDelayed(() -> signOutLiveData.setValue(true), 1500);

		return signOutLiveData;
	}

	@Override
	protected void onCleared() {
		super.onCleared();
	}
}

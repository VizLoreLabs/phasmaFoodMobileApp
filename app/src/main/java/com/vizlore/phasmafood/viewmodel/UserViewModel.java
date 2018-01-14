package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.UserApi;

import javax.inject.Inject;

/**
 * Created by smedic on 1/4/18.
 */

public class UserViewModel extends ViewModel {

	@Inject
	UserApi userApi;

	// testing
	private boolean hasSession;

	private MutableLiveData<Boolean> hasSessionLiveData;
	private MutableLiveData<Boolean> signInLiveData;
	private MutableLiveData<Boolean> signOutLiveData;

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

	public LiveData<Boolean> signIn() {

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
}

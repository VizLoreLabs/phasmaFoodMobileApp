package com.vizlore.phasmafood.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.FcmMobileApi;
import com.vizlore.phasmafood.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by smedic on 1/23/18.
 */

public class FcmMobileViewModel extends AndroidViewModel {

	private static final String TAG = "SMEDIC ConfigViewModel";

	private MutableLiveData<Boolean> createFcmMobileLiveData;
	private MutableLiveData<Boolean> readFcmTokenLiveData;
	private MutableLiveData<Boolean> updateFcmDataLiveData;
	private MutableLiveData<Boolean> partialUpdateFcmDataLiveData;
	private MutableLiveData<Boolean> deleteFcmTokenLiveData;

	@Inject
	SharedPreferences prefs;

	@Inject
	FcmMobileApi mobileApi;

	public FcmMobileViewModel(@NonNull Application application) {
		super(application);

		MyApplication.getComponent().inject(this);
	}

	public LiveData<Boolean> sendFcmToken() {

		if (createFcmMobileLiveData == null) {
			createFcmMobileLiveData = new MutableLiveData<>();
		}

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung A5_A520F");
		requestBody.put("registration_id", FirebaseInstanceId.getInstance().getToken());
		requestBody.put("device_id", Utils.getUUID(prefs));
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.sendFcmData(Utils.getHeader(prefs), requestBody).observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
			@Override
			public void onSubscribe(Disposable d) {
			}

			@Override
			public void onComplete() {
				createFcmMobileLiveData.postValue(true);
			}

			@Override
			public void onError(Throwable e) {
				Log.d(TAG, "onError: " + e.toString());
				createFcmMobileLiveData.postValue(false);
			}
		});

		return createFcmMobileLiveData;
	}

	// validate current token
	public LiveData<Boolean> readFcmToken() {

		if (readFcmTokenLiveData == null) {
			readFcmTokenLiveData = new MutableLiveData<>();
		}

		mobileApi.readFcmToken(Utils.getHeader(prefs), FirebaseInstanceId.getInstance().getToken())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new SingleObserver<ResponseBody>() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onSuccess(ResponseBody responseBody) {
					//do nothing with data (for now)
					readFcmTokenLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					readFcmTokenLiveData.postValue(false);
				}
			});

		return readFcmTokenLiveData;
	}

	public LiveData<Boolean> updateFcmData() {

		if (updateFcmDataLiveData == null) {
			updateFcmDataLiveData = new MutableLiveData<>();
		}

		String regId = FirebaseInstanceId.getInstance().getToken();

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung A5");
		requestBody.put("registration_id", regId);
		requestBody.put("device_id", Utils.getUUID(prefs));
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.updateFcmData(Utils.getHeader(prefs), regId, requestBody)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					Log.d(TAG, "onComplete updateFcmData: ");
					updateFcmDataLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError updateFcmData: " + e.toString());
					updateFcmDataLiveData.postValue(false);
				}
			});
		return updateFcmDataLiveData;
	}

	public LiveData<Boolean> partialUpdateFcmData() {

		if (partialUpdateFcmDataLiveData == null) {
			partialUpdateFcmDataLiveData = new MutableLiveData<>();
		}

		String regId = FirebaseInstanceId.getInstance().getToken();

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung A5");
		requestBody.put("registration_id", regId);
		requestBody.put("device_id", Utils.getUUID(prefs));
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.partialUpdateFcmData(Utils.getHeader(prefs), regId, requestBody)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					Log.d(TAG, "onComplete partialUpdateFcmData: ");
					partialUpdateFcmDataLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError partialUpdateFcmData: " + e.toString());
					partialUpdateFcmDataLiveData.postValue(false);
				}
			});

		return partialUpdateFcmDataLiveData;
	}

	public LiveData<Boolean> deleteFcmToken() {

		if (deleteFcmTokenLiveData == null) {
			deleteFcmTokenLiveData = new MutableLiveData<>();
		}

		mobileApi.deleteFcmToken(Utils.getHeader(prefs), FirebaseInstanceId.getInstance().getToken())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					Log.d(TAG, "onComplete deleteFcmToken: ");
					deleteFcmTokenLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError deleteFcmToken: " + e.getMessage());
					deleteFcmTokenLiveData.postValue(false);
				}
			});

		return deleteFcmTokenLiveData;
	}
}

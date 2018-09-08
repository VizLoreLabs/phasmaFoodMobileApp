package com.vizlore.phasmafood.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.FcmMobileApi;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
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

	private SingleLiveEvent<Boolean> createFcmMobileLiveData;
	private SingleLiveEvent<Boolean> readFcmTokenLiveData;
	private SingleLiveEvent<Boolean> updateFcmDataLiveData;
	private SingleLiveEvent<Boolean> partialUpdateFcmDataLiveData;
	private SingleLiveEvent<Boolean> deleteFcmTokenLiveData;

	@Inject
	FcmMobileApi mobileApi;

	public FcmMobileViewModel(@NonNull Application application) {
		super(application);

		MyApplication.getComponent().inject(this);
	}

	public LiveData<Boolean> sendFcmToken() {

		if (createFcmMobileLiveData == null) {
			createFcmMobileLiveData = new SingleLiveEvent<>();
		}

		//example: Samsung Note 4 6.0 MARSHMALLOW
		final String deviceDetails = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE
			+ " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

		final Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", deviceDetails);
		requestBody.put("registration_id", FirebaseInstanceId.getInstance().getToken());
		requestBody.put("device_id", Utils.getMobileUUID());
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.sendFcmData(requestBody)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new CompletableObserver() {
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
			readFcmTokenLiveData = new SingleLiveEvent<>();
		}

		mobileApi.readFcmToken(FirebaseInstanceId.getInstance().getToken())
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
			updateFcmDataLiveData = new SingleLiveEvent<>();
		}

		String regId = FirebaseInstanceId.getInstance().getToken();

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung A5");
		requestBody.put("registration_id", regId);
		requestBody.put("device_id", Utils.getMobileUUID());
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.updateFcmData(regId, requestBody)
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
			partialUpdateFcmDataLiveData = new SingleLiveEvent<>();
		}

		String regId = FirebaseInstanceId.getInstance().getToken();

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Samsung A5");
		requestBody.put("registration_id", regId);
		requestBody.put("device_id", Utils.getMobileUUID());
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.partialUpdateFcmData(regId, requestBody)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					partialUpdateFcmDataLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					partialUpdateFcmDataLiveData.postValue(false);
				}
			});

		return partialUpdateFcmDataLiveData;
	}

	public LiveData<Boolean> deleteFcmToken() {

		if (deleteFcmTokenLiveData == null) {
			deleteFcmTokenLiveData = new SingleLiveEvent<>();
		}

		mobileApi.deleteFcmToken(FirebaseInstanceId.getInstance().getToken())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					deleteFcmTokenLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					deleteFcmTokenLiveData.postValue(false);
				}
			});

		return deleteFcmTokenLiveData;
	}
}

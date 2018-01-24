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

import java.io.IOException;
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
		requestBody.put("name", "Samsung A5");
		requestBody.put("registration_id", FirebaseInstanceId.getInstance().getToken());
		requestBody.put("device_id", Utils.getUUID(prefs));
		//requestBody.put("active", "true");
		requestBody.put("type", "android");

		mobileApi.sendFcmData(Utils.getHeader(prefs), requestBody).observeOn(AndroidSchedulers.mainThread())
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

		return createFcmMobileLiveData;
	}

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
					Log.d(TAG, "onSubscribe: ");
				}

				@Override
				public void onSuccess(ResponseBody responseBody) {
					try {
						Log.d(TAG, "onSuccess: " + responseBody.string());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError: " + e.toString());
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
					Log.d(TAG, "onSubscribe updateFcmData: ");
				}

				@Override
				public void onComplete() {
					Log.d(TAG, "onComplete updateFcmData: ");
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError updateFcmData: " + e.toString());
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
					Log.d(TAG, "onSubscribe partialUpdateFcmData: ");
				}

				@Override
				public void onComplete() {
					Log.d(TAG, "onComplete partialUpdateFcmData: ");
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError partialUpdateFcmData: " + e.toString());
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
					Log.d(TAG, "onSubscribe deleteFcmToken: ");
				}

				@Override
				public void onComplete() {
					Log.d(TAG, "onComplete deleteFcmToken: ");
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError deleteFcmToken: " + e.getMessage());
				}
			});

		return deleteFcmTokenLiveData;
	}
}

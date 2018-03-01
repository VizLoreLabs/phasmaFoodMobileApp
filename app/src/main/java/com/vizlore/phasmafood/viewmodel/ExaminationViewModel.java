package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;
import com.vizlore.phasmafood.api.ExaminationApi;
import com.vizlore.phasmafood.model.results.Examination;
import com.vizlore.phasmafood.utils.JsonFileLoader;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
import com.vizlore.phasmafood.utils.Utils;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 2/10/18.
 */

public class ExaminationViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";

	private static final String EXAMPLE_DEVICE_ID = "d946a1ce-af55-41d2-9aa0-e";

	private Examination examination;

	private MutableLiveData<Examination> examinationLiveData;
	private SingleLiveEvent<Boolean> createExaminationRequestLiveData;

	@Inject
	ExaminationApi examinationApi;

	public ExaminationViewModel() {

		MyApplication.getComponent().inject(this);

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();

		// FIXME: 3/1/18 remove test results sending
		String json = new JsonFileLoader().fromAsset("results.json");
		examination = gson.fromJson(json, Examination.class);
	}

	public LiveData<Examination> getExamination() {
		if (examinationLiveData == null) {
			examinationLiveData = new MutableLiveData<>();
		}
		examinationLiveData.postValue(examination);
		return examinationLiveData;
	}

	public LiveData<Boolean> createExaminationRequest(String userId) {

		if (createExaminationRequestLiveData == null) {
			createExaminationRequestLiveData = new SingleLiveEvent<>();
		}

		// TODO: 2/10/18 find better solution
		String sampleId = String.valueOf(new Date().getTime() % 1000000000);
		examination.setSampleID(sampleId);
		examination.setUserID(userId);
		examination.setDeviceID(Utils.getBluetoothDeviceUUID());
		examination.setMobileID(Utils.getMobileUUID());

		examinationApi.createExaminationRequest(Utils.getHeader(), examination)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					createExaminationRequestLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError error: " + e.toString());
					createExaminationRequestLiveData.postValue(false);
				}
			});

		return createExaminationRequestLiveData;
	}
}

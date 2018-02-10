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
	private MutableLiveData<Boolean> createExaminationRequestLiveData;

	@Inject
	ExaminationApi examinationApi;

	public ExaminationViewModel() {

		MyApplication.getComponent().inject(this);

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
		String json = new JsonFileLoader().fromAsset("assets/results.json");
		examination = gson.fromJson(json, Examination.class);

		Log.d(TAG, "ExaminationViewModel: examination: " + examination);
	}

	public LiveData<Examination> getExamination() {
		if (examinationLiveData == null) {
			examinationLiveData = new MutableLiveData<>();
		}
		examinationLiveData.postValue(examination);
		return examinationLiveData;
	}

	public LiveData<Boolean> createExaminationRequest() {

		if (createExaminationRequestLiveData == null) {
			createExaminationRequestLiveData = new MutableLiveData<>();
		}

		// TODO: 2/10/18 find better solution
		String sampleId = String.valueOf(new Date().getTime() % 1000000000);
		examination.setSampleId(sampleId);
		Log.d(TAG, "createExaminationRequest: time: " + sampleId);
		examination.setUserId("10"); // FIXME: 2/10/18
		examination.setDeviceId(EXAMPLE_DEVICE_ID); // FIXME: 2/10/18

		examinationApi.createExaminationRequest(examination)
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

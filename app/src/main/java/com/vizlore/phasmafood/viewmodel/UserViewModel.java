package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by smedic on 1/4/18.
 */

public class UserViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";

	public static final String TOKEN_KEY = "TOKEN";
	public static final String USER_KEY = "USER";

	@Inject
	SharedPreferences sharedPreferences;

	@Inject
	UserApi userApi;

	private MutableLiveData<Boolean> signOutLiveData;

	private MutableLiveData<Boolean> createAccountLiveData;
	private MutableLiveData<Boolean> getTokenLiveData;
	private MutableLiveData<Boolean> getRefreshTokenLiveData;
	private MutableLiveData<String> getProfileLiveData;

	public UserViewModel() {
		MyApplication.getComponent().inject(this);
	}

	public boolean hasSession() {
		return sharedPreferences.contains(TOKEN_KEY);
	}

	public String createHeader() {
		return "JWT " + sharedPreferences.getString(TOKEN_KEY, "");
	}

	/**
	 * Creates account
	 *
	 * @param firstName field
	 * @param lastName  field
	 * @param username  field
	 * @param company   field
	 * @param email     field
	 * @param password  field
	 * @return observable live data
	 */
	public LiveData<Boolean> createAccount(String firstName, String lastName, String username, String company,
										   String email, String password) {
		if (createAccountLiveData == null) {
			createAccountLiveData = new MutableLiveData<>();
		}

		Map<String, String> requestBody = new HashMap<>();

		if (!firstName.isEmpty()) {
			requestBody.put("first_name", firstName);
		}
		if (!lastName.isEmpty()) {
			requestBody.put("last_name", lastName);
		}
		requestBody.put("username", username);
		requestBody.put("company", company);
		requestBody.put("email", email);
		requestBody.put("company", company);
		requestBody.put("password", password);

		userApi.createAccount(requestBody)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new CompletableObserver() {
					@Override
					public void onSubscribe(Disposable d) {
					}

					@Override
					public void onComplete() {
						createAccountLiveData.postValue(true);
					}

					@Override
					public void onError(Throwable e) {
						Log.d(TAG, "onError: e: " + e.toString());
						createAccountLiveData.postValue(false);
					}
				});

		return createAccountLiveData;
	}

	/**
	 * Gets token for given fields
	 *
	 * @param email    field
	 * @param password field
	 * @return observable live data
	 */
	public LiveData<Boolean> getToken(@NonNull final String email, @NonNull final String password) {

		if (getTokenLiveData == null) {
			getTokenLiveData = new MutableLiveData<>();
		}

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", email);
		requestBody.put("password", password);

		userApi.getToken(requestBody)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<ResponseBody>() {
					@Override
					public void onSubscribe(Disposable d) {
						Log.d(TAG, "onSubscribe: ");
					}

					@Override
					public void onSuccess(ResponseBody responseBody) {

						// save received token and user
						try {
							JSONObject jsonObject = new JSONObject(responseBody.string());
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.putString(TOKEN_KEY, jsonObject.getString("token"));
							editor.putString(USER_KEY, jsonObject.getString("user"));
							editor.apply();
							getTokenLiveData.postValue(true);
						} catch (IOException | JSONException e) {
							Log.d(TAG, "onSuccess exception: " + e.getMessage());
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable e) {
						Log.d(TAG, "onError: " + e.toString());
						getTokenLiveData.postValue(false);
					}
				});

		return getTokenLiveData;
	}

	/**
	 * Gets refresh token for given current token
	 *
	 * @param token field
	 * @return observable live data
	 */
	public LiveData<Boolean> getRefreshToken(@NonNull final String token) {

		if (getRefreshTokenLiveData == null) {
			getRefreshTokenLiveData = new MutableLiveData<>();
		}

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("token", token);

		userApi.getRefreshToken(requestBody)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<ResponseBody>() {
					@Override
					public void onSubscribe(Disposable d) {
						Log.d(TAG, "onSubscribe: ");
					}

					@Override
					public void onSuccess(ResponseBody responseBody) {
						// TODO: 1/17/18 get refresh token from response
						getRefreshTokenLiveData.postValue(true);
					}

					@Override
					public void onError(Throwable e) {
						Log.d(TAG, "onError: " + e.toString());
						getRefreshTokenLiveData.postValue(false);
					}
				});

		return getRefreshTokenLiveData;
	}


	/**
	 * Get current profile
	 *
	 * @return observable live data with profile json string
	 */
	public LiveData<String> getProfile() {
		if (!hasSession()) {
			throw new IllegalStateException("Get profile called for not logged user");
		}

		if (getProfileLiveData == null) {
			getProfileLiveData = new MutableLiveData<>();
		}

		// provide header token
		userApi.getProfile(createHeader())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SingleObserver<ResponseBody>() {
					@Override
					public void onSubscribe(Disposable d) {
					}

					@Override
					public void onSuccess(ResponseBody responseBody) {
						try {
							getProfileLiveData.postValue(responseBody.string());
						} catch (IOException e) {
							getProfileLiveData.postValue("");
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable e) {
						getProfileLiveData.postValue("");
					}
				});

		return getProfileLiveData;
	}

	/**
	 * Sign out and remove token
	 *
	 * @return observable live data for notifying when sign out is done
	 */
	public LiveData<Boolean> signOut() {

		if (signOutLiveData == null) {
			signOutLiveData = new MutableLiveData<>();
		}

		// TODO: 1/17/18 do some saving if needed
		if (hasSession()) {
			sharedPreferences.edit().remove(TOKEN_KEY).apply();
			signOutLiveData.postValue(true);
		} else {
			signOutLiveData.postValue(false);
		}

		return signOutLiveData;
	}

	@Override
	protected void onCleared() {
		super.onCleared();
	}
}

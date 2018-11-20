package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.UserApi;
import com.vizlore.phasmafood.model.User;
import com.vizlore.phasmafood.utils.SingleLiveEvent;

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

	private MutableLiveData<Boolean> signOutLiveData;

	private MutableLiveData<Boolean> createAccountLiveData;
	private SingleLiveEvent<String> getTokenLiveData;
	private MutableLiveData<Boolean> getRefreshTokenLiveData;
	private SingleLiveEvent<User> getProfileLiveData;
	private MutableLiveData<Boolean> updateProfileLiveData;

	@Inject
	SharedPreferences sharedPreferences;

	@Inject
	UserApi userApi;

	@Inject
	Gson gson;

	public UserViewModel() {
		MyApplication.getComponent().inject(this);
	}

	public boolean hasSession() {
		return sharedPreferences.contains(TOKEN_KEY);
	}

	public String getSalvedToken() {
		return sharedPreferences.getString(TOKEN_KEY, null);
	}

	public void saveToken(@NonNull String token) {
		sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
	}

	public void clearToken() {
		sharedPreferences.edit().remove(TOKEN_KEY).apply();
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
					Log.d(TAG, "onError: " + e.getMessage());
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
	public LiveData<String> getToken(@NonNull final String email, @NonNull final String password) {

		if (getTokenLiveData == null) {
			getTokenLiveData = new SingleLiveEvent<>();
		}

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", email);
		requestBody.put("password", password);

		userApi.getToken(requestBody)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.map(responseBody -> {
				JSONObject jsonObject = new JSONObject(responseBody.string());
				if (jsonObject.has("token") && jsonObject.has("user")) {
					try {
						String token = jsonObject.getString("token");
						String user = jsonObject.getString("user");
						// save received token and user
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putString(TOKEN_KEY, "JWT " + token);
						editor.putString(USER_KEY, user);
						editor.apply();
						return token;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return null;
			})
			.subscribe(new SingleObserver<String>() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onSuccess(String token) {
					getTokenLiveData.postValue(token);
				}

				@Override
				public void onError(Throwable e) {
					Log.d(TAG, "onError: " + e.toString());
					getTokenLiveData.postValue(null);
				}
			});

		return getTokenLiveData;
	}

	/**
	 * Gets refresh token for given current token
	 *
	 * @return observable live data
	 */
//	public LiveData<Boolean> getRefreshToken() {
//
//		if (getRefreshTokenLiveData == null) {
//			getRefreshTokenLiveData = new MutableLiveData<>();
//		}
//
//		Map<String, String> requestBody = new HashMap<>();
//		requestBody.put("token", getSavedToken());
//
//		userApi.getRefreshToken(requestBody)
//			.subscribeOn(Schedulers.io())
//			.observeOn(AndroidSchedulers.mainThread())
//			.subscribe(new SingleObserver<ResponseBody>() {
//				@Override
//				public void onSubscribe(Disposable d) {
//				}
//
//				@Override
//				public void onSuccess(ResponseBody responseBody) {
//					if (responseBody != null) {
//						try {
//							JsonObject object = new JsonParser().parse(responseBody.string()).getAsJsonObject();
//							saveToken(object.get("token").getAsString());
//							getRefreshTokenLiveData.postValue(true);
//						} catch (IOException e) {
//							getRefreshTokenLiveData.postValue(false);
//							e.printStackTrace();
//						}
//					}
//				}
//
//				@Override
//				public void onError(Throwable e) {
//					Log.d(TAG, "onError: " + e.toString());
//					getRefreshTokenLiveData.postValue(false);
//				}
//			});
//
//		return getRefreshTokenLiveData;
//	}


	/**
	 * Get current profile
	 *
	 * @return observable live data with User profile
	 */
	public LiveData<User> getUserProfile() {
		if (!hasSession()) {
			throw new IllegalStateException("Get profile called for not logged user");
		}

		if (getProfileLiveData == null) {
			getProfileLiveData = new SingleLiveEvent<>();
		}

		// provide header token
		userApi.getCurrentProfile()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new SingleObserver<ResponseBody>() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onSuccess(ResponseBody responseBody) {
					try {
						User user = gson.fromJson(responseBody.string(), User.class);
						getProfileLiveData.postValue(user);
					} catch (IOException e) {
						getProfileLiveData.postValue(null);
						e.printStackTrace();
					}
				}

				@Override
				public void onError(Throwable e) {
					getProfileLiveData.postValue(null);
				}
			});

		return getProfileLiveData;
	}

	/**
	 * Updates user profile
	 *
	 * @param user profile
	 * @return observable live data with boolean result
	 */
	public LiveData<Boolean> updateProfile(@NonNull final User user) {

		if (updateProfileLiveData == null) {
			updateProfileLiveData = new MutableLiveData<>();
		}

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("first_name", user.firstName());
		requestBody.put("last_name", user.lastName());
		requestBody.put("username", user.username());
		requestBody.put("company", user.company());

		userApi.updateProfile(requestBody)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new CompletableObserver() {
				@Override
				public void onSubscribe(Disposable d) {
				}

				@Override
				public void onComplete() {
					updateProfileLiveData.postValue(true);
				}

				@Override
				public void onError(Throwable e) {
					updateProfileLiveData.postValue(false);
				}
			});

		return updateProfileLiveData;
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
			clearToken();
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

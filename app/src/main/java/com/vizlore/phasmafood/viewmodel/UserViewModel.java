package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.api.UserApi;
import com.vizlore.phasmafood.model.User;
import com.vizlore.phasmafood.utils.Resource;
import com.vizlore.phasmafood.utils.SingleLiveEvent;
import com.vizlore.phasmafood.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smedic on 1/4/18.
 */

public class UserViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";

	public static final String TOKEN_KEY = "TOKEN";
	public static final String USER_ID_KEY = "USER_ID";

	private CompositeDisposable disposable = new CompositeDisposable();

	private SingleLiveEvent<Boolean> createAccountLiveData;
	private SingleLiveEvent<Resource<String>> getTokenLiveData;
	private SingleLiveEvent<User> getProfileLiveData;
	private SingleLiveEvent<Boolean> updateProfileLiveData;

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

	private void clearToken() {
		sharedPreferences.edit().remove(TOKEN_KEY).apply();
	}

	private void clearUser() {
		sharedPreferences.edit().remove(USER_ID_KEY).apply();
	}

	/**
	 * Creates account
	 */
	public LiveData<Boolean> createAccount(@NonNull String firstName, @NonNull String lastName, @NonNull String username,
										   @NonNull String company, @NonNull String email, @NonNull String password) {
		if (createAccountLiveData == null) {
			createAccountLiveData = new SingleLiveEvent<>();
		}

		final Map<String, String> requestBody = new HashMap<>();

		requestBody.put("first_name", firstName);
		requestBody.put("last_name", lastName);
		requestBody.put("username", username);
		requestBody.put("company", company);
		requestBody.put("email", email);
		requestBody.put("password", password);

		disposable.add(userApi.createAccount(requestBody)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> createAccountLiveData.postValue(true),
				error -> {
					createAccountLiveData.postValue(false);
				}
			));

		return createAccountLiveData;
	}

	/**
	 * Login user and returns auth token
	 */
	public LiveData<Resource<String>> login(@NonNull final String email, @NonNull final String password) {

		if (getTokenLiveData == null) {
			getTokenLiveData = new SingleLiveEvent<>();
		}

		final Map<String, String> requestBody = new HashMap<>();
		requestBody.put("email", email);
		requestBody.put("password", password);

		disposable.add(userApi.login(requestBody)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(response -> {
					Utils.saveAuthToken(response.getToken());
					Utils.saveUserId(response.getUser().id());
					getTokenLiveData.postValue(Resource.success("Success"));
				},
				error -> getTokenLiveData.postValue(Resource.error(error.getMessage(), null))));

		return getTokenLiveData;
	}

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
		disposable.add(userApi.getCurrentProfile()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(user -> getProfileLiveData.postValue(user),
				error -> getProfileLiveData.postValue(null)));

		return getProfileLiveData;
	}

	/**
	 * Updates user profile
	 *
	 * @param user profile
	 * @return observable live data with boolean result
	 */
	public LiveData<Boolean> updateProfile(@NonNull final User user) {
		if (!hasSession()) {
			throw new IllegalStateException("Get profile called for not logged user");
		}

		if (updateProfileLiveData == null) {
			updateProfileLiveData = new SingleLiveEvent<>();
		}

		final Map<String, String> requestBody = new HashMap<>();
		requestBody.put("first_name", user.firstName());
		requestBody.put("last_name", user.lastName());
		requestBody.put("username", user.username());
		requestBody.put("company", user.company());

		disposable.add(userApi.updateProfile(requestBody)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(() -> updateProfileLiveData.postValue(true), error -> updateProfileLiveData.postValue(false)));

		return updateProfileLiveData;
	}

	/**
	 * Sign out and remove token
	 */
	public void signOut() {
		clearUser();
		clearToken();
		Utils.clearUuids();
	}

	@Override
	protected void onCleared() {
		if (!disposable.isDisposed()) {
			disposable.dispose();
		}
		super.onCleared();
	}
}

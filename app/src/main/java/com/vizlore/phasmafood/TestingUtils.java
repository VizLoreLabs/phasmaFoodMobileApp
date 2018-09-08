package com.vizlore.phasmafood;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.ui.ResultsActivity;
import com.vizlore.phasmafood.utils.JsonFileLoader;
import com.vizlore.phasmafood.viewmodel.DeviceViewModel;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

public class TestingUtils {

	private static final String TAG = "SMEDIC TESTING";

	//DEBUG
	static boolean IS_DEBUG = false;

	// just for testing
	public static void performTestMeasurement(@NonNull final FragmentActivity activity) {

		final MeasurementViewModel model = ViewModelProviders.of(activity).get(MeasurementViewModel.class);
		final UserViewModel userViewModel = ViewModelProviders.of(activity).get(UserViewModel.class);
		final DeviceViewModel deviceViewModel = ViewModelProviders.of(activity).get(DeviceViewModel.class);

		userViewModel.getUserProfile().observe(activity, user -> {

			final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
			final String json = new JsonFileLoader().fromAsset("usecase1_updated_response.json");
			final Measurement measurement = gson.fromJson(json, Measurement.class);

			// TODO: 9/8/18 refactor
			MyApplication.getInstance().saveMeasurement(measurement);

			final Intent intent = new Intent(activity, ResultsActivity.class);
			intent.putExtra("VIS", "IPO3");
			intent.putExtra("NIR", "IPO3");
			intent.putExtra("FLOU", "N/A");

			if (IS_DEBUG) {
				activity.startActivity(intent);
			} else {
				Log.d(TAG, "performTestMeasurement: user id: " + user.id());
				deviceViewModel.createDevice().observe(activity, res -> {

					Log.d(TAG, "performTestMeasurement: create device result: " + res);

					model.createMeasurementRequest(user.id(),
						measurement.getResponse().getSample(),
						deviceViewModel.getDeviceID())
						.observe(activity, result -> {
							if (result != null && !result) {
								Toast.makeText(activity, "Examination request failed!", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(activity, "Examination successful!", Toast.LENGTH_SHORT).show();
								activity.startActivity(intent);
							}
						});
				});
			}
		});
	}
}

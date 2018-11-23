package com.vizlore.phasmafood;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vizlore.phasmafood.api.AutoValueGsonFactory;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.utils.JsonFileLoader;
import com.vizlore.phasmafood.viewmodel.DeviceViewModel;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import javax.inject.Inject;

public class TestingUtils {

	private static final String TAG = "SMEDIC TESTING";
	private static boolean IS_DEBUG = false;

	@Inject
	MeasurementRepository measurementRepository;

	public TestingUtils() {
		MyApplication.getComponent().inject(this);
	}

	// just for testing
	public void performTestMeasurement(@NonNull final AppCompatActivity activity) {

		final MeasurementViewModel model = ViewModelProviders.of(activity).get(MeasurementViewModel.class);
		final UserViewModel userViewModel = ViewModelProviders.of(activity).get(UserViewModel.class);
		final DeviceViewModel deviceViewModel = ViewModelProviders.of(activity).get(DeviceViewModel.class);

		final ProgressDialog dialog = new ProgressDialog(activity);
		dialog.setMessage("Please wait while measurement is in progress...");

		dialog.dismiss();

		userViewModel.getUserProfile().observe(activity, user -> {

			final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
			final String json = new JsonFileLoader().fromAsset("measurements_10_sample_full.json");
			final Measurement measurement = gson.fromJson(json, Measurement.class);

			// TODO: 9/8/18 refactor
			measurementRepository.saveMeasurement(measurement);

			final Intent intent = new Intent(activity, MeasurementResultsActivity.class);
			intent.putExtra("VIS", "N/A");
			intent.putExtra("NIR", "N/A");
			intent.putExtra("FLOU", "N/A");

			if (IS_DEBUG) {
				activity.startActivity(intent);
			} else {
				dialog.show();

				Log.d(TAG, "performTestMeasurement: user id: " + user.id());
				deviceViewModel.createDevice().observe(activity, res -> {

					Log.d(TAG, "performTestMeasurement: create device result: " + res);

					model.createMeasurementRequest(user.id(), measurement.getResponse().getSample(), "90:70:65:EF:4A:CE") // TODO: 9/11/18 fix)
						.observe(activity, result -> {
							if (result != null && !result) {
								Toast.makeText(activity, "Examination request failed!", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(activity, "Examination successful!", Toast.LENGTH_SHORT).show();
								activity.startActivity(intent);
							}
							dialog.dismiss();
						});
				});
			}
		});
	}

	public static Measurement readMeasurementFromJson(@NonNull final String jsonName) {
		final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
		final String json = new JsonFileLoader().fromAsset(jsonName);
		return gson.fromJson(json, Measurement.class);
	}
}

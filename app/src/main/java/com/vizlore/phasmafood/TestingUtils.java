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
import com.vizlore.phasmafood.utils.Constants;
import com.vizlore.phasmafood.utils.JsonFileLoader;
import com.vizlore.phasmafood.utils.Resource;
import com.vizlore.phasmafood.utils.Utils;
import com.vizlore.phasmafood.viewmodel.DeviceViewModel;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import javax.inject.Inject;

public class TestingUtils {

	private static final String TAG = "SMEDIC TESTING";
	private static boolean IS_DEBUG = false;

	private static final String MEASUREMENT_10_SAMPLES = "measurements_10_sample_full.json";
	private static final String WHITE_REF_MEASUREMENT_10_SAMPLES = "white_reference_10_measurements.json";

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

		final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
		final String json = new JsonFileLoader().fromAsset(WHITE_REF_MEASUREMENT_10_SAMPLES);
		final Measurement measurement = gson.fromJson(json, Measurement.class);

		// TODO: 9/8/18 refactor
		measurementRepository.saveMeasurement(measurement);

		final Intent intent = new Intent(activity, MeasurementResultsActivity.class);
		intent.putExtra(MeasurementResultsActivity.IS_FROM_SERVER, false);
		intent.putExtra(Constants.VIS, "N/A");
		intent.putExtra(Constants.NIR, "N/A");
		intent.putExtra(Constants.FLUO, "N/A");

		if (IS_DEBUG) {
			activity.startActivity(intent);
		} else {
			dialog.show();

			Log.d(TAG, "performTestMeasurement: user id: " + Utils.getUserId());
			deviceViewModel.createDevice().observe(activity, res -> {

				Log.d(TAG, "performTestMeasurement: create device result: " + res);

				model.createMeasurementRequest(Utils.getUserId(), measurement.getResponse().getSample(),
					"90:70:65:EF:4A:CE", true) // TODO: 9/11/18 fix)
					.observe(activity, result -> {
						if (result == null) {
							Toast.makeText(activity, "Result null! Contact support", Toast.LENGTH_SHORT).show();
							return;
						}

						if (result.status == Resource.Status.ERROR) {
							//Toast.makeText(activity, "Examination request failed!", Toast.LENGTH_SHORT).show();
							Toast.makeText(activity, "Failure! Error: " + result.message, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();
							activity.startActivity(intent);
						}
						dialog.dismiss();
					});
			});
		}
	}

	public static Measurement readMeasurementFromJson(@NonNull final String jsonName) {
		final Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
		final String json = new JsonFileLoader().fromAsset(jsonName);
		return gson.fromJson(json, Measurement.class);
	}
}

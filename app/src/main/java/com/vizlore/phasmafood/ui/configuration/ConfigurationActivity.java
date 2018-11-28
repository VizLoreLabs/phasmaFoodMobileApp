package com.vizlore.phasmafood.ui.configuration;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.bluetooth.BluetoothService;
import com.vizlore.phasmafood.model.configuration.Camera;
import com.vizlore.phasmafood.model.configuration.Configuration;
import com.vizlore.phasmafood.model.configuration.NirMicrolamps;
import com.vizlore.phasmafood.model.configuration.NirSpectrometer;
import com.vizlore.phasmafood.model.configuration.VisLeds;
import com.vizlore.phasmafood.model.configuration.VisSpectrometer;
import com.vizlore.phasmafood.ui.BaseActivity;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.ui.wizard.WizardActivity;
import com.vizlore.phasmafood.utils.Constants;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

import static com.vizlore.phasmafood.utils.Config.DEFAULT_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_CAMERA_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_BINNING_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_GAIN_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_SINGLE_SHOT;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_BINNING_FLOURESCENCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_EXPOSURE_TIME_FLOURESCENCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_GAIN_FLOURESCENCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_WHITE_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MAX_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_CAMERA_VOLTAGE_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_BINNING_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_GAIN_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_UV_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_VOLTAGE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_BINNING_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_GAIN_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_UV_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_WHITE_LEDS_CURRENT;

public class ConfigurationActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";

	private static final String USE_CASE_1_PARAM_1 = "alfatoxinName";
	private static final String USE_CASE_1_PARAM_2 = "alfatoxinValue";
	private static final String USE_CASE_1_PARAM_3 = "alfatoxinUnit";

	private static final String USE_CASE_2_PARAM_1 = "microbiologicalUnit";
	private static final String USE_CASE_2_PARAM_2 = "microbiologicalValue";

	private static final String USE_CASE_WHITE_REF_PARAM_1 = "timestamp";

	private static final String USE_CASE_1_JSON = "measurements_10_sample_full.json";
	private static final String USE_CASE_2_JSON = "usecase2_updated_response.json";

	private BluetoothService bluetoothService;
	private CompositeDisposable disposable = new CompositeDisposable();
	private MeasurementViewModel measurementViewModel;
	private JSONObject wizardJsonObject = null;
	private SharedPreferences prefs;

	private UseCaseType useCaseType;
	private String testSample;

	//use case labels
	@BindView(R.id.useCase1LabelsGroup)
	LinearLayout useCase1LabelsGroup;
	@BindView(R.id.alfatoxinName)
	EditText alfatoxinName;
	@BindView(R.id.alfatoxinValue)
	EditText alfatoxinValue;
	@BindView(R.id.alfatoxinUnit)
	EditText alfatoxinUnit;

	@BindView(R.id.useCase2LabelsGroup)
	LinearLayout useCase2LabelsGroup;
	@BindView(R.id.microbiologicalUnit)
	EditText microbiologicalUnit;
	@BindView(R.id.microbiologicalValue)
	EditText microbiologicalValue;

	//camera configuration
	@BindView(R.id.exposureTime)
	EditText cameraExposureTime;
	@BindView(R.id.drivingVoltage)
	EditText cameraVoltage;

	// NIR
	@BindView(R.id.nirGroup)
	LinearLayout nirGroup;
	@BindView(R.id.singleShotRadioGroup)
	RadioGroup singleShotRadioGroup;
	@BindView(R.id.averagesEditText)
	EditText averagesEditText;
	@BindView(R.id.nirMicrolampsCurrent)
	EditText nirMicrolampsCurrent;
	@BindView(R.id.nirMicrolampsWarmingTime)
	EditText nirMicrolampsWarmingTime;

	// VIS
	@BindView(R.id.visGroup)
	LinearLayout visGroup;
	@BindView(R.id.exposureTimeReflectance)
	EditText exposureTimeReflectance;
	@BindView(R.id.gainReflectance)
	EditText gainReflectance;
	@BindView(R.id.binningReflectance)
	EditText binningReflectance;
	@BindView(R.id.whiteLEDs)
	EditText whiteLEDsCurrent;

	@BindView(R.id.exposureTimeFluorescence)
	EditText exposureTimeFluorescence;
	@BindView(R.id.gainFluorescence)
	EditText gainFluorescence;
	@BindView(R.id.binningFluorescence)
	EditText binningFluorescence;
	@BindView(R.id.UVLEDs)
	EditText UVLEDsCurrent;

	@OnClick({R.id.backButton, R.id.sendRequest, R.id.setDefaults})
	void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.setDefaults:
				setDefaults();
				break;
			case R.id.sendRequest:
				try {
					sendRequest();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	private void sendRequest() throws JSONException {

		switch (wizardJsonObject.getString(Constants.USE_CASE_KEY)) {
			case Constants.USE_CASE_1:
				wizardJsonObject.put(USE_CASE_1_PARAM_1, alfatoxinName.getText().toString());
				wizardJsonObject.put(USE_CASE_1_PARAM_2, alfatoxinValue.getText().toString());
				wizardJsonObject.put(USE_CASE_1_PARAM_3, alfatoxinUnit.getText().toString());
				break;
			case Constants.USE_CASE_2:
				wizardJsonObject.put(USE_CASE_2_PARAM_1, microbiologicalValue.getText().toString());
				wizardJsonObject.put(USE_CASE_2_PARAM_2, microbiologicalUnit.getText().toString());
				break;
			case Constants.USE_CASE_WHITE_REF:
				wizardJsonObject.put(USE_CASE_WHITE_REF_PARAM_1, String.valueOf(new Date().getTime()));
				break;
		}

		//add config parameters to json request (camera, nir, vis)
		final Configuration configuration = getConfiguration();
		if (configuration != null) {

			saveParamsState(); //save new values to shared preferences
			measurementViewModel.saveConfigurationParams(configuration);

			final String configurationJson = new Gson().toJson(configuration);
			wizardJsonObject.put("configuration", configurationJson);
			//wrap into request
			final JSONObject jsonObjectRequest = new JSONObject();
			jsonObjectRequest.put("Request", wizardJsonObject);

			Log.d(TAG, "sendRequest: SEND: \n" + jsonObjectRequest.toString());

			showSendActionDialog(jsonObjectRequest.toString());
		} else {
			Toast.makeText(this, "Wrong input. Check fields", Toast.LENGTH_SHORT).show();
		}
	}

	private void showSendActionDialog(final String jsonToSend) {
		final AlertDialog alertDialog = new AlertDialog.Builder(ConfigurationActivity.this).create();
		alertDialog.setMessage(getString(R.string.sendDataMessage));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.yes), (dialog, which) -> {

			final boolean isDebugMode = prefs.getBoolean(WizardActivity.DEBUG_MODE_KEY, false);
			if (!isDebugMode) {
				Log.d(TAG, "showSendActionDialog: SEND REAL MESSAGE TO DEVICE!");
				bluetoothService.sendMessage(jsonToSend);
			} else {
				Log.d(TAG, "showSendActionDialog: PERFORM FAKE MEASUREMENT (debug mode)");
				//send params directly to server (use case 1 of 2)
				//load use case 1 by default
				String jsonFileName = USE_CASE_1_JSON;
				if (useCaseType == UseCaseType.USE_CASE_2) {
					jsonFileName = USE_CASE_2_JSON;
				}

				disposable.add(bluetoothService.sendFakeMessage(jsonFileName).subscribe(measurement -> {
						measurementViewModel.saveMeasurement(measurement);
						final Intent intent = new Intent(ConfigurationActivity.this, MeasurementResultsActivity.class);
						intent.putExtra(MeasurementResultsActivity.IS_FROM_SERVER, false);
						startActivity(intent);
					},
					e -> Log.d(TAG, "onError: " + e.getMessage())
				));
			}
			dialog.dismiss();
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.no), (dialog, which) -> dialog.dismiss());
		alertDialog.show();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		ButterKnife.bind(this);

		measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);

		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		// load from preferences or set defaults
		loadStartingValues();

		loadWizardJson();

		// start bluetooth service
		final Intent intent = new Intent(this, BluetoothService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE); //Binding to the service!
	}

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) iBinder;
			bluetoothService = binder.getServiceInstance(); //Get instance of your service!
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			bluetoothService = null;
		}
	};

	private void loadWizardJson() {

		if (getIntent() == null || getIntent().getExtras() == null || !getIntent().getExtras().containsKey(Constants.WIZARD_DATA_KEY)) {
			riseError("Error loading JSON from wizard! Contact support");
		}

		final String json = getIntent().getExtras().getString(Constants.WIZARD_DATA_KEY);

		try {
			wizardJsonObject = new JSONObject(json);

			if (!wizardJsonObject.has(Constants.USE_CASE_KEY)) {
				riseError("No use cases found! Contact support");
			}

			final String useCaseKey = wizardJsonObject.getString(Constants.USE_CASE_KEY);
			switch (useCaseKey) {
				case Constants.USE_CASE_1:
					useCase1LabelsGroup.setVisibility(View.VISIBLE);
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					useCaseType = UseCaseType.USE_CASE_1;
					break;
				case Constants.USE_CASE_2:
					useCase2LabelsGroup.setVisibility(View.VISIBLE);
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					useCaseType = UseCaseType.USE_CASE_2;
					break;
				case Constants.USE_CASE_3:
					useCaseType = UseCaseType.USE_CASE_3;
					break;
				case Constants.USE_CASE_WHITE_REF:
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					useCaseType = UseCaseType.USE_CASE_WHITE_REFERENCE;
					break;
				case Constants.USE_CASE_TEST:
					testSample = wizardJsonObject.getString(Constants.USE_CASE_TEST);
					if (testSample.contains(Constants.USE_CASE_TEST_NIR)) {
						nirGroup.setVisibility(View.VISIBLE);
					}
					if (testSample.contains(Constants.USE_CASE_TEST_VIS_FLUO)) {
						visGroup.setVisibility(View.VISIBLE);
					}
					useCaseType = UseCaseType.USE_CASE_TEST;
					break;
				default:
					useCase1LabelsGroup.setVisibility(View.GONE);
					useCase2LabelsGroup.setVisibility(View.GONE);
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					useCaseType = UseCaseType.USE_CASE_1;
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			riseError(e.getMessage());
		}
	}

	private void riseError(@NonNull final String error) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
		finish();
	}

	private void loadStartingValues() {
		// camera
		cameraExposureTime.setText(String.valueOf(prefs.getInt(KEY_CAMERA_EXPOSURE_TIME, DEFAULT_CAMERA_EXPOSURE_TIME)));
		cameraVoltage.setText(String.valueOf(prefs.getInt(KEY_CAMERA_VOLTAGE, DEFAULT_CAMERA_VOLTAGE)));
		// nir
		int saveRadioButtonIndex = prefs.getInt(KEY_NIR_SINGLE_SHOT, 1); // 1 is no (N)
		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(saveRadioButtonIndex).getId());
		averagesEditText.setText(String.valueOf(prefs.getInt(KEY_NIR_SPEC_AVERAGES, DEFAULT_NIR_SPEC_AVERAGES)));
		nirMicrolampsCurrent.setText(String.valueOf(prefs.getInt(KEY_NIR_MICROLAMPS_CURRENT, DEFAULT_NIR_MICROLAMPS_CURRENT)));
		nirMicrolampsWarmingTime.setText(String.valueOf(prefs.getInt(KEY_NIR_MICROLAMPS_WARMING_TIME, DEFAULT_NIR_MICROLAMPS_WARMING_TIME)));
		// vis
		exposureTimeReflectance.setText(String.valueOf(prefs.getInt(KEY_VIS_EXPOSURE_TIME_REFLECTANCE, DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE)));
		gainReflectance.setText(String.valueOf(prefs.getInt(KEY_VIS_GAIN_REFLECTANCE, DEFAULT_VIS_GAIN_REFLECTANCE)));
		binningReflectance.setText(String.valueOf(prefs.getInt(KEY_VIS_BINNING_REFLECTANCE, DEFAULT_VIS_BINNING_REFLECTANCE)));
		whiteLEDsCurrent.setText(String.valueOf(prefs.getInt(KEY_VIS_WHITE_LEDS_VOLTAGE, DEFAULT_VIS_WHITE_LEDS_CURRENT)));
		exposureTimeFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE)));
		gainFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_GAIN_FLOURESCENCE, DEFAULT_VIS_GAIN_FLUORESCENCE)));
		binningFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_BINNING_FLOURESCENCE, DEFAULT_VIS_BINNING_FLUORESCENCE)));
		UVLEDsCurrent.setText(String.valueOf(prefs.getInt(KEY_VIS_UV_LEDS_VOLTAGE, DEFAULT_VIS_UV_LEDS_VOLTAGE)));
	}

	private void setDefaults() {
		// camera
		cameraExposureTime.setText(String.valueOf(DEFAULT_CAMERA_EXPOSURE_TIME));
		cameraVoltage.setText(String.valueOf(DEFAULT_CAMERA_VOLTAGE));
		// nir
		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(1).getId()); //no is default // TODO: 4/22/18
		averagesEditText.setText(String.valueOf(DEFAULT_NIR_SPEC_AVERAGES));
		nirMicrolampsCurrent.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_CURRENT));
		nirMicrolampsWarmingTime.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_WARMING_TIME));
		// vis
		exposureTimeReflectance.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE));
		gainReflectance.setText(String.valueOf(DEFAULT_VIS_GAIN_REFLECTANCE));
		binningReflectance.setText(String.valueOf(DEFAULT_VIS_BINNING_REFLECTANCE));
		whiteLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_WHITE_LEDS_CURRENT));
		exposureTimeFluorescence.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE));
		gainFluorescence.setText(String.valueOf(DEFAULT_VIS_GAIN_FLUORESCENCE));
		binningFluorescence.setText(String.valueOf(DEFAULT_VIS_BINNING_FLUORESCENCE));
		UVLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_UV_LEDS_VOLTAGE));
	}

	private void saveParamsState() {
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(KEY_CAMERA_EXPOSURE_TIME, getValue(cameraExposureTime));
		editor.putInt(KEY_CAMERA_VOLTAGE, getValue(cameraVoltage));

		final int radioButtonID = singleShotRadioGroup.getCheckedRadioButtonId();
		final View radioButton = singleShotRadioGroup.findViewById(radioButtonID);
		final int idx = singleShotRadioGroup.indexOfChild(radioButton);
		editor.putInt(KEY_NIR_SINGLE_SHOT, idx);
		editor.putInt(KEY_NIR_SPEC_AVERAGES, getValue(averagesEditText));
		editor.putInt(KEY_NIR_MICROLAMPS_CURRENT, getValue(nirMicrolampsCurrent));
		editor.putInt(KEY_NIR_MICROLAMPS_WARMING_TIME, getValue(nirMicrolampsWarmingTime));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_REFLECTANCE, getValue(exposureTimeReflectance));
		editor.putInt(KEY_VIS_GAIN_REFLECTANCE, getValue(gainReflectance));
		editor.putInt(KEY_VIS_BINNING_REFLECTANCE, getValue(binningReflectance));
		editor.putInt(KEY_VIS_UV_LEDS_VOLTAGE, getValue(UVLEDsCurrent));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, getValue(exposureTimeFluorescence));
		editor.putInt(KEY_VIS_GAIN_FLOURESCENCE, getValue(gainFluorescence));
		editor.putInt(KEY_VIS_BINNING_FLOURESCENCE, getValue(binningFluorescence));
		editor.putInt(KEY_VIS_WHITE_LEDS_VOLTAGE, getValue(whiteLEDsCurrent));

		editor.apply();
	}

	private Configuration getConfiguration() {
		if (!isWithinBounds(getValue(cameraExposureTime), MIN_CAMERA_EXPOSURE_TIME, MAX_CAMERA_EXPOSURE_TIME)) {
			showError(cameraExposureTime);
			return null;
		}
		if (!isWithinBounds(getValue(cameraVoltage), MIN_CAMERA_VOLTAGE_TIME, MAX_CAMERA_VOLTAGE_TIME)) {
			showError(cameraVoltage);
			return null;
		}
		if (!isWithinBounds(getValue(averagesEditText), MIN_NIR_SPEC_AVERAGES, MAX_NIR_SPEC_AVERAGES)) {
			showError(averagesEditText);
			return null;
		}
		if (!isWithinBounds(getValue(nirMicrolampsCurrent), MIN_NIR_MICROLAMPS_CURRENT, MAX_NIR_MICROLAMPS_CURRENT)) {
			showError(nirMicrolampsCurrent);
			return null;
		}
		if (!isWithinBounds(getValue(nirMicrolampsWarmingTime), MIN_NIR_MICROLAMPS_WARMING_TIME, MAX_NIR_MICROLAMPS_WARMING_TIME)) {
			showError(nirMicrolampsWarmingTime);
			return null;
		}
		if (!isWithinBounds(getValue(exposureTimeReflectance), MIN_VIS_EXPOSURE_TIME_REFLECTANCE, MAX_VIS_EXPOSURE_TIME_REFLECTANCE)) {
			showError(exposureTimeReflectance);
			return null;
		}
		if (!isWithinBounds(getValue(gainReflectance), MIN_VIS_GAIN_REFLECTANCE, MAX_VIS_GAIN_REFLECTANCE)) {
			showError(gainReflectance);
			return null;
		}
		if (!isWithinBounds(getValue(binningReflectance), MIN_VIS_BINNING_REFLECTANCE, MAX_VIS_BINNING_REFLECTANCE)) {
			showError(binningReflectance);
			return null;
		}
		if (!isWithinBounds(getValue(whiteLEDsCurrent), MIN_VIS_WHITE_LEDS_CURRENT, MAX_VIS_WHITE_LEDS_CURRENT)) {
			showError(whiteLEDsCurrent);
			return null;
		}
		if (!isWithinBounds(getValue(exposureTimeFluorescence), MIN_VIS_EXPOSURE_TIME_FLUORESCENCE, MAX_VIS_EXPOSURE_TIME_FLUORESCENCE)) {
			showError(exposureTimeFluorescence);
			return null;
		}
		if (!isWithinBounds(getValue(gainFluorescence), MIN_VIS_GAIN_FLUORESCENCE, MAX_VIS_GAIN_FLUORESCENCE)) {
			showError(gainFluorescence);
			return null;
		}
		if (!isWithinBounds(getValue(binningFluorescence), MIN_VIS_BINNING_FLUORESCENCE, MAX_VIS_BINNING_FLUORESCENCE)) {
			showError(binningFluorescence);
			return null;
		}
		if (!isWithinBounds(getValue(UVLEDsCurrent), MIN_VIS_UV_LEDS_CURRENT, MAX_VIS_UV_LEDS_CURRENT)) {
			showError(UVLEDsCurrent);
			return null;
		}

		final Configuration configuration = new Configuration();

		// Camera
		final Camera camera = Camera.builder()
			.setTCam(getValue(cameraExposureTime))
			.setVwCam(getValue(cameraVoltage))
			.build();
		configuration.setCamera(camera);

		// NirMicrolamps
		// Add if not test sample at all OR it's a NIR test sample
		if (testSample == null || testSample.contains(Constants.USE_CASE_TEST_NIR)) {
			final NirMicrolamps nirMicrolamps = new NirMicrolamps();
			nirMicrolamps.setTNir(getValue(nirMicrolampsWarmingTime));
			nirMicrolamps.setVNir(getValue(nirMicrolampsCurrent));

			final String selectedOption = singleShotRadioGroup.getCheckedRadioButtonId() == R.id.yes ? "Y" : "N";
			final NirSpectrometer nirSpectrometer = NirSpectrometer.builder()
				.setSingle(selectedOption)
				.setAvNIRm(getValue(averagesEditText))
				.setNirMicrolamps(nirMicrolamps)
				.build();
			configuration.setNirSpectrometer(nirSpectrometer);
		}

		// VIS
		// Add if not test sample at all OR it's a VIS-FLUO test sample
		if (testSample == null || testSample.contains(Constants.USE_CASE_TEST_VIS_FLUO)) {
			final VisLeds visLeds = new VisLeds();
			visLeds.setVUV(getValue(UVLEDsCurrent));
			visLeds.setVwVis(getValue(whiteLEDsCurrent));

			final VisSpectrometer visSpectrometer = new VisSpectrometer();
			visSpectrometer.setBFluo(getValue(binningFluorescence));
			visSpectrometer.setBVis(getValue(binningReflectance));
			visSpectrometer.setGFluo(getValue(gainFluorescence));
			visSpectrometer.setGVis(getValue(gainReflectance));
			visSpectrometer.setTFluo(getValue(exposureTimeFluorescence));
			visSpectrometer.setTVis(getValue(exposureTimeReflectance));
			configuration.setVisSpectrometer(visSpectrometer);
		}

		return configuration;
	}

	private boolean isWithinBounds(int value, int min, int max) {
		return value >= min && value <= max;
	}

	private int getValue(EditText editText) {
		final String text = editText.getText().toString();
		if (!text.isEmpty()) {
			return Integer.parseInt(editText.getText().toString());
		}
		return -1; //value not added (empty field)
	}

	private void showError(EditText editText) {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		editText.startAnimation(shake);
	}

	@Override
	protected void onDestroy() {
		if (!disposable.isDisposed()) {
			disposable.clear();
		}
		super.onDestroy();
	}

	private enum UseCaseType {
		USE_CASE_1,
		USE_CASE_2,
		USE_CASE_3,
		USE_CASE_WHITE_REFERENCE,
		USE_CASE_TEST
	}
}

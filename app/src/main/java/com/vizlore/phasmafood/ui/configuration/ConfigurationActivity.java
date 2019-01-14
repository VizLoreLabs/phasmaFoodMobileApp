package com.vizlore.phasmafood.ui.configuration;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.vizlore.phasmafood.model.configuration.Camera;
import com.vizlore.phasmafood.model.configuration.Configuration;
import com.vizlore.phasmafood.model.configuration.NirMicrolamps;
import com.vizlore.phasmafood.model.configuration.NirSpectrometer;
import com.vizlore.phasmafood.model.configuration.VisLeds;
import com.vizlore.phasmafood.model.configuration.VisSpectrometer;
import com.vizlore.phasmafood.ui.BaseActivity;
import com.vizlore.phasmafood.ui.SendRequestActivity;
import com.vizlore.phasmafood.utils.Constants;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.vizlore.phasmafood.utils.Config.DEFAULT_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_CAMERA_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_MICROBIOLOGICAL_UNIT;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_MICROBIOLOGICAL_UNIT;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_SINGLE_SHOT;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_EXPOSURE_TIME_FLOURESCENCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_WHITE_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MAX_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_CAMERA_VOLTAGE_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_UV_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_VOLTAGE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_MICROLAMPS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_UV_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_WHITE_LEDS_CURRENT;

public class ConfigurationActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";

	private static final String USE_CASE_1_PARAM_1 = "alfatoxinName";
	private static final String USE_CASE_1_PARAM_2 = "alfatoxinValue";
	private static final String USE_CASE_1_PARAM_3 = "alfatoxinUnit";

	private static final String USE_CASE_2_PARAM_1 = "microbiologicalUnit";
	private static final String USE_CASE_2_PARAM_2 = "microbiologicalValue";
	private static final String USE_CASE_2_PARAM_3 = "microbioSampleId";

	private static final String USE_CASE_WHITE_REF_PARAM_1 = "timestamp";

	private MeasurementViewModel measurementViewModel;
	private JSONObject wizardJsonObject = null;
	private SharedPreferences prefs;

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
	@BindView(R.id.microbioSampleId)
	EditText microbioSampleId;

	//use case 3
	//param 1 - spirits
	@BindView(R.id.uc3param1)
	LinearLayout uc3param1Holder;
	@BindView(R.id.adulterationSampleID)
	EditText adulterationSampleID;
	@BindView(R.id.authentic)
	EditText authentic;
	@BindView(R.id.diluted)
	EditText diluted;
	@BindView(R.id.hazard1)
	EditText hazard1;
	@BindView(R.id.hazard1Percent)
	EditText hazard1Percent;
	@BindView(R.id.hazard2)
	EditText hazard2;
	@BindView(R.id.hazard2Percent)
	EditText hazard2Percent;

	//param 2 - Minced raw meat
	@BindView(R.id.uc3param2)
	LinearLayout uc3param2Holder;
	@BindView(R.id.adulterationSampleIDParam2)
	EditText adulterationSampleIDParam2;
	@BindView(R.id.authenticParam2)
	EditText authenticParam2;
	@BindView(R.id.dilution1)
	EditText dilution1;
	@BindView(R.id.dilution1Percent)
	EditText dilution1Percent;
	@BindView(R.id.dilution2)
	EditText dilution2;
	@BindView(R.id.dilution2Percent)
	EditText dilution2Percent;

	//param 3 - Edible oils
	@BindView(R.id.uc3param3)
	LinearLayout uc3param3Holder;
	@BindView(R.id.adulterationSampleIDParam3)
	EditText adulterationSampleIDParam3;
	@BindView(R.id.purity)
	EditText purity;
	@BindView(R.id.lowValueFiller)
	EditText lowValueFiller;
	@BindView(R.id.nitrogenEnhancer)
	EditText nitrogenEnhancer;

	//param 4 - Skimmed milk powder
	@BindView(R.id.uc3param4)
	LinearLayout uc3param4Holder;
	@BindView(R.id.adulterationSampleIDParam4)
	EditText adulterationSampleIDParam4;
	@BindView(R.id.authenticParam4)
	EditText authenticParam4;
	@BindView(R.id.otherSpecies)
	EditText otherSpecies;

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
	@BindView(R.id.whiteLEDs)
	EditText whiteLEDsCurrent;

	// FLUO
	@BindView(R.id.fluoGroup)
	LinearLayout fluoGroup;
	@BindView(R.id.exposureTimeFluorescence)
	EditText exposureTimeFluorescence;
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
				wizardJsonObject.put(USE_CASE_2_PARAM_1, microbiologicalUnit.getText().toString());
				wizardJsonObject.put(USE_CASE_2_PARAM_2, microbiologicalValue.getText().toString());
				wizardJsonObject.put(USE_CASE_2_PARAM_3, microbioSampleId.getText().toString());
				break;
			case Constants.USE_CASE_3:
				final String useCase3Param = wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE);
				switch (useCase3Param) {
					case Constants.USE_CASE_3_PARAM_1:
						wizardJsonObject.put(getString(R.string.adulterationSampleID), adulterationSampleID.getText().toString());
						wizardJsonObject.put(getString(R.string.authentic), authentic.getText().toString());
						wizardJsonObject.put(getString(R.string.diluted), diluted.getText().toString());
						wizardJsonObject.put(getString(R.string.hazard1), hazard1.getText().toString());
						wizardJsonObject.put(getString(R.string.hazard1Percent), hazard1Percent.getText().toString());
						wizardJsonObject.put(getString(R.string.hazard2), hazard2.getText().toString());
						wizardJsonObject.put(getString(R.string.hazard2Percent), hazard2Percent.getText().toString());
						break;
					case Constants.USE_CASE_3_PARAM_2:
						wizardJsonObject.put(getString(R.string.adulterationSampleID), adulterationSampleIDParam2.getText().toString());
						wizardJsonObject.put(getString(R.string.authentic), authenticParam2.getText().toString());
						wizardJsonObject.put(getString(R.string.dilution1), dilution1.getText().toString());
						wizardJsonObject.put(getString(R.string.dilution1Percent), dilution1Percent.getText().toString());
						wizardJsonObject.put(getString(R.string.dilution2), dilution2.getText().toString());
						wizardJsonObject.put(getString(R.string.dilution2Percent), dilution2Percent.getText().toString());
						break;
					case Constants.USE_CASE_3_PARAM_3:
						wizardJsonObject.put(getString(R.string.adulterationSampleID), adulterationSampleIDParam3.getText().toString());
						wizardJsonObject.put(getString(R.string.purity), purity.getText().toString());
						wizardJsonObject.put(getString(R.string.lowValueFiller), lowValueFiller.getText().toString());
						wizardJsonObject.put(getString(R.string.nitrogenEnhancer), nitrogenEnhancer.getText().toString());
						break;
					case Constants.USE_CASE_3_PARAM_4:
						wizardJsonObject.put(getString(R.string.adulterationSampleID), adulterationSampleIDParam4.getText().toString());
						wizardJsonObject.put(getString(R.string.authentic), authenticParam4.getText().toString());
						wizardJsonObject.put(getString(R.string.otherSpecies), otherSpecies.getText().toString());
						break;
				}
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
			wizardJsonObject.put("configuration", new JSONObject(configurationJson));

			// next step - send request
			final Intent intent = new Intent(this, SendRequestActivity.class);
			intent.putExtra(Constants.WIZARD_DATA_KEY, wizardJsonObject.toString());
			startActivity(intent);

		} else {
			Toast.makeText(this, "Wrong input. Check fields", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_configuration;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);

		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		// load from preferences or set defaults
		loadStartingValues();

		readWizardJson();
	}

	private void readWizardJson() {

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
					fluoGroup.setVisibility(View.VISIBLE);
					break;
				case Constants.USE_CASE_2:
					useCase2LabelsGroup.setVisibility(View.VISIBLE);
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					fluoGroup.setVisibility(View.VISIBLE);
					break;
				case Constants.USE_CASE_3:
					final String useCase3Param = wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE);
					switch (useCase3Param) {
						case Constants.USE_CASE_3_PARAM_1:
							uc3param1Holder.setVisibility(View.VISIBLE);
							break;
						case Constants.USE_CASE_3_PARAM_2:
							uc3param2Holder.setVisibility(View.VISIBLE);
							break;
						case Constants.USE_CASE_3_PARAM_3:
							uc3param3Holder.setVisibility(View.VISIBLE);
							break;
						case Constants.USE_CASE_3_PARAM_4:
							uc3param4Holder.setVisibility(View.VISIBLE);
							break;
					}
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					fluoGroup.setVisibility(View.VISIBLE);
					break;
				case Constants.USE_CASE_WHITE_REF:
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					fluoGroup.setVisibility(View.VISIBLE);
					break;
				case Constants.USE_CASE_TEST:
					testSample = wizardJsonObject.getString(Constants.USE_CASE_TEST);
					if (testSample.contains(Constants.USE_CASE_TEST_NIR)) {
						nirGroup.setVisibility(View.VISIBLE);
					}
					if (testSample.contains(Constants.USE_CASE_TEST_VIS)) {
						visGroup.setVisibility(View.VISIBLE);
					}
					if (testSample.contains(Constants.USE_CASE_TEST_FLUO)) {
						fluoGroup.setVisibility(View.VISIBLE);
					}
					break;
				default:
					useCase1LabelsGroup.setVisibility(View.GONE);
					useCase2LabelsGroup.setVisibility(View.GONE);
					nirGroup.setVisibility(View.VISIBLE);
					visGroup.setVisibility(View.VISIBLE);
					fluoGroup.setVisibility(View.VISIBLE);
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
		// use case 2
		microbiologicalUnit.setText(String.valueOf(prefs.getString(KEY_MICROBIOLOGICAL_UNIT, DEFAULT_MICROBIOLOGICAL_UNIT)));
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
		whiteLEDsCurrent.setText(String.valueOf(prefs.getInt(KEY_VIS_WHITE_LEDS_VOLTAGE, DEFAULT_VIS_WHITE_LEDS_CURRENT)));
		// fluo
		exposureTimeFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE)));
		UVLEDsCurrent.setText(String.valueOf(prefs.getInt(KEY_VIS_UV_LEDS_VOLTAGE, DEFAULT_VIS_UV_LEDS_VOLTAGE)));
	}

	private void setDefaults() {
		//use case 2
		microbiologicalUnit.setText(DEFAULT_MICROBIOLOGICAL_UNIT);
		// camera
		cameraExposureTime.setText(String.valueOf(DEFAULT_CAMERA_EXPOSURE_TIME));
		cameraVoltage.setText(String.valueOf(DEFAULT_CAMERA_VOLTAGE));
		// nir
		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(1).getId());
		averagesEditText.setText(String.valueOf(DEFAULT_NIR_SPEC_AVERAGES));
		nirMicrolampsCurrent.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_CURRENT));
		nirMicrolampsWarmingTime.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_WARMING_TIME));
		// vis
		exposureTimeReflectance.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE));
		whiteLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_WHITE_LEDS_CURRENT));
		//fluo
		exposureTimeFluorescence.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE));
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
		editor.putInt(KEY_VIS_UV_LEDS_VOLTAGE, getValue(UVLEDsCurrent));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, getValue(exposureTimeFluorescence));
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
		if (!isWithinBounds(getValue(whiteLEDsCurrent), MIN_VIS_WHITE_LEDS_CURRENT, MAX_VIS_WHITE_LEDS_CURRENT)) {
			showError(whiteLEDsCurrent);
			return null;
		}
		if (!isWithinBounds(getValue(exposureTimeFluorescence), MIN_VIS_EXPOSURE_TIME_FLUORESCENCE, MAX_VIS_EXPOSURE_TIME_FLUORESCENCE)) {
			showError(exposureTimeFluorescence);
			return null;
		}

		if (!isWithinBounds(getValue(UVLEDsCurrent), MIN_VIS_UV_LEDS_CURRENT, MAX_VIS_UV_LEDS_CURRENT)) {
			showError(UVLEDsCurrent);
			return null;
		}

		final Configuration configuration = new Configuration();

		// Camera
		final Camera camera = new Camera(getValue(cameraExposureTime), getValue(cameraVoltage));
		configuration.setCamera(camera);

		boolean isNirAvailable = false;
		boolean isVisAvailable = false;
		boolean isFluoAvailable = false;

		// NirMicrolamps
		// Add if not test sample at all OR it's a NIR test sample
		if (testSample == null || testSample.contains(Constants.USE_CASE_TEST_NIR)) {
			final NirMicrolamps nirMicrolamps = new NirMicrolamps();
			nirMicrolamps.setTNir(getValue(nirMicrolampsWarmingTime));
			nirMicrolamps.setVNir(getValue(nirMicrolampsCurrent));

			final String selectedOption = singleShotRadioGroup.getCheckedRadioButtonId() == R.id.yes ? "Y" : "N";
			final NirSpectrometer nirSpectrometer = new NirSpectrometer(selectedOption, getValue(averagesEditText), nirMicrolamps);
			configuration.setNirSpectrometer(nirSpectrometer);
			isNirAvailable = true;
		}

		final VisSpectrometer visSpectrometer = new VisSpectrometer();
		final VisLeds visLeds = new VisLeds();
		// VIS
		// Add if not test sample at all OR it's a VIS test sample
		if (testSample == null || testSample.contains(Constants.USE_CASE_TEST_VIS)) {
			visLeds.setVwVis(getValue(whiteLEDsCurrent));
			visSpectrometer.setTVis(getValue(exposureTimeReflectance));
			visSpectrometer.setVisLeds(visLeds);
			configuration.setVisSpectrometer(visSpectrometer);
			isVisAvailable = true;
		}

		// FLUO
		// Add if not test sample at all OR it's a FLUO test sample
		if (testSample == null || testSample.contains(Constants.USE_CASE_TEST_FLUO)) {
			visLeds.setVUV(getValue(UVLEDsCurrent));
			visSpectrometer.setTFluo(getValue(exposureTimeFluorescence));
			visSpectrometer.setVisLeds(visLeds);
			configuration.setVisSpectrometer(visSpectrometer);
			isFluoAvailable = true;
		}

		// NIR not available, fill with zeroes
		if (!isNirAvailable) {
			final NirMicrolamps nirMicrolamps = new NirMicrolamps();
			nirMicrolamps.setTNir(0);
			nirMicrolamps.setVNir(0);
			final NirSpectrometer nirSpectrometer = new NirSpectrometer("N", 0, nirMicrolamps);
			configuration.setNirSpectrometer(nirSpectrometer);
		}

		// VIS not available, fill with zeroes
		if (!isVisAvailable) {
			visLeds.setVwVis(0);
			visSpectrometer.setTVis(0);
			visSpectrometer.setVisLeds(visLeds);
			configuration.setVisSpectrometer(visSpectrometer);
		}

		// FLUO not available, fill with zeroes
		if (!isFluoAvailable) {
			visLeds.setVUV(0);
			visSpectrometer.setTFluo(0);
			visSpectrometer.setVisLeds(visLeds);
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
}

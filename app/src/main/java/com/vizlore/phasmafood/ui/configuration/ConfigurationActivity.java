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

import static com.vizlore.phasmafood.utils.Config.DEFAULT_MICROBIOLOGICAL_UNIT;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_EXPOSURE_TIME_NIR;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_EXPOSURE_TIME_UV;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_EXPOSURE_TIME_WHITE_LEDS;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_SINGLE_SHOT;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_EXPOSURE_TIME_FLOURESCENCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_VIS_WHITE_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MAX_LIGHTS_ON_DURATION;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_UV_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_LIGHTS_ON_DURATION;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_UV_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_WHITE_LEDS_CURRENT;
import static com.vizlore.phasmafood.utils.Constants.USE_CASE_3_PARAM_1;

public class ConfigurationActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";

	private static final String USE_CASE_1_PARAM_1 = "alfatoxinName";
	private static final String USE_CASE_1_PARAM_2 = "alfatoxinValue";
	private static final String USE_CASE_1_PARAM_3 = "alfatoxinUnit";

	private static final String USE_CASE_2_PARAM_1 = "microbiologicalUnit";
	private static final String USE_CASE_2_PARAM_2 = "microbiologicalValue";
	private static final String USE_CASE_2_PARAM_3 = "microbioSampleId";
	private static final String USE_CASE_2_PARAM_4 = "package";

	private static final String USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID = "adulterationSampleId";
	private static final String USE_CASE_3_PARAM_AUTHENTIC = "authentic";
	private static final String USE_CASE_3_PARAM_PURITY_SMP = "puritySMP";
	private static final String USE_CASE_3_PARAM_LOW_VALUE_FILLER = "lowValueFiller";
	private static final String USE_CASE_3_PARAM_NITROGEN_ENHANCER = "nitrogenEnhancer";
	private static final String USE_CASE_3_PARAM_HAZARD_1_NAME = "hazardOneName";
	private static final String USE_CASE_3_PARAM_HAZARD_1_PCT = "hazardOnePct";
	private static final String USE_CASE_3_PARAM_HAZARD_2_NAME = "hazardTwoName";
	private static final String USE_CASE_3_PARAM_HAZARD_2_PCT = "hazardTwoPct";
	private static final String USE_CASE_3_PARAM_OTHER_SPECIES = "otherSpecies";
	private static final String USE_CASE_3_PARAM_DILUTED_PCT = "dilutedPct";
	private static final String USE_CASE_3_PARAM_ALCOHOL_LABEL = "alcoholLabel";
	private static final String USE_CASE_3_PARAM_SPIRIT_TYPE = "foodSubtype";

	private static final String USE_CASE_WHITE_REF_PARAM_1 = "timestamp";
	private static final String USE_CASE_TEST_LIGHTS_ON_DURATION = "lightsOnDuration";

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
	@BindView(R.id.packageParam)
	EditText packageParam;

	@BindView(R.id.uc3paramOnlyAdulterationSampleId)
	LinearLayout uc3paramOnlyAdulterationSampleId;

	@BindView(R.id.adulterationSampleIdOnly)
	EditText adulterationSampleIdOnly;

	@BindView(R.id.uc3paramSpirits)
	LinearLayout uc3paramSpiritsHolder;

	//spirits only (label + type)
	@BindView(R.id.alcoholLabel)
	EditText alcoholLabel;

	@BindView(R.id.spiritType)
	EditText spiritType;

	//param 1 - Alcoholic beverages
	@BindView(R.id.uc3param1)
	LinearLayout uc3param1Holder;
	@BindView(R.id.adulterationSampleID)
	EditText adulterationSampleID;
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

	//param 2 - Edible oils
	@BindView(R.id.uc3param2)
	LinearLayout uc3param2Holder;
	@BindView(R.id.adulterationSampleIDParam2)
	EditText adulterationSampleIDParam2;
	@BindView(R.id.dilution1)
	EditText dilution1;
	@BindView(R.id.dilution1Percent)
	EditText dilution1Percent;
	@BindView(R.id.dilution2)
	EditText dilution2;
	@BindView(R.id.dilution2Percent)
	EditText dilution2Percent;

	//param 3 - Skimmed milk powder
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

	//param 4 - Minced raw meat
	@BindView(R.id.uc3param4)
	LinearLayout uc3param4Holder;
	@BindView(R.id.adulterationSampleIDParam4)
	EditText adulterationSampleIDParam4;
	@BindView(R.id.otherSpecies)
	EditText otherSpecies;
	@BindView(R.id.authenticParam)
	EditText authenticParam;

	//camera configuration
	@BindView(R.id.captureImageWhiteRadioGroup)
	RadioGroup captureImageWhiteRadioGroup;
	@BindView(R.id.captureImageUVRadioGroup)
	RadioGroup captureImageUVRadioGroup;
	@BindView(R.id.captureImageNIRRadioGroup)
	RadioGroup captureImageNIRRadioGroup;
	@BindView(R.id.exposureTimeWhiteLEDs)
	EditText cameraExposureTimeWhiteLEDs;
	@BindView(R.id.exposureTimeUV)
	EditText cameraExposureTimeUV;
	@BindView(R.id.exposureTimeNIR)
	EditText cameraExposureTimeNIR;

	@BindView(R.id.exposureTimeWhiteLEDsHolder)
	LinearLayout cameraExposureTimeWhiteLEDsHolder;
	@BindView(R.id.exposureTimeUVHolder)
	LinearLayout cameraExposureTimeUVHolder;
	@BindView(R.id.exposureTimeNIRHolder)
	LinearLayout cameraExposureTimeNIRHolder;

	//test configuration
	@BindView(R.id.useCaseTestLabelsGroup)
	LinearLayout useCaseTestLabelsGroup;
	@BindView(R.id.lightsOnDuration)
	EditText lightsOnDuration;

	// NIR
	@BindView(R.id.nirGroup)
	LinearLayout nirGroup;
	@BindView(R.id.singleShotRadioGroup)
	RadioGroup singleShotRadioGroup;
	@BindView(R.id.averagesEditText)
	EditText nirSpectrometerAveragesEditText;

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

		boolean areAllInputsValid = true;

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
				wizardJsonObject.put(USE_CASE_2_PARAM_4, packageParam.getText().toString());
				break;
			case Constants.USE_CASE_3:
				final String useCase3Param = wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE);
				switch (useCase3Param) {
					case USE_CASE_3_PARAM_1:
						if (!isAuthenticNone()) {
							wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleID.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_DILUTED_PCT, diluted.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_1_NAME, hazard1.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_1_PCT, hazard1Percent.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_2_NAME, hazard2.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_2_PCT, hazard2Percent.getText().toString());
						} else {
							wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleIdOnly.getText().toString());
						}

						if (wizardJsonObject.getString(Constants.USE_CASE_3_PARAM_1).equals("Spirits")) {
							wizardJsonObject.put(USE_CASE_3_PARAM_ALCOHOL_LABEL, alcoholLabel.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_SPIRIT_TYPE, spiritType.getText().toString());
						}
						break;
					case Constants.USE_CASE_3_PARAM_2:
						if (!isAuthenticNone()) {
							wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleIDParam2.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_1_NAME, dilution1.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_1_PCT, dilution1Percent.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_2_NAME, dilution2.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_HAZARD_2_PCT, dilution2Percent.getText().toString());
						} else {
							wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleIdOnly.getText().toString());
						}
						break;
					case Constants.USE_CASE_3_PARAM_3:
						if (!isAuthenticNone()) {
							wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleIDParam3.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_PURITY_SMP, purity.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_LOW_VALUE_FILLER, lowValueFiller.getText().toString());
							wizardJsonObject.put(USE_CASE_3_PARAM_NITROGEN_ENHANCER, nitrogenEnhancer.getText().toString());
						} else {
							wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleIdOnly.getText().toString());
						}
						break;
					case Constants.USE_CASE_3_PARAM_4:
						wizardJsonObject.put(USE_CASE_3_PARAM_ADULTERATION_SAMPLE_ID, adulterationSampleIDParam4.getText().toString());
						wizardJsonObject.put(USE_CASE_3_PARAM_OTHER_SPECIES, otherSpecies.getText().toString());
						wizardJsonObject.put(USE_CASE_3_PARAM_AUTHENTIC, authenticParam.getText().toString());
						break;
				}
				final String sample = wizardJsonObject.getString(Constants.USE_CASE_3_PARAM_SAMPLE);
				if (sample != null && !sample.isEmpty()) {
					if (sample.equals(Constants.USE_CASE_3_PARAM_AUTHENTIC)) {
						wizardJsonObject.put(USE_CASE_3_PARAM_AUTHENTIC, "yes");
					} else if (sample.equals(Constants.USE_CASE_3_PARAM_ADULTERATED)) {
						wizardJsonObject.put(USE_CASE_3_PARAM_AUTHENTIC, "no");
					} else {
						wizardJsonObject.put(USE_CASE_3_PARAM_AUTHENTIC, "none");
					}
				}
				Log.d(TAG, "sendRequest: SAMPLE: " + wizardJsonObject.toString());
				break;
			case Constants.USE_CASE_WHITE_REF:
				wizardJsonObject.put(USE_CASE_WHITE_REF_PARAM_1, String.valueOf(new Date().getTime()));
				break;
			case Constants.USE_CASE_TEST:
				if (!isWithinBounds(getValue(lightsOnDuration), MIN_LIGHTS_ON_DURATION, MAX_LIGHTS_ON_DURATION)) {
					showError(lightsOnDuration);
					areAllInputsValid = false;
					break;
				}
				wizardJsonObject.put(USE_CASE_TEST_LIGHTS_ON_DURATION, lightsOnDuration.getText().toString());
				break;
		}

		if (!areAllInputsValid) {
			return; //some of the input values are not okay, do not send request
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

	private boolean isAuthenticNone() {
		final String sample;
		try {
			sample = wizardJsonObject.getString(Constants.USE_CASE_3_PARAM_SAMPLE);
			if (sample != null && !sample.isEmpty()) {
				if (!sample.equals(Constants.USE_CASE_3_PARAM_AUTHENTIC) &&
					!sample.equals(Constants.USE_CASE_3_PARAM_ADULTERATED)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return false;
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
						case USE_CASE_3_PARAM_1:
							if (!isAuthenticNone()) {
								uc3param1Holder.setVisibility(View.VISIBLE);
							} else {
								uc3paramOnlyAdulterationSampleId.setVisibility(View.VISIBLE);
							}
							if (wizardJsonObject.getString(Constants.USE_CASE_3_PARAM_1).equals("Spirits")) {
								uc3paramSpiritsHolder.setVisibility(View.VISIBLE);
							}
							break;
						case Constants.USE_CASE_3_PARAM_2:
							if (!isAuthenticNone()) {
								uc3param2Holder.setVisibility(View.VISIBLE);
							} else {
								uc3paramOnlyAdulterationSampleId.setVisibility(View.VISIBLE);
							}
							break;
						case Constants.USE_CASE_3_PARAM_3:
							if (!isAuthenticNone()) {
								uc3param3Holder.setVisibility(View.VISIBLE);
							} else {
								uc3paramOnlyAdulterationSampleId.setVisibility(View.VISIBLE);
							}
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
					useCaseTestLabelsGroup.setVisibility(View.VISIBLE);

					cameraExposureTimeNIRHolder.setVisibility(View.GONE);
					cameraExposureTimeWhiteLEDsHolder.setVisibility(View.GONE);
					cameraExposureTimeUVHolder.setVisibility(View.GONE);

					if (testSample.contains(Constants.USE_CASE_TEST_NIR)) {
						cameraExposureTimeNIRHolder.setVisibility(View.VISIBLE);
						nirGroup.setVisibility(View.VISIBLE);
					}
					if (testSample.contains(Constants.USE_CASE_TEST_VIS)) {
						cameraExposureTimeWhiteLEDsHolder.setVisibility(View.VISIBLE);
						visGroup.setVisibility(View.VISIBLE);
					}
					if (testSample.contains(Constants.USE_CASE_TEST_FLUO)) {
						cameraExposureTimeUVHolder.setVisibility(View.VISIBLE);
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

		// load from preferences or set defaults
		setDefaults();
	}

	private void riseError(@NonNull final String error) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
		finish();
	}

	private void setDefaults() {

		microbiologicalUnit.setText(DEFAULT_MICROBIOLOGICAL_UNIT);
		int saveRadioButtonIndex = prefs.getInt(KEY_NIR_SINGLE_SHOT, 1); // 1 is no (N)
		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(saveRadioButtonIndex).getId());

		captureImageWhiteRadioGroup.check(R.id.noCaptureImageWhite);
		captureImageUVRadioGroup.check(R.id.noCaptureImageUV);
		captureImageNIRRadioGroup.check(R.id.noCaptureImageNIR);

		cameraExposureTimeWhiteLEDs.setText(String.valueOf(1000));
		cameraExposureTimeUV.setText(String.valueOf(1000));
		cameraExposureTimeNIR.setText(String.valueOf(1000));

		try {
			final String useCaseKey = wizardJsonObject.getString(Constants.USE_CASE_KEY);
			switch (useCaseKey) {
				case Constants.USE_CASE_1:
					//nir
					nirSpectrometerAveragesEditText.setText(String.valueOf(100));
					//vis
					exposureTimeReflectance.setText(String.valueOf(55));
					whiteLEDsCurrent.setText(String.valueOf(1));
					//fluo
					exposureTimeFluorescence.setText(String.valueOf(55));
					UVLEDsCurrent.setText(String.valueOf(15));
					break;
				case Constants.USE_CASE_2:
					//vis
					exposureTimeReflectance.setText(String.valueOf(55));
					whiteLEDsCurrent.setText(String.valueOf(1));
					//nir
					nirSpectrometerAveragesEditText.setText(String.valueOf(100));
					//fluo
					if (wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE).contains("Minced pork")
						|| wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE).contains("Fish")
						|| wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE).contains("Ready to eat baby spinach")
						|| wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE).contains("Ready to eat rocket salad")) {
						exposureTimeFluorescence.setText(String.valueOf(100));
					} else { //pineapple
						exposureTimeFluorescence.setText(String.valueOf(55));
					}
					UVLEDsCurrent.setText(String.valueOf(10));
					break;
				case Constants.USE_CASE_3:
					final String useCase3Param = wizardJsonObject.getString(Constants.USE_CASE_3_FOOD_TYPE);
					switch (useCase3Param) {
						case Constants.USE_CASE_3_PARAM_1: //Alcoholic beverages
							nirSpectrometerAveragesEditText.setText(String.valueOf(255));
							exposureTimeFluorescence.setText(String.valueOf(30));
							UVLEDsCurrent.setText(String.valueOf(10));
							break;
						case Constants.USE_CASE_3_PARAM_2: //Edible oils
							nirSpectrometerAveragesEditText.setText(String.valueOf(255));
							exposureTimeFluorescence.setText(String.valueOf(55));
							UVLEDsCurrent.setText(String.valueOf(30));
							break;
						case Constants.USE_CASE_3_PARAM_3: // skimmed milk powder
							nirSpectrometerAveragesEditText.setText(String.valueOf(255));
							exposureTimeFluorescence.setText(String.valueOf(55));
							UVLEDsCurrent.setText(String.valueOf(8));
							break;
						case Constants.USE_CASE_3_PARAM_4: //Minced raw meat
							nirSpectrometerAveragesEditText.setText(String.valueOf(100));
							exposureTimeFluorescence.setText(String.valueOf(100));
							UVLEDsCurrent.setText(String.valueOf(10));
							break;
					}
					//vis
					exposureTimeReflectance.setText(String.valueOf(55));
					whiteLEDsCurrent.setText(String.valueOf(1));
					break;
				default:
					//nir
					nirSpectrometerAveragesEditText.setText(String.valueOf(100));
					// vis
					exposureTimeReflectance.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE));
					whiteLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_WHITE_LEDS_CURRENT));
					// fluo
					exposureTimeFluorescence.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE));
					UVLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_UV_LEDS_VOLTAGE));
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			riseError(e.getMessage());
		}
	}

//	private void setDefaults() {
//		//use case 2
//		microbiologicalUnit.setText(DEFAULT_MICROBIOLOGICAL_UNIT);
//		// camera
//		cameraExposureTime.setText(String.valueOf(DEFAULT_CAMERA_EXPOSURE_TIME));
//		cameraVoltage.setText(String.valueOf(DEFAULT_CAMERA_VOLTAGE));
//		// nir
//		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(1).getId());
//		nirSpectrometerAveragesEditText.setText(String.valueOf(DEFAULT_NIR_SPEC_AVERAGES));
//		nirMicrolampsCurrent.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_CURRENT));
//		nirMicrolampsWarmingTime.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_WARMING_TIME));
//		// vis
//		exposureTimeReflectance.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE));
//		whiteLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_WHITE_LEDS_CURRENT));
//		//fluo
//		exposureTimeFluorescence.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE));
//		UVLEDsCurrent.setText(String.valueOf(DEFAULT_VIS_UV_LEDS_VOLTAGE));
//	}

	private void saveParamsState() {
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(KEY_CAMERA_EXPOSURE_TIME_WHITE_LEDS, getValue(cameraExposureTimeWhiteLEDs));
		editor.putInt(KEY_CAMERA_EXPOSURE_TIME_UV, getValue(cameraExposureTimeUV));
		editor.putInt(KEY_CAMERA_EXPOSURE_TIME_NIR, getValue(cameraExposureTimeNIR));

		final int radioButtonID = singleShotRadioGroup.getCheckedRadioButtonId();
		final View radioButton = singleShotRadioGroup.findViewById(radioButtonID);
		final int idx = singleShotRadioGroup.indexOfChild(radioButton);
		editor.putInt(KEY_NIR_SINGLE_SHOT, idx);
		editor.putInt(KEY_NIR_SPEC_AVERAGES, getValue(nirSpectrometerAveragesEditText));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_REFLECTANCE, getValue(exposureTimeReflectance));
		editor.putInt(KEY_VIS_UV_LEDS_VOLTAGE, getValue(UVLEDsCurrent));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, getValue(exposureTimeFluorescence));
		editor.putInt(KEY_VIS_WHITE_LEDS_VOLTAGE, getValue(whiteLEDsCurrent));

		editor.apply();
	}

	private Configuration getConfiguration() {
		if (!isWithinBounds(getValue(cameraExposureTimeWhiteLEDs), MIN_CAMERA_EXPOSURE_TIME, Integer.MAX_VALUE)) {
			showError(cameraExposureTimeWhiteLEDs);
			return null;
		}
		if (!isWithinBounds(getValue(cameraExposureTimeUV), MIN_CAMERA_EXPOSURE_TIME, Integer.MAX_VALUE)) {
			showError(cameraExposureTimeUV);
			return null;
		}
		if (!isWithinBounds(getValue(cameraExposureTimeNIR), MIN_CAMERA_EXPOSURE_TIME, Integer.MAX_VALUE)) {
			showError(cameraExposureTimeNIR);
			return null;
		}
		if (!isWithinBounds(getValue(nirSpectrometerAveragesEditText), MIN_NIR_SPEC_AVERAGES, MAX_NIR_SPEC_AVERAGES)) {
			showError(nirSpectrometerAveragesEditText);
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

		final String selectedCaptureOptionWhite = captureImageWhiteRadioGroup.getCheckedRadioButtonId() ==
			R.id.yesCaptureImageWhite ? "YES" : "";

		final String selectedCaptureOptionUV = captureImageUVRadioGroup.getCheckedRadioButtonId() ==
			R.id.yesCaptureImageUV ? "YES" : "";

		final String selectedCaptureOptionNIR = captureImageNIRRadioGroup.getCheckedRadioButtonId() ==
			R.id.yesCaptureImageNIR ? "YES" : "";

		// Camera
		final Camera camera = new Camera(
			selectedCaptureOptionWhite,
			selectedCaptureOptionUV,
			selectedCaptureOptionNIR,
			getValue(cameraExposureTimeWhiteLEDs),
			getValue(cameraExposureTimeUV),
			getValue(cameraExposureTimeNIR)
		);
		configuration.setCamera(camera);

		boolean isNirAvailable = false;
		boolean isVisAvailable = false;
		boolean isFluoAvailable = false;

		// NirMicrolamps
		// Add if not test sample at all OR it's a NIR test sample
		if (testSample == null || testSample.contains(Constants.USE_CASE_TEST_NIR)) {
			final String selectedOption = singleShotRadioGroup.getCheckedRadioButtonId() == R.id.yes ? "Y" : "N";
			final NirSpectrometer nirSpectrometer = new NirSpectrometer(selectedOption, getValue(nirSpectrometerAveragesEditText));
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
			configuration.setNirSpectrometer(new NirSpectrometer("N", 0));
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
			try {
				return Integer.parseInt(editText.getText().toString());
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1; //value not added (empty field)
	}

	private void showError(EditText editText) {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		editText.startAnimation(shake);
	}
}

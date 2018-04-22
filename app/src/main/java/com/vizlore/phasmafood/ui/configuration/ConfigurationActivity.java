package com.vizlore.phasmafood.ui.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.bluetooth.BluetoothService;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vizlore.phasmafood.utils.Config.DEFAULT_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_CAMERA_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_MICROLAMPS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_BINNING_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_GAIN_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.DEFAULT_VIS_WHITE_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.KEY_CAMERA_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.KEY_NIR_MICROLAMPS_VOLTAGE;
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
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_MICROLAMPS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.MAX_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_BINNING_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_GAIN_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MAX_VIS_WHITE_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_EXPOSURE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_CAMERA_VOLTAGE_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_MICROLAMPS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_MICROLAMPS_WARMING_TIME;
import static com.vizlore.phasmafood.utils.Config.MIN_NIR_SPEC_AVERAGES;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_BINNING_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_BINNING_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_EXPOSURE_TIME_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_GAIN_FLUORESCENCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_GAIN_REFLECTANCE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_UV_LEDS_VOLTAGE;
import static com.vizlore.phasmafood.utils.Config.MIN_VIS_WHITE_LEDS_VOLTAGE;

public class ConfigurationActivity extends Activity {

	private static final String TAG = "SMEDIC";

	private BluetoothService bluetoothService;

	@Inject
	SharedPreferences prefs;

	//camera configuration
	@BindView(R.id.exposureTime)
	EditText cameraExposureTime;
	@BindView(R.id.drivingVoltage)
	EditText cameraVoltage;

	// NIR
	@BindView(R.id.singleShotRadioGroup)
	RadioGroup singleShotRadioGroup;
	@BindView(R.id.averagesEditText)
	EditText averagesEditText;
	@BindView(R.id.nirMicrolampsVoltage)
	EditText nirMicrolampsVoltage;
	@BindView(R.id.nirMicrolampsWarmingTime)
	EditText nirMicrolampsWarmingTime;

	// VIS
	@BindView(R.id.exposureTimeReflectance)
	EditText exposureTimeReflectance;
	@BindView(R.id.gainReflectance)
	EditText gainReflectance;
	@BindView(R.id.binningReflectance)
	EditText binningReflectance;
	@BindView(R.id.whiteLEDs)
	EditText whiteLEDsVoltage;

	@BindView(R.id.exposureTimeFluorescence)
	EditText exposureTimeFluorescence;
	@BindView(R.id.gainFluorescence)
	EditText gainFluorescence;
	@BindView(R.id.binningFluorescence)
	EditText binningFluorescence;
	@BindView(R.id.UVLEDs)
	EditText UVLEDsVoltage;

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
				// TODO: 4/21/18 receive json object
				if (getIntent() != null && getIntent().getExtras() != null
					&& getIntent().getExtras().containsKey("wizard_data")) {
					final String json = getIntent().getExtras().getString("wizard_data");
					try {
						final JSONObject jsonObject = new JSONObject(json);
						//add config parameters to json request (camera, nir, vis)
						if (getConfigurationParams() != null) {
							saveParamsState(); //save new values to shared preferences
							jsonObject.put("configuration", getConfigurationParams());
							//wrap into request
							JSONObject jsonObjectRequest = new JSONObject();
							jsonObjectRequest.put("Request", jsonObject);

							Log.d(TAG, "onClick: JSON TO SEND: " + jsonObject.toString());
							showSendActionDialog(jsonObjectRequest.toString());
						} else {
							Toast.makeText(ConfigurationActivity.this, "Wrong input. Check fields", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
		}
	}

	private void showSendActionDialog(final String jsonToSend) {
		AlertDialog alertDialog = new AlertDialog.Builder(ConfigurationActivity.this).create();
		alertDialog.setMessage(getString(R.string.sendDataMessage));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.yes), (dialog, which) -> {
			//bluetoothService.sendMessage(jsonToSend);
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
		MyApplication.getComponent().inject(this);

		// load from preferences or set defaults
		loadStartingValues();

		Intent intent = new Intent(this, BluetoothService.class);
		//startService(intent); //Starting the service
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//stopService(new Intent(this, BluetoothService.class));
		if (bluetoothService != null && connection != null) {
			bluetoothService.closeConnection();
		}
	}

	private void loadStartingValues() {
		// camera
		cameraExposureTime.setText(String.valueOf(prefs.getInt(KEY_CAMERA_EXPOSURE_TIME, DEFAULT_CAMERA_EXPOSURE_TIME)));
		cameraVoltage.setText(String.valueOf(prefs.getInt(KEY_CAMERA_VOLTAGE, DEFAULT_CAMERA_VOLTAGE)));
		// nir
		int saveRadioButtonIndex = prefs.getInt(KEY_NIR_SINGLE_SHOT, 1); // 1 is no (N)
		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(saveRadioButtonIndex).getId());
		averagesEditText.setText(String.valueOf(prefs.getInt(KEY_NIR_SPEC_AVERAGES, DEFAULT_NIR_SPEC_AVERAGES)));
		nirMicrolampsVoltage.setText(String.valueOf(prefs.getInt(KEY_NIR_MICROLAMPS_VOLTAGE, DEFAULT_NIR_MICROLAMPS_VOLTAGE)));
		nirMicrolampsWarmingTime.setText(String.valueOf(prefs.getInt(KEY_NIR_MICROLAMPS_WARMING_TIME, DEFAULT_NIR_MICROLAMPS_WARMING_TIME)));
		// vis
		exposureTimeReflectance.setText(String.valueOf(prefs.getInt(KEY_VIS_EXPOSURE_TIME_REFLECTANCE, DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE)));
		gainReflectance.setText(String.valueOf(prefs.getInt(KEY_VIS_GAIN_REFLECTANCE, DEFAULT_VIS_GAIN_REFLECTANCE)));
		binningReflectance.setText(String.valueOf(prefs.getInt(KEY_VIS_BINNING_REFLECTANCE, DEFAULT_VIS_BINNING_REFLECTANCE)));
		whiteLEDsVoltage.setText(String.valueOf(prefs.getInt(KEY_VIS_WHITE_LEDS_VOLTAGE, DEFAULT_VIS_WHITE_LEDS_VOLTAGE)));
		exposureTimeFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE)));
		gainFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_GAIN_FLOURESCENCE, DEFAULT_VIS_GAIN_FLUORESCENCE)));
		binningFluorescence.setText(String.valueOf(prefs.getInt(KEY_VIS_BINNING_FLOURESCENCE, DEFAULT_VIS_BINNING_FLUORESCENCE)));
		UVLEDsVoltage.setText(String.valueOf(prefs.getInt(KEY_VIS_UV_LEDS_VOLTAGE, DEFAULT_VIS_UV_LEDS_VOLTAGE)));
	}

	private void setDefaults() {
		// camera
		cameraExposureTime.setText(String.valueOf(DEFAULT_CAMERA_EXPOSURE_TIME));
		cameraVoltage.setText(String.valueOf(DEFAULT_CAMERA_VOLTAGE));
		// nir
		singleShotRadioGroup.check(singleShotRadioGroup.getChildAt(1).getId()); //no is default // TODO: 4/22/18

		averagesEditText.setText(String.valueOf(DEFAULT_NIR_SPEC_AVERAGES));
		nirMicrolampsVoltage.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_VOLTAGE));
		nirMicrolampsWarmingTime.setText(String.valueOf(DEFAULT_NIR_MICROLAMPS_WARMING_TIME));
		// vis
		exposureTimeReflectance.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE));
		gainReflectance.setText(String.valueOf(DEFAULT_VIS_GAIN_REFLECTANCE));
		binningReflectance.setText(String.valueOf(DEFAULT_VIS_BINNING_REFLECTANCE));
		whiteLEDsVoltage.setText(String.valueOf(DEFAULT_VIS_WHITE_LEDS_VOLTAGE));
		exposureTimeFluorescence.setText(String.valueOf(DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE));
		gainFluorescence.setText(String.valueOf(DEFAULT_VIS_GAIN_FLUORESCENCE));
		binningFluorescence.setText(String.valueOf(DEFAULT_VIS_BINNING_FLUORESCENCE));
		UVLEDsVoltage.setText(String.valueOf(DEFAULT_VIS_UV_LEDS_VOLTAGE));
	}

	private void saveParamsState() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(KEY_CAMERA_EXPOSURE_TIME, getValue(cameraExposureTime));
		editor.putInt(KEY_CAMERA_VOLTAGE, getValue(cameraVoltage));

		int radioButtonID = singleShotRadioGroup.getCheckedRadioButtonId();
		View radioButton = singleShotRadioGroup.findViewById(radioButtonID);
		int idx = singleShotRadioGroup.indexOfChild(radioButton);
		editor.putInt(KEY_NIR_SINGLE_SHOT, idx);
		editor.putInt(KEY_NIR_SPEC_AVERAGES, getValue(averagesEditText));
		editor.putInt(KEY_NIR_MICROLAMPS_VOLTAGE, getValue(nirMicrolampsVoltage));
		editor.putInt(KEY_NIR_MICROLAMPS_WARMING_TIME, getValue(nirMicrolampsWarmingTime));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_REFLECTANCE, getValue(exposureTimeReflectance));
		editor.putInt(KEY_VIS_GAIN_REFLECTANCE, getValue(gainReflectance));
		editor.putInt(KEY_VIS_BINNING_REFLECTANCE, getValue(binningReflectance));
		editor.putInt(KEY_VIS_UV_LEDS_VOLTAGE, getValue(UVLEDsVoltage));

		editor.putInt(KEY_VIS_EXPOSURE_TIME_FLOURESCENCE, getValue(exposureTimeFluorescence));
		editor.putInt(KEY_VIS_GAIN_FLOURESCENCE, getValue(gainFluorescence));
		editor.putInt(KEY_VIS_BINNING_FLOURESCENCE, getValue(binningFluorescence));
		editor.putInt(KEY_VIS_WHITE_LEDS_VOLTAGE, getValue(whiteLEDsVoltage));

		editor.apply();
	}

	private JSONObject getConfigurationParams() throws JSONException {
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
		if (!isWithinBounds(getValue(nirMicrolampsVoltage), MIN_NIR_MICROLAMPS_VOLTAGE, MAX_NIR_MICROLAMPS_VOLTAGE)) {
			showError(nirMicrolampsVoltage);
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
		if (!isWithinBounds(getValue(whiteLEDsVoltage), MIN_VIS_WHITE_LEDS_VOLTAGE, MAX_VIS_WHITE_LEDS_VOLTAGE)) {
			showError(whiteLEDsVoltage);
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
		if (!isWithinBounds(getValue(UVLEDsVoltage), MIN_VIS_UV_LEDS_VOLTAGE, MAX_VIS_UV_LEDS_VOLTAGE)) {
			showError(UVLEDsVoltage);
			return null;
		}

		final JSONObject jsonObject = new JSONObject();

		// Camera json object
		final JSONObject cameraJsonObject = new JSONObject();
		cameraJsonObject.put("t_cam", getValue(cameraExposureTime));
		cameraJsonObject.put("vw_cam", getValue(cameraVoltage));
		jsonObject.put("camera", cameraJsonObject);

		// NIR
		final JSONObject nirJsonObject = new JSONObject();
		String selectedRadioButtonValue;
		if (singleShotRadioGroup.getCheckedRadioButtonId() == R.id.yes) {
			selectedRadioButtonValue = "Y";
		} else {
			selectedRadioButtonValue = "N";
		}
		nirJsonObject.put("single", selectedRadioButtonValue);
		nirJsonObject.put("av_NIR", getValue(averagesEditText));
		final JSONObject nirMicrolampsJsonObject = new JSONObject();
		nirMicrolampsJsonObject.put("V_nir", getValue(nirMicrolampsVoltage));
		nirMicrolampsJsonObject.put("t_nir", getValue(nirMicrolampsWarmingTime));
		nirJsonObject.put("NirMicrolamps", nirMicrolampsJsonObject);
		jsonObject.put("NirSpectrometer", nirJsonObject);

		// VIS
		final JSONObject visJsonObject = new JSONObject();
		visJsonObject.put("t_vis", getValue(exposureTimeReflectance));
		visJsonObject.put("g_vis", getValue(gainReflectance));
		visJsonObject.put("b_vis", getValue(binningReflectance));
		visJsonObject.put("t_fluo", getValue(exposureTimeFluorescence));
		visJsonObject.put("g_fluo", getValue(gainFluorescence));
		visJsonObject.put("b_fluo", getValue(binningFluorescence));
		final JSONObject ledsJsonObject = new JSONObject();
		ledsJsonObject.put("Vw_vis", getValue(whiteLEDsVoltage));
		ledsJsonObject.put("V_UV", getValue(UVLEDsVoltage));
		visJsonObject.put("visLeds", ledsJsonObject);
		jsonObject.put("VisSpectrometer", visJsonObject);

		return jsonObject;
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

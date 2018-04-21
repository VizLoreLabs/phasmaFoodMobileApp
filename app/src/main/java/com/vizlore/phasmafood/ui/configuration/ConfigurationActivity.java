package com.vizlore.phasmafood.ui.configuration;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.vizlore.phasmafood.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationActivity extends Activity {

	//camera configuration
	@BindView(R.id.exposureTime)
	EditText exposureTime;
	@BindView(R.id.drivingVoltage)
	EditText drivingVoltage;

	// NIR
	@BindView(R.id.singleShotRadioGroup)
	RadioGroup singleShotRadioGroup;
	@BindView(R.id.averagesEditText)
	EditText averagesEditText;
	@BindView(R.id.voltageEditText)
	EditText voltageEditText;
	@BindView(R.id.warmingTimeEditText)
	EditText warmingTimeEditText;

	// VIS
	@BindView(R.id.exposureTimeVisibleReflectance)
	EditText exposureTimeVisibleReflectance;
	@BindView(R.id.gainVisibleReflectance)
	EditText gainVisibleReflectance;
	@BindView(R.id.binningVisibleReflectance)
	EditText binningVisibleReflectance;
	@BindView(R.id.whiteLEDs)
	EditText whiteLEDs;

	@BindView(R.id.exposureTimeFluorescence)
	EditText exposureTimeFluorescence;
	@BindView(R.id.gainFluorescence)
	EditText gainFluorescence;
	@BindView(R.id.binningFluorescence)
	EditText binningFluorescence;
	@BindView(R.id.UVLEDs)
	EditText UVLEDs;

	@OnClick({R.id.backButton, R.id.sendRequest})
	void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.sendRequest:
				// TODO: 4/21/18
				break;
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		ButterKnife.bind(this);
	}
}

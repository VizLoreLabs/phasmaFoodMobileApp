package com.vizlore.phasmafood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.results.Examination;
import com.vizlore.phasmafood.model.results.Sample;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 3/2/18.
 */

public class ResultsActivity extends BaseActivity {

	@BindView(R.id.visValue)
	TextView visValue;

	@BindView(R.id.nirValue)
	TextView nirValue;

	@BindView(R.id.flouValue)
	TextView flouTitle;

	@BindView(R.id.useCaseValue)
	TextView useCaseValue;

	@BindView(R.id.sampleValue)
	TextView sampleValue;

	@BindView(R.id.param1Value)
	TextView param1Value;

	@BindView(R.id.param2Value)
	TextView param2Value;

	@BindView(R.id.param3Value)
	TextView param3Value;

	@BindView(R.id.param4Value)
	TextView param4Value;

	//keys changed for testing purposes // FIXME: 3/5/18
	@BindView(R.id.param1Title)
	TextView param1Title;
	@BindView(R.id.param2Title)
	TextView param2Title;
	@BindView(R.id.param3Title)
	TextView param3Title;
	@BindView(R.id.param4Title)
	TextView param4Title;


	@OnClick(R.id.done)
	void onDoneClick() {
		finish();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		ButterKnife.bind(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			visValue.setText(bundle.getString("VIS"));
			nirValue.setText(bundle.getString("NIR"));
			flouTitle.setText(bundle.getString("FLOU"));

			Examination examination = MyApplication.getInstance().getExamination();
			if (examination != null) {
				Sample sample = examination.getResponse().getSample();
				useCaseValue.setText(sample.getUseCase());
				sampleValue.setText(sample.getFoodType());

				if (sample.getGranularity() != null) {
					param1Title.setText("Granularity:");
					param2Title.setText("Mycotoxins:");
					param3Title.setText("Aflatoxin unit:");
					param4Title.setText("Contamination:");
					param1Value.setText(sample.getGranularity());
					param2Value.setText(sample.getMycotoxins());
					param3Value.setText(sample.getAflatoxinUnit());
					param4Value.setText(sample.getContamination());
				} else {
					param1Title.setText("Temperature:");
					param2Title.setText("Temp exposure hours:");
					param3Title.setText("Microbiological unit:");
					param4Title.setText("Microbiological value:");
					param1Value.setText(sample.getTemperature());
					param2Value.setText(sample.getTempExposureHours());
					param3Value.setText(sample.getMicrobiologicalUnit());
					param4Value.setText(sample.getMicrobiologicalValue());
				}
			}
		}
	}
}

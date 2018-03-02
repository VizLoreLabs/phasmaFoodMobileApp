package com.vizlore.phasmafood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.results.Examination;

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

	@BindView(R.id.granularityValue)
	TextView granularityValue;

	@BindView(R.id.exposureTimeValue)
	TextView exposureTimeValue;

	@BindView(R.id.storageTemperatureValue)
	TextView storageTemperatureValue;

	@BindView(R.id.datasetLabelValue)
	TextView datasetLabelValue;

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
			visValue.setText(bundle.getString("vis"));
			nirValue.setText(bundle.getString("nir"));
			flouTitle.setText(bundle.getString("flou"));

			Examination examination = MyApplication.getInstance().getExamination();
			if (examination != null) {
				useCaseValue.setText(examination.getUseCase());
				sampleValue.setText(examination.getFoodType());
				granularityValue.setText(examination.getGranularity());
				exposureTimeValue.setText("time");
				storageTemperatureValue.setText("temp");
				datasetLabelValue.setText(examination.getMycotoxins());
			}
		}
	}
}

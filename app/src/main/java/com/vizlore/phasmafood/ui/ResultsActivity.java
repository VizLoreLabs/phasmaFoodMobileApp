package com.vizlore.phasmafood.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.results.AverageAbsorbance;
import com.vizlore.phasmafood.model.results.Examination;
import com.vizlore.phasmafood.model.results.Preprocessed;
import com.vizlore.phasmafood.model.results.Sample;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 3/2/18.
 */

public class ResultsActivity extends BaseActivity {

	private LineDataSet dataSet;
	private LineDataSet dataSet2;

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

	@BindView(R.id.chart)
	LineChart lineChart;

	@OnClick(R.id.backButton)
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

				List<Entry> entriesVIS = new ArrayList<>();
				List<Entry> entriesNIR = new ArrayList<>();

				List<Preprocessed> preprocessedList = sample.getVIS().getPreprocessed();
				List<AverageAbsorbance> averageAbsorbanceList = sample.getNIR().getAverageAbsorbance();

				for (int i = 0; i < preprocessedList.size(); i++) {
					float wave = Float.parseFloat(preprocessedList.get(i).getWave());
					float measurement = Float.parseFloat(preprocessedList.get(i).getMeasurement());
					entriesVIS.add(new Entry(wave, measurement));
				}
				for (int i = 0; i < averageAbsorbanceList.size(); i++) {
					float wave = Float.parseFloat(averageAbsorbanceList.get(i).getWave());
					float measurement = Float.parseFloat(averageAbsorbanceList.get(i).getMeasurement());
					entriesNIR.add(new Entry(wave, measurement));
				}

				dataSet = new LineDataSet(entriesVIS, "VIS");
				dataSet2 = new LineDataSet(entriesNIR, "NIR");
				dataSet.setColor(getResources().getColor(R.color.orange));
				dataSet.setCircleColor(getResources().getColor(R.color.orange));

				List<ILineDataSet> dataSets = new ArrayList<>();
				dataSets.add(dataSet);
				dataSets.add(dataSet2);

				lineChart.setData(new LineData(dataSet));

				lineChart.getXAxis().setTextColor(Color.WHITE);
				lineChart.getAxisLeft().setTextColor(Color.WHITE);
				lineChart.getAxisRight().setTextColor(Color.WHITE);
				lineChart.getLegend().setTextColor(Color.WHITE);
				lineChart.getDescription().setText("");
				lineChart.animateX(2000);
				lineChart.invalidate(); // refresh
			}
		}
	}
}

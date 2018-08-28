package com.vizlore.phasmafood.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
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

	private List<ILineDataSet> dataSets;
	private LineDataSet dataSetVIS;
	private LineDataSet dataSetNIR;

	boolean isVisDisplayed = true;
	boolean isNirDisplayed = true;

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

	@BindView(R.id.buttonVis)
	Button buttonVis;

	@BindView(R.id.buttonNir)
	Button buttonNir;

	@OnClick(R.id.buttonVis)
	void onMockClick1() {
		if (isVisDisplayed) {
			dataSets.remove(dataSetVIS);
			lineChart.clear();
			buttonVis.setSelected(false);
		} else {
			buttonVis.setSelected(true);
			dataSets.add(dataSetVIS);
		}
		lineChart.setData(new LineData(dataSets));
		isVisDisplayed = !isVisDisplayed;
		drawChart();
	}

	@OnClick(R.id.buttonNir)
	void onMockClick2() {
		if (isNirDisplayed) {
			dataSets.remove(dataSetNIR);
			lineChart.clear();
			buttonNir.setSelected(false);
		} else {
			buttonNir.setSelected(true);
			dataSets.add(dataSetNIR);
		}
		isNirDisplayed = !isNirDisplayed;
		lineChart.setData(new LineData(dataSets));
		drawChart();
	}

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

				final List<Entry> entriesVIS = new ArrayList<>();
				final List<Entry> entriesNIR = new ArrayList<>();

				final List<Preprocessed> preprocessedList = sample.getVIS().getPreprocessed();
				final List<AverageAbsorbance> averageAbsorbanceList = sample.getNIR().getAverageAbsorbance();

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

				dataSetVIS = new LineDataSet(entriesVIS, "VIS");
				dataSetVIS.setColor(getResources().getColor(R.color.orange));
				dataSetVIS.setCircleColor(getResources().getColor(R.color.orange));

				dataSetNIR = new LineDataSet(entriesNIR, "NIR");
				dataSetNIR.setColor(getResources().getColor(R.color.blue));
				dataSetNIR.setCircleColor(getResources().getColor(R.color.blue));

				dataSets = new ArrayList<>();
				dataSets.add(dataSetVIS);
				dataSets.add(dataSetNIR);

				lineChart.setData(new LineData(dataSets));

				lineChart.getXAxis().setTextColor(Color.WHITE);
				lineChart.getAxisLeft().setTextColor(Color.WHITE);
				lineChart.getAxisRight().setTextColor(Color.WHITE);
				lineChart.getLegend().setEnabled(false);
				lineChart.getDescription().setText("");

				drawChart();

				//display both graphs (VIS/NIR)
				buttonVis.setSelected(true);
				buttonNir.setSelected(true);
			}
		}
	}

	private void drawChart() {
		lineChart.animateX(1000);
		lineChart.invalidate(); // refresh
	}
}

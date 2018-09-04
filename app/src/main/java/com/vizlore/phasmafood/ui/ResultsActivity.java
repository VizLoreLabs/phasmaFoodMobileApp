package com.vizlore.phasmafood.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.vizlore.phasmafood.model.results.Measurement;
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

	private List<ILineDataSet> dataSets = new ArrayList<>();
	private LineDataSet dataSetVIS;
	private LineDataSet dataSetNIR;
	private LineDataSet dataSetFLUO;

	boolean isVisDisplayed = true;
	boolean isNirDisplayed = true;
	boolean isFluoDisplayed = true;

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

	@BindView(R.id.buttonFluo)
	Button buttonFluo;

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

	@OnClick(R.id.buttonFluo)
	void onMockClick3() {
		if (isFluoDisplayed) {
			dataSets.remove(dataSetFLUO);
			lineChart.clear();
			buttonFluo.setSelected(false);
		} else {
			buttonFluo.setSelected(true);
			dataSets.add(dataSetFLUO);
		}
		isFluoDisplayed = !isFluoDisplayed;
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

			final Measurement measurement = MyApplication.getInstance().getMeasurement();
			if (measurement != null) {
				Sample sample = measurement.getResponse().getSample();
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

				dataSets = new ArrayList<>();

				if (sample.getVIS() != null && sample.getVIS().getPreprocessed() != null) {
					dataSetVIS = new LineDataSet(getPreprocessedEntries(sample.getVIS().getPreprocessed()), "VIS");
					dataSetVIS.setColor(getResources().getColor(R.color.orange));
					dataSetVIS.setCircleColor(getResources().getColor(R.color.orange));
					dataSets.add(dataSetVIS);
				}

				if (sample.getNIR() != null && sample.getNIR().getPreprocessed() != null) {
					dataSetNIR = new LineDataSet(getPreprocessedEntries(sample.getNIR().getPreprocessed()), "NIR");
					dataSetNIR.setColor(getResources().getColor(R.color.blue));
					dataSetNIR.setCircleColor(getResources().getColor(R.color.blue));
					dataSets.add(dataSetNIR);
				}

				if (sample.getFLUO() != null && sample.getFLUO().getPreprocessed() != null) {
					dataSetFLUO = new LineDataSet(getPreprocessedEntries(sample.getFLUO().getPreprocessed()), "FLUO");
					dataSetFLUO.setColor(getResources().getColor(R.color.green));
					dataSetFLUO.setCircleColor(getResources().getColor(R.color.green));
					dataSets.add(dataSetFLUO);
				}

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
				buttonFluo.setSelected(true);
			}
		}
	}

	private List<Entry> getPreprocessedEntries(@NonNull List<Preprocessed> preprocessedList) {
		final List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < preprocessedList.size(); i++) {
			float wave = Float.parseFloat(preprocessedList.get(i).getWave());
			float value = Float.parseFloat(preprocessedList.get(i).getMeasurement());
			entries.add(new Entry(wave, value));
		}
		return entries;
	}

	private void drawChart() {
		lineChart.animateX(1000);
		lineChart.invalidate(); // refresh
	}
}

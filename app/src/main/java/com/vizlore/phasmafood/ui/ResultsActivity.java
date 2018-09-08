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
import com.vizlore.phasmafood.model.results.DarkReference;
import com.vizlore.phasmafood.model.results.FLUO;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.NIR;
import com.vizlore.phasmafood.model.results.Preprocessed;
import com.vizlore.phasmafood.model.results.Sample;
import com.vizlore.phasmafood.model.results.VIS;
import com.vizlore.phasmafood.model.results.WhiteReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 3/2/18.
 */

public class ResultsActivity extends BaseActivity {

	private List<ILineDataSet> visDataSets = new ArrayList<>();
	private List<ILineDataSet> nirDataSets = new ArrayList<>();
	private List<ILineDataSet> fluoDataSets = new ArrayList<>();

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
		buttonVis.setSelected(true);
		buttonNir.setSelected(false);
		buttonFluo.setSelected(false);
		lineChart.setData(new LineData(visDataSets));
		drawChart();
	}

	@OnClick(R.id.buttonNir)
	void onMockClick2() {
		buttonVis.setSelected(false);
		buttonNir.setSelected(true);
		buttonFluo.setSelected(false);
		lineChart.setData(new LineData(nirDataSets));
		drawChart();
	}

	@OnClick(R.id.buttonFluo)
	void onMockClick3() {
		buttonNir.setSelected(false);
		buttonVis.setSelected(false);
		buttonFluo.setSelected(true);
		lineChart.setData(new LineData(fluoDataSets));
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

		final Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			visValue.setText(bundle.getString("VIS"));
			nirValue.setText(bundle.getString("NIR"));
			flouTitle.setText(bundle.getString("FLOU"));

			final Measurement measurement = MyApplication.getInstance().getMeasurement();
			if (measurement != null) {
				final Sample sample = measurement.getResponse().getSample();
				if (sample == null) { //keep safe
					return;
				}

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

				final VIS vis = sample.getVIS();
				final NIR nir = sample.getNIR();
				final FLUO fluo = sample.getFLUO();

				if (vis != null) {
					visDataSets.addAll(getEntries(vis.getPreprocessed(), vis.getDarkReference(), vis.getWhiteReference()));
				}

				if (nir != null) {
					nirDataSets.addAll(getEntries(nir.getPreprocessed(), nir.getDarkReference(), nir.getWhiteReference()));
				}

				if (fluo != null) {
					fluoDataSets.addAll(getEntries(fluo.getPreprocessed(), fluo.getDarkReference(), fluo.getWhiteReference()));
				}

				//show VIS by default
				lineChart.setData(new LineData(visDataSets));

				lineChart.getXAxis().setTextColor(Color.WHITE);
				lineChart.getAxisLeft().setTextColor(Color.WHITE);
				lineChart.getAxisRight().setTextColor(Color.WHITE);
				//lineChart.getLegend().setEnabled(false);
				lineChart.getDescription().setText("");
				lineChart.setScaleEnabled(false);

				drawChart();

				//display VIS on start
				buttonVis.setSelected(true);
			}
		}
	}

	private List<ILineDataSet> getEntries(final List<Preprocessed> preprocessedList,
										  final List<DarkReference> darkReferenceList,
										  final List<WhiteReference> whiteReferenceList) {

		final List<ILineDataSet> dataSets = new ArrayList<>();

		if (preprocessedList != null) {
			final LineDataSet dataSetVIS = new LineDataSet(getPreprocessedEntries(preprocessedList), "preprocessed");
			dataSetVIS.setColor(getResources().getColor(R.color.orange));
			dataSetVIS.setCircleColor(getResources().getColor(R.color.orange));
			dataSets.add(dataSetVIS);
		}

		if (darkReferenceList != null) {
			final LineDataSet dataSetVIS = new LineDataSet(getDarkReferenceEntries(darkReferenceList), "dark");
			dataSetVIS.setColor(getResources().getColor(R.color.black));
			dataSetVIS.setCircleColor(getResources().getColor(R.color.black));
			dataSets.add(dataSetVIS);
		}

		if (whiteReferenceList != null) {
			final LineDataSet dataSetVIS = new LineDataSet(getWhiteReferenceEntries(whiteReferenceList), "white");
			dataSetVIS.setColor(getResources().getColor(R.color.white));
			dataSetVIS.setCircleColor(getResources().getColor(R.color.white));
			dataSets.add(dataSetVIS);
		}
		return dataSets;
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

	private List<Entry> getDarkReferenceEntries(@NonNull List<DarkReference> darkReferences) {
		final List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < darkReferences.size(); i++) {
			float wave = Float.parseFloat(darkReferences.get(i).getWave());
			float value = Float.parseFloat(darkReferences.get(i).getMeasurement());
			entries.add(new Entry(wave, value));
		}
		return entries;
	}

	private List<Entry> getWhiteReferenceEntries(@NonNull List<WhiteReference> whiteReferences) {
		final List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < whiteReferences.size(); i++) {
			float wave = Float.parseFloat(whiteReferences.get(i).getWave());
			float value = Float.parseFloat(whiteReferences.get(i).getMeasurement());
			entries.add(new Entry(wave, value));
		}
		return entries;
	}

	private void drawChart() {
		lineChart.animateX(1000);
		lineChart.invalidate(); // refresh
	}
}

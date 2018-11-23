package com.vizlore.phasmafood.ui.results;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.results.DarkReference;
import com.vizlore.phasmafood.model.results.FLUO;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.NIR;
import com.vizlore.phasmafood.model.results.Preprocessed;
import com.vizlore.phasmafood.model.results.Sample;
import com.vizlore.phasmafood.model.results.VIS;
import com.vizlore.phasmafood.model.results.WhiteReference;
import com.vizlore.phasmafood.ui.BaseActivity;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by smedic on 3/2/18.
 */

public class ResultsActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";
	private static final String SAMPLE_PREPROCESSED = "preprocessed";
	private static final String SAMPLE_DARK_REFERENCE = "dark_reference";
	private static final String SAMPLE_WHITE_REFERENCE = "white_reference";

	private MeasurementViewModel measurementViewModel;

	private List<ILineDataSet> visDataSets = new ArrayList<>();
	private List<ILineDataSet> nirDataSets = new ArrayList<>();
	private List<ILineDataSet> fluoDataSets = new ArrayList<>();

	private List<ILineDataSet> currentlySelectedDataSet = new ArrayList<>();

	@BindView(R.id.visValue)
	TextView visValue;

	@BindView(R.id.nirValue)
	TextView nirValue;

	@BindView(R.id.fluoValue)
	TextView fluoValue;

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

	@BindView(R.id.buttonPreprocessed)
	Button buttonPreprocessed;

	@BindView(R.id.buttonDarkReference)
	Button buttonDarkReference;

	@BindView(R.id.buttonWhiteReference)
	Button buttonWhiteReference;

	@BindView(R.id.buttonShowAllSamples)
	Button buttonShowAllSamples;

	@BindView(R.id.samplesRadioGroup)
	RadioGroup samplesRadioGroup;

	@OnClick(R.id.buttonPreprocessed)
	void onButtonPreprocessedClick() {
		buttonPreprocessed.setSelected(true);
		buttonDarkReference.setSelected(false);
		buttonWhiteReference.setSelected(false);
		buttonShowAllSamples.setSelected(false);
		lineChart.setData(new LineData(getSampleValues(SAMPLE_PREPROCESSED)));
		drawChart();
	}

	@OnClick(R.id.buttonDarkReference)
	void onButtonDarkReferenceClick() {
		buttonPreprocessed.setSelected(false);
		buttonDarkReference.setSelected(true);
		buttonWhiteReference.setSelected(false);
		buttonShowAllSamples.setSelected(false);
		lineChart.setData(new LineData(getSampleValues(SAMPLE_DARK_REFERENCE)));
		drawChart();
	}

	@OnClick(R.id.buttonWhiteReference)
	void onButtonWhiteReferenceClick() {
		buttonPreprocessed.setSelected(false);
		buttonDarkReference.setSelected(false);
		buttonWhiteReference.setSelected(true);
		buttonShowAllSamples.setSelected(false);
		lineChart.setData(new LineData(getSampleValues(SAMPLE_WHITE_REFERENCE)));
		drawChart();
	}

	@OnClick(R.id.buttonShowAllSamples)
	void onShowAllClicked() {
		buttonPreprocessed.setSelected(false);
		buttonDarkReference.setSelected(false);
		buttonWhiteReference.setSelected(false);
		buttonShowAllSamples.setSelected(true);
		lineChart.setData(new LineData(currentlySelectedDataSet));
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

		measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);

		final Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			visValue.setText(bundle.getString("VIS"));
			nirValue.setText(bundle.getString("NIR"));
			fluoValue.setText(bundle.getString("FLUO"));

			final Measurement measurement = measurementViewModel.getSavedMeasurement();
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

				lineChart.getXAxis().setTextColor(Color.WHITE);
				lineChart.getAxisLeft().setTextColor(Color.WHITE);
				lineChart.getAxisRight().setTextColor(Color.WHITE);
				//lineChart.getLegend().setEnabled(false);
				lineChart.getDescription().setText("");
				lineChart.setScaleEnabled(false);

				//display VIS on start
				lineChart.setData(new LineData(visDataSets));
				currentlySelectedDataSet.addAll(visDataSets);
				drawChart();
				samplesRadioGroup.check(R.id.visRadioButton);
				buttonShowAllSamples.setSelected(true);
			}
		}
	}

	/**
	 * Get list of samples for: preprocessed, dark reference or white reference
	 */
	private List<ILineDataSet> getSampleValues(@NonNull final String sample) {
		final List<ILineDataSet> sampleDataSet = new ArrayList<>();
		for (final ILineDataSet dataSet : currentlySelectedDataSet) {
			if (dataSet.getLabel().equals(sample)) {
				sampleDataSet.add(dataSet);
			}
		}
		return sampleDataSet;
	}

	private List<ILineDataSet> getEntries(final List<Preprocessed> preprocessedList,
										  final List<DarkReference> darkReferenceList,
										  final List<WhiteReference> whiteReferenceList) {

		final List<ILineDataSet> dataSets = new ArrayList<>();

		if (preprocessedList != null) {
			final LineDataSet dataSet = new LineDataSet(getPreprocessedEntries(preprocessedList), SAMPLE_PREPROCESSED);
			dataSet.setColor(getResources().getColor(R.color.orange));
			dataSet.setCircleColor(getResources().getColor(R.color.orange));
			dataSets.add(dataSet);
		}

		if (darkReferenceList != null) {
			final LineDataSet dataSet = new LineDataSet(getDarkReferenceEntries(darkReferenceList), SAMPLE_DARK_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.black));
			dataSet.setCircleColor(getResources().getColor(R.color.black));
			dataSets.add(dataSet);
		}

		if (whiteReferenceList != null) {
			final LineDataSet dataSet = new LineDataSet(getWhiteReferenceEntries(whiteReferenceList), SAMPLE_WHITE_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.white));
			dataSet.setCircleColor(getResources().getColor(R.color.white));
			dataSets.add(dataSet);
		}
		return dataSets;
	}

	private List<Entry> getPreprocessedEntries(@NonNull List<Preprocessed> preprocessedList) {
		final List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < preprocessedList.size(); i++) {
			try {
				float wave = Float.parseFloat(preprocessedList.get(i).getWave());
				float value = Float.parseFloat(preprocessedList.get(i).getMeasurement());
				entries.add(new Entry(wave, value));
			} catch (NumberFormatException e) {
				Log.d(TAG, "getPreprocessedEntries: skipped pp: " + preprocessedList.get(i).getMeasurement());
			}
		}
		return entries;
	}

	private List<Entry> getDarkReferenceEntries(@NonNull List<DarkReference> darkReferences) {
		final List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < darkReferences.size(); i++) {
			try {
				float wave = Float.parseFloat(darkReferences.get(i).getWave());
				float value = Float.parseFloat(darkReferences.get(i).getMeasurement());
				entries.add(new Entry(wave, value));
			} catch (NumberFormatException e) {
				Log.d(TAG, "getPreprocessedEntries: skipped dark ref: " + darkReferences.get(i).getMeasurement());
			}
		}
		return entries;
	}

	private List<Entry> getWhiteReferenceEntries(@NonNull List<WhiteReference> whiteReferences) {
		final List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < whiteReferences.size(); i++) {
			try {
				float wave = Float.parseFloat(whiteReferences.get(i).getWave());
				float value = Float.parseFloat(whiteReferences.get(i).getMeasurement());
				entries.add(new Entry(wave, value));
			} catch (NumberFormatException e) {
				Log.d(TAG, "getPreprocessedEntries: skipped white ref: " + whiteReferences.get(i).getMeasurement());
			}
		}
		return entries;
	}

	private void drawChart() {
		lineChart.animateX(1000);
		lineChart.invalidate(); // refresh
	}

	public void onRadioButtonClicked(final View view) {
		currentlySelectedDataSet.clear();
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
			case R.id.visRadioButton:
				if (checked) {
					lineChart.setData(new LineData(visDataSets));
					currentlySelectedDataSet.addAll(visDataSets);
				}
				break;
			case R.id.nirRadioButton:
				if (checked) {
					lineChart.setData(new LineData(nirDataSets));
					currentlySelectedDataSet.addAll(nirDataSets);
				}
				break;
			case R.id.fluoRadioButton:
				if (checked) {
					lineChart.setData(new LineData(fluoDataSets));
					currentlySelectedDataSet.addAll(fluoDataSets);
				}
				break;
		}
		drawChart();
	}
}

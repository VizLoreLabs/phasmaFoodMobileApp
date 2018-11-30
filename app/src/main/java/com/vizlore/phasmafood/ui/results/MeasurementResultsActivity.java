package com.vizlore.phasmafood.ui.results;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.results.FLUO;
import com.vizlore.phasmafood.model.results.MeasuredSample;
import com.vizlore.phasmafood.model.results.Measurement;
import com.vizlore.phasmafood.model.results.NIR;
import com.vizlore.phasmafood.model.results.Sample;
import com.vizlore.phasmafood.model.results.VIS;
import com.vizlore.phasmafood.ui.BaseActivity;
import com.vizlore.phasmafood.ui.view.ChartMarkerView;
import com.vizlore.phasmafood.utils.Constants;
import com.vizlore.phasmafood.utils.Utils;
import com.vizlore.phasmafood.viewmodel.DeviceViewModel;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeasurementResultsActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";

	public static final String TITLE = "title";
	public static final String IS_FROM_SERVER = "IS_FROM_SERVER";

	private static final String SAMPLE_PREPROCESSED = "preprocessed";
	private static final String SAMPLE_DARK_REFERENCE = "dark_reference";
	private static final String SAMPLE_WHITE_REFERENCE = "white_reference";
	private static final String SAMPLE_RAW_DATA_REFERENCE = "raw_data";
	private static final String SAMPLE_RAW_DARK_REFERENCE = "raw_dark";
	private static final String SAMPLE_RAW_WHITE_REFERENCE = "raw_white";

	private List<ILineDataSet> visDataSets = new ArrayList<>();
	private List<ILineDataSet> nirDataSets = new ArrayList<>();
	private List<ILineDataSet> fluoDataSets = new ArrayList<>();

	private List<ILineDataSet> currentlySelectedDataSet = new ArrayList<>();

	private MeasurementViewModel measurementViewModel;
	private DeviceViewModel deviceViewModel;

	@BindView(R.id.title)
	TextView title;

	@BindView(R.id.useCaseValue)
	TextView useCaseValue;

	@BindView(R.id.sampleValue)
	TextView sampleValue;

	@BindView(R.id.param1Value)
	TextView param1Value;

	@BindView(R.id.visValue)
	TextView visValue;

	@BindView(R.id.nirValue)
	TextView nirValue;

	@BindView(R.id.fluoValue)
	TextView fluoValue;

	//keys changed for testing purposes // FIXME: 3/5/18
	@BindView(R.id.param1Title)
	TextView param1Title;

	@BindView(R.id.chart)
	LineChart lineChart;

	@BindViews({R.id.buttonPreprocessed, R.id.buttonDarkReference, R.id.buttonWhiteReference,
		R.id.buttonRawData, R.id.buttonRawDark, R.id.buttonRawWhite, R.id.buttonShowAllSamples})
	List<Button> sampleTypes;

	@BindView(R.id.buttonRawData)
	Button buttonRawData;

	@BindView(R.id.buttonRawDark)
	Button buttonRawDark;

	@BindView(R.id.buttonRawWhite)
	Button buttonRawWhite;

	@BindView(R.id.buttonShowAllSamples)
	Button buttonShowAllSamples;

	@BindView(R.id.samplesRadioGroup)
	RadioGroup samplesRadioGroup;

	@BindView(R.id.btDeviceCameraImage)
	ImageView btDeviceCameraImage;

	@BindView(R.id.previousButton)
	Button previousButton;

	@BindView(R.id.storeOnServerAndAnalyze)
	Button storeOnServerAndAnalyze;

	@BindView(R.id.storeOnServer)
	Button storeOnServer;

	@BindString(R.string.granularity)
	String granularityString;

	@BindString(R.string.temperatureString)
	String temperatureString;

	@BindString(R.string.resultsFromServer)
	String resultsFromServer;

	@BindString(R.string.resultsFromBtDevice)
	String resultsFromBtDevice;

	@OnClick({R.id.buttonPreprocessed, R.id.buttonDarkReference, R.id.buttonWhiteReference,
		R.id.buttonRawData, R.id.buttonRawDark, R.id.buttonRawWhite, R.id.buttonShowAllSamples})
	void onSampleTypeClicked(final View view) {
		lineChart.setDrawMarkers(false);
		switch (view.getId()) {
			case R.id.buttonPreprocessed:
				lineChart.setData(new LineData(getSampleValues(SAMPLE_PREPROCESSED)));
				break;
			case R.id.buttonDarkReference:
				lineChart.setData(new LineData(getSampleValues(SAMPLE_DARK_REFERENCE)));
				break;
			case R.id.buttonWhiteReference:
				lineChart.setData(new LineData(getSampleValues(SAMPLE_WHITE_REFERENCE)));
				break;
			case R.id.buttonRawData:
				lineChart.setData(new LineData(getSampleValues(SAMPLE_RAW_DATA_REFERENCE)));
				break;
			case R.id.buttonRawDark:
				lineChart.setData(new LineData(getSampleValues(SAMPLE_RAW_DARK_REFERENCE)));
				break;
			case R.id.buttonRawWhite:
				lineChart.setData(new LineData(getSampleValues(SAMPLE_RAW_WHITE_REFERENCE)));
				break;
			case R.id.buttonShowAllSamples:
			default:
				lineChart.setData(new LineData(currentlySelectedDataSet));

		}
		setEnabledSampleType(view.getId());
		drawChart();
	}

	@OnClick(R.id.backButton)
	void onDoneClick() {
		finish();
	}

	@OnClick({R.id.storeOnServerAndAnalyze, R.id.storeOnServer})
	void onNextClick(final View v) {
		final Measurement measurement = measurementViewModel.getSavedMeasurement();
		final boolean shouldAnalyze = v.getId() == R.id.storeOnServerAndAnalyze;
		sendMeasurementToServer(measurement.getResponse().getSample(), shouldAnalyze);
	}

	@OnClick(R.id.previousButton)
	void onRepeatTest() {
		// TODO: 9/11/18 implemented
		// for now, go back so new test can be invoked on a device
		finish();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_measurement_results;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measurement_results);
		ButterKnife.bind(this);

		measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);
		deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);

		final Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			if (bundle.containsKey(IS_FROM_SERVER)) {
				final boolean isFromServer = bundle.getBoolean(IS_FROM_SERVER);
				title.setText(isFromServer ? resultsFromServer : resultsFromBtDevice);
				storeOnServer.setVisibility(isFromServer ? View.GONE : View.VISIBLE);
				storeOnServerAndAnalyze.setVisibility(isFromServer ? View.GONE : View.VISIBLE);
				previousButton.setVisibility(isFromServer ? View.GONE : View.VISIBLE);
			}

			if (bundle.containsKey(TITLE)) {
				title.setText(bundle.getString(TITLE));
			}

			if (bundle.containsKey(Constants.VIS)) {
				visValue.setVisibility(View.VISIBLE);
				visValue.setText(bundle.getString(Constants.VIS));
			}
			if (bundle.containsKey(Constants.NIR)) {
				nirValue.setVisibility(View.VISIBLE);
				nirValue.setText(bundle.getString(Constants.NIR));
			}
			if (bundle.containsKey(Constants.FLUO)) {
				fluoValue.setVisibility(View.VISIBLE);
				fluoValue.setText(bundle.getString(Constants.FLUO));
			}
		}

		// show saved image path
		final String savedImagePath = measurementViewModel.getMeasurementImagePath();
		if (savedImagePath != null) {
			Log.d(TAG, "onCreate: image path: " + savedImagePath);
			final File file = new File(savedImagePath);
			final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			btDeviceCameraImage.setVisibility(View.VISIBLE);
			btDeviceCameraImage.setImageBitmap(bitmap);
		}

		// show measurement chart
		final Measurement measurement = measurementViewModel.getSavedMeasurement();
		if (measurement != null) {
			final Sample sample = measurement.getResponse().getSample();
			if (sample == null) { //keep safe
				return;
			}

			useCaseValue.setText(sample.getUseCase());
			sampleValue.setText(sample.getFoodType());

			if (sample.getGranularity() != null) {
				param1Title.setText(granularityString);
				param1Value.setText(sample.getGranularity());
			} else {
				param1Title.setText(temperatureString);
				param1Value.setText(sample.getTemperature());
			}

			final VIS vis = sample.getVIS();
			final NIR nir = sample.getNIR();
			final FLUO fluo = sample.getFLUO();

			if (vis != null) {
				visDataSets.addAll(getEntries(vis.getPreprocessed(), vis.getDarkReference(), vis.getWhiteReference(), //regular data
					vis.getRawData(), vis.getRawDark(), vis.getRawWhite())); //raw data
			}

			if (nir != null) {
				nirDataSets.addAll(getEntries(nir.getPreprocessed(), nir.getDarkReference(), nir.getWhiteReference(),  //regular data
					null, null, null)); //raw data
			}

			if (fluo != null) {
				fluoDataSets.addAll(getEntries(fluo.getPreprocessed(), fluo.getDarkReference(), fluo.getWhiteReference(),  //regular data
					fluo.getRawData(), fluo.getRawDark(), fluo.getRawWhite())); //raw data
			}

			lineChart.getXAxis().setTextColor(Color.WHITE);
			lineChart.getAxisLeft().setTextColor(Color.WHITE);
			lineChart.getAxisRight().setTextColor(Color.WHITE);
			lineChart.setMarker(new ChartMarkerView(this));
			lineChart.getDescription().setText("");
			lineChart.setScaleEnabled(false);

			lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
				@Override
				public void onValueSelected(Entry e, Highlight h) {
					lineChart.setDrawMarkers(true);
				}

				@Override
				public void onNothingSelected() {
				}
			});

			//display VIS on start
			lineChart.setData(new LineData(visDataSets));
			currentlySelectedDataSet.addAll(visDataSets);
			drawChart();
			samplesRadioGroup.check(R.id.visRadioButton);
			buttonShowAllSamples.setSelected(true);
		}
	}

	/**
	 * Set just one button selected and mark others as non-selected
	 */
	private void setEnabledSampleType(int viewId) {
		for (final Button button : sampleTypes) {
			button.setSelected(button.getId() == viewId);
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

	private List<ILineDataSet> getEntries(final List<MeasuredSample> preprocessedList,
										  final List<MeasuredSample> darkReferenceList,
										  final List<MeasuredSample> whiteReferenceList,
										  final List<List<MeasuredSample>> rawDataList,
										  final List<List<MeasuredSample>> rawDarkList,
										  final List<List<MeasuredSample>> rawWhiteList) {

		final List<ILineDataSet> dataSets = new ArrayList<>();

		if (preprocessedList != null) {
			final LineDataSet dataSet = new LineDataSet(getMeasuredSamplesEntries(preprocessedList), SAMPLE_PREPROCESSED);
			dataSet.setColor(getResources().getColor(R.color.orange));
			dataSet.setCircleColor(getResources().getColor(R.color.orange));
			dataSets.add(dataSet);
		}

		if (darkReferenceList != null) {
			final LineDataSet dataSet = new LineDataSet(getMeasuredSamplesEntries(darkReferenceList), SAMPLE_DARK_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.black));
			dataSet.setCircleColor(getResources().getColor(R.color.black));
			dataSets.add(dataSet);
		}

		if (whiteReferenceList != null) {
			final LineDataSet dataSet = new LineDataSet(getMeasuredSamplesEntries(whiteReferenceList), SAMPLE_WHITE_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.white));
			dataSet.setCircleColor(getResources().getColor(R.color.white));
			dataSets.add(dataSet);
		}

		if (rawDataList != null) {
			final List<MeasuredSample> averages = calculateAverages(rawDataList);
			final LineDataSet dataSet = new LineDataSet(getMeasuredSamplesEntries(averages), SAMPLE_RAW_DATA_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.blue));
			dataSet.setCircleColor(getResources().getColor(R.color.blue));
			dataSets.add(dataSet);
		}

		if (rawDarkList != null) {
			final List<MeasuredSample> averages = calculateAverages(rawDarkList);
			final LineDataSet dataSet = new LineDataSet(getMeasuredSamplesEntries(averages), SAMPLE_RAW_DARK_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.green));
			dataSet.setCircleColor(getResources().getColor(R.color.green));
			dataSets.add(dataSet);
		}

		if (rawWhiteList != null) {
			final List<MeasuredSample> averages = calculateAverages(rawWhiteList);
			final LineDataSet dataSet = new LineDataSet(getMeasuredSamplesEntries(averages), SAMPLE_RAW_WHITE_REFERENCE);
			dataSet.setColor(getResources().getColor(R.color.red));
			dataSet.setCircleColor(getResources().getColor(R.color.red));
			dataSets.add(dataSet);
		}
		return dataSets;
	}

	/**
	 * Calculate averages for input array of 10 measurements, each 288 numbers (variable size)
	 */
	private List<MeasuredSample> calculateAverages(@NonNull final List<List<MeasuredSample>> dataList) {
		final int rawDataListSize = dataList.get(0).size(); //most likely high number (288 for instance)
		final String[] waves = new String[rawDataListSize];
		long[] measurementSums = new long[rawDataListSize];
		for (List<MeasuredSample> listOf10 : dataList) { //10 here!
			for (int i = 0; i < listOf10.size(); i++) { //288 here!
				waves[i] = listOf10.get(i).getWave();
				measurementSums[i] += Long.parseLong(listOf10.get(i).getMeasurement());
			}
		}
		final List<MeasuredSample> averages = new ArrayList<>();
		for (int i = 0; i < measurementSums.length; i++) {
			final String wave = waves[i];
			final String measurement = String.valueOf(measurementSums[i] / dataList.size()); //divide by number of measurements (10)
			averages.add(new MeasuredSample(wave, measurement));
		}
		return averages;
	}

	private List<Entry> getMeasuredSamplesEntries(@NonNull List<MeasuredSample> preprocessedList) {
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


	private void drawChart() {
		lineChart.animateX(1000);
		lineChart.invalidate(); // refresh
	}

	public void onRadioButtonClicked(final View view) {
		currentlySelectedDataSet.clear();
		final boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
			case R.id.visRadioButton:
				if (checked) {
					lineChart.setData(new LineData(visDataSets));
					currentlySelectedDataSet.addAll(visDataSets);
					buttonRawData.setEnabled(true);
					buttonRawDark.setEnabled(true);
					buttonRawWhite.setEnabled(true);
				}
				break;
			case R.id.nirRadioButton:
				if (checked) {
					lineChart.setData(new LineData(nirDataSets));
					currentlySelectedDataSet.addAll(nirDataSets);
					//no raw data, disable buttons
					buttonRawData.setEnabled(false);
					buttonRawDark.setEnabled(false);
					buttonRawWhite.setEnabled(false);
				}
				break;
			case R.id.fluoRadioButton:
				if (checked) {
					lineChart.setData(new LineData(fluoDataSets));
					currentlySelectedDataSet.addAll(fluoDataSets);
					buttonRawData.setEnabled(true);
					buttonRawDark.setEnabled(true);
					buttonRawWhite.setEnabled(true);
				}
				break;
		}
		onSampleTypeClicked(buttonShowAllSamples);
	}

	private void sendMeasurementToServer(@NonNull final Sample sample, boolean shouldAnalyze) {
		final String deviceId = deviceViewModel.getDeviceID();
		Log.d(TAG, "sendMeasurementToServer: device id: " + deviceId);
		if (deviceId != null) {
			measurementViewModel.createMeasurementRequest(Utils.getUserId(), sample, deviceId, shouldAnalyze).observe(this,
				status -> Log.d(TAG, "sendMeasurementToServer: status: " + status));
		} else {
			Toast.makeText(this, "Device null! Not registered yet?", Toast.LENGTH_SHORT).show();
		}
	}
}

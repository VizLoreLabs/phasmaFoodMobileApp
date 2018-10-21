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
import com.vizlore.phasmafood.ui.BaseActivity;
import com.vizlore.phasmafood.viewmodel.DeviceViewModel;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeasurementResultsActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";

	private static final String SAMPLE_PREPROCESSED = "preprocessed";
	private static final String SAMPLE_DARK_REFERENCE = "dark_reference";
	private static final String SAMPLE_WHITE_REFERENCE = "white_reference";

	private List<ILineDataSet> visDataSets = new ArrayList<>();
	private List<ILineDataSet> nirDataSets = new ArrayList<>();
	private List<ILineDataSet> fluoDataSets = new ArrayList<>();

	private List<ILineDataSet> currentlySelectedDataSet = new ArrayList<>();

	private UserViewModel userViewModel;
	private MeasurementViewModel measurementViewModel;
	private DeviceViewModel deviceViewModel;

	@BindView(R.id.useCaseValue)
	TextView useCaseValue;

	@BindView(R.id.sampleValue)
	TextView sampleValue;

	@BindView(R.id.param1Value)
	TextView param1Value;

	//keys changed for testing purposes // FIXME: 3/5/18
	@BindView(R.id.param1Title)
	TextView param1Title;

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

	@BindView(R.id.previousButton)
	Button repeatTest;

	@BindView(R.id.nextButton)
	Button sendToServerButton;

	@BindView(R.id.btDeviceCameraImage)
	ImageView btDeviceCameraImage;

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

	@OnClick(R.id.nextButton)
	void onNextClick() {
		final Measurement measurement = MyApplication.getInstance().getMeasurement();
		sendMeasurementToServer(measurement.getResponse().getSample());
	}

	@OnClick(R.id.previousButton)
	void onRepeatTest() {
		// TODO: 9/11/18 implemented
		// for now, go back so new test can be invoked on a device
		finish();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bt_device_measurements);
		ButterKnife.bind(this);

		userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
		measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);
		deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);

		// show saved image path
		final String savedImagePath = MyApplication.getInstance().getMeasurementImagePath();
		if (savedImagePath != null) {
			Log.d(TAG, "onCreate: image path: " + savedImagePath);
			File file = new File(savedImagePath);
			final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			btDeviceCameraImage.setImageBitmap(bitmap);
		}

		// show measurement chart
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
				param1Value.setText(sample.getGranularity());
			} else {
				param1Title.setText("Temperature:");
				param1Value.setText(sample.getTemperature());
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

	private void sendMeasurementToServer(@NonNull final Sample sample) {
		userViewModel.getUserProfile().observe(this, user -> {
			//deviceViewModel.createDevice().observe(this, res -> {
			//Log.d(TAG, "sendMeasurementToServer: device created ? " + res);
			String deviceId = deviceViewModel.getDeviceID();
			deviceId = "90:70:65:EF:4A:CE"; // TODO: 9/11/18 fix
			Log.d(TAG, "sendMeasurementToServer: device id: " + deviceId);
			if (deviceId != null) {
				measurementViewModel.createMeasurementRequest(user.id(), sample, deviceId).observe(this, status -> {
					Log.d(TAG, "sendMeasurementToServer: status: " + status);
				});
			} else {
				Toast.makeText(this, "Device null! Not registered yet?", Toast.LENGTH_SHORT).show();
			}
			//});
		});
	}

}

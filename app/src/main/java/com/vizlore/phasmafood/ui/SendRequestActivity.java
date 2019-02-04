package com.vizlore.phasmafood.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.bluetooth.BluetoothService;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.utils.Constants;
import com.vizlore.phasmafood.viewmodel.MeasurementViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Stevan Medic
 * <p>
 * Created on Nov 2018
 */
public class SendRequestActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";
	public static final String DEBUG_MODE_KEY = "debug_mode";

	//private static final String USE_CASE_1_JSON = "measurements_10_sample_full.json";

	private static final String USE_CASE_1_JSON = "output_micotoxins_detection.json";
	private static final String USE_CASE_2_JSON = "usecase2_updated_response.json";
	private static final String USE_CASE_3_JSON = "use_case_3_sample.json";

	private UseCaseType useCaseType;
	private JSONObject wizardJsonObject = null;
	private MeasurementViewModel measurementViewModel;
	private BluetoothService bluetoothService;
	private CompositeDisposable disposable = new CompositeDisposable();

	//for testing purposes
	@BindView(R.id.debugModeSwitch)
	Switch debugModeSwitch;

	@OnClick(R.id.sendRequest)
	void onSendRequestClick() {
		//wrap into request
		final JSONObject jsonObjectRequest = new JSONObject();
		try {
			jsonObjectRequest.put("Request", wizardJsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "showSendActionDialog: SEND:\n" + jsonObjectRequest.toString());
		showSendActionDialog(jsonObjectRequest.toString());
	}

	@OnClick(R.id.backButton)
	void onBackClick() {
		finish();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_send_request;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);

		// just for testing (is debug mode or not)
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		debugModeSwitch.setChecked(prefs.getBoolean(DEBUG_MODE_KEY, false));

		debugModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) ->
			prefs.edit().putBoolean(DEBUG_MODE_KEY, isChecked).apply());

		// start bluetooth service
		final Intent intent = new Intent(this, BluetoothService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE); //Binding to the service!

		readWizardJson();
	}

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			final BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) iBinder;
			bluetoothService = binder.getServiceInstance(); //Get instance of your service!
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			bluetoothService = null;
		}
	};

	private void readWizardJson() {
		if (getIntent() == null || getIntent().getExtras() == null || !getIntent().getExtras().containsKey(Constants.WIZARD_DATA_KEY)) {
			riseError("Error loading JSON from wizard! Contact support");
		}
		final Bundle extras = getIntent().getExtras();
		final String json = extras.getString(Constants.WIZARD_DATA_KEY);
		try {
			wizardJsonObject = new JSONObject(json);
			if (!wizardJsonObject.has(Constants.USE_CASE_KEY)) {
				riseError("No use cases found! Contact support");
			}
			getUseCaseType();
		} catch (JSONException e) {
			e.printStackTrace();
			riseError(e.getMessage());
		}
	}

	private void getUseCaseType() throws JSONException {
		final String useCaseKey = wizardJsonObject.getString(Constants.USE_CASE_KEY);
		switch (useCaseKey) {
			case Constants.USE_CASE_1:
				useCaseType = UseCaseType.USE_CASE_1;
				break;
			case Constants.USE_CASE_2:
				useCaseType = UseCaseType.USE_CASE_2;
				break;
			case Constants.USE_CASE_3:
				useCaseType = UseCaseType.USE_CASE_3;
				break;
			case Constants.USE_CASE_WHITE_REF:
				useCaseType = UseCaseType.USE_CASE_WHITE_REFERENCE;
				break;
			case Constants.USE_CASE_TEST:
				useCaseType = UseCaseType.USE_CASE_TEST;
				break;
		}
	}

	private void showSendActionDialog(final String jsonToSend) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(getString(R.string.sendDataMessage));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.yes), (dialog, which) -> {
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			final boolean isDebugMode = prefs.getBoolean(DEBUG_MODE_KEY, false);
			if (!isDebugMode) {
				bluetoothService.sendMessage(jsonToSend, false);
				Toast.makeText(this, "Sending data to BT device", Toast.LENGTH_LONG).show();
				finish();
			} else {
				Log.d(TAG, "showSendActionDialog: PERFORM FAKE MEASUREMENT (debug mode)");
				//send params directly to server (use case 1 of 2)
				//load use case 1 by default
				String jsonFileName = USE_CASE_1_JSON;
				if (useCaseType == UseCaseType.USE_CASE_2) {
					jsonFileName = USE_CASE_2_JSON;
				} else if (useCaseType == UseCaseType.USE_CASE_3) {
					jsonFileName = USE_CASE_3_JSON;
				}

				disposable.add(bluetoothService.sendFakeMessage(jsonFileName).subscribe(measurement -> {
						measurementViewModel.saveMeasurement(measurement);
						final Intent intent = new Intent(this, MeasurementResultsActivity.class);
						intent.putExtra(MeasurementResultsActivity.IS_FROM_SERVER, false);
						startActivity(intent);
					},
					e -> Log.d(TAG, "onError: " + e.getMessage())
				));
			}
			dialog.dismiss();
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.no), (dialog, which) -> dialog.dismiss());
		alertDialog.show();
	}

	private void riseError(@NonNull final String error) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	protected void onDestroy() {
		if (!disposable.isDisposed()) {
			disposable.clear();
		}
		super.onDestroy();
	}

	public enum UseCaseType {
		USE_CASE_1,
		USE_CASE_2,
		USE_CASE_3,
		USE_CASE_WHITE_REFERENCE,
		USE_CASE_TEST
	}
}

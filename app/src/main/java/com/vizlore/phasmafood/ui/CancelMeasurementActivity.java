package com.vizlore.phasmafood.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.bluetooth.BluetoothService;
import com.vizlore.phasmafood.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;

/**
 * @author Stevan Medic
 * <p>
 * Created on Dec 2018
 */
public class CancelMeasurementActivity extends BaseActivity {

	private static final String TAG = "SMEDIC";
	private BluetoothService bluetoothService;

	@OnClick({R.id.yes, R.id.no})
	void onClick(View v) {
		if (v.getId() == R.id.yes) {
			sendCancelMeasurementRequest();
		} else if (v.getId() == R.id.no) {
			finish();
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_cancel_measurement;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = new Intent(this, BluetoothService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE); //Binding to the service!
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

	private void sendCancelMeasurementRequest() {
		final JSONObject jsonObjectRequest = new JSONObject();
		try {
			JSONObject jsonCancelObject = new JSONObject();
			jsonCancelObject.put(Constants.USE_CASE_KEY, "Cancel");
			jsonObjectRequest.put("Request", jsonCancelObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "sendCancelMeasurementRequest: SEND: " + jsonObjectRequest.toString());
		bluetoothService.sendMessage(jsonObjectRequest.toString(), true);
		finish();
	}
}

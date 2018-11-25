package com.vizlore.phasmafood.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;

public class BluetoothDeviceResultsReceiver extends BroadcastReceiver {

	private static final String TAG = "SMEDIC";

	@Override
	public void onReceive(@NonNull final Context context, final Intent intent) {
		Log.d(TAG, "onReceive: ");
		final Intent intentNew = new Intent(context, MeasurementResultsActivity.class);
		intent.putExtra("title", "Results from server");
		intentNew.putExtra("extras", intent.getExtras());
		context.startActivity(intent);
	}
}

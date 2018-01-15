package com.vizlore.phasmafood.utils;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.List;

/**
 * Created by smedic on 1/15/18.
 */

public class Utils {

	private static final String TAG = "SMEDIC Utils";

	public static void prettyPrintDevices(String title, List<BluetoothDevice> devices) {
		Log.d(TAG, "\n-------------- " + title + " ---------------");
		for (BluetoothDevice device : devices) {
			Log.d(TAG, "" + device.getName() + " - " + device.getAddress() + " - " + device.getType());
		}
		Log.d(TAG, "-------------------------------------");

	}
}

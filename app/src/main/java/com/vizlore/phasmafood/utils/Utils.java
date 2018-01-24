package com.vizlore.phasmafood.utils;

import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import static com.vizlore.phasmafood.utils.Config.UUID_KEY;
import static com.vizlore.phasmafood.viewmodel.UserViewModel.TOKEN_KEY;

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

	public static String getUUID(@NonNull SharedPreferences prefs) {
		String uuid;
		if (prefs.contains(UUID_KEY)) {
			uuid = prefs.getString(UUID_KEY, "");
		} else {
			uuid = UUID.randomUUID().toString();
		}
		return uuid;
	}

	public static String getHeader(@NonNull SharedPreferences prefs) {
		return "JWT " + prefs.getString(TOKEN_KEY, "");
	}
}

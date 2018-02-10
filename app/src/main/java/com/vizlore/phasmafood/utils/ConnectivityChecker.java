package com.vizlore.phasmafood.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by smedic on 2/10/18.
 */

public class ConnectivityChecker {

	public static boolean isNetworkEnabled(Context context) {
		ConnectivityManager androidConnectivityManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = androidConnectivityManager.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public static boolean isBluetoothEnabled(Context context) {
		final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) { // check if device supports Bluetooth
			Toast.makeText(context, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
			return false;
		}
		return bluetoothAdapter.isEnabled();
	}
}

package com.vizlore.phasmafood.utils;

import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static com.vizlore.phasmafood.utils.Config.BT_DEVICE_UUID_KEY;
import static com.vizlore.phasmafood.utils.Config.MOBILE_DEVICE_UUID_KEY;
import static com.vizlore.phasmafood.viewmodel.UserViewModel.TOKEN_KEY;

/**
 * Created by smedic on 1/15/18.
 */

public class Utils {

	private static final String TAG = "SMEDIC Utils";

	private static String btDeviceUuid = null;
	private static String mobileDeviceUuid = null;

	public static void prettyPrintDevices(String title, List<BluetoothDevice> devices) {
		Log.d(TAG, "\n-------------- " + title + " ---------------");
		for (BluetoothDevice device : devices) {
			Log.d(TAG, "" + device.getName() + " - " + device.getAddress() + " - " + device.getType());
		}
		Log.d(TAG, "-------------------------------------");

	}

	/**
	 * Get uuid of current mobile device
	 */

	public synchronized static String getMobileUUID() {
		return Settings.Secure.getString(MyApplication.getAppContext().getContentResolver(),
			Settings.Secure.ANDROID_ID);
	}

	public synchronized static String getBluetoothDeviceUUID() {
		return "40cff00a-11ad-4439-9047-08c5da711063";
//		if (btDeviceUuid == null) {
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
//			btDeviceUuid = prefs.getString(BT_DEVICE_UUID_KEY, null);
//			if (btDeviceUuid == null) {
//				btDeviceUuid = UUID.randomUUID().toString();
//				prefs.edit().putString(BT_DEVICE_UUID_KEY, btDeviceUuid).apply();
//			}
//		}
//		return btDeviceUuid;
	}

	//do on logout
	public synchronized static void clearUuids() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(MOBILE_DEVICE_UUID_KEY);
		editor.remove(BT_DEVICE_UUID_KEY);
		editor.apply();
	}

	public static String getHeader() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
		return "JWT " + prefs.getString(TOKEN_KEY, "");
	}

	public static String removeMagicChar(String string) {
		if (string.contains("!")) {
			int magicCharPos = string.indexOf("!");
			return string.substring(magicCharPos + 1);
		}
		return string;
	}

	public static void saveToSdCard(String filename, String data) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath());
		dir.mkdirs();
		File file = new File(dir, filename);
		try {
			FileOutputStream f = new FileOutputStream(file);
			f.write(data.getBytes(Charset.forName("UTF-8")));
			f.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "saveToSdCard: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

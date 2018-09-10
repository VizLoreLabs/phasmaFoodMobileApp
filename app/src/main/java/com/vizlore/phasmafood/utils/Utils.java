package com.vizlore.phasmafood.utils;

import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import static com.vizlore.phasmafood.ui.wizard.WizardActivity.DEBUG_MODE_KEY;
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

	@Nullable
	public synchronized static String getBluetoothDeviceUUID() {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
		final boolean isDebugMode = prefs.getBoolean(DEBUG_MODE_KEY, false);
		if (isDebugMode) {
			return UUID.randomUUID().toString();
		}
		return prefs.getString(BT_DEVICE_UUID_KEY, null);
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
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
		return prefs.getString(TOKEN_KEY, "");
	}

	public static String removeMagicChar(String string) {
		if (string == null) {
			return "";
		}
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

package com.vizlore.phasmafood.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.vizlore.phasmafood.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import static com.vizlore.phasmafood.ui.SendRequestActivity.DEBUG_MODE_KEY;
import static com.vizlore.phasmafood.viewmodel.UserViewModel.TOKEN_KEY;
import static com.vizlore.phasmafood.viewmodel.UserViewModel.USER_ID_KEY;

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

	/**
	 * Get uuid of current mobile device
	 */

	public synchronized static String getMobileUUID() {
		return Settings.Secure.getString(MyApplication.getInstance().getContentResolver(),
			Settings.Secure.ANDROID_ID);
	}

	public static String generateSampleId() {
		final Random rand = new Random();
		final int randomValue = rand.nextInt(100000000);
		return String.valueOf(randomValue);
	}

	//our BT device in Greece has this uuid: "90:70:65:EF:4A:CE"
	public synchronized static String getBluetoothDeviceAddress() {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
		final boolean isDebugMode = prefs.getBoolean(DEBUG_MODE_KEY, false);
		if (isDebugMode) {
			return "90:70:65:EF:4A:CE";
		}
		return prefs.getString(Config.BT_DEVICE_ADDRESS_KEY, null);
	}

	//do on logout
	public synchronized static void clearUuids() {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
		final SharedPreferences.Editor editor = prefs.edit();
		editor.remove(Config.BT_DEVICE_ADDRESS_KEY);
		editor.apply();
	}

	public static void saveAuthToken(@NonNull final String authToken) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putString(TOKEN_KEY, "JWT " + authToken);
		editor.apply();
	}

	public static String getAuthToken() {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
		return prefs.getString(TOKEN_KEY, "");
	}

	public static void saveUserId(@NonNull final String userId) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putString(USER_ID_KEY, userId);
		editor.apply();
	}

	public static String getUserId() {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
		return prefs.getString(USER_ID_KEY, "");
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

	public static String tempFileImage(Context context, Bitmap bitmap, String name) {

		File outputDir = context.getCacheDir();
		File imageFile = new File(outputDir, name + ".jpg");

		OutputStream os;
		try {
			os = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.e(TAG, "Error writing file", e);
		}

		return imageFile.getAbsolutePath();
	}

	// dump measurement response data if needed
	public static String tempFile2(byte[] text) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/smedic");
		dir.mkdirs();
		File file = new File(dir, "output.txt");
		try {
			FileOutputStream f = new FileOutputStream(file);
			f.write(text);
			f.flush();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public static void deleteDirectory(final String directoryName) {
		File dir = new File(directoryName);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file.getPath());
				} else {
					boolean wasSuccessful = file.delete();
					if (wasSuccessful) {
						Log.i("SMEDIC Deleted ", "successfully " + file.getName());
					}
				}
			}
			dir.delete();
		}
	}

	public static void writeToFile(byte[] data) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/phasma");
		dir.mkdirs();
		File file = new File(dir, "images.zip");
		try {
			FileOutputStream f = new FileOutputStream(file);
			f.write(data);
			f.flush();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String encodeToBase64(final File file) {
		String base64File = "";
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a file from file system
			byte[] fileData = new byte[(int) file.length()];
			imageInFile.read(fileData);
			base64File = Base64.encodeToString(fileData, Base64.DEFAULT);
		} catch (FileNotFoundException e) {
			Log.d(TAG, "encodeToBase64: File not found" + e);
		} catch (IOException ioe) {
			Log.d(TAG, "encodeToBase64: Exception while reading the file " + ioe);
		}
		return base64File;
	}
}

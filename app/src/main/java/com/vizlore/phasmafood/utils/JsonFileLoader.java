package com.vizlore.phasmafood.utils;

import android.content.res.AssetManager;

import com.vizlore.phasmafood.MyApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by smedic on 1/21/18.
 */

public class JsonFileLoader {
	/**
	 * @param fileName Full path of the asset file. Example "assets/weekly/weekModel.json"
	 * @return String if successful, null if it fails.
	 */
	public String fromAsset(final String fileName) {
		String json = null;
		try {
			AssetManager am = MyApplication.getAppContext().getAssets();
			InputStream inputStream = am.open(fileName);
			if (inputStream != null) {
				int size = inputStream.available();
				if (size > 0) {
					byte[] buffer = new byte[size];
					inputStream.read(buffer);
					inputStream.close();
					json = new String(buffer, "UTF-8");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	public boolean fileExists(final String fileName) {
		AssetManager am = MyApplication.getAppContext().getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is != null;
	}
}
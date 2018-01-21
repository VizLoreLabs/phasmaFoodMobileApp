package com.vizlore.phasmafood.utils;

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

			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

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
		InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		return is != null;
	}
}
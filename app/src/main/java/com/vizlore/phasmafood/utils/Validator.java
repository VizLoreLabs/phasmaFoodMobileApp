package com.vizlore.phasmafood.utils;

import android.widget.EditText;

/**
 * Created by smedic on 1/21/18.
 */

public class Validator {

	/**
	 * Validates edit text fields (first name, last name, email etc.)
	 *
	 * @param fields edit text
	 * @return true if fields are valid
	 */
	public static boolean validateFields(EditText[] fields) {
		for (EditText field : fields) {
			if (field.getText() == null || field.getText().toString().isEmpty()) {
				return false;
			}
		}
		return true;
	}
}

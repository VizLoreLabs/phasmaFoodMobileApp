package wizardpager.ui;

import android.os.Bundle;
import android.text.InputType;

public class NumberFragment extends TextFragment {

	public static NumberFragment create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		NumberFragment f = new NumberFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	protected void setInputType() {
		mEditTextInput.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

}

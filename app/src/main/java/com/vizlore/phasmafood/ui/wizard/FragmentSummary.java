package com.vizlore.phasmafood.ui.wizard;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.viewmodel.WizardViewModel;

import butterknife.BindView;

/**
 * Created by smedic on 1/15/18.
 */

public class FragmentSummary extends WizardBaseFragment {

	private static final String TAG = "SMEDIC";

	@BindView(R.id.option_1)
	TextView option1;

	@BindView(R.id.option_2)
	TextView option2;

	@BindView(R.id.option_3)
	TextView option3;

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Log.d(TAG, "onViewCreated: SUMMARY FRAGMENT ON VIEW CREATED");

		WizardViewModel wizardViewModel = ViewModelProviders.of(getActivity()).get(WizardViewModel.class);

		option1.setText(wizardViewModel.getSelection1Text());
		option2.setText(wizardViewModel.getSelection2Text());
		option3.setText(wizardViewModel.getSelection3Text());
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_wizard_summary;
	}
}

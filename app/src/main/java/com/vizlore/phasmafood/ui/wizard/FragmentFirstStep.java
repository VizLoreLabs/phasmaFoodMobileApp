package com.vizlore.phasmafood.ui.wizard;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.UseCase;
import com.vizlore.phasmafood.viewmodel.WizardViewModel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by smedic on 1/15/18.
 */

public class FragmentFirstStep extends WizardBaseFragment {

	private static final String TAG = "SMEDIC";

	@BindView(R.id.radioGroup)
	RadioGroup wizardOptions;

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		WizardViewModel wizardViewModel = ViewModelProviders.of(getActivity()).get(WizardViewModel.class);

		final List<UseCase> useCases = wizardViewModel.getUseCases();
		if (useCases != null) {
			for (int i = 0; i < useCases.size(); i++) {
				RadioButton radioButtonView = (RadioButton) getLayoutInflater().inflate(R.layout.wizard_radio_button, null);
				radioButtonView.setText(useCases.get(i).getName());

				RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.buttonHeight), 1.0f);
				param.leftMargin = (int) getResources().getDimension(R.dimen.buttonHorizontalMargin);
				param.rightMargin = (int) getResources().getDimension(R.dimen.buttonHorizontalMargin);
				param.bottomMargin = (int) getResources().getDimension(R.dimen.buttonsSpacing);
				radioButtonView.setLayoutParams(param);

				wizardOptions.addView(radioButtonView);
			}
		}

		wizardOptions.setOnCheckedChangeListener((radioGroup, i) -> {
			View radioButton = radioGroup.findViewById(i);
			int index = radioGroup.indexOfChild(radioButton);
			wizardViewModel.setSelection1(index);
		});

		//set default option - 0
		wizardViewModel.setSelection1(0);

		// mark previously chosen option
		int selection = wizardViewModel.getSelection1();
		if (selection != -1 && wizardOptions.getChildAt(selection) != null) {
			((RadioButton) wizardOptions.getChildAt(selection)).setChecked(true);
		}
	}

	@Override
	protected int getFragmentLayout() {
		return R.layout.fragment_first_step;
	}

}


package com.vizlore.phasmafood.profile_setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vizlore.phasmafood.profile_setup.viewmodel.ProfileSetupViewModel;

import butterknife.ButterKnife;

public abstract class ProfileBaseFragment extends Fragment {

	protected ProfileSetupViewModel profileSetupViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		profileSetupViewModel = ViewModelProviders.of(getActivity()).get(ProfileSetupViewModel.class);
		return inflater.inflate(getFragmentLayout(), container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
	}

	/**
	 * Every fragment has to inflate a layout in the onCreateView method. We have added this method to
	 * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
	 * inflate in this method when extends BaseFragment.
	 */
	protected abstract int getFragmentLayout();
}

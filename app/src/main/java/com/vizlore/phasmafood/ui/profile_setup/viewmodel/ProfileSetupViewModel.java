package com.vizlore.phasmafood.ui.profile_setup.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.vizlore.phasmafood.ui.profile_setup.ProfileAction;
import com.vizlore.phasmafood.utils.SingleLiveEvent;

/**
 * Created by smedic on 1/16/18.
 */

public class ProfileSetupViewModel extends ViewModel {
	// Use this event to signal selected item change
	private SingleLiveEvent<ProfileAction> selectedPageEvent = null;

	/**
	 * By observing this event observers will get page change event.
	 *
	 * @return data to observe
	 */
	public LiveData<ProfileAction> getSelectedEvent() {
		if (selectedPageEvent == null) {
			selectedPageEvent = new SingleLiveEvent<>();
			selectedPageEvent.setValue(ProfileAction.ACTION_MAIN_SELECTED);
		}

		return selectedPageEvent;
	}

	/**
	 * Use this method to select a page. Observers will be notified.
	 *
	 * @param selected action
	 */
	public void setSelected(ProfileAction selected) {
		if (selectedPageEvent != null) {
			selectedPageEvent.setValue(selected);
		}
	}
}

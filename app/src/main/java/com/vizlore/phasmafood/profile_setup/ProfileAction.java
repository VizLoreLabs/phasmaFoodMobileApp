package com.vizlore.phasmafood.profile_setup;

/**
 * Created by smedic on 1/16/18.
 */

public enum ProfileAction {

	// default state
	ACTION_MAIN_SELECTED,

	// sign in screens
	CREATE_ACCOUNT_CLICKED,
	CONFIRM_EMAIL_CLICKED,
	SIGNED_IN,
	RECOVER_PASSWORD,

	// main screen actions
	START_MEASUREMENT_CLICKED,
	MEASUREMENT_HISTORY_CLICKED,
	YOUR_PROFILE_CLICKED,
	LEARN_MORE_CLICKED,

	// back navigation
	BACK_TO_SIGN_IN,
	GO_BACK_TO_ROOT,
	GO_BACK
}

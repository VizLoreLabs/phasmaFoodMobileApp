package com.vizlore.phasmafood.ui.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by smedic on 1/23/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	public MyFirebaseMessagingService() {
		super();
	}

	private static final String TAG = "SMEDIC";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		Log.d(TAG, "******************* onMessageReceived: ");
	}

	@Override
	public void onDeletedMessages() {
		super.onDeletedMessages();
	}

	@Override
	public void onMessageSent(String s) {
		super.onMessageSent(s);
	}

	@Override
	public void onSendError(String s, Exception e) {
		super.onSendError(s, e);
	}
}

package com.vizlore.phasmafood.ui.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vizlore.phasmafood.ui.ResultsActivity;

import java.util.Map;

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

		Log.d(TAG, "*** onMessageReceived: " + remoteMessage);
		Log.d(TAG, "*** onMessageReceived: " + remoteMessage.getNotification().getTitle());
		Log.d(TAG, "*** onMessageReceived: " + remoteMessage.getNotification().getBody());

		if (remoteMessage.getData() != null) {
			Bundle bundle = new Bundle();
			for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
				Log.d(TAG, "*** onMessageReceived: " + entry.getKey() + " - " + entry.getValue());
				bundle.putString(entry.getKey(), entry.getValue());
			}

			Intent intent = new Intent(this, ResultsActivity.class);
			intent.putExtra("vis", remoteMessage.getData().get("VIS"));
			intent.putExtra("nir", remoteMessage.getData().get("NIR"));
			intent.putExtra("flou", remoteMessage.getData().get("FLOU"));
			startActivity(intent);
		}
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

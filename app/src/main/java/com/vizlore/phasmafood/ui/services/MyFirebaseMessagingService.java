package com.vizlore.phasmafood.ui.services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vizlore.phasmafood.ui.ResultsActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

		Intent intent = new Intent(this, ResultsActivity.class);
		try {
			JSONObject jsonObject = new JSONObject(remoteMessage.getNotification().getBody());
			if (jsonObject.has("VIS"))
				intent.putExtra("VIS", jsonObject.getString("VIS"));
			if (jsonObject.has("NIR"))
				intent.putExtra("NIR", jsonObject.getString("NIR"));
			if (jsonObject.has("FLOU"))
				intent.putExtra("FLOU", jsonObject.getString("FLOU"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		startActivity(intent);
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

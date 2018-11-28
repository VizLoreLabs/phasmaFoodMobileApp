package com.vizlore.phasmafood.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vizlore.phasmafood.ui.results.MeasurementResultsActivity;
import com.vizlore.phasmafood.utils.Constants;

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
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		Log.d(TAG, "*** onMessageReceived: " + remoteMessage);
		Log.d(TAG, "*** onMessageReceived: " + remoteMessage.getNotification().getTitle());
		Log.d(TAG, "*** onMessageReceived: " + remoteMessage.getNotification().getBody());

		Intent intent = new Intent(this, MeasurementResultsActivity.class);
		intent.putExtra(MeasurementResultsActivity.IS_FROM_SERVER, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			JSONObject jsonObject = new JSONObject(remoteMessage.getNotification().getBody());
			if (jsonObject.has(Constants.VIS))
				intent.putExtra(Constants.VIS, jsonObject.getString(Constants.VIS));
			if (jsonObject.has(Constants.NIR))
				intent.putExtra(Constants.NIR, jsonObject.getString(Constants.NIR));
			if (jsonObject.has(Constants.FLUO))
				intent.putExtra(Constants.FLUO, jsonObject.getString(Constants.FLUO));
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

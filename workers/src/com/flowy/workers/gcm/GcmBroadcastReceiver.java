package com.flowy.workers.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import static com.flowy.workers.Constants.*;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		
	    // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
		if (LOCAL_LOGV) {
			Log.v(LOG_TAG, "GcmBroadcastReceiver msgType: " + messageType + " msg: " + extras.toString());
		}
        if (!extras.isEmpty()) {
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            	Toast.makeText(context, "GCM: Send error: " + extras.toString(), Toast.LENGTH_LONG).show();
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            	Toast.makeText(context, "GCM: Deleted message on server: " + extras.toString(), Toast.LENGTH_LONG).show();
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	if (LOCAL_LOGV) {
            		Log.v(LOG_TAG, "GCM MSG RECEIVED:" + extras.toString());
            	}
            	Toast.makeText(context, "GCM: Received: " + extras.toString(), Toast.LENGTH_LONG).show();
            	
            }
        }
        setResultCode(Activity.RESULT_OK);
	}
	

}

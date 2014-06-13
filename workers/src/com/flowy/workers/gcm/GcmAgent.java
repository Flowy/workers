package com.flowy.workers.gcm;

import static com.flowy.workers.Constants.LOG_TAG;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.flowy.workers.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmAgent {

	Context context;

	public static final String EXTRA_MESSAGE = "msg";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	GoogleCloudMessaging gcm;
	String gcmRegistrationId;
    AtomicInteger msgId = new AtomicInteger();
    
    TextView tvIn, tvOut;

	  String SENDER_ID = "53984817665";  //workers project
//	String SENDER_ID = "526444376080";  //echo server

	public GcmAgent(Context context, TextView out) {
		this.context = context;
		gcm = GoogleCloudMessaging.getInstance(context);
		gcmRegistrationId = getRegistrationId(context);

		this.tvOut = out;
		
		if (gcmRegistrationId.isEmpty()) {
			registerInBackground();
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					gcmRegistrationId = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + gcmRegistrationId;

					// You should send the registration ID to your server over HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();
					// For this demo: we don't need to send it because the device will send
					// upstream messages to a server that echo back the message using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, gcmRegistrationId);
				} catch (IOException ex) {
					msg = "Error: " + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				tvOut.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

	/**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
	private void sendRegistrationIdToBackend() {
		 // Your implementation here.
	}

	private void storeRegistrationId(Context context,
			String gcmRegistrationId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(LOG_TAG, "GCM: Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, gcmRegistrationId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	// Send an upstream message
	public void sendMsg(final String message) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg;
				try {
					Bundle data = new Bundle();
					data.putString("my_message", message);
					String id = Integer.toString(msgId.incrementAndGet());
					gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
					msg = "Sent message id: " + msgId.get();
				} catch (IOException ex) {
					msg = "Error: " + ex.getMessage();
				}
				return msg;
			}
			
			@Override
			protected void onPostExecute(String msg) {
				tvOut.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(LOG_TAG, "GCM Registration not found.");
			return registrationId;
		}

		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(LOG_TAG, "GCM: App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context2) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private int getAppVersion(Context context2) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			//should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}


}

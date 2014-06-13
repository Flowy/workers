package com.flowy.workers;

import static com.flowy.workers.Constants.*;

import java.util.Date;

import com.flowy.workers.db.DayEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class LeaveRequest implements LoggingRequest, OnCompletionListener {

	Context context;

	LeaveRequest(Context context) {
		this.context = context;
	}

	@Override
	public void parseQRCode(String code) {
		Cursor result = context.getContentResolver().query(
				Uri.parse(CONTENT_URI).buildUpon()
						.appendQueryParameter("table", PERSON_TABLE).build(),
				PERSON_PROJECTION,
				PERSON_COMPANY_ID + " IN ( SELECT " + QR_FK_PERSON_ID
						+ " FROM " + QR_TABLE + " WHERE " + QR_CODE + " = ? )",
				new String[] { code }, null);
		result.moveToFirst();

		String personId = result.getString(result
				.getColumnIndex(PERSON_COMPANY_ID));
		result.close();
		execute(personId);
	}

	public void execute(String personId) {
		Cursor result = context
				.getContentResolver()
				.query(Uri.parse(CONTENT_URI).buildUpon()
						.appendQueryParameter("table", DAY_ENTRY_TABLE).build(),
						DAY_ENTRY_PROJECTION, DAY_ENTRY_FK_PERSON_ID + " = ?",
						new String[] { personId }, null);
		if (LOCAL_LOGV) {
			Log.v(LOG_TAG, "Found rows: " + result.getCount());
		}

		result.moveToLast();

		if ((result.getCount() > 0)
				&& result.isNull(result.getColumnIndex(DAY_ENTRY_END))) { // last
																			// day
																			// opened
																			// -
																			// closing
			if (LOCAL_LOGV) {
				Log.v(LOG_TAG, "Ending day for: " + personId);
			}
			ContentValues values = new ContentValues();
			values.put(DAY_ENTRY_END, new Date().getTime());
			context.getContentResolver().update(
					Uri.parse(CONTENT_URI).buildUpon()
							.appendQueryParameter("table", DAY_ENTRY_TABLE)
							.build(),
					values,
					DAY_ENTRY_START + " = ? AND " + DAY_ENTRY_FK_PERSON_ID
							+ " = ?",
					new String[] {
							Long.toString(result.getLong(result
									.getColumnIndex(DAY_ENTRY_START))),
							result.getString(result
									.getColumnIndex(DAY_ENTRY_FK_PERSON_ID)) });

			Toast.makeText(context, personId + " logged OUT", Toast.LENGTH_LONG)
					.show();

			MediaPlayer mediaPlayer = MediaPlayer
					.create(context, R.raw.goodbye);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.start();
		} else { // can request when no day opened - open new day with start ==
					// end
			if (LOCAL_LOGV) {
				Log.v(LOG_TAG, "Opening new day for: " + personId);
			}
			Date date = new Date();
			DayEntry dayEntry = new DayEntry(date, personId);
			dayEntry.end = date;
			context.getContentResolver().insert(
					Uri.parse(CONTENT_URI).buildUpon()
							.appendQueryParameter("table", DAY_ENTRY_TABLE)
							.build(), dayEntry.toRow());

			Toast.makeText(context, personId + " logged OUT", Toast.LENGTH_LONG)
					.show();

			MediaPlayer mediaPlayer = MediaPlayer
					.create(context, R.raw.goodbye);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.start();

			result.close();
		}

		result.close();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.release();
		mp = null;
		Log.v(LOG_TAG, "Media player released");
	}
}

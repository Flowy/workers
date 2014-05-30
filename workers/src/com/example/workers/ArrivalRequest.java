package com.example.workers;

import static com.example.workers.Constants.*;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ArrivalRequest implements LoggingRequest, OnCompletionListener {

	Context context;

	ArrivalRequest(Context context) {
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
						DAY_ENTRY_PROJECTION,
						DAY_ENTRY_END + " IS NULL AND "
								+ DAY_ENTRY_FK_PERSON_ID + " = ?",
						new String[] { personId }, null);
		if (LOCAL_LOGV) {
			Log.v(LOG_TAG, "Found rows: " + result.getCount());
		}

		// opening new day entry
		if (LOCAL_LOGV) {
			Log.v(LOG_TAG, "Opening new day for: " + personId);
		}
		DayEntry dayEntry = new DayEntry(new Date(), personId);
		context.getContentResolver()
				.insert(Uri.parse(CONTENT_URI).buildUpon()
						.appendQueryParameter("table", DAY_ENTRY_TABLE).build(),
						dayEntry.toRow());

		Toast.makeText(context, personId + " logged IN", Toast.LENGTH_LONG)
				.show();

		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.welcome);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.start();

		result.close();

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.release();
		mp = null;
		Log.v(LOG_TAG, "Media player released");
	}

}

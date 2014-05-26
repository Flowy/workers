package com.example.workers;

import static com.example.workers.Constants.BREAK_REASON_COLUMN;
import android.content.ContentValues;

public class BreakReason implements Entity {
	String reason;

	public ContentValues toRow() {
		ContentValues values = new ContentValues();
		values.put(BREAK_REASON_COLUMN, reason);
		return values;
	}
}

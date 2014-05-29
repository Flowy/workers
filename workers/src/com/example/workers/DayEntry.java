package com.example.workers;

import static com.example.workers.Constants.DAY_ENTRY_END;
import static com.example.workers.Constants.DAY_ENTRY_FK_PERSON_ID;
import static com.example.workers.Constants.DAY_ENTRY_START;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class DayEntry implements Entity {
	Date start;
	Date end;
	String person;

	private DayEntry() {
		start = null;
		end = null;
		person = null;
	}

	public DayEntry(Date start, String person) {
		this.start = start;
		this.person = person;
	}

	@Override
	public String toString() {
		return "Person: " + person + " working from: " + start.toString();
	}

	public ContentValues toRow() {
		ContentValues values = new ContentValues();
		if (start != null) {
			values.put(DAY_ENTRY_START, start.getTime());
		}
		if (end != null) {
			values.put(DAY_ENTRY_END, end.getTime());
		}
		values.put(DAY_ENTRY_FK_PERSON_ID, person);
		return values;
	}

	public static DayEntry parseCursor(Cursor cursor) {
		DayEntry result = new DayEntry();
		result.person = cursor.getString(cursor
				.getColumnIndex(DAY_ENTRY_FK_PERSON_ID));
		long startTime = cursor.getLong(cursor.getColumnIndex(DAY_ENTRY_START));
		long endTime = cursor.getLong(cursor.getColumnIndex(DAY_ENTRY_END));
		if (startTime != 0) {
			result.start = new Date(startTime);
		}
		if (endTime != 0) {
			result.end = new Date(endTime);
		}
		return result;
	}
}

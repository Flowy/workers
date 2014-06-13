package com.flowy.workers.db;

import static com.flowy.workers.Constants.*;

import java.util.Date;

import android.content.ContentValues;

public class WorkBreak implements Entity {
	Date start;
	Date end;
	String reason;
	Date dayStart;
	String person;

	WorkBreak(Date start, String reason, Date dayStart, String person) {
		this.start = start;
		this.reason = reason;
		this.dayStart = dayStart;
		this.person = person;
	}

	public ContentValues toRow() {
		ContentValues values = new ContentValues();
		values.put(WORK_BREAK_START, start.getTime());
		values.put(WORK_BREAK_END, end.getTime());
		values.put(WORK_BREAK_FK_REASON, reason);
		values.put(WORK_BREAK_FK_DAY_START, dayStart.getTime());
		values.put(WORK_BREAK_FK_DAY_PERSON, person);
		return values;
	}
}

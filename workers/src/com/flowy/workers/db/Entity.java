package com.flowy.workers.db;

import android.content.ContentValues;

public interface Entity {
	public ContentValues toRow();
}

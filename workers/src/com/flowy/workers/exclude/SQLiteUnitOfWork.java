package com.flowy.workers.db;

import android.database.sqlite.SQLiteDatabase;

import com.flowy.workers.UnitOfWork;
import com.flowy.workers.UnitOfWork.Transaction;

public class SQLiteUnitOfWork implements UnitOfWork {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public SQLiteUnitOfWork(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.db = dbHelper.getWritableDatabase();
	}

	@Override
	public void commit() {
		try {
			if (db.inTransaction()) {
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
			dbHelper.close();
		}
	}

	@Override
	public UnitOfWork startTransaction(Transaction transaction) {
		db.beginTransaction();
		try {
			transaction.execute();
		} catch (Exception e) {
			db.endTransaction();
		}
		return this;
	}

}

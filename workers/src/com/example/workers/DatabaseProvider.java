package com.example.workers;

import static com.example.workers.Constants.*;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class DatabaseProvider extends ContentProvider {

	private DatabaseHelper mDBHelper;

	final static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			// database.setForeignKeyConstraintsEnabled(true);
			database.execSQL("CREATE TABLE " + PERSON_TABLE + "("
					+ PERSON_COMPANY_ID + " TEXT NOT NULL PRIMARY KEY, "
					+ PERSON_PASSWORD_HASH + " TEXT) ");
			database.execSQL("CREATE TABLE " + QR_TABLE + "(" + QR_CODE
					+ " TEXT NOT NULL PRIMARY KEY, " + QR_NAME + " TEXT, "
					+ QR_FK_PERSON_ID + " TEXT REFERENCES " + PERSON_TABLE
					+ "(" + PERSON_COMPANY_ID + "))");
			database.execSQL("CREATE TABLE " + DAY_ENTRY_TABLE + "("
					+ DAY_ENTRY_START + " NUMERIC NOT NULL, " + DAY_ENTRY_END
					+ " NUMERIC, " + DAY_ENTRY_FK_PERSON_ID
					+ " TEXT NOT NULL REFERENCES " + PERSON_TABLE + " ("
					+ PERSON_COMPANY_ID + "), CONSTRAINT "
					+ DAY_ENTRY_PRIMARY_KEY + " PRIMARY KEY ("
					+ DAY_ENTRY_START + ", " + DAY_ENTRY_FK_PERSON_ID + "))");
			database.execSQL("CREATE TABLE " + BREAK_REASON_TABLE + "("
					+ BREAK_REASON_COLUMN + " TEXT NOT NULL PRIMARY KEY )");
			database.execSQL("CREATE TABLE " + WORK_BREAK_TABLE + "("
					+ WORK_BREAK_START + " NUMERIC NOT NULL, " + WORK_BREAK_END
					+ "NUMERIC NOT NULL, " + WORK_BREAK_FK_REASON
					+ " TEXT NOT NULL REFERENCES " + BREAK_REASON_TABLE + " ("
					+ BREAK_REASON_COLUMN + "), " + WORK_BREAK_FK_DAY_START
					+ " NUMERIC NOT NULL REFERENCES " + DAY_ENTRY_TABLE + " ("
					+ DAY_ENTRY_START + "), " + WORK_BREAK_FK_DAY_PERSON
					+ " TEXT NOT NULL REFERENCES " + DAY_ENTRY_TABLE + " ("
					+ DAY_ENTRY_FK_PERSON_ID + "), CONSTRAINT "
					+ WORK_BREAK_PRIMARY_KEY + " PRIMARY KEY ("
					+ WORK_BREAK_START + ", " + WORK_BREAK_FK_DAY_START + ", "
					+ WORK_BREAK_FK_DAY_PERSON + "))");
			if (LOCAL_LOGV) {
				Log.v(LOG_TAG,
						"Integrity check OK: "
								+ database.isDatabaseIntegrityOk());
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + QR_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DAY_ENTRY_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + BREAK_REASON_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + WORK_BREAK_TABLE);
			onCreate(db);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			if (!db.isReadOnly()) {
				// Enable foreign key constraints
				db.setForeignKeyConstraintsEnabled(true);
			}
		}
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] proj, String sel, String[] selArg,
			String order) {

		boolean distinct = uri.getQueryParameter("distinct") == "true" ? true
				: false;
		String table = uri.getQueryParameter("table");
		String[] columns = proj;
		String selection = sel;
		String[] selectionArgs = selArg;
		String groupBy = uri.getQueryParameter("groupBy");
		String having = uri.getQueryParameter("having");
		String orderBy = order;
		String limit = uri.getQueryParameter("limit");

		if (LOCAL_LOGV) {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			if (proj != null) {
				sb.append(Arrays.toString(proj));
			}
			sb.append(" FROM " + table + " WHERE " + selection + " ARGS: ");
			if (selectionArgs != null) {
				sb.append(Arrays.toString(selectionArgs));
			}

			Log.v(LOG_TAG, sb.toString());
		}

		Cursor cursor = null;
		SQLiteDatabase database = mDBHelper.getReadableDatabase();

		cursor = database.query(distinct, table, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/**
	 * needs to check if id is -1
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = uri.getQueryParameter("table");

		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			database.beginTransaction();
			// TODO: check if result isnt -1 -> not inserted
			database.insert(table, null, values);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ((database != null) && database.inTransaction()) {
				database.endTransaction();
			}
		}
		if (LOCAL_LOGV) {
			Log.v(LOG_TAG, "New inserted entity");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri
				.parse(CONTENT_URI)
				.buildUpon()
				.appendQueryParameter("table", PERSON_TABLE)
				.appendQueryParameter(PERSON_COMPANY_ID,
						values.getAsString(PERSON_COMPANY_ID)).build();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String table = uri.getQueryParameter("table");
		int rowsAffected = 0;
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			database.beginTransaction();
			rowsAffected = database.update(table, values, selection,
					selectionArgs);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ((database != null) && database.inTransaction()) {
				database.endTransaction();
			}
		}
		return rowsAffected;
	}

}

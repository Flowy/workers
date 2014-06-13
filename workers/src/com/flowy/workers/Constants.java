package com.flowy.workers;

public class Constants {

	public final static boolean LOCAL_LOG = true;
	public final static boolean LOCAL_LOGV = true;
	public final static String LOG_TAG = "DEBUG_TAG";

	public final static String INTENT_EXTRA_CHOOSEN_ID = "choosenPersonId";
	public final static com.google.common.hash.HashFunction HASH_FUNCTION = com.google.common.hash.Hashing
			.sha256();

	public final static String PERSON_TABLE = "PERSON";
	public final static String PERSON_COMPANY_ID = "PERSONAL_ID";
	public final static String PERSON_PASSWORD_HASH = "PASSWORD";
	public final static String PERSON_QR_CODE = "QR_CODE";
	public final static String PERSON_NFC_CODE = "NFC_CODE";
	public final static String PERSON_LAST_UPDATED = "LAST_UPDATED";

	public final static String QR_TABLE = "QR_CARD";
	public final static String QR_ID = "_id";
	public final static String QR_CODE = "CODE";
	public final static String QR_NAME = "NAME";
	public final static String QR_FK_PERSON_ID = "PERSON_ID";

	public final static String DAY_ENTRY_TABLE = "WORK_DAY_ENTRY";
	public final static String DAY_ENTRY_START = "WORK_DAY_START";
	public final static String DAY_ENTRY_END = "WORK_DAY_END";
	public final static String DAY_ENTRY_FK_PERSON_ID = "PERSON_ID";
	public final static String DAY_ENTRY_PRIMARY_KEY = "work_day_pk";

	public final static String WORK_BREAK_TABLE = "WORK_BREAK";
	public final static String WORK_BREAK_START = "WORK_BREAK_START";
	public final static String WORK_BREAK_END = "WORK_BREAK_START";
	public final static String WORK_BREAK_FK_REASON = "WORK_BREAK_REASON";
	public final static String WORK_BREAK_FK_DAY_START = "WORK_DAY_START";
	public final static String WORK_BREAK_FK_DAY_PERSON = "PERSON_ID";
	public final static String WORK_BREAK_PRIMARY_KEY = "work_break_pk";

	public final static String BREAK_REASON_TABLE = "BREAK_REASON";
	public final static String BREAK_REASON_COLUMN = "BREAK_REASON";

	public final static String AUTHORITY = "com.flowy.workers.provider";
	public final static String CONTENT_URI = "content://" + AUTHORITY + "/";

	// final static String PERSONS_PATH = "person/";
	// final static String QR_PATH = "qr/";
	//
	// final static int REQUEST_PERSONS = 1;
	// final static int INSERT_PERSON = 1;
	// final static int REQUEST_PERSON_BY_ID = 2;
	// final static int REQUEST_QRS = 3;
	// final static int INSERT_QR = 3;
	// final static int REQUEST_PERSON_BY_QR_CODE = 4;
	// final static int REQUEST_UNCLOSED_DAY_FOR_PERSON = 5;
	public final static String[] PERSON_PROJECTION = { PERSON_COMPANY_ID,
		PERSON_COMPANY_ID, PERSON_PASSWORD_HASH };
	public final static String[] QR_PROJECTION = { QR_ID, QR_CODE, QR_NAME,
		QR_FK_PERSON_ID };
	public final static String[] DAY_ENTRY_PROJECTION = { DAY_ENTRY_START,
		DAY_ENTRY_END, DAY_ENTRY_FK_PERSON_ID };

}

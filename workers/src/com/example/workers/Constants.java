package com.example.workers;

public class Constants {

	final static boolean LOCAL_LOG = true;
	final static boolean LOCAL_LOGV = true;
	final static String LOG_TAG = "DEBUG_TAG";

	final static String INTENT_EXTRA_CHOOSEN_ID = "choosenPersonId";
	final static com.google.common.hash.HashFunction HASH_FUNCTION = com.google.common.hash.Hashing
			.sha256();

	final static String PERSON_TABLE = "PERSON";
	final static String PERSON_ID = "_id";
	final static String PERSON_COMPANY_ID = "PERSONAL_ID";
	final static String PERSON_PASSWORD_HASH = "PASSWORD";

	final static String QR_TABLE = "QR_CARD";
	final static String QR_ID = "_id";
	final static String QR_CODE = "CODE";
	final static String QR_NAME = "NAME";
	final static String QR_FK_PERSON_ID = "PERSON_ID";

	final static String DAY_ENTRY_TABLE = "WORK_DAY_ENTRY";
	final static String DAY_ENTRY_START = "WORK_DAY_START";
	final static String DAY_ENTRY_END = "WORK_DAY_END";
	final static String DAY_ENTRY_FK_PERSON_ID = "PERSON_ID";
	final static String DAY_ENTRY_PRIMARY_KEY = "work_day_pk";

	final static String WORK_BREAK_TABLE = "WORK_BREAK";
	final static String WORK_BREAK_START = "WORK_BREAK_START";
	final static String WORK_BREAK_END = "WORK_BREAK_START";
	final static String WORK_BREAK_FK_REASON = "WORK_BREAK_REASON";
	final static String WORK_BREAK_FK_DAY_START = "WORK_DAY_START";
	final static String WORK_BREAK_FK_DAY_PERSON = "PERSON_ID";
	final static String WORK_BREAK_PRIMARY_KEY = "work_break_pk";

	final static String BREAK_REASON_TABLE = "BREAK_REASON";
	final static String BREAK_REASON_COLUMN = "BREAK_REASON";

	final static String AUTHORITY = "com.flowy.workers.provider";
	final static String CONTENT_URI = "content://" + AUTHORITY + "/";

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
	final static String[] PERSON_PROJECTION = { PERSON_COMPANY_ID,
		PERSON_COMPANY_ID, PERSON_PASSWORD_HASH };
	final static String[] QR_PROJECTION = { QR_ID, QR_CODE, QR_NAME,
		QR_FK_PERSON_ID };
	final static String[] DAY_ENTRY_PROJECTION = { DAY_ENTRY_START,
		DAY_ENTRY_END, DAY_ENTRY_FK_PERSON_ID };

}

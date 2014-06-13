package com.flowy.workers.db;

import com.flowy.workers.Constants;

import android.content.ContentValues;

public class QRCard implements Entity {

	public String code;
	public String name;
	public String personIdFK;

	QRCard(String code, String name, String personIdFK) {
		this.code = code;
		this.name = name;
		this.personIdFK = personIdFK;
	}

	public QRCard(String code, String personIdFK) {
		this.code = code;
		this.personIdFK = personIdFK;
	}

	// String getCode() {
	// return code;
	// }
	//
	// String getName() {
	// return name;
	// }
	//
	// String getPersonIdFK() {
	// return personIdFK;
	// }
	//
	// void setCode(String code) {
	// this.code = code;
	// }
	//
	// void setName(String name) {
	// this.name = name;
	// }
	//
	// void setPersonIdFK(String personId) {
	// this.personIdFK = personId;
	// }

	public ContentValues toRow() {
		ContentValues values = new ContentValues();
		values.put(Constants.QR_CODE, code);
		values.put(Constants.QR_NAME, name);
		if (personIdFK != "") {
			values.put(Constants.QR_FK_PERSON_ID, personIdFK);
		}
		return values;
	}

}

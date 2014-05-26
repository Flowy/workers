package com.example.workers;

import static com.example.workers.Constants.HASH_FUNCTION;
import static com.example.workers.Constants.PERSON_COMPANY_ID;
import static com.example.workers.Constants.PERSON_PASSWORD_HASH;

import java.nio.charset.Charset;
import java.util.Random;

import android.content.ContentValues;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;

class Person implements Entity {

	private String password;
	String companyId;

	public Person() {
		int number = new Random().nextInt(1000);
		this.password = "r";
		this.companyId = "id" + number;
	}

	public Person(String personalId) {
		this.companyId = personalId;
	}

	public String getPassword() {
		return password;
	}

	// public String getPeronalId() {
	// return companyId;
	// }

	public void setPassword(String password) {
		Hasher hasher = HASH_FUNCTION.newHasher();
		HashCode hc = hasher.putString(password, Charset.defaultCharset())
				.hash();
		this.password = hc.toString();
	}

	/*
	 * public String getTitle() { return title; } public String getMiddleName()
	 * { return middleName; } public String getBirthDate() { return birthDate; }
	 * public String getTelephoneNumber() { return telephoneNumber; } public
	 * String getMailAddress() { return mailAddress; } public String
	 * getAddress() { return address; }
	 * 
	 * 
	 * public void setTitle(String title) { this.title = title; } public void
	 * setFirstName(String firstName) { this.firstName = firstName; } public
	 * void setMiddleName(String middleName) { this.middleName = middleName; }
	 * public void setBirthDate(String birthDate) { this.birthDate = birthDate;
	 * } public void setTelephoneNumber(String telephoneNumber) {
	 * this.telephoneNumber = telephoneNumber; } public void
	 * setMailAddress(String mailAddress) { this.mailAddress = mailAddress; }
	 * public void setAddress(String address) { this.address = address; }
	 */

	public ContentValues toRow() {
		ContentValues values = new ContentValues();
		/*
		 * values.put(Constants.PERSON_COLUMN_TITLE, getTitle());
		 * values.put(Constants.PERSON_FIRSTNAME, getFirstName());
		 * values.put(Constants.PERSON_MIDDLENAME, getMiddleName());
		 * values.put(Constants.PERSON_LASTNAME, getLastName());
		 */
		values.put(PERSON_COMPANY_ID, companyId);
		values.put(PERSON_PASSWORD_HASH, getPassword());
		return values;
	}

	@Override
	public String toString() {
		return companyId;
	}
}

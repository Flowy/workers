package com.example.workers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PersonDataSource {
	SQLiteDatabase database;
	MySQLiteHelper dbHelper;
	
	//TODO: remove
	Context context;
	
	public PersonDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
		this.context = context;
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * 
	 * @param person
	 * @return null if insert was not successful
	 */
	public Person createPerson(Person person) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.personIdColumn, person.getId());
		values.put(MySQLiteHelper.personLastNameColumn, person.getLastName());
		values.put(MySQLiteHelper.personFirstNameColumn, person.getFirstName());
		values.put(MySQLiteHelper.personTitleColumn, person.getTitle());
		values.put(MySQLiteHelper.personMiddleNameColumn, person.getMiddleName());
		values.put(MySQLiteHelper.personBirthDateColumn, person.getBirthDate());
		values.put(MySQLiteHelper.personTelephoneNumberColumn, person.getTelephoneNumber());
		values.put(MySQLiteHelper.personMailAddressColumn, person.getMailAddress());
		values.put(MySQLiteHelper.personAddressColumn, person.getAddress());
		
		long insertId = database.insert(MySQLiteHelper.personTable, null, values);
		Person newPerson = null;
		if (insertId == -1) {
			//throw error line not inserted
		} else {
			Cursor cursor = database.query(MySQLiteHelper.personTable, MySQLiteHelper.personAllColumns, MySQLiteHelper.personIdColumn + " = " + person.getId(), null, null, null, null);
			cursor.moveToFirst();
			newPerson = Person.cursorToPerson(cursor);
			cursor.close();
			
		}
		return newPerson;
	}
	
	public void deletePerson(Person person) {
		String id = person.getId();
		database.delete(MySQLiteHelper.personTable, MySQLiteHelper.personIdColumn + " = " + id, null);
	}
	
	public List<Person> getPersons(String[] columns, String where, String orderBy) {
		List<Person> personList = new ArrayList<Person>();
		
		Cursor cursor = database.query(MySQLiteHelper.personTable, columns, where, null, null, null, orderBy);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Person person = Person.cursorToPerson(cursor);
			personList.add(person);
			cursor.moveToNext();
		}
		cursor.close();
		
		return personList;
	}
	

	
}

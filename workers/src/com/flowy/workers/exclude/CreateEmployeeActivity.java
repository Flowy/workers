package com.flowy.workers;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CreateEmployeeActivity extends Activity {

	private PersonDataSource datasource;
	EditText firstName, lastName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_employee);

		datasource = new PersonDataSource(this);
		datasource.open();
		
		firstName = (EditText) findViewById(R.id.et_firstName);
		lastName = (EditText) findViewById(R.id.et_lastName);
		
		Button create = (Button) findViewById(R.id.buttonOk);
		create.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addPerson();
			}
		});
		
	}

	private void addPerson() {
		String name = firstName.getText().toString();
		String surname = lastName.getText().toString();
		Person person = new Person(name, surname);
		datasource.createPerson(person);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_employee, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

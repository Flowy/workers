package com.example.workers;

import static com.example.workers.Constants.*;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.workers.barcodeScannerImpl.IntentIntegrator;

public class MainActivity extends Activity {

	// Button createEmployee, chooseEmployee;
	ContentResolver contentResolver;

	static final int CHOOSE_PERSON_REQUEST = 2;
	static final int SCAN_QR_REQUEST = 3;

	LoggingRequest logRequest = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentResolver = getContentResolver();

		Cursor c = contentResolver.query(Uri.parse(CONTENT_URI).buildUpon()
				.appendQueryParameter("table", PERSON_TABLE).build(),
				PERSON_PROJECTION, null, null, null);
		if (LOCAL_LOGV) {
			Log.v(LOG_TAG, "Database initialized: "
					+ (c.getCount() == 0 ? "false" : "true"));
		}
		if (c.getCount() == 0) {
			initdb();
		}
	}

	void initdb() {
		ContentResolver cr = contentResolver;

		ArrayList<String> list = new ArrayList<String>();
		list.add("1417|Juraj Moric");
		list.add("1418|Peter Klein");
		list.add("1419|Radovan Èulok");
		list.add("1420|Miloš Neboška");
		list.add("1421|Mária Janásková");
		list.add("1422|Martina Moricová");
		list.add("1423|Adriana Boïová");
		list.add("1424|Miroslava Rizmanová");
		list.add("1425|Ladislav Varga");
		list.add("1426|Peter Peèner");

		for (String row : list) {
			Person newPerson = new Person(row.substring(5));
			QRCard newCard = new QRCard(row, newPerson.companyId);
			cr.insert(
					Uri.parse(CONTENT_URI).buildUpon()
					.appendQueryParameter("table", PERSON_TABLE)
					.build(), newPerson.toRow());
			cr.insert(
					Uri.parse(CONTENT_URI).buildUpon()
					.appendQueryParameter("table", QR_TABLE).build(),
					newCard.toRow());
		}
	}

	public void choosePersonButtonClick(View view) {

		// Intent choosePerson = new Intent(Intent.ACTION_PICK, null, this,
		// ChooseEmployeeActivity.class);
		// startActivityForResult(choosePerson, CHOOSE_PERSON_REQUEST);
	}

	public void arrivalButtonClick(View view) {
		logRequest = new ArrivalRequest(this);
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan(SCAN_QR_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == CHOOSE_PERSON_REQUEST) {
				String personId = data.getStringExtra(INTENT_EXTRA_CHOOSEN_ID);
				Toast.makeText(this, "Choosen: " + personId, Toast.LENGTH_LONG)
						.show();
			} else if (requestCode == SCAN_QR_REQUEST) {
				String scanResult = data.getStringExtra("SCAN_RESULT");
				if (LOCAL_LOGV) {
					Log.v(LOG_TAG, "QR scan result: " + scanResult);
				}
				if (logRequest != null) {
					logRequest.parseQRCode(scanResult);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

}

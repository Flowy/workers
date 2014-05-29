package com.example.workers;

import static com.example.workers.Constants.*;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
			contentResolver.insert(Uri.parse(CONTENT_URI).buildUpon()
					.appendQueryParameter("table", PERSON_TABLE).build(),
					newPerson.toRow());
			contentResolver.insert(Uri.parse(CONTENT_URI).buildUpon()
					.appendQueryParameter("table", QR_TABLE).build(),
					newCard.toRow());
		}
	}

	public void choosePersonButtonClick(View view) {
		writeDB();
		// Intent choosePerson = new Intent(Intent.ACTION_PICK, null, this,
		// ChooseEmployeeActivity.class);
		// startActivityForResult(choosePerson, CHOOSE_PERSON_REQUEST);
	}

	private void writeDB() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(path, "database.txt");

			BufferedWriter bw = null;
			try {
				path.mkdirs();
				boolean exists = path.exists();
				if (!exists) {
					Toast.makeText(
							this,
							"Cant create folder on external storage for writing database",
							Toast.LENGTH_LONG).show();
					return;
				}
				bw = new BufferedWriter(new FileWriter(file, false));
				// TODO: add date selection
				Cursor cursor = contentResolver.query(
						Uri.parse(CONTENT_URI).buildUpon()
								.appendQueryParameter("table", DAY_ENTRY_TABLE)
								.build(), DAY_ENTRY_PROJECTION, null, null,
						DAY_ENTRY_FK_PERSON_ID);
				cursor.moveToFirst();
				String formatString = "%-20s%30s%30s%30s";
				String header = String.format(formatString, "PERSON", "START",
						"END", "DIFFERENCE");
				bw.write(header);
				bw.newLine();

				// DateFormat dateFormat = new SimpleDateFormat("d.M.Y H:m:s",
				// Locale.getDefault());
				DateFormat dateFormat = DateFormat.getDateTimeInstance();
				// DateFormat.MEDIUM, DateFormat.MEDIUM,
				// XXX: Locale.getDefault());
				String lastPerson = null;
				while (!cursor.isAfterLast()) {
					DayEntry actual = DayEntry.parseCursor(cursor);
					String startEndDiff = "";
					String start, end, person;
					if ((actual.start != null) && (actual.end != null)) {
						Date diff = new Date(actual.end.getTime()
								- actual.start.getTime());
						DateFormat timeFormat = DateFormat.getTimeInstance();
						timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						startEndDiff = timeFormat.format(diff);
					}
					if (actual.start == null) {
						start = "Not started";
					} else {
						start = dateFormat.format(actual.start);
					}
					if (actual.end == null) {
						end = "Not ended";
					} else {
						end = dateFormat.format(actual.end);
					}
					if ((actual.person == null)
							|| actual.person.equals(lastPerson)) {
						person = "";
					} else {
						person = actual.person;
						lastPerson = actual.person;
					}
					String line = String.format(formatString, person, start,
							end, startEndDiff);
					bw.write(line);
					bw.newLine();
					cursor.moveToNext();
				}
				cursor.close();
				Toast.makeText(
						this,
						"File with data from database saved into sdcard/downloads/database.txt",
						Toast.LENGTH_LONG).show();
				MediaScannerConnection.scanFile(this,
						new String[] { file.toString() }, null, null);
			} catch (IOException e) {

			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			Toast.makeText(this, "Cant access external storage for write",
					Toast.LENGTH_LONG).show();
		}

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

package com.flowy.workers;

import static com.flowy.workers.Constants.*;

import java.nio.charset.Charset;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.example.workers.R;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;

public class ChooseEmployeeActivity extends ListActivity implements
		LoaderCallbacks<Cursor>, OnItemClickListener {

	private SimpleCursorAdapter mCursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_employee);
		setListView();
	}

	private void setListView() {
		getLoaderManager().initLoader(0, null, this);
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.person_row,
				null, new String[] { Constants.PERSON_ID,
						Constants.PERSON_COMPANY_ID }, new int[] {
						R.id.labelId, R.id.labelPersonalId }, 0);
		setListAdapter(mCursorAdapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_employee, menu);
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
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(this, Uri
				.parse(CONTENT_URI).buildUpon()
				.appendQueryParameter("table", PERSON_TABLE).build(),
				PERSON_PROJECTION, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mCursorAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Cursor item = (Cursor) parent.getItemAtPosition(position);

		askForPassword(item);
	}

	private Cursor cursorItem = null;

	private void askForPassword(Cursor item) {
		cursorItem = item;

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.fragment_password_dialog, null);

		final EditText passwordEditText = (EditText) view
				.findViewById(R.id.et_password);

		new AlertDialog.Builder(this)
				.setTitle(R.string.passwordDialogTitle)
				.setView(view)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								doPositiveClick(passwordEditText.getText()
										.toString());
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								doNegativeClick();
							}
						}).show();
	}

	private void doPositiveClick(String password) {
		if (cursorItem != null) {
			String passwordOrigin = cursorItem.getString(cursorItem
					.getColumnIndex(PERSON_PASSWORD_HASH));
			String passwordHash = null;
			if (password != null) {
				Hasher hasher = HASH_FUNCTION.newHasher();
				HashCode hc = hasher.putString(password,
						Charset.defaultCharset()).hash();
				passwordHash = hc.toString();
			}
			if (LOCAL_LOGV) {
				Log.v(LOG_TAG, "Origin: " + passwordOrigin + " Entry: "
						+ passwordHash);
			}
			if (((passwordOrigin == null) && (passwordHash == null))
					|| ((passwordHash != null) && (passwordOrigin != null) && passwordOrigin
							.equals(passwordHash))) {
				// return RESULT_OK
				finish();
			} else {
				doNegativeClick();
			}
		}

	}

	public void doNegativeClick() {
		cursorItem = null;
		Toast.makeText(this, R.string.wrongPassword, Toast.LENGTH_LONG).show();
	}

	@Override
	public void finish() {
		if (cursorItem == null) {
			setResult(RESULT_CANCELED);
		} else {
			Intent intent = new Intent();
			intent.putExtra(INTENT_EXTRA_CHOOSEN_ID,
					cursorItem.getString(cursorItem.getColumnIndex(PERSON_ID)));
			cursorItem = null;
			setResult(RESULT_OK, intent);
		}
		super.finish();
	}
}

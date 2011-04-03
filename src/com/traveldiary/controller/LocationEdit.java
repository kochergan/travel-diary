package com.traveldiary.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.traveldiary.bs.HttpRequestPerformer;
import com.traveldiary.bs.Response;
import com.traveldiary.pd.Location;
import com.traveldiary.persistence.Database;

public class LocationEdit extends Activity {
	private EditText mTitleText;
	private EditText mBodyText;
	private TextView mDateText;
	private TextView mLongituteText;
	private TextView mLatitudeText;
	private Long mRowId;
	private Database mDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDatabase = new Database(this);
		mDatabase.open();

		setContentView(R.layout.location_edit);
		setTitle(R.string.view_location);

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		mDateText = (TextView) findViewById(R.id.date);
		mLongituteText = (TextView) findViewById(R.id.longitude);
		mLatitudeText = (TextView) findViewById(R.id.latitude);

		Button confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}
		});

		Button mShowMapButton = (Button) findViewById(R.id.show_map);
		mShowMapButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				showLocationOnMap();
			}
		});

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState.getSerializable(Database.KEY_ROWID);

		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(Database.KEY_ROWID) : null;
		}

		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(Database.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void populateFields() {
		// edit
		if (mRowId != null) {
			showInformationFromDatabase();
			showInformationFromServer();
		} else { // new
			showLocation();
		}
	}

	private void showInformationFromServer() {
		Location remoteLocation = new Location(mRowId);
		Response response = HttpRequestPerformer.getInfo(remoteLocation);

		if (response == null) {
			Log.w("Response", "Empty response");
		} else {
			Log.i("Response", String.valueOf(response.getStatus()));
			mBodyText.setText(response.getLocation().getText());
			mLatitudeText.setText(String.valueOf(response.getLocation()
					.getLatitude()));
			mLongituteText.setText(String.valueOf(response.getLocation()
					.getLongitude()));
		}
	}

	private void showInformationFromDatabase() {
		Cursor location = mDatabase.getLocation(mRowId);
		startManagingCursor(location);
		mTitleText.setText(location.getString(location
				.getColumnIndexOrThrow(Database.KEY_TITLE)));
		mDateText.setText(location.getString(location
				.getColumnIndexOrThrow(Database.KEY_DATETIME)));
	}

	private void showLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		android.location.Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			mLatitudeText.setText(String.valueOf(lat));
			mLongituteText.setText(String.valueOf(lng));
		} else {
			mLatitudeText.setText(R.string.gps_not_available);
			mLongituteText.setText(R.string.gps_not_available);
		}
	}

	private void showLocationOnMap() {
		Intent i = new Intent(this, Pathfinder.class);
		startActivity(i);
	}

	private void saveState() {
		if (mRowId == null) {
			String title = mTitleText.getText().toString();
			String text = mBodyText.getText().toString();
			long date = Integer.parseInt(mDateText.getText().toString());
			double latitude = Double.parseDouble(String.valueOf(mLatitudeText
					.getText()));
			double longitude = Double.parseDouble(String.valueOf(mLongituteText
					.getText()));

			Location location = new Location(0, title, text, date, latitude,
					longitude);
			Response response = HttpRequestPerformer.postInfo(location);

			if (response == null) {
				Log.w("Response", "Empty response");
			} else {
				Log.i("Response", String.valueOf(response.getStatus()));
				mRowId = response.getLocation().getId();
			}

			mDatabase.addLocation(response.getLocation());
		}
	}
}

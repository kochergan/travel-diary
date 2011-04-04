package com.traveldiary.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.LocationListener;
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

public class LocationEdit extends Activity implements LocationListener {
	private static SimpleDateFormat date_format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private EditText mTitleText;
	private EditText mBodyText;
	private TextView mDateText;
	private TextView mLongituteText;
	private TextView mLatitudeText;
	private Long mRowId;
	private Database mDatabase;
	private LocationManager mLocationMgr;
	private String mBest;

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

		startLocationRetrieval();
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

		if (isNewLocationState()) {
			mLocationMgr.removeUpdates(this);
		}

		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isNewLocationState()) {
			mLocationMgr.requestLocationUpdates(mBest, 15000, 1, this);
		}

		populateFields();
	}

	private boolean isNewLocationState() {
		return mRowId == null;
	}

	private void populateFields() {
		if (!isNewLocationState()) {
			showInformationFromDatabase();
			showInformationFromServer();
		} else {
			showCurrentTime();
		}
	}

	private void showCurrentTime() {
		String date = date_format.format(new Date());
		mDateText.setText(date);
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
		String date = date_format.format(new Date(location.getLong(location
				.getColumnIndexOrThrow(Database.KEY_DATETIME))));
		mDateText.setText(date);
	}

	private void startLocationRetrieval() {
		mLocationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		mBest = mLocationMgr.getBestProvider(criteria, true);
		android.location.Location location = mLocationMgr
				.getLastKnownLocation(mBest);
		showLocationInformation(location);
	}

	private void showLocationInformation(android.location.Location location) {
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
		Intent i = new Intent(this, com.traveldiary.controller.Pathfinder.class);
		startActivity(i);
	}

	private void saveState() {
		if (mRowId == null) {
			String title = mTitleText.getText().toString();
			String text = mBodyText.getText().toString();
			long date;

			try {
				date = date_format.parse(mDateText.getText().toString())
						.getTime();
			} catch (ParseException e) {
				Log.w("Problem", "Problem parsing date");
				date = 0;
			}

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

	public void onLocationChanged(android.location.Location location) {
		showLocationInformation(location);
	}

	public void onProviderDisabled(String provider) {
		Log.i("Location", "Provider disabled: " + provider);
	}

	public void onProviderEnabled(String provider) {
		Log.i("Location", "Provider enabled: " + provider);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("Location", "Status changed: " + provider);
	}
}

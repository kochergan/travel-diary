package com.traveldiary.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.traveldiary.pd.Location;

public class Database {
	public static final String KEY_TITLE = "title";
	public static final String KEY_DATETIME = "datetime";
	public static final String KEY_ROWID = "_id";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "traveldiary.db";
	private static final String TABLE_NAME = "locations";
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
			+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY," + KEY_TITLE
			+ " TEXT NOT NULL," + KEY_DATETIME + " TEXT NOT NULL)";

	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	public Database(Context ctx) {
		this.mCtx = ctx;
	}

	public Database open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	public Cursor getLocations() {
		return mDb.query(TABLE_NAME, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_DATETIME }, null, null, null, null, KEY_DATETIME);
	}

	public boolean addLocation(Location loc) {
		long ret = mDb.insert(TABLE_NAME, null, getContentValues(loc));

		return ret >= 0;
	}

	public boolean modLocation(Location loc) {
		long ret = mDb.update(TABLE_NAME, getContentValues(loc), KEY_ROWID
				+ "=?", new String[] { Long.toString(loc.getId()) });

		return ret >= 1;
	}

	public boolean delLocation(Location loc) {
		int ret = mDb.delete(TABLE_NAME, KEY_ROWID + "=?",
				new String[] { Long.toString(loc.getId()) });

		return ret >= 1;
	}

	private ContentValues getContentValues(Location loc) {
		ContentValues values = new ContentValues();

		values.put(KEY_ROWID, loc.getId());
		values.put(KEY_TITLE, loc.getTitle());
		values.put(KEY_DATETIME, loc.getTmstmp().toLocaleString());

		return values;
	}
}
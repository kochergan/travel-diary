package com.traveldiary.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.traveldiary.pd.Location;

public class Database extends SQLiteOpenHelper {
	
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "traveldiary.db";
    private static final String TABLE_NAME = "locations";

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
				   "id INTEGER PRIMARY KEY," +
				   "title TEXT NOT NULL," +
				   "datetime TEXT NOT NULL)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}
	
	public List<Location> getLocations(SQLiteDatabase db) {
		
		List<Location> lst = new ArrayList<Location>();
		Cursor crsr = db.query(TABLE_NAME, 
							   new String[] {"id", "title", "datetime"}, 
							   null, 
							   null, 
							   null, 
							   null, 
							   "datetime");
		
		while(crsr.moveToNext()) {
			lst.add(new Location(crsr.getInt(0),
								 crsr.getString(1),
								 new Date(Date.parse(crsr.getString(2)))));
		}
		
		return lst;
		
	}
	
	public boolean addLocation(SQLiteDatabase db, Location loc) {
		
		long ret = db.insert(TABLE_NAME, 
							 null, 
							 getContentValues(loc));
		
		if(ret >= 0) return true;
		else		 return false;
		
	}
	
	public boolean modLocation(SQLiteDatabase db, Location loc) {
		
		long ret = db.update(TABLE_NAME, 
							 getContentValues(loc),
							 "id=?", 
							 new String[] {Integer.toString(loc.getId())});
		
		if(ret >= 1) return true;
		else		 return false;
		
	}
	
	public boolean delLocation(SQLiteDatabase db, Location loc) {
		
		int ret = db.delete(TABLE_NAME, 
							"id=?", 
							new String[] {Integer.toString(loc.getId())});
		
		if(ret >= 1) return true;
		else         return false;
		
	}
	
	private ContentValues getContentValues(Location loc) {
		
		ContentValues values = new ContentValues();
		
		values.put("id", loc.getId());
		values.put("title", loc.getTitle());
		values.put("datetime", loc.getTmstmp().toLocaleString());
		
		return values;
		
	}

}
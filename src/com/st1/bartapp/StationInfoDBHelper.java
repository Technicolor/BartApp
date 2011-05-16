package com.st1.bartapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationInfoDBHelper extends SQLiteOpenHelper {
	
	private static final String STATIONINFO_DBNAME = "BartStationInfoDB";
	private static final int STATIONINFO_DBVERSION = 1;
	
	public static final String STINFDB_STNINFO_TABLE = "StationInfoTable";
	public static final String STINFDB_KEY_NAME = "Name";
	public static final String STINFDB_KEY_ABBR = "Abbr";
	
	public static final String STINFDB_KEY_LONG = "Long";
	public static final String STINFDB_KEY_LAT = "Lat";
	
	public static final String STINFDB_KEY_STREET = "Street";
	public static final String STINFDB_KEY_CITY = "City";
	public static final String STINFDB_KEY_COUNTY = "County";
	public static final String STINFDB_KEY_STATE = "State";
	public static final String STINFDB_KEY_ZIP = "Zip";
	
	//SQLite command for creating the table, with column defs
	private static final String DBCREATE_CMD =
		"CREATE TABLE " + STATIONINFO_DBNAME + " (" +
		STINFDB_KEY_NAME + " TEXT, " +
		STINFDB_KEY_ABBR + " TEXT, " +
		STINFDB_KEY_LONG + " TEXT, " +
		STINFDB_KEY_LAT + " TEXT, " +
		STINFDB_KEY_STREET + " TEXT, " +
		STINFDB_KEY_CITY + " TEXT, " +
		STINFDB_KEY_COUNTY + " TEXT, " +
		STINFDB_KEY_STATE + " TEXT, " +
		STINFDB_KEY_ZIP + " TEXT, " +
		");";

	//Mainly for onCreate, indicates if the dB is populated, or if the data is being downloaded
	private boolean isValid = false;
	
	public StationInfoDBHelper(Context context) {
		super(context, STATIONINFO_DBNAME, null, STATIONINFO_DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBCREATE_CMD);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		isValid = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public boolean isDBValid () {
		return isValid;
	}
}

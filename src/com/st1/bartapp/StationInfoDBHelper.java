package com.st1.bartapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationInfoDBHelper extends SQLiteOpenHelper {	
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

	//Route info, northbound and southbound routes.
	//Route strings are stored according to AbstractCollection.toString().
	public static final String STINFDB_KEY_NROUTES = "NRoutes";
	public static final String STINFDB_KEY_SROUTES = "SRoutes";

	//Platform Info, northbound and southbound platform numbers
	//platform numbers are stored according to AbstractCollection.toString().
	public static final String STINFDB_KEY_NPLATFORMS = "NPlatforms";
	public static final String STINFDB_KEY_SPLATFORMS = "SPlatforms";
	public static final String STINFDB_KEY_PLATINFO = "PlatformInfo";

	public static final String STINFDB_KEY_INTRO = "Intro";
	public static final String STINFDB_KEY_CROSSST = "CrossSt";
	public static final String STINFDB_KEY_FOOD = "Food";
	public static final String STINFDB_KEY_SHOP = "Shopping";
	public static final String STINFDB_KEY_ATTR = "Attractions";
	public static final String STINFDB_KEY_HLINK = "HLink";

	//SQLite command for creating the table, with column defs
	private static final String STNINFOTABLE_CREATE_CMD =
		"CREATE TABLE " + STINFDB_STNINFO_TABLE + " (" +
		STINFDB_KEY_NAME + " TEXT, " +
		STINFDB_KEY_ABBR + " TEXT, " +
		
		STINFDB_KEY_LONG + " TEXT, " +
		STINFDB_KEY_LAT + " TEXT, " +
		
		STINFDB_KEY_STREET + " TEXT, " +
		STINFDB_KEY_CITY + " TEXT, " +
		STINFDB_KEY_COUNTY + " TEXT, " +
		STINFDB_KEY_STATE + " TEXT, " +
		STINFDB_KEY_ZIP + " TEXT, " +

		STINFDB_KEY_NROUTES + " TEXT, " +
		STINFDB_KEY_SROUTES + " TEXT, " +
		
		STINFDB_KEY_NPLATFORMS + " TEXT, " +
		STINFDB_KEY_SPLATFORMS + " TEXT, " +
		STINFDB_KEY_PLATINFO + " TEXT, " +

		STINFDB_KEY_INTRO + " TEXT, " +
		STINFDB_KEY_CROSSST + " TEXT, " +
		STINFDB_KEY_FOOD + " TEXT, " +
		STINFDB_KEY_SHOP + " TEXT, " +
		STINFDB_KEY_ATTR + " TEXT, " +
		STINFDB_KEY_HLINK + " TEXT, " +

		");";

	//Mainly for onCreate, indicates if the dB is populated, or if the data is being downloaded
	private boolean isValid = false;
	
	public StationInfoDBHelper(Context context) {
		super(context, StationInfoDB.STATIONINFO_DBNAME, null, StationInfoDB.STATIONINFO_DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(STNINFOTABLE_CREATE_CMD);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		//Currently only contains one table, but in future will need to check for table's presence
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

package com.st1.bartapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.Uri.Builder;

public class StationInfoProvider extends ContentProvider {
	
	public static final String STNPROVIDER_AUTHORITY = "com.st1.bartapp.stnprovider";
	
	public static final String STATIONLIST_URL = "/stations/list";
	public static final String STATIONINFO_URL = "/stations/info";

	private static final int STATIONROOT = 0;
	private static final int STATIONLIST = STATIONROOT+1;
	private static final int STATIONINFO = STATIONROOT+2;
	
	private static final UriMatcher sUriMatcher = new UriMatcher(STATIONROOT);
	static {
		sUriMatcher.addURI(STNPROVIDER_AUTHORITY, STATIONLIST_URL, STATIONLIST);
		sUriMatcher.addURI(STNPROVIDER_AUTHORITY, STATIONINFO_URL, STATIONINFO);
	}
	
	//Regexp for splitting up the platform and route information strings
	//stored in the database, as AbstractCollection.toString()
	//for example [1, 2, 3, 4]
	private static final String SPLIT_REGEXP = "\\[|,\\s+|\\]";
	
	private StationInfoDBHelper SIDBHelper;
	private SQLiteDatabase theStationsDB;
	
	public static Uri makeURI(String urlpath) {
		Builder b = new Uri.Builder();
		return b.authority(STNPROVIDER_AUTHORITY).path(urlpath).build();
	}

	@Override
	public boolean onCreate() {
		SIDBHelper = new StationInfoDBHelper (getContext());
		theStationsDB = SIDBHelper.getReadableDatabase();
		if (SIDBHelper.isDBValid()) {
		}
		return false;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = 0l;
		switch (sUriMatcher.match(uri)) {
		case STATIONLIST:
		case STATIONINFO:
			rowID = theStationsDB.insertWithOnConflict(StationInfoDBHelper.STINFDB_STNINFO_TABLE,
					"", values, SQLiteDatabase.CONFLICT_REPLACE);
			break;
		}
		
		if (rowID != -1) getContext().getContentResolver().notifyChange(uri, null);
		
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		switch (sUriMatcher.match(uri)) {
		case STATIONLIST:
			return theStationsDB.query(StationInfoDBHelper.STINFDB_STNINFO_TABLE,
					projection, selection, selectionArgs,
					null, null, sortOrder);
		case STATIONINFO:
			break;
		}
		
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package com.st1.bartapp;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.os.Handler;
import android.os.Message;

public class StationInfoDB {
	public static final int SIDB_DONE = 1;
	public static final int SIDB_ERROR = -1;
	
	private Context context;

	private StationInfoDBHelper SIDBHelper;
	private SQLiteDatabase theStationsDB;
	private Handler getStationsHandler;

	public StationInfoDB (Context c, Handler theHandler) {
		context = c;
		getStationsHandler = theHandler;
	}
	
	//TODO Spawn this as a separate thread?
	public void loadStations() {
		try {
			SIDBHelper = new StationInfoDBHelper (context);
			theStationsDB = SIDBHelper.getReadableDatabase();
			if (SIDBHelper.isDBValid()) {
				Cursor allStations = theStationsDB.query(StationInfoDBHelper.STINFDB_STNINFO_TABLE, null, null, null, null, null, null);
				FillStationInfo(allStations);
			} else {
				//Start download
			}
		} catch (SQLiteException sqle) {
			Message msg = new Message();
			msg.what = SIDB_ERROR;
			getStationsHandler.sendMessage(msg);
		}
	}
	
	private void FillStationInfo(Cursor allStations) {
		ArrayList<StationInfo> theStations = new ArrayList<StationInfo>(allStations.getCount());
		allStations.moveToFirst();
		do {
			StationInfo newStation = new StationInfo();
			theStations.add(newStation);

			newStation.Name = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_NAME));
			newStation.Abbr = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_ABBR));

			newStation.longitude = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_LONG));
			newStation.latitude = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_LAT));

			newStation.street = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_STREET));
			newStation.city = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_CITY));
			newStation.county = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_COUNTY));
			newStation.state = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_STATE));
			newStation.zip = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_ZIP));
		} while (allStations.moveToNext());
		SIDBHelper.close();
		//Alert the calling thread
		Message msg = new Message();
		msg.what = SIDB_DONE;
		getStationsHandler.sendMessage(msg);
	}

	public interface StationInfoDBListener {
		abstract void onRetrievedStations (ArrayList<StationInfo> theStationsList);
		abstract void onRetrievedStationsError ();
	}

}

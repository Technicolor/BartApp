package com.st1.bartapp;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.*;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class StationInfoDB {	
//	public static final String STATIONINFO_DBNAME = "BartStationInfoDB";
//	public static final int STATIONINFO_DBVERSION = 1;
	public static final int SIDB_DONE = 1;
	public static final int SIDB_DBERROR = -1;
	public static final int SIDB_DBNETERROR = -2;
	
	//Regexp for splitting up the platform and route information strings
	//stored in the database, as AbstractCollection.toString()
	//for example [1, 2, 3, 4]
	private static final String SPLIT_REGEXP = "\\[|,\\s+|\\]";

	private Context mContext;

//	private StationInfoDBHelper SIDBHelper;
//	private SQLiteDatabase theStationsDB;
	private Handler mGetStationsHandler;

	public StationInfoDB (Context c, Handler theHandler) {
		mContext = c;
		mGetStationsHandler = theHandler;
	}
	
	//Called at the start to load stations in memory
	public void loadStations() {
		Uri getStationUri = StationInfoProvider.makeURI(StationInfoProvider.STATIONLIST_URL);
		mContext.getContentResolver().unregisterContentObserver(dbUpdateObserver);
		
		Cursor allStations = mContext.getContentResolver().query(
				getStationUri, null, null, null, null);
		
		if (allStations == null || !allStations.moveToFirst()) {
			//Start off the service and exit
			mContext.getContentResolver().registerContentObserver(getStationUri, true,
					dbUpdateObserver);
			Intent intent = new Intent(StnInfoDownloadService.BARTAPP_DLD_STNLIST, null, mContext, StnInfoDownloadService.class);
			mContext.startService(intent);
			return;
		}
		
		FillStationInfo(allStations);
	}
	
	//Is this needed? Interface to link network download with a handler...
	public interface StationInfoDBListener {
		abstract void onRetrievedStationsList (ArrayList<StationInfo> theStationsList);
		abstract void onRetrievedStationsInfo (ArrayList<StationInfo> theStationsList);
		abstract void onRetrievedStationsError ();
	}

	private ContentObserver dbUpdateObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			//StationData has been initialised, but only with basic station information
			//Unregister the observer
			mContext.getContentResolver().unregisterContentObserver(this);
			//TODO: Notify whoever that we've got basic station data!
			//Set up the service to get complete station info.
			Uri getStationUri = StationInfoProvider.makeURI(StationInfoProvider.STATIONINFO_URL);
			mContext.getContentResolver().registerContentObserver(getStationUri, true,
					dbUpdateInfoObserver);
			Intent intent = new Intent(StnInfoDownloadService.BARTAPP_DLD_STNINFO, null, mContext, StnInfoDownloadService.class);
			mContext.startService(intent);
		}
	};

	//TODO what to do here???
	private ContentObserver dbUpdateInfoObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			//At least one station info has been received
			//Unregister the observer
			mContext.getContentResolver().unregisterContentObserver(this);
			//TODO: Notify whoever that we've got full station data!
		}
	};
	
	private void FillStationInfo(Cursor allStations) {
		StationData theStations = StationData.getStationData();
		allStations.moveToFirst();
		do {
			StationInfo newStation = new StationInfo();

			newStation.Name = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_NAME));
			newStation.Abbr = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_ABBR));
			//Can only add after we get the station abbreviation
			theStations.addStationInfo(newStation);

			newStation.longitude = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_LONG));
			newStation.latitude = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_LAT));

			newStation.street = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_STREET));
			newStation.city = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_CITY));
			newStation.county = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_COUNTY));
			newStation.state = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_STATE));
			newStation.zip = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_ZIP));
			
			//Routes stored as comma-delineated strings
			String theRoutes = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_NROUTES));
			newStation.NRoutes = Arrays.asList(theRoutes.split(SPLIT_REGEXP));
			theRoutes = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_SROUTES));
			newStation.SRoutes = Arrays.asList(theRoutes.split(SPLIT_REGEXP));
			
			//Platform numbers are stored as comma-delineated strings
			String thePlatforms = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_NPLATFORMS));
			String[] PlatStrings = thePlatforms.split(SPLIT_REGEXP);
			for (String plat : PlatStrings) {
				newStation.NPlatforms.add(Integer.parseInt(plat));
			}
			thePlatforms = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_SPLATFORMS));
			PlatStrings = thePlatforms.split(SPLIT_REGEXP);
			for (String plat : PlatStrings) {
				newStation.SPlatforms.add(Integer.parseInt(plat));
			}
			newStation.PlatformInfo = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_PLATINFO));

			newStation.StationIntro = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_INTRO));
			newStation.CrossSt = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_CROSSST));
			newStation.Food = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_FOOD));
			newStation.Shopping = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_SHOP));
			newStation.Attractions = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_ATTR));
			newStation.HLink = allStations.getString(allStations.getColumnIndex(StationInfoDBHelper.STINFDB_KEY_HLINK));
			
		} while (allStations.moveToNext());
		//Alert the calling thread
		Message msg = new Message();
		msg.what = SIDB_DONE;
		mGetStationsHandler.sendMessage(msg);
	}

}

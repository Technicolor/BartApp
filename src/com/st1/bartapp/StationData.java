package com.st1.bartapp;

import java.util.*;
import android.content.ContentValues;

import android.util.Log;

//Storing the stations in a hash map
public class StationData {
	private static StationData mData;
	
	private HashMap<String, StationInfo> theStationsHash;
	
	private StationData() {
		theStationsHash = new HashMap<String, StationInfo>();
	}
	
	public static StationData getStationData() {
		if (mData == null) {
			mData = new StationData();
		}
		return mData;
	}
	
	public HashMap<String, StationInfo> getStations() {
		return theStationsHash;
	}
	
	public synchronized void addStationInfo (StationInfo currStation) {
		theStationsHash.put(currStation.Abbr, currStation);
	}
	
	public StationInfo getStationInfo (String stnAbbr) {
		return theStationsHash.get(stnAbbr);
	}
	
	public List<ContentValues> getStationValues () {
		List<ContentValues> stationValues = new ArrayList<ContentValues>();
		
		if (mData == null) return null; //We haven't instantiated the class yet!
		
		Iterator stnList = theStationsHash.values().iterator();
		while (stnList.hasNext()) {
			ContentValues stnValues = new ContentValues();
			StationInfo stnInfo = (StationInfo)stnList.next();
			//Store the station information in ContentValues
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_ABBR, stnInfo.Abbr);

			//Get the rest of the station info.
			//Station Name
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_NAME, stnInfo.Name);
			//Station Address
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_STREET, stnInfo.street);
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_CITY, stnInfo.city);
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_COUNTY, stnInfo.county);
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_STATE, stnInfo.state);
			stnValues.put(StationInfoDBHelper.STINFDB_KEY_ZIP, stnInfo.zip);
		}
		
		return stationValues;
	}

}

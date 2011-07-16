package com.st1.bartapp;

import java.util.HashMap;

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

}

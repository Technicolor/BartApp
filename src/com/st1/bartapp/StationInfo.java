package com.st1.bartapp;

import java.util.*;

import android.content.ContentValues;

public class StationInfo {

	public String Name;
	public String Abbr;
	
	//GSP Coords. Should these be floats instead?
	public String longitude; 
	public String latitude;
	
	public String street;
	public String city;
	public String county;
	public String state;
	public String zip;
	
	public List<String> NRoutes; //"Northbound" route numbers 
	public List<String> SRoutes; //"Southbound" route numbers
	
	//Platform numbers, north and south
	public List<Integer> NPlatforms;
	public List<Integer> SPlatforms;
	public String PlatformInfo;
	
	//Estimated Time of Departure array, one list for each destination station
	public List<List<EtdEstimate>> ETDs;
	
	//Miscellaneous Station Info
	public String StationIntro; //Brief informational text
	public String CrossSt; //Nearest cross street
	public String Food; //Nearby food
	public String Shopping; //Nearby shopping
	public String Attractions; //Nearby attractions
	public String HLink; //URL for bart.gov station info
	
	
	public StationInfo () {
		NRoutes = new ArrayList<String>();
		SRoutes = new ArrayList<String>();
		NPlatforms = new ArrayList<Integer>();
		SPlatforms = new ArrayList<Integer>();
		ETDs = new ArrayList<List<EtdEstimate>>();
	}
	
	public ContentValues toContentValues () {
		ContentValues retCV = new ContentValues();
		retCV.put(StationInfoDBHelper.STINFDB_KEY_NAME, Name);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_ABBR, Abbr);
		
		retCV.put(StationInfoDBHelper.STINFDB_KEY_LONG, longitude);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_LAT, latitude);
		
		retCV.put(StationInfoDBHelper.STINFDB_KEY_STREET, street);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_CITY, city);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_COUNTY, county);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_STATE, state);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_ZIP, zip);

		retCV.put(StationInfoDBHelper.STINFDB_KEY_NROUTES, NRoutes.toString());
		retCV.put(StationInfoDBHelper.STINFDB_KEY_SROUTES, SRoutes.toString());
		
		retCV.put(StationInfoDBHelper.STINFDB_KEY_NPLATFORMS, NPlatforms.toString());
		retCV.put(StationInfoDBHelper.STINFDB_KEY_SPLATFORMS, SPlatforms.toString());
		retCV.put(StationInfoDBHelper.STINFDB_KEY_PLATINFO, PlatformInfo);

		retCV.put(StationInfoDBHelper.STINFDB_KEY_INTRO, StationIntro);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_CROSSST, CrossSt);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_FOOD, Food);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_SHOP, Shopping);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_ATTR, Attractions);
		retCV.put(StationInfoDBHelper.STINFDB_KEY_HLINK, HLink);
		
		return retCV;
	}
	
	public String toString() {
		return (Abbr + " " + Name + "\n" +
		street + " " + city + " " + county + " " + state + " " + zip + "\n" +
		"!");
	}
}

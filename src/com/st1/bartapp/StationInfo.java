package com.st1.bartapp;

import java.util.*;

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
	
}

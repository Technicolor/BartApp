package com.st1.bartapp;

import java.util.*;

public class StationInfo {

	public String Name;
	public String Abbr;
	
	public String Address;
	public String City;
	public String County;
	public String State;
	public String Zip;
	
	public List<List<EtdEstimate>> ETDs;
	
	public StationInfo () {
		ETDs = new ArrayList<List<EtdEstimate>>();
	}
	
}

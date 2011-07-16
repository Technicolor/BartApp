package com.st1.bartapp;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.*;
import android.util.Log;

//TODO: ST1 Assumes a specific StationInfo object has been passed in
public class StnAPIParser extends APIParser {

	private StationData theStations;
	private StationInfo currentStation;
	
	private List<String> Routes = null;
	private List<Integer> Platforms = null;
	
	//In this case stnBase is the <stations> element
	public StnAPIParser(Element stnBase, StationData allStations) {
		theStations = allStations;
		//Now for the more specialised ones
		Element station = stnBase.getChild("station");
		
		//<station/>
		station.setElementListener(StationListener);
		station.getChild("name").setEndTextElementListener(NameEndListener);
		station.getChild("abbr").setEndTextElementListener(AbbrEndListener);
		
		station.getChild("address").setEndTextElementListener(StreetListener);
		station.getChild("city").setEndTextElementListener(CityListener);
		station.getChild("county").setEndTextElementListener(CountyListener);
		station.getChild("state").setEndTextElementListener(StateListener);
		station.getChild("zipcode").setEndTextElementListener(ZipListener);
	}
	
	ElementListener StationListener = new ElementListener() {
		@Override
		public void start(Attributes arg0) {
			currentStation = new StationInfo();
		}
		@Override
		public void end() {
			theStations.addStationInfo(currentStation);
			currentStation = null; //Garbage collection aid
		}
	};

	EndTextElementListener NameEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.Name = body;
		}
	};
	
	//Do we need this?
	EndTextElementListener AbbrEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.Abbr = body;
		}
	};

	//Address fields
	EndTextElementListener StreetListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.street = body;	
		}
	};
	EndTextElementListener CityListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.city = body;	
		}
	};
	EndTextElementListener CountyListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.county = body;	
		}
	};
	EndTextElementListener StateListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.state = body;	
		}
	};
	EndTextElementListener ZipListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.zip = body;	
		}
	};

}

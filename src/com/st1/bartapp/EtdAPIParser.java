package com.st1.bartapp;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.util.Log;

//TODO: ST1 Currently this creates a new StationInfo object when parsing.
//But in the future we likely want to have a pre-allocated StationInfo array.
public class EtdAPIParser extends APIParser {

	private StationInfo currentStation;
	private List<EtdEstimate> Estimates;
	private EtdEstimate currentEstimate;
	private String currentDestination;
	
	private APIParser myparent;
	
	//In this case etdBase is the same as root!
	public EtdAPIParser(Element etdBase, APIParser parent) {
		myparent = parent;
		//Now for the more specialised ones
		Element station = etdBase.getChild("station");
		
		//<station/>
		station.setElementListener(StationListener);
		station.getChild("name").setEndTextElementListener(NameEndListener);
		station.getChild("abbr").setEndTextElementListener(AbbrEndListener);
		
		//<station><etd/></station>
		Element etd = station.getChild("etd");
		etd.setElementListener(ETDListener);
		etd.getChild("destination").setEndTextElementListener(DestinationEndListener);
		etd.getChild("abbreviation").setEndTextElementListener(AbbrEndListener);

		//<station><etd><estimate/></etd></station>
		Element estimate = etd.getChild("estimate");
		estimate.setElementListener(EstimateListener);
		estimate.getChild("minutes").setEndTextElementListener(MinutesEndListener);
		estimate.getChild("platform").setEndTextElementListener(PlatformEndListener);
		estimate.getChild("direction").setEndTextElementListener(DirectionEndListener);
		estimate.getChild("length").setEndTextElementListener(LengthEndListener);
	}
	
	ElementListener StationListener = new ElementListener() {
		@Override
		public void start(Attributes arg0) {
			Log.d("Station", "start");
			currentStation = new StationInfo();
		}
		@Override
		public void end() {
			Log.d("Station", "end");
			myparent.retVal = currentStation;
			currentStation = null; //Garbage collection aid
		}
	};

	EndTextElementListener NameEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.Name = body;
		}
	};
	
	ElementListener ETDListener = new ElementListener() {
		@Override
		public void start(Attributes attributes) {
			Estimates = new ArrayList<EtdEstimate>();
		}
		@Override
		public void end() {
			currentStation.ETDs.add(Estimates);
			Estimates = null;
		}
	};
	
	EndTextElementListener DestinationEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentDestination = body;
		}
	};
	
	//Do we need this?
	EndTextElementListener AbbrEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			Log.d("Abbr", body);
		}
	};
	
	ElementListener EstimateListener = new ElementListener() {
		@Override
		public void start(Attributes attributes) {
			currentEstimate = new EtdEstimate();
			currentEstimate.Destination = currentDestination;
		}
		@Override
		public void end() {
			Estimates.add(currentEstimate);
			currentEstimate = null;
		}
	};	

	//Elements in <estimate/>
	EndTextElementListener MinutesEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			try {
				currentEstimate.ETDTime = Integer.parseInt(body);
			} catch (NumberFormatException nfe) {
				currentEstimate.ETDTime = 0; //The string might be some text like "Arrived" instead
			}
		}
	};
	EndTextElementListener PlatformEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentEstimate.Platform = Integer.parseInt(body);
		}
	};
	EndTextElementListener DirectionEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			if (body.equalsIgnoreCase("North")) currentEstimate.Direction = EtdEstimate.North;
			if (body.equalsIgnoreCase("South")) currentEstimate.Direction = EtdEstimate.South;
			if (body.equalsIgnoreCase("East")) currentEstimate.Direction = EtdEstimate.East;
			if (body.equalsIgnoreCase("West")) currentEstimate.Direction = EtdEstimate.West;
		}
	};
	EndTextElementListener LengthEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentEstimate.TrainLength = Integer.parseInt(body);
		}
	};

}

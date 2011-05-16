package com.st1.bartapp;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.*;
import android.util.Log;

//TODO: ST1 Assumes a specific StationInfo object has been passed in
public class StnInfoAPIParser extends APIParser {

	private StationInfo currentStation;
	
	private List<String> Routes = null;
	private List<Integer> Platforms = null;
	
	//In this case stnBase is the <stations> element
	public StnInfoAPIParser(Element stnBase, StationInfo currentStation) {
		//Now for the more specialised ones
		Element station = stnBase.getChild("station");
		
		//<station/>
		station.setElementListener(StationListener);
		station.getChild("name").setEndTextElementListener(NameEndListener);
		station.getChild("abbr").setEndTextElementListener(AbbrEndListener);
		station.getChild("gtfs_latitude").setEndTextElementListener(LatEndListener);
		station.getChild("gtfs_longitude").setEndTextElementListener(LongEndListener);
		
		station.getChild("address").setEndTextElementListener(StreetListener);
		station.getChild("city").setEndTextElementListener(CityListener);
		station.getChild("county").setEndTextElementListener(CountyListener);
		station.getChild("state").setEndTextElementListener(StateListener);
		station.getChild("zipcode").setEndTextElementListener(ZipListener);
		
		//<station><north_routes/></station>
		Element nroutes = station.getChild("north_routes");
		nroutes.setElementListener(NRoutesListener);
		nroutes.getChild("route").setEndTextElementListener(RouteListener);
		//<station><south_routes/></station>
		Element sroutes = station.getChild("south_routes");
		sroutes.setElementListener(SRoutesListener);
		sroutes.getChild("route").setEndTextElementListener(RouteListener);

		//<station><north_platforms/></station>
		Element nplats = station.getChild("north_platforms");
		nplats.setElementListener(NPlatsListener);
		nplats.getChild("platform").setEndTextElementListener(PlatformListener);
		//<station><south_platforms/></station>
		Element splats = station.getChild("south_platforms");
		splats.setElementListener(SPlatsListener);
		splats.getChild("platform").setEndTextElementListener(PlatformListener);

		station.getChild("platform_info").setEndTextElementListener(PlatInfoListener);
		
		//Misc Info
		station.getChild("intro").setEndTextElementListener(IntroListener);
		station.getChild("cross_street").setEndTextElementListener(CrossStreetListener);
		station.getChild("food").setEndTextElementListener(FoodListener);
		station.getChild("shopping").setEndTextElementListener(ShoppingListener);
		station.getChild("attraction").setEndTextElementListener(AttractionListener);
		station.getChild("link").setEndTextElementListener(LinkListener);
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
//			myparent.retVal = currentStation;
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
			Log.d("Abbr", body);
		}
	};

	//Long/Lat coordinates
	EndTextElementListener LatEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.latitude = body;
		}		
	};	
	EndTextElementListener LongEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.longitude = body;
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

	//Routes
	ElementListener NRoutesListener = new ElementListener() {
		@Override
		public void start(Attributes attributes) {
			Routes = currentStation.NRoutes;
		}
		@Override
		public void end() {
			Routes = null;
		}
	};
	ElementListener SRoutesListener = new ElementListener() {
		@Override
		public void start(Attributes attributes) {
			Routes = currentStation.SRoutes;
		}
		@Override
		public void end() {
			Routes = null;
		}
	};
	EndTextElementListener RouteListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			Routes.add(body);
		}
	};

	//Platforms
	ElementListener NPlatsListener = new ElementListener() {
		@Override
		public void start(Attributes attributes) {
			Platforms = currentStation.NPlatforms;
		}
		@Override
		public void end() {
			Platforms = null;
		}
	};
	ElementListener SPlatsListener = new ElementListener() {
		@Override
		public void start(Attributes attributes) {
			Platforms = currentStation.SPlatforms;
		}
		@Override
		public void end() {
			Platforms = null;
		}
	};
	EndTextElementListener PlatformListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			Platforms.add(Integer.parseInt(body));
		}
	};
	EndTextElementListener PlatInfoListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.PlatformInfo = body;
		}
	};

	//Misc Info
	EndTextElementListener IntroListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.StationIntro = body;
		}
	};
	EndTextElementListener CrossStreetListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.CrossSt = body;
		}
	};
	EndTextElementListener FoodListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.Food = body;
		}
	};
	EndTextElementListener ShoppingListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.Shopping = body;
		}
	};
	EndTextElementListener AttractionListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.Attractions = body;
		}
	};
	EndTextElementListener LinkListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			currentStation.HLink = body;
		}
	};

}

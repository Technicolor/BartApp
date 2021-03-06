package com.st1.bartapp;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.*;
import android.util.*;

public class APIParser {
	protected RootElement root;
	protected Object retVal;

	private int cmdType;
	private APIParser thisParser;
	//XML-specific parsing arg, contents depends on the XML assumed to be parsed, or ignored.
	private String apiArg;
	
	//Various API parsers
	private APIParser childParser;
	
	public APIParser() {
		init();
	}
	
	public APIParser(String arg) {
		init();
		apiArg = arg;
	}
	
	private void init() {
		thisParser = this;
		root = new RootElement("root");
		root.setEndElementListener(RootEndListener);
		apiArg = null;
	}
	
	public int Parse(String inXML, Object[] model) {
		//First the standard elements, though not all APIs have date and time...
		Element uri = root.getChild("uri");
		Element date = root.getChild("date");
		Element time = root.getChild("time");
		Element message = root.getChild("message");
		
		//Set up the listeners
		uri.setEndTextElementListener(URIEndListener);
		date.setEndTextElementListener(BasicTextEndListener);
		time.setEndTextElementListener(BasicTextEndListener);

		//Initialise return value. Should get properly set in URIEndListener
		cmdType = 0;
		try {
			Xml.parse(inXML, root.getContentHandler());
			model[0] = retVal;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cmdType;
	}
	
	protected EndTextElementListener BasicTextEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			Log.d("BasicText", body);
		}
	};
	
	private EndTextElementListener URIEndListener = new EndTextElementListener() {
		@Override
		public void end(String body) {
			//Set up additional listeners based on the XML type
			if (body.contains(APIManager.BARTAPI_CMDSTR+APIManager.BARTAPI_ETDCMD)) {
				childParser = new EtdAPIParser (root, thisParser);
				childParser.root = root;
				cmdType = APIManager.BARTAPI_ETDCODE; 
			}
			else if (body.contains(APIManager.BARTAPI_CMDSTR+APIManager.BARTAPI_STNSCMD)) {
				StationData allStations = StationData.getStationData();
				childParser = new StnAPIParser(root.getChild("stations"), allStations);
				childParser.root = root;
				cmdType = APIManager.BARTAPI_STNSCODE;
			}
			else if (body.contains(APIManager.BARTAPI_CMDSTR+APIManager.BARTAPI_STNINFOCMD)) {
				StationData allStations = StationData.getStationData();
				childParser = new StnInfoAPIParser(root.getChild("stations"), allStations.getStationInfo(apiArg));
				childParser.root = root;
				cmdType = APIManager.BARTAPI_STNINFOCODE;
			}
		}
	};
	
	private EndElementListener RootEndListener = new EndElementListener() {
		@Override
		public void end() {
		}
	};
	
}

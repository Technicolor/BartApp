package com.st1.bartapp;

import java.util.*;
import java.io.*;

import android.content.Context;
import android.net.http.*;
import android.os.*;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.*;
import org.apache.http.protocol.HTTP;

public class APIManager {
	//Development key
	private static final String BARTAPI_DEVKEY="MW9S-E7SL-26DU-VV8V";
	
	//BART API URLs and webapps
	private static final String BARTAPI_URL="http://api.bart.gov/api/"; 
	private static final String BARTAPI_ETD="etd.aspx";
	private static final String BARTAPI_BSA="bsa.aspx";
	private static final String BARTAPI_ROUTE="route.aspx";
	private static final String BARTAPI_SCHED="sched.aspx";
	private static final String BARTAPI_STN="stn.aspx";
	
	//BART API cmd field strings and corresponding internal code numbers
	public static final String BARTAPI_CMDSTR="cmd=";
	public static final String BARTAPI_ETDCMD="etd";
	public static final int BARTAPI_ETDCODE=0x10;
	public static final String BARTAPI_BSACMD="bsa";
	public static final int BARTAPI_BSACODE=0x11;
	public static final String BARTAPI_ELEVCMD="elev";
	public static final int BARTAPI_ELEVCODE=0x12;
	public static final String BARTAPI_STNSCMD="stns";
	public static final int BARTAPI_STNSCODE=0x13;
	public static final String BARTAPI_STNINFOCMD="stninfo";
	public static final int BARTAPI_STNINFOCODE=0x14;
	public static final String BARTAPI_STNACCCMD="stnaccess";
	public static final int BARTAPI_STNACCCODE=0x15;
		
	private AndroidHttpClient httpClient;
	private Handler APICallbackHandler;
	
	public APIManager (Context context, Handler cbHandler) {
		httpClient = AndroidHttpClient.newInstance(context.getString(R.string.UserAgent));
		APICallbackHandler = cbHandler;
	}
	
	public void OnDestroy() {
		httpClient.close();
	}
	
	public static void ParseResponse(String response) {
		
	}
	
	//Assemble the complete URL, example http://api.bart.gov/api/etd.aspx?cmd=etd&key=MW9S-E7SL-26DU-VV8V&orig=ALL
	private String makeAPIRequestURL(String api, String args) {
		return BARTAPI_URL + api + "?" + args;
	}

	//Assemble the basic API params, cmd, key, and orig
	private String makeAPIBaseParams(String cmd, String stnOrig) {
		String args = BARTAPI_CMDSTR + cmd + "&key=" + BARTAPI_DEVKEY;
		if (stnOrig != null) args += "&orig=" + stnOrig;
		return args;
	}
	
	//Make the HttpPost object based on the request URL
	private HttpPost makeAPIPost(String reqURL) {
		HttpPost reqPost = new HttpPost(reqURL);
		return reqPost;
	}
	
	//Send the POST request via thread
	private void sendAPIPost (Context context, HttpPost request) {
		Thread newThread = new Thread(new BartAPIRunnable(context, request));
		newThread.start();
	}
	
	public void getETD(Context context, String stnOrig) {
		String reqArgs = makeAPIBaseParams(BARTAPI_ETDCMD, stnOrig);
		String reqURL = makeAPIRequestURL(BARTAPI_ETD, reqArgs);
		HttpPost reqPost = makeAPIPost(reqURL);
		sendAPIPost(context, reqPost);
	}

	public void getElev(Context context) {
		String reqArgs = makeAPIBaseParams(BARTAPI_ELEVCMD, null);
		String reqURL = makeAPIRequestURL(BARTAPI_BSA, reqArgs);
		HttpPost reqPost = makeAPIPost(reqURL);
		sendAPIPost(context, reqPost);
	}

	public void getBSA(Context context, String stnOrig) {
		String reqArgs = makeAPIBaseParams(BARTAPI_BSACMD, stnOrig);
		String reqURL = makeAPIRequestURL(BARTAPI_BSA, reqArgs);
		HttpPost reqPost = makeAPIPost(reqURL);
		sendAPIPost(context, reqPost);
	}

	public void getStns(Context context) {
		String reqArgs = makeAPIBaseParams(BARTAPI_STNSCMD, null);
		String reqURL = makeAPIRequestURL(BARTAPI_STN, reqArgs);
		HttpPost reqPost = makeAPIPost(reqURL);
		sendAPIPost(context, reqPost);
	}

	public void getStnInfo(Context context, String stnOrig) {
		String reqArgs = makeAPIBaseParams(BARTAPI_STNINFOCMD, stnOrig);
		String reqURL = makeAPIRequestURL(BARTAPI_STN, reqArgs);
		HttpPost reqPost = makeAPIPost(reqURL);
		sendAPIPost(context, reqPost);
	}

	public void getStnAccess(Context context, String stnOrig, boolean showLegend) {
		String reqArgs = makeAPIBaseParams(BARTAPI_STNACCCMD, stnOrig);
		if (showLegend) reqArgs += "&l=1";
		String reqURL = makeAPIRequestURL(BARTAPI_STN, reqArgs);
		HttpPost reqPost = makeAPIPost(reqURL);
		sendAPIPost(context, reqPost);
	}

	class BartAPIRunnable implements Runnable {
		private HttpPost httpRequest;

		public BartAPIRunnable (Context context, HttpPost request) {
			httpRequest = request;
		}
		
		@Override
		public void run() {
			try {
				httpClient.execute(httpRequest, APIResponseHandler);
			} catch (ClientProtocolException e) {
//				e.printStackTrace();
				SendMessageError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				SendMessageError(HttpStatus.SC_REQUEST_TIMEOUT);
			}
		}

		BasicResponseHandler APIResponseHandler = new BasicResponseHandler() {
			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				
				String APIResponse = super.handleResponse(response);
				Message respMsg = new Message ();
				respMsg.what = HttpStatus.SC_OK;
				respMsg.obj = APIResponse;
				//Done with networking, send the message!
				APICallbackHandler.sendMessage(respMsg);
				return APIResponse;
			}
		};
		
		private void SendMessageError(int ErrorCode) {
			Message respMsg = new Message ();
			respMsg.what = ErrorCode;
			respMsg.obj = "Network Error";
			//Done with networking, send the message!
			APICallbackHandler.sendMessage(respMsg);
		}
		
	}
}
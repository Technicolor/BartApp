package com.st1.bartapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

public class StnInfoDownloadService extends Service {
	public static final String BARTAPP_DLD_STNLIST = "com.st1.bartapp.stnlist";
	public static final String BARTAPP_DLD_STNINFO = "com.st1.bartapp.stninfo";
	
	private APIManager mBartAPIs;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		mBartAPIs = new APIManager(this, APICallbackHandler);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	String dldMode = intent.getAction();
    	if (dldMode.equals(BARTAPP_DLD_STNLIST)) {
    		mBartAPIs.getStns(this);
    	}
    	if (dldMode.equals(BARTAPP_DLD_STNINFO)) {
    		StationData theStations = StationData.getStationData();
    		Iterator<String> theStationsKeys = theStations.getStations().keySet().iterator();
    		while (theStationsKeys.hasNext()) {
    			mBartAPIs.getStnInfo(this, theStationsKeys.next());
    		}
    	}
        return START_STICKY;
    }

    Handler APICallbackHandler = new Handler() {
    	private Service myService = null;
    	
		public void handleMessage(Message msg) {
			//TODO Handle error
			if (msg.what == HttpStatus.SC_OK) {
				Object[] k = new Object[1];
				String arg = null;
				if (msg.peekData() != null) {
					arg = msg.getData().getString(APIManager.BARTAPI_BUNDLE_ARGKEY);
				}
		        APIParser mainAPIParser = new APIParser(arg);
				switch (mainAPIParser.Parse((String)msg.obj, k)) {
				case APIManager.BARTAPI_STNSCODE:
				case APIManager.BARTAPI_STNINFOCODE:
					StationData theStations = StationData.getStationData();
					List<ContentValues>stnInfoValuesList = theStations.getStationValues();
					ArrayList<ContentProviderOperation> stnAddOps = new ArrayList<ContentProviderOperation>();
					for (ContentValues stnInfoValues : stnInfoValuesList) {
						stnAddOps.add(ContentProviderOperation.newInsert(StationInfoProvider.makeURI(StationInfoProvider.STATIONLIST_URL)).
						withValues(stnInfoValues).build());
					}
					try {
						myService.getContentResolver().applyBatch(StationInfoProvider.STNPROVIDER_AUTHORITY, stnAddOps);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OperationApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		public Handler init(Service theService) {
			myService = theService;
			return this;
		}
	}.init(this);
	
}

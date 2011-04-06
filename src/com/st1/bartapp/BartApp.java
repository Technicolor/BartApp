package com.st1.bartapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.apache.http.*;

public class BartApp extends Activity {
	private APIManager bartAPIs;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bartAPIs = new APIManager(this, APICallbackHandler);
    }
    
    public void onDestroy() {
    	bartAPIs.OnDestroy();
    	super.onDestroy();
    }

    Handler APICallbackHandler = new Handler() {
		public void handleMessage(Message msg) {
			//TODO Handle error
			if (msg.what == HttpStatus.SC_OK) {
				TextView text = (TextView)findViewById(R.id.NextTrainLabel);
				Object[] k = new Object[1];
		        APIParser mainAPIParser = new APIParser();
				switch (mainAPIParser.Parse((String)msg.obj, k)) {
				case APIManager.BARTAPI_ETDCODE:
					StationInfo theStation = (StationInfo)k[0];
					String str = theStation.Name + "\n";
					for (int i = 0; i < theStation.ETDs.size(); i++) {
						str += theStation.ETDs.get(i).get(0).Destination + " " + theStation.ETDs.get(i).get(0).ETDTime + "\n";
					}
					text.setText(str);
					break;
				case APIManager.BARTAPI_BSACODE:
					break;
				case APIManager.BARTAPI_ELEVCODE:
					break;
				}
			}
		}
	};
	
	public void ButtonClick (View view) {
		String stnOrig = "ALL"; //Testing, use "ALL" stations 
		switch (view.getId()) {
		case R.id.BSABtn:
			bartAPIs.getBSA(this, stnOrig);
			break;
		case R.id.ETDBtn:
			bartAPIs.getETD(this, "MONT");
			break;
		}
	}
}
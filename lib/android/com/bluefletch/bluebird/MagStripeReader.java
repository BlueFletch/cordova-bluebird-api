package com.bluefletch.bluebird;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Written by blakebyrnes on 4/7/14
 */
public class MagStripeReader extends BaseIntentHandler {


    @Override
    protected String getOpenIntent() {
        return "kr.co.bluebird.android.bbapi.action.MSR_OPEN";
    }
    @Override
    protected String getCloseIntent(){
        return "kr.co.bluebird.android.bbapi.action.MSR_CLOSE";
    }
    @Override
    protected String getCallbackDataReceivedIntent(){
        return "kr.co.bluebird.android.bbapi.action.MSR_CALLBACK_DECODING_DATA";
    }
    @Override
    protected String getCallbackSuccessIntent(){
        return "kr.co.bluebird.android.bbapi.action.MSR_CALLBACK_REQUEST_SUCCESS";
    }
    @Override
    protected String getCallbackFailedIntent() {
        return "kr.co.bluebird.android.bbapi.action.MSR_CALLBACK_REQUEST_FAILED";
    }
    @Override
    protected int getTimeoutErrorCode() {
        return 2;
    }
    protected ScanCallback<List<String>> readCallback;
    public void setStripeReadCallback(ScanCallback<List<String>> callback){
        readCallback = callback;
    }

    private static final String ACTION_READ = "kr.co.bluebird.android.bbapi.action.MSR_READ";
    private static final String EXTRA_TRACK_1 = "EXTRA_MSR_TRACK1_DATA";
    private static final String EXTRA_TRACK_2 = "EXTRA_MSR_TRACK2_DATA";
    private static final String EXTRA_TRACK_3 = "EXTRA_MSR_TRACK3_DATA";
    private static final String EXTRA_HANDLE_ID = "EXTRA_HANDLE";

    private static final int READ_REQUEST_ID = 23;
    private int handle = -1;
    private boolean triggerReadOnOpen = false;
 
    public MagStripeReader(Context appContext){
        super(appContext);
    }
    
    public void read(ScanCallback<Boolean> requestResult) {
        resultCallbackMap.put(READ_REQUEST_ID, requestResult);

        //if handle isn't ready, need to wait
        if (this.handle == -1) {
            Log.i(TAG, "Read prompted, but need to hold for completion of open event.");
            this.triggerReadOnOpen = true;
        } else {
            sendRead();
        }
    }

    public boolean isReady() {
        return this.handle >= 0;
    }
    private void sendRead(){
        Log.i(TAG, "Beginning read magstripe reader intent.");
        Intent scanOnIntent = new Intent(ACTION_READ);
        scanOnIntent.putExtra(EXTRA_HANDLE_ID, this.handle);
        scanOnIntent.putExtra(getIntentExtraSequenceField(), READ_REQUEST_ID);
        
        applicationContext.sendBroadcast(scanOnIntent);
    }

    @Override
    protected void processCloseIntent(Intent intent){
        intent.putExtra(EXTRA_HANDLE_ID, this.handle);
        this.handle = -1;
        this.triggerReadOnOpen = false;
    }
    
    @Override  
    protected void onDataRead(Intent intent){
        List<String> tracks = new ArrayList<String>();
        if(intent.hasExtra(EXTRA_TRACK_1)) {
            String track = new String(intent.getByteArrayExtra(EXTRA_TRACK_1)).trim();
            tracks.add(track);
        }
        if(intent.hasExtra(EXTRA_TRACK_2)) {
            String track = new String(intent.getByteArrayExtra(EXTRA_TRACK_2)).trim();
            tracks.add(track);
        }
         if(intent.hasExtra(EXTRA_TRACK_3)) {
            String track = new String(intent.getByteArrayExtra(EXTRA_TRACK_3)).trim();
            tracks.add(track);
        }
        if (readCallback != null) readCallback.execute(tracks);
        else Log.e(TAG, "Read callback not provided to magstripe reader class");
    }
  
    @Override
    protected void processIntentSuccessCallback(Intent intent) {
        //read handle from intent
        this.handle = intent.getIntExtra(EXTRA_HANDLE_ID, -1);
        if (this.triggerReadOnOpen) {
            sendRead();
        }
    }


    @Override
    protected String getErrorTranslation(int code) {
        switch(code) {
            case -1:
                return "NA";
            case 2: 
                return "Timeout";
            case 3: 
                return "Bad Data";
            case 4:
                return "Not Supported";
            case 5:
                return "No Data";
            case 6:
                return "Read Exception";
            case 14:
                return "Copy Fault";
            case 16:
                return "Busy";
        }
        return "";
    }
}

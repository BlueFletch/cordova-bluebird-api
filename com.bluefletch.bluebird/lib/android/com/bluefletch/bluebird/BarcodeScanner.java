package com.bluefletch.bluebird;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BarcodeScanner extends BaseIntentHandler {
    @Override
    protected String getOpenIntent() {
        return "kr.co.bluebird.android.bbapi.action.BARCODE_OPEN";
    }
    @Override
    protected String getCloseIntent(){
        return "kr.co.bluebird.android.bbapi.action.BARCODE_CLOSE";
    }
    @Override
    protected String getCallbackDataReceivedIntent(){
        return "kr.co.bluebird.android.bbapi.action.BARCODE_CALLBACK_DECODING_DATA";
    }
    @Override
    protected String getCallbackSuccessIntent(){
        return "kr.co.bluebird.android.bbapi.action.BARCODE_CALLBACK_REQUEST_SUCCESS";
    }
    @Override
    protected String getCallbackFailedIntent() {
        return "kr.co.bluebird.android.bbapi.action.BARCODE_CALLBACK_REQUEST_FAILED";
    }
    @Override
    protected int getTimeoutErrorCode() {
        return -6;
    }


    protected ScanCallback<String> scanCallback;
    public void setScanCallback(ScanCallback<String> callback){
        scanCallback = callback;
    }
    private static final String ACTION_BARCODE_SET_TRIGGER = "kr.co.bluebird.android.bbapi.action.BARCODE_SET_TRIGGER";
    private static final String EXTRA_BARCODE_DATA_BYTE_ARRAY = "EXTRA_BARCODE_DECODING_DATA";
    private static final String EXTRA_TRIGGER_TOGGLE = "EXTRA_INT_DATA2";
    

    public BarcodeScanner(Context appContext){
        super(appContext);
    }
    
    public void softScanOn(ScanCallback<Boolean> requestResult, ScanCallback<Void> onTimeout) {
        final int softScanOnId = 24;
        resultCallbackMap.put(softScanOnId, requestResult);
        timeoutCallbackMap.put(softScanOnId, onTimeout);

        Intent scanOnIntent = new Intent(ACTION_BARCODE_SET_TRIGGER);
        scanOnIntent.putExtra(EXTRA_TRIGGER_TOGGLE, 1);
        scanOnIntent.putExtra(getIntentExtraSequenceField(), softScanOnId);

        applicationContext.sendBroadcast(scanOnIntent);
    }

    public void softScanOff(ScanCallback<Boolean> requestResult) {
        final int softScanOffId = 32;
        resultCallbackMap.put(softScanOffId, requestResult);

        Intent scanOffIntent = new Intent(ACTION_BARCODE_SET_TRIGGER);
        scanOffIntent.putExtra(EXTRA_TRIGGER_TOGGLE, 0);
        scanOffIntent.putExtra(getIntentExtraSequenceField(), softScanOffId);

        applicationContext.sendBroadcast(scanOffIntent);
    }

    @Override
    protected void onDataRead(Intent intent){
        String barcode;
        if(intent.hasExtra(EXTRA_BARCODE_DATA_BYTE_ARRAY)) {
            barcode = new String(intent.getByteArrayExtra(EXTRA_BARCODE_DATA_BYTE_ARRAY)).trim();
        } else {
            barcode = "null";
        }
        if (scanCallback != null) scanCallback.execute(barcode);
        else Log.e(TAG, "Scan callback not provided to barcode scanner class");
    }


    @Override
    protected String getErrorTranslation(int code) {
        switch(code) {
            case -1:
                return "NA";
            case -6: 
                return "Timeout";
        }
        return "";
    }
}

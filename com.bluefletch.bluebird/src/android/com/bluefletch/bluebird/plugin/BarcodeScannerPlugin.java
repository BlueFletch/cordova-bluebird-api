package com.bluefletch.bluebird.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bluefletch.bluebird.BarcodeScanner;
import com.bluefletch.bluebird.ScanCallback;

public class BarcodeScannerPlugin extends CordovaPlugin {
    
    private BarcodeScanner scanner;
    protected static String TAG;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView)
    {
        super.initialize(cordova, webView);
        TAG = this.getClass().getSimpleName();
        scanner = new BarcodeScanner(cordova.getActivity().getBaseContext());
    }
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if ("register".equals(action)) {
            scanner.setScanCallback(new ScanCallback<String>() {
                @Override
                public void execute(String result) {
                    Log.i(TAG, "Scan result [" + result + "].");
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                }
            }); 
            scanner.start();
        }
        else if ("unregister".equals(action)) {
            scanner.stop();
        }
        else if ("softScanOn".equals(action)){
            scanner.softScanOn(new ScanCallback<Boolean>() {
                @Override
                public void execute(Boolean result) {
                    Log.i(TAG, "Soft scanner on - result [" + result + "].");
                    callbackContext.success();
                }
            }, new ScanCallback<Void>() {
                @Override
                public void execute(Void v) {
                    Log.i(TAG, "Soft scanner failed");
                    callbackContext.error("Error turning on scanner");
                }
            });
        }
        else if ("softScanOff".equals(action)) {
            scanner.softScanOff(new ScanCallback<Boolean>() {
                @Override
                public void execute(Boolean result) {
                    Log.i(TAG, "Soft scanner off - result [" + result + "].");
                    if(result)
                        callbackContext.success();
                    else
                        callbackContext.error("Error turning off scanner");
                }
            });
        }

        return true;
    }
    /**
    * Always close the current intent reader
    */
    @Override
    public void onPause(boolean multitasking)
    {
        super.onPause(multitasking);
        scanner.stop();
    }


    /**
    * Always resume the current activity
    */
    @Override
    public void onResume(boolean multitasking)
    {
        super.onResume(multitasking);
        scanner.start();
    }
}

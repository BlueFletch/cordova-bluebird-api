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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.bluefletch.bluebird.MagStripeReader;
import com.bluefletch.bluebird.ScanCallback;

/**
 * Written by blakebyrnes on 4/7/14 for cordova
 */
public class MagStripeReaderPlugin extends CordovaPlugin {

    private MagStripeReader reader;
    protected static String TAG;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView)
    {
        super.initialize(cordova, webView);
        TAG = this.getClass().getSimpleName();
        reader = new MagStripeReader(cordova.getActivity().getBaseContext());
    }
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if ("register".equals(action)) {
            reader.setStripeReadCallback(new ScanCallback<List<String>>() {
                @Override
                public void execute(List<String> result) {
                    Log.i(TAG, "Magstripe result [" + result + "].");
                    JSONArray tracks = new JSONArray(result);
                    //send plugin result
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, tracks);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                }
            }); 
            reader.start();
        }
        else if ("unregister".equals(action)) {
            reader.stop();
        } 
        else if ("read".equals(action)){
            reader.read(new ScanCallback<Boolean>() {
                @Override
                public void execute(Boolean result) {
                    Log.i(TAG, "Starting read - result [" + result + "].");
                    callbackContext.success();
                }
            });
        }
        else if ("isReady".equals(action)){
            return reader.isReady();
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
        reader.stop();
    }

    /**
    * Always resume the current activity
    */
    @Override
    public void onResume(boolean multitasking)
    {
        super.onResume(multitasking);
        reader.start();
    }
}

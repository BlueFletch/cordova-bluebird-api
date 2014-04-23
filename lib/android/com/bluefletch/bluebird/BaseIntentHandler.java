package com.bluefletch.bluebird;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

/**
 * Written by blakebyrnes on 4/7/14 for cordova
 */
public abstract class BaseIntentHandler {
    protected abstract String getOpenIntent();
    protected abstract String getCloseIntent();
    protected abstract String getCallbackDataReceivedIntent();
    protected abstract String getCallbackSuccessIntent();
    protected abstract String getCallbackFailedIntent();
    
    private static final String EXTRA_SEQUENCE_ID = "EXTRA_INT_DATA3";
    protected String getIntentExtraSequenceField() {
        return EXTRA_SEQUENCE_ID;
    }

    private static final String EXTRA_ERROR_ID = "EXTRA_INT_DATA2";
    protected String getIntentExtraErrorField() {
        return EXTRA_ERROR_ID;
    }    

    protected abstract int getTimeoutErrorCode();
    protected abstract String getErrorTranslation(int code);
    

    private static final int OPEN_REQUEST_ID = 8;
    private static final int CLOSE_REQUEST_ID = 16;
    private static final int READ_REQUEST_ID = 24;

    protected Map<Integer, ScanCallback<Boolean>> resultCallbackMap = Collections.synchronizedMap(new HashMap<Integer, ScanCallback<Boolean>>());
    protected Map<Integer, ScanCallback<Void>> timeoutCallbackMap = Collections.synchronizedMap(new HashMap<Integer, ScanCallback<Void>>());

    protected Object stateLock = new Object();
    protected boolean hasInitialized = false;

    protected static String TAG;

    protected Context applicationContext;

    public BaseIntentHandler(Context context) {
        TAG = this.getClass().getSimpleName();
        applicationContext = context;
    }

    public void start() {
        open(new ScanCallback<Boolean>() {
            @Override
            public void execute(Boolean result) {
                Log.i(TAG, "opened - result [" + result + "].");
            }
        });
    }

    public void stop() {
        close(new ScanCallback<Boolean>() {
            @Override
            public void execute(Boolean result) {
                Log.i(TAG, "closed - result [" + result + "].");
            }
        });
    }

    
    public void open(ScanCallback<Boolean> openResult) {
        Log.i(TAG, "Open called");
        if (hasInitialized) {
            return;
        }
        synchronized (stateLock) {
            if (hasInitialized) {
                return;
            }

            Log.i(TAG, "Making open intent registration calls");

            resultCallbackMap.put(OPEN_REQUEST_ID, openResult);

            applicationContext.registerReceiver(dataReceiver, new IntentFilter(getCallbackDataReceivedIntent()));

            IntentFilter requestFilter = new IntentFilter();
            requestFilter.addAction(getCallbackSuccessIntent());
            requestFilter.addAction(getCallbackFailedIntent());
            applicationContext.registerReceiver(resultReceiver, requestFilter);

            Intent openIntent = new Intent(getOpenIntent());
            openIntent.putExtra(EXTRA_SEQUENCE_ID, OPEN_REQUEST_ID);

            applicationContext.sendBroadcast(openIntent);
            hasInitialized = true;
        }
    }

    /*
    * Override to add any extra variables to the close intent
    */
    protected void processCloseIntent(Intent intent){
        //no-op.  override to add to intent (such as handles)
    }

    public void close(ScanCallback<Boolean> closeResult) {
        Log.i(TAG, "Close called");
        if (!hasInitialized) {
            return;
        }
        synchronized (stateLock) {
            if (!hasInitialized) {
                return;
            }

            Log.i(TAG, "Running close intents");

            //don't wait for a response, let's just close up shop
            Intent closeIntent = new Intent(getCloseIntent());
            processCloseIntent(closeIntent);
            closeIntent.putExtra(getIntentExtraSequenceField(), CLOSE_REQUEST_ID);


            try {
                applicationContext.unregisterReceiver(dataReceiver);
            } catch(Exception ex) {
                Log.e(TAG, "Exception while unregistering data receiver. Was open ever called?", ex);
            }

            try {
                applicationContext.unregisterReceiver(resultReceiver);
            } catch(Exception ex) {
                Log.e(TAG, "Exception while unregistering results receiver. Was open ever called?", ex);
            }

            applicationContext.sendBroadcast(closeIntent);

            //force a closed execution - don't wait for response (as we are probably leaving the foreground anyway!)
            closeResult.execute(true);
            hasInitialized = false;
        }
    }

    protected abstract void onDataRead(Intent intent);

    /**
     * Receiver to handle receiving data from intents
     */
    private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Data receiver trigged");
            try {
                Integer sequenceId = intent.getIntExtra(getIntentExtraSequenceField(), -1);

                onDataRead(intent);

                //remove any timeout or result callbacks registered for this
                timeoutCallbackMap.remove(sequenceId);
            } catch(Exception ex) {
                Log.e(TAG, "Exception raised during callback processing.", ex);
            }
        }
    };

    /*
    * Handle reading any extra data out of the intent to store in the class
    */
    protected void processIntentSuccessCallback(Intent intent) {
        //clear handles or flags
    }
    private BroadcastReceiver resultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Result returned from intent registration: " + intent.getAction());
            int sequenceId = intent.getIntExtra(getIntentExtraSequenceField(), -1);

            int errorCode = intent.getIntExtra(getIntentExtraErrorField(), -1);
            if (errorCode > 0) {
                Log.e(TAG, "ERROR on intent result: " + getErrorTranslation(errorCode));
            }
            //execute callbacks first
            try {
                if(getCallbackSuccessIntent().equals(intent.getAction())) {
                    processIntentSuccessCallback(intent);
                    //success
                    if(resultCallbackMap.containsKey(sequenceId)) {
                        resultCallbackMap.remove(sequenceId).execute(true);
                    }
                } else if(errorCode == getTimeoutErrorCode()) {
                    //timeout
                    if(timeoutCallbackMap.containsKey(sequenceId)) {
                        timeoutCallbackMap.remove(sequenceId).execute(null);
                    }

                    //shouldn't have any result callbacks still waiting, but clear them anyway
                    resultCallbackMap.remove(sequenceId);
                } else {
                    //general failure
                    if(resultCallbackMap.containsKey(sequenceId)) {
                        resultCallbackMap.remove(sequenceId).execute(false);
                    }
                }
            } catch(Exception ex) {
                //don't let a callback's exception throw off the receiver.
                Log.e(TAG, "Exception raised during callback processing.", ex);
            }
        }
    };

}

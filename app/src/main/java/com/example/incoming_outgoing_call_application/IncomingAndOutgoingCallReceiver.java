package com.example.incoming_outgoing_call_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class IncomingAndOutgoingCallReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "OnReceive");
        String action = intent.getAction();
        Log.i(TAG, "action : " + action);
        callHandler(intent);
    }

    private void callHandler(Intent intent) {
        String PhoneNumber = "NA";

        try {
            Log.i(TAG, "State : " + intent.getStringExtra(TelephonyManager.EXTRA_STATE));
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state == null) {
                PhoneNumber = "NA";
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                PhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG, "Incoming number : " + PhoneNumber);
            }
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                PhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.d(TAG, "Outgoing number : " + PhoneNumber);
            }
            if (!PhoneNumber.contentEquals("NA")) {
                Log.i(TAG, "PhoneNumber : " + PhoneNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception is : ", e);
        }
    }
}

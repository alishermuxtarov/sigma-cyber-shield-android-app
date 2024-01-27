package com.sigma.caller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.sigma.caller.BlockListManager;
public class IncomingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Received SMS", Toast.LENGTH_SHORT).show();

//        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//
//        if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//            // Handle incoming call
//            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//
//            // Check if the number is in the block list and take action
//            if (BlockListManager.isNumberBlocked(incomingNumber)) {
//                // Block the call
//                abortBroadcast();
//            }
//        }
    }
}


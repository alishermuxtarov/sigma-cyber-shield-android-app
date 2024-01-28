package com.sigma.caller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class IncomingCallReceiver extends BroadcastReceiver {
    public static FrgService fgs;

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // Handle incoming call
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (Objects.nonNull(incomingNumber)) {
                Analyser anal = FrgService.analayser;
                Analyser.Result result = anal.check(incomingNumber);

                if (!result.isOk || (!Objects.isNull(result.extra) && Objects.equals(result.extra.level, CheckData.CheckDataInfo.LEVEL_WARNING))) {
                    int color = context.getResources().getIdentifier(result.extra.level, "color", context.getPackageName());

                    if (Objects.nonNull(fgs)) {
                        fgs.showNotification(result.extra.title, result.extra.message, color);
                    }
                }
            }
        }
    }
}


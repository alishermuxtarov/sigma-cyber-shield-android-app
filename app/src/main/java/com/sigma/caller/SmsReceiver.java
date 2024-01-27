package com.sigma.caller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.View;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class SmsReceiver extends BroadcastReceiver {
    public static View view;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");

            if (pdus != null) {
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                for (SmsMessage message : messages) {
                    String content = message.getMessageBody();
                    String from = message.getOriginatingAddress();

                    Analyser anal = new Analyser();
                    Analyser.Result result = anal.checkWithContent(from, content);

                    if (!result.isOk || (!Objects.isNull(result.extra) && Objects.equals(result.extra.level, CheckData.CheckDataInfo.LEVEL_WARNING))) {
                        int color = context.getResources().getIdentifier(result.extra.level, "color", context.getPackageName());

                        Snackbar snackbar = Snackbar.make(view, result.extra.message, Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(
                                ContextCompat.getColor(context, color)
                        );

                        snackbar.show();
                    }
                }
            }
        }
    }
}

package com.sigma.caller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class SmsReceiver extends BroadcastReceiver {
    public static FrgService fgs;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");

            if (pdus != null) {
                // SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder messageText = new StringBuilder();
                String from = "";

                for (int i = 0; i < pdus.length; i++) {
                    // messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String messageBody = smsMessage.getMessageBody();
                    from = smsMessage.getOriginatingAddress();

                    messageText.append(messageBody);
                }

                Analyser anal = FrgService.analayser;
                Analyser.Result result = anal.checkWithContent(from, String.valueOf(messageText));

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

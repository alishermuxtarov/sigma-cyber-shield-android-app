package com.sigma.caller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class FrgService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
//    private Handler handler;
//    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
//        handler = new Handler(getMainLooper());

        SmsReceiver.fgs = this;
        IncomingCallReceiver.fgs = this;

//        smsBroadcastReceiver = new SmsBroadcastReceiver();
//        registerSmsReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

//        // Show Notification using Handler
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                showNotification("Foreground Service started");
//            }
//        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregisterSmsReceiver();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification(String title, String message) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MAX)  // Set priority to max
                .setDefaults(NotificationCompat.DEFAULT_ALL)  // Add defaults (lights, sound, etc.)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

//    private void registerSmsReceiver() {
//        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(smsBroadcastReceiver, intentFilter);
//    }

//    private void unregisterSmsReceiver() {
//        unregisterReceiver(smsBroadcastReceiver);
//    }

    // BroadcastReceiver to handle SMS received events
//    private class SmsBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
//                // Extract SMS messages
//                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
//
//                if (messages != null && messages.length > 0) {
//                    // Handle SMS messages and show a notification
//                    String sender = messages[0].getOriginatingAddress();
//                    String messageBody = messages[0].getMessageBody();
//
//                    String notificationText = "SMS Received from " + sender + ": " + messageBody;
//
//                    // Show Notification
//                    showNotification(notificationText);
//                }
//            }
//        }
//    }
}

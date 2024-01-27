package com.sigma.caller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class FrgService extends Service {

    private static final int NOTIFICATION_ID = 1;
    public static boolean showUIToast = false;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    public static View view;

    @Override
    public void onCreate() {
        super.onCreate();
        SmsReceiver.fgs = this;
        IncomingCallReceiver.fgs = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification(String title, String message, int color) {
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
                .setColor(color)  // Set the color here
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

        if (showUIToast) {
            CustomToast.showLongDurationToast(getApplicationContext(), message, 20_000);
        } else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

//        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        snackbar.getView().setBackgroundColor(
//                ContextCompat.getColor(context, color)
//        );
//
//        snackbar.show();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            serviceChannel.setDescription("pew pew");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

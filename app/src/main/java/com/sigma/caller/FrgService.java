package com.sigma.caller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FrgService extends Service {

    private static final int NOTIFICATION_ID = 1;
    public static Analyser analayser;
    public static boolean showUIToast = false;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    public static View view;

    @Override
    public void onCreate() {
        super.onCreate();
        analayser = new Analyser();
        SmsReceiver.fgs = this;
        IncomingCallReceiver.fgs = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        fetchDataFromServer();

        schedulePeriodicTask();

        return START_STICKY;
    }

    private void schedulePeriodicTask() {
        // Use a Handler or a ScheduledExecutorService to schedule the task
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Fetch JSON data from localhost:3000
                fetchDataFromServer();

                // Schedule the next execution
                handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1));
            }
        }, TimeUnit.MINUTES.toMillis(1));
    }

    private void fetchDataFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.24.202.10:3000/")  // Replace with your actual local host address
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Map<String, ResponseItem>> call = apiService.getData();

        call.enqueue(new Callback<Map<String, ResponseItem>>() {
            @Override
            public void onResponse(Call<Map<String, ResponseItem>> call, Response<Map<String, ResponseItem>> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response here
                    Map<String, ResponseItem> data = response.body();

                    for (Map.Entry<String, ResponseItem> entry : data.entrySet()) {
                        analayser.addData(entry);
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Map<String, ResponseItem>> call, Throwable t) {
                Log.d("a", "aaa");
            }
        });
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
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(color)  // Set the color here
                .setColor(ContextCompat.getColor(getApplicationContext(), color))
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

package com.sigma.caller;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////
        LayoutInflater.from(this).inflate(R.layout.custom_snackbar_layout, null);

        FrgService.view = findViewById(android.R.id.content);

        Intent serviceIntent = new Intent(this, FrgService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        ///////


        requestSmsPermission();
    }

    private void requestSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.INTERNET,
                        },
                        SMS_PERMISSION_REQUEST_CODE
                );
            } else {
                // Permission already granted, start SMS listening
                startSmsListener();
            }
        } else {
            // For devices below M, no runtime permission required, start SMS listening
            startSmsListener();
        }
    }

    private void startSmsListener() {
        // Start listening to incoming SMS
        // You can add your logic here to handle the incoming SMS
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start SMS listening
                startSmsListener();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "SMS permission denied. App may not function properly.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        FrgService.showUIToast = true;
        super.onResume();
    }

    @Override
    protected void onRestart() {
        FrgService.showUIToast = true;
        super.onRestart();
        // Perform actions when the app is restarting after being stopped
    }

    @Override
    protected void onDestroy() {
        FrgService.showUIToast = false;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        FrgService.showUIToast = false;
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FrgService.showUIToast = false;
        super.onSaveInstanceState(outState);
        // Save state information before the activity is paused
    }

    @Override
    protected void onStop() {
        FrgService.showUIToast = false;

        super.onStop();
        // Perform cleanup or resource releases when the app is going into the background
    }

}
package com.sigma.caller;
import android.Manifest;
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

        SmsReceiver.view = findViewById(android.R.id.content);

        // Create Snackbar with custom view

        ///////


        requestSmsPermission();
    }

    private void requestSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_PHONE_STATE,
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
        Toast.makeText(this, "SMS permission granted. Listening for SMS.", Toast.LENGTH_SHORT).show();
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
}
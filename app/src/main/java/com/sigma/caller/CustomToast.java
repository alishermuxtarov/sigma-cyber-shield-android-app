package com.sigma.caller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

    public static void showLongDurationToast(Context context, String message, int durationMillis) {
        // Create a custom view for the toast
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null);
        TextView textView = view.findViewById(R.id.toast_text);
        textView.setText(message);

        // Set the background color dynamically
        view.setBackgroundColor(context.getResources().getColor(R.color.critical));

        // Create a custom toast
        final Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);

        // Use a handler to control the duration of the custom toast
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();  // Cancel the toast after the specified duration
            }
        }, durationMillis);

        // Show the custom toast
        toast.show();
    }
}

package com.spot_the_ballgame;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Toast_Message {
    public static void showToastMessage(Context context, String message) {

        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) ((Activity) context).findViewById(R.id.customToast));
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}

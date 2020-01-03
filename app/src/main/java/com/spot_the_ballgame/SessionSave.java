package com.spot_the_ballgame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionSave {
    public static String SaveSession(String key, String value, Context context) {
        if (context != null) {
            SharedPreferences.Editor editor = context.getSharedPreferences("FCM", Activity.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.apply();
        }

        return key;
    }

    public static String getSession(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("FCM", Activity.MODE_PRIVATE);
        return prefs.getString(key, "No data");
    }

    public static void clearAllSession(Context context) {
        if (context != null) {
            context.getSharedPreferences("KEY", 0).getAll().clear();
        }
    }

    public static SharedPreferences.Editor ClearSession(String string, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("FCM", Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        return editor;
    }

}


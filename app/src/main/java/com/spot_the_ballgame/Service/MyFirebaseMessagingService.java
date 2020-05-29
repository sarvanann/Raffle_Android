package com.spot_the_ballgame.Service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.spot_the_ballgame.Game_Two_Act;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Splash_Screen_Act;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String str_auth_token = "";
    private static final String TAG = "FCM Service";
    private static int count = 0;

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        //you can get your text message here.
        String text = data.get("message");
        Log.e("message_message_log", text);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        String str_contest_idd = Objects.requireNonNull(remoteMessage.getData().get("contest"));

        Log.e(TAG, "Message_Notification_Title" + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "Message_Notification_remoteMessage" + remoteMessage.getData());
        Log.e(TAG, "Message_Notification_Message" + remoteMessage.getNotification().getBody());
        Log.e(TAG, "Message_Notification_Click_action" + remoteMessage.getNotification().getClickAction());
        Log.e(TAG, "Message_Notification_str_contest_idd" + str_contest_idd);
        SessionSave.SaveSession("Session_str_contest_idd", str_contest_idd, this);
        Intent intent = null;
        if (click_action.equals("TRANSACTIONACTIVITY")) {
            intent = new Intent(click_action);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (click_action.equals("GAMETWOACTIVITY")) {
            intent = new Intent(click_action);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(count, mBuilder.build());
    }
}
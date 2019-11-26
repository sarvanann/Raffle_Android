package com.spot_the_ballgame;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Dummy_Splash_Act extends AppCompatActivity {
    private static final String TAG = "splashscreen";
    public static SQLiteDatabase db;
    static long SLEEP_TIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy__splash_);
        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private class IntentLauncher extends Thread {
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME * 180);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Dummy_Splash_Act.this, Navigation_Drawer_Act.class);
            startActivity(intent);
        }

    }
}

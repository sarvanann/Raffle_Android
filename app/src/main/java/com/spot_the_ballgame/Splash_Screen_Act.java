package com.spot_the_ballgame;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.spot_the_ballgame.Registration.Mobile_Num_Registration;
import com.spot_the_ballgame.Registration.SignIn.Email_Sign_In_Act;
import com.spot_the_ballgame.Registration.SignUp.All_Btn_OnClick_Sign_Up_Act;
import com.spot_the_ballgame.Registration.Update_New_Password_Act;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class Splash_Screen_Act extends AppCompatActivity {
    private static final String TAG = "splashscreen";
    public static SQLiteDatabase db;
    static long SLEEP_TIME = 2;
    int int_normal_status, int_carosel_status, int_verify_status;
    public static String str_global_mail_id, str_carosel_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash__screen_);
        getSupportActionBar().hide();
        db = getApplicationContext().openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar,EMAIL varchar,PHONENO varchar,APPID varchar,WALET double," +
                "TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar,USERNAME varchar,ACTIVE int,VERIFIED int,SIGNUPSTATUS int,BALANCE int,USER_SELECTION_VALUE varchar)");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
        printHashKey(getApplicationContext());
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private class IntentLauncher extends Thread {
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME * 180);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String select = "Select SIGNUPSTATUS from LOGINDETAILS where STATUS ='" + 1 + "'";
            Cursor cursor = db.rawQuery(select, null);
            int n1 = cursor.getCount();
            if (n1 > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int_normal_status = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                int_normal_status = 0;
            }
            str_carosel_status = (SessionSave.getSession("Carosel_Status", Splash_Screen_Act.this));
            Log.e("str_carosel_status", "" + str_carosel_status);
            Log.e("int_normal_status", "" + int_normal_status);
//            if (int_normal_status == 0 && str_carosel_status.equalsIgnoreCase("No data")) {
            if (str_carosel_status.equalsIgnoreCase("No data")) {
                Intent intent = new Intent(Splash_Screen_Act.this, Carousel_View_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

            if (int_normal_status == 1) {
                Intent intent = new Intent(Splash_Screen_Act.this, Mobile_Num_Registration.class);
//                Intent intent = new Intent(Splash_Screen_Act.this, Navigation_Drawer_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (int_normal_status == 2) {
                Intent intent = new Intent(Splash_Screen_Act.this, Navigation_Drawer_Act.class);
//                Intent intent = new Intent(Splash_Screen_Act.this, Mobile_Num_Verification_Otp_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (int_normal_status == 3 || int_normal_status == 6) {
                Intent intent = new Intent(Splash_Screen_Act.this, Navigation_Drawer_Act.class);
//                Intent intent = new Intent(Splash_Screen_Act.this, Home_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (int_normal_status == 4) {
                Intent intent = new Intent(Splash_Screen_Act.this, Email_Sign_In_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (int_normal_status == 5) {
                Intent intent = new Intent(Splash_Screen_Act.this, Update_New_Password_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (str_carosel_status.equalsIgnoreCase("1")) {
                Intent intent = new Intent(Splash_Screen_Act.this, All_Btn_OnClick_Sign_Up_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }
    }
}

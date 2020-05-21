package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.UserModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Referral_Code_Act extends Activity implements View.OnClickListener {
    ConstraintLayout constraintLayout_referral_layout, constraintLayout_referral_success_layout;
    TextView tv_changing_message;
    EditText et_referral_code;
    TextView tv_skip;
    Button btn_submit;
    SQLiteDatabase db;

    String str_intent_source_details = "";
    String str_email = "";
    String str_status = "";
    String str_referral_code = "";
    String str_auth_token = "";

    Handler handler;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_code_act_layout);
        et_referral_code = findViewById(R.id.et_referral_code);
        tv_skip = findViewById(R.id.tv_skip);
        btn_submit = findViewById(R.id.btn_submit);
        constraintLayout_referral_layout = findViewById(R.id.constraintLayout_referral_layout);
        constraintLayout_referral_success_layout = findViewById(R.id.constraintLayout_referral_success_layout);
        tv_changing_message = findViewById(R.id.tv_changing_message);


        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar,EMAIL varchar,PHONENO int,APPID varchar,WALET double,TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar,BALANCE int,USERNAME varchar,ACTIVE int,VERIFIED int)");

        String select = "Select SOURCEDETAILS ,EMAIL,SIGNUPSTATUS ,PHONENO FROM LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_intent_source_details = cursor.getString(0);
                str_email = cursor.getString(1);
                str_status = String.valueOf(cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        str_auth_token = SessionSave.getSession("Token_value", Referral_Code_Act.this);
        btn_submit.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                str_referral_code = et_referral_code.getText().toString();
                if (str_referral_code.isEmpty()) {
                    Toast_Message.showToastMessage(Referral_Code_Act.this, "Please enter referral code");
                } else {
                    Referral_Code_Details();
                }
                break;
            case R.id.tv_skip:
                ContentValues contentValues = new ContentValues();
                contentValues.put("SIGNUPSTATUS", 8);
                db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                DBEXPORT();

                Insert_Referral_Code_Sessio_Details();
                Intent intent = new Intent(Referral_Code_Act.this, Navigation_Drawer_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    @SuppressLint("LongLogTag")
    private void Referral_Code_Details() {
        str_referral_code = et_referral_code.getText().toString();
        Log.e("emailemail", str_email);
        Log.e("str_referral_code", str_referral_code);
        Log.e("str_auth_token_referral_code", str_auth_token);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            jsonObject.put("referral", str_referral_code);
            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.VERIFY_REFERRAL_RESPONSE_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = Objects.requireNonNull(response.body()).status;
                            String str_message = response.body().message;
                            if (str_status.equalsIgnoreCase("Success")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("SIGNUPSTATUS", 8);
                                db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                                DBEXPORT();

                                tv_changing_message.setText("Success!!");
                                constraintLayout_referral_layout.setVisibility(View.GONE);
                                constraintLayout_referral_success_layout.setVisibility(View.VISIBLE);
                                Toast_Message.showToastMessage(Referral_Code_Act.this, str_message);
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Insert_Referral_Code_Sessio_Details();
                                        Intent intent = new Intent(Referral_Code_Act.this, Navigation_Drawer_Act.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                                        Toast.makeText(Referral_Code_Act.this, "" + str_message, Toast.LENGTH_SHORT).show();
                                    }
                                }, 1500);

                            } else {
                                Toast_Message.showToastMessage(Referral_Code_Act.this, str_message);
//                                Toast.makeText(Referral_Code_Act.this, "" + str_message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response.code() == 401) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DBEXPORT() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.spot_the_ballgame" + "/databases/" + "Spottheball.db";
        String backupDBPath = "Spottheball_Demo.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(getApplicationContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Insert_Referral_Code_Sessio_Details() {
        SessionSave.SaveSession("Referral_Code_Status", "1", Referral_Code_Act.this);
    }
}

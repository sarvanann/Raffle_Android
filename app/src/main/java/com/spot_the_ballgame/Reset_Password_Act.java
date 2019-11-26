package com.spot_the_ballgame;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Objects;

public class Reset_Password_Act extends AppCompatActivity implements View.OnClickListener {
    Button btn_update;
    SQLiteDatabase db;
    TextView tv_resend_code;
    EditText et_email_pwd;
    ProgressDialog pd;
    String str_email_pwd;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_act_layout);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar,EMAIL varchar,PHONENO int,APPID varchar,WALET double,TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar,BALANCE int,USERNAME varchar,ACTIVE int,VERIFIED int)");

        pd = new ProgressDialog(Reset_Password_Act.this);
        tv_resend_code = findViewById(R.id.tv_resend_code);
        et_email_pwd = findViewById(R.id.et_email_pwd);
        btn_update = findViewById(R.id.btn_update);

        btn_update.setOnClickListener(this);
        tv_resend_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend_code:
                Get_Resend_OTP_Details();
                break;
            case R.id.btn_update:
                str_email_pwd = et_email_pwd.getText().toString();
                Get_Email_OTP_Details();
                break;
        }
    }

    private void Get_Email_OTP_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("", "");
        } catch (Exception e) {

        }
    }

    private void Get_Resend_OTP_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", "");

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

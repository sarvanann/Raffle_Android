package com.spot_the_ball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Email_Sign_In_Act extends AppCompatActivity implements View.OnClickListener {
    EditText et_email_id_in_email_signin, et_pwd_in_email_signin;
    Button tv_forgot_pwd_in_email_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__sign__in_);
        getSupportActionBar().hide();

        et_email_id_in_email_signin = findViewById(R.id.et_email_id_in_email_signin);
        et_pwd_in_email_signin = findViewById(R.id.et_pwd_in_email_signin);
        tv_forgot_pwd_in_email_signin = findViewById(R.id.tv_forgot_pwd_in_email_signin);
        tv_forgot_pwd_in_email_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_pwd_in_email_signin:
                Intent intent = new Intent(Email_Sign_In_Act.this, Forgot_Pwd_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

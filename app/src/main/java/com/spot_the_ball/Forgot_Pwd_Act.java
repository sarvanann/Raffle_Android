package com.spot_the_ball;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Forgot_Pwd_Act extends AppCompatActivity implements View.OnClickListener {
    Button tv_send_verification_code_in_forgot_pwd;
    EditText et_email_id_in_forgot_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__pwd_);
        getSupportActionBar().hide();
        tv_send_verification_code_in_forgot_pwd = findViewById(R.id.tv_send_verification_code_in_forgot_pwd);
        et_email_id_in_forgot_pwd = findViewById(R.id.et_email_id_in_forgot_pwd);
        tv_send_verification_code_in_forgot_pwd.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_verification_code_in_forgot_pwd:
                Intent intent = new Intent(Forgot_Pwd_Act.this, Mobile_Num_Verification.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }
}

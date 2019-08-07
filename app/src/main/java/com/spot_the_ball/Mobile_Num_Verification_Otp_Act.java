package com.spot_the_ball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Mobile_Num_Verification_Otp_Act extends AppCompatActivity {
    TextView tv_pincode_national_in_otp;
    EditText et_mobile_number_in_otp, et_otp_01, et_otp_02, et_otp_03, et_otp_04;
    Button btn_send_code_in_otp, btn_resend_code_in_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__num__verification__otp_);
        getSupportActionBar().hide();
        tv_pincode_national_in_otp = findViewById(R.id.tv_pincode_national_in_otp);
        et_mobile_number_in_otp = findViewById(R.id.et_mobile_number_in_otp);
        et_otp_01 = findViewById(R.id.et_otp_01);
        et_otp_02 = findViewById(R.id.et_otp_02);
        et_otp_03 = findViewById(R.id.et_otp_03);
        et_otp_04 = findViewById(R.id.et_otp_04);
        btn_send_code_in_otp = findViewById(R.id.btn_send_code_in_otp);
        btn_resend_code_in_otp = findViewById(R.id.btn_resend_code_in_otp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

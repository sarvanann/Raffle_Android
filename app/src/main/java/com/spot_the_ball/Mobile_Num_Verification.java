package com.spot_the_ball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Mobile_Num_Verification extends AppCompatActivity implements View.OnClickListener {
    TextView tv_pincode_national;
    EditText et_mobile_number;
    Button btn_send_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__num__verification);
        getSupportActionBar().hide();
        tv_pincode_national = findViewById(R.id.tv_pincode_national_in_mobile_num_verification);
        et_mobile_number = findViewById(R.id.et_mobile_number_in_mobile_num_verification);
        btn_send_code = findViewById(R.id.btn_send_code_in_mobile_num_verification);
        btn_send_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code_in_mobile_num_verification:
                Intent intent = new Intent(Mobile_Num_Verification.this, Mobile_Num_Verification_Otp_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

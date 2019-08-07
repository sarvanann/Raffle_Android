package com.spot_the_ball;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Sign_Up_Act extends AppCompatActivity implements View.OnClickListener {
    TextView tv_sign_up_in_signup, tv_sign_in_in_signup;
    ConstraintLayout constraintLayout_google_signup_in_signup, constraintLayout_facebook_signup_in_signup, constraintLayout_email_signup_in_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        tv_sign_up_in_signup = findViewById(R.id.tv_sign_up_in_signup);
        tv_sign_in_in_signup = findViewById(R.id.tv_sign_in_in_signup);

        constraintLayout_google_signup_in_signup = findViewById(R.id.constraintLayout_google_signup_in_signup);
        constraintLayout_facebook_signup_in_signup = findViewById(R.id.constraintLayout_facebook_signup_in_signup);
        constraintLayout_email_signup_in_signup = findViewById(R.id.constraintLayout_email_signup_in_signup);
        tv_sign_in_in_signup.setOnClickListener(this);
        constraintLayout_google_signup_in_signup.setOnClickListener(this);
        constraintLayout_facebook_signup_in_signup.setOnClickListener(this);
        constraintLayout_email_signup_in_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_in_in_signup:
                Intent intent = new Intent(Sign_Up_Act.this, Sign_In_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.constraintLayout_google_signup_in_signup:
                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                break;
            case R.id.constraintLayout_facebook_signup_in_signup:
                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                break;
            case R.id.constraintLayout_email_signup_in_signup:
                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

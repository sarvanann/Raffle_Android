package com.spot_the_ball;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Sign_In_Act extends AppCompatActivity implements View.OnClickListener {
    TextView tv_sign_up_in_signin, tv_sign_in_in_signin;
    ConstraintLayout constraintLayout_google_signin, constraintLayout_facebook_signin, constraintLayout_email_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        tv_sign_in_in_signin = findViewById(R.id.tv_sign_in_in_signin);
        tv_sign_up_in_signin = findViewById(R.id.tv_sign_up_in_signin);

        constraintLayout_google_signin = findViewById(R.id.constraintLayout_google_signin);
        constraintLayout_facebook_signin = findViewById(R.id.constraintLayout_facebook_signin);
        constraintLayout_email_signin = findViewById(R.id.constraintLayout_email_signin);

        tv_sign_up_in_signin.setOnClickListener(this);
        constraintLayout_google_signin.setOnClickListener(this);
        constraintLayout_facebook_signin.setOnClickListener(this);
        constraintLayout_email_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up_in_signin:
                Intent intent = new Intent(Sign_In_Act.this, Sign_Up_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.constraintLayout_google_signin:
                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                break;
            case R.id.constraintLayout_facebook_signin:
                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                break;
            case R.id.constraintLayout_email_signin:
                Intent intent1 = new Intent(Sign_In_Act.this, Email_Sign_In_Act.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
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

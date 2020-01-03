package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Code_Testing_Page extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonus_points_layout);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).asGif().load(R.drawable.giphy_conft_02).into(imageView);
    }
}
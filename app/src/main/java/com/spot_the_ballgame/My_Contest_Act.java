package com.spot_the_ballgame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.spot_the_ballgame.Fragments.My_Contest_Fragment;

public class My_Contest_Act extends AppCompatActivity {
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contest);
        getSupportActionBar().hide();
        Show_Checking_Fragment();
    }

    private void Show_Checking_Fragment() {
        fragment = new My_Contest_Fragment();
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }
}

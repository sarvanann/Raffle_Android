package com.spot_the_ballgame;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Game_Details_Screen_Act_02 extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout constraintLayout_game_details_screen_in_game_two, constraintLayout_prize_layout, constraintLayout_rules_list, constraintLayout_score_board_layout;
    TextView tv_price_btn, tv_rules_btn, tv_watch_ads_btn, tv_enter_contest_btn, tv_score_board;
    String str_onclick_contest_value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen_02);
        getSupportActionBar().hide();
        tv_price_btn = findViewById(R.id.tv_price_btn);
        tv_rules_btn = findViewById(R.id.tv_rules_btn);
        tv_watch_ads_btn = findViewById(R.id.tv_watch_ads_btn);
        tv_enter_contest_btn = findViewById(R.id.tv_enter_contest_btn);
        tv_score_board = findViewById(R.id.tv_score_board);
        constraintLayout_prize_layout = findViewById(R.id.constraintLayout_prize_layout);
        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_rules_list = findViewById(R.id.constraintLayout_rules_list);
        constraintLayout_score_board_layout = findViewById(R.id.constraintLayout_score_board_layout);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle == null) {
//            str_onclick_contest_value = null;
//        } else {
//            str_onclick_contest_value = bundle.getString("onclick_contest_value");
//        }
//        Log.e("str_onclick_contest", str_onclick_contest_value);
//        if (str_onclick_contest_value == null) {
//            tv_watch_ads_btn.setVisibility(View.VISIBLE);
//            tv_enter_contest_btn.setVisibility(View.VISIBLE);
//        } else {
//            tv_watch_ads_btn.setVisibility(View.GONE);
//            tv_enter_contest_btn.setVisibility(View.GONE);
//        }

        tv_price_btn.setOnClickListener(this);
        tv_rules_btn.setOnClickListener(this);
        tv_score_board.setOnClickListener(this);
       // tv_watch_ads_btn.setOnClickListener(this);
        //tv_enter_contest_btn.setOnClickListener(this);
//        FullScreenMethod();

    }

    private void FullScreenMethod() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(Game_Details_Screen_Act.this, Navigation_Drawer_Act.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


        startActivity(intent);
        finish();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_btn:
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.white_color));
                constraintLayout_prize_layout.setVisibility(View.VISIBLE);
                constraintLayout_rules_list.setVisibility(View.GONE);
                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_rules_btn:
                constraintLayout_prize_layout.setVisibility(View.GONE);
                constraintLayout_rules_list.setVisibility(View.VISIBLE);
                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.white_color));
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_watch_ads_btn:
                constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                Intent intent = new Intent(Game_Details_Screen_Act_02.this, Game_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.tv_enter_contest_btn:
                Intent intent_01 = new Intent(Game_Details_Screen_Act_02.this, Reward_Video_Act.class);
                intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_01);
                break;
            case R.id.tv_score_board:
                constraintLayout_score_board_layout.setVisibility(View.VISIBLE);
                constraintLayout_prize_layout.setVisibility(View.GONE);
                constraintLayout_rules_list.setVisibility(View.GONE);
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
        }
    }
}

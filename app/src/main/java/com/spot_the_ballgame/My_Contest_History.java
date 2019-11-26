package com.spot_the_ballgame;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class My_Contest_History extends AppCompatActivity implements View.OnClickListener {
    TextView textView28, textView288, textView289;
    TextView tv_price_btn, tv_rules_btn, tv_answers_btn, tv_score_board;
    ConstraintLayout constraintLayout_game_details_screen_in_game_two, constraintLayout_prize_layout, constraintLayout_rules_list, constraintLayout_score_board_layout, constraintLayout_answer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen_03);
        getSupportActionBar().hide();
//        textView28 = findViewById(R.id.textView28);
//        textView288 = findViewById(R.id.textView288);
//        textView289 = findViewById(R.id.textView289);
//
//        textView28.setMovementMethod(new ScrollingMovementMethod());
//        textView288.setMovementMethod(new ScrollingMovementMethod());
//        textView289.setMovementMethod(new ScrollingMovementMethod());

        tv_price_btn = findViewById(R.id.tv_price_btn);
        tv_rules_btn = findViewById(R.id.tv_rules_btn);
        tv_answers_btn = findViewById(R.id.tv_answers_btn);
        tv_score_board = findViewById(R.id.tv_score_board);

        constraintLayout_prize_layout = findViewById(R.id.constraintLayout_prize_layout);
        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_rules_list = findViewById(R.id.constraintLayout_rules_list);
        constraintLayout_score_board_layout = findViewById(R.id.constraintLayout_score_board_layout);
        constraintLayout_answer_layout = findViewById(R.id.constraintLayout_answer_layout);

        constraintLayout_answer_layout.setVisibility(View.GONE);
        tv_price_btn.setOnClickListener(this);
        tv_rules_btn.setOnClickListener(this);
        tv_score_board.setOnClickListener(this);
        tv_answers_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        My_Contest_History.this.getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_btn:
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.white_color));
                constraintLayout_prize_layout.setVisibility(View.VISIBLE);
                constraintLayout_rules_list.setVisibility(View.GONE);

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                constraintLayout_answer_layout.setVisibility(View.GONE);

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_rules_btn:
                constraintLayout_prize_layout.setVisibility(View.GONE);
                constraintLayout_rules_list.setVisibility(View.VISIBLE);
                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.white_color));


                constraintLayout_score_board_layout.setVisibility(View.GONE);
                constraintLayout_answer_layout.setVisibility(View.GONE);
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_score_board:
                constraintLayout_score_board_layout.setVisibility(View.VISIBLE);
                constraintLayout_prize_layout.setVisibility(View.GONE);
                constraintLayout_rules_list.setVisibility(View.GONE);
                constraintLayout_answer_layout.setVisibility(View.GONE);

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;


            case R.id.tv_answers_btn:
                constraintLayout_answer_layout.setVisibility(View.VISIBLE);
                constraintLayout_score_board_layout.setVisibility(View.GONE);
                constraintLayout_prize_layout.setVisibility(View.GONE);
                constraintLayout_rules_list.setVisibility(View.GONE);

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;

        }
    }
}

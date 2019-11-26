package com.spot_the_ballgame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Objects;

public class Dummy_Image_Act extends Activity {
    ArrayList<Integer> imageIntegerArrayList = new ArrayList<>();
    ImageView iv_changing_image;
    CountDownTimer countDownTimer;
    public long seconds;
    TextView tv_remaining_count_value, tv_total_count_value,
            tv_timer_seconds_count, tv_timer_seconds_txt;
    Vibrator vibrator;
    Dialog dialog_fr_timer;
    int int_count_value = 0;
    int int_reaming_page_count_value = 1;
    String str_count_down_seconds;
    long milliseconds;
    ConstraintLayout constraintLayout_count_down_timer;

    private boolean mAllowShake = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_for_image_03);
        tv_remaining_count_value = findViewById(R.id.tv_remaing_count_value_in_game_two);
        tv_total_count_value = findViewById(R.id.tv_total_count_value);
        tv_timer_seconds_count = findViewById(R.id.tv_timer_seconds_count_game_two);
        tv_timer_seconds_txt = findViewById(R.id.tv_timer_seconds_txt_game_two);
        iv_changing_image = findViewById(R.id.iv_changing_image);
        constraintLayout_count_down_timer = findViewById(R.id.constraintLayout_count_down_timer);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dialog_fr_timer = new Dialog(Dummy_Image_Act.this);
        dialog_fr_timer.setContentView(R.layout.please_wait_lay);
        Objects.requireNonNull(dialog_fr_timer.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*1st set of images*/
        imageIntegerArrayList.add(R.mipmap.ic_cover_1);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.mipmap.ic_cover_1);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.mipmap.ic_cover_1);
        /*2nd set of images*/
        imageIntegerArrayList.add(R.mipmap.ic_cover_1);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.mipmap.ic_cover_1);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.mipmap.ic_cover_1);


        assert str_count_down_seconds != null;
        int time = 6;
        milliseconds = time * 1000 + 1;
        startTimer(milliseconds);

        if (int_reaming_page_count_value == 1) {
            tv_remaining_count_value.setText(String.valueOf(int_reaming_page_count_value));
            iv_changing_image.setImageResource(imageIntegerArrayList.get(0));
        }
    }

    private void startTimer(long milliseconds) {
        long milli = milliseconds + 1000;
        countDownTimer = new CountDownTimer(milli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
                Log.e("Seconds", "" + seconds);
                String s1 = String.format("%2d", seconds).trim();
                if (s1.length() == 2) {
                    tv_timer_seconds_count.setText(s1);
                } else {
                    tv_timer_seconds_count.setText("0" + s1);
                }
                if (seconds == 9) {
                    tv_timer_seconds_count.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                } else if (seconds == 3) {
                    if (mAllowShake) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(50);
                        }
                    }
                    tv_timer_seconds_count.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                    constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_red_alert_bg));
                } else if (seconds == 2) {
                    if (mAllowShake) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(50);
                        }
                    }
                } else if (seconds == 1) {
                    if (mAllowShake) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(50);
                        }
                    }
                } else if (seconds == 0) {
                    Toast.makeText(Dummy_Image_Act.this, "kkkksfld;mfld;s", Toast.LENGTH_SHORT).show();
                    tv_timer_seconds_count.setText("00");
                }
            }

            @Override
            public void onFinish() {
                tv_timer_seconds_count.setText("00");
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_timer_seconds_count.setTextColor(getResources().getColor(R.color.white_color));
                tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                do {
                    if (int_count_value < imageIntegerArrayList.size()) {
                        int n1 = Integer.parseInt(tv_remaining_count_value.getText().toString());
                        int_reaming_page_count_value = n1 + 1;
                        int_count_value = n1;
                        Log.e("reamingpg_count_value", "" + int_reaming_page_count_value);
                        Log.e("int_count_value", "" + int_count_value);
                        try {
                            if (int_reaming_page_count_value <= 10) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog_fr_timer.dismiss();
                                        tv_remaining_count_value.setText(String.valueOf(int_reaming_page_count_value));
                                        iv_changing_image.setImageResource(imageIntegerArrayList.get(int_count_value));
                                        tv_timer_seconds_count.setText("00");
                                        Log.e("int_count_value", "" + int_count_value);
                                        tv_timer_seconds_count.setText("00");
                                        startTimer(milliseconds);
                                    }
                                }, 1000);

                            } else {
                                dialog_fr_timer.dismiss();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Dummy_Image_Act.this, Navigation_Drawer_Act.class);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                    }
                                }, 2000);

                            }

                        } catch (IndexOutOfBoundsException ari) {
                            ari.printStackTrace();
                        }
                    }
                } while (int_count_value > imageIntegerArrayList.size());
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAllowShake = false;
        vibrator.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAllowShake = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

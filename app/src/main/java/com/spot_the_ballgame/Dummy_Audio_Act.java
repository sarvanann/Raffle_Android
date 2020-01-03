package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Dummy_Audio_Act extends Activity {
    private boolean mAllowShake = false;
    Button btn_rewind;
    ArrayList<Integer> audio_IntegerArrayList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private SeekBar seekbar;
    public static int oneTimeOnly = 0;
    CountDownTimer countDownTimer;
    public long seconds;
    TextView tv_remaining_count_value, tv_total_count_value,
            tv_timer_seconds_count, tv_timer_seconds_txt, textView94;
    private int progressStatus = 0;
    Vibrator vibrator;
    Dialog dialog_fr_timer;

    int int_count_value = 0;
    int int_reaming_page_count_value = 1;
    int int_2_x_count = 0;

    SeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen_02);
        tv_remaining_count_value = findViewById(R.id.tv_remaing_count_value_in_game_two);
        tv_total_count_value = findViewById(R.id.tv_total_count_value);
        tv_timer_seconds_count = findViewById(R.id.tv_timer_seconds_count_game_two);
        tv_timer_seconds_txt = findViewById(R.id.tv_timer_seconds_txt_game_two);
        textView94 = findViewById(R.id.textView94);


        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dialog_fr_timer = new Dialog(Dummy_Audio_Act.this);
        dialog_fr_timer.setContentView(R.layout.please_wait_lay);
        Objects.requireNonNull(dialog_fr_timer.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        audio_IntegerArrayList.add(R.raw.cut_01);
        audio_IntegerArrayList.add(R.raw.cut_02);
        audio_IntegerArrayList.add(R.raw.cut_03);
        audio_IntegerArrayList.add(R.raw.cut_04);
        audio_IntegerArrayList.add(R.raw.cut_01);
        audio_IntegerArrayList.add(R.raw.cut_02);
        audio_IntegerArrayList.add(R.raw.cut_03);
        audio_IntegerArrayList.add(R.raw.cut_04);
        audio_IntegerArrayList.add(R.raw.cut_01);
        audio_IntegerArrayList.add(R.raw.cut_02);

        btn_rewind = findViewById(R.id.btn_rewind);
        startTimer();

        mediaPlayer = MediaPlayer.create(this, audio_IntegerArrayList.get(0));
//        mediaPlayer.start();
//        mediaPlayer.setLooping(true);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        if (!mediaPlayer.isPlaying()) {
            Audio_Play_Method();
        }

        btn_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                Button_Rewind_Method();
            }
        });
    }

    private void Button_Rewind_Method() {
        btn_rewind.setBackground(getResources().getDrawable(R.drawable.pause_circular_button));
        mediaPlayer.start();
//        mediaPlayer.setLooping(true);
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 0);
    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(15000, 1000) {
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
                }
            }

            @Override
            public void onFinish() {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_timer_seconds_count.setTextColor(getResources().getColor(R.color.white_color));
                tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                do {
                    if (int_count_value < audio_IntegerArrayList.size()) {
                        int n1 = Integer.parseInt(tv_remaining_count_value.getText().toString());
                        int_reaming_page_count_value = n1 + 1;
                        int_count_value = n1;
                        try {
                            if (int_reaming_page_count_value <= 10) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog_fr_timer.dismiss();
                                        progressStatus = 0;
                                        progressStatus += 1;

                                        tv_remaining_count_value.setText(String.valueOf(int_reaming_page_count_value));
                                        seekbar.setBackgroundResource(audio_IntegerArrayList.get(int_count_value));

                                        mediaPlayer = MediaPlayer.create(Dummy_Audio_Act.this, audio_IntegerArrayList.get(int_count_value));
                                        mediaPlayer.start();
                                        btn_rewind.setBackground(getResources().getDrawable(R.drawable.pause_circular_button));

//                                        mediaPlayer.setLooping(true);
                                        Log.e("int_count_value", "" + int_count_value);
                                        tv_timer_seconds_count.setText("00");
                                        startTimer();
//                                        Button_Rewind_Method();
                                    }
                                }, 1000);

                            } else {
                                dialog_fr_timer.dismiss();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Dummy_Audio_Act.this, Navigation_Drawer_Act.class);
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
                } while (int_count_value > audio_IntegerArrayList.size());
            }
        }.start();
    }

    @SuppressLint("DefaultLocale")
    private void Audio_Play_Method() {
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 0);

    }

    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            textView94.setText(String.format("%d sec",
//                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );

            if (finalTime <= startTime) {
                btn_rewind.setBackground(getResources().getDrawable(R.drawable.play_button));
            }
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mAllowShake = false;
        vibrator.cancel();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAllowShake = true;
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
            btn_rewind.setBackgroundResource(R.drawable.play_button);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
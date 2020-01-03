package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Adapter.ContestAdapter_For_End_Game;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory_For_Categories;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_Act extends AppCompatActivity implements View.OnClickListener {
    String str_auth_token;
    Dialog dialog;
    SQLiteDatabase db;
    String str_source_details, str_email, str_phone_no, str_balance;
    int int_onclick_getting_remaing_value, int_signup_status;
    TextView tv_A_alphabet, tv_B_alphabet, tv_C_alphabet, tv_D_alphabet,
            tv_2x_txt, tv_2x_power_up, tv_remaining_count_value, tv_total_count_value,
            tv_timer_seconds_count_game_one, tv_timer_seconds_txt;
    CountDownTimer countDownTimer;
    ConstraintLayout constraintLayout_timer;
    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    public int int_sec = 10;
    private long totalTimeCountInMilliseconds;
    public long seconds;
    ConstraintLayout constraintLayout_tv_2x_power_up, constraintLayout_just_a_moment, constraintLayout_game, constraintLayout_end_game, constraintLayout_count_down_timer_game_one, constraintLayout_tv_just_a_moment_contest_one_txt;
//    ConstraintLayout constraintLayout32;


    ImageView iv_changing_image;
    int int_count_value = 0;
    int int_reaming_page_count_value = 1;
    int int_2_x_count = 0;
    ArrayList<Integer> imageIntegerArrayList = new ArrayList<Integer>();
    String str_alphabet_input_value = "";
    String str_remaining_count_value = "";

    String str_user_selection_value;
    String str_db_values;
    String str_db_concat;
    TextView tv_just_a_moment_contest_one, tv_close_end_game;

    ProgressBar progress_bar_horizontal;
    private int progressStatus = 0;
    //The number of milliseconds in the future from the
    //call to start() until the count down is done
    private long millisInFuture = 10000; //20 seconds (make it dividable by 1000)
    //The interval along the way to receive onTick() callbacks
    private long countDownInterval = 1000; //1 second (don't change this value)
    int progressBarMaximumValue;

    //  AnimationDrawable animationDrawable;

    Vibrator vibrator;
    Dialog dialog_fr_timer;
    Bundle bundle;
    String str_count_down_seconds, str_2x_powerup, str_entry_fees, str_correct_ans, str_wrong_ans, str_skip;
    int int_2x_onclick_count = 0;
    int int_entry_fee, int_correct_ans, int_wrong_ans, int_skip, int_db_balance;
    TextView tv_time_limit_sec_txt;
    private AdView mAdView;

    int n1_old_value, n2_new_value, n3_normal_value;
    int int_onclcik_2x_power_up = 0;
    ImageView iv_ready_steady_go_state;
    ImageView iv_end_game_sucess_message_gif;

    RecyclerView rv_single_card_end_game_one;
    ContestAdapter_For_End_Game contestAdapter_for_end_game;
    String str_code, str_message;

    TextView tv_skip_points_values, tv_wrong_ans_values, tv_correct_ans_values;
    private AdView mAdView_end_game;
    AdRequest adRequest;

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_03);
        db = getApplicationContext().openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar," +
                "EMAIL varchar,PHONENO int,APPID varchar,WALET double,TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar" +
                ",USERNAME varchar,ACTIVE int,VERIFIED int,SIGNUPSTATUS int,BALANCE int,USER_SELECTION_VALUE varchar)");
        getSupportActionBar().hide();
        tv_A_alphabet = findViewById(R.id.tv_A_alphabet);
        tv_B_alphabet = findViewById(R.id.tv_B_alphabet);
        tv_C_alphabet = findViewById(R.id.tv_C_alphabet);
        tv_D_alphabet = findViewById(R.id.tv_D_alphabet);
        tv_2x_txt = findViewById(R.id.tv_2x_txt);
        iv_ready_steady_go_state = findViewById(R.id.iv_ready_steady_go_state);
        Glide.with(Game_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
        tv_2x_power_up = findViewById(R.id.tv_2x_power_up);
        tv_remaining_count_value = findViewById(R.id.tv_remaining_count_value);
        tv_total_count_value = findViewById(R.id.tv_total_count_value);
        tv_timer_seconds_count_game_one = findViewById(R.id.tv_timer_seconds_count_game_one);
        tv_timer_seconds_txt = findViewById(R.id.tv_timer_seconds_txt_game_one);
        constraintLayout_just_a_moment = findViewById(R.id.constraintLayout_just_a_moment_contest_one);
        constraintLayout_tv_2x_power_up = findViewById(R.id.constraintLayout_tv_2x_power_up);
        constraintLayout_tv_just_a_moment_contest_one_txt = findViewById(R.id.constraintLayout_tv_just_a_moment_contest_one_txt);
        tv_just_a_moment_contest_one = findViewById(R.id.tv_just_a_moment_contest_one);
        tv_time_limit_sec_txt = findViewById(R.id.tv_time_limit_sec_txt);

        tv_correct_ans_values = findViewById(R.id.tv_correct_ans_values);
        tv_wrong_ans_values = findViewById(R.id.tv_wrong_ans_values);
        tv_skip_points_values = findViewById(R.id.tv_skip_points_values);


        tv_close_end_game = findViewById(R.id.tv_close_end_game);
        tv_close_end_game.setText(R.string.go_back_txt);

        constraintLayout_end_game = findViewById(R.id.constraintLayout_end_game);
        constraintLayout_game = findViewById(R.id.constraintLayout_game_screen);
        iv_changing_image = findViewById(R.id.iv_changing_image);
        constraintLayout_count_down_timer_game_one = findViewById(R.id.constraintLayout_count_down_timer_game_one);
//        constraintLayout32 = findViewById(R.id.constraintLayout32);
        progress_bar_horizontal = findViewById(R.id.progress_bar_horizontal);
        progress_bar_horizontal.setVisibility(View.GONE);
        iv_end_game_sucess_message_gif = findViewById(R.id.iv_end_game_sucess_message_gif);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dialog_fr_timer = new Dialog(Game_Act.this);
        dialog_fr_timer.setContentView(R.layout.please_wait_lay);
        Objects.requireNonNull(dialog_fr_timer.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAdView = findViewById(R.id.adView);
        mAdView_end_game = findViewById(R.id.adView_end_game);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView_end_game.loadAd(adRequest);

        rv_single_card_end_game_one = findViewById(R.id.rv_single_card_end_game);

        str_auth_token = SessionSave.getSession("Token_value", Game_Act.this);
        Log.e("authtoken_game", str_auth_token);

        rv_single_card_end_game_one.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_single_card_end_game_one.setHasFixedSize(true);
        rv_single_card_end_game_one.setVisibility(View.VISIBLE);

        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv_A_alphabet.setOnClickListener(this);
        tv_B_alphabet.setOnClickListener(this);
        tv_C_alphabet.setOnClickListener(this);
        tv_D_alphabet.setOnClickListener(this);
        tv_2x_txt.setOnClickListener(this);
        tv_close_end_game.setOnClickListener(this);
        /*1st set of images*/
        imageIntegerArrayList.add(R.drawable.play_img_01);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.drawable.play_img_01);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.drawable.play_img_01);
        /*2nd set of images*/
        imageIntegerArrayList.add(R.drawable.play_img_01);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.drawable.play_img_01);
        imageIntegerArrayList.add(R.drawable.play_img_02);
        imageIntegerArrayList.add(R.drawable.play_img_01);

        String select = "select EMAIL,PHONENO,BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
                str_phone_no = cursor.getString(1);
                str_balance = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("db_blnce", str_balance);
        bundle = getIntent().getExtras();
        if (bundle == null) {
            str_2x_powerup = null;
        } else {
            str_2x_powerup = bundle.getString("str_2x_powerup");
            str_correct_ans = bundle.getString("str_correct_ans");
            str_wrong_ans = bundle.getString("str_wrong_ans");
            str_skip = bundle.getString("str_skip");
            str_entry_fees = bundle.getString("str_entry_fees");
        }
//        Log.e("str_correct_ans", str_correct_ans);
//        Log.e("str_wrong_ans", str_wrong_ans);
//        Log.e("str_skip", str_skip);
//        Log.e("str_entry_fees", str_entry_fees);
        tv_correct_ans_values.setText("+ " + str_correct_ans + " Points");
        tv_wrong_ans_values.setText("- " + str_wrong_ans + " Points");
        tv_skip_points_values.setText(str_skip + " Points");


        try {
            int_entry_fee = Integer.parseInt(str_entry_fees);
            int_correct_ans = Integer.parseInt(str_correct_ans);
            int_wrong_ans = Integer.parseInt(str_wrong_ans);
            int_skip = Integer.parseInt(str_skip);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }


        //This is used for Full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FullScreenMethod();



       /* String select = "select EMAIL from LOGINDETAILS";
        Cursor cursor = Splash_Screen_Act.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("str_email111", str_email);*/
      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                constraintLayout_just_a_moment.setVisibility(View.VISIBLE);
                constraintLayout_game.setVisibility(View.GONE);
                constraintLayout_end_game.setVisibility(View.GONE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    constraintLayout_game.setVisibility(View.VISIBLE);
                    constraintLayout_just_a_moment.setVisibility(View.GONE);
                    constraintLayout_end_game.setVisibility(View.GONE);
                    startTimer();
                }
            }
        }, 5000);*/

       /* // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout_tv_just_a_moment_contest_one_txt.getBackground();
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);
        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();*/


        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        tv_just_a_moment_contest_one.startAnimation(zoomOutAnimation);
        ProgressBar_Init_Method();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            str_count_down_seconds = null;
        } else {
            str_count_down_seconds = bundle.getString("str_count_down_seconds");
            str_2x_powerup = bundle.getString("str_2x_powerup");

            str_correct_ans = bundle.getString("str_correct_ans");
            str_wrong_ans = bundle.getString("str_wrong_ans");
            str_skip = bundle.getString("str_skip");
        }
        int_2x_onclick_count = Integer.parseInt(str_2x_powerup);
        Log.e("str_2x_powerup", "" + int_2x_onclick_count);
        tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
        Log.e("str_count_down_seconds", str_count_down_seconds);
        tv_time_limit_sec_txt.setText(str_count_down_seconds + "S");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                constraintLayout_game.setVisibility(View.VISIBLE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    int_db_balance = Integer.parseInt(str_balance);

                    if (!(str_entry_fees.equalsIgnoreCase("Free"))) {
                        int_entry_fee = Integer.parseInt(str_entry_fees);
                        int int_minus_value = 50;
                        Log.e("int_db_balance", "" + int_db_balance);
                        Log.e("int_entry_fee", "" + int_entry_fee);
                        int int_calc = int_db_balance - int_entry_fee;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("BALANCE", int_calc);
                        Log.e("Content_Values_mob_reg", contentValues.toString());
                        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                    }

                    assert str_count_down_seconds != null;
//                    int time = Integer.parseInt(str_count_down_seconds);
                    int time = 12;
                    long s1 = time * 1000 + 1;
                    startTimer(s1);

                    if (int_reaming_page_count_value == 1) {
                        tv_remaining_count_value.setText(String.valueOf(int_reaming_page_count_value));
                        iv_changing_image.setImageResource(imageIntegerArrayList.get(0));
                    }
                }
            }
        }, 3800);
        constraintLayout_end_game.setVisibility(View.GONE);

        //Cast long value to int value
        //When defining above variables, make sure 'progressBarMaximumValue' always rerun integer value
        progressBarMaximumValue = (int) (millisInFuture / countDownInterval);
        progress_bar_horizontal.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        //Set ProgressBar maximum value
        //ProgressBar range (0 to maximum value)
        progress_bar_horizontal.setMax(progressBarMaximumValue);
    }

    private void ProgressBar_Init_Method() {
        progressStatus = 0;
        progressStatus += 1;
        //Cast long value to int value
        //When defining above variables, make sure 'progressBarMaximumValue' always rerun integer value
        progressBarMaximumValue = (int) (millisInFuture / countDownInterval);
        progress_bar_horizontal.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        //Set ProgressBar maximum value
        //ProgressBar range (0 to maximum value)
        progress_bar_horizontal.setMax(progressBarMaximumValue);
    }

    private void startTimer(long countDownIntervalSeconds) {
        long milli = countDownIntervalSeconds + 1000;
        countDownTimer = new CountDownTimer(milli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
                Log.e("Seconds", "" + seconds);
                String s1 = String.format("%2d", seconds).trim();
                if (s1.length() == 2) {
                    tv_timer_seconds_count_game_one.setText(s1);
                } else {
                    tv_timer_seconds_count_game_one.setText("0" + s1);
                }

                progressStatus += 1;
                progress_bar_horizontal.setProgress(progressStatus);
//                Log.e("seconds_value", String.valueOf(seconds));
//                Log.e("progressBarMaximumValue", "" + progressBarMaximumValue);

                if (seconds == 9) {
                    constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                    tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                } else if (seconds == 3) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                    progress_bar_horizontal.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                    constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_red_alert_bg));
                } else if (seconds == 2) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                } else if (seconds == 1) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                } else if (seconds == 0) {
                    tv_timer_seconds_count_game_one.setText("00");
                }
            }

            @Override
            public void onFinish() {
                tv_timer_seconds_count_game_one.setText("00");
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);

                if (int_2_x_count == 1) {
                    tv_2x_txt.setVisibility(View.GONE);
                } else {
                    tv_2x_txt.setVisibility(View.VISIBLE);
                }
                tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));

                tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));

                tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                do {
                    if (int_count_value < imageIntegerArrayList.size()) {
//                        int_count_value++;
//                        int_reaming_page_count_value++;
                        int n1 = Integer.parseInt(tv_remaining_count_value.getText().toString());
                        int_reaming_page_count_value = n1 + 1;
                        int_count_value = n1;
//                        Log.e("onfinish_count", "" + int_reaming_page_count_value);
                        try {
                            if (int_reaming_page_count_value <= 10) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog_fr_timer.dismiss();
                                        progressStatus = 0;
                                        progressStatus += 1;
                                        progress_bar_horizontal.setProgress(progressStatus);
                                        progress_bar_horizontal.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                                        ProgressBar_Init_Method();

                                        tv_remaining_count_value.setText(String.valueOf(int_reaming_page_count_value));
                                        iv_changing_image.setImageResource(imageIntegerArrayList.get(int_count_value));
                                        tv_timer_seconds_count_game_one.setText("00");

                                        assert str_count_down_seconds != null;
//                                        int time = Integer.parseInt(str_count_down_seconds);
                                        int time = 12;
                                        long s1 = time * 1000 + 1;
                                        startTimer(s1);
                                        ProgressBar_Init_Method();

                                        if (int_onclcik_2x_power_up != 0) {
                                            if (int_2x_onclick_count != 0) {
                                                tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_2x_txt.setEnabled(true);
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                            } else if (int_2x_onclick_count == 0) {
                                                if (int_onclcik_2x_power_up != 0) {
                                                    int_2x_onclick_count = int_2x_onclick_count + 1;
                                                    int_onclcik_2x_power_up = 1;
                                                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                                                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                    tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                    tv_2x_txt.setEnabled(true);
                                                } else {
                                                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                                                    tv_2x_txt.setEnabled(false);
                                                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                                                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                                                }
                                            }
                                        }
                                        int_onclcik_2x_power_up = 0;
                                    }
                                }, 1000);
                            } else {
                                dialog_fr_timer.dismiss();
//                                dialog_fr_timer.dismiss();
//                                Toast.makeText(Game_Act.this, "els", Toast.LENGTH_SHORT).show();
                                constraintLayout_game.setVisibility(View.GONE);
//                                constraintLayout32.setVisibility(View.GONE);
                                constraintLayout_just_a_moment.setVisibility(View.GONE);
                                Glide.with(Game_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
                                constraintLayout_end_game.setVisibility(View.VISIBLE);
                                Get_End_Game_Contest_Details();
                                constraintLayout_end_game.setBackgroundResource((R.color.white_color));
                               /* Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                    }
                                }, 2000);
*/
                            }

                        } catch (IndexOutOfBoundsException ari) {
                            ari.printStackTrace();
                        }
                    }
                } while (int_count_value > imageIntegerArrayList.size());


            }
        }.start();
        performTick(10000);
    }

    private void Get_End_Game_Contest_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory_For_Categories.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        try {
//                        Toast.makeText(Game_Act.this, "Else_Response", Toast.LENGTH_SHORT).show();
                            if (response.body().data == null) {
//                            Toast.makeText(Game_Act.this, "Null", Toast.LENGTH_SHORT).show();
                                rv_single_card_end_game_one.setVisibility(View.VISIBLE);
                            } else {
                                rv_single_card_end_game_one.setVisibility(View.VISIBLE);
                            }
                            str_code = response.body().code;
                            str_message = response.body().message;
//                        Log.e("str_code-->", str_code);
//                        Log.e("str_message-->", str_message);
//                        Log.e("str_datum_value-->", String.valueOf(response.body()));
                            rv_single_card_end_game_one.setVisibility(View.VISIBLE);
                            contestAdapter_for_end_game = new ContestAdapter_For_End_Game(Game_Act.this, response.body().data);
                            rv_single_card_end_game_one.setAdapter(contestAdapter_for_end_game);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performTick(int millisUntilFinished) {
        tv_timer_seconds_count_game_one.setText(String.valueOf(Math.round(millisUntilFinished * 0.001f)));
//        Log.e("performclick", "" + Math.round(millisUntilFinished * 0.001f));
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

    private void DBEXPORT() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.spot_the_ballgame" + "/databases/" + "Spottheball.db";
        String backupDBPath = "Spottheball_Demo.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (countDownTimer != null) {
//            Toast.makeText(this, "nnn", Toast.LENGTH_SHORT).show();
            dialog = new Dialog(Game_Act.this);
            dialog.setContentView(R.layout.logout_alert);
            TextView tv_cancel, tv_yes;
            tv_yes = dialog.findViewById(R.id.tv_yes);
            tv_cancel = dialog.findViewById(R.id.tv_cancel);
            dialog.setCancelable(false);
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
//                    System.exit(0);
//                    finish();
                    dialog.dismiss();
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } else {
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close_end_game:
                Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                startActivity(intent);
                break;
            case R.id.tv_A_alphabet:
                if (int_2x_onclick_count == 0) {
                    tv_2x_txt.setEnabled(false);
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                    tv_2x_txt.setEnabled(true);
                }

                countDownTimer.cancel();
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                tv_A_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                if (int_2_x_count == 1) {
                    tv_2x_txt.setVisibility(View.GONE);
                } else {
                    tv_2x_txt.setVisibility(View.VISIBLE);
                }
                Handler handler_01 = new Handler();
                handler_01.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        str_alphabet_input_value = "A";
                        tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        String s1 = tv_remaining_count_value.getText().toString();
                        int n1_1 = Integer.parseInt(s1);
                        Log.e("n1_1_value", "" + n1_1);
                        if (n1_1 <= 9) {
                            if (countDownTimer == null) {
                                countDownTimer.start();
                            }
                            dialog_fr_timer.dismiss();
                            int n2 = n1_1 + 1;
                            tv_remaining_count_value.setText(String.valueOf(n2));
                            Log.e("tv_remaingcnt_vlue_AA", tv_remaining_count_value.getText().toString());
                            iv_changing_image.setImageResource(imageIntegerArrayList.get(n1_1));
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                constraintLayout_count_down_timer_game_one.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.stop_animation_shake));
                                constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                                int time = 12;
                                long s11 = time * 1000 + 1;
                                startTimer(s11);
//                                ProgressBar_Init_Method();
                            }
                        } else if (n1_1 == 10) {
                            dialog_fr_timer.dismiss();
                            constraintLayout_game.setVisibility(View.GONE);
//                            constraintLayout32.setVisibility(View.GONE);
                            constraintLayout_just_a_moment.setVisibility(View.GONE);
                            Glide.with(Game_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
                            constraintLayout_end_game.setVisibility(View.VISIBLE);
                            Get_End_Game_Contest_Details();
                            constraintLayout_end_game.setBackgroundResource((R.color.white_color));
                           /* Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                }
                            }, 2000);*/
                        }
//                Alphabet_Input_Method();
                    }
                }, 1000);

                break;
            case R.id.tv_B_alphabet:
                if (int_2x_onclick_count == 0) {
                    tv_2x_txt.setEnabled(false);
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                    tv_2x_txt.setEnabled(true);
                }
                countDownTimer.cancel();
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                tv_B_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                if (int_2_x_count == 1) {
                    tv_2x_txt.setVisibility(View.GONE);
                } else {
                    tv_2x_txt.setVisibility(View.VISIBLE);
                }
                Handler handler_B = new Handler();
                handler_B.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*if (countDownTimer != null) {
                            countDownTimer.cancel();
//                            startTimer();
                            assert str_count_down_seconds != null;
//                            int time = Integer.parseInt(str_count_down_seconds);
                            int time = 12;
                            long s1 = time * 1000 + 1;
                            startTimer(s1);

                            ProgressBar_Init_Method();
                        }*/
                        str_alphabet_input_value = "B";
                        tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        str_remaining_count_value = tv_remaining_count_value.getText().toString();

                        String s2 = tv_remaining_count_value.getText().toString();
                        int n1_2 = Integer.parseInt(s2);
                        Log.e("n1_2_value", "" + n1_2);
                        if (n1_2 <= 9) {
                            if (countDownTimer == null) {
                                countDownTimer.start();
                            }
                            dialog_fr_timer.dismiss();
                            int n2 = n1_2 + 1;
                            tv_remaining_count_value.setText(String.valueOf(n2));
                            Log.e("tv_remaingcnt_vlue_BB", tv_remaining_count_value.getText().toString());
                            iv_changing_image.setImageResource(imageIntegerArrayList.get(n1_2));
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                constraintLayout_count_down_timer_game_one.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.stop_animation_shake));
                                constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                                int time = 12;
                                long s11 = time * 1000 + 1;
                                startTimer(s11);
//                                ProgressBar_Init_Method();
                            }
                        } else if (n1_2 == 10) {
                            dialog_fr_timer.dismiss();
                            constraintLayout_game.setVisibility(View.GONE);
//                            constraintLayout32.setVisibility(View.GONE);
                            constraintLayout_just_a_moment.setVisibility(View.GONE);
                            Glide.with(Game_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
                            constraintLayout_end_game.setVisibility(View.VISIBLE);
                            constraintLayout_end_game.setBackgroundResource((R.color.white_color));
                            Get_End_Game_Contest_Details();
                           /* Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                }
                            }, 2000);*/
                        }
//                Alphabet_Input_Method();
                    }
                }, 1000);
                break;
            case R.id.tv_C_alphabet:
                if (int_2x_onclick_count == 0) {
                    tv_2x_txt.setEnabled(false);
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                    tv_2x_txt.setEnabled(true);
                }
                countDownTimer.cancel();
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                tv_C_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                if (int_2_x_count == 1) {
                    tv_2x_txt.setVisibility(View.GONE);
                } else {
                    tv_2x_txt.setVisibility(View.VISIBLE);
                }
                Handler handler_C = new Handler();
                handler_C.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*if (countDownTimer != null) {
                            countDownTimer.cancel();
//                            startTimer();
                            assert str_count_down_seconds != null;
//                            int time = Integer.parseInt(str_count_down_seconds);
                            int time = 12;
                            long s1 = time * 1000 + 1;
                            startTimer(s1);


                            ProgressBar_Init_Method();
                        }*/
                        str_alphabet_input_value = "C";
                        tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        str_remaining_count_value = tv_remaining_count_value.getText().toString();
                        String s3 = tv_remaining_count_value.getText().toString();
                        int n1_3 = Integer.parseInt(s3);
                        Log.e("n1_3_value", "" + n1_3);
                        if (n1_3 <= 9) {
                            if (countDownTimer == null) {
                                countDownTimer.start();
                            }
                            dialog_fr_timer.dismiss();
                            int n2 = n1_3 + 1;
                            tv_remaining_count_value.setText(String.valueOf(n2));
                            Log.e("tv_remaingcnt_vlue_CC", tv_remaining_count_value.getText().toString());
                            iv_changing_image.setImageResource(imageIntegerArrayList.get(n1_3));

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                constraintLayout_count_down_timer_game_one.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.stop_animation_shake));
                                constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                                int time = 12;
                                long s11 = time * 1000 + 1;
                                startTimer(s11);
//                                ProgressBar_Init_Method();
                            }
                        } else if (n1_3 == 10) {
                            dialog_fr_timer.dismiss();
                            constraintLayout_game.setVisibility(View.GONE);
//                            constraintLayout32.setVisibility(View.GONE);
                            constraintLayout_just_a_moment.setVisibility(View.GONE);
                            Glide.with(Game_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
                            constraintLayout_end_game.setVisibility(View.VISIBLE);
                            constraintLayout_end_game.setBackgroundResource((R.color.white_color));
                            Get_End_Game_Contest_Details();
                            /*Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                }
                            }, 2000);*/
                        }
//                Alphabet_Input_Method();
                    }
                }, 1000);
                break;
            case R.id.tv_D_alphabet:
                if (int_2x_onclick_count == 0) {
                    tv_2x_txt.setEnabled(false);
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                    tv_2x_txt.setEnabled(true);
                }

                countDownTimer.cancel();
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                tv_D_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                if (int_2_x_count == 1) {
                    tv_2x_txt.setVisibility(View.GONE);
                } else {
                    tv_2x_txt.setVisibility(View.VISIBLE);
                }
                Handler handler_D = new Handler();
                handler_D.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*if (countDownTimer != null) {
                            countDownTimer.cancel();
//                            startTimer();
                            assert str_count_down_seconds != null;
//                            int time = Integer.parseInt(str_count_down_seconds);
                            int time = 12;
                            long s1 = time * 1000 + 1;
                            startTimer(s1);

                            ProgressBar_Init_Method();
                        }*/
                        str_alphabet_input_value = "D";
                        tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        str_remaining_count_value = tv_remaining_count_value.getText().toString();
                        String s4 = tv_remaining_count_value.getText().toString();
                        int n1_4 = Integer.parseInt(s4);
                        Log.e("n1_4_value", "" + n1_4);
                        if (n1_4 <= 9) {
                            if (countDownTimer == null) {
                                countDownTimer.start();
                            }
                            dialog_fr_timer.dismiss();
                            int n2 = n1_4 + 1;
                            tv_remaining_count_value.setText(String.valueOf(n2));
                            Log.e("tv_remaingcnt_vlue_DD", tv_remaining_count_value.getText().toString());
                            iv_changing_image.setImageResource(imageIntegerArrayList.get(n1_4));

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                constraintLayout_count_down_timer_game_one.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.stop_animation_shake));
                                constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                                int time = 12;
                                long s11 = time * 1000 + 1;
                                startTimer(s11);
//                                ProgressBar_Init_Method();
                            }

                        } else if (n1_4 == 10) {
                            dialog_fr_timer.dismiss();
                            constraintLayout_game.setVisibility(View.GONE);
//                            constraintLayout32.setVisibility(View.GONE);
                            constraintLayout_just_a_moment.setVisibility(View.GONE);
                            Glide.with(Game_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
                            constraintLayout_end_game.setVisibility(View.VISIBLE);
                            constraintLayout_end_game.setBackgroundResource((R.color.white_color));
                            Get_End_Game_Contest_Details();
                           /* Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                }
                            }, 2000);*/
                        }
//                Alphabet_Input_Method();
                    }
                }, 1000);

                break;
            case R.id.tv_2x_txt:
                n2_new_value = Integer.parseInt(tv_remaining_count_value.getText().toString());
                n1_old_value = n2_new_value;
                int_onclcik_2x_power_up = 1;
                Log.e("2x_count", "" + int_2x_onclick_count);
//                Toast.makeText(Game_Act.this, "2x_power_up" + int_onclcik_2x_power_up, Toast.LENGTH_SHORT).show();
                if (int_2x_onclick_count == 0) {
                    tv_2x_txt.setEnabled(false);
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                } else {
                    int_2x_onclick_count = int_2x_onclick_count - 1;
                    int_2_x_count = 0;
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                    tv_2x_txt.setEnabled(false);

                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                }
                break;
        }
    }

    private void Alphabet_Input_Method_For_2X(int int_2_x_count, String
            str_remaining_count_value) {
        Toast_Message.showToastMessage(Game_Act.this, "2_X_Value :" + int_2_x_count + "  Remaining count :" + str_remaining_count_value);
    }

    private void Alphabet_Input_Method() {
        str_remaining_count_value = tv_remaining_count_value.getText().toString();
        str_user_selection_value = str_remaining_count_value + str_alphabet_input_value;
        str_db_concat = str_user_selection_value;
        Log.e("str_db_concat1111", str_db_concat);
        ArrayList<String> arrayList = new ArrayList<>();
        String select = "select USER_SELECTION_VALUE from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_db_values = cursor.getString(0);
                Log.e("str_db_values", str_db_values);
                if (!str_db_values.isEmpty()) {
                    arrayList.add(str_db_values);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("arrayList", arrayList.toString());
        if (arrayList.size() != 0) {
            for (int i = 0; i <= arrayList.size() - 1; i++) {
              /*  if (i == 0) {
                    str_db_concat = arrayList.get(i);
                } else {
              */
                str_db_concat = arrayList.get(i) + "," + str_db_concat;
                // }
            }
            Log.e("str_db_concat_after", str_db_concat);
        }
       /* else {
            Toast.makeText(this, "hhhh", Toast.LENGTH_SHORT).show();
        }*/
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_SELECTION_VALUE", str_db_concat);
        Log.e("USER_SELECTIONcontent", contentValues.toString());
        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
        DBEXPORT();
    }


    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
    }

    /*This method automatically detect whether the internet is available or not
     * if internet in not available GetDrawTiming,GetBalanceDetails will get stop
     * */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void setSnackbarMessage(String status) {
        String internetStatus;
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = getResources().getString(R.string.back_online_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.black_color);
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar
                    .make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundResource(R.color.black_color);
        }

        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        if (internetStatus.equalsIgnoreCase(getResources().getString(R.string.check_internet_conn_txt))) {
            if (internetConnected) {
                snackbar.show();
                internetConnected = false;
            }
        } else {
            if (!internetConnected) {
                internetConnected = true;
                snackbar.show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
//            Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        }
    }
}

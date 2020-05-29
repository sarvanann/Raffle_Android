package com.spot_the_ballgame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adcolony.sdk.AdColony;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;
import com.spot_the_ballgame.Adapter.Price_List_Adapter;
import com.spot_the_ballgame.Adapter.Ranking_List_Adapter;
import com.spot_the_ballgame.Adapter.Rules_List_Name_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Model.Winnings_Model;
import com.tomer.fadingtextview.FadingTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_Details_Screen_Act extends AppCompatActivity implements View.OnClickListener {
    private static final int SEND_STORAGE_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "TEST";

    SQLiteDatabase db;
    ConstraintLayout constraintLayout_just_a_moment_contest_one,
            constraintLayout_end_game_game_two,
            constraintLayout_game_details_screen_in_game_two,
            constraintLayout_watch_ads_btn_inside,
            constraintLayout_watch_ads_btn,
            constraintLayout_rewarded_video,
            constraintLayout_score_board_top_layout;

    TextView tv_enter_contest_btn,
            tv_score_board,
            tv_winnings_btn,
            tv_rules_btn,
            tv_enter_contest_btn_for_free,
            tv_entry_fee_details,
            tv_earn_coins,
            tv_loading_tx,
            tv_play_btn,
            tv_coins,
            tv_prize_pool_details_icon;
    ImageView iv_categoires_image_game_details_act,
            gif_image,
            iv_ready_steady_go_state;
    TextView tv_game_end_time_game_details,
            tv_prize_amount_game_details,
            tv_game_name, tv_entry_fee_details_coins;

    RecyclerView rv_price_list;
    Snackbar snackbar;

    String str_onclick_contest_value = "";
    String str_playby,
            str_game_name,
            str_prize_amount,
            str_count_down_seconds,
            str_correct_ans,
            str_2x_powerup,
            str_contest_id,
            str_entry_fees,
            str_wrong_ans,
            str_imagepath,
            str_phone_no,
            str_message,
            str_wallet1,
            str_wallet2,
            str_email,
            str_skip,
            str_code;

    int int_db_balance,
            int_entry_fee,
            int_correct_ans,
            int_wrong_ans,
            int_skip,
            int_db_balance_for_reward_video,
            int_rewarded_coins_point,
            int_total_value;
    double double_db_balance;
    Price_List_Adapter price_list_adapter;

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    private ShimmerLayout shimmer_view_container_winning_list_game_two,
            shimmer_view_container_for_rules,
            shimmer_view_container_for_scoreboard;

    /*This components are used for Rewarded video*/
    private static final String TIME_REMAINING_KEY = "TIME_REMAINING";
    private static final String COIN_COUNT_KEY = "COIN_COUNT";
    private static final String GAME_PAUSE_KEY = "IS_GAME_PAUSED";
    private static final String GAME_OVER_KEY = "IS_GAME_OVER";
    private int mCoinCount;
    private TextView mCoinCountText;
    private boolean mGameOver;
    private boolean mGamePaused;
    private RewardedVideoAd mRewardedVideoAd;
    private long mTimeRemaining;
    ProgressBar progress_bar_in_reward_video;
    Bundle savedInstanceState;

    Handler handler;

    RecyclerView rv_rules_list_game_two;
    RecyclerView rv_score_board_list;
    Rules_List_Name_Adapter rules_list_name_adapter;
    String str_rule_id;
    String str_auth_token;
    String str_session_reward_amount;
    FirebaseAnalytics mFirebaseAnalytics;
    com.facebook.ads.InterstitialAd interstitialAd_fb;
    private AdView mAdView, mAdView_end_game;
    private InterstitialAd interstitialAd;

    /*This unit id using google test mail id*/
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    int int_play_status = 0;
    FadingTextView tv_just_a_moment;

    long different_milli_seconds,
            lng_seconds,
            milliseconds,
            seconds;
    Date current_system_time, end_date_api;
    String str_status_onclick,
            str_end_time,
            str_categories,
            str_question_type,
            str_prize_type,
            str_connect_host_image,
            str_local_host, str_nav_curnt_amnt;
    CountDownTimer countDownTimer_game_details;
    TextView tv_watch_ads_btn, tv_skip_points_values,
            tv_wrong_ans_values,
            tv_correct_ans_values,
            tv_current_user_rank;
    int int_2x_onclick_count = 0;
    Bundle bundle;
    ArrayList<Winnings_Model> winning_arrayList;
    Ranking_List_Adapter ranking_list_adapter;
    int int_tv_points;
    TextView tv_no_data_available_fr_score_board;
    ArrayList<String> image_ArrayList_GD_Act = new ArrayList<>();
    ArrayList<String> imagequestionsIntegerArrayList = new ArrayList<>();
    Set<String> linkedHashSet;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(Game_Details_Screen_Act.this).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        str_auth_token = SessionSave.getSession("Token_value", Game_Details_Screen_Act.this);
//        Log.e("authtoken_gamedetail", str_auth_token);

        String select = "select EMAIL,PHONENO from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
                str_phone_no = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();

        Get_Wallet_Balance_Details_Without_Ad();


        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_just_a_moment_contest_one = findViewById(R.id.constraintLayout_just_a_moment_contest_one);
        constraintLayout_end_game_game_two = findViewById(R.id.constraintLayout_end_game_game_two);
        constraintLayout_just_a_moment_contest_one = findViewById(R.id.constraintLayout_just_a_moment_contest_one);
        constraintLayout_watch_ads_btn_inside = findViewById(R.id.constraintLayout_watch_ads_btn_inside);
        constraintLayout_rewarded_video = findViewById(R.id.constraintLayout_rewarded_video);
        constraintLayout_watch_ads_btn = findViewById(R.id.constraintLayout_watch_ads_btn);
        constraintLayout_score_board_top_layout = findViewById(R.id.constraintLayout_score_board_top_layout);

        tv_just_a_moment = findViewById(R.id.tv_just_a_moment_contest_one);
        tv_game_end_time_game_details = findViewById(R.id.tv_game_end_time_game_details);
        tv_enter_contest_btn = findViewById(R.id.tv_enter_contest_btn);
        tv_prize_amount_game_details = findViewById(R.id.tv_prize_amount_game_details);
        tv_score_board = findViewById(R.id.tv_score_board);
        tv_winnings_btn = findViewById(R.id.tv_winnings_btn);
        tv_rules_btn = findViewById(R.id.tv_rules_btn);
        shimmer_view_container_for_rules = findViewById(R.id.shimmer_view_container_for_rules);
        shimmer_view_container_winning_list_game_two = findViewById(R.id.shimmer_view_container_winning_list_game_two);
        shimmer_view_container_for_scoreboard = findViewById(R.id.shimmer_view_container_for_scoreboard);

        tv_enter_contest_btn_for_free = findViewById(R.id.tv_enter_contest_btn_for_free);
        tv_entry_fee_details = findViewById(R.id.tv_entry_fee_details);
        rv_price_list = findViewById(R.id.rv_price_list);
        tv_game_name = findViewById(R.id.tv_game_name);
        tv_entry_fee_details_coins = findViewById(R.id.tv_entry_fee_details_icon);
        tv_earn_coins = findViewById(R.id.tv_earn_coins);
        tv_play_btn = findViewById(R.id.tv_play_btn);
        tv_coins = findViewById(R.id.tv_coins);
        tv_prize_pool_details_icon = findViewById(R.id.tv_prize_pool_details_icon);
        tv_no_data_available_fr_score_board = findViewById(R.id.tv_no_data_available_fr_score_board);

        iv_ready_steady_go_state = findViewById(R.id.iv_ready_steady_go_state);
        tv_watch_ads_btn = findViewById(R.id.tv_watch_ads_btn);
        rv_rules_list_game_two = findViewById(R.id.rv_rules_list_game_two);
        rv_rules_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        rv_score_board_list = findViewById(R.id.rv_score_board_list_game_two);
        rv_score_board_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        tv_skip_points_values = findViewById(R.id.tv_skip_points_values);
        tv_wrong_ans_values = findViewById(R.id.tv_wrong_ans_values);
        tv_correct_ans_values = findViewById(R.id.tv_correct_ans_values);
        tv_current_user_rank = findViewById(R.id.tv_current_user_rank);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mCoinCountText = findViewById(R.id.coin_count_text);

        /*This components are used for Rewarded video*/
        tv_loading_tx = findViewById(R.id.tv_loading_txt);
        gif_image = findViewById(R.id.gif_image);
        iv_categoires_image_game_details_act = findViewById(R.id.iv_categoires_image_game_details_act);
        Glide.with(this).asGif().load(R.drawable.giphy).into(gif_image);
        progress_bar_in_reward_video = findViewById(R.id.progress_bar_in_reward_video);


        //Ironsource_code
        IronSource.setConsent(true);
        //This is for ad colony
        AdColony.configure(this, "appd9b3bb873a744248bd", "vz09df69e0202642b88a");
        str_session_reward_amount = SessionSave.getSession("Reward_Point", Game_Details_Screen_Act.this);


        rv_price_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_price_list.setVisibility(View.VISIBLE);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle == null) {
//            str_onclick_contest_value = null;
//        } else {
//            str_onclick_contest_value = bundle.getString("onclick_contest_value");
//        }
//        Log.e("str_onclick_contest", str_onclick_contest_value);
//        if (str_onclick_contest_value == null) {
//            tv_enter_contest_btn.setVisibility(View.VISIBLE);
//            tv_enter_contest_btn_for_free.setVisibility(View.VISIBLE);
//        } else {
//            tv_enter_contest_btn.setVisibility(View.GONE);
//            tv_enter_contest_btn_for_free.setVisibility(View.GONE);
//        }
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        bundle = getIntent().getExtras();
        if (bundle == null) {
            str_game_name = null;
        } else {
            str_game_name = bundle.getString("game_name");
            str_2x_powerup = bundle.getString("str_2x_powerup");
            str_count_down_seconds = bundle.getString("count_down_seconds");
            str_contest_id = bundle.getString("str_contest_id");
            str_entry_fees = bundle.getString("str_entry_fees");
            str_prize_amount = bundle.getString("prize_amount");
            str_end_time = bundle.getString("end_time");
            str_rule_id = bundle.getString("str_rule_id");
            str_status_onclick = bundle.getString("str_status_onclick");
            str_imagepath = bundle.getString("str_imagepath");

            str_correct_ans = bundle.getString("str_correct_ans");
            str_wrong_ans = bundle.getString("str_wrong_ans");
            str_skip = bundle.getString("str_skip");
            str_categories = bundle.getString("str_categories");
            str_question_type = bundle.getString("str_question_type");
            str_prize_type = bundle.getString("prize_type");

            tv_game_name.setText(str_game_name);
            tv_prize_amount_game_details.setText(str_prize_amount);
            tv_entry_fee_details.setText(str_entry_fees);

//            Log.e("str_end_time", str_end_time);

        }

        if (str_prize_type.equalsIgnoreCase("Coin")) {
            tv_prize_pool_details_icon.setBackground(getResources().getDrawable(R.drawable.coin_new));
            tv_entry_fee_details_coins.setBackground(getResources().getDrawable(R.drawable.coin_new));
        } else if (str_prize_type.equalsIgnoreCase("Cash") || str_prize_type.equalsIgnoreCase("Fee")) {
            tv_prize_pool_details_icon.setBackground(getResources().getDrawable(R.drawable.rupee_indian));
            tv_entry_fee_details_coins.setBackground(getResources().getDrawable(R.drawable.rupee_indian));
        }
        if (str_entry_fees.equalsIgnoreCase("Free")) {
            tv_entry_fee_details_coins.setVisibility(View.GONE);
        }

        try {
            int_entry_fee = Integer.parseInt(str_entry_fees);
            int_correct_ans = Integer.parseInt(str_correct_ans);
            int_wrong_ans = Integer.parseInt(str_wrong_ans);
            int_skip = Integer.parseInt(str_skip);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        tv_correct_ans_values.setText("+ " + str_correct_ans + " Points");
//        tv_wrong_ans_values.setText("- " + str_wrong_ans + " Points");
        tv_wrong_ans_values.setText(str_wrong_ans + " Points");
        tv_skip_points_values.setText(str_skip + " Points");

        if (str_entry_fees.equalsIgnoreCase("Free")) {
            tv_enter_contest_btn_for_free.setVisibility(View.VISIBLE);
            tv_watch_ads_btn.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
        }

        if (str_status_onclick.equals("2")) {
            tv_enter_contest_btn_for_free.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
            tv_enter_contest_btn.setVisibility(View.GONE);
        }

        int_2x_onclick_count = Integer.parseInt(str_2x_powerup);
//        tv_2x_power_up.setText(str_2x_powerup);
//        Log.e("str_count_down_seconds", str_count_down_seconds);
//        tv_time_limit_sec_txt.setText(str_count_down_seconds + "s");

        tv_game_name.setText(str_game_name);
        tv_prize_amount_game_details.setText(str_prize_amount);
        tv_entry_fee_details.setText(String.valueOf(str_entry_fees));

//        Log.e("str_imagepath_intent", str_imagepath);

        str_connect_host_image = str_local_host + "" + str_imagepath;
        Glide.with(Game_Details_Screen_Act.this).load(str_connect_host_image)
                .thumbnail(Glide.with(Game_Details_Screen_Act.this).load(str_connect_host_image))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_categoires_image_game_details_act);


//        FullScreenMethod();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(Game_Details_Screen_Act.this);

        AudienceNetworkAds.initialize(this);
        interstitialAd_fb = new com.facebook.ads.InterstitialAd(this, "YOUR_PLACEMENT_ID");
//        Load_FB_ADS();
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        mAdView_end_game = findViewById(R.id.adView_end_game);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView_end_game.loadAd(adRequest);

        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(AD_UNIT_ID);
        showInterstitial();
        startGame();

        Get_Questions_Details();

        if (str_entry_fees.equalsIgnoreCase("Free")) {
            tv_enter_contest_btn_for_free.setText("Contest Loading");
        }

        if (str_entry_fees.equalsIgnoreCase("Free")) {
            tv_just_a_moment.stop();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    tv_enter_contest_btn_for_free.setText(R.string.play_for_free_txt);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onAdClosed() {
                    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
                    int_play_status = 1;
                    Getting_Update_Status_Details_Initial(int_play_status);
                    startGame();
                    mAdView.setVisibility(View.GONE);
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                    constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);
                    tv_just_a_moment.restart();
//                    Glide.with(Game_Details_Screen_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);


                    if (!isNetworkAvaliable()) {
                        registerInternetCheckReceiver();
                    } else {
                        //  startTimer(milliseconds);
//                        Toast.makeText(Game_Details_Screen_Act.this, "Adcolsed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Game_Details_Screen_Act.this, Game_Act.class);
                        intent.putExtra("str_count_down_seconds", str_count_down_seconds);
                        intent.putExtra("str_contest_id", str_contest_id);
                        intent.putExtra("str_2x_powerup", str_2x_powerup);
                        intent.putExtra("str_correct_ans", str_correct_ans);
                        intent.putExtra("str_wrong_ans", str_wrong_ans);
                        intent.putExtra("str_skip", str_skip);
                        intent.putExtra("str_entry_fees", str_entry_fees);


                        intent.putExtra("int_play_status", "1");
                        intent.putExtra("str_playby", "Coins");
                        intent.putExtra("str_mCoinCountText", mCoinCountText.getText().toString());

//                        Log.e("str_int_play_status_log_Ad_close", "" + int_play_status);
//                        Log.e("str_playby_log", "Coins_Ad_close");
//                        Log.e("str_mCoinCountText_log_Ad_close", str_entry_fees);


                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                        }
                    }, 3500);
                    constraintLayout_end_game_game_two.setVisibility(View.GONE);
                }
            });
        }

//        milliseconds = Long.parseLong(str_count_down_seconds) * 1000 + 1;


        final String str_t2_game_two = str_end_time;
//        Log.e("str_t2_game_two", str_t2_game_two);

        /*Getting Current Time*/
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strDate = mdformat.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            current_system_time = mdformat.parse(strDate);
            end_date_api = sdf.parse(str_t2_game_two);
//            Log.e("end_date_api_2", String.valueOf(end_date_api));
//            Log.e("parse_cur_system_time_2", String.valueOf(current_system_time));
            different_milli_seconds = end_date_api.getTime() - current_system_time.getTime();
//            Log.e("diff_milli_sec_2", String.valueOf(different_milli_seconds));
            if (different_milli_seconds < 0) {
                if (str_status_onclick.equalsIgnoreCase("2")) {
                    tv_game_end_time_game_details.setText("Played");
                } else {
                    tv_game_end_time_game_details.setText("Finished");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.e("str_status_onclick_2", str_status_onclick);

        if (str_status_onclick.equalsIgnoreCase("2")) {
            tv_game_end_time_game_details.setText("Played");
        } else {
            countDownTimer_game_details = new CountDownTimer(different_milli_seconds, 1000) {
                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public void onTick(long millisUntilFinished) {
                    lng_seconds = millisUntilFinished;
                    @SuppressLint("DefaultLocale") String s1 = String.format("%2d", lng_seconds).trim();
                    @SuppressLint("DefaultLocale") String ss = String.format("%02d:%02d:%02d", lng_seconds / 60, lng_seconds % 60, 0);

                    @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//                    System.out.println("HH_MM_SS:: " + hms);


                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = lng_seconds / daysInMilli;
                    lng_seconds = lng_seconds % daysInMilli;
                    if (elapsedDays == 1) {
                        tv_game_end_time_game_details.setText(elapsedDays + " day left");
                    } else if (elapsedDays > 1) {
                        tv_game_end_time_game_details.setText(elapsedDays + " days left");
                    } else {
                        tv_game_end_time_game_details.setText(hms);
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            shimmer_view_container_winning_list_game_two.setVisibility(View.VISIBLE);
            shimmer_view_container_winning_list_game_two.startShimmerAnimation();
            Get_Winning_List_Details();
        }


        tv_winnings_btn.setOnClickListener(this);
        tv_rules_btn.setOnClickListener(this);
        tv_score_board.setOnClickListener(this);
        tv_enter_contest_btn.setOnClickListener(this);
        tv_enter_contest_btn_for_free.setOnClickListener(this);
        constraintLayout_watch_ads_btn.setOnClickListener(this);
    }

    private void Get_Questions_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            Log.e("Json_value_question", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Details_Screen_Act.this);
                //This is for skyrand
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Factory.BASE_URL_MOBILE_APP + "get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_object = (JSONObject) jsonArray.get(i);
                                String image_photo = json_object.getString("image_photo");
                                String s11 = str_local_host + image_photo;
                                Log.e("s1111_json", s11);
                                imagequestionsIntegerArrayList.add(s11);
                            }
                            linkedHashSet = new LinkedHashSet<>(imagequestionsIntegerArrayList);

                            SessionSave.SaveSession("All_Image_File", String.valueOf(linkedHashSet), Game_Details_Screen_Act.this);
                            for (int j = 0; j < linkedHashSet.size(); j++) {
                                Log.e("resultIAV_get_of_i", imagequestionsIntegerArrayList.get(j));
                            }
                            String s1 = SessionSave.getSession("All_Image_File", Game_Details_Screen_Act.this);
                            Log.e("All_Image_File_session", s1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept", "application/json");
                        headers.put("Authorization", str_auth_token);
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Get_Wallet_Balance_Details_Without_Ad() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
//            Log.e("blnc_without_Ad", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_WALLET_BALALNCE_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_nav_curnt_amnt = response.body().current_amt;
//                            Log.e("str_amount_nav", "" + str_nav_curnt_amnt);
                            int_tv_points = Integer.parseInt(str_nav_curnt_amnt);

                            if (int_entry_fee > int_tv_points) {
//                                Log.e("topinside_entry_free", String.valueOf(int_entry_fee));
//                                Log.e("topinside_balance", String.valueOf(int_tv_points));
                                int n11 = int_entry_fee - int_tv_points;
//                                Log.e("n1111", String.valueOf(n11));
                                tv_enter_contest_btn.setVisibility(View.GONE);
                                if (str_status_onclick.equals("0")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
                                    tv_earn_coins.setVisibility(View.VISIBLE);
                                } else if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                    tv_earn_coins.setVisibility(View.GONE);
                                }
                                tv_play_btn.setVisibility(View.GONE);
                                tv_earn_coins.setText(String.valueOf(n11));
                                tv_coins.setVisibility(View.VISIBLE);
                            } else {
                                if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    tv_play_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                } else {
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    tv_play_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                }

                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            startGame();
        }
    }

    private void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }
    }

    private void Get_Winning_List_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
//            Log.e("volley_json", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Details_Screen_Act.this);
                //This is for SK
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.2.3/raffle/api/v1/get_prize_distribution", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                //This is for skyrand
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Factory.BASE_URL_MOBILE_APP + "get_prize_distribution", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            winning_arrayList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Winnings_Model winnings_model = new Winnings_Model();
                                    JSONObject json_object = (JSONObject) jsonArray.get(i);
//                                    winnings_model.setRank(json_object.getString("rank"));
                                    winnings_model.setRank_short(json_object.getString("rank_short"));
                                    winnings_model.setPrize_amount(json_object.getString("prize_amount"));
                                    winning_arrayList.add(winnings_model);
                                    tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                                    rv_rules_list_game_two.setVisibility(View.GONE);
                                    rv_score_board_list.setVisibility(View.GONE);
                                    constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                                    rv_price_list.setVisibility(View.VISIBLE);
                                    shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
//                                    Log.e("winning_arrayList", winning_arrayList.toString());

                                    price_list_adapter = new Price_List_Adapter(Game_Details_Screen_Act.this, winning_arrayList);
                                    rv_price_list.setAdapter(price_list_adapter);
                                    shimmer_view_container_winning_list_game_two.stopShimmerAnimation();
                                    shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);

                                }
                            } else {
                                rv_rules_list_game_two.setVisibility(View.GONE);
                                rv_score_board_list.setVisibility(View.GONE);
                                constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                                shimmer_view_container_winning_list_game_two.setVisibility(View.VISIBLE);
                                shimmer_view_container_winning_list_game_two.startShimmerAnimation();
                                rv_price_list.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept", "application/json");
                        headers.put("Authorization", str_auth_token);
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onBackPressed() {
    /*    Intent intent = new Intent(Game_Details_Screen_Act.this, Navigation_Drawer_Act.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();*/

        Intent intent = new Intent(Game_Details_Screen_Act.this, Navigation_Drawer_Act.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constraintLayout_watch_ads_btn:
//                Toast.makeText(this, "constraintLayout_watch_ads_btn", Toast.LENGTH_SHORT).show();

                //Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
                // production code
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
                    //I will skip it for this demo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            displayNeverAskAgainDialog();
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    SEND_STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    }
                } else {
                    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
                    int_play_status = 1;
                    Get_Enter_Game_Page();

                    if (int_entry_fee > int_tv_points) {
                        int n11 = int_entry_fee - int_tv_points;
                        tv_enter_contest_btn.setVisibility(View.GONE);

                        if (str_status_onclick.equals("0")) {
                            constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                            constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
                            tv_earn_coins.setVisibility(View.VISIBLE);
                        } else if (str_status_onclick.equals("2")) {
                            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                            tv_earn_coins.setVisibility(View.GONE);
                        }
                        tv_play_btn.setVisibility(View.GONE);
                        tv_earn_coins.setText(String.valueOf(n11));
                        tv_coins.setVisibility(View.VISIBLE);
                        constraintLayout_rewarded_video.setVisibility(View.VISIBLE);
                        Get_Rewarded_Video_Method(savedInstanceState);
                    } else {
                        if (str_status_onclick.equals("2")) {
                            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                            tv_play_btn.setVisibility(View.GONE);
                            constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                        } else {
                            constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                            tv_play_btn.setVisibility(View.VISIBLE);
                            constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                        }
//                    str_playby = "Coins";
//                    Getting_Update_Status_Details_Initial(int_play_status);
//                    Get_Points_Add_Delete_Details(str_playby);


//                    Get_User_Wallet_Details();
                    }
                }
                break;
            case R.id.tv_winnings_btn:
                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                shimmer_view_container_winning_list_game_two.setVisibility(View.VISIBLE);
                shimmer_view_container_winning_list_game_two.startShimmerAnimation();

                shimmer_view_container_for_rules.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);

                rv_score_board_list.setVisibility(View.GONE);
                rv_price_list.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                Get_Winning_List_Details();

                tv_winnings_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_winnings_btn.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_rules_btn:
                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                shimmer_view_container_for_rules.startShimmerAnimation();

                shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);

                rv_rules_list_game_two.setVisibility(View.GONE);
                rv_score_board_list.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                rv_price_list.setVisibility(View.GONE);
                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                Get_Rules_Name_Details();

                shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.white_color));

                tv_winnings_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_winnings_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_enter_contest_btn:
//                Toast.makeText(this, "tv_enter_contest_btn_onclick", Toast.LENGTH_SHORT).show();

                //Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
                // production code
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
                    //I will skip it for this demo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            displayNeverAskAgainDialog();
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    SEND_STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    }
                } else {
                    Intent intent = new Intent(Game_Details_Screen_Act.this, Game_Act.class);
                    intent.putExtra("str_count_down_seconds", str_count_down_seconds);
                    intent.putExtra("str_2x_powerup", str_2x_powerup);
                    intent.putExtra("str_correct_ans", str_correct_ans);
                    intent.putExtra("str_wrong_ans", str_wrong_ans);
                    intent.putExtra("str_skip", str_skip);
                    intent.putExtra("str_entry_fees", str_entry_fees);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
//                Game_Details_Screen_Act.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                break;
            case R.id.tv_enter_contest_btn_for_free:
//                Toast.makeText(this, "showInterstitial", Toast.LENGTH_SHORT).show();

                //Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
                // production code
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
                    //I will skip it for this demo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            displayNeverAskAgainDialog();
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    SEND_STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    }
                } else {
                    showInterstitial();
                }
               /* Intent intent_01 = new Intent(Game_Details_Screen_Act.this, Reward_Video_Act.class);
                intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_01);*/
                break;
            case R.id.tv_score_board:
                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                shimmer_view_container_for_scoreboard.startShimmerAnimation();

                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.GONE);

                rv_price_list.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                rv_score_board_list.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                Get_Ranking_List_Details();

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_winnings_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_winnings_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                break;
        }

    }

    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
    private void Getting_Update_Status_Details_Initial(int int_play_status) {
        /*play_status==>1 Live or Playing*/
        /*play_status==>2 Completed*/
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", int_play_status);
            jsonObject.put("email", str_email);
//            Log.e("update_response_init", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {

                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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

    private void Get_Ranking_List_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("email", str_email);

            APIInterface apiInterface = Factory.getClient();
//            Log.e("rank_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_RANKING_LIST_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = response.body().status;
                            String str_message = response.body().message;

                            if (str_status.equalsIgnoreCase("error")) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                                        tv_no_data_available_fr_score_board.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_score_board.setText(str_message);
                                        rv_score_board_list.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                shimmer_view_container_for_scoreboard.startShimmerAnimation();
                                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                            } else if (response.body().data.isEmpty()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                                        tv_no_data_available_fr_score_board.setVisibility(View.VISIBLE);
                                        rv_score_board_list.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.stopShimmerAnimation();

                                        rv_price_list.setVisibility(View.GONE);
                                        rv_rules_list_game_two.setVisibility(View.GONE);
                                    }
                                }, 2500);

                                rv_price_list.setVisibility(View.GONE);
                                rv_rules_list_game_two.setVisibility(View.GONE);
                                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                                rv_score_board_list.setVisibility(View.GONE);
                                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_scoreboard.startShimmerAnimation();
                            } else {
                                rv_price_list.setVisibility(View.GONE);
                                rv_rules_list_game_two.setVisibility(View.GONE);
                                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                                constraintLayout_score_board_top_layout.setVisibility(View.VISIBLE);
                                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                                String str_crnt_user_rank = response.body().your_rank;
                                tv_current_user_rank.setText("Your Rank : " + str_crnt_user_rank);
                                rv_score_board_list.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_scoreboard.stopShimmerAnimation();
                                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                assert response.body() != null;
                                ranking_list_adapter = new Ranking_List_Adapter(Game_Details_Screen_Act.this, Objects.requireNonNull(response.body()).data);
                                rv_score_board_list.setAdapter(ranking_list_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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

    private void Get_Rules_Name_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", str_rule_id);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_RULES_NAME_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().data.isEmpty()) {
                            shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                            shimmer_view_container_for_rules.startShimmerAnimation();

                            rv_price_list.setVisibility(View.GONE);
                            rv_rules_list_game_two.setVisibility(View.GONE);
                            rv_score_board_list.setVisibility(View.GONE);
                            constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                        } else {
                            rv_rules_list_game_two.setVisibility(View.GONE);
                            rv_score_board_list.setVisibility(View.GONE);
                            constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                            tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                            shimmer_view_container_for_rules.setVisibility(View.GONE);
                            shimmer_view_container_for_rules.stopShimmerAnimation();
                            rv_rules_list_game_two.setVisibility(View.VISIBLE);
                            tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                            rules_list_name_adapter = new Rules_List_Name_Adapter(getApplicationContext(), response.body().data);
                            rv_rules_list_game_two.setAdapter(rules_list_name_adapter);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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

    @SuppressLint("LongLogTag")
    private void Get_Enter_Game_Page() {
//        Toast.makeText(Game_Details_Screen_Act.this, "Get_Enter_Game_Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Game_Details_Screen_Act.this, Game_Act.class);
        intent.putExtra("str_count_down_seconds", str_count_down_seconds);
        intent.putExtra("str_2x_powerup", str_2x_powerup);
        intent.putExtra("str_correct_ans", str_correct_ans);
        intent.putExtra("str_wrong_ans", str_wrong_ans);
        intent.putExtra("str_skip", str_skip);
        intent.putExtra("str_entry_fees", str_entry_fees);
        intent.putExtra("str_contest_id", str_contest_id);

        intent.putExtra("int_play_status", "1");
        intent.putExtra("str_playby", "Coins");
        intent.putExtra("str_mCoinCountText", mCoinCountText.getText().toString());

//        Log.e("str_int_play_status_log_enter_game_method", "" + this.int_play_status);
//        Log.e("str_playby_log_enter_game_method", "Coins");
//        Log.e("str_mCoinCountText_log_enter_game_method", str_entry_fees);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void Get_User_Wallet_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("wallet_json_dashboard", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_WalletDetailsModelCall("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_code = response.body().code;
                            str_message = response.body().message;
                            str_wallet1 = response.body().data.get(0).wallet1;
                            str_wallet2 = response.body().data.get(0).wallet2;
//                            Log.e("str_wallet1111", str_wallet1);
//                            Log.e("str_wallet2", str_wallet2);
                            double_db_balance = Double.parseDouble(str_wallet1);
                            int_db_balance = (int) double_db_balance;
//                        Log.e("int_balance_convert", String.valueOf(int_db_balance));


                            if (int_entry_fee > int_db_balance) {
                                int n11 = int_entry_fee - int_db_balance;
//                            Log.e("n1111", String.valueOf(n11));
                                tv_enter_contest_btn.setVisibility(View.GONE);
                                if (str_status_onclick.equals("0")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
                                    tv_earn_coins.setVisibility(View.VISIBLE);
                                } else if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                    tv_earn_coins.setVisibility(View.GONE);
                                }
                                tv_play_btn.setVisibility(View.GONE);
                                tv_earn_coins.setText(String.valueOf(n11));
                                tv_coins.setVisibility(View.VISIBLE);
                            } else {
                                if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                    tv_play_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                                } else {
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    tv_play_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                                }
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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

    private void Get_Rewarded_Video_Method(Bundle savedInstanceState) {
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
            }

            @Override
            public void onRewardedVideoAdLoaded() {
//                Toast.makeText(getBaseContext(), "Ad loaded.", Toast.LENGTH_SHORT).show();
                showRewardedVideo_New();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getBaseContext(), "Ad loaded handler.", Toast.LENGTH_SHORT).show();
                    }
                }, 500);

                tv_loading_tx.setVisibility(View.GONE);
                progress_bar_in_reward_video.setVisibility(View.GONE);
            }

            @Override
            public void onRewardedVideoAdOpened() {
//                Toast.makeText(getBaseContext(), "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
//                Toast.makeText(getBaseContext(), "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
//                Toast.makeText(getBaseContext(), "Ad closeddddd.", Toast.LENGTH_SHORT).show();
                constraintLayout_rewarded_video.setVisibility(View.GONE);
                constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
                recreate();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
//                Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
//                Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                int n1 = Integer.parseInt(str_session_reward_amount);
//                Log.e("reward_session_point", "" + n1);
                addCoins(n1);
            }
        });

        if (savedInstanceState == null) {
            mCoinCount = 0;
            mCoinCountText.setText(String.valueOf(mCoinCount));
            startGame_Rewarded_Video();
        }
    }

    private void AD_Closed_Method() {
        constraintLayout_rewarded_video.setVisibility(View.GONE);
        constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
        Get_Wallet_Balance_Details();

//        recreate();

//        finish();
//        startActivity(getIntent());

//        Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();



        /*
        String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        cursor.close();

//        Log.e("ad_closed_log", "Ad closed");
//        Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();
//        Log.e("coin_count", mCoinCountText.getText().toString());
        int_rewarded_coins_point = Integer.parseInt(mCoinCountText.getText().toString());
        int_total_value = int_db_balance_for_reward_video + int_rewarded_coins_point;
        ContentValues contentValues = new ContentValues();
        Log.e("int_total_value", "" + int_total_value);
        contentValues.put("BALANCE", int_total_value);
        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
//        Log.e("Content_Values_mob_reg", contentValues.toString());
        DBEXPORT();
        Toast_Message_For_Reward.showToastMessage(Game_Details_Screen_Act.this, "You have earned " + int_rewarded_coins_point + " coins from video," + "\n Your current balance point is " + String.valueOf(int_total_value) + ".");
        str_playby = "Ads";

        Get_User_Wallet_Details();
        if (int_entry_fee > int_total_value) {
            int n11 = int_entry_fee - int_total_value;
            tv_earn_coins.setText(String.valueOf(n11));
//            Log.e("int_entry_fee_ad", "" + int_entry_fee);
//            Log.e("n1111", "" + n11);
//            Log.e("n2222", "" + int_rewarded_coins_point);
//            Log.e("n3333", "" + int_total_value);
//            Toast.makeText(Game_Details_Screen_Act.this, "if_side", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(Game_Details_Screen_Act.this, "else_side", Toast.LENGTH_SHORT).show();
            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
            tv_play_btn.setVisibility(View.VISIBLE);
            constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
        }
        if (int_rewarded_coins_point != 10) {
            constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
            constraintLayout_rewarded_video.setVisibility(View.GONE);
        }
        constraintLayout_rewarded_video.setVisibility(View.GONE);
        constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
        Glide.with(Game_Details_Screen_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
              *//*  if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    startTimer(milliseconds);
                }*//*
            }

        }, 5500);*/
    }

    private void Get_Wallet_Balance_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("ad_cls_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_WALLET_BALALNCE_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_nav_curnt_amnt = response.body().current_amt;
//                            Log.e("str_amount_nav_with_ad", str_nav_curnt_amnt);
                            int_rewarded_coins_point = Integer.parseInt(mCoinCountText.getText().toString());
//                            Log.e("int_rewdcns_pnt", "" + int_rewarded_coins_point);
                            int n1 = int_tv_points + int_rewarded_coins_point;
//                            Log.e("n11dfsfs", "" + n1);
//                            Toast_Message_For_Reward.showToastMessage(Game_Details_Screen_Act.this, "You have earned " + int_rewarded_coins_point + " coins from video," + "\n Your current balance point is " + String.valueOf(n1) + ".");
                            Toast_Message_For_Reward.showToastMessage(Game_Details_Screen_Act.this, "You have earned " + int_rewarded_coins_point + " coins." + "\n Your current coin balance is " + String.valueOf(n1) + ".");
                            int int_tv_points11 = Integer.parseInt(str_nav_curnt_amnt);
//                            Log.e("after_ad", "" + str_nav_curnt_amnt);
                            if (int_entry_fee > int_tv_points11) {
//                                recreate();
                                int n11 = int_entry_fee - int_tv_points11;
                                tv_earn_coins.setText(String.valueOf(n11));
//                                Log.e("n1111", "" + n11);
                                constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);
                                constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                            } else {
//                                recreate();
                                if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    tv_play_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                } else {
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    tv_play_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                }
                            }
                            if (int_rewarded_coins_point != 10) {
                                constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
                                constraintLayout_rewarded_video.setVisibility(View.GONE);
                                constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);
                                constraintLayout_end_game_game_two.setVisibility(View.GONE);
                            }

                            str_playby = "Ads";
                            Get_Points_Add_Delete_Details(str_playby);
                            recreate();
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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


    @SuppressLint("LongLogTag")
    private void Get_Points_Add_Delete_Details(String str_playby) {
        try {
            /*String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
            Cursor cursor = db.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {

                } while (cursor.moveToNext());
            }
            cursor.close();*/
            JSONObject jsonObject = new JSONObject();
            if (str_playby.equalsIgnoreCase("Ads")) {
                jsonObject.put("email", str_email);
                jsonObject.put("wallet", mCoinCountText.getText().toString());
                jsonObject.put("playby", str_playby);
//                Log.e("add_dlt_json_value_ad", jsonObject.toString());
            } else if (str_playby.equalsIgnoreCase("Coins")) {
                jsonObject.put("email", str_email);
                jsonObject.put("wallet", str_entry_fees);
                jsonObject.put("playby", str_playby);
//                Log.e("add_dlt_json_value_coins", jsonObject.toString());
            }

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_Wallet_Point_Delete_Call("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        String str_amount = response.body().current_amt;
                        Navigation_Drawer_Act.tv_points.setText(str_amount);
                        recreate();
//                        Log.e("recreate_json_value_coins", jsonObject.toString());
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Details_Screen_Act.this, response.message());
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

    private void DBEXPORT() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.spot_the_ballgame" + "/databases/" + "Spottheball.db";
        String backupDBPath = "Spottheball.db";
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

    private void startGame_Rewarded_Video() {
        mGamePaused = false;
        mGameOver = false;
        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }

    private void showRewardedVideo_New() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void addCoins(int coins) {
        mCoinCountText.setVisibility(View.VISIBLE);
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText(String.valueOf(mCoinCount));
        AD_Closed_Method();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Guideline getNewGuideline(Game_Details_Screen_Act game_details_screen_act, int horizontal) {
        Guideline guideline = new Guideline(game_details_screen_act);
        guideline.setId(Guideline.generateViewId());
        ConstraintLayout.LayoutParams lp =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.orientation = horizontal;
        guideline.setLayoutParams(lp);

        return guideline;
    }

    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setSnackbarMessage(String status) {
        String internetStatus;
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = getResources().getString(R.string.back_online_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.timer_bg_color);
            Get_Winning_List_Details();
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar
                    .make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundResource(R.color.red_color_new);
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
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        shimmer_view_container_winning_list_game_two.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        shimmer_view_container_winning_list_game_two.stopShimmerAnimation();
    }

    private void displayNeverAskAgainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need to download images for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (SEND_STORAGE_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted successfully");
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_LONG).show();
            } else {
                PermissionUtils.setShouldShowStatus(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private static class PermissionUtils {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public static boolean neverAskAgainSelected(final Activity activity, final String permission) {
            final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission);
            final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
            return prevShouldShowStatus != currShouldShowStatus;
        }

        public static void setShouldShowStatus(final Context context, final String permission) {
            SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = genPrefs.edit();
            editor.putBoolean(permission, true);
            editor.commit();
        }

        public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
            SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
            return genPrefs.getBoolean(permission, false);
        }
    }
}

/*
package com.spot_the_ballgame;
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
import android.graphics.drawable.AnimationDrawable;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
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
import com.spot_the_ballgame.Adapter.Price_List_Adapter_Game_Two;
import com.spot_the_ballgame.Adapter.Rules_List_Name_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Interface.Factory_For_Categories;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_Two_Act extends AppCompatActivity implements View.OnClickListener {
    int int_entry_fee, int_db_balance;
    Date current_system_time, end_date_api;
    long different_milli_seconds;
    private long lng_seconds;
    long milliseconds;

    int n1_1;
    int n1_old_value, n2_new_value, n3_normal_value;
    Dialog dialog;
    private boolean isCanceled = false;
    TextView tv_timer_seconds_count_game_two, tv_timer_seconds_txt_game_two, tv_two_x_game_two, tv_remaing_count_value_in_game_two,
            tv_questions_in_game_two, tv_ans_0_game_two,
            tv_ans_1_game_two, tv_ans_2_game_two, tv_ans_3_game_two;
    public int int_sec = 10;
    private long totalTimeCountInMilliseconds;
    public long seconds;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer_game_details;
    int int_count_value = 0;
    int int_reaming_page_count_value = 1;
    ConstraintLayout constraintLayout_just_a_moment_game_two, constraintLayout_end_game_game_two, constraintLayout_game_screen_in_game_two;

    ConstraintLayout constraintLayout_answer_set_01;
    ConstraintLayout constraintLayout_game_details_screen_in_game_two;
    ConstraintLayout constraintLayout_rules_list;
    ConstraintLayout constraintLayout_prize_layout;
    ConstraintLayout constraintLayout_count_down_timer;
    ConstraintLayout constraintLayout_score_board_layout;
    ConstraintLayout constraintLayout_rewarded_video;
    ConstraintLayout constraintLayout_watch_ads_btn_inside;

    TextView tv_price_btn, tv_rules_btn, tv_watch_ads_btn, tv_coins, tv_enter_contest_btn, tv_score_board, tv_enter_contest_btn_for_free, tv_earn_coins, tv_play_btn;
    ConstraintLayout constraintLayout_watch_ads_btn;
    ArrayList<String> questionsIntegerArrayList = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_01 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_02 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_03 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_04 = new ArrayList<>();
    ArrayList<String> Final_Answer_ArrayList = new ArrayList<>();

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    ProgressBar progressBar;

    String str_remaining_count_value = "";
    int int_2_x_count = 0;

    Handler handler;
    AnimationDrawable animationDrawable;
    TextView tv_just_a_moment;

    Vibrator vibrator;
    Dialog dialog_fr_timer;
    Context context;
    String str_game_name, str_prize_amount, str_end_time;
    TextView tv_game_name, tv_2x_power_up;
    RecyclerView rv_price_list_game_two;
    Price_List_Adapter_Game_Two price_list_adapter_game_two;

    int int_2x_onclick_count = 0;
    String str_2x_powerup, str_seconds, str_contest_id, str_entry_fees;
    TextView tv_time_limit_sec_txt, tv_entry_fee_details;
    private ShimmerFrameLayout mShimmerViewContainer, shimmer_view_container_for_rules;
    private AdView mAdView, mAdView_end_game;

    String str_onclick_answer_selection, str_onclick_time, str_onclicked_question_number, str_onclick_2x_powerup, str_final_answer;
    int int_onclcik_2x_power_up = 0;
    TextView tv_prize_amount_game_details, tv_game_end_time_game_details;
    ConstraintLayout constraintLayout_tv_2x_power_up;

    ArrayList<String> answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> timer_ArrayList = new ArrayList<>();
    ArrayList<String> questionnumber_ArrayList = new ArrayList<>();
    ArrayList<String> _2x_power_up_ArrayList = new ArrayList<>();

    HashMap<String, String> hashMap = new HashMap<>();
    HashMap<Integer, String> finalhashMap = new HashMap<>();
    String question_count;
    Bundle bundle;

    int int_str_seconds, int_str_onclick_time, difference_time, int_number_of_questions;
    String str_difference_time;

    TextView tv_number_of_questions_in_api;

    private InterstitialAd interstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-7961776813129160/4130614251";
//    private static final String AD_UNIT_ID = "ca-app-pub-7961776813129160~6949709129";

    AdLoader adLoader;
    ImageView iv_ready_steady_go_state;
    FirebaseAnalytics mFirebaseAnalytics;

    RecyclerView rv_rules_list_game_two;
    Rules_List_Name_Adapter rules_list_name_adapter;
    String str_rule_id, str_status_onclick, str_imagepath;

    private final String TAG = Game_Two_Act.class.getSimpleName();
    com.facebook.ads.InterstitialAd interstitialAd_fb;

    TextView tv_end_game_sucess_message;

    SQLiteDatabase db;

    String str_phone_no, str_email, str_balance;
    String str_onclick_play_by_value = "";


    */
/*This components are used for Rewarded video*//*

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
    TextView tv_loading_tx;
    ProgressBar progress_bar_in_reward_video;
    ImageView gif_image, iv_categoires_image_game_two_act;

    Bundle savedInstanceState;

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(Game_Two_Act.this).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);

        */
/*This components are used for Rewarded video*//*

        tv_loading_tx = findViewById(R.id.tv_loading_txt);
        gif_image = findViewById(R.id.gif_image);
        iv_categoires_image_game_two_act = findViewById(R.id.iv_categoires_image_game_two_act);
        Glide.with(this).asGif().load(R.drawable.giphy).into(gif_image);
        progress_bar_in_reward_video = findViewById(R.id.progress_bar_in_reward_video);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mCoinCountText = findViewById(R.id.coin_count_text);

        //Ironsource_code
        IronSource.setConsent(true);
        //This is for ad colony
        AdColony.configure(this, "appd9b3bb873a744248bd", "vz09df69e0202642b88a");


        tv_timer_seconds_count_game_two = findViewById(R.id.tv_timer_seconds_count_game_two);
        tv_timer_seconds_txt_game_two = findViewById(R.id.tv_timer_seconds_txt_game_two);
        tv_two_x_game_two = findViewById(R.id.tv_two_x_game_two);
        tv_remaing_count_value_in_game_two = findViewById(R.id.tv_remaing_count_value_in_game_two);
        tv_questions_in_game_two = findViewById(R.id.tv_questions_in_game_two);
        tv_just_a_moment = findViewById(R.id.tv_just_a_moment);
        tv_game_name = findViewById(R.id.tv_game_name);
        tv_2x_power_up = findViewById(R.id.tv_2x_power_up);
        tv_time_limit_sec_txt = findViewById(R.id.tv_time_limit_sec_txt);
        tv_entry_fee_details = findViewById(R.id.tv_entry_fee_details);
        tv_game_end_time_game_details = findViewById(R.id.tv_game_end_time_game_details);
        tv_prize_amount_game_details = findViewById(R.id.tv_prize_amount_game_details);
        constraintLayout_tv_2x_power_up = findViewById(R.id.constraintLayout_tv_2x_power_up);
        tv_number_of_questions_in_api = findViewById(R.id.tv_number_of_questions_in_api);
        iv_ready_steady_go_state = findViewById(R.id.iv_ready_steady_go_state);
        tv_end_game_sucess_message = findViewById(R.id.tv_end_game_sucess_message);

        constraintLayout_rewarded_video = findViewById(R.id.constraintLayout_rewarded_video);
        constraintLayout_watch_ads_btn_inside = findViewById(R.id.constraintLayout_watch_ads_btn_inside);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(Game_Two_Act.this);

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


        String select = "select EMAIL,PHONENO ,BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
                str_phone_no = cursor.getString(1);
                str_balance = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("str_email", str_email);
        Log.e("str_phone_no", str_phone_no);
//        Log.e("str_balance", str_balance);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        shimmer_view_container_for_rules = findViewById(R.id.shimmer_view_container_for_rules);
        rv_price_list_game_two = findViewById(R.id.rv_price_list_game_two);
        rv_price_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_price_list_game_two.setVisibility(View.VISIBLE);

        rv_rules_list_game_two = findViewById(R.id.rv_rules_list_game_two);
        rv_rules_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dialog_fr_timer = new Dialog(Game_Two_Act.this);
        dialog_fr_timer.setContentView(R.layout.please_wait_lay);
        Objects.requireNonNull(dialog_fr_timer.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        */
/*These components are for Quiz game  screen*//*

        tv_ans_0_game_two = findViewById(R.id.tv_ans_0_game_two);
        tv_ans_1_game_two = findViewById(R.id.tv_ans_1_game_two);
        tv_ans_2_game_two = findViewById(R.id.tv_ans_2_game_two);
        tv_ans_3_game_two = findViewById(R.id.tv_ans_3_game_two);
        constraintLayout_answer_set_01 = findViewById(R.id.constraintLayout_answer_set_01);
        constraintLayout_just_a_moment_game_two = findViewById(R.id.constraintLayout_just_a_moment_game_two);
        constraintLayout_end_game_game_two = findViewById(R.id.constraintLayout_end_game_game_two);
        constraintLayout_game_screen_in_game_two = findViewById(R.id.constraintLayout_game_screen_in_game_two);

        */
/*These components are for game details screen*//*

        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_rules_list = findViewById(R.id.constraintLayout_rules_list);
        constraintLayout_prize_layout = findViewById(R.id.constraintLayout_prize_layout);
        constraintLayout_count_down_timer = findViewById(R.id.constraintLayout_count_down_timer);
        tv_price_btn = findViewById(R.id.tv_price_btn);
        tv_rules_btn = findViewById(R.id.tv_rules_btn);
        tv_watch_ads_btn = findViewById(R.id.tv_watch_ads_btn);
        tv_earn_coins = findViewById(R.id.tv_earn_coins);
        tv_play_btn = findViewById(R.id.tv_play_btn);
        tv_coins = findViewById(R.id.tv_coins);
        constraintLayout_watch_ads_btn = findViewById(R.id.constraintLayout_watch_ads_btn);
        tv_enter_contest_btn = findViewById(R.id.tv_enter_contest_btn);
        tv_enter_contest_btn_for_free = findViewById(R.id.tv_enter_contest_btn_for_free);

        constraintLayout_score_board_layout = findViewById(R.id.constraintLayout_score_board_layout);
        tv_score_board = findViewById(R.id.tv_score_board);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), android.graphics.PorterDuff.Mode.SRC_IN);
        tv_questions_in_game_two.setMovementMethod(new ScrollingMovementMethod());
        bundle = getIntent().getExtras();
        if (bundle == null) {
            str_game_name = null;
        } else {
            str_game_name = bundle.getString("game_name");
            str_2x_powerup = bundle.getString("str_2x_powerup");
            str_seconds = bundle.getString("count_down_seconds");
            str_contest_id = bundle.getString("str_contest_id");
            str_entry_fees = bundle.getString("str_entry_fees");
            str_prize_amount = bundle.getString("prize_amount");
            str_end_time = bundle.getString("end_time");
            str_rule_id = bundle.getString("str_rule_id");
            str_status_onclick = bundle.getString("str_status_onclick");
            str_imagepath = bundle.getString("str_imagepath");
            Log.e("statusonclick_gameact", str_status_onclick);
            Log.e("str_entry_fees_gameact", str_entry_fees);
        }

        try {
            int_entry_fee = Integer.parseInt(str_entry_fees);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        int_db_balance = Integer.parseInt(str_balance);

        Log.e("int_entry_free", String.valueOf(int_entry_fee));
        Log.e("int_balance", String.valueOf(int_db_balance));


        if (int_entry_fee > int_db_balance) {
            Log.e("topinside_entry_free", String.valueOf(int_entry_fee));
            Log.e("topinside_balance", String.valueOf(int_db_balance));
            int n11 = int_entry_fee - int_db_balance;
            Log.e("n1111", String.valueOf(n11));
            tv_enter_contest_btn.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
            constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
            tv_earn_coins.setVisibility(View.VISIBLE);
            tv_play_btn.setVisibility(View.GONE);
            tv_earn_coins.setText(String.valueOf(n11));
            tv_coins.setVisibility(View.VISIBLE);
        } else {
            tv_play_btn.setVisibility(View.VISIBLE);
            constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
        }
        if (str_entry_fees.equalsIgnoreCase("Free")) {
            tv_enter_contest_btn_for_free.setVisibility(View.VISIBLE);
            tv_enter_contest_btn.setVisibility(View.GONE);
            tv_watch_ads_btn.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
        }*/
/* else {
            tv_enter_contest_btn.setVisibility(View.VISIBLE);
            tv_watch_ads_btn.setVisibility(View.VISIBLE);
            constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
            tv_enter_contest_btn_for_free.setVisibility(View.GONE);
        }*//*


        if (str_status_onclick.equals("1")) {
            Toast.makeText(Game_Two_Act.this, "inside", Toast.LENGTH_SHORT).show();
            tv_enter_contest_btn_for_free.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
            tv_enter_contest_btn.setVisibility(View.GONE);
        }


        int_2x_onclick_count = Integer.parseInt(str_2x_powerup);
        tv_2x_power_up.setText(str_2x_powerup);
        tv_time_limit_sec_txt.setText(str_seconds + "S");

        tv_game_name.setText(str_game_name);
        tv_prize_amount_game_details.setText(str_prize_amount);
        tv_entry_fee_details.setText(String.valueOf(str_entry_fees));
        tv_game_end_time_game_details.setText(str_end_time);

        Glide.with(Game_Two_Act.this).load(str_imagepath)
                .thumbnail(Glide.with(Game_Two_Act.this).load(str_imagepath))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_categoires_image_game_two_act);


        if (str_entry_fees.equalsIgnoreCase("Free")) {
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                }

                @Override
                public void onAdClosed() {
                    startGame();
                    mAdView.setVisibility(View.GONE);
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                    constraintLayout_just_a_moment_game_two.setVisibility(View.VISIBLE);

                    Glide.with(Game_Two_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
                    Get_Questions_Details();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            constraintLayout_game_screen_in_game_two.setVisibility(View.VISIBLE);
                            if (!isNetworkAvaliable()) {
                                registerInternetCheckReceiver();
                            } else {
                                startTimer(milliseconds);
                            }
                        }
                    }, 5500);
                    constraintLayout_end_game_game_two.setVisibility(View.GONE);
                }
            });

        }


        milliseconds = Long.parseLong(str_seconds) * 1000 + 1;
        Log.e("millisecondssss", "" + milliseconds);
        */
/*Getting Current Time*//*

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strDate = mdformat.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            current_system_time = mdformat.parse(strDate);
            end_date_api = sdf.parse(str_end_time);
            tv_game_end_time_game_details.setText(sdf.format(end_date_api));
            different_milli_seconds = end_date_api.getTime() - current_system_time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        countDownTimer_game_details = new CountDownTimer(different_milli_seconds, 1000) {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                lng_seconds = millisUntilFinished;
                String s1 = String.format("%2d", lng_seconds).trim();
                String ss = String.format("%02d:%02d:%02d", lng_seconds / 60, lng_seconds % 60, 0);

                @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                //System.out.println("HH_MM_SS:: " + hms);


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

        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            mShimmerViewContainer.startShimmerAnimation();
            Get_Prize_List_Details();
        }
        tv_two_x_game_two.setOnClickListener(this);
        tv_ans_0_game_two.setOnClickListener(this);
        tv_ans_1_game_two.setOnClickListener(this);
        tv_ans_2_game_two.setOnClickListener(this);
        tv_ans_3_game_two.setOnClickListener(this);


        tv_price_btn.setOnClickListener(this);
        tv_rules_btn.setOnClickListener(this);
        constraintLayout_watch_ads_btn.setOnClickListener(this);
        tv_enter_contest_btn.setOnClickListener(this);
        tv_enter_contest_btn_for_free.setOnClickListener(this);
        tv_score_board.setOnClickListener(this);
        constraintLayout_end_game_game_two.setVisibility(View.GONE);
//        FullScreenMethod();
        handler = new Handler();

    }

    private void Load_FB_ADS() {
        interstitialAd_fb.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd_fb.loadAd();
    }

    private void Get_Rules_Name_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", str_rule_id);
            APIInterface apiInterface = Factory_For_Categories.getClient();
            Call<Category_Model> call = apiInterface.GET_RULES_NAME_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    assert response.body() != null;
                    */
/*if (response.body().data == null) {
                        Toast.makeText(Game_Two_Act.this, "Null", Toast.LENGTH_SHORT).show();
                        constraintLayout_rules_list.setVisibility(View.GONE);
                        shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                        rv_rules_list_game_two.setVisibility(View.GONE);
                    } else {
                        constraintLayout_rules_list.setVisibility(View.VISIBLE);
                        shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                        rv_rules_list_game_two.setVisibility(View.VISIBLE);
                    }*//*

                    rv_rules_list_game_two.setVisibility(View.VISIBLE);
                    constraintLayout_rules_list.setVisibility(View.VISIBLE);
                    shimmer_view_container_for_rules.stopShimmerAnimation();
                    shimmer_view_container_for_rules.setVisibility(View.GONE);
                    rules_list_name_adapter = new Rules_List_Name_Adapter(Game_Two_Act.this, response.body().data);
                    rv_rules_list_game_two.setAdapter(rules_list_name_adapter);
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }
    }

    private void Get_Questions_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            Log.e("img_res", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Two_Act.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.0.113/stb-api/index.php/categories/get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_object = (JSONObject) jsonArray.get(i);
                                String question = json_object.getString("question");
                                String option_a = json_object.getString("option_a");
                                String option_b = json_object.getString("option_b");
                                String option_c = json_object.getString("option_c");
                                String option_d = json_object.getString("option_d");
                                String answer = json_object.getString("answer");
                                question_count = json_object.getString("question_count");
                                tv_number_of_questions_in_api.setText(question_count);
                                String s1 = tv_number_of_questions_in_api.getText().toString();
                                int_number_of_questions = Integer.parseInt(s1);

//                                Log.e("question_json", "" + question);
//                                Log.e("option_a_json", option_a);
//                                Log.e("option_b_json", option_b);
//                                Log.e("option_c_json", option_c);
//                                Log.e("option_d_json", option_d);
//                                Log.e("answer_json", answer);
//                                Log.e("question_count_json", question_count);
                                questionsIntegerArrayList.add(question);
                                answersIntegerArrayList_01.add(option_a);
                                answersIntegerArrayList_02.add(option_b);
                                answersIntegerArrayList_03.add(option_c);
                                answersIntegerArrayList_04.add(option_d);
                                Final_Answer_ArrayList.add(answer);
                                Log.e("Initial_final_ans", Final_Answer_ArrayList.toString());
                                tv_questions_in_game_two.setText(questionsIntegerArrayList.get(0));
                                Log.e("qstnsIntegerAryLstSze", "" + questionsIntegerArrayList.size());
                                Log.e("apiArrayList", questionsIntegerArrayList.toString());
                                Log.e("option_a_apiArrayList", answersIntegerArrayList_01.toString());
                                Log.e("option_b_apiArrayList", answersIntegerArrayList_02.toString());
                                Log.e("option_c_apiArrayList", answersIntegerArrayList_03.toString());
                                Log.e("option_d_apiArrayList", answersIntegerArrayList_04.toString());


                                if (answersIntegerArrayList_01.get(0).length() >= 50 || answersIntegerArrayList_02.get(0).length() >= 50
                                        || answersIntegerArrayList_03.get(0).length() >= 50 || answersIntegerArrayList_04.get(0).length() >= 50) {

                                    tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(0).substring(2));
                                    tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(0).substring(2));
                                    tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(0).substring(2));
                                    tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(0).substring(2));

                                    tv_ans_0_game_two.setTextSize(13);
                                    tv_ans_1_game_two.setTextSize(13);
                                    tv_ans_2_game_two.setTextSize(13);
                                    tv_ans_3_game_two.setTextSize(13);
                                } else {
                                    tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(0).substring(2));
                                    tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(0).substring(2));
                                    tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(0).substring(2));
                                    tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(0).substring(2));
                                }
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

    private void Get_Prize_List_Details() {
        try {
//            Log.e("inside_contest_id", str_contest_id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            APIInterface apiInterface = Factory_For_Categories.getClient();
            Log.e("json_game_two", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_PRIZE_DISTRINUTION_CALL(jsonObject.toString());
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() == null) {
                                Toast_Message.showToastMessage(Game_Two_Act.this, "Null_Response");
                            } else {
                                price_list_adapter_game_two = new Price_List_Adapter_Game_Two(Game_Two_Act.this, response.body().data);
                                rv_price_list_game_two.setAdapter(price_list_adapter_game_two);
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                            }
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

    private void startTimer(long totalTimeCountInMilliseconds) {
        long milli = totalTimeCountInMilliseconds + 1000;
        countDownTimer = new CountDownTimer(milli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
//                Log.e("seconds_value", String.valueOf(seconds));
                String s1 = String.format("%2d", seconds).trim();
                if (s1.length() == 2) {
                    tv_timer_seconds_count_game_two.setText(s1);
                } else {
                    tv_timer_seconds_count_game_two.setText("0" + s1);
                }
                if (seconds == 9) {
//                    constraintLayout_timer.setBackgroundResource(R.drawable.timer_bg);
                    constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                    tv_timer_seconds_count_game_two.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt_game_two.setTextColor(getResources().getColor(R.color.white_color));
                } else if (seconds == 3) {
                    constraintLayout_count_down_timer.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.shake));
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                    tv_timer_seconds_count_game_two.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt_game_two.setTextColor(getResources().getColor(R.color.white_color));
                    constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_red_alert_bg));
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
                    tv_timer_seconds_count_game_two.setText("00");
                }
            }

            @Override
            public void onFinish() {
                tv_timer_seconds_count_game_two.setText("00");
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_two_x_game_two.setEnabled(true);
               */
/* if (int_2_x_count == 1) {
                    tv_two_x_game_two.setVisibility(View.GONE);
                } else {
                    tv_two_x_game_two.setVisibility(View.VISIBLE);
                }*//*

                tv_timer_seconds_count_game_two.setTextColor(getResources().getColor(R.color.white_color));
                tv_timer_seconds_txt_game_two.setTextColor(getResources().getColor(R.color.white_color));
                do {
                    if (int_count_value < questionsIntegerArrayList.size()) {
//                        int_count_value++;
//                        int_reaming_page_count_value++;
                        int n1 = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
                        int_reaming_page_count_value = n1 + 1;
                        int_count_value = n1;
//                            Log.e("onfinish_count", "" + int_reaming_page_count_value);
                        try {
                            if (int_reaming_page_count_value <= int_number_of_questions) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                                        tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                                        tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                                        tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                                        tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.white_color));

                                        constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                                        tv_timer_seconds_count_game_two.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_timer_seconds_txt_game_two.setTextColor(getResources().getColor(R.color.white_color));
                                        dialog_fr_timer.dismiss();
                                        tv_remaing_count_value_in_game_two.setText(String.valueOf(int_reaming_page_count_value));
                                        tv_questions_in_game_two.setText(questionsIntegerArrayList.get(int_count_value));

                                        if (answersIntegerArrayList_01.get(0).length() >= 50 || answersIntegerArrayList_02.get(0).length() >= 50
                                                || answersIntegerArrayList_03.get(0).length() >= 50 || answersIntegerArrayList_04.get(0).length() >= 50) {

                                            tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(int_count_value).substring(2));
                                            tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(int_count_value).substring(2));
                                            tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(int_count_value).substring(2));
                                            tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(int_count_value).substring(2));

                                            tv_ans_0_game_two.setTextSize(13);
                                            tv_ans_1_game_two.setTextSize(13);
                                            tv_ans_2_game_two.setTextSize(13);
                                            tv_ans_3_game_two.setTextSize(13);

                                        } else {

                                            tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(int_count_value).substring(2));
                                            tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(int_count_value).substring(2));
                                            tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(int_count_value).substring(2));
                                            tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(int_count_value).substring(2));

                                        }

                                        str_onclick_answer_selection = "Skip";
                                        int int_question_number = int_count_value;
                                        int int_previous_page = int_reaming_page_count_value - 1;
                                        Log.e("not_answered", str_onclick_answer_selection);
                                        Log.e("int_question_number", "" + int_question_number);
                                        Log.e("int_previous_page", "" + int_previous_page);


                                        questionnumber_ArrayList.add(String.valueOf(int_previous_page));
                                        answerselection_ArrayList.add(str_onclick_answer_selection);
                                        timer_ArrayList.add("0");
                                        _2x_power_up_ArrayList.add("0");

                                        Log.e("finish_qstnnum_AryLst", "" + questionnumber_ArrayList.toString());
                                        Log.e("finish_anslctn_AryLst", answerselection_ArrayList.toString());
                                        Log.e("finish_timer_AryLst", timer_ArrayList.toString());
                                        Log.e("finish_2x_pwr_up_AryLst", _2x_power_up_ArrayList.toString());

                                        Log.e("onfinish_2x_power", String.valueOf(int_onclcik_2x_power_up));
//                                            Screen_01_Method(int_previous_page, str_onclick_answer_selection, "0", "0");
                                        str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();
                                        startTimer(milliseconds);
                                        if (str_onclick_answer_selection.equalsIgnoreCase("Skip") && ((int_onclcik_2x_power_up != 0))) {
                                          */
/*  if (int_2x_onclick_count == 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                tv_two_x_game_two.setVisibility(View.VISIBLE);
                                                tv_2x_power_up.setVisibility(View.VISIBLE);
                                                constraintLayout_tv_2x_power_up.setVisibility(View.VISIBLE);
                                            } else {
                                                tv_2x_power_up.setText("0");
                                                tv_two_x_game_two.setEnabled(false);
                                                tv_two_x_game_two.setVisibility(View.VISIBLE);
                                                tv_2x_power_up.setVisibility(View.VISIBLE);
                                                constraintLayout_tv_2x_power_up.setVisibility(View.VISIBLE);
                                            }
                                           *//*

//                                            Toast.makeText(Game_Two_Act.this, "2x_count" + int_2x_onclick_count, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(Game_Two_Act.this, "2x_power_up_outside: " + int_onclcik_2x_power_up, Toast.LENGTH_SHORT).show();
                                            if (int_2x_onclick_count != 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_two_x_game_two.setEnabled(true);
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                            } else if (int_2x_onclick_count == 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                */
/*int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                *//*



                                                if (int_onclcik_2x_power_up != 0) {
                                                    Toast.makeText(Game_Two_Act.this, "2x_power_up_inside: " + int_onclcik_2x_power_up, Toast.LENGTH_SHORT).show();


                                                    int_2x_onclick_count = int_2x_onclick_count + 1;
                                                    int_onclcik_2x_power_up = 1;
                                                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

//                                                  tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
//                                                    tv_two_x_game_two.setEnabled(true);
                                                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                    tv_two_x_game_two.setEnabled(true);

                                                } else {
                                                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                                                    tv_two_x_game_two.setEnabled(false);

                                                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                                                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
                                                }
//                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                            }
                                            */
/*if (tv_2x_power_up.getText().toString().equalsIgnoreCase("1")) {
                                                int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

//                                                tv_two_x_game_two.setVisibility(View.GONE);
//                                                tv_2x_power_up.setVisibility(View.GONE);
//                                                constraintLayout_tv_2x_power_up.setVisibility(View.GONE);

                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_two_x_game_two.setEnabled(true);

                                            } else {
                                                int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_two_x_game_two.setEnabled(true);
                                            }*//*

                                        }
                                        int_onclcik_2x_power_up = 0;
                                    }
                                }, 2300);
                            } else {
                                dialog_fr_timer.dismiss();
                                cancel();
                                constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                                constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                                constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
                                constraintLayout_end_game_game_two.setBackgroundResource((R.drawable.end_game_grey_bg));
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        str_onclick_answer_selection = "Skip";
                                        int int_question_number = int_count_value;
                                        int int_previous_page = int_reaming_page_count_value - 1;
//                                            Log.e("finish_not_answered", str_onclick_answer_selection);
//                                            Log.e("finish_int_question_number", "" + int_question_number);
//                                            Log.e("finish_int_previous_page", "" + int_previous_page);


                                        questionnumber_ArrayList.add(String.valueOf(int_previous_page));
                                        answerselection_ArrayList.add(str_onclick_answer_selection);
                                        timer_ArrayList.add("0");
                                        _2x_power_up_ArrayList.add("0");

                                        Log.e("finish10qstnnum_AryLst", "" + questionnumber_ArrayList.toString());
                                        Log.e("finish10anselctn_AryLst", answerselection_ArrayList.toString());
                                        Log.e("finish10timer_AryLst", timer_ArrayList.toString());
                                        Log.e("finish102x_pwrup_AryLst", _2x_power_up_ArrayList.toString());
                                        Getting_Final_Details();
                                        Getting_Update_Status_Details();
                                        int_onclcik_2x_power_up = 0;
                                        countDownTimer.cancel();
                                        Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                    }
                                }, 2300);
                            }
                        } catch (IndexOutOfBoundsException ari) {
                            ari.printStackTrace();
                        }
                    }
                } while (int_count_value > questionsIntegerArrayList.size());
            }
        }.start();
        performTick(10000);
    }

    private void Getting_Final_Details() {
        int int_question_count = Integer.parseInt(question_count);
        Log.e("timer_ArrayList_size", "" + timer_ArrayList.size());
        for (int i = 0; i < int_question_count; i++) {
            hashMap.put("onclick_time", timer_ArrayList.get(i));
            hashMap.put("onclick_answer", answerselection_ArrayList.get(i));
            hashMap.put("onclick_2x", _2x_power_up_ArrayList.get(i));
            finalhashMap.put((i + 1), String.valueOf(hashMap));
        }
        Map<Integer, String> integerStringMap = new TreeMap<>(finalhashMap);
        Log.e("Final_Hashmap", "" + finalhashMap.toString());
        Log.e("Final_IntegerStringMap", "" + integerStringMap.toString());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", str_email);
            jsonObject.put("phonenumber", str_phone_no);
            jsonObject.put("playby", str_onclick_play_by_value);
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("onclick_values", "" + integerStringMap.toString());

            Log.e("Final_Json_values", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_RESULT_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        tv_end_game_sucess_message.setText(response.body().message);
                        constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                        constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                        constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
                        constraintLayout_end_game_game_two.setBackgroundResource((R.drawable.end_game_grey_bg));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                countDownTimer.cancel();
                                Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                        }, 2000);
                    }
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void performTick(int millisUntilFinished) {
        tv_timer_seconds_count_game_two.setText(String.valueOf(Math.round(millisUntilFinished * 0.001f)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_two_x_game_two:
                n2_new_value = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
                n1_old_value = n2_new_value;
                int_onclcik_2x_power_up = 1;
                Log.e("2x_count", "" + int_2x_onclick_count);
                Toast.makeText(Game_Two_Act.this, "2x_power_up" + int_onclcik_2x_power_up, Toast.LENGTH_SHORT).show();
                if (int_2x_onclick_count == 0) {
                    tv_two_x_game_two.setEnabled(false);
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

//                    tv_two_x_game_two.setVisibility(View.GONE);
//                    tv_2x_power_up.setVisibility(View.GONE);
//                    constraintLayout_tv_2x_power_up.setVisibility(View.GONE);
                } else {
                    int_2x_onclick_count = int_2x_onclick_count - 1;
                    int_2_x_count = 0;
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                    tv_two_x_game_two.setEnabled(false);

                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal));
                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
                }

                    */
/*if (tv_2x_power_up.getText().toString().equalsIgnoreCase("1")) {
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                    tv_two_x_game_two.setEnabled(false);

                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal));
                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
                } else {*//*


                break;
            */
/*For Screen 1*//*

            case R.id.tv_ans_0_game_two:
//                tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(0).substring(2));
//                Log.e("strrrrrrrr_02", tv_ans_0_game_two.getText().toString());


//                tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_effect_rectangle));
//                tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.black_color));
                isCanceled = true;
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_01.get(0).substring(0, 1));
//                str_onclick_answer_selection = tv_ans_0_game_two.getText().toString();
//                Log.e("str_onclick_ans_slctin_01", str_onclick_answer_selection);
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();

                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                Log.e("difference_time", "" + difference_time);


                str_final_answer = Final_Answer_ArrayList.get(0);
                Log.e("str_final_answer_01", str_final_answer);
                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);
                Log.e("onclk_2x_power_up_01", str_onclick_2x_powerup);

                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                Log.e("n1111", "" + n1_1);
                String s11 = Final_Answer_ArrayList.toString();
                int n1 = n1_1 - 1;
                if (Final_Answer_ArrayList.get(n1).equals(str_onclick_answer_selection)) {
                    Log.e("Answer_Final", s11);
                    Log.e("clickanswerselection", str_onclick_answer_selection);
                    tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                    tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_0_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));
                    */
/*if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(100);
                    }*//*

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 2300);
                */
/*if (s11.equals(str_onclick_answer_selection)) {
                    Toast.makeText(this, "correct", Toast.LENGTH_SHORT).show();
                    tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                } else {
                    Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();
                    tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                }*//*



//                Log.e("int_reaming_page_count_value", String.valueOf(n1_1));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup);
                    }
                }, 3800);
                break;
            case R.id.tv_ans_1_game_two:
//                tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(0).substring(2));
//                Log.e("strrrrrrrr_02", tv_ans_1_game_two.getText().toString());
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
//                tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_effect_rectangle));
//                tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.black_color));
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_02.get(0).substring(0, 1));
//                str_onclick_answer_selection = tv_ans_1_game_two.getText().toString();
//                Log.e("str_onclick_ans_slctin_02", str_onclick_answer_selection);
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();
                Log.e("str_onclick_timer_02", str_onclick_time);


                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                Log.e("difference_time", "" + difference_time);

                str_final_answer = Final_Answer_ArrayList.get(0);
                Log.e("str_final_answer_02", str_final_answer);

                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);
//                Log.e("int_onclcik_2x_power_up_02", str_onclick_2x_powerup);

                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
//                Log.e("int_reaming_page_count_value_02", String.valueOf(n1_1));


                Log.e("n1111", "" + n1_1);
                String s12 = Final_Answer_ArrayList.toString();
                int n2 = n1_1 - 1;
                if (Final_Answer_ArrayList.get(n2).equals(str_onclick_answer_selection)) {
                    tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                    tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_1_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));
                    */
/*if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(100);
                    }*//*

                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 2300);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup);
                    }
                }, 3800);
                break;
            case R.id.tv_ans_2_game_two:
//                tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(0).substring(2));
//                Log.e("strrrrrrrr_03", tv_ans_2_game_two.getText().toString());
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
//                tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_effect_rectangle));
//                tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.black_color));
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_03.get(0).substring(0, 1));
//                str_onclick_answer_selection = tv_ans_2_game_two.getText().toString();
//                Log.e("str_onclick_ans_slctin_03", str_onclick_answer_selection)
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();
                Log.e("str_onclick_timer_03", str_onclick_time);

                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                Log.e("difference_time", "" + difference_time);


                str_final_answer = Final_Answer_ArrayList.get(0);
                Log.e("str_final_answer_03", str_final_answer);

                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);
//                Log.e("int_onclcik_2x_power_up_03", str_onclick_2x_powerup);

                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
//                Log.e("int_reaming_page_count_value_03", String.valueOf(n1_1));


                Log.e("n1111", "" + n1_1);
                String s13 = Final_Answer_ArrayList.toString();
                int n3 = n1_1 - 1;
                if (Final_Answer_ArrayList.get(n3).equals(str_onclick_answer_selection)) {
                    tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                    tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_2_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));

                    */
/*if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(100);
                    }*//*

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 2300);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup);
                    }
                }, 3800);
                break;
            case R.id.tv_ans_3_game_two:
//                tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(0).substring(2));
//                Log.e("strrrrrrrr_04", tv_ans_3_game_two.getText().toString());
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
//                tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_effect_rectangle));
//                tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.black_color));
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_04.get(0).substring(0, 1));
//                str_onclick_answer_selection = tv_ans_3_game_two.getText().toString();
//                Log.e("str_onclick_ans_slctin_04", str_onclick_answer_selection);
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();
//                Log.e("str_onclick_timer_04", str_onclick_time);


                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                Log.e("difference_time", "" + difference_time);


                str_final_answer = Final_Answer_ArrayList.get(0);
                Log.e("str_final_answer_04", str_final_answer);

                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);
                Log.e("onclk_2x_power_up_04", str_onclick_2x_powerup);

                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
//                Log.e("int_reaming_page_count_value_04", String.valueOf(n1_1));


                Log.e("n1111", "" + n1_1);
                String s14 = Final_Answer_ArrayList.toString();
                int n4 = n1_1 - 1;
                if (Final_Answer_ArrayList.get(n4).equals(str_onclick_answer_selection)) {
                    tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                    tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.black_color));
                } else {
                    tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_3_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));
                    */
/*if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(100);
                    }*//*

                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 2300);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup);
                    }
                }, 3800);
                break;
            */
/*
                Handler handler_04 = new Handler();
                handler_04.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
*//*

            case R.id.tv_price_btn:
                Get_Prize_List_Details();
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.white_color));
//                constraintLayout_prize_layout.setVisibility(View.VISIBLE);
                rv_price_list_game_two.setVisibility(View.VISIBLE);
                constraintLayout_rules_list.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_rules_btn:
                shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                constraintLayout_rules_list.setVisibility(View.VISIBLE);
                shimmer_view_container_for_rules.startShimmerAnimation();
                Get_Rules_Name_Details();

                mShimmerViewContainer.setVisibility(View.GONE);
                rv_price_list_game_two.setVisibility(View.GONE);
                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.white_color));
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.constraintLayout_watch_ads_btn:
                */
/*//*
/ initializing animation drawable by getting background from constraint layout
                animationDrawable = (AnimationDrawable) tv_just_a_moment.getBackground();
                // setting enter fade animation duration to 5 seconds
                animationDrawable.setEnterFadeDuration(5000);
                // setting exit fade animation duration to 2 seconds
                animationDrawable.setExitFadeDuration(2000);
                animationDrawable.start();*//*

                */
/*This components are used for Rewarded video*//*

                Log.e("clk_inside_entry_free", String.valueOf(int_entry_fee));
                Log.e("clk_inside_balance", String.valueOf(int_db_balance));
                if (int_entry_fee > int_db_balance) {
                    int n11 = int_entry_fee - int_db_balance;
                    Log.e("clk_n1111", String.valueOf(n11));
                    tv_enter_contest_btn.setVisibility(View.GONE);
                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                    constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);

                    tv_earn_coins.setVisibility(View.VISIBLE);
                    tv_play_btn.setVisibility(View.GONE);
                    tv_earn_coins.setText(String.valueOf(n11));
                    tv_coins.setVisibility(View.VISIBLE);
                    Get_Rewarded_Video_Method(savedInstanceState);
                } else {
                    tv_play_btn.setVisibility(View.VISIBLE);
                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                    Get_Enter_Game_Page();
                }
//                Get_Enter_Game_Page();
                break;
            case R.id.tv_enter_contest_btn:
                str_onclick_play_by_value = "Free";
                constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                constraintLayout_rewarded_video.setVisibility(View.VISIBLE);
                Glide.with(this).asGif().load(R.drawable.giphy).into(gif_image);

                */
/*This components are used for Rewarded video*//*

                Get_Rewarded_Video_Method(savedInstanceState);

                */
/*Intent intent2 = new Intent(Game_Two_Act.this, Reward_Video_Act.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);*//*

                break;
            case R.id.tv_enter_contest_btn_for_free:
               */
/* AdLoader.Builder builder = new AdLoader.Builder(Game_Two_Act.this, "ca-app-pub-3940256099942544/2247696110");

                adLoader = builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        adLoader.loadAd(new AdRequest.Builder().build());
                    }
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                // Methods in the NativeAdOptions.Builder class can be
                                // used here to specify individual options settings.
                                .build())
                        .build();
                // Load the Native ads.
                adLoader.loadAds(new AdRequest.Builder().build(), 5);*//*



                showInterstitial();
//                Load_FB_ADS();
                break;
            case R.id.tv_score_board:
                mShimmerViewContainer.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.GONE);
                constraintLayout_score_board_layout.setVisibility(View.VISIBLE);
//                constraintLayout_prize_layout.setVisibility(View.GONE);
                rv_price_list_game_two.setVisibility(View.GONE);
                constraintLayout_rules_list.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
        }
    }

    private void Get_Enter_Game_Page() {
        int int_balance = Integer.parseInt(str_balance);
        int int_minus_value = 50;
        int int_calc = int_balance - int_minus_value;
        ContentValues contentValues = new ContentValues();
        contentValues.put("BALANCE", int_calc);
        Log.e("Content_Values_mob_reg", contentValues.toString());
        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);

        str_onclick_play_by_value = "Playby50";
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, bundle);
        constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
        constraintLayout_just_a_moment_game_two.setVisibility(View.VISIBLE);
        Get_Questions_Details();
        Glide.with(Game_Two_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                constraintLayout_game_screen_in_game_two.setVisibility(View.VISIBLE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    startTimer(milliseconds);
                }
            }
        }, 5000);
        constraintLayout_end_game_game_two.setVisibility(View.GONE);
    }

    */
/*This components are used for showing Rewarded video*//*

    private void Get_Rewarded_Video_Method(Bundle savedInstanceState) {
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                // addCoins(rewardItem.getAmount());
                addCoins(10);
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(), "Ad loaded.", Toast.LENGTH_SHORT).show();
                showRewardedVideo_New();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Ad loaded handler.", Toast.LENGTH_SHORT).show();
                    }
                }, 500);

                tv_loading_tx.setVisibility(View.GONE);
                progress_bar_in_reward_video.setVisibility(View.GONE);
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.e("ad_opened_log", "Ad Opened");
                Toast.makeText(getBaseContext(), "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.e("ad_started_log", "Ad started");
                Toast.makeText(getBaseContext(), "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                recreate();
                int int_n1, int_n2, int_n3;
                String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
                Cursor cursor = db.rawQuery(select, null);
                if (cursor.moveToFirst()) {
                    do {
                        str_balance = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                int_n1 = Integer.parseInt(str_balance);

                Log.e("ad_closed_log", "Ad closed");
                Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();

                Log.e("coin_count", mCoinCountText.getText().toString());
                int_n2 = Integer.parseInt(mCoinCountText.getText().toString());
                int_n3 = int_n1 + int_n2;


                ContentValues contentValues = new ContentValues();
                contentValues.put("BALANCE", int_n3);
                db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                Log.e("Content_Values_mob_reg", contentValues.toString());
                DBEXPORT();
                Toast_Message_For_Reward.showToastMessage(Game_Two_Act.this, "You have earned " + String.valueOf(int_n2) + " coins from video," + "\n Your current balance point is " + String.valueOf(int_n3) + ".");
                if (int_entry_fee > int_n3) {
                    recreate();
                    int n11 = int_entry_fee - int_n3;
                    tv_earn_coins.setText(String.valueOf(n11));
                    Log.e("int_entry_fee_ad", "" + int_entry_fee);
                    Log.e("n1111", "" + n11);
                    Log.e("n2222", "" + int_n2);
                    Log.e("n3333", "" + int_n3);
                    Toast.makeText(context, "if_side", Toast.LENGTH_SHORT).show();
                } else {
                    recreate();
                    Toast.makeText(context, "else_side", Toast.LENGTH_SHORT).show();
                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                    tv_play_btn.setVisibility(View.VISIBLE);
                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                }

                if (int_n2 != 10) {
//                    Log.e("ifff","method");
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
                    constraintLayout_rewarded_video.setVisibility(View.GONE);
                    constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                    constraintLayout_end_game_game_two.setVisibility(View.GONE);

                } */
/*else {
//                    Log.e("Elseee","method");
                    constraintLayout_rewarded_video.setVisibility(View.GONE);
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                    constraintLayout_just_a_moment_game_two.setVisibility(View.VISIBLE);

                    Glide.with(Game_Two_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
                    Get_Questions_Details();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            constraintLayout_game_screen_in_game_two.setVisibility(View.VISIBLE);
                            if (!isNetworkAvaliable()) {
                                registerInternetCheckReceiver();
                            } else {
                                startTimer(milliseconds);
                            }
                        }
                    }, 5500);
                    constraintLayout_end_game_game_two.setVisibility(View.GONE);
                }
*//*


            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
                Log.e("ad_left_log", "Ad left application");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
                Log.e("Failed_log", "Ad failed to load.");
            }

            @Override
            public void onRewardedVideoCompleted() {
            }
        });

        if (savedInstanceState == null) {
            mCoinCount = 0;
            mCoinCountText.setText(String.valueOf(mCoinCount));
            startGame_Rewarded_Video();
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

    */
/*This components are used for Rewarded video*//*

    private void startGame_Rewarded_Video() {
        mGamePaused = false;
        mGameOver = false;
        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }

    */
/*This components are used for Rewarded video*//*

    private void showRewardedVideo_New() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    */
/*This components are used for Rewarded video*//*

    private void addCoins(int coins) {
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText(String.valueOf(mCoinCount));
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            startGame();
        }
    }

    private void Screen_01_Method(int int_reaming_page_count_value, String str_onclick_answer_selection, String str_onclick_time, String str_onclick_2x_powerup) {
        if (int_2x_onclick_count == 0) {
            tv_two_x_game_two.setEnabled(false);
            tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
            constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
            tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
            tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
        } else {
            tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
            tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
            tv_two_x_game_two.setEnabled(true);
        }

        dialog_fr_timer.dismiss();
        Log.e("2x_Onclick_value", str_onclick_2x_powerup);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            constraintLayout_count_down_timer.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.stop_animation_shake));
            constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
            startTimer(milliseconds);
        }
        constraintLayout_answer_set_01.setVisibility(View.VISIBLE);
        str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
        int n1_1 = Integer.parseInt(str_remaining_count_value);
        Log.e("int_number_of_questions", "" + int_number_of_questions);
        Log.e("qstnIntrAryLstSzeonclk", "" + questionsIntegerArrayList.size());
        Log.e("n1_111", "" + n1_1);
        Log.e("int_number_of_questions", "" + int_number_of_questions);
        Log.e("n1_1", "" + n1_1);


        if (n1_1 < int_number_of_questions) {
            if (countDownTimer == null) {
                countDownTimer.start();
            }
            int n2 = n1_1 + 1;
            tv_remaing_count_value_in_game_two.setText(String.valueOf(n2));
            tv_questions_in_game_two.setText(questionsIntegerArrayList.get(n1_1));
            String s1 = tv_questions_in_game_two.getText().toString();
            Log.e("s111", s1);
            Toast.makeText(this, "no.of.count" + s1, Toast.LENGTH_SHORT).show();
            if (answersIntegerArrayList_01.get(0).length() >= 50 || answersIntegerArrayList_02.get(0).length() >= 50
                    || answersIntegerArrayList_03.get(0).length() >= 50 || answersIntegerArrayList_04.get(0).length() >= 50) {

                tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(n1_1).substring(2));
                tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(n1_1).substring(2));
                tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(n1_1).substring(2));
                tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(n1_1).substring(2));

                tv_ans_0_game_two.setTextSize(13);
                tv_ans_1_game_two.setTextSize(13);
                tv_ans_2_game_two.setTextSize(13);
                tv_ans_3_game_two.setTextSize(13);
            } else {

                tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(n1_1).substring(2));
                tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(n1_1).substring(2));
                tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(n1_1).substring(2));
                tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(n1_1).substring(2));

            }


            tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
            tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
            tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
            tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
            tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.white_color));
            tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.white_color));
            tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.white_color));
            tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.white_color));

            if (int_onclcik_2x_power_up == 1) {
                int_onclcik_2x_power_up = 0;
            }
            questionnumber_ArrayList.add(String.valueOf(int_reaming_page_count_value));
            answerselection_ArrayList.add(str_onclick_answer_selection);
            timer_ArrayList.add(str_onclick_time);
            _2x_power_up_ArrayList.add(str_onclick_2x_powerup);

            Log.e("qstnnum_AryLst", questionnumber_ArrayList.toString());
            Log.e("anslctn_ArrayList", answerselection_ArrayList.toString());
            Log.e("timer_ArrayList", timer_ArrayList.toString());
            Log.e("_2x_power_up_ArrayList", _2x_power_up_ArrayList.toString());

        }
        if (n1_1 == int_number_of_questions) {
            questionnumber_ArrayList.add(String.valueOf(int_reaming_page_count_value));
            answerselection_ArrayList.add(str_onclick_answer_selection);
            timer_ArrayList.add(str_onclick_time);
            _2x_power_up_ArrayList.add(str_onclick_2x_powerup);
            Log.e("_10_qstnnum_AryLst", "" + questionnumber_ArrayList.toString());
            Log.e("_10_ansltin_AryLst", answerselection_ArrayList.toString());
            Log.e("_10_timer_AryLst", timer_ArrayList.toString());
            Log.e("_10__2x_power_up_AryLst", _2x_power_up_ArrayList.toString());

            Getting_Final_Details();
            Getting_Update_Status_Details();

        }
    }

    private void Getting_Update_Status_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("status", "1");
            Log.e("update_json_response", jsonObject.toString());
            APIInterface apiInterface = Factory_For_Categories.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
//                        tv_end_game_sucess_message.setText(response.body().message);
                        constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                        constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                        constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
                        constraintLayout_end_game_game_two.setBackgroundResource((R.drawable.end_game_grey_bg));
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

    */
/*This method is used for network connectivity*//*

    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
    }

    */
/*This method automatically detect whether the internet is available or not
     * if internet in not available GetDrawTiming,GetBalanceDetails will get stop
     * *//*

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    */
/**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     *//*

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (countDownTimer != null) {
            dialog = new Dialog(Game_Two_Act.this);
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
                    int n1 = timer_ArrayList.size();

                    */
/*this following lines are used for setting unanswered questions for "0"*//*

                    int int_current_question_num = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
                    int n3 = int_number_of_questions - int_current_question_num;
                    Log.e("crnt_question_num", "" + int_current_question_num);
                    Log.e("int_number_of_questions", "" + int_number_of_questions);
                    Log.e("differencevalue", "" + n3);
                    for (int i = 0; i <= n3; i++) {
                        timer_ArrayList.add("0");
                        answerselection_ArrayList.add("Skip");
                        _2x_power_up_ArrayList.add("0");
                    }
                    Log.e("answerselection_yes", answerselection_ArrayList.toString());
                    Getting_Final_Details();
                    Getting_Update_Status_Details();

                    dialog.dismiss();
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
//                    System.exit(0);
                            finish();
                        }
                    }, 1000);
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } else {
          */
/*  Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();*//*

//          System.exit(0);

            Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_remaing_count_value_in_game_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                n3_normal_value = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
//                Log.e("onResumemmee", "" + n3_normal_value);
            }
        });

        mShimmerViewContainer.startShimmerAnimation();

        */
/*This components are used for Rewarded video*//*

        if (!mGameOver && mGamePaused) {
            resumeGame();
        }
        IronSource.onResume(Game_Two_Act.this);
//        mRewardedVideoAd.resume(Game_Two_Act.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
        */
/*This components are used for Rewarded video*//*

        pauseGame();
        IronSource.onPause(this);
        mRewardedVideoAd.pause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
        */
/*This components are used for Rewarded video*//*

        mRewardedVideoAd.destroy(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    */
/*This components are used for Rewarded video*//*

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mGamePaused = savedInstanceState.getBoolean(GAME_PAUSE_KEY);
        mGameOver = savedInstanceState.getBoolean(GAME_OVER_KEY);
        mTimeRemaining = savedInstanceState.getLong(TIME_REMAINING_KEY);
        mCoinCount = savedInstanceState.getInt(COIN_COUNT_KEY);
        mCoinCountText.setText(String.valueOf(mCoinCount));
    }

    */
/*This components are used for Rewarded video*//*

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(GAME_PAUSE_KEY, mGamePaused);
        outState.putBoolean(GAME_OVER_KEY, mGameOver);
        outState.putLong(TIME_REMAINING_KEY, mTimeRemaining);
        outState.putInt(COIN_COUNT_KEY, mCoinCount);
        super.onSaveInstanceState(outState);
    }

    */
/*This components are used for Rewarded video*//*

    private void pauseGame() {
        mGamePaused = true;
    }

    */
/*This components are used for Rewarded video*//*

    private void resumeGame() {
        mGamePaused = false;
    }
}

*/

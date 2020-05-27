package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import com.google.gson.Gson;
import com.ironsource.mediationsdk.IronSource;
import com.makeramen.roundedimageview.RoundedImageView;
import com.spot_the_ballgame.Adapter.ContestAdapter_For_End_Game;
import com.spot_the_ballgame.Adapter.Price_List_Adapter_Game_Two;
import com.spot_the_ballgame.Adapter.Ranking_List_Adapter_Game_Two;
import com.spot_the_ballgame.Adapter.Rules_List_Name_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Model.Winnings_Model;
import com.tomer.fadingtextview.FadingTextView;

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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_Two_Act extends AppCompatActivity implements View.OnClickListener {
    Fragment fragment;
    SQLiteDatabase db;
    ConstraintLayout constraintLayout_just_a_moment_game_two,
            constraintLayout_end_game_game_two,
            constraintLayout_game_screen_in_game_two,
            constraintLayout_answer_set_01,
            constraintLayout_game_details_screen_in_game_two,
            constraintLayout_count_down_timer,
            constraintLayout_rewarded_video,
            constraintLayout_watch_ads_btn_inside,
            constraintLayout_watch_ads_btn,
            constraintLayout_tv_2x_power_up,
            constraintLayout_score_board_top_layout;
    TextView tv_timer_seconds_count_game_two,
            tv_timer_seconds_txt_game_two,
            tv_two_x_game_two,
            tv_remaing_count_value_in_game_two,
            tv_questions_in_game_two,
            tv_ans_0_game_two,
            tv_ans_1_game_two,
            tv_ans_2_game_two,
            tv_ans_3_game_two,
            tv_winnings_btn,
            tv_rules_btn,
            tv_watch_ads_btn,
            tv_coins,
            tv_enter_contest_btn,
            tv_score_board,
            tv_enter_contest_btn_for_free,
            tv_earn_coins, tv_play_btn,
            tv_loading_tx,
            tv_close_end_game,
            tv_game_name,
            tv_2x_power_up,
            tv_time_limit_sec_txt,
            tv_entry_fee_details,
            tv_prize_amount_game_details,
            tv_game_end_time_game_details,
            tv_number_of_questions_in_api,
            tv_skip_points_values,
            tv_wrong_ans_values,
            tv_correct_ans_values,
            tv_current_user_rank,
            tv_no_data_available_fr_score_board;

    ImageView iv_ready_steady_go_state,
            iv_end_game_sucess_message_gif,
            gif_image,
            iv_categoires_image_game_two_act;

    RecyclerView rv_price_list_game_two,
            rv_single_card_end_game,
            rv_rules_list_game_two,
            rv_score_board_list_game_two;

    CountDownTimer countDownTimer_game_details, countDownTimer;

    Dialog dialog, dialog_fr_timer;

    Price_List_Adapter_Game_Two price_list_adapter_game_two;
    Ranking_List_Adapter_Game_Two ranking_list_adapter_game_two;
    Rules_List_Name_Adapter rules_list_name_adapter;
    ContestAdapter_For_End_Game contestAdapter_for_end_game;

    String str_playby,
            str_game_name,
            str_prize_amount,
            str_end_time,
            str_start_time,
            str_code,
            str_message,
            str_wallet1,
            str_wallet2,
            str_2x_powerup,
            str_seconds,
            str_contest_id,
            str_entry_fees,
            str_onclick_answer_selection,
            str_onclick_string_answer_selection,
            str_onclick_time,
            str_onclick_2x_powerup,
            str_final_answer,
            question_count,
            str_difference_time,
            str_rule_id,
            str_status_onclick,
            str_imagepath,
            str_local_host,
            str_connect_host_image,
            str_connect_host_image_01,
            str_connect_host_image_02,
            str_team_a_path_name_txt,
            str_team_b_path_name_txt,
            str_correct_ans,
            str_wrong_ans,
            str_skip,
            str_phone_no,
            str_email,
            str_team_a_path,
            str_team_b_path,
            str_option_a,
            str_option_b,
            str_option_c,
            str_option_d;
    String str_remaining_count_value = "";
    String str_onclick_play_by_value = "";

    int int_db_balance_for_reward_video,
            int_rewarded_coins_point,
            int_total_value,
            int_entry_fee,
            int_db_balance,
            n1_1,
            n1_old_value,
            n2_new_value,
            n3_normal_value,
            int_str_seconds,
            int_str_onclick_time,
            difference_time,
            int_number_of_questions,
            int_previous_or_courrent_question_number,
            int_correct_ans,
            int_wrong_ans,
            int_skip,
            int_skip_else,
            int_correct_ans_notations,
            int_duplicate_total_value,
            int_duplicate_corect_answer_value;
    int int_count_value = 0;
    int int_reaming_page_count_value = 1;
    int int_2x_onclick_count = 0;
    //    This variable is used for status check whether 2x is enabled or not.
    int int_onclcik_2x_power_up = 0;
    int int_onclcik_2x_enable_disable;
    int int_play_status = 0;
    double double_db_balance;

    long different_milli_seconds,
            lng_seconds,
            milliseconds,
            seconds;

    Date current_system_time, end_date_api;

    ArrayList<Integer> total_onclick_correct_answer_values_ArrayList = new ArrayList<>();
    ArrayList<Integer> total_onclick_wrong_answer_values_ArrayList = new ArrayList<>();
    ArrayList<String> total_all_onclick_point_values_ArrayList = new ArrayList<>();
    ArrayList<String> total_onclick_time_ArrayList = new ArrayList<>();
    ArrayList<Integer> total_skip_values_ArrayList = new ArrayList<>();
    ArrayList<String> answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_correct_answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_onclick_time_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_onclick_point_ArrayList = new ArrayList<>();
    ArrayList<String> answers_string_velue_selection_ArrayList = new ArrayList<>();
    ArrayList<String> questionsIntegerArrayList = new ArrayList<>();
    ArrayList<String> imagequestionsIntegerArrayList = new ArrayList<>();
    ArrayList<String> questionnumber_ArrayList = new ArrayList<>();
    ArrayList<String> _2x_power_up_ArrayList = new ArrayList<>();
    ArrayList<String> _2x_power_up_continous_update_to_api_ArrayList = new ArrayList<>();
    ArrayList<String> Final_Answer_ArrayList = new ArrayList<>();
    ArrayList<String> timer_ArrayList = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_01 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_02 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_03 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_04 = new ArrayList<>();
    ArrayList<String> hintArrayList = new ArrayList<>();

    HashMap<String, String> hashMap = new HashMap<>();
    HashMap<Integer, String> finalhashMap = new HashMap<>();

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    ProgressBar progressBar;


    Handler handler;
    AnimationDrawable animationDrawable;
    Vibrator vibrator;
    Context context;
    private ShimmerLayout shimmer_view_container_winning_list_game_two, shimmer_view_container_for_rules, shimmer_view_container_for_scoreboard;
    private AdView mAdView, mAdView_end_game;
    Bundle bundle;
    private InterstitialAd interstitialAd;
    /*This unit id using google test mail id*/
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    /*This unit id using skyrandtech mail id*//*
    private static final String AD_UNIT_ID = "ca-app-pub-7961776813129160/4130614251";*/
    //    private static final String AD_UNIT_ID = "ca-app-pub-7961776813129160~6949709129";
    AdLoader adLoader;
    FirebaseAnalytics mFirebaseAnalytics;
    private final String TAG = Game_Two_Act.class.getSimpleName();
    com.facebook.ads.InterstitialAd interstitialAd_fb;
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
    private static final String TAG1 = "SyncService";

    TextView tv_hint_text, tv_play_anoter_contest;
    ImageView iv_hint_icon;
    FadingTextView tv_just_a_moment;
    String str_auth_token,
            str_session_reward_amount,
            str_nav_curnt_amnt,
            str_categories,
            str_question_type,
            str_prize_type,
            str_session_scoreboard_value,
            str_session_scoreboard_current_user_rank_value;
    int int_tv_points;
    ArrayList<Winnings_Model> winning_arrayList;
    ConstraintLayout constraintLayout_country_flag_layout;
    ImageView iv_first_country_flag, iv_second_country_flag;
    TextView tv_first_country_txt, tv_second_country_txt;

    ConstraintLayout constraintLayout_game_two_top_layout, constraintLayout_trivia_text__plus_hint, constraintLayout_image_plus_text_contest;
    TextView tv_prize_pool_details_icon, tv_entry_fee_details_icon;
    RoundedImageView iv_changing_image;
    TextView tv_questions_in_image_plus_txt;

    boolean is_2x_btn_clicked = false;
    int int_2x_onclick_or_not = 0;
    String str_crnt_user_rank = "";

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_for_image_03);

        getSupportActionBar().hide();
        db = Objects.requireNonNull(Game_Two_Act.this).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);

        str_auth_token = SessionSave.getSession("Token_value", Game_Two_Act.this);
//        Log.e("authtoken_game2", str_auth_token);

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
        /*This components are used for Rewarded video*/
        tv_loading_tx = findViewById(R.id.tv_loading_txt);
        gif_image = findViewById(R.id.gif_image);
        iv_categoires_image_game_two_act = findViewById(R.id.iv_categoires_image_game_two_act);
        Glide.with(this).asGif().load(R.drawable.giphy).into(gif_image);
        progress_bar_in_reward_video = findViewById(R.id.progress_bar_in_reward_video);
        constraintLayout_country_flag_layout = findViewById(R.id.constraintLayout_country_flag_layout);
        tv_first_country_txt = findViewById(R.id.tv_first_country_txt);
        tv_second_country_txt = findViewById(R.id.tv_second_country_txt);
        iv_first_country_flag = findViewById(R.id.iv_first_country_flag);
        iv_second_country_flag = findViewById(R.id.iv_second_country_flag);
        tv_prize_pool_details_icon = findViewById(R.id.tv_prize_pool_details_icon);
        tv_entry_fee_details_icon = findViewById(R.id.tv_entry_fee_details_icon);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mCoinCountText = findViewById(R.id.coin_count_text);
        tv_close_end_game = findViewById(R.id.tv_close_end_game);
        constraintLayout_game_two_top_layout = findViewById(R.id.constraintLayout_game_two_top_layout);
        constraintLayout_trivia_text__plus_hint = findViewById(R.id.constraintLayout_trivia_text__plus_hint);
        constraintLayout_image_plus_text_contest = findViewById(R.id.constraintLayout_image_plus_text_contest);
        iv_changing_image = findViewById(R.id.iv_changing_image);
        tv_questions_in_image_plus_txt = findViewById(R.id.tv_questions_in_image_plus_txt);
        constraintLayout_image_plus_text_contest = findViewById(R.id.constraintLayout_image_plus_text_contest);
        tv_close_end_game.setText(R.string.go_back_txt);
        //Ironsource_code
        IronSource.setConsent(true);
        //This is for ad colony
        AdColony.configure(this, "appd9b3bb873a744248bd", "vz09df69e0202642b88a");


        str_session_reward_amount = SessionSave.getSession("Reward_Point", Game_Two_Act.this);
        str_session_scoreboard_value = SessionSave.getSession("ScoreBoard_Value", Game_Two_Act.this);
        str_session_scoreboard_current_user_rank_value = SessionSave.getSession("ScoreBoard_User_Rank_Value", Game_Two_Act.this);

        tv_timer_seconds_count_game_two = findViewById(R.id.tv_timer_seconds_count_game_two);
        tv_timer_seconds_txt_game_two = findViewById(R.id.tv_timer_seconds_txt_game_two);
        tv_two_x_game_two = findViewById(R.id.tv_two_x_game_two);
        tv_remaing_count_value_in_game_two = findViewById(R.id.tv_remaing_count_value_in_game_two);
        tv_questions_in_game_two = findViewById(R.id.tv_questions_in_game_two);
        tv_just_a_moment = findViewById(R.id.tv_just_a_moment);

        tv_hint_text = findViewById(R.id.tv_hint_text);
        iv_hint_icon = findViewById(R.id.iv_hint_icon);
        tv_play_anoter_contest = findViewById(R.id.tv_play_anoter_contest);

        tv_game_name = findViewById(R.id.tv_game_name);
        tv_2x_power_up = findViewById(R.id.tv_2x_power_up);
        tv_time_limit_sec_txt = findViewById(R.id.tv_time_limit_sec_txt);
        tv_entry_fee_details = findViewById(R.id.tv_entry_fee_details);
        tv_game_end_time_game_details = findViewById(R.id.tv_game_end_time_game_details);
        tv_prize_amount_game_details = findViewById(R.id.tv_prize_amount_game_details);
        constraintLayout_tv_2x_power_up = findViewById(R.id.constraintLayout_tv_2x_power_up);
        tv_number_of_questions_in_api = findViewById(R.id.tv_number_of_questions_in_api);

        tv_skip_points_values = findViewById(R.id.tv_skip_points_values);
        tv_wrong_ans_values = findViewById(R.id.tv_wrong_ans_values);
        tv_correct_ans_values = findViewById(R.id.tv_correct_ans_values);
        tv_current_user_rank = findViewById(R.id.tv_current_user_rank);
        tv_no_data_available_fr_score_board = findViewById(R.id.tv_no_data_available_fr_score_board);
        constraintLayout_score_board_top_layout = findViewById(R.id.constraintLayout_score_board_top_layout);


        iv_ready_steady_go_state = findViewById(R.id.iv_ready_steady_go_state);
        iv_end_game_sucess_message_gif = findViewById(R.id.iv_end_game_sucess_message_gif);

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

        shimmer_view_container_winning_list_game_two = findViewById(R.id.shimmer_view_container_winning_list_game_two);
        shimmer_view_container_for_rules = findViewById(R.id.shimmer_view_container_for_rules);
        shimmer_view_container_for_scoreboard = findViewById(R.id.shimmer_view_container_for_scoreboard);
        rv_single_card_end_game = findViewById(R.id.rv_single_card_end_game);

        rv_price_list_game_two = findViewById(R.id.rv_price_list_game_two);
        rv_price_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        rv_rules_list_game_two = findViewById(R.id.rv_rules_list_game_two);
        rv_rules_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        rv_score_board_list_game_two = findViewById(R.id.rv_score_board_list_game_two);
        rv_score_board_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        rv_single_card_end_game.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_single_card_end_game.setHasFixedSize(true);
        rv_single_card_end_game.setVisibility(View.GONE);


        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dialog_fr_timer = new Dialog(Game_Two_Act.this);
        dialog_fr_timer.setContentView(R.layout.please_wait_lay);
        Objects.requireNonNull(dialog_fr_timer.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        /*These components are for Quiz game screen*/
        tv_ans_0_game_two = findViewById(R.id.tv_ans_0_game_two);
        tv_ans_1_game_two = findViewById(R.id.tv_ans_1_game_two);
        tv_ans_2_game_two = findViewById(R.id.tv_ans_2_game_two);
        tv_ans_3_game_two = findViewById(R.id.tv_ans_3_game_two);
        constraintLayout_answer_set_01 = findViewById(R.id.constraintLayout_answer_set_01);
        constraintLayout_just_a_moment_game_two = findViewById(R.id.constraintLayout_just_a_moment_game_two);
        constraintLayout_end_game_game_two = findViewById(R.id.constraintLayout_end_game_game_two);
        constraintLayout_game_screen_in_game_two = findViewById(R.id.constraintLayout_game_screen_in_game_two);

        /*These components are for game details screen*/
        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
//        constraintLayout_rules_list = findViewById(R.id.constraintLayout_rules_list);
        constraintLayout_count_down_timer = findViewById(R.id.constraintLayout_count_down_timer);
        tv_winnings_btn = findViewById(R.id.tv_winnings_btn);
        tv_rules_btn = findViewById(R.id.tv_rules_btn);
        tv_watch_ads_btn = findViewById(R.id.tv_watch_ads_btn);
        tv_earn_coins = findViewById(R.id.tv_earn_coins);
        tv_play_btn = findViewById(R.id.tv_play_btn);
        tv_coins = findViewById(R.id.tv_coins);
        constraintLayout_watch_ads_btn = findViewById(R.id.constraintLayout_watch_ads_btn);
        tv_enter_contest_btn = findViewById(R.id.tv_enter_contest_btn);
        tv_enter_contest_btn_for_free = findViewById(R.id.tv_enter_contest_btn_for_free);

//        constraintLayout_score_board_layout = findViewById(R.id.constraintLayout_score_board_layout);
        tv_score_board = findViewById(R.id.tv_score_board);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), android.graphics.PorterDuff.Mode.SRC_IN);
        tv_questions_in_game_two.setMovementMethod(new ScrollingMovementMethod());
        tv_questions_in_image_plus_txt.setMovementMethod(new ScrollingMovementMethod());
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
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
            str_start_time = bundle.getString("start_time");
            str_rule_id = bundle.getString("str_rule_id");
            str_status_onclick = bundle.getString("str_status_onclick");
            str_imagepath = bundle.getString("str_imagepath");
            str_team_a_path = bundle.getString("str_team_a_path");
            str_team_b_path = bundle.getString("str_team_b_path");

            str_team_a_path_name_txt = bundle.getString("str_team_a_path_name_txt");
            str_team_b_path_name_txt = bundle.getString("str_team_b_path_name_txt");

            str_correct_ans = bundle.getString("str_correct_ans");
            str_wrong_ans = bundle.getString("str_wrong_ans");
            str_skip = bundle.getString("str_skip");
            str_categories = bundle.getString("str_categories");
            str_question_type = bundle.getString("str_question_type");
            str_prize_type = bundle.getString("prize_type");

//            Log.e("str_end_time_game_2", str_end_time);
        }
        if (str_prize_type.equalsIgnoreCase("Coin")) {
            tv_prize_pool_details_icon.setBackground(getResources().getDrawable(R.drawable.coin_new));
            tv_entry_fee_details_icon.setBackground(getResources().getDrawable(R.drawable.coin_new));
        } else if (str_prize_type.equalsIgnoreCase("Cash") || str_prize_type.equalsIgnoreCase("Fee")) {
            tv_prize_pool_details_icon.setBackground(getResources().getDrawable(R.drawable.rupee_indian));
            tv_entry_fee_details_icon.setBackground(getResources().getDrawable(R.drawable.rupee_indian));
        }

        /*
         *
         * 0-->Text
         * 1-->Image
         * 2-->Audio
         * 3-->Prediction
         * 4-->STB
         *
         * */
//        if (str_categories.equalsIgnoreCase("Trivia") || str_categories.equalsIgnoreCase("Prediction")) {
        if (str_question_type.equals("0") || str_question_type.equals("3")) {
            constraintLayout_image_plus_text_contest.setVisibility(View.GONE);
            constraintLayout_trivia_text__plus_hint.setVisibility(View.VISIBLE);
        }
        /*
         *
         * 0-->Text
         * 1-->Image
         * 2-->Audio
         * 3-->Prediction
         * 4-->STB
         *
         * */
//        if (str_categories.equalsIgnoreCase("Trivia_Image")) {
        if (str_question_type.equals("1")) {
            constraintLayout_trivia_text__plus_hint.setVisibility(View.GONE);
            constraintLayout_image_plus_text_contest.setVisibility(View.VISIBLE);
        }

        /*
         *
         * 0-->Text
         * 1-->Image
         * 2-->Audio
         * 3-->Prediction
         * 4-->STB
         *
         * */
//        if (str_categories.equalsIgnoreCase("Prediction")) {
        if (str_question_type.equals("3")) {
            constraintLayout_country_flag_layout.setVisibility(View.VISIBLE);
            tv_game_name.setVisibility(View.GONE);

            str_connect_host_image_01 = str_local_host + "" + str_team_a_path;
            Glide.with(Game_Two_Act.this).load(str_connect_host_image_01)
                    .thumbnail(Glide.with(Game_Two_Act.this).load(str_connect_host_image_01))
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_first_country_flag);

            str_connect_host_image_02 = str_local_host + "" + str_team_b_path;
            Glide.with(Game_Two_Act.this).load(str_connect_host_image_02)
                    .thumbnail(Glide.with(Game_Two_Act.this).load(str_connect_host_image_02))
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_second_country_flag);
            tv_first_country_txt.setText(str_team_a_path_name_txt);
            tv_second_country_txt.setText(str_team_b_path_name_txt);
        } else {
            tv_game_name.setVisibility(View.VISIBLE);
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
            tv_enter_contest_btn.setVisibility(View.GONE);
            tv_watch_ads_btn.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
        }

        /*
         *
         * 0-->Text
         * 1-->Image
         * 2-->Audio
         * 3-->Prediction
         * 4-->STB
         *
         * */
//        if (str_categories.equalsIgnoreCase("Prediction")) {
        if (str_question_type.equals("3")) {
            tv_hint_text.setVisibility(View.VISIBLE);
            iv_hint_icon.setVisibility(View.VISIBLE);
        } else {
            tv_hint_text.setVisibility(View.GONE);
            iv_hint_icon.setVisibility(View.GONE);
        }


        if (str_status_onclick.equals("2")) {
            tv_enter_contest_btn_for_free.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
            constraintLayout_watch_ads_btn.setVisibility(View.GONE);
            tv_enter_contest_btn.setVisibility(View.GONE);
        }


        int_2x_onclick_count = Integer.parseInt(str_2x_powerup);
        tv_2x_power_up.setText(str_2x_powerup);
        tv_time_limit_sec_txt.setText(str_seconds + "s");

        tv_game_name.setText(str_game_name);
        tv_prize_amount_game_details.setText(str_prize_amount);
        tv_entry_fee_details.setText(String.valueOf(str_entry_fees));
        str_connect_host_image = str_local_host + "" + str_imagepath;
        Glide.with(Game_Two_Act.this).load(str_connect_host_image)
                .thumbnail(Glide.with(Game_Two_Act.this).load(str_connect_host_image))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_categoires_image_game_two_act);

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

                @Override
                public void onAdClosed() {
                    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
                    int_play_status = 1;
                    Getting_Update_Status_Details_Initial(int_play_status);
                    startGame();
                    mAdView.setVisibility(View.GONE);
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                    constraintLayout_just_a_moment_game_two.setVisibility(View.VISIBLE);
                    tv_just_a_moment.restart();
                    Glide.with(Game_Two_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
                    Get_Questions_Details();
//                    Get_User_Wallet_Details();
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
                    }, 3500);
                    constraintLayout_end_game_game_two.setVisibility(View.GONE);
                }
            });

        }
        try {
            milliseconds = Long.parseLong(str_seconds) * 1000 + 1;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }


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
            shimmer_view_container_winning_list_game_two.startShimmerAnimation();
            Get_Winning_List_Details();
        }
        tv_two_x_game_two.setOnClickListener(this);
        tv_ans_0_game_two.setOnClickListener(this);
        tv_ans_1_game_two.setOnClickListener(this);
        tv_ans_2_game_two.setOnClickListener(this);
        tv_ans_3_game_two.setOnClickListener(this);
        tv_close_end_game.setOnClickListener(this);


        tv_winnings_btn.setOnClickListener(this);
        tv_rules_btn.setOnClickListener(this);
        constraintLayout_watch_ads_btn.setOnClickListener(this);
        tv_enter_contest_btn.setOnClickListener(this);
        tv_enter_contest_btn_for_free.setOnClickListener(this);
        tv_score_board.setOnClickListener(this);
        constraintLayout_end_game_game_two.setVisibility(View.GONE);
//        FullScreenMethod();
        handler = new Handler();
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
                                int n11 = int_entry_fee - int_tv_points;
//                                Log.e("n1111", String.valueOf(n11));
                                tv_enter_contest_btn.setVisibility(View.GONE);
                                if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                    tv_earn_coins.setVisibility(View.GONE);
                                } else if (str_status_onclick.equals("0")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
                                    tv_earn_coins.setVisibility(View.VISIBLE);
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
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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
                                if (str_status_onclick.equals("2")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                                    tv_earn_coins.setVisibility(View.GONE);
                                } else if (str_status_onclick.equals("0")) {
                                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                                    constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
                                    tv_earn_coins.setVisibility(View.VISIBLE);
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
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    private void Load_FB_ADS() {
        interstitialAd_fb.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
//                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
//                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
//                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
//                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
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
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_RULES_NAME_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().data.isEmpty()) {
                            rv_rules_list_game_two.setVisibility(View.GONE);
                            shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                            shimmer_view_container_for_rules.startShimmerAnimation();
                        } else {
                            rv_rules_list_game_two.setVisibility(View.VISIBLE);
                            rv_price_list_game_two.setVisibility(View.GONE);
                            rv_score_board_list_game_two.setVisibility(View.GONE);
                            constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                            shimmer_view_container_for_rules.stopShimmerAnimation();
                            shimmer_view_container_for_rules.setVisibility(View.GONE);
                            rules_list_name_adapter = new Rules_List_Name_Adapter(getApplicationContext(), response.body().data);
                            rv_rules_list_game_two.setAdapter(rules_list_name_adapter);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Two_Act.this);
                //This is for SK
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.2.3/raffle/api/v1/get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                //This is for skyrand
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Factory.BASE_URL_MOBILE_APP + "get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_object = (JSONObject) jsonArray.get(i);
                                String question = json_object.getString("question");
                                String image_photo = json_object.getString("image_photo");
                                str_option_a = json_object.getString("option_a");
                                str_option_b = json_object.getString("option_b");
                                str_option_c = json_object.getString("option_c");
                                str_option_d = json_object.getString("option_d");
                                String answer = json_object.getString("answer");
                                String hint = json_object.getString("hint");
                                question_count = json_object.getString("question_count");
                                tv_number_of_questions_in_api.setText(question_count);
                                String s1 = tv_number_of_questions_in_api.getText().toString();
                                int_number_of_questions = Integer.parseInt(s1);

                                /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
                                if (str_option_a.equals("empty_option")) {
                                    tv_ans_0_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_0_game_two.setVisibility(View.VISIBLE);
                                }
                                if (str_option_b.equals("empty_option")) {
                                    tv_ans_1_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_1_game_two.setVisibility(View.VISIBLE);
                                }
                                if (str_option_c.equals("empty_option")) {
//                                    Toast.makeText(Game_Two_Act.this, "Get_Question_if", Toast.LENGTH_SHORT).show();
                                    tv_ans_2_game_two.setVisibility(View.GONE);
                                } else {
//                                    Toast.makeText(Game_Two_Act.this, "Get_Question_else", Toast.LENGTH_SHORT).show();
                                    tv_ans_2_game_two.setVisibility(View.VISIBLE);
                                }
                                if (str_option_d.equals("empty_option")) {
                                    tv_ans_3_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_3_game_two.setVisibility(View.VISIBLE);
                                }
//                                Log.e("hint_json", hint);
//                                Log.e("image_photo_json", image_photo);
                                questionsIntegerArrayList.add(question);
                                String s11 = str_local_host + image_photo;
//                                Log.e("s1111_json", s11);
                                imagequestionsIntegerArrayList.add(s11);
                                answersIntegerArrayList_01.add(str_option_a);
                                answersIntegerArrayList_02.add(str_option_b);
                                answersIntegerArrayList_03.add(str_option_c);
                                answersIntegerArrayList_04.add(str_option_d);
                                Final_Answer_ArrayList.add(answer);
                                hintArrayList.add(hint);
                                /*
                                 *
                                 * 0-->Text
                                 * 1-->Image
                                 * 2-->Audio
                                 * 3-->Prediction
                                 * 4-->STB
                                 *
                                 * */
//                                if (str_categories.equalsIgnoreCase("Trivia_Image")) {
                                if (str_question_type.equals("1")) {
                                    tv_questions_in_image_plus_txt.setText(questionsIntegerArrayList.get(0));
//                                    Log.e("qstn_length1", "" + tv_questions_in_image_plus_txt.getText().toString().length());

                                    Glide.with(Game_Two_Act.this).load(imagequestionsIntegerArrayList.get(0))
                                            .thumbnail(Glide.with(Game_Two_Act.this).load(str_team_a_path))
                                            .into(iv_changing_image);

                                    if (tv_questions_in_image_plus_txt.getText().toString().length() >= 200) {
                                        tv_questions_in_image_plus_txt.setTextSize(12);
                                    }
                                }
                                /*
                                 *
                                 * 0-->Text
                                 * 1-->Image
                                 * 2-->Audio
                                 * 3-->Prediction
                                 * 4-->STB
                                 *
                                 * */
//                                if (str_categories.equalsIgnoreCase("Trivia") || str_categories.equalsIgnoreCase("Prediction")) {
                                if (str_question_type.equals("0") || str_question_type.equals("3")) {
                                    tv_questions_in_game_two.setText(questionsIntegerArrayList.get(0));
//                                    Log.e("qstn_length1", "" + tv_questions_in_game_two.getText().toString().length());
                                    if (tv_questions_in_game_two.getText().toString().length() >= 200) {
                                        tv_questions_in_game_two.setTextSize(12);
                                    }
                                }

                                /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
                                if (answersIntegerArrayList_01.get(0).contains("A-empty_option")) {
                                    tv_ans_0_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_0_game_two.setVisibility(View.VISIBLE);
                                }
                                if (answersIntegerArrayList_02.get(0).contains("B-empty_option")) {
                                    tv_ans_1_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_1_game_two.setVisibility(View.VISIBLE);
                                }
                                if (answersIntegerArrayList_03.get(0).contains("C-empty_option")) {
                                    tv_ans_2_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_2_game_two.setVisibility(View.VISIBLE);
                                }
                                if (answersIntegerArrayList_04.get(0).contains("D-empty_option")) {
                                    tv_ans_3_game_two.setVisibility(View.GONE);
                                } else {
                                    tv_ans_3_game_two.setVisibility(View.VISIBLE);
                                }
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
                                    if (tv_hint_text.getText().toString().length() >= 40) {
                                        tv_hint_text.setTextSize(12);
                                        tv_hint_text.setText(hintArrayList.get(0));
                                    } else {
                                        tv_hint_text.setText(hintArrayList.get(0));
                                    }
                                } else {
                                    tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(0).substring(2));
                                    tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(0).substring(2));
                                    tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(0).substring(2));
                                    tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(0).substring(2));

                                    if (tv_hint_text.getText().toString().length() >= 40) {
                                        tv_hint_text.setTextSize(12);
                                        tv_hint_text.setText(hintArrayList.get(0));
                                    } else {
                                        tv_hint_text.setText(hintArrayList.get(0));
                                    }
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

    private void Get_Winning_List_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
//            Log.e("volley_json", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Two_Act.this);
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
//                                    constraintLayout_score_board_layout.setVisibility(View.GONE);
//                                    constraintLayout_rules_list.setVisibility(View.GONE);
                                    rv_rules_list_game_two.setVisibility(View.GONE);
                                    Winnings_Model winnings_model = new Winnings_Model();
                                    JSONObject json_object = (JSONObject) jsonArray.get(i);
//                                    winnings_model.setRank(json_object.getString("rank"));
                                    winnings_model.setRank_short(json_object.getString("rank_short"));
                                    winnings_model.setPrize_amount(json_object.getString("prize_amount"));
                                    winning_arrayList.add(winnings_model);

                                    rv_price_list_game_two.setVisibility(View.VISIBLE);
                                    rv_rules_list_game_two.setVisibility(View.GONE);
                                    rv_score_board_list_game_two.setVisibility(View.GONE);
                                    constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                                    shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
//                                    Log.e("winning_arrayList", winning_arrayList.toString());

                                    price_list_adapter_game_two = new Price_List_Adapter_Game_Two(Game_Two_Act.this, winning_arrayList);
                                    rv_price_list_game_two.setAdapter(price_list_adapter_game_two);
                                    shimmer_view_container_winning_list_game_two.stopShimmerAnimation();
                                    shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);

                                }
                            } else {
                                shimmer_view_container_winning_list_game_two.startShimmerAnimation();
                                shimmer_view_container_winning_list_game_two.setVisibility(View.VISIBLE);
                                rv_price_list_game_two.setVisibility(View.GONE);
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

    private void startTimer(long totalTimeCountInMilliseconds) {
        long milli = totalTimeCountInMilliseconds + 1000;
        countDownTimer = new CountDownTimer(milli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
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

                tv_timer_seconds_count_game_two.setTextColor(getResources().getColor(R.color.white_color));
                tv_timer_seconds_txt_game_two.setTextColor(getResources().getColor(R.color.white_color));
                do {
                    if (int_count_value < questionsIntegerArrayList.size()) {
//                        int_count_value++;
//                        int_reaming_page_count_value++;
                        int n1 = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
                        int_reaming_page_count_value = n1 + 1;
                        int_count_value = n1;
                        try {
                            if (int_reaming_page_count_value <= int_number_of_questions) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
                                        if (answersIntegerArrayList_01.toString().equals("empty_option")) {
                                            tv_ans_0_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_0_game_two.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_02.toString().equals("empty_option")) {
                                            tv_ans_1_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_1_game_two.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_03.toString().equals("empty_option")) {
//                                            Toast.makeText(Game_Two_Act.this, "OnFinish_if", Toast.LENGTH_SHORT).show();
                                            tv_ans_2_game_two.setVisibility(View.GONE);
                                        } else {
//                                            Toast.makeText(Game_Two_Act.this, "OnFinish_else", Toast.LENGTH_SHORT).show();
                                            tv_ans_2_game_two.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_04.toString().equals("empty_option")) {
                                            tv_ans_3_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_3_game_two.setVisibility(View.VISIBLE);
                                        }


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

                                        /*
                                         *
                                         * 0-->Text
                                         * 1-->Image
                                         * 2-->Audio
                                         * 3-->Prediction
                                         * 4-->STB
                                         *
                                         * */
//                                        if (str_categories.equalsIgnoreCase("Trivia_Image")) {
                                        if (str_question_type.equals("1")) {
                                            Glide.with(Game_Two_Act.this).load(imagequestionsIntegerArrayList.get(int_count_value))
                                                    .thumbnail(Glide.with(Game_Two_Act.this).load(str_team_a_path))
                                                    .into(iv_changing_image);

                                            tv_questions_in_image_plus_txt.setText(questionsIntegerArrayList.get(int_count_value));
//                                            Log.e("qstn_length2_tri_image", "" + tv_questions_in_image_plus_txt.getText().toString().length());
                                            if (tv_questions_in_image_plus_txt.getText().toString().length() >= 200) {
                                                tv_questions_in_image_plus_txt.setTextSize(12);
                                            }
                                        }
                                        /*
                                         *
                                         * 0-->Text
                                         * 1-->Image
                                         * 2-->Audio
                                         * 3-->Prediction
                                         * 4-->STB
                                         *
                                         * */
//                                        if (str_categories.equalsIgnoreCase("Trivia") || str_categories.equalsIgnoreCase("Prediction")) {
                                        if (str_question_type.equals("0") || str_question_type.equals("3")) {
                                            tv_questions_in_game_two.setText(questionsIntegerArrayList.get(int_count_value));
//                                            Log.e("qstn_length2", "" + tv_questions_in_game_two.getText().toString().length());
                                            if (tv_questions_in_game_two.getText().toString().length() >= 200) {
                                                tv_questions_in_game_two.setTextSize(12);
                                            }
                                        }

                                        /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
                                        if (answersIntegerArrayList_01.get(int_count_value).contains("A-empty_option")) {
                                            tv_ans_0_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_0_game_two.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_02.get(int_count_value).contains("B-empty_option")) {
                                            tv_ans_1_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_1_game_two.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_03.get(int_count_value).contains("C-empty_option")) {
                                            tv_ans_2_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_2_game_two.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_04.get(int_count_value).contains("D-empty_option")) {
                                            tv_ans_3_game_two.setVisibility(View.GONE);
                                        } else {
                                            tv_ans_3_game_two.setVisibility(View.VISIBLE);
                                        }

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
                                            if (tv_hint_text.getText().toString().length() >= 40) {
                                                tv_hint_text.setTextSize(12);
                                                tv_hint_text.setText(hintArrayList.get(int_count_value));
                                            } else {
                                                tv_hint_text.setText(hintArrayList.get(int_count_value));
                                            }
                                        } else {
                                            tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(int_count_value).substring(2));
                                            tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(int_count_value).substring(2));
                                            tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(int_count_value).substring(2));
                                            tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(int_count_value).substring(2));

                                            if (tv_hint_text.getText().toString().length() >= 40) {
                                                tv_hint_text.setTextSize(12);
                                                tv_hint_text.setText(hintArrayList.get(int_count_value));
                                            } else {
                                                tv_hint_text.setText(hintArrayList.get(int_count_value));
                                            }
                                        }

                                        str_onclick_answer_selection = "Skip";
                                        str_onclick_string_answer_selection = "Skip";
                                        int int_previous_page = int_reaming_page_count_value - 1;
                                        questionnumber_ArrayList.add(String.valueOf(int_previous_page));
                                        answerselection_ArrayList.add(str_onclick_answer_selection);
                                        timer_ArrayList.add("0");
                                        _2x_power_up_ArrayList.add("0");

                                        int_correct_ans = 0;
                                        int_correct_ans_notations = 1;
//                                        Log.e("onfinish_01", "" + int_onclcik_2x_power_up);
                                        int int_2x_powerup = 0;
                                        Total_Result_Value_Method(int_2x_powerup, int_correct_ans, str_seconds, int_skip, int_correct_ans_notations, int_previous_page, str_onclick_string_answer_selection);
                                        str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();
                                        startTimer(milliseconds);
                                        if (str_onclick_answer_selection.equalsIgnoreCase("Skip") && ((int_onclcik_2x_power_up != 0))) {
                                            if (int_2x_onclick_count != 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.colorAmber_900));
                                                tv_two_x_game_two.setEnabled(true);

                                                int_2x_onclick_or_not = 0;

                                                int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                                                int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_two_x_game_two.setTextColor(getResources().getColor(R.color.colorAmber_900));
                                            } else if (int_2x_onclick_count == 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                if (int_onclcik_2x_power_up != 0) {
                                                    int_2x_onclick_or_not = 0;
                                                    int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                                                    int_2x_onclick_count = int_2x_onclick_count + 1;
                                                    int_onclcik_2x_power_up = 1;
                                                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));

                                                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.colorAmber_900));
                                                    tv_two_x_game_two.setEnabled(true);
                                                } else {
                                                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                                                    tv_two_x_game_two.setEnabled(false);

                                                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                                                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
                                                }
                                            }
                                        }
                                        int_onclcik_2x_power_up = 0;
                                    }
                                }, 2300);
                            } else {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        str_onclick_answer_selection = "Skip";
                                        str_onclick_string_answer_selection = "Skip";
                                        int int_question_number = int_count_value;
                                        int int_previous_page = int_reaming_page_count_value - 1;
                                        questionnumber_ArrayList.add(String.valueOf(int_previous_page));
                                        answerselection_ArrayList.add(str_onclick_answer_selection);
                                        timer_ArrayList.add("0");
                                        _2x_power_up_ArrayList.add("0");
                                        int_correct_ans = 0;
                                        int_correct_ans_notations = 1;
//                                        Log.e("onfinish_02", "" + int_onclcik_2x_power_up);
                                        int int_2x_powerup = 0;
                                        Total_Result_Value_Method(int_2x_powerup, int_correct_ans, str_seconds, int_skip, int_correct_ans_notations, int_previous_page, str_onclick_string_answer_selection);

                                        int_onclcik_2x_power_up = 0;
                                        countDownTimer.cancel();
                                        constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                                        constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                                        constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
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
        dialog_fr_timer.dismiss();
        int int_question_count = Integer.parseInt(question_count);
        for (int i = 0; i < int_question_count; i++) {
            hashMap.put("onclick_time", timer_ArrayList.get(i));
            hashMap.put("onclick_answer", answerselection_ArrayList.get(i));
            hashMap.put("onclick_2x", _2x_power_up_ArrayList.get(i));
            finalhashMap.put((i + 1), String.valueOf(hashMap));
        }
        Map<Integer, String> integerStringMap = new TreeMap<>(finalhashMap);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", str_email);
            jsonObject.put("phonenumber", str_phone_no);
            jsonObject.put("playby", str_onclick_play_by_value);
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("onclick_values", "" + integerStringMap.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_RESULT_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            Glide.with(Game_Two_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
                            constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                            constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                            GetContest_Details();
                            constraintLayout_end_game_game_two.setBackgroundResource((R.drawable.end_game_grey_bg));
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close_end_game:
                SessionSave.ClearSession("Contest_Value", Game_Two_Act.this);
                SessionSave.ClearSession("Banner_Contest_Value", Game_Two_Act.this);
                Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
                startActivity(intent);
                break;
            case R.id.tv_two_x_game_two:
                n2_new_value = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
                n1_old_value = n2_new_value;

//                Log.e("2x_count", "" + int_2x_onclick_count);
                /*if (int_2x_onclick_count == 0) {
                    tv_two_x_game_two.setEnabled(false);
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                } else {*/
                if (int_2x_onclick_or_not == 0) {
                    int_2x_onclick_or_not = 1;
                    int_onclcik_2x_power_up = 1;
//                    Toast.makeText(Game_Two_Act.this, "elseeee", Toast.LENGTH_SHORT).show();
                    is_2x_btn_clicked = true;
//                    Log.e("tv_2x_power_up_minus", tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = int_2x_onclick_count - 1;
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal_orange));
                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));

                        /*if (int_2x_onclick_count == 0) {
                            tv_two_x_game_two.setEnabled(false);
                            tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                        }*/
                } else if (int_2x_onclick_or_not == 1) {
                    int_2x_onclick_or_not = 0;
                    int_onclcik_2x_power_up = 0;
//                    Toast.makeText(Game_Two_Act.this, "ifffff", Toast.LENGTH_SHORT).show();
//                    Log.e("tv_2x_power_up_plus", tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = int_2x_onclick_count + 1;
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                    tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                    tv_two_x_game_two.setTextColor(getResources().getColor(R.color.colorAmber_900));


                    /*else {
                        int_onclcik_2x_power_up = 1;
                        is_2x_btn_clicked = true;
                        Toast.makeText(Game_Two_Act.this, "elseeee", Toast.LENGTH_SHORT).show();
                        is_2x_btn_clicked = true;
                        Log.e("tv_2x_power_up_minus", tv_2x_power_up.getText().toString());
                        int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                        int_2x_onclick_count = int_2x_onclick_count - 1;
                        tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                        tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal));
                        tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
                    }*/
                }
                break;
            /*For Screen 1*/
            case R.id.tv_ans_0_game_two:
//                str_onclick_string_answer_selection = tv_ans_0_game_two.getText().toString();
                str_onclick_string_answer_selection = "A";
                boolean isCanceled = true;
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_01.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();

                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);

                str_onclick_2x_powerup = String.valueOf(int_2x_onclick_count);
                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                int n1 = n1_1 - 1;
                String s11 = Final_Answer_ArrayList.toString();
                /*This condition is used for checking user onclicked value is equal
                 *to api answer value if yes means normal else it means wrong and gives vibration effect
                 * */
//                if (Final_Answer_ArrayList.get(n1).equals(str_onclick_answer_selection)) {
                tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.black_color));
                Disable_All_Buttons();
                if (int_onclcik_2x_power_up != 0) {
//                    Log.e("int_onclcik_2x_power_up_if_side_btn_01", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
//                    Log.e("int_onclcik_2x_power_up_else_side_btn_01", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }
               /* } else {
                    tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_ans_0_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));
                    Disable_All_Buttons();
                    if (int_onclcik_2x_power_up != 0) {
                        int_skip_else = 0;
                        int_correct_ans_notations = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    } else {
                        int_skip_else = 0;
                        int_correct_ans_notations = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    }
                }*/
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_0_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_0_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1,
                                str_onclick_answer_selection,
                                str_difference_time,
                                str_onclick_2x_powerup,
                                int_2x_onclick_or_not);
                    }
                }, 1200);
                break;
            case R.id.tv_ans_1_game_two:
//                str_onclick_string_answer_selection = tv_ans_1_game_two.getText().toString();
                str_onclick_string_answer_selection = "B";
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_02.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();

                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);
                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);
                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                String s12 = Final_Answer_ArrayList.toString();
                int n2 = n1_1 - 1;
//                if (Final_Answer_ArrayList.get(n2).equals(str_onclick_answer_selection)) {
                tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.black_color));
                if (int_onclcik_2x_power_up != 0) {
//                    Log.e("int_onclcik_2x_power_up_if_side_btn_02", "" + int_onclcik_2x_power_up);
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
//                    Log.e("int_onclcik_2x_power_up_else_side_btn_02", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }
                Disable_All_Buttons();
                /*} else {
                    tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_ans_1_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));

                    if (int_onclcik_2x_power_up != 0) {
                        int_correct_ans_notations = 0;
                        int_skip_else = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    } else {
                        int_skip_else = 0;
                        int_correct_ans_notations = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    }
                    Disable_All_Buttons();
                }*/

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_1_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_1_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup, int_2x_onclick_or_not);
                    }
                }, 1200);
                break;
            case R.id.tv_ans_2_game_two:
//                str_onclick_string_answer_selection = tv_ans_2_game_two.getText().toString();
                str_onclick_string_answer_selection = "C";
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_03.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();

                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);


                str_final_answer = Final_Answer_ArrayList.get(0);


                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);


                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                String s13 = Final_Answer_ArrayList.toString();
                int n3 = n1_1 - 1;
//                if (Final_Answer_ArrayList.get(n3).equals(str_onclick_answer_selection)) {
                tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.black_color));
                if (int_onclcik_2x_power_up != 0) {
//                    Log.e("int_onclcik_2x_power_up_if_side_btn_03", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
//                    Log.e("int_onclcik_2x_power_up_else_side_btn_03", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }
                Disable_All_Buttons();
                /*} else {
                    tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_ans_2_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));
                    if (int_onclcik_2x_power_up != 0) {
                        int_skip_else = 0;
                        int_correct_ans_notations = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    } else {
                        int_skip_else = 0;
                        int_correct_ans_notations = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    }
                    Disable_All_Buttons();
                }*/
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_2_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_2_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup, int_2x_onclick_or_not);
                    }
                }, 1200);
                break;
            case R.id.tv_ans_3_game_two:
//                str_onclick_string_answer_selection = tv_ans_3_game_two.getText().toString();
                str_onclick_string_answer_selection = "D";
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_04.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_two.getText().toString();
                int_str_seconds = Integer.parseInt(str_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = String.valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);
                str_onclick_2x_powerup = String.valueOf(int_onclcik_2x_power_up);
                str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                String s14 = Final_Answer_ArrayList.toString();
                int n4 = n1_1 - 1;
//                if (Final_Answer_ArrayList.get(n4).equals(str_onclick_answer_selection)) {
                tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.black_color));

                if (int_onclcik_2x_power_up != 0) {
//                    Log.e("int_onclcik_2x_power_up_if_side_btn_04", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
//                    Log.e("int_onclcik_2x_power_up_else_side_btn_04", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }
                Disable_All_Buttons();
                /*} else {
                    tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer));
                    tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_ans_3_game_two.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.onclick_wrong_answer_shake));
                    if (int_onclcik_2x_power_up != 0) {
                        int_correct_ans_notations = 0;
                        int_skip_else = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    } else {
                        int_skip_else = 0;
                        int_correct_ans_notations = 0;
                        int_wrong_ans = Integer.parseInt(str_wrong_ans);
                        Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                    }
                    Disable_All_Buttons();
                }*/

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_ans_3_game_two.setBackground(getResources().getDrawable(R.drawable.normal_rectangle_effect));
                        tv_ans_3_game_two.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1, str_onclick_answer_selection, str_difference_time, str_onclick_2x_powerup, int_2x_onclick_or_not);
                    }
                }, 1200);
                break;
            case R.id.tv_winnings_btn:

                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                shimmer_view_container_winning_list_game_two.setVisibility(View.VISIBLE);
                shimmer_view_container_winning_list_game_two.startShimmerAnimation();

                shimmer_view_container_for_rules.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);

                rv_price_list_game_two.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                rv_score_board_list_game_two.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);

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

                rv_price_list_game_two.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                rv_score_board_list_game_two.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);


                rv_rules_list_game_two.setVisibility(View.GONE);
                Get_Rules_Name_Details();

//                constraintLayout_score_board_layout.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.white_color));
                tv_winnings_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_winnings_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.constraintLayout_watch_ads_btn:
                if (int_entry_fee > int_tv_points) {
                    int n11 = int_entry_fee - int_tv_points;
                    tv_enter_contest_btn.setVisibility(View.GONE);
                    if (str_status_onclick.equals("2")) {
                        constraintLayout_watch_ads_btn.setVisibility(View.GONE);
                        constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                        tv_earn_coins.setVisibility(View.GONE);
                    } else if (str_status_onclick.equals("0")) {
                        constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                        constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);
                        tv_earn_coins.setVisibility(View.VISIBLE);
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
                    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
                    /*int_play_status = 1;
                    Getting_Update_Status_Details_Initial(int_play_status);
                    Get_Enter_Game_Page();
                    str_playby = "Coins";
                    Get_Points_Add_Delete_Details(str_playby);*/

                    Get_Enter_Game_Page();

//                    Get_User_Wallet_Details();
                }
                break;
            case R.id.tv_enter_contest_btn:
                str_onclick_play_by_value = "Free";
                constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                constraintLayout_rewarded_video.setVisibility(View.VISIBLE);
                Glide.with(this).asGif().load(R.drawable.giphy).into(gif_image);

                /*This components are used for Rewarded video*/
                Get_Rewarded_Video_Method(savedInstanceState);
                break;
            case R.id.tv_enter_contest_btn_for_free:
                showInterstitial();
//                Load_FB_ADS();
                break;
            case R.id.tv_score_board:
                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                shimmer_view_container_for_scoreboard.startShimmerAnimation();

                shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.GONE);


                rv_rules_list_game_two.setVisibility(View.GONE);
                rv_price_list_game_two.setVisibility(View.GONE);

                constraintLayout_score_board_top_layout.setVisibility(View.VISIBLE);
                rv_score_board_list_game_two.setVisibility(View.VISIBLE);


                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_winnings_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_winnings_btn.setTextColor(getResources().getColor(R.color.black_color));
                tv_no_data_available_fr_score_board.setVisibility(View.GONE);

                str_session_scoreboard_value = SessionSave.getSession("ScoreBoard_Value", Game_Two_Act.this);
                str_session_scoreboard_current_user_rank_value = SessionSave.getSession("ScoreBoard_User_Rank_Value", Game_Two_Act.this);
//                Log.e("str_session_scoreboard_value", str_session_scoreboard_value);
//                Log.e("str_session_scoreboard_current_user_rank_value", str_session_scoreboard_current_user_rank_value);
                /*
                 * Here we use cache concept when application is destroyed
                 * Session value gets 'No data' it call 'get_contest' method
                 * else if app not destroyed means rv_game_list use the session value to show list of data
                 */
                if (str_session_scoreboard_value.equalsIgnoreCase("No data") && str_session_scoreboard_current_user_rank_value.equalsIgnoreCase("No data")) {
//                Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
                    shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                    shimmer_view_container_for_scoreboard.startShimmerAnimation();
                    Get_Ranking_List_Details();
                } else {
                    Get_Ranking_List_Details();
                    /*Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void run() {
//                            Toast.makeText(Game_Two_Act.this, "Session", Toast.LENGTH_SHORT).show();
                            shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                            shimmer_view_container_for_scoreboard.stopShimmerAnimation();

                            shimmer_view_container_for_rules.setVisibility(View.GONE);
                            shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);

                            shimmer_view_container_winning_list_game_two.setVisibility(View.GONE);
                            shimmer_view_container_winning_list_game_two.stopShimmerAnimation();

                            rv_score_board_list_game_two.setVisibility(View.VISIBLE);
                            constraintLayout_score_board_top_layout.setVisibility(View.VISIBLE);

                            rv_rules_list_game_two.setVisibility(View.GONE);
                            rv_price_list_game_two.setVisibility(View.GONE);
//                            constraintLayout_rules_list.setVisibility(View.GONE);
//                            constraintLayout_rules_list.setBackgroundColor(getResources().getColor(R.color.yellow_color));
//                            constraintLayout_score_board_layout.setVisibility(View.VISIBLE);
//                            Log.e("str_session_scoreboard_current_user_rank_value", str_session_scoreboard_current_user_rank_value);
                            if (str_session_scoreboard_current_user_rank_value.equals("0")) {
                                tv_current_user_rank.setText("Your Rank : " + "----");
                            } else {
                                tv_current_user_rank.setText("Your Rank : " + str_crnt_user_rank);
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Category_Model.Data>>() {
                            }.getType();
                            ArrayList<Category_Model.Data> arrayList = gson.fromJson(str_session_scoreboard_value, type);
                            tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                            ranking_list_adapter_game_two = new Ranking_List_Adapter_Game_Two(Game_Two_Act.this, arrayList);
                            rv_score_board_list_game_two.setAdapter(ranking_list_adapter_game_two);
                        }
                    }, 500);*/
                    break;
                }
        }
    }

    private void Get_Ranking_List_Details() {
//        constraintLayout_rules_list.setVisibility(View.GONE);

//        constraintLayout_score_board_layout.setBackgroundColor(getResources().getColor(R.color.blue_color));
//        constraintLayout_rules_list.setBackgroundColor(getResources().getColor(R.color.red_color_new_light));

        rv_price_list_game_two.setVisibility(View.GONE);
        rv_rules_list_game_two.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Log.e("rank_json", jsonObject.toString());
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
                                        rv_score_board_list_game_two.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                shimmer_view_container_for_scoreboard.startShimmerAnimation();
                                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                            } else if (response.body().data.isEmpty()) {
//                                Toast.makeText(Game_Two_Act.this, "Empty_Toast", Toast.LENGTH_SHORT).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                                        tv_no_data_available_fr_score_board.setVisibility(View.VISIBLE);
                                        rv_score_board_list_game_two.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.stopShimmerAnimation();
                                    }
                                }, 2500);
                                constraintLayout_score_board_top_layout.setVisibility(View.VISIBLE);
                                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                                rv_score_board_list_game_two.setVisibility(View.GONE);
                                rv_price_list_game_two.setVisibility(View.GONE);
                                rv_rules_list_game_two.setVisibility(View.GONE);

                                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_scoreboard.startShimmerAnimation();
                            } else {
//                                Toast.makeText(Game_Two_Act.this, "Non_Empty_Toast", Toast.LENGTH_SHORT).show();
                                constraintLayout_score_board_top_layout.setVisibility(View.VISIBLE);

                                tv_no_data_available_fr_score_board.setVisibility(View.GONE);
                                str_crnt_user_rank = response.body().your_rank;
//                                Log.e("str_crnt_user_rank", str_crnt_user_rank);
                                if (str_crnt_user_rank.equals("0")) {
                                    tv_current_user_rank.setText("Your Rank : " + "----");
                                } else {
                                    tv_current_user_rank.setText("Your Rank : " + str_crnt_user_rank);
                                }
                                rv_score_board_list_game_two.setVisibility(View.VISIBLE);
                                rv_price_list_game_two.setVisibility(View.GONE);
                                rv_rules_list_game_two.setVisibility(View.GONE);

                                /*
                                 *Here we use the following concept for saving the data to shared preference.
                                 */
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body().data);
                                SessionSave.SaveSession("ScoreBoard_Value", json, Game_Two_Act.this);
                                SessionSave.SaveSession("ScoreBoard_User_Rank_Value", str_crnt_user_rank, Game_Two_Act.this);
//                                Log.e("ScoreBoard_Value_inside_method", SessionSave.getSession("ScoreBoard_Value", Game_Two_Act.this));
//                                Log.e("ScoreBoard_User_Rank_Value_inside_method", SessionSave.getSession("ScoreBoard_User_Rank_Value", Game_Two_Act.this));
                                shimmer_view_container_for_scoreboard.stopShimmerAnimation();
                                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                assert response.body() != null;
                                ranking_list_adapter_game_two = new Ranking_List_Adapter_Game_Two(Game_Two_Act.this, Objects.requireNonNull(response.body()).data);
                                rv_score_board_list_game_two.setAdapter(ranking_list_adapter_game_two);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
    private void Getting_Update_Status_Details_Initial(int int_play_status) {
        /*play_status==>1 Live or Playing*/
        /*play_status==>2 Completed*/
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", int_play_status);
            jsonObject.put("email", str_email);
            Log.e("update_response_init", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {

                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    private void Total_Result_Value_Method(int int_onclcik_2x_power_up,
                                           int int_correct_ans,
                                           String str_difference_time,
                                           int int_skip,
                                           int int_correct_ans_notations,
                                           int int_previous_or_courrent_question_number,
                                           String str_onclick_string_answer_selection) {
        dialog_fr_timer.dismiss();
        int int_sum_total_time = 0;
        int int_sum_total_crct_ans = 0;
        int int_sum_total_wrng_ans = 0;
        int int_sum_total_skip_values = 0;
        int int_onclcik_2x_power_up_values = 0;
        int int_difference_onclick_values;
        int int_difference_final_values;
        StringBuilder str_selection_values = new StringBuilder();
//        Log.e("str_difference_time_nr", "" + str_difference_time);


        /*This if loop is used for setting correct and wrong answer for different arraylist values.*/
        if (int_correct_ans_notations == 1) {
            total_onclick_correct_answer_values_ArrayList.add(int_correct_ans);
        }
        if (int_correct_ans_notations == 0) {
            total_onclick_wrong_answer_values_ArrayList.add(int_correct_ans);
        }
        /*This arraylist used for setting time difference value to arraylist*/
        total_onclick_time_ArrayList.add(str_difference_time);

        /*This arraylist used for setting skip values to arraylist*/
        total_skip_values_ArrayList.add(int_skip);
        _2x_power_up_continous_update_to_api_ArrayList.add(String.valueOf(int_onclcik_2x_power_up));

//        Log.e("_2x_power_up_continous_update_to_api_ArrayList", _2x_power_up_continous_update_to_api_ArrayList.toString());

        /*This arraylist used for setting string_selection values to arraylist*/
        answers_string_velue_selection_ArrayList.add(str_onclick_string_answer_selection);


//        Log.e("crnt_ans_list", total_onclick_correct_answer_values_ArrayList.toString());
//        Log.e("wrng_ans_list", total_onclick_wrong_answer_values_ArrayList.toString());

        /*The following for loops are used getting arrayvalues for addition method*/
        for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
        }
        for (int i = 0; i < total_onclick_correct_answer_values_ArrayList.size(); i++) {
            int_sum_total_crct_ans += total_onclick_correct_answer_values_ArrayList.get(i);
        }
        for (int i = 0; i < total_onclick_wrong_answer_values_ArrayList.size(); i++) {
            int_sum_total_wrng_ans += total_onclick_wrong_answer_values_ArrayList.get(i);
        }

        for (int i = 0; i < total_skip_values_ArrayList.size(); i++) {
            int_sum_total_skip_values += total_skip_values_ArrayList.get(i);
        }

        for (int i = 0; i < answers_string_velue_selection_ArrayList.size(); i++) {
            if (i < answers_string_velue_selection_ArrayList.size() - 1) {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i).concat(","));
            } else {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i));
            }
        }


        int_difference_onclick_values = int_sum_total_crct_ans - int_sum_total_wrng_ans;
        int_difference_final_values = int_difference_onclick_values + int_sum_total_skip_values;
//        Log.e("int_sum_total_time", "" + int_sum_total_time);
//        Log.e("int_sum_total_crct_ans", "" + int_sum_total_crct_ans);
//        Log.e("int_sum_total_wrng_ans", "" + int_sum_total_wrng_ans);
//        Log.e("int_sum_total_skp_vlus", "" + int_sum_total_skip_values);
//        Log.e("diffe_onclick_values", "" + int_difference_onclick_values);
//        Log.e("diffe_final_values", "" + int_difference_final_values);
//        Log.e("str_selection_values", "" + str_selection_values);
//        Log.e("total_onclick_time_ArrayList", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
//        Log.e("prev_r_crnt_qstn_num", "" + int_previous_or_courrent_question_number);
        if (int_previous_or_courrent_question_number == int_number_of_questions) {
            dialog_fr_timer.dismiss();
            countDownTimer.cancel();
            Glide.with(Game_Two_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
            GetContest_Details();
            int finalInt_sum_total_time = int_sum_total_time;
            String finalStr_selection_values = str_selection_values.toString();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
                    int n1_1 = Integer.parseInt(str_remaining_count_value);
                    try {
                        dialog_fr_timer.dismiss();
                        countDownTimer.cancel();
                        JSONObject jsonObject = new JSONObject();
                        if (str_question_type.equals("3")) {
                            jsonObject.put("email", str_email);
                            jsonObject.put("contest_id", str_contest_id);
                            jsonObject.put("total_onclick_time", finalInt_sum_total_time);
                            jsonObject.put("contest_answer", finalStr_selection_values);
                            jsonObject.put("onclick_time", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
                            jsonObject.put("contest_2x", _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", ""));

                            jsonObject.put("total_onclick_answer_values", int_difference_final_values);
                            jsonObject.put("click_points_correct", "");
                            jsonObject.put("click_points_skip", "");
                            jsonObject.put("click_points_wrong", "");
                            Log.e("IF_Total_Json_Values", jsonObject.toString());
                        } else {
                            jsonObject.put("email", str_email);
                            jsonObject.put("contest_id", str_contest_id);
                            jsonObject.put("total_onclick_time", finalInt_sum_total_time);
                            jsonObject.put("contest_answer", finalStr_selection_values);
                            jsonObject.put("onclick_time", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
                            jsonObject.put("contest_2x", _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", ""));

                            jsonObject.put("click_points_correct", total_onclick_correct_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
                            jsonObject.put("click_points_skip", total_skip_values_ArrayList.toString().replace("[", "").replace("]", ""));
                            jsonObject.put("click_points_wrong", total_onclick_wrong_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
                            jsonObject.put("total_onclick_answer_values", int_difference_final_values);
                            Log.e("ELSE_Total_Json_Values", jsonObject.toString());
                        }
//                        Getting_Update_Status_Details();
                        APIInterface apiInterface = Factory.getClient();
                        Call<Category_Model> call = apiInterface.GET_FINAL_RESULT_CALL("application/json", jsonObject.toString(), str_auth_token);
                        call.enqueue(new Callback<Category_Model>() {
                            @Override
                            public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                                if (response.code() == 200) {
                                    if (response.isSuccessful()) {

                                        /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
                                        int_play_status = 2;
                                        Getting_Update_Status_Details_Initial(int_play_status);
                                        str_playby = "Coins";
                                        Get_Points_Add_Delete_Details(str_playby);


                                        constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
                                        constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                                        constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                                        countDownTimer.cancel();
                                        Getting_Update_Status_Details();
                                        GetContest_Details();
                                        constraintLayout_end_game_game_two.setBackgroundResource((R.drawable.end_game_grey_bg));
                                    }
                                } else if (response.code() == 401) {
                                    Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                                } else if (response.code() == 500) {
                                    Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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
            }, 1200);
        }
    }

    private void Disable_All_Buttons() {
        tv_two_x_game_two.setEnabled(false);
        tv_ans_0_game_two.setEnabled(false);
        tv_ans_1_game_two.setEnabled(false);
        tv_ans_2_game_two.setEnabled(false);
        tv_ans_3_game_two.setEnabled(false);
    }

    private void Get_Enter_Game_Page() {
//        int int_minus_value = 50;
//        int_entry_fee = Integer.parseInt(str_entry_fees);
//        int int_calc = int_balance - int_entry_fee;
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("BALANCE", int_calc);
//        Log.e("Cnt_Vlus_mob_reg", contentValues.toString());
//        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);

//        str_onclick_play_by_value = "Playby50";
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
        }, 2500);
        constraintLayout_end_game_game_two.setVisibility(View.GONE);
    }

    /*This components are used for showing Rewarded video*/
    private void Get_Rewarded_Video_Method(Bundle savedInstanceState) {
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
//                Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                // addCoins(rewardItem.getAmount());
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
//                Log.e("ad_opened_log", "Ad Opened");
//                Toast.makeText(getBaseContext(), "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
//                Log.e("ad_started_log", "Ad started");
//                Toast.makeText(getBaseContext(), "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
//                Toast.makeText(getBaseContext(), "Ad closeddddd.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(), "Ad Closed.", Toast.LENGTH_SHORT).show();

                constraintLayout_rewarded_video.setVisibility(View.GONE);
                constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
                recreate();
//                onBackPressed();
            }

            /*@Override
            public void onRewardedVideoAdClosed() {
                int int_n1, int_n2, int_n3;
              *//*  String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
                Cursor cursor = db.rawQuery(select, null);
                if (cursor.moveToFirst()) {
                    do {

                    } while (cursor.moveToNext());
                }
                cursor.close();
              *//*
                int_n1 = Integer.parseInt(str_wallet1);

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
                    int n11 = int_entry_fee - int_n3;
                    tv_earn_coins.setText(String.valueOf(n11));
                    Log.e("int_entry_fee_ad", "" + int_entry_fee);
                    Log.e("n1111", "" + n11);
                    Log.e("n2222", "" + int_n2);
                    Log.e("n3333", "" + int_n3);
                    Toast.makeText(context, "if_side", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "else_side", Toast.LENGTH_SHORT).show();
                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                    tv_play_btn.setVisibility(View.VISIBLE);
                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                }

                if (int_n2 != 10) {
//                    Log.e("ifff","method");
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
                    constraintLayout_rewarded_video.setVisibility(View.GONE);
                    constraintLayout_end_game_game_two.setVisibility(View.GONE);

                } *//*else {
//                    Log.e("Elseee","method");
                    constraintLayout_rewarded_video.setVisibility(View.GONE);
                    constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);

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
*/
            @Override
            public void onRewardedVideoAdLeftApplication() {
//                Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
//                Log.e("ad_left_log", "Ad left application");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
//                Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
//                Log.e("Failed_log", "Ad failed to load.");
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
//                            Toast_Message_For_Reward.showToastMessage(Game_Two_Act.this, "You have earned " + int_rewarded_coins_point + " coins from video," + "\n Your current balance point is " + n1 + ".");
                            Toast_Message_For_Reward.showToastMessage(Game_Two_Act.this, "You have earned " + int_rewarded_coins_point + " coins." + "\n Your current coin balance is " + n1 + ".");
                            int int_tv_points11 = Integer.parseInt(str_nav_curnt_amnt);
//                            Log.e("after_ad", "" + str_nav_curnt_amnt);

                            if (int_entry_fee > int_tv_points11) {
//                                recreate();
                                int n11 = int_entry_fee - int_tv_points11;
                                tv_earn_coins.setText(String.valueOf(n11));
//                                Log.e("n1111", "" + n11);
                                constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                                constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
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
                                constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                                constraintLayout_end_game_game_two.setVisibility(View.GONE);
                            }

                            str_playby = "Ads";
                            Get_Points_Add_Delete_Details(str_playby);
                            recreate();

                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    private void AD_Closed_Method() {
        constraintLayout_rewarded_video.setVisibility(View.GONE);
        constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
        Get_Wallet_Balance_Details();

/*  String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        cursor.close();*/
//        Log.e("ad_closed_log", "Ad closed");

//        Log.e("coin_count", mCoinCountText.getText().toString());

//        int_total_value = int_db_balance_for_reward_video + int_rewarded_coins_point;
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("BALANCE", int_total_value);
        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);*/
//        Log.e("Content_Values_mob_reg", contentValues.toString());
        //        Get_User_Wallet_Details();

//        recreate();
//        onBackPressed();
        /*finish();
        startActivity(getIntent());*/

//        Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();


//        constraintLayout_rewarded_video.setVisibility(View.GONE);
//        constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
        /*

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
        constraintLayout_end_game_game_two.setVisibility(View.GONE);*/
    }

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
                Log.e("add_dlt_json_value_ad", jsonObject.toString());
            } else if (str_playby.equalsIgnoreCase("Coins")) {
                jsonObject.put("email", str_email);
                jsonObject.put("playby", str_playby);
                if (str_entry_fees.equalsIgnoreCase("Free")) {
                    jsonObject.put("wallet", "0");
                } else {
                    jsonObject.put("wallet", str_entry_fees);
                }
                Log.e("add_dlt_json_value_coins", jsonObject.toString());
            }

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_Wallet_Point_Delete_Call("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        String str_amount = response.body().current_amt;
                        Navigation_Drawer_Act.tv_points.setText(str_amount);
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    /*This components are used for Rewarded video*/
    private void startGame_Rewarded_Video() {
        mGamePaused = false;
        mGameOver = false;
        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }

    /*This components are used for Rewarded video*/
    private void showRewardedVideo_New() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    /*This components are used for Rewarded video*/
    private void addCoins(int coins) {
        mCoinCountText.setVisibility(View.VISIBLE);
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText(String.valueOf(mCoinCount));
        AD_Closed_Method();
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            startGame();
        }
    }

    private void Screen_01_Method(int int_reaming_page_count_value,
                                  String str_onclick_answer_selection,
                                  String str_onclick_time,
                                  String str_onclick_2x_powerup,
                                  int int_2x_onclick_or_not1) {
        tv_two_x_game_two.setEnabled(true);
        tv_ans_0_game_two.setEnabled(true);
        tv_ans_1_game_two.setEnabled(true);
        tv_ans_2_game_two.setEnabled(true);
        tv_ans_3_game_two.setEnabled(true);

        if (int_2x_onclick_count == 0) {
            tv_two_x_game_two.setEnabled(false);
            tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
            constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
            tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
            tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
        } else {
            tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//            tv_two_x_game_two.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
            tv_two_x_game_two.setTextColor(getResources().getColor(R.color.colorAmber_900));
            tv_two_x_game_two.setEnabled(true);
        }
        int_2x_onclick_or_not = int_2x_onclick_or_not1;
        dialog_fr_timer.dismiss();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            constraintLayout_count_down_timer.startAnimation(AnimationUtils.loadAnimation(Game_Two_Act.this, R.anim.stop_animation_shake));
            constraintLayout_count_down_timer.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
            startTimer(milliseconds);
        }
        constraintLayout_answer_set_01.setVisibility(View.VISIBLE);
        str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
        int n1_1 = Integer.parseInt(str_remaining_count_value);
//        Log.e("int_number_of_questions", "" + int_number_of_questions);
//        Log.e("qstnIntrAryLstSzeonclk", "" + questionsIntegerArrayList.size());
        if (n1_1 < int_number_of_questions) {
            if (countDownTimer == null) {
                countDownTimer.start();
            }
            int n2 = n1_1 + 1;
            tv_remaing_count_value_in_game_two.setText(String.valueOf(n2));
            /*
             *
             * 0-->Text
             * 1-->Image
             * 2-->Audio
             * 3-->Prediction
             * 4-->STB
             *
             * */
//            if (str_categories.equalsIgnoreCase("Trivia_Image")) {
            if (str_question_type.equals("1")) {
                Glide.with(Game_Two_Act.this).load(imagequestionsIntegerArrayList.get(n1_1))
                        .thumbnail(Glide.with(Game_Two_Act.this).load(str_team_a_path))
                        .into(iv_changing_image);
                tv_questions_in_image_plus_txt.setText(questionsIntegerArrayList.get(n1_1));
//                Log.e("qstn_length3", "" + tv_questions_in_image_plus_txt.getText().toString().length());
                if (tv_questions_in_image_plus_txt.getText().toString().length() >= 200) {
                    tv_questions_in_image_plus_txt.setTextSize(12);
                }
            }
            /*
             *
             * 0-->Text
             * 1-->Image
             * 2-->Audio
             * 3-->Prediction
             * 4-->STB
             *
             * */
//            if (str_categories.equalsIgnoreCase("Trivia") || str_categories.equalsIgnoreCase("Prediction")) {
            if (str_question_type.equals("0") || str_question_type.equals("3")) {
                tv_questions_in_game_two.setText(questionsIntegerArrayList.get(n1_1));
//                Log.e("qstn_length3", "" + tv_questions_in_game_two.getText().toString().length());
                if (tv_questions_in_game_two.getText().toString().length() >= 200) {
                    tv_questions_in_game_two.setTextSize(12);
                }
            }

            String s1 = tv_questions_in_game_two.getText().toString();
//            Log.e("s111", s1);
//            Toast.makeText(this, "no.of.count" + s1, Toast.LENGTH_SHORT).show();

            /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
            if (answersIntegerArrayList_01.get(n1_1).contains("A-empty_option")) {
                tv_ans_0_game_two.setVisibility(View.GONE);
            } else {
                tv_ans_0_game_two.setVisibility(View.VISIBLE);
            }
            if (answersIntegerArrayList_02.get(n1_1).contains("B-empty_option")) {
                tv_ans_1_game_two.setVisibility(View.GONE);
            } else {
                tv_ans_1_game_two.setVisibility(View.VISIBLE);
            }
            if (answersIntegerArrayList_03.get(n1_1).contains("C-empty_option")) {
                tv_ans_2_game_two.setVisibility(View.GONE);
            } else {
                tv_ans_2_game_two.setVisibility(View.VISIBLE);
            }
            if (answersIntegerArrayList_04.get(n1_1).contains("D-empty_option")) {
                tv_ans_3_game_two.setVisibility(View.GONE);
            } else {
                tv_ans_3_game_two.setVisibility(View.VISIBLE);
            }
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

                if (tv_hint_text.getText().toString().length() >= 40) {
                    tv_hint_text.setTextSize(12);
                    tv_hint_text.setText(hintArrayList.get(n1_1));
                } else {
                    tv_hint_text.setText(hintArrayList.get(n1_1));
                }
            } else {
                tv_ans_0_game_two.setText(answersIntegerArrayList_01.get(n1_1).substring(2));
                tv_ans_1_game_two.setText(answersIntegerArrayList_02.get(n1_1).substring(2));
                tv_ans_2_game_two.setText(answersIntegerArrayList_03.get(n1_1).substring(2));
                tv_ans_3_game_two.setText(answersIntegerArrayList_04.get(n1_1).substring(2));
                if (tv_hint_text.getText().toString().length() >= 40) {
                    tv_hint_text.setTextSize(12);
                    tv_hint_text.setText(hintArrayList.get(n1_1));
                } else {
                    tv_hint_text.setText(hintArrayList.get(n1_1));
                }
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
        }
    }

    private void Getting_Update_Status_Details() {
        /*play_status==>1 Live or Playing*/
        /*play_status==>2 Completed*/
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", "2");
            jsonObject.put("email", str_email);
            Log.e("update_response_end", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
//                        iv_end_game_sucess_message_gif.setText(response.body().message);
                            constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                            constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            constraintLayout_end_game_game_two.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                            GetContest_Details();
                            constraintLayout_end_game_game_two.setBackgroundResource((R.drawable.end_game_grey_bg));
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    public void GetContest_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("end_contest_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_CONTEST_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            try {
                                String str_play_status = "";
                                str_play_status = Objects.requireNonNull(response.body()).data.get(0).play_status;
//                                Log.e("str_play_status", str_play_status);

                                str_code = response.body().code;
                                str_message = response.body().message;
                                tv_close_end_game.setVisibility(View.VISIBLE);
//                                Toast.makeText(Game_Two_Act.this, "PlayStatus:" + str_play_status, Toast.LENGTH_SHORT).show();
                                if ((str_play_status.equals("2") || str_play_status.equals("1"))) {
                                    rv_single_card_end_game.setVisibility(View.GONE);
                                    tv_close_end_game.setVisibility(View.VISIBLE);
                                    tv_play_anoter_contest.setVisibility(View.GONE);
                                } else {
                                    rv_single_card_end_game.setVisibility(View.VISIBLE);
                                    tv_close_end_game.setVisibility(View.VISIBLE);
                                    tv_play_anoter_contest.setVisibility(View.VISIBLE);
                                    contestAdapter_for_end_game = new ContestAdapter_For_End_Game(Game_Two_Act.this, response.body().data);
                                    rv_single_card_end_game.setAdapter(contestAdapter_for_end_game);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {
//                Log.e("error_Response", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            snackbar.getView().setBackgroundResource(R.color.timer_bg_color);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (constraintLayout_end_game_game_two.getVisibility() == View.VISIBLE) {
            Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
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
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        constraintLayout_game_two_top_layout.setVisibility(View.GONE);
                        constraintLayout_end_game_game_two.setVisibility(View.GONE);
                        constraintLayout_just_a_moment_game_two.setVisibility(View.GONE);
                        constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                        constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
                        int n1 = timer_ArrayList.size();

                        /*this following lines are used for setting unanswered questions for "0"*/
                        int int_current_question_num = Integer.parseInt(tv_remaing_count_value_in_game_two.getText().toString());
                        int n3 = int_number_of_questions - int_current_question_num;


//                        Log.e("crnt_question_num", "" + int_current_question_num);
//                        Log.e("int_number_of_questions", "" + int_number_of_questions);
//                        Log.e("differencevalue", "" + n3);

                        int_correct_ans_notations = 1;
//                        Log.e("answerselection_ArrayList_size", "" + answerselection_ArrayList.size());
                        int int_ary_size = answerselection_ArrayList.size();

                        int int_sum_total_time = 0;
//                        Log.e("Before_total_onclick_time_ArrayList_alert", total_onclick_time_ArrayList.toString());
                        for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
                            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
                        }

//                        Log.e("int_sum_total_time_alert", String.valueOf(int_sum_total_time));
//                        Log.e("After_total_onclick_time_ArrayList_alert", total_onclick_time_ArrayList.toString());
                        for (int i = int_ary_size; i < int_number_of_questions; i++) {
//                            Log.e("n333", "" + i);
//                            Log.e("str_skip_loop", "" + str_skip);
//                        timer_ArrayList.add("0");
//                            int int_sec = Integer.parseInt(str_seconds);
                            duplicate_answerselection_ArrayList.add("Skip");
                            duplicate_onclick_time_ArrayList.add(str_seconds);
                            duplicate_onclick_point_ArrayList.add(str_skip);
                            duplicate_correct_answerselection_ArrayList.add(String.valueOf(int_correct_ans));
                        }
//                        _2x_power_up_continous_update_to_api_ArrayList.add(String.valueOf(int_onclcik_2x_power_up));
//                        Log.e("int_ary_size", String.valueOf(int_ary_size));
//                        Log.e("_2x_power_up_continous_update_to_api_ArrayListtttt", _2x_power_up_continous_update_to_api_ArrayList.toString());
                        for (int i = int_ary_size; i < int_number_of_questions; i++) {
                            _2x_power_up_continous_update_to_api_ArrayList.add("0");
                        }
//                        Log.e("_2x_power_up_continous_update_to_api_ArrayList_wt", _2x_power_up_continous_update_to_api_ArrayList.toString());
                        String str_duplicate_answerselection = duplicate_answerselection_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_onclick_time = duplicate_onclick_time_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_onclick_point_value = duplicate_onclick_point_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_onclick_correct_answer_point_value = duplicate_correct_answerselection_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_2x_power_values = _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", "");

                        //                      Getting_Final_Details();
//                        Log.e("int_correct_ans_logout_alert", "" + int_correct_ans);
//                        Log.e("str_duplicate_2x_power_values_logout_alert", str_duplicate_2x_power_values);
//                        Log.e("str_seconds_logout_alert", str_seconds);
//                        Log.e("int_skip_logout_alert", "" + int_skip);
//                        Log.e("str_skip_logout_alert", "" + str_skip);
//                        Log.e("int_correct_ans_notations_logout_alert", "" + int_correct_ans_notations);
//                        Log.e("str_duplicate_answerselection_logout_alert", "" + str_duplicate_answerselection);
//                        Log.e("str_duplicate_onclick_time_logout_alert", "" + str_duplicate_onclick_time);
//                        Log.e("str_duplicate_onclick_point_value_logout_alert", "" + str_duplicate_onclick_point_value);
//                        Log.e("str_duplicate_onclick_correct_answer_point_value", "" + str_duplicate_onclick_correct_answer_point_value);

                        int int_sum_total_time1 = 0;
//                        Log.e("Before_total_onclick_time_ArrayList_alert_02", duplicate_onclick_time_ArrayList.toString());
                        for (int i = 0; i < duplicate_onclick_time_ArrayList.size(); i++) {
                            int_sum_total_time1 += Integer.parseInt(duplicate_onclick_time_ArrayList.get(i));
                        }
//                        Log.e("int_sum_total_time_alert_02", String.valueOf(int_sum_total_time1));
//                        Log.e("After_total_onclick_time_ArrayList_alert_02", duplicate_onclick_time_ArrayList.toString());

                        int int_total_onclick_time = int_sum_total_time + int_sum_total_time1;
//                        Log.e(" int_total_onclick_time", String.valueOf(int_total_onclick_time));
                        Total_Result_Value_Method_Without_End_Game_Screen(
                                int_onclcik_2x_power_up,
                                int_correct_ans,
                                str_seconds,
                                int_skip,
                                int_correct_ans_notations,
                                str_duplicate_answerselection,
                                str_duplicate_onclick_time,
                                str_duplicate_onclick_point_value,
                                str_duplicate_onclick_correct_answer_point_value,
                                int_total_onclick_time,
                                str_duplicate_2x_power_values);


//                        Log.e("int_correct_ans_yes", "" + int_correct_ans);
//                        Log.e("str_seconds_yes", "" + str_seconds);
                    /*Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);*/


                        dialog.dismiss();
                    /*Handler handler = new Handler();
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
                    }, 1000);*/
                    }
                });
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            } else {
          /*  Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();*/
//          System.exit(0);

                Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void Getting_Update_Status_Details_Without_End_Game_Screen() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", "2");
            jsonObject.put("email", str_email);
            Log.e("update_json_response_without_end_game_screen", jsonObject.toString());
//            Log.e("str_auth_tokenkkkk_without_end_game_screen", str_auth_token);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            countDownTimer.cancel();
                            Intent intent = new Intent(Game_Two_Act.this, Navigation_Drawer_Act.class);
                            intent.putExtra("refresh_value", "1");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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
    private void Total_Result_Value_Method_Without_End_Game_Screen(
            int int_onclcik_2x_power_up,
            final int int_correct_ans,
            String str_difference_time,
            int int_skip,
            int int_correct_ans_notations,
            String str_onclick_string_answer_selection,
            String str_duplicate_onclick_time,
            String str_duplicate_onclick_point_value,
            String str_duplicate_onclick_correct_answer_point_value,
            int int_total_onclick_time,
            String str_duplicate_2x_power_values) {
        dialog_fr_timer.dismiss();
        int int_sum_total_time = 0;
        int int_sum_total_crct_ans = 0;
        int int_sum_total_wrng_ans = 0;
        int int_sum_total_skip_values = 0;
        int int_onclcik_2x_power_up_values = 0;
        int int_difference_onclick_values;
        int int_difference_final_values;
        StringBuilder str_selection_values = new StringBuilder();
        StringBuilder str_onclick_time_values = new StringBuilder();
        StringBuilder str_onclick_point_values = new StringBuilder();
//        Log.e("str_difference_time_wt", "" + str_difference_time);
//        Log.e("int_correct_ans_wt", "" + int_correct_ans);
//        Log.e("str_onclick_string_answer_selection_wt", "" + str_onclick_string_answer_selection);
//        Log.e("str_duplicate_onclick_point_value_wt", "" + str_duplicate_onclick_point_value);
//        Log.e("str_duplicate_onclick_time_wt", "" + str_duplicate_onclick_time);

        /*This if loop is used for setting correct and wrong answer for different arraylist values.*/
        if (int_correct_ans_notations == 1) {
            total_onclick_correct_answer_values_ArrayList.add(int_correct_ans);
        }
        if (int_correct_ans_notations == 0) {
            total_onclick_wrong_answer_values_ArrayList.add(int_correct_ans);
        }
        /*This arraylist used for setting time difference value to arraylist*/
//        total_onclick_time_ArrayList.add(str_difference_time);

//        Log.e("total_onclick_time_ArrayList_without", total_onclick_time_ArrayList.toString());
        /*This arraylist used for setting skip values to arraylist*/
        total_skip_values_ArrayList.add(int_skip);
//        Log.e("total_skip_values_ArrayList_value", total_skip_values_ArrayList.toString());
        /*This arraylist used for setting string_selection values to arraylist*/
        answers_string_velue_selection_ArrayList.add(str_onclick_string_answer_selection);

        /*The following for loops are used getting arrayvalues for addition method*/
        /*for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
        }*/

//        Log.e("int_sum_total_time_inside_method", "" + int_sum_total_time);
//        Log.e("BEFORE_total_onclick_time_ArrayList_inside_method", "" + total_onclick_time_ArrayList.toString());
//        total_onclick_time_ArrayList.add(String.valueOf(str_duplicate_onclick_time));
        total_onclick_time_ArrayList.add(str_duplicate_onclick_time);
//        Log.e("AFTER_total_onclick_time_ArrayList_inside_method", "" + total_onclick_time_ArrayList.toString());


        /*for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
        }*/

//        Log.e("int_sum_total_time", "" + int_sum_total_time);
        for (int i = 0; i < total_onclick_correct_answer_values_ArrayList.size(); i++) {
            int_sum_total_crct_ans += total_onclick_correct_answer_values_ArrayList.get(i);
        }
        for (int i = 0; i < total_onclick_wrong_answer_values_ArrayList.size(); i++) {
            int_sum_total_wrng_ans += total_onclick_wrong_answer_values_ArrayList.get(i);
        }

        for (int i = 0; i < total_skip_values_ArrayList.size(); i++) {
            int_sum_total_skip_values += total_skip_values_ArrayList.get(i);
        }


        for (int i = 0; i < answers_string_velue_selection_ArrayList.size(); i++) {
            if (i < answers_string_velue_selection_ArrayList.size() - 1) {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i).concat(","));
            } else {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i));
            }
        }







        /*for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            try {
                Log.e("Before_for_loop", "" + Integer.parseInt(total_onclick_time_ArrayList.get(i)));
                int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
                Log.e("After_for_loop", "" + int_sum_total_time);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            if (i < total_onclick_time_ArrayList.size() - 1) {
                str_onclick_time_values.append(total_onclick_time_ArrayList.get(i).concat(","));
            } else {
                str_onclick_time_values.append(total_onclick_time_ArrayList.get(i));
            }
        }*/
        if (int_correct_ans_notations == 1) {
            total_all_onclick_point_values_ArrayList.add(String.valueOf(int_correct_ans));
            total_all_onclick_point_values_ArrayList.add(String.valueOf(int_skip));
        } else if (int_correct_ans_notations == 0) {
            total_all_onclick_point_values_ArrayList.add(String.valueOf(int_correct_ans));
            total_all_onclick_point_values_ArrayList.add(String.valueOf(int_skip));
        }
//        Log.e("total_onclick_correct_answer_values_ArrayList_wt", total_onclick_correct_answer_values_ArrayList.toString());
        total_all_onclick_point_values_ArrayList.add(str_duplicate_onclick_point_value);
        for (int i = 0; i < total_all_onclick_point_values_ArrayList.size(); i++) {
            if (i < total_all_onclick_point_values_ArrayList.size() - 1) {
                str_onclick_point_values.append(total_all_onclick_point_values_ArrayList.get(i).concat(","));
            } else {
                str_onclick_point_values.append(total_all_onclick_point_values_ArrayList.get(i));
            }

        }

        for (int i = 0; i < duplicate_onclick_point_ArrayList.size(); i++) {
            int_duplicate_total_value += Integer.parseInt(duplicate_onclick_point_ArrayList.get(i));
        }
//        Log.e("int_duplicate_total_value_logout_alert", "" + int_duplicate_total_value);
//        Log.e("inside_method_skip_value_logout_alert", "" + duplicate_onclick_point_ArrayList.toString());
//        Log.e("json_skip_value_arraylist", "" + total_skip_values_ArrayList.toString());


        for (int i = 0; i < duplicate_correct_answerselection_ArrayList.size(); i++) {
            int_duplicate_corect_answer_value += Integer.parseInt(duplicate_correct_answerselection_ArrayList.get(i));
        }
//        Log.e("int_duplicate_corect_answer_value_logout_alert", "" + int_duplicate_corect_answer_value);
//        Log.e("duplicate_correct_answerselection_ArrayList_inside_method", "" + duplicate_correct_answerselection_ArrayList.toString());

        int_difference_onclick_values = int_sum_total_crct_ans - int_sum_total_wrng_ans;
        int_difference_final_values = int_difference_onclick_values + int_sum_total_skip_values;
//        Log.e("int_total_onclick_time_wt", "" + int_total_onclick_time);
//        Log.e("sum_total_crct_ans_wt", "" + int_sum_total_crct_ans);
//        Log.e("sum_total_wrng_ans_wt", "" + int_sum_total_wrng_ans);
//        Log.e("sum_total_skp_vlus_wt", "" + int_sum_total_skip_values);
//        Log.e("diff_onclick_values_wt", "" + int_difference_onclick_values);
//        Log.e("diff_final_values_wt", "" + int_difference_final_values);
//        Log.e("str_onclick_point_values_wt", "" + str_onclick_point_values);
//        Log.e("total_onclick_time_ArrayList_wt", String.valueOf(total_onclick_time_ArrayList.size() - 1));
//        Log.e("prev_r_crnt_qstn_num", "" + int_previous_or_courrent_question_number);
        dialog_fr_timer.dismiss();
        countDownTimer.cancel();
        int finalInt_sum_total_time = int_total_onclick_time;
        String finalStr_selection_values = str_selection_values.toString();
        str_remaining_count_value = tv_remaing_count_value_in_game_two.getText().toString();
        int n1_1 = Integer.parseInt(str_remaining_count_value);
        try {
            dialog_fr_timer.dismiss();
            countDownTimer.cancel();
            JSONObject jsonObject = new JSONObject();
            if (str_question_type.equals("3")) {
//                Log.e("str_auth_token_if_wt", str_auth_token);
                jsonObject.put("email", str_email);
                jsonObject.put("contest_id", str_contest_id);
                jsonObject.put("total_onclick_time", finalInt_sum_total_time);
                jsonObject.put("total_onclick_answer_values", int_difference_onclick_values);
                jsonObject.put("contest_answer", finalStr_selection_values);
                jsonObject.put("onclick_time", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
                jsonObject.put("contest_2x", str_duplicate_2x_power_values);
                /*jsonObject.put("click_points_correct", "");
                jsonObject.put("click_points_skip", "");
                jsonObject.put("click_points_wrong", "");*/

                jsonObject.put("click_points_skip", duplicate_onclick_point_ArrayList.toString().replace("[", "").replace("]", ""));
                jsonObject.put("click_points_wrong", total_onclick_wrong_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
                jsonObject.put("click_points_correct", duplicate_correct_answerselection_ArrayList.toString().replace("[", "").replace("]", ""));

//              jsonObject.put("contest_2x", _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", ""));
//              jsonObject.put("click_points_correct", total_all_onclick_point_values_ArrayList.toString().replace("[", "").replace("]", "") + total_onclick_correct_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));

                Log.e("IF_Total_Json_Values_wt", jsonObject.toString());
            } else {
                Log.e("str_auth_token_else_wt", str_auth_token);
                jsonObject.put("email", str_email);
                jsonObject.put("contest_id", str_contest_id);
                jsonObject.put("total_onclick_time", finalInt_sum_total_time);
                jsonObject.put("total_onclick_answer_values", int_difference_onclick_values);
                jsonObject.put("contest_answer", finalStr_selection_values);
                jsonObject.put("onclick_time", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
                jsonObject.put("contest_2x", str_duplicate_2x_power_values);
                jsonObject.put("click_points_skip", duplicate_onclick_point_ArrayList.toString().replace("[", "").replace("]", ""));
                jsonObject.put("click_points_wrong", total_onclick_wrong_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
                jsonObject.put("click_points_correct", duplicate_correct_answerselection_ArrayList.toString().replace("[", "").replace("]", ""));
//              jsonObject.put("click_points_correct", total_all_onclick_point_values_ArrayList.toString().replace("[", "").replace("]", "") + total_onclick_correct_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
//              jsonObject.put("contest_2x", _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", ""));
                Log.e("ELSE_Total_Json_Values_wt", jsonObject.toString());
            }

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_FINAL_RESULT_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {

                            /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live ,
                             or else 2 means contest is completed*/
                            int_play_status = 2;
                            Getting_Update_Status_Details_Initial(int_play_status);
                            str_playby = "Coins";
                            Get_Points_Add_Delete_Details(str_playby);


//                            Log.e("without_crct_res", "trueeee");
                            constraintLayout_game_screen_in_game_two.setVisibility(View.GONE);
                            countDownTimer.cancel();
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            Getting_Update_Status_Details_Without_End_Game_Screen();
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Two_Act.this, response.message());
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

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
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
            }
        });

        shimmer_view_container_winning_list_game_two.startShimmerAnimation();

        /*This components are used for Rewarded video*/
        if (!mGameOver && mGamePaused) {
            resumeGame();
        }
        IronSource.onResume(Game_Two_Act.this);
//        mRewardedVideoAd.resume(Game_Two_Act.this);
    }

    @Override
    protected void onPause() {
        super.onPause();


        shimmer_view_container_winning_list_game_two.stopShimmerAnimation();
        /*if (countDownTimer != null) {
            countDownTimer.cancel();
        }*/
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        } else {
            vibrator.cancel();
        }
        /*This components are used for Rewarded video*/
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
        /*This components are used for Rewarded video*/
        mRewardedVideoAd.destroy(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    /*This components are used for Rewarded video*/
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mGamePaused = savedInstanceState.getBoolean(GAME_PAUSE_KEY);
        mGameOver = savedInstanceState.getBoolean(GAME_OVER_KEY);
        mTimeRemaining = savedInstanceState.getLong(TIME_REMAINING_KEY);
        mCoinCount = savedInstanceState.getInt(COIN_COUNT_KEY);
        mCoinCountText.setText(String.valueOf(mCoinCount));
    }

    /*This components are used for Rewarded video*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(GAME_PAUSE_KEY, mGamePaused);
        outState.putBoolean(GAME_OVER_KEY, mGameOver);
        outState.putLong(TIME_REMAINING_KEY, mTimeRemaining);
        outState.putInt(COIN_COUNT_KEY, mCoinCount);
        super.onSaveInstanceState(outState);
    }

    /*This components are used for Rewarded video*/
    private void pauseGame() {
        mGamePaused = true;
    }

    /*This components are used for Rewarded video*/
    private void resumeGame() {
        mGamePaused = false;
    }

}

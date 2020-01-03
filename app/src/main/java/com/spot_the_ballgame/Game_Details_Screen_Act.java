package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Adapter.Price_List_Adapter;
import com.spot_the_ballgame.Adapter.Rules_List_Name_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_Details_Screen_Act extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    ConstraintLayout constraintLayout_game_details_screen_in_game_two,
            constraintLayout_game_details_screen_in_game_one,
            constraintLayout_just_a_moment_contest_one,
            constraintLayout_rules_list_contest_one,
            constraintLayout_watch_ads_btn_inside,
            constraintLayout_score_board_layout,
            constraintLayout_rewarded_video,
            constraintLayout_watch_ads_btn,
            constraintLayout_prize_layout;

    TextView tv_watch_ads_btn_contest_one,
            tv_score_board_contest_one,
            tv_price_btn_contest_one,
            tv_rules_btn_contest_one,
            tv_enter_contest_one,
            tv_entry_fee_details,
            tv_earn_coins,
            tv_loading_tx,
            tv_play_btn,
            tv_coins;
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
            str_game_end_time,
            str_count_down_seconds,
            str_correct_ans,
            str_2x_powerup,
            str_contest_id,
            str_entry_fees,
            str_wrong_ans,
            str_imagepath,
            str_phone_no,
            str_balance,
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
    private ShimmerFrameLayout mShimmerViewContainer, shimmer_view_container_for_rules;

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
    Rules_List_Name_Adapter rules_list_name_adapter;
    String str_rule_id;
    String str_auth_token;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(Game_Details_Screen_Act.this).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_just_a_moment_contest_one = findViewById(R.id.constraintLayout_just_a_moment_contest_one);
        constraintLayout_rules_list_contest_one = findViewById(R.id.constraintLayout_rules_list_contest_one);
        constraintLayout_watch_ads_btn_inside = findViewById(R.id.constraintLayout_watch_ads_btn_inside);
        constraintLayout_score_board_layout = findViewById(R.id.constraintLayout_score_board_layout);
        constraintLayout_rewarded_video = findViewById(R.id.constraintLayout_rewarded_video);
        constraintLayout_watch_ads_btn = findViewById(R.id.constraintLayout_watch_ads_btn);
        constraintLayout_prize_layout = findViewById(R.id.constraintLayout_prize_layout);

        constraintLayout_game_details_screen_in_game_one = findViewById(R.id.constraintLayout_game_details_screen_in_game_one);
        tv_game_end_time_game_details = findViewById(R.id.tv_game_end_time_game_details);
        tv_watch_ads_btn_contest_one = findViewById(R.id.tv_watch_ads_btn_contest_one);
        tv_prize_amount_game_details = findViewById(R.id.tv_prize_amount_game_details);
        tv_score_board_contest_one = findViewById(R.id.tv_score_board_contest_one);
        tv_price_btn_contest_one = findViewById(R.id.tv_price_btn_contest_one);
        tv_rules_btn_contest_one = findViewById(R.id.tv_rules_btn_contest_one);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        shimmer_view_container_for_rules = findViewById(R.id.shimmer_view_container_for_rules);

        tv_enter_contest_one = findViewById(R.id.tv_enter_contest_one);
        tv_entry_fee_details = findViewById(R.id.tv_entry_fee_details);
        rv_price_list = findViewById(R.id.rv_price_list);
        tv_game_name = findViewById(R.id.tv_game_name);
        tv_entry_fee_details_coins = findViewById(R.id.tv_entry_fee_details_coins);
        tv_earn_coins = findViewById(R.id.tv_earn_coins);
        tv_play_btn = findViewById(R.id.tv_play_btn);
        tv_coins = findViewById(R.id.tv_coins);
        iv_ready_steady_go_state = findViewById(R.id.iv_ready_steady_go_state);

        rv_rules_list_game_two = findViewById(R.id.rv_rules_list_game_two);
        rv_rules_list_game_two.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


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
        Log.e("str_balance_game", str_balance);

        str_auth_token = SessionSave.getSession("Token_value", Game_Details_Screen_Act.this);
        Log.e("authtoken_gamedetail", str_auth_token);

        rv_price_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_price_list.setVisibility(View.VISIBLE);
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            constraintLayout_game_details_screen_in_game_two.setVisibility(View.VISIBLE);
            Get_Prize_List_Details();
        }
//        Bundle bundle = getIntent().getExtras();
//        if (bundle == null) {
//            str_onclick_contest_value = null;
//        } else {
//            str_onclick_contest_value = bundle.getString("onclick_contest_value");
//        }
//        Log.e("str_onclick_contest", str_onclick_contest_value);
//        if (str_onclick_contest_value == null) {
//            tv_watch_ads_btn_contest_one.setVisibility(View.VISIBLE);
//            tv_enter_contest_one.setVisibility(View.VISIBLE);
//        } else {
//            tv_watch_ads_btn_contest_one.setVisibility(View.GONE);
//            tv_enter_contest_one.setVisibility(View.GONE);
//        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            str_game_name = null;
        } else {
            str_game_name = bundle.getString("game_name");
            str_prize_amount = bundle.getString("prize_amount");
            str_game_end_time = bundle.getString("end_time");
            str_count_down_seconds = bundle.getString("count_down_seconds");
            str_2x_powerup = bundle.getString("str_2x_powerup");
            str_contest_id = bundle.getString("str_contest_id");
            str_entry_fees = bundle.getString("str_entry_fees");
            str_imagepath = bundle.getString("str_imagepath");
            str_rule_id = bundle.getString("str_rule_id");


            str_correct_ans = bundle.getString("str_correct_ans");
            str_wrong_ans = bundle.getString("str_wrong_ans");
            str_skip = bundle.getString("str_skip");


            tv_game_name.setText(str_game_name);
            tv_prize_amount_game_details.setText(str_prize_amount);
            tv_game_end_time_game_details.setText(str_game_end_time);
            tv_entry_fee_details.setText(str_entry_fees);
        }
        if (str_entry_fees.equalsIgnoreCase("Free")) {
            tv_entry_fee_details_coins.setVisibility(View.GONE);
        } else {
            tv_entry_fee_details_coins.setVisibility(View.VISIBLE);
        }

        try {
            int_entry_fee = Integer.parseInt(str_entry_fees);
            int_correct_ans = Integer.parseInt(str_correct_ans);
            int_wrong_ans = Integer.parseInt(str_wrong_ans);
            int_skip = Integer.parseInt(str_skip);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        int_db_balance = Integer.parseInt(str_balance);
        if (int_entry_fee > int_db_balance) {
            Log.e("topinside_entry_free", String.valueOf(int_entry_fee));
            Log.e("topinside_balance", String.valueOf(int_db_balance));
            int n11 = int_entry_fee - int_db_balance;
            Log.e("n1111", String.valueOf(n11));
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

        Glide.with(Game_Details_Screen_Act.this).load(str_imagepath)
                .thumbnail(Glide.with(Game_Details_Screen_Act.this).load(str_imagepath))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_categoires_image_game_details_act);
        tv_price_btn_contest_one.setOnClickListener(this);
        tv_rules_btn_contest_one.setOnClickListener(this);
        tv_score_board_contest_one.setOnClickListener(this);
        tv_watch_ads_btn_contest_one.setOnClickListener(this);
        tv_enter_contest_one.setOnClickListener(this);
        constraintLayout_watch_ads_btn.setOnClickListener(this);
//        FullScreenMethod();

    }

    private void Get_Prize_List_Details() {
        /*try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_PRIZE_DISTRINUTION_CALL(jsonObject.toString(),str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() == null) {
                                Toast_Message.showToastMessage(Game_Details_Screen_Act.this, "Null_Response");
                            } else {
                                price_list_adapter = new Price_List_Adapter(Game_Details_Screen_Act.this, response.body().data);
                                rv_price_list.setAdapter(price_list_adapter);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            Log.e("volley_json", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Details_Screen_Act.this);
                //This is for SK
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.2.3/raffle/api/v1/get_prize_distribution", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                //This is for skyrand
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Factory.BASE_URL_MOBILE_APP + "get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_object = (JSONObject) jsonArray.get(i);
                                String prize_amount = json_object.getString("prize_amount");
                                String rank = json_object.getString("rank");
                                String no_prize_amount = json_object.getString("no_prize_amount");
                                String total_rank = json_object.getString("total_rank");
                                String total_amount = json_object.getString("total_amount");
                                Log.e("volley_prize_amount", prize_amount);
                                Log.e("volley_rank", rank);
                                Log.e("volley_no_prize", no_prize_amount);
                                Log.e("volley_total_rank", total_rank);
                                Log.e("volley_total_amount", total_amount);

                                try {
                                    price_list_adapter = new Price_List_Adapter(Game_Details_Screen_Act.this, prize_amount, rank);
                                    rv_price_list.setAdapter(price_list_adapter);
                                    mShimmerViewContainer.stopShimmerAnimation();
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
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
                if (int_entry_fee > int_db_balance) {
                    int n11 = int_entry_fee - int_db_balance;
//                    Log.e("clk_n1111", String.valueOf(n11));
                    constraintLayout_watch_ads_btn.setVisibility(View.VISIBLE);
                    constraintLayout_watch_ads_btn_inside.setVisibility(View.VISIBLE);

                    tv_earn_coins.setVisibility(View.VISIBLE);
                    tv_play_btn.setVisibility(View.GONE);
                    tv_earn_coins.setText(String.valueOf(n11));
                    tv_coins.setVisibility(View.VISIBLE);
                    constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);
                    constraintLayout_rewarded_video.setVisibility(View.VISIBLE);
                    Get_Rewarded_Video_Method(savedInstanceState);
                } else {
                    tv_play_btn.setVisibility(View.VISIBLE);
                    constraintLayout_watch_ads_btn.setBackground(getResources().getDrawable(R.drawable.game_list_bg_free_btn));
                    constraintLayout_watch_ads_btn_inside.setVisibility(View.GONE);
                    Get_Enter_Game_Page();
                    str_playby = "Coins";
                    Get_Points_Add_Delete_Details(str_playby);
                    Get_User_Wallet_Details();
                }

                break;
            case R.id.tv_price_btn_contest_one:
                rv_rules_list_game_two.setVisibility(View.GONE);
                Get_Prize_List_Details();
                tv_price_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_price_btn_contest_one.setTextColor(getResources().getColor(R.color.white_color));
//                constraintLayout_prize_layout.setVisibility(View.VISIBLE);

                rv_price_list.setVisibility(View.VISIBLE);
                constraintLayout_rules_list_contest_one.setVisibility(View.GONE);
                tv_rules_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_rules_btn_contest_one:
                mShimmerViewContainer.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                shimmer_view_container_for_rules.startShimmerAnimation();
//                constraintLayout_prize_layout.setVisibility(View.GONE);
                rv_price_list.setVisibility(View.GONE);
                constraintLayout_rules_list_contest_one.setVisibility(View.VISIBLE);
                tv_rules_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn_contest_one.setTextColor(getResources().getColor(R.color.white_color));
                tv_price_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));

                Get_Rules_Name_Details();
                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_watch_ads_btn_contest_one:
//                Toast.makeText(this, "dkmdsld", Toast.LENGTH_SHORT).show();
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
//                Game_Details_Screen_Act.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                constraintLayout_game_details_screen_in_game_two.setVisibility(View.GONE);
//                constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);
                break;
            case R.id.tv_enter_contest_one:
                Intent intent_01 = new Intent(Game_Details_Screen_Act.this, Reward_Video_Act.class);
                intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_01);
                break;
            case R.id.tv_score_board_contest_one:
                mShimmerViewContainer.setVisibility(View.GONE);
                constraintLayout_score_board_layout.setVisibility(View.VISIBLE);
//                constraintLayout_prize_layout.setVisibility(View.GONE);
                rv_price_list.setVisibility(View.GONE);
                constraintLayout_rules_list_contest_one.setVisibility(View.GONE);
                rv_rules_list_game_two.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.GONE);
                tv_score_board_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board_contest_one.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                tv_price_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                break;
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
                    assert response.body() != null;
                    rv_rules_list_game_two.setVisibility(View.VISIBLE);
                    constraintLayout_rules_list_contest_one.setVisibility(View.VISIBLE);
                    shimmer_view_container_for_rules.stopShimmerAnimation();
                    shimmer_view_container_for_rules.setVisibility(View.GONE);
                    rules_list_name_adapter = new Rules_List_Name_Adapter(getApplicationContext(), response.body().data);
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

    private void Get_Enter_Game_Page() {
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
                    if (response.isSuccessful()) {
                        str_code = response.body().code;
                        str_message = response.body().message;
                        str_wallet1 = response.body().data.get(0).wallet1;
                        str_wallet2 = response.body().data.get(0).wallet2;
//                        Log.e("str_wallet1111", str_wallet1);
//                        Log.e("str_wallet2", str_wallet2);
                        double_db_balance = Double.parseDouble(str_wallet1);
                        int_db_balance = (int) double_db_balance;
//                        Log.e("int_balance_convert", String.valueOf(int_db_balance));


                        if (int_entry_fee > int_db_balance) {
//                            Log.e("topinside_entry_free", String.valueOf(int_entry_fee));
//                            Log.e("topinside_balance", String.valueOf(int_db_balance));
                            int n11 = int_entry_fee - int_db_balance;
//                            Log.e("n1111", String.valueOf(n11));
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
//                Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                addCoins(10);
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
                constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);
                constraintLayout_rewarded_video.setVisibility(View.GONE);
//                Toast.makeText(getBaseContext(), "Ad closeddddd.", Toast.LENGTH_SHORT).show();
                AD_Closed_Method();
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
            }
        });

        if (savedInstanceState == null) {
            mCoinCount = 0;
            mCoinCountText.setText(String.valueOf(mCoinCount));
            startGame_Rewarded_Video();
        }
    }

    private void AD_Closed_Method() {
        recreate();
        String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_balance = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        int_db_balance_for_reward_video = Integer.parseInt(str_balance);
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
        Get_Points_Add_Delete_Details(str_playby);
        Get_User_Wallet_Details();
        if (int_entry_fee > int_total_value) {
            recreate();
            int n11 = int_entry_fee - int_total_value;
            tv_earn_coins.setText(String.valueOf(n11));
//            Log.e("int_entry_fee_ad", "" + int_entry_fee);
//            Log.e("n1111", "" + n11);
//            Log.e("n2222", "" + int_rewarded_coins_point);
//            Log.e("n3333", "" + int_total_value);
//            Toast.makeText(Game_Details_Screen_Act.this, "if_side", Toast.LENGTH_SHORT).show();
        } else {
            recreate();
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
        constraintLayout_just_a_moment_contest_one.setVisibility(View.GONE);

        Glide.with(Game_Details_Screen_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                constraintLayout_game_details_screen_in_game_one.setVisibility(View.VISIBLE);
              /*  if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    startTimer(milliseconds);
                }*/
            }

        }, 5500);
    }

    private void Get_Points_Add_Delete_Details(String str_playby) {
        try {
            String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
            Cursor cursor = db.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {
                    str_balance = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            jsonObject.put("wallet", mCoinCountText.getText().toString());
            jsonObject.put("playby", str_playby);
            Log.e("add_dlt_json_value", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_WalletDetailsModelCall("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
//                    Toast.makeText(Game_Details_Screen_Act.this, "Sucessssss", Toast.LENGTH_SHORT).show();
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
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText(String.valueOf(mCoinCount));
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
            snackbar.getView().setBackgroundResource(R.color.black_color);
            Get_Prize_List_Details();
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
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        mShimmerViewContainer.stopShimmerAnimation();
    }
}

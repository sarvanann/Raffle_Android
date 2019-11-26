package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Adapter.Price_List_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory_For_Categories;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_Details_Screen_Act extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout constraintLayout_game_details_screen_in_game_two,
            constraintLayout_prize_layout,
            constraintLayout_rules_list_contest_one,
            constraintLayout_score_board_layout,
            constraintLayout_just_a_moment_contest_one, constraintLayout_game_details_screen_in_game_one;
    TextView tv_price_btn_contest_one, tv_rules_btn_contest_one, tv_watch_ads_btn_contest_one, tv_enter_contest_one, tv_score_board_contest_one, tv_entry_fee_details, tv_music_player_icon;
    String str_onclick_contest_value = "";
    String str_game_name, str_prize_amount, str_game_end_time, str_count_down_seconds, str_2x_powerup, str_contest_id, str_entry_fees;
    TextView tv_game_name, tv_prize_amount_game_details, tv_game_end_time_game_details;

    RecyclerView rv_price_list;
    Price_List_Adapter price_list_adapter;

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    private ShimmerFrameLayout mShimmerViewContainer;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen);
        getSupportActionBar().hide();
        tv_price_btn_contest_one = findViewById(R.id.tv_price_btn_contest_one);
        tv_rules_btn_contest_one = findViewById(R.id.tv_rules_btn_contest_one);
        tv_watch_ads_btn_contest_one = findViewById(R.id.tv_watch_ads_btn_contest_one);
        tv_enter_contest_one = findViewById(R.id.tv_enter_contest_one);
        tv_score_board_contest_one = findViewById(R.id.tv_score_board_contest_one);
        tv_entry_fee_details = findViewById(R.id.tv_entry_fee_details);
        constraintLayout_prize_layout = findViewById(R.id.constraintLayout_prize_layout);
        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_rules_list_contest_one = findViewById(R.id.constraintLayout_rules_list_contest_one);
        constraintLayout_score_board_layout = findViewById(R.id.constraintLayout_score_board_layout);
        constraintLayout_just_a_moment_contest_one = findViewById(R.id.constraintLayout_just_a_moment_contest_one);
        tv_game_name = findViewById(R.id.tv_game_name);
        tv_music_player_icon = findViewById(R.id.tv_music_player_icon);
        tv_prize_amount_game_details = findViewById(R.id.tv_prize_amount_game_details);
        tv_game_end_time_game_details = findViewById(R.id.tv_game_end_time_game_details);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        constraintLayout_game_details_screen_in_game_one = findViewById(R.id.constraintLayout_game_details_screen_in_game_one);

        rv_price_list = findViewById(R.id.rv_price_list);
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
            tv_game_name.setText(str_game_name);
            tv_prize_amount_game_details.setText(str_prize_amount);
            tv_game_end_time_game_details.setText(str_game_end_time);
            tv_entry_fee_details.setText(str_entry_fees);
        }

        tv_price_btn_contest_one.setOnClickListener(this);
        tv_rules_btn_contest_one.setOnClickListener(this);
        tv_score_board_contest_one.setOnClickListener(this);
        tv_watch_ads_btn_contest_one.setOnClickListener(this);
        tv_enter_contest_one.setOnClickListener(this);
//        FullScreenMethod();

    }

    private void Get_Prize_List_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            APIInterface apiInterface = Factory_For_Categories.getClient();
            Call<Category_Model> call = apiInterface.GET_PRIZE_DISTRINUTION_CALL(jsonObject.toString());
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
            case R.id.tv_price_btn_contest_one:
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
//                constraintLayout_prize_layout.setVisibility(View.GONE);
                rv_price_list.setVisibility(View.GONE);
                constraintLayout_rules_list_contest_one.setVisibility(View.VISIBLE);
                tv_rules_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn_contest_one.setTextColor(getResources().getColor(R.color.white_color));
                tv_price_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));

                constraintLayout_score_board_layout.setVisibility(View.GONE);
                tv_score_board_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_watch_ads_btn_contest_one:
                Intent intent = new Intent(Game_Details_Screen_Act.this, Game_Act.class);
                intent.putExtra("str_count_down_seconds", str_count_down_seconds);
                intent.putExtra("str_2x_powerup", str_2x_powerup);
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
                tv_score_board_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board_contest_one.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                tv_price_btn_contest_one.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn_contest_one.setTextColor(getResources().getColor(R.color.black_color));
                break;
        }
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

package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.adcolony.sdk.AdColony;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.ironsource.mediationsdk.IronSource;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Reward_Video_Act extends Activity {
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
    ImageView gif_image;
    String str_session_reward_amount, str_playby, str_email, str_auth_token;
    SQLiteDatabase db;
    ConstraintLayout constraintLayout_rewarded_video_top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward__video_);
        db = Objects.requireNonNull(Reward_Video_Act.this).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        tv_loading_tx = findViewById(R.id.tv_loading_txt);
        gif_image = findViewById(R.id.gif_image);
        constraintLayout_rewarded_video_top = findViewById(R.id.constraintLayout_rewarded_video_top);
        Glide.with(this).asGif().load(R.drawable.giphy).into(gif_image);
        progress_bar_in_reward_video = findViewById(R.id.progress_bar_in_reward_video);


        str_auth_token = SessionSave.getSession("Token_value", Reward_Video_Act.this);
//        Log.e("authtoken_game2", str_auth_token);

        String select = "select EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mCoinCountText = findViewById(R.id.coin_count_text);

        //Ironsource_code
        IronSource.setConsent(true);
        //This is for ad colony
        AdColony.configure(this, "appd9b3bb873a744248bd", "vz09df69e0202642b88a");
        str_session_reward_amount = SessionSave.getSession("Reward_Point", Reward_Video_Act.this);
        //This is used for Full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FullScreenMethod();
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
//                Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
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

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onRewardedVideoAdClosed() {
                recreate();
                onBackPressed();
                /*recreate();
                str_playby = "Ads";
                Get_Points_Add_Delete_Details(str_playby);
                Get_Balance_Details();
                onBackPressed();*/

//                Log.e("ad_closed_log", "Ad closed");
//                Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();

               /* finish();
                startActivity(getIntent());*/
                /*Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);*/

                /*Intent intent = new Intent(Reward_Video_Act.this, Game_Two_Act.class);
                startActivity(intent);*/

            }

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
//                Log.e("rewd_session_point_act", "" + n1);
                addCoins(n1);
            }
        });

        if (savedInstanceState == null) {
            mCoinCountText.setText(String.valueOf(mCoinCount));
            startGame();
        }
    }

    private void Get_Points_Add_Delete_Details(String str_playby) {
        Log.e("mCoinssssss", mCoinCountText.getText().toString());
        try {
            JSONObject jsonObject = new JSONObject();
            if (str_playby.equalsIgnoreCase("Ads")) {
                jsonObject = new JSONObject();
                jsonObject.put("email", str_email);
                jsonObject.put("wallet", mCoinCountText.getText().toString());
                jsonObject.put("playby", str_playby);
//                Log.e("add_dlt_json_value_ad", jsonObject.toString());
            }
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_Wallet_Point_Delete_Call("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
//                    Get_Balance_Details();
//                    Log.e("gfgdsdfdfs", Objects.requireNonNull(response.body()).current_amt);
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get_Balance_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("ad_cls_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_WALLET_BALALNCE_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
//                        Log.e("reward_amount", response.body().current_amt + str_session_reward_amount);
                        String str_curnt_blnc_frm_api = response.body().current_amt;
                        Log.e("str_curnt_blnc_frm_api", str_curnt_blnc_frm_api);
                        int int_current_balance_frm_api = Integer.parseInt(str_curnt_blnc_frm_api);
                        int int_session_blnc = Integer.parseInt(str_session_reward_amount);
                        int int_calculated_balance = int_current_balance_frm_api + int_session_blnc;
                        Log.e("int_current_balance_frm_api", "" + int_current_balance_frm_api);
                        Log.e("int_session_blnc", "" + int_session_blnc);
                        Log.e("int_calculated_balance", "" + int_calculated_balance);
                        int int_current_balance = int_current_balance_frm_api + int_session_blnc;
                        Log.e("int_calculated_balance", "" + int_calculated_balance);
                        Navigation_Drawer_Act.tv_points.setText(String.valueOf(int_calculated_balance));
                        Toast_Message_For_Reward.showToastMessage(Reward_Video_Act.this, "You have earned " + int_session_blnc + " coins." + "\n Your current coin balance is " + int_calculated_balance + ".");

                        /*Here we use this method point value not gets conflicted and balance value added to api using post method. */
                        str_playby = "Ads";
                        Get_Points_Add_Delete_Details(str_playby);

                        recreate();
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

    private void showRewardedVideo_New() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mGamePaused = savedInstanceState.getBoolean(GAME_PAUSE_KEY);
        mGameOver = savedInstanceState.getBoolean(GAME_OVER_KEY);
        mTimeRemaining = savedInstanceState.getLong(TIME_REMAINING_KEY);
        mCoinCount = savedInstanceState.getInt(COIN_COUNT_KEY);
        mCoinCountText.setText(String.valueOf(mCoinCount));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(GAME_PAUSE_KEY, mGamePaused);
        outState.putBoolean(GAME_OVER_KEY, mGameOver);
        outState.putLong(TIME_REMAINING_KEY, mTimeRemaining);
        outState.putInt(COIN_COUNT_KEY, mCoinCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseGame();
        IronSource.onPause(this);
        mRewardedVideoAd.pause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRewardedVideoAd.destroy(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGameOver && mGamePaused) {
            resumeGame();
        }
        IronSource.onResume(this);
        mRewardedVideoAd.resume(this);
    }

    private void pauseGame() {
        mGamePaused = true;
    }

    private void resumeGame() {
        mGamePaused = false;
    }

    private void addCoins(int coins) {
        Log.e("coinscoins", "" + coins);
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText(String.valueOf(mCoinCount));
        Log.e("ccccccccccccccccc", mCoinCountText.getText().toString());
        Get_Balance_Details();
        onBackPressed();
    }

    private void startGame() {
//        Log.e("kklklkl",mRewardedVideoAd.toString());
        mGamePaused = false;
        mGameOver = false;
        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }


    public void showRewardedVideo(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this, "backpress", Toast.LENGTH_SHORT).show();
    }
}

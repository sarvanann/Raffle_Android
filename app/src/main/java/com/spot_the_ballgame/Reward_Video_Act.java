package com.spot_the_ballgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.ironsource.mediationsdk.IronSource;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward__video_);

        tv_loading_tx = findViewById(R.id.tv_loading_txt);
        gif_image = findViewById(R.id.gif_image);
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

        //This is used for Full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FullScreenMethod();
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
                Log.e("ad_closed_log", "Ad closed");
                Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Reward_Video_Act.this, Game_Two_Act.class);
                startActivity(intent);
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
            mCoinCountText.setText("Coins: " + mCoinCount);

            startGame();
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
        mCoinCountText.setText("Coins: " + mCoinCount);
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
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText("Coins: " + mCoinCount);
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
        Toast.makeText(this, "backpress", Toast.LENGTH_SHORT).show();
    }
}

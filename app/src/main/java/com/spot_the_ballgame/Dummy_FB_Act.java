package com.spot_the_ballgame;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;



public class Dummy_FB_Act extends Activity {
    private static final long COUNTER_TIME = 10;
    private static final int GAME_OVER_REWARD = 1;
    private static final String TIME_REMAINING_KEY = "TIME_REMAINING";
    private static final String COIN_COUNT_KEY = "COIN_COUNT";
    private static final String GAME_PAUSE_KEY = "IS_GAME_PAUSED";
    private static final String GAME_OVER_KEY = "IS_GAME_OVER";

    private int mCoinCount;
    private TextView mCoinCountText;
    private CountDownTimer mCountDownTimer;
    private boolean mGameOver;
    private boolean mGamePaused;
    private RewardedVideoAd mRewardedVideoAd;
    private Button mRetryButton;
    private Button mShowVideoButton;
    private long mTimeRemaining;
    int int_video_loaded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward__video_);


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        // Create the "retry" button, which starts a new game.
        mRetryButton = ((Button) findViewById(R.id.retry_button));
        mRetryButton.setVisibility(View.INVISIBLE);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame();
 if(int_video_loaded==1){
                    startGame();
                }
                else {
                 //   Toast.makeText(getBaseContext(), "int_video_loaded is 0", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Hide the "watch video" until the end of a game.
        mShowVideoButton = ((Button) findViewById(R.id.watch_video));
        mShowVideoButton.setVisibility(View.INVISIBLE);

        mCoinCountText = ((TextView) findViewById(R.id.coin_count_text));

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
//                Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                // addCoins(rewardItem.getAmount());
                addCoins(10);
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                int_video_loaded = 1;
//                Toast.makeText(getBaseContext(), "Ad loaded.", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();
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
            mCoinCountText.setText("Coins: " + mCoinCount);
            startGame();
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
        if (mGameOver) {
            mRetryButton.setVisibility(View.VISIBLE);
        }
        if (mGameOver && mRewardedVideoAd.isLoaded()) {
            mShowVideoButton.setVisibility(View.VISIBLE);
        }
        mRewardedVideoAd.resume(this);
    }

    private void pauseGame() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mGamePaused = true;
    }

    private void resumeGame() {
        createTimer(mTimeRemaining);
        mGamePaused = false;
    }

    private void addCoins(int coins) {
        mCoinCount = mCoinCount + coins;
        mCoinCountText.setText("Coins: " + mCoinCount);
    }

    private void startGame() {
        // Hide the retry button and start the timer.
        mRetryButton.setVisibility(View.INVISIBLE);
        mShowVideoButton.setVisibility(View.INVISIBLE);
        createTimer(COUNTER_TIME);
        mGamePaused = false;
        mGameOver = false;


        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }

    // Create the game timer, which counts down to the end of the level.
    private void createTimer(long time) {
        final TextView textView = ((TextView) findViewById(R.id.timer));
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(time * 400, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimeRemaining = ((millisUnitFinished / 1000) + 1);
                textView.setText("seconds remaining: " + mTimeRemaining);
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        };
        mCountDownTimer.start();
    }

    private void gameOver() {
        final TextView textView = ((TextView) findViewById(R.id.timer));
        if (mRewardedVideoAd.isLoaded()) {
            mShowVideoButton.setVisibility(View.VISIBLE);
        }
        //  textView.setText("You Lose!");
        addCoins(GAME_OVER_REWARD);
        mRetryButton.setVisibility(View.VISIBLE);
        mGameOver = true;
    }

    public void showRewardedVideo(View view) {
        mShowVideoButton.setVisibility(View.INVISIBLE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
}

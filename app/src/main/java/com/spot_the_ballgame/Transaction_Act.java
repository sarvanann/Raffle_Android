package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Adapter.Wallet_History_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaction_Act extends AppCompatActivity implements View.OnClickListener {
    private AdView mAdView;
    Button btn_transaction_history, btn_redeem_history;
    ConstraintLayout constraintLayout_transaction_history, constraintLayout_redeem_history;
    String str_email, str_date, str_time, str_playby, str_values_in, str_values_out;
    RecyclerView rv_wallet_history;
    Wallet_History_Adapter wallet_history_adapter;
    SQLiteDatabase db;
    private ShimmerFrameLayout mShimmerViewContainer;

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_transaction_fragment);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(Transaction_Act.this).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        btn_redeem_history = findViewById(R.id.btn_redeem_history);
        btn_transaction_history = findViewById(R.id.btn_transaction_history);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        constraintLayout_transaction_history = findViewById(R.id.constraintLayout_transaction_history);
        constraintLayout_redeem_history = findViewById(R.id.constraintLayout_redeem_history);
        rv_wallet_history = findViewById(R.id.rv_wallet_history);

        rv_wallet_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_wallet_history.setHasFixedSize(true);
        rv_wallet_history.setVisibility(View.VISIBLE);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btn_transaction_history.setOnClickListener(this);
        btn_redeem_history.setOnClickListener(this);


        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            rv_wallet_history.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            Get_Wallet_Histroy();
        }
    }

    private void Get_Wallet_Histroy() {
        try {
            String select = "select EMAIL,PHONENO,BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
            Cursor cursor = db.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {
                    str_email = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_WALLET_HISTORY_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        mShimmerViewContainer.setVisibility(View.GONE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        wallet_history_adapter = new Wallet_History_Adapter(Transaction_Act.this, response.body().data);
                        rv_wallet_history.setAdapter(wallet_history_adapter);
                    }else {
                        mShimmerViewContainer.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.startShimmerAnimation();
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
    public void onBackPressed() {
        Transaction_Act.this.getSupportFragmentManager().popBackStack();
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_transaction_history:
                constraintLayout_transaction_history.setVisibility(View.VISIBLE);
                constraintLayout_redeem_history.setVisibility(View.GONE);

                btn_transaction_history.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                btn_transaction_history.setTextColor(getResources().getColor(R.color.black_color));

                btn_redeem_history.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                btn_redeem_history.setTextColor(getResources().getColor(R.color.history_grey_color));

                break;
            case R.id.btn_redeem_history:
                constraintLayout_redeem_history.setVisibility(View.VISIBLE);
                constraintLayout_transaction_history.setVisibility(View.GONE);

                btn_redeem_history.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                btn_redeem_history.setTextColor(getResources().getColor(R.color.black_color));

                btn_transaction_history.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                btn_transaction_history.setTextColor(getResources().getColor(R.color.history_grey_color));

                break;
        }
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
//            GetContest_Details();
        } else {
            rv_wallet_history.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
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

    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        mShimmerViewContainer.stopShimmerAnimation();
    }
}

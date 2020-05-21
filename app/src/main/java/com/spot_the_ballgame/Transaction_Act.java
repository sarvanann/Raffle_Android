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
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Adapter.Redeem_History_Adapter;
import com.spot_the_ballgame.Adapter.Wallet_History_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONObject;

import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaction_Act extends AppCompatActivity implements View.OnClickListener {
    private AdView mAdView;
    Button btn_transaction_history, btn_redeem_history;
    ConstraintLayout constraintLayout_transaction_history,
            constraintLayout_redeem_history,
            constraintLayout_transaction_top,
            constraintLayout_redeem_top;
    String str_email, str_date, str_time, str_playby, str_values_in, str_values_out;
    Wallet_History_Adapter wallet_history_adapter;
    Redeem_History_Adapter redeem_history_adapter;
    SQLiteDatabase db;
    private ShimmerLayout mShimmerViewContainer, mShimmerViewContainer_redeem_history;

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    String str_auth_token;

    RecyclerView rv_redeem_history;
    RecyclerView rv_wallet_history;
    TextView tv_no_data_available_fr_transaction_act, tv_no_data_available_fr_transaction_act_redeem;
    String str_one_signal = "";

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
        mShimmerViewContainer_redeem_history = findViewById(R.id.mShimmerViewContainer_redeem_history);
        constraintLayout_transaction_history = findViewById(R.id.constraintLayout_transaction_history);
        constraintLayout_redeem_history = findViewById(R.id.constraintLayout_redeem_history);
        constraintLayout_transaction_top = findViewById(R.id.constraintLayout_transaction_top);
        constraintLayout_redeem_top = findViewById(R.id.constraintLayout_redeem_top);
        rv_wallet_history = findViewById(R.id.rv_wallet_history);
        rv_redeem_history = findViewById(R.id.rv_redeem_history);

        tv_no_data_available_fr_transaction_act = findViewById(R.id.tv_no_data_available_fr_transaction_act);
        tv_no_data_available_fr_transaction_act_redeem = findViewById(R.id.tv_no_data_available_fr_transaction_act_redeem);


        str_one_signal = getIntent().getStringExtra("customkey");
        if ((str_one_signal != null)) {
            Toast.makeText(this, "From_One_Signal" + str_one_signal, Toast.LENGTH_SHORT).show();
            Log.e("str_one_signal_frm_trans", str_one_signal);
        }

        rv_wallet_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_wallet_history.setHasFixedSize(true);
        rv_wallet_history.setVisibility(View.VISIBLE);


        rv_redeem_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_redeem_history.setHasFixedSize(true);
        rv_redeem_history.setVisibility(View.VISIBLE);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btn_transaction_history.setOnClickListener(this);
        btn_redeem_history.setOnClickListener(this);

        str_auth_token = SessionSave.getSession("Token_value", Transaction_Act.this);
//        Log.e("authtoken_transact", str_auth_token);


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
            String select = "select EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
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
//            Log.e("json_transaction", jsonObject.toString());
//            Call<Category_Model> call = apiInterface.GET_WALLET_HISTORY_CALL("application/json", jsonObject.toString(), "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xOTIuMTY4LjIuM1wvcmFmZmxlXC9hcGlcL3YxXC9zaWdudXAiLCJpYXQiOjE1NzY5MDM3NTEsImV4cCI6MTU3OTQ5NTc1MSwibmJmIjoxNTc2OTAzNzUxLCJqdGkiOiJSMTRrOGM3V0tWbkhyZEtzIiwic3ViIjo5LCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.r7fI_g-PixK-pURw7_Vv6Kb6hgBG5wVxv2SBiaPxDJU");
            Call<Category_Model> call = apiInterface.GET_WALLET_HISTORY_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().data.isEmpty()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_no_data_available_fr_transaction_act.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                                        constraintLayout_redeem_top.setVisibility(View.GONE);
                                        constraintLayout_transaction_top.setVisibility(View.GONE);
                                        rv_wallet_history.setVisibility(View.GONE);
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                constraintLayout_transaction_top.setVisibility(View.VISIBLE);
                                constraintLayout_redeem_top.setVisibility(View.GONE);
                                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                                mShimmerViewContainer.setVisibility(View.VISIBLE);
                                mShimmerViewContainer.startShimmerAnimation();
                            } else {
                                constraintLayout_transaction_top.setVisibility(View.VISIBLE);
                                constraintLayout_redeem_top.setVisibility(View.GONE);
                                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                                rv_wallet_history.setVisibility(View.VISIBLE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                mShimmerViewContainer.stopShimmerAnimation();
                                wallet_history_adapter = new Wallet_History_Adapter(Transaction_Act.this, response.body().data);
                                rv_wallet_history.setAdapter(wallet_history_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Transaction_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Transaction_Act.this, response.message());
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
                mShimmerViewContainer.startShimmerAnimation();
                constraintLayout_transaction_top.setVisibility(View.VISIBLE);
                constraintLayout_redeem_top.setVisibility(View.VISIBLE);
                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                Get_Wallet_Histroy();
                break;
            case R.id.btn_redeem_history:
                constraintLayout_redeem_history.setVisibility(View.VISIBLE);
                constraintLayout_transaction_history.setVisibility(View.GONE);

                btn_redeem_history.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                btn_redeem_history.setTextColor(getResources().getColor(R.color.black_color));

                btn_transaction_history.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                btn_transaction_history.setTextColor(getResources().getColor(R.color.history_grey_color));
                mShimmerViewContainer_redeem_history.startShimmerAnimation();
                constraintLayout_transaction_top.setVisibility(View.VISIBLE);
                constraintLayout_redeem_top.setVisibility(View.VISIBLE);
                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                Get_Redeem_History();
                break;
        }
    }

    private void Get_Redeem_History() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_REDEEM_HISTORY_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().data.isEmpty()) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                                        constraintLayout_transaction_top.setVisibility(View.GONE);
                                        constraintLayout_redeem_top.setVisibility(View.GONE);
                                        mShimmerViewContainer_redeem_history.setVisibility(View.GONE);
                                        mShimmerViewContainer_redeem_history.stopShimmerAnimation();
                                        rv_redeem_history.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                constraintLayout_redeem_top.setVisibility(View.VISIBLE);
                                constraintLayout_transaction_top.setVisibility(View.VISIBLE);
                                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                                mShimmerViewContainer_redeem_history.setVisibility(View.VISIBLE);
                                mShimmerViewContainer_redeem_history.startShimmerAnimation();
                                rv_redeem_history.setVisibility(View.GONE);
                            } else {
                                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);
                                constraintLayout_redeem_top.setVisibility(View.VISIBLE);
                                constraintLayout_transaction_top.setVisibility(View.GONE);

                                rv_redeem_history.setVisibility(View.VISIBLE);
                                mShimmerViewContainer_redeem_history.setVisibility(View.GONE);
                                mShimmerViewContainer_redeem_history.stopShimmerAnimation();
                                redeem_history_adapter = new Redeem_History_Adapter(Transaction_Act.this, response.body().data);
                                rv_redeem_history.setAdapter(redeem_history_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Transaction_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Transaction_Act.this, response.message());
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
//            GetContest_Details();
        } else {
            rv_wallet_history.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
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
                Log.e("internetStatus_else", internetStatus);
                snackbar.show();
                internetConnected = false;

                tv_no_data_available_fr_transaction_act.setVisibility(View.GONE);
                tv_no_data_available_fr_transaction_act_redeem.setVisibility(View.GONE);

                rv_wallet_history.setVisibility(View.GONE);
                rv_redeem_history.setVisibility(View.GONE);

                mShimmerViewContainer_redeem_history.setVisibility(View.VISIBLE);
                mShimmerViewContainer_redeem_history.startShimmerAnimation();

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
            }
        } else {
            if (!internetConnected) {
                Log.e("internetStatus_if", internetStatus);
                internetConnected = true;
                snackbar.show();

                mShimmerViewContainer.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();

                mShimmerViewContainer_redeem_history.setVisibility(View.GONE);
                mShimmerViewContainer_redeem_history.stopShimmerAnimation();


                rv_wallet_history.setVisibility(View.VISIBLE);
                rv_redeem_history.setVisibility(View.VISIBLE);

                Get_Wallet_Histroy();
                Get_Redeem_History();
            }
        }
    }

    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer_redeem_history.startShimmerAnimation();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer_redeem_history.stopShimmerAnimation();
    }
}

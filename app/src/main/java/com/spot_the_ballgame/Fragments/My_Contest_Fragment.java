package com.spot_the_ballgame.Fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Adapter.History_Contest_Details_Adapter;
import com.spot_the_ballgame.Adapter.Live_Contest_Details_Adapter;
import com.spot_the_ballgame.Game_Details_Screen_Act_02;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Toast_Message;

import org.json.JSONObject;

import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class My_Contest_Fragment extends Fragment implements View.OnClickListener {
    SQLiteDatabase db;
    String str_db_values, str_balance;
    private TextView tv_no_data_available_fr_live_contest, tv_no_data_available_fr_history_contest;
    private TextView tv_present_contest, tv_history_contest;
    private ConstraintLayout constraintLayout_present_contest_layout,
            constraintLayout_history_contest_layout,
            constraintLayout_history_onclick_contest_layout;
    Intent intent;

    private RecyclerView rv_contest_live_details, rv_contest_history_details;
    private String str_email;
    private String str_auth_token;
    private Live_Contest_Details_Adapter live_contest_details_adapter;
    private History_Contest_Details_Adapter history_contest_details_adapter;
    private ShimmerLayout mShimmerViewContainer, shimmer_view_container_history;

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    private Snackbar snackbar;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_contest_fragment, container, false);
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        rv_contest_live_details = view.findViewById(R.id.rv_contest_live_details);
        rv_contest_history_details = view.findViewById(R.id.rv_contest_history_details);
        tv_no_data_available_fr_live_contest = view.findViewById(R.id.tv_no_data_available_fr_live_contest);
        tv_no_data_available_fr_history_contest = view.findViewById(R.id.tv_no_data_available_fr_history_contest);

        tv_present_contest = view.findViewById(R.id.tv_present_contest);
        tv_history_contest = view.findViewById(R.id.tv_history_contest);

        constraintLayout_present_contest_layout = view.findViewById(R.id.constraintLayout_present_contest_layout);
        constraintLayout_history_contest_layout = view.findViewById(R.id.constraintLayout_history_contest_layout);
        constraintLayout_history_onclick_contest_layout = view.findViewById(R.id.constraintLayout_history_onclick_contest_layout);


        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        shimmer_view_container_history = view.findViewById(R.id.shimmer_view_container_history);

        Navigation_Drawer_Act.tv_title_txt.setText(R.string.contest_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        tv_present_contest.setOnClickListener(this);
        tv_history_contest.setOnClickListener(this);


        constraintLayout_present_contest_layout.setVisibility(View.VISIBLE);
        constraintLayout_history_contest_layout.setVisibility(View.GONE);
        constraintLayout_history_onclick_contest_layout.setVisibility(View.GONE);

        ((SimpleItemAnimator) Objects.requireNonNull(rv_contest_live_details.getItemAnimator())).setSupportsChangeAnimations(false);
        rv_contest_live_details.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_contest_live_details.setHasFixedSize(true);
        rv_contest_live_details.setVisibility(View.VISIBLE);

        ((SimpleItemAnimator) Objects.requireNonNull(rv_contest_history_details.getItemAnimator())).setSupportsChangeAnimations(false);
        rv_contest_history_details.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_contest_history_details.setHasFixedSize(true);
        rv_contest_history_details.setVisibility(View.VISIBLE);
        String select = "select EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        str_auth_token = SessionSave.getSession("Token_value", Objects.requireNonNull(getActivity()));
//        Log.e("str_auth_token_contest", str_auth_token);
        mShimmerViewContainer.startShimmerAnimation();
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            Get_Live_Details();
        }
       /* db = Objects.requireNonNull(Objects.requireNonNull(getActivity()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null));
        String select = "select USER_SELECTION_VALUE  from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_db_values = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("str_source_details_contest", str_db_values);*/
        return view;
    }

    private void Get_Live_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_LIVE_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            mShimmerViewContainer.startShimmerAnimation();
                            assert response.body() != null;
                            String str_status = response.body().status;
                            String str_message = response.body().message;
                            if (str_status.equalsIgnoreCase("error")) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                                        tv_no_data_available_fr_live_contest.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_live_contest.setText(str_message);
                                        rv_contest_live_details.setVisibility(View.GONE);
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                mShimmerViewContainer.startShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.VISIBLE);
                            } else if (response.body().data.isEmpty()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                                        tv_no_data_available_fr_live_contest.setVisibility(View.VISIBLE);
                                        rv_contest_live_details.setVisibility(View.GONE);
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                mShimmerViewContainer.startShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.VISIBLE);
                            } else {
                                tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                                tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                                rv_contest_live_details.setVisibility(View.VISIBLE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                mShimmerViewContainer.stopShimmerAnimation();

                                live_contest_details_adapter = new Live_Contest_Details_Adapter(getActivity(), response.body().data);
                                rv_contest_live_details.setAdapter(live_contest_details_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                    }
                    /*} else {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Something went wrong try again :)");
                    }*/
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
//        Log.e("backStackCnt_contest", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

        /*Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
       /* Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_present_contest:
                rv_contest_live_details.setVisibility(View.GONE);
                rv_contest_history_details.setVisibility(View.GONE);
                constraintLayout_present_contest_layout.setVisibility(View.VISIBLE);
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                constraintLayout_history_contest_layout.setVisibility(View.GONE);
                shimmer_view_container_history.setVisibility(View.GONE);
                constraintLayout_history_onclick_contest_layout.setVisibility(View.GONE);
                tv_present_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_present_contest.setTextColor(getResources().getColor(R.color.black_color));
                tv_history_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_history_contest.setTextColor(getResources().getColor(R.color.history_grey_color));
                mShimmerViewContainer.startShimmerAnimation();
                tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                Get_Live_Details();
                break;
            case R.id.tv_history_contest:
                rv_contest_live_details.setVisibility(View.GONE);
                rv_contest_history_details.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.GONE);
                constraintLayout_history_contest_layout.setVisibility(View.VISIBLE);
                constraintLayout_history_onclick_contest_layout.setVisibility(View.VISIBLE);
                shimmer_view_container_history.setVisibility(View.VISIBLE);
                constraintLayout_present_contest_layout.setVisibility(View.GONE);
                tv_history_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_history_contest.setTextColor(getResources().getColor(R.color.black_color));
                constraintLayout_present_contest_layout.setVisibility(View.GONE);
                tv_present_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_present_contest.setTextColor(getResources().getColor(R.color.history_grey_color));
                shimmer_view_container_history.setVisibility(View.VISIBLE);
                shimmer_view_container_history.startShimmerAnimation();
                tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                Get_History_Details();
                break;
            case R.id.constraintLayout_game_11:
                intent = new Intent(getContext(), Game_Details_Screen_Act_02.class);
                intent.putExtra("onclick_contest_value", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_22:
                intent = new Intent(getContext(), Game_Details_Screen_Act_02.class);
                intent.putExtra("onclick_contest_value", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
        }
    }

    private void Get_History_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_HISTORY_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (Objects.requireNonNull(response.body()).data != null) {
                        if (response.code() == 200) {
                            if (response.isSuccessful()) {
                                if (Objects.requireNonNull(response.body()).data.isEmpty()) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_no_data_available_fr_history_contest.setVisibility(View.VISIBLE);
                                            tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                                            rv_contest_history_details.setVisibility(View.GONE);
                                            shimmer_view_container_history.setVisibility(View.GONE);
                                        }
                                    }, 2500);
                                    shimmer_view_container_history.setVisibility(View.VISIBLE);
                                    shimmer_view_container_history.startShimmerAnimation();
                                } else {
                                    tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                                    tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                                    rv_contest_history_details.setVisibility(View.VISIBLE);
                                    shimmer_view_container_history.stopShimmerAnimation();
                                    shimmer_view_container_history.setVisibility(View.GONE);
//                                Log.e("str_datum_value-->", String.valueOf(response.body().data));

                                    history_contest_details_adapter = new History_Contest_Details_Adapter(getActivity(), response.body().data);
                                    rv_contest_history_details.setAdapter(history_contest_details_adapter);
                                }
                            }
                        } else if (response.code() == 401) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        } else if (response.code() == 500) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        }
                    } else {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Something went wrong try again :)");
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
    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.contest_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        shimmer_view_container_history.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        mShimmerViewContainer.stopShimmerAnimation();
        shimmer_view_container_history.stopShimmerAnimation();
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
        getActivity().registerReceiver(broadcastReceiver, internetFilter);
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
            snackbar = Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.timer_bg_color);
            snackbar.setActionTextColor(Color.BLACK);
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar
                    .make(Objects.requireNonNull(getView()).findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundResource(R.color.red_color_new);
            snackbar.setActionTextColor(Color.WHITE);
        }
        // Changing message text color

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

                rv_contest_history_details.setVisibility(View.GONE);
                rv_contest_live_details.setVisibility(View.GONE);

                tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                tv_no_data_available_fr_history_contest.setVisibility(View.GONE);

                shimmer_view_container_history.setVisibility(View.VISIBLE);
                shimmer_view_container_history.startShimmerAnimation();

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

                shimmer_view_container_history.setVisibility(View.GONE);
                shimmer_view_container_history.stopShimmerAnimation();

                rv_contest_live_details.setVisibility(View.VISIBLE);
                rv_contest_history_details.setVisibility(View.VISIBLE);

                Get_Live_Details();
                Get_History_Details();
            }
        }
    }

}

package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.spot_the_ballgame.Adapter.History_Contest_Details_Adapter;
import com.spot_the_ballgame.Adapter.Live_Contest_Details_Adapter;
import com.spot_the_ballgame.Game_Details_Screen_Act_02;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.My_Contest_History;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Toast_Message;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class My_Contest_Fragment extends Fragment implements View.OnClickListener {
    SQLiteDatabase db;
    String str_db_values, str_balance;
    TextView tv_your_points, tv_your_game_time, tv_your_game_point_values, tv_your_game_title, tv_no_data_available_fr_live_contest, tv_no_data_available_fr_history_contest;
    TextView tv_present_contest, tv_history_contest;
    ConstraintLayout constraintLayout_present_contest_layout, constraintLayout_history_contest_layout, constraintLayout_history_onclick_contest_layout, constraintLayout_game_1, constraintLayout_game_2, constraintLayout_game_3, constraintLayout_game_1_history, constraintLayout_game_2_history, constraintLayout_game_3_history;
    Intent intent;

    RecyclerView rv_contest_live_details, rv_contest_history_details;
    private String str_email;
    String str_auth_token;
    Live_Contest_Details_Adapter live_contest_details_adapter;
    History_Contest_Details_Adapter history_contest_details_adapter;
    private ShimmerFrameLayout mShimmerViewContainer, shimmer_view_container_history;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_contest_fragment, container, false);
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        tv_your_points = view.findViewById(R.id.tv_your_points);
        rv_contest_live_details = view.findViewById(R.id.rv_contest_live_details);
        rv_contest_history_details = view.findViewById(R.id.rv_contest_history_details);

        tv_your_game_time = view.findViewById(R.id.tv_your_game_time);
        tv_your_game_point_values = view.findViewById(R.id.tv_your_game_point_values);
        tv_your_game_title = view.findViewById(R.id.tv_your_game_title);
        tv_no_data_available_fr_live_contest = view.findViewById(R.id.tv_no_data_available_fr_live_contest);
        tv_no_data_available_fr_history_contest = view.findViewById(R.id.tv_no_data_available_fr_history_contest);

        tv_present_contest = view.findViewById(R.id.tv_present_contest);
        tv_history_contest = view.findViewById(R.id.tv_history_contest);

        constraintLayout_present_contest_layout = view.findViewById(R.id.constraintLayout_present_contest_layout);
        constraintLayout_history_contest_layout = view.findViewById(R.id.constraintLayout_history_contest_layout);
        constraintLayout_history_onclick_contest_layout = view.findViewById(R.id.constraintLayout_history_onclick_contest_layout);

        constraintLayout_game_1_history = view.findViewById(R.id.constraintLayout_game_1_history);
        constraintLayout_game_2_history = view.findViewById(R.id.constraintLayout_game_2_history);
        constraintLayout_game_3_history = view.findViewById(R.id.constraintLayout_game_3_history);

        constraintLayout_game_1 = view.findViewById(R.id.constraintLayout_game_11);
        constraintLayout_game_2 = view.findViewById(R.id.constraintLayout_game_22);
        constraintLayout_game_3 = view.findViewById(R.id.constraintLayout_game_33);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        shimmer_view_container_history = view.findViewById(R.id.shimmer_view_container_history);

        Navigation_Drawer_Act.tv_title_txt.setText(R.string.contest_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        tv_present_contest.setOnClickListener(this);
        tv_history_contest.setOnClickListener(this);
        constraintLayout_game_1_history.setOnClickListener(this);
        constraintLayout_game_2_history.setOnClickListener(this);
        constraintLayout_game_3_history.setOnClickListener(this);

        constraintLayout_game_1.setOnClickListener(this);
        constraintLayout_game_2.setOnClickListener(this);
        constraintLayout_game_3.setOnClickListener(this);

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
        Log.e("str_auth_token_contest", str_auth_token);
        mShimmerViewContainer.startShimmerAnimation();
        Get_Live_Details();

       /* db = Objects.requireNonNull(Objects.requireNonNull(getActivity()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null));
        String select = "select USER_SELECTION_VALUE  from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_db_values = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("str_source_details_contest", str_db_values);
        tv_your_game_point_values.setText(str_db_values);*/
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
                            if (response.body().data.isEmpty()) {
//                            tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                                rv_contest_live_details.setVisibility(View.GONE);
                                mShimmerViewContainer.startShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.VISIBLE);

                            } else {
                                //tv_no_data_available_fr_live_contest.setVisibility(View.VISIBLE);
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
        Log.e("backStackCnt_contest", "" + backStackEntryCount);
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
                tv_present_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_present_contest.setTextColor(getResources().getColor(R.color.black_color));
                constraintLayout_present_contest_layout.setVisibility(View.VISIBLE);
                constraintLayout_history_contest_layout.setVisibility(View.GONE);
                constraintLayout_history_onclick_contest_layout.setVisibility(View.GONE);
                tv_history_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_history_contest.setTextColor(getResources().getColor(R.color.history_grey_color));
                mShimmerViewContainer.startShimmerAnimation();
                Get_Live_Details();
                break;
            case R.id.tv_history_contest:
                constraintLayout_history_contest_layout.setVisibility(View.VISIBLE);
                constraintLayout_history_onclick_contest_layout.setVisibility(View.VISIBLE);
                tv_history_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_history_contest.setTextColor(getResources().getColor(R.color.black_color));
                constraintLayout_present_contest_layout.setVisibility(View.GONE);
                tv_present_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_present_contest.setTextColor(getResources().getColor(R.color.history_grey_color));
                shimmer_view_container_history.startShimmerAnimation();
                Get_History_Details();
                break;
            case R.id.constraintLayout_game_1_history:
                intent = new Intent(getContext(), My_Contest_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_2_history:
                intent = new Intent(getContext(), My_Contest_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_3_history:
                intent = new Intent(getContext(), My_Contest_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
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
            case R.id.constraintLayout_game_33:
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
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            if (Objects.requireNonNull(response.body()).data.size() == 0) {
//                            tv_no_data_available_fr_history_contest.setVisibility(View.VISIBLE);
//                            tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
                                rv_contest_history_details.setVisibility(View.GONE);
                                shimmer_view_container_history.setVisibility(View.VISIBLE);
                                shimmer_view_container_history.startShimmerAnimation();
                            } else {
//                            tv_no_data_available_fr_live_contest.setVisibility(View.GONE);
//                            tv_no_data_available_fr_history_contest.setVisibility(View.GONE);
                                rv_contest_history_details.setVisibility(View.VISIBLE);
                                Log.e("str_datum_value-->", String.valueOf(response.body().data));
                                rv_contest_history_details.setVisibility(View.VISIBLE);
                                shimmer_view_container_history.stopShimmerAnimation();
                                shimmer_view_container_history.setVisibility(View.GONE);

                                history_contest_details_adapter = new History_Contest_Details_Adapter(getActivity(), response.body().data);
                                rv_contest_history_details.setAdapter(history_contest_details_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
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
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.contest_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        shimmer_view_container_history.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
        shimmer_view_container_history.stopShimmerAnimation();
    }
}

package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.spot_the_ballgame.Adapter.Answers_List_Adapter;
import com.spot_the_ballgame.Adapter.Price_List_Adapter_My_History;
import com.spot_the_ballgame.Adapter.Ranking_List_Adapter_My_History;
import com.spot_the_ballgame.Adapter.Rules_List_Name_Adapter_My_History;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Model.Winnings_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Contest_History extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    TextView tv_price_btn, tv_rules_btn, tv_answers_btn, tv_score_board, tv_current_user_rank, tv_no_data_available_fr_ranking_my_history, tv_no_data_available_fr_played_records_my_history;
    ConstraintLayout
            constraintLayout_game_details_screen_in_game_two,
            constraintLayout_score_board_top_layout;
    private String
            str_auth_token,
            str_contest_id_histroy,
            str_rule_id,
            str_email;
    Bundle bundle;


    RecyclerView rv_answer_list,
            rv_price_list_fr_my_history,
            rv_rules_list_fr_my_history,
            rv_score_board_list_fr_my_history;


    Answers_List_Adapter answers_list_adapter;
    Price_List_Adapter_My_History price_list_adapter_game_two;
    Ranking_List_Adapter_My_History ranking_list_adapter_game_two;
    Rules_List_Name_Adapter_My_History rules_list_name_adapter;

    private ShimmerLayout shimmer_view_container_for_rules, shimmer_view_container_for_scoreboard, shimmer_view_container_for_winnings, shimmer_view_container_for_answer_list;
    String str_rank, str_points, str_winnings, str_date, str_contest_name, str_categories_image, str_local_host, str_connect_host_image, str_categories,
            str_connect_host_image_01, str_connect_host_image_02, str_team_a_path, str_team_b_path;
    TextView tv_rank_history, tv_point_history, tv_entry_fee_history, tv_date_history, tv_contest_name_history;
    ArrayList<Winnings_Model> winning_arrayList;
    ConstraintLayout constraintLayout_country_flag_layout;
    ImageView iv_first_country_flag, iv_second_country_flag, iv_categoires_image;
    TextView tv_first_country, tv_second_country;
    String str_team_a_path_name_txt,
            str_team_b_path_name_txt;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game__details_screen_03);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        tv_price_btn = findViewById(R.id.tv_price_btn);
        tv_rules_btn = findViewById(R.id.tv_rules_btn);
        tv_answers_btn = findViewById(R.id.tv_answers_btn);
        tv_score_board = findViewById(R.id.tv_score_board);
        tv_current_user_rank = findViewById(R.id.tv_current_user_rank);
        tv_no_data_available_fr_ranking_my_history = findViewById(R.id.tv_no_data_available_fr_ranking_my_history);
        tv_no_data_available_fr_played_records_my_history = findViewById(R.id.tv_no_data_available_fr_played_records_my_history);

        constraintLayout_country_flag_layout = findViewById(R.id.constraintLayout_country_flag_layout);
        iv_first_country_flag = findViewById(R.id.iv_first_country_flag);
        iv_second_country_flag = findViewById(R.id.iv_second_country_flag);
        iv_categoires_image = findViewById(R.id.iv_categoires_image);
        tv_first_country = findViewById(R.id.tv_first_country);
        tv_second_country = findViewById(R.id.tv_second_country);

        tv_rank_history = findViewById(R.id.tv_rank_history);
        tv_point_history = findViewById(R.id.tv_point_history);
        tv_entry_fee_history = findViewById(R.id.tv_entry_fee_history);
        tv_date_history = findViewById(R.id.tv_date_history);
        tv_contest_name_history = findViewById(R.id.tv_contest_name_history);


        rv_price_list_fr_my_history = findViewById(R.id.rv_price_list_fr_my_history);
        rv_price_list_fr_my_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_price_list_fr_my_history.setVisibility(View.VISIBLE);


        rv_rules_list_fr_my_history = findViewById(R.id.rv_rules_list_fr_my_history);
        rv_rules_list_fr_my_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_rules_list_fr_my_history.setVisibility(View.VISIBLE);


        rv_score_board_list_fr_my_history = findViewById(R.id.rv_score_board_list_fr_my_history);
        rv_score_board_list_fr_my_history.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_score_board_list_fr_my_history.setVisibility(View.VISIBLE);


        rv_answer_list = findViewById(R.id.rv_answer_list);
        rv_answer_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_answer_list.setVisibility(View.VISIBLE);

        shimmer_view_container_for_winnings = findViewById(R.id.shimmer_view_container_for_winnings);
        shimmer_view_container_for_answer_list = findViewById(R.id.shimmer_view_container_for_answer_list);
        shimmer_view_container_for_rules = findViewById(R.id.shimmer_view_container_for_rules);
        shimmer_view_container_for_scoreboard = findViewById(R.id.shimmer_view_container_for_scoreboard);
        constraintLayout_game_details_screen_in_game_two = findViewById(R.id.constraintLayout_game_details_screen_in_game_two);
        constraintLayout_score_board_top_layout = findViewById(R.id.constraintLayout_score_board_top_layout);

        rv_answer_list.setVisibility(View.GONE);
        tv_price_btn.setOnClickListener(this);
        tv_rules_btn.setOnClickListener(this);
        tv_score_board.setOnClickListener(this);
        tv_answers_btn.setOnClickListener(this);
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        bundle = getIntent().getExtras();
        if (bundle == null) {
            str_contest_id_histroy = null;
        } else {
            str_contest_id_histroy = bundle.getString("contest_id_history");
            str_rank = bundle.getString("rank_history");
            str_winnings = bundle.getString("winnings_history");
            str_rule_id = bundle.getString("rule_id_history");
            str_date = bundle.getString("date_history");
            str_contest_name = bundle.getString("contest_name_history");
            str_points = bundle.getString("points_history");

            str_categories = bundle.getString("categories_history");
            str_categories_image = bundle.getString("categories_image_history");

            str_team_a_path = bundle.getString("str_team_a_path_history");
            str_team_b_path = bundle.getString("str_team_b_path_history");

            str_team_a_path_name_txt = bundle.getString("str_team_a_path_name_txt_history");
            str_team_b_path_name_txt = bundle.getString("str_team_b_path_name_txt_history");


//            Log.e("str_contest_id_contest", str_contest_id_histroy);
//            Log.e("str_rank", str_rank);
//            Log.e("str_winnings", str_winnings);
//            Log.e("str_date", str_date);
//            Log.e("str_contest_name", str_contest_name);
//            Log.e("str_points", str_points);
        }

        if (str_categories.equalsIgnoreCase("Prediction")) {
            constraintLayout_country_flag_layout.setVisibility(View.VISIBLE);
            tv_contest_name_history.setVisibility(View.GONE);
        } else {
            tv_contest_name_history.setVisibility(View.VISIBLE);
        }

        if (str_categories.equalsIgnoreCase("Prediction")) {
            constraintLayout_country_flag_layout.setVisibility(View.VISIBLE);
            tv_contest_name_history.setVisibility(View.GONE);


            str_connect_host_image_01 = str_local_host + "" + str_team_a_path;
//            Log.e("conn_host_image_01_live", str_connect_host_image_01);
            Glide.with(My_Contest_History.this).load(str_connect_host_image_01)
                    .thumbnail(Glide.with(My_Contest_History.this).load(str_connect_host_image_01))
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_first_country_flag);

            str_connect_host_image_02 = str_local_host + "" + str_team_b_path;
//            Log.e("conn_host_image_02_live", str_connect_host_image_02);
            Glide.with(My_Contest_History.this).load(str_connect_host_image_02)
                    .thumbnail(Glide.with(My_Contest_History.this).load(str_connect_host_image_02))
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_second_country_flag);

            tv_first_country.setText(str_team_a_path_name_txt);
            tv_second_country.setText(str_team_b_path_name_txt);
        } else {
            tv_contest_name_history.setVisibility(View.VISIBLE);
        }


//        Log.e("str_imagepath_intent", str_categories_image);
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        str_connect_host_image = str_local_host + "" + str_categories_image;
        Glide.with(My_Contest_History.this).load(str_connect_host_image)
                .thumbnail(Glide.with(My_Contest_History.this).load(str_connect_host_image))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_categoires_image);

        str_auth_token = SessionSave.getSession("Token_value", My_Contest_History.this);
//        Log.e("str_auth_token_contest", str_auth_token);

        String select = "select EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        shimmer_view_container_for_winnings.startShimmerAnimation();
        Get_Winning_List_Details();
        tv_rank_history.setText(str_rank);
        tv_point_history.setText(str_points);
        tv_entry_fee_history.setText(str_winnings);
        tv_date_history.setText(str_date);
        tv_contest_name_history.setText(str_contest_name);
    }

    @Override
    public void onBackPressed() {
        My_Contest_History.this.getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_btn:

                tv_no_data_available_fr_played_records_my_history.setVisibility(View.GONE);
                tv_no_data_available_fr_ranking_my_history.setVisibility(View.GONE);

                shimmer_view_container_for_winnings.setVisibility(View.VISIBLE);
                shimmer_view_container_for_winnings.startShimmerAnimation();

                shimmer_view_container_for_rules.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                shimmer_view_container_for_answer_list.setVisibility(View.GONE);

                rv_price_list_fr_my_history.setVisibility(View.GONE);
                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                rv_answer_list.setVisibility(View.GONE);

                Get_Winning_List_Details();

                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.white_color));


                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_rules_btn:

                tv_no_data_available_fr_played_records_my_history.setVisibility(View.GONE);
                tv_no_data_available_fr_ranking_my_history.setVisibility(View.GONE);

                shimmer_view_container_for_rules.setVisibility(View.VISIBLE);
                shimmer_view_container_for_rules.startShimmerAnimation();

                shimmer_view_container_for_winnings.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                shimmer_view_container_for_answer_list.setVisibility(View.GONE);

                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                rv_price_list_fr_my_history.setVisibility(View.GONE);
                rv_answer_list.setVisibility(View.GONE);
                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                Get_Rules_Name_Details();

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.white_color));

                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_score_board:

                tv_no_data_available_fr_played_records_my_history.setVisibility(View.GONE);
                tv_no_data_available_fr_ranking_my_history.setVisibility(View.GONE);

                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                shimmer_view_container_for_scoreboard.startShimmerAnimation();

                shimmer_view_container_for_winnings.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.GONE);
                shimmer_view_container_for_answer_list.setVisibility(View.GONE);

                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                rv_price_list_fr_my_history.setVisibility(View.GONE);
                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                rv_answer_list.setVisibility(View.GONE);

                Get_Ranking_List_Details();

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.tv_answers_btn:

                tv_no_data_available_fr_played_records_my_history.setVisibility(View.GONE);
                tv_no_data_available_fr_ranking_my_history.setVisibility(View.GONE);

                shimmer_view_container_for_answer_list.setVisibility(View.VISIBLE);
                shimmer_view_container_for_answer_list.startShimmerAnimation();

                shimmer_view_container_for_winnings.setVisibility(View.GONE);
                shimmer_view_container_for_rules.setVisibility(View.GONE);
                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);

                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                rv_price_list_fr_my_history.setVisibility(View.GONE);
                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                rv_answer_list.setVisibility(View.GONE);

                Get_Played_Record_Details();

                tv_answers_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_new_normal));
                tv_answers_btn.setTextColor(getResources().getColor(R.color.white_color));

                tv_rules_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_rules_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_price_btn.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_price_btn.setTextColor(getResources().getColor(R.color.black_color));

                tv_score_board.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_score_board.setTextColor(getResources().getColor(R.color.black_color));
                break;
        }
    }

    private void Get_Ranking_List_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id_histroy);
            jsonObject.put("email", str_email);

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_RANKING_LIST_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @SuppressLint("SetTextI18n")
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
                                        tv_no_data_available_fr_ranking_my_history.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_ranking_my_history.setText(str_message);
                                        rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                                        shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                shimmer_view_container_for_scoreboard.startShimmerAnimation();
                                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                            } else if (Objects.requireNonNull(response.body()).data.isEmpty()) {
                                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                                shimmer_view_container_for_scoreboard.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_scoreboard.startShimmerAnimation();

                                rv_price_list_fr_my_history.setVisibility(View.GONE);
                                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                                rv_answer_list.setVisibility(View.GONE);
                            } else {
                                rv_price_list_fr_my_history.setVisibility(View.GONE);
                                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                                rv_answer_list.setVisibility(View.GONE);

                                rv_score_board_list_fr_my_history.setVisibility(View.VISIBLE);
                                constraintLayout_score_board_top_layout.setVisibility(View.VISIBLE);

                                String str_crnt_user_rank = response.body().your_rank;
                                tv_current_user_rank.setText("Your Rank : " + str_crnt_user_rank);

                                shimmer_view_container_for_scoreboard.stopShimmerAnimation();
                                shimmer_view_container_for_scoreboard.setVisibility(View.GONE);
                                assert response.body() != null;
                                ranking_list_adapter_game_two = new Ranking_List_Adapter_My_History(My_Contest_History.this, Objects.requireNonNull(response.body()).data);
                                rv_score_board_list_fr_my_history.setAdapter(ranking_list_adapter_game_two);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(My_Contest_History.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(My_Contest_History.this, response.message());
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

    private void Get_Rules_Name_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", str_rule_id);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("json_rules", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_RULES_NAME_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().data.isEmpty()) {
                            rv_rules_list_fr_my_history.setVisibility(View.GONE);
                            shimmer_view_container_for_rules.startShimmerAnimation();
                            shimmer_view_container_for_rules.setVisibility(View.VISIBLE);

                            rv_price_list_fr_my_history.setVisibility(View.GONE);
                            rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                            constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                            rv_answer_list.setVisibility(View.GONE);
                        } else {
                            rv_price_list_fr_my_history.setVisibility(View.GONE);
                            rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                            constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                            rv_answer_list.setVisibility(View.GONE);

                            rv_rules_list_fr_my_history.setVisibility(View.VISIBLE);
                            shimmer_view_container_for_rules.stopShimmerAnimation();
                            shimmer_view_container_for_rules.setVisibility(View.GONE);
                            rules_list_name_adapter = new Rules_List_Name_Adapter_My_History(getApplicationContext(), response.body().data);
                            rv_rules_list_fr_my_history.setAdapter(rules_list_name_adapter);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(My_Contest_History.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(My_Contest_History.this, response.message());
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

    private void Get_Winning_List_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id_histroy);
//            Log.e("volley_json", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(My_Contest_History.this);
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
                                    Winnings_Model winnings_model = new Winnings_Model();
                                    JSONObject json_object = (JSONObject) jsonArray.get(i);
                                /*JSONObject json_object = (JSONObject) jsonArray.get(i);
                                String prize_amount = json_object.getString("prize_amount");
                                String rank = json_object.getString("rank");
                                String no_prize_amount = json_object.getString("no_prize_amount");
                                String total_rank = json_object.getString("total_rank");
                                String total_amount = json_object.getString("total_amount");
                                Log.e("volley_prize_amount", prize_amount);
                                Log.e("volley_rank", rank);
                                Log.e("volley_no_prize", no_prize_amount);
                                Log.e("volley_total_rank", total_rank);
                                Log.e("volley_total_amount", total_amount);*/

                                    rv_rules_list_fr_my_history.setVisibility(View.GONE);
                                    rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                                    constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                                    rv_answer_list.setVisibility(View.GONE);

                                    rv_price_list_fr_my_history.setVisibility(View.VISIBLE);
//                                    winnings_model.setRank(json_object.getString("rank"));
                                    winnings_model.setRank_short(json_object.getString("rank_short"));
                                    winnings_model.setPrize_amount(json_object.getString("prize_amount"));
                                    winning_arrayList.add(winnings_model);

//                                    Log.e("winning_arrayList", winning_arrayList.toString());

                                    try {
                                        price_list_adapter_game_two = new Price_List_Adapter_My_History(My_Contest_History.this, winning_arrayList);
                                        rv_price_list_fr_my_history.setAdapter(price_list_adapter_game_two);
                                        shimmer_view_container_for_winnings.stopShimmerAnimation();
                                        shimmer_view_container_for_winnings.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                shimmer_view_container_for_winnings.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_winnings.startShimmerAnimation();
                                rv_price_list_fr_my_history.setVisibility(View.GONE);
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

    private void Get_Played_Record_Details() {
        try {
//            Log.e("auth_tokennn", str_auth_token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            jsonObject.put("contest_id", str_contest_id_histroy);
            Log.e("mycontest_json", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_PLAYED_RECORD_DETAILS("application/json", jsonObject.toString(), str_auth_token);
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
                                        tv_no_data_available_fr_played_records_my_history.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_played_records_my_history.setText(str_message);
                                        rv_answer_list.setVisibility(View.GONE);
                                        shimmer_view_container_for_answer_list.setVisibility(View.GONE);
                                    }
                                }, 2500);
                                shimmer_view_container_for_answer_list.startShimmerAnimation();
                                shimmer_view_container_for_answer_list.setVisibility(View.VISIBLE);
                            } else if (Objects.requireNonNull(response.body()).data.isEmpty()) {
                                shimmer_view_container_for_answer_list.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_answer_list.startShimmerAnimation();
                                rv_answer_list.setVisibility(View.GONE);

                                rv_price_list_fr_my_history.setVisibility(View.GONE);
                                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                                constraintLayout_score_board_top_layout.setVisibility(View.GONE);
                            } else {
                                rv_price_list_fr_my_history.setVisibility(View.GONE);
                                rv_rules_list_fr_my_history.setVisibility(View.GONE);
                                rv_score_board_list_fr_my_history.setVisibility(View.GONE);
                                constraintLayout_score_board_top_layout.setVisibility(View.GONE);

                                shimmer_view_container_for_answer_list.stopShimmerAnimation();
                                shimmer_view_container_for_answer_list.setVisibility(View.GONE);
                                rv_answer_list.setVisibility(View.VISIBLE);
                                answers_list_adapter = new Answers_List_Adapter(My_Contest_History.this, Objects.requireNonNull(response.body()).data);
                                rv_answer_list.setAdapter(answers_list_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(My_Contest_History.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(My_Contest_History.this, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}

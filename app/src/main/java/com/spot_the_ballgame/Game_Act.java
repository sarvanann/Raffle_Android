package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.spot_the_ballgame.Adapter.ContestAdapter_For_End_Game;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.format;
import static java.lang.String.valueOf;


public class Game_Act extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout constraintLayout_resource_downloading_please_wait;
    String str_url = "", str_status = "";
    String str_session_images = "";
    Set<String> linkedHashSet;

    String StorezipFileLocation = Environment.getExternalStorageDirectory() + "/DownloadedZip";
    String DirectoryName = Environment.getExternalStorageDirectory() + "/RaffleSTB/files/";

    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler_progress_bar = new Handler();

    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;

    Dialog dialog, dialog_fr_timer;

    SQLiteDatabase db;
    String str_count_down_seconds_for_res_downloading = "";
    String str_email,
            str_phone_no,
            question_count,
            str_auth_token,
            str_user_selection_value,
            str_db_values,
            str_db_concat,
            str_count_down_seconds,
            str_2x_powerup,
            str_entry_fees,
            str_correct_ans,
            str_wrong_ans,
            str_skip,
            str_code,
            str_message,
            str_onclick_answer_selection,
            str_onclick_string_answer_selection,
            str_contest_id,
            str_local_host,
            str_onclick_time,
            str_difference_time,
            str_final_answer,
            str_onclick_2x_powerup,
            str_int_play_status,
            str_playby,
            str_mCoinCountText;

    String str_alphabet_input_value = "";
    String str_remaining_count_value = "";

    int int_count_value = 0;
    int int_reaming_page_count_value = 1;
    int int_2_x_count = 0;
    int int_2x_onclick_count = 0;
    int int_onclcik_2x_power_up = 0;

    int int_number_of_questions,
            int_entry_fee,
            int_correct_ans,
            int_correct_ans_notations,
            int_wrong_ans,
            int_skip,
            n1_old_value,
            n2_new_value,
            int_previous_or_courrent_question_number,
            int_str_seconds,
            int_str_onclick_time,
            difference_time,
            n1_1,
            int_skip_else,
            int_duplicate_total_value,
            int_duplicate_corect_answer_value;


    public long seconds,
            milliseconds;

    TextView tv_A_alphabet,
            tv_B_alphabet,
            tv_C_alphabet,
            tv_D_alphabet,
            tv_2x_txt,
            tv_2x_power_up,
            tv_remaining_count_value,
            tv_number_of_questions_in_api,
            tv_timer_seconds_count_game_one,
            tv_timer_seconds_txt,
            tv_just_a_moment_contest_one,
            tv_close_end_game,
            tv_time_limit_sec_txt,
            tv_skip_points_values,
            tv_wrong_ans_values,
            tv_correct_ans_values;

    CountDownTimer countDownTimer;


    ConstraintLayout constraintLayout_tv_2x_power_up,
            constraintLayout_just_a_moment,
            constraintLayout_game,
            constraintLayout_end_game,
            constraintLayout_count_down_timer_game_one,
            constraintLayout_tv_just_a_moment_contest_one_txt;
    public static ImageView iv_changing_image;
    ImageView iv_ready_steady_go_state,
            iv_end_game_sucess_message_gif;

    private AdView mAdView;
    Vibrator vibrator;
    Bundle bundle;


    ContestAdapter_For_End_Game contestAdapter_for_end_game;
    RecyclerView rv_single_card_end_game_one;

    private AdView mAdView_end_game;
    AdRequest adRequest;


    ArrayList<Integer> total_onclick_correct_answer_values_ArrayList = new ArrayList<>();
    ArrayList<Integer> total_onclick_wrong_answer_values_ArrayList = new ArrayList<>();
    ArrayList<String> imagequestionsIntegerArrayList = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_01 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_02 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_03 = new ArrayList<>();
    ArrayList<String> answersIntegerArrayList_04 = new ArrayList<>();
    ArrayList<String> Final_Answer_ArrayList = new ArrayList<>();
    ArrayList<String> questionnumber_ArrayList = new ArrayList<>();
    ArrayList<String> answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> timer_ArrayList = new ArrayList<>();
    ArrayList<String> _2x_power_up_ArrayList = new ArrayList<>();
    ArrayList<String> total_all_onclick_point_values_ArrayList = new ArrayList<>();
    ArrayList<String> total_onclick_time_ArrayList = new ArrayList<>();
    ArrayList<Integer> total_skip_values_ArrayList = new ArrayList<>();
    ArrayList<String> answers_string_velue_selection_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_onclick_time_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_onclick_point_ArrayList = new ArrayList<>();
    ArrayList<String> duplicate_correct_answerselection_ArrayList = new ArrayList<>();
    ArrayList<String> _2x_power_up_continous_update_to_api_ArrayList = new ArrayList<>();
    FirebaseAnalytics mFirebaseAnalytics;
    Handler handler;

    int int_2x_onclick_or_not = 0;
    boolean is_2x_btn_clicked = false;

    @SuppressLint({"WrongConstant", "SetTextI18n", "LongLogTag"})
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_03);
        db = getApplicationContext().openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar," +
                "EMAIL varchar,PHONENO int,APPID varchar,WALET double,TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar" +
                ",USERNAME varchar,ACTIVE int,VERIFIED int,SIGNUPSTATUS int,BALANCE int,USER_SELECTION_VALUE varchar)");
        getSupportActionBar().hide();

        String select = "select EMAIL,PHONENO from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
                str_phone_no = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();


        tv_A_alphabet = findViewById(R.id.tv_A_alphabet);
        tv_B_alphabet = findViewById(R.id.tv_B_alphabet);
        tv_C_alphabet = findViewById(R.id.tv_C_alphabet);
        tv_D_alphabet = findViewById(R.id.tv_D_alphabet);
        tv_2x_txt = findViewById(R.id.tv_2x_txt);
        iv_ready_steady_go_state = findViewById(R.id.iv_ready_steady_go_state);
        Glide.with(Game_Act.this).asGif().load(R.drawable.ready_steady_go).into(iv_ready_steady_go_state);
        tv_2x_power_up = findViewById(R.id.tv_2x_power_up);
        tv_remaining_count_value = findViewById(R.id.tv_remaining_count_value);
        tv_number_of_questions_in_api = findViewById(R.id.tv_number_of_questions_in_api);
        tv_timer_seconds_count_game_one = findViewById(R.id.tv_timer_seconds_count_game_one);
        tv_timer_seconds_txt = findViewById(R.id.tv_timer_seconds_txt_game_one);
        constraintLayout_just_a_moment = findViewById(R.id.constraintLayout_just_a_moment_contest_one);
        constraintLayout_tv_2x_power_up = findViewById(R.id.constraintLayout_tv_2x_power_up);
        constraintLayout_tv_just_a_moment_contest_one_txt = findViewById(R.id.constraintLayout_tv_just_a_moment_contest_one_txt);
        tv_just_a_moment_contest_one = findViewById(R.id.tv_just_a_moment_contest_one);
        tv_time_limit_sec_txt = findViewById(R.id.tv_time_limit_sec_txt);

        tv_correct_ans_values = findViewById(R.id.tv_correct_ans_values);
        tv_wrong_ans_values = findViewById(R.id.tv_wrong_ans_values);
        tv_skip_points_values = findViewById(R.id.tv_skip_points_values);
        handler = new Handler();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(Game_Act.this);
        tv_close_end_game = findViewById(R.id.tv_close_end_game);
        tv_close_end_game.setText(R.string.go_back_txt);

        constraintLayout_end_game = findViewById(R.id.constraintLayout_end_game);
        constraintLayout_game = findViewById(R.id.constraintLayout_game_screen);
        iv_changing_image = findViewById(R.id.iv_changing_image);
        constraintLayout_count_down_timer_game_one = findViewById(R.id.constraintLayout_count_down_timer_game_one);
        iv_end_game_sucess_message_gif = findViewById(R.id.iv_end_game_sucess_message_gif);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dialog_fr_timer = new Dialog(Game_Act.this);
        dialog_fr_timer.setContentView(R.layout.please_wait_lay);
        Objects.requireNonNull(dialog_fr_timer.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAdView = findViewById(R.id.adView);
        mAdView_end_game = findViewById(R.id.adView_end_game);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView_end_game.loadAd(adRequest);

        rv_single_card_end_game_one = findViewById(R.id.rv_single_card_end_game);

        str_auth_token = SessionSave.getSession("Token_value", Game_Act.this);

        rv_single_card_end_game_one.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_single_card_end_game_one.setHasFixedSize(true);
        rv_single_card_end_game_one.setVisibility(View.VISIBLE);

        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv_A_alphabet.setOnClickListener(this);
        tv_B_alphabet.setOnClickListener(this);
        tv_C_alphabet.setOnClickListener(this);
        tv_D_alphabet.setOnClickListener(this);
        tv_2x_txt.setOnClickListener(this);
        tv_close_end_game.setOnClickListener(this);
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;

        bundle = getIntent().getExtras();
        if (bundle == null) {
            str_2x_powerup = null;
        } else {
            str_2x_powerup = bundle.getString("str_2x_powerup");
            str_count_down_seconds = bundle.getString("str_count_down_seconds");
            str_correct_ans = bundle.getString("str_correct_ans");
            str_wrong_ans = bundle.getString("str_wrong_ans");
            str_skip = bundle.getString("str_skip");
            str_entry_fees = bundle.getString("str_entry_fees");
            str_contest_id = bundle.getString("str_contest_id");


            str_int_play_status = bundle.getString("int_play_status");
            str_playby = bundle.getString("str_playby");
            str_mCoinCountText = bundle.getString("str_mCoinCountText");

//            Log.e("str_int_play_status_log_Game_Act", str_int_play_status);
//            Log.e("str_playby_log_Game_Act", str_playby);
//            Log.e("str_mCoinCountText_log_Game_Act", str_entry_fees);
            Getting_Update_Status_Details_Initial(str_int_play_status);
        }
        tv_correct_ans_values.setText("+ " + str_correct_ans + " Points");
//        tv_wrong_ans_values.setText("- " + str_wrong_ans + " Points");
        tv_wrong_ans_values.setText(str_wrong_ans + " Points");
        tv_skip_points_values.setText(str_skip + " Points");
        try {
            int_entry_fee = Integer.parseInt(str_entry_fees);
            int_correct_ans = Integer.parseInt(str_correct_ans);
            int_wrong_ans = Integer.parseInt(str_wrong_ans);
            int_skip = Integer.parseInt(str_skip);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        //This is used for Full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FullScreenMethod();

        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        tv_just_a_moment_contest_one.startAnimation(zoomOutAnimation);


        int_2x_onclick_count = Integer.parseInt(str_2x_powerup);
//        Log.e("str_2x_powerup", "" + int_2x_onclick_count);
        tv_2x_power_up.setText(valueOf(int_2x_onclick_count));
//        Log.e("str_count_down_seconds", str_count_down_seconds);
        tv_time_limit_sec_txt.setText(str_count_down_seconds + "s");


        constraintLayout_resource_downloading_please_wait = findViewById(R.id.constraintLayout_resource_downloading_please_wait);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);


        /*if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            assert str_count_down_seconds_for_res_downloading != null;
            str_count_down_seconds_for_res_downloading = "1";
            milliseconds = Long.parseLong(str_count_down_seconds_for_res_downloading) * 1000 + 1;
            startTimer_For_Resource_Downloading(milliseconds);
        }*/
        Before_Download_Method();
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus + "/" + progressBar.getMax());
                            if (progressStatus == 100) {
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        constraintLayout_resource_downloading_please_wait.setVisibility(View.GONE);
                                        constraintLayout_just_a_moment.setVisibility(View.VISIBLE);
                                        After_Download_Method();
                                    }
                                }, 400);
                            }
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


//        Get_Image_Assets_Details();
    }

    private void Before_Download_Method() {
        str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
        Log.e("str_session_images_log_Before", str_session_images);
        if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
            String[] str_array = str_session_images.replace("[", "").replace("]", "").split(",");
            List<String> fixedLenghtList = Arrays.asList(str_array);
            ArrayList<String> stringArrayList = new ArrayList<String>(fixedLenghtList);
            linkedHashSet = new LinkedHashSet<>(stringArrayList);
            Log.e("linkedHashSet_sizeee", String.valueOf(linkedHashSet.size()));
            for (int j = 0; j < linkedHashSet.size(); j++) {
                imagequestionsIntegerArrayList.add(stringArrayList.get(j));
                Log.e("imagequestionsIntegerArrayList_get_00_Before", imagequestionsIntegerArrayList.get(j));

                Glide.with(Game_Act.this)
                        .load(imagequestionsIntegerArrayList.get(j))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                        .into(iv_changing_image);
            }

            /*String str_image_path = imagequestionsIntegerArrayList.get(0);
            Log.e("str_image_path_get_00_Before", str_image_path);
            str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
            if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
//                File imgFile_01 = new File(imagequestionsIntegerArrayList.get(j).replace("/ ", ""));
                Log.e("imgFile_01_get_question_Before", String.valueOf(str_image_path));
                Glide.with(Game_Act.this)
                        .load(str_image_path)
                        .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                        .into(iv_changing_image);
            }*/
        }
    }

    private void Get_Image_Assets_Details() {
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("contest_id", "contesty3wIYK");
            Log.e("_JSON_VALUE", jsonObject1.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_ASSETS_CALL("application/json", jsonObject1.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        str_url = response.body() != null ? response.body().url : null;
                        str_status = response.body() != null ? response.body().status : null;
                        assert str_url != null;
                        assert str_status != null;
//                        DownloadZipfile mew = new DownloadZipfile();
//                        mew.execute(str_url);
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

    private void After_Download_Method() {
        iv_changing_image.setVisibility(View.VISIBLE);
//        Toast.makeText(Game_Act.this, "After_Download_Method_Toast", Toast.LENGTH_SHORT).show();
        String str_image_path = imagequestionsIntegerArrayList.get(0);
//        Log.e("str_image_path_get_00", str_image_path);
        str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
        if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
//            Log.e("imgFile_01_get_question", String.valueOf(str_image_path));
            Glide.with(Game_Act.this)
                    .load(str_image_path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                    .into(iv_changing_image);
        }
        /*str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
        Log.e("str_session_images_log", str_session_images);
        if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
            String[] str_array = str_session_images.replace("[", "").replace("]", "").split(",");
            List<String> fixedLenghtList = Arrays.asList(str_array);
            ArrayList<String> stringArrayList = new ArrayList<String>(fixedLenghtList);
            linkedHashSet = new LinkedHashSet<>(stringArrayList);

            for (int j = 0; j < linkedHashSet.size(); j++) {
                imagequestionsIntegerArrayList.add(stringArrayList.get(j));
//                        Log.e("resultIAV_get_of_i_next", stringArrayList.get(j));
//                Log.e("resultIAV_get_of_i_tostring", imagequestionsIntegerArrayList.toString().replace("/ ", "").replace("[", "").replace("]", ""));
            }
            str_remaining_count_value = tv_remaining_count_value.getText().toString();
            int n1_1 = Integer.parseInt(str_remaining_count_value);
            int nn = n1_1 - 1;
            Log.e("n111_1_after_download", String.valueOf(n1_1));
            Log.e("imagequestionsIntegerArrayList_get_00", imagequestionsIntegerArrayList.get(0));

            String str_image_path = imagequestionsIntegerArrayList.get(0);
            Log.e("str_image_path_get_00", str_image_path);
            str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
            if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
//                File imgFile_01 = new File(String.valueOf(imagequestionsIntegerArrayList.indexOf(n1_1)));
//                File imgFile_01 = new File(imagequestionsIntegerArrayList.get(n1_1).replace("/ ", ""));
                Log.e("imgFile_01_get_question", String.valueOf(str_image_path));
                Glide.with(Game_Act.this)
//                        .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                        .load(str_image_path)
                        .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                        .into(iv_changing_image);
            }
        }*/

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
//                Toast.makeText(Game_Act.this, "Handler_Toast", Toast.LENGTH_SHORT).show();
                Get_Questions_Details();
                constraintLayout_just_a_moment.setVisibility(View.GONE);
                constraintLayout_game.setVisibility(View.VISIBLE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    assert str_count_down_seconds != null;
                    milliseconds = Long.parseLong(str_count_down_seconds) * 1000 + 1;
                    startTimer(milliseconds);
                }
            }
//        }, 2900);
        }, 2800);
        constraintLayout_end_game.setVisibility(View.GONE);
    }


    private void Get_Questions_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
//            Log.e("Json_value_question", jsonObject.toString());
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Game_Act.this);
                //This is for SK
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.2.3/raffle/api/v1/get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                //This is for skyrand
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Factory.BASE_URL_MOBILE_APP + "get_question", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_object = (JSONObject) jsonArray.get(i);
                                String question = json_object.getString("question");
                                String image_photo = json_object.getString("image_photo");
                                String option_a = json_object.getString("option_a");
                                String option_b = json_object.getString("option_b");
                                String option_c = json_object.getString("option_c");
                                String option_d = json_object.getString("option_d");
                                String answer = json_object.getString("answer");
                                String hint = json_object.getString("hint");
                                question_count = json_object.getString("question_count");
                                tv_number_of_questions_in_api.setText(question_count);
                                String s1 = tv_number_of_questions_in_api.getText().toString();
                                int_number_of_questions = Integer.parseInt(s1);
//                                Log.e("int_number_of_questions_log", "" + int_number_of_questions);
//                                Log.e("image_photo_json", image_photo);
                                String s11 = str_local_host + image_photo;
//                                Log.e("s1111_json", s11);
//                                imagequestionsIntegerArrayList.add(s11);
                                answersIntegerArrayList_01.add(option_a);
                                answersIntegerArrayList_02.add(option_b);
                                answersIntegerArrayList_03.add(option_c);
                                answersIntegerArrayList_04.add(option_d);
                                Final_Answer_ArrayList.add(answer);

                                int n1 = Integer.parseInt(tv_remaining_count_value.getText().toString());
//                                Log.e("tv_remaining_count_value_get_quest", "" + n1);
                                int_reaming_page_count_value = n1 + 1;
                                int_count_value = n1;
//                                Log.e("int_count_value_get_question", "" + int_count_value);

                                /*str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
                                if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                                    File imgFile_01 = new File(imagequestionsIntegerArrayList.get(0).replace("/ ", ""));
                                    Log.e("imgFile_01_get_quest", String.valueOf(imgFile_01));
                                    Glide.with(Game_Act.this)
                                            .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                                            .into(iv_changing_image);
                                }*/


                                /*str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
                                if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                                    File imgFile_01 = new File(imagequestionsIntegerArrayList.get(0).replace("/ ", ""));
                                    Log.e("imgFile_01_get_question", String.valueOf(imgFile_01));
                                    Glide.with(Game_Act.this)
                                            .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                                            .into(iv_changing_image);
                                }*/


                                if (answersIntegerArrayList_01.get(0).length() >= 50 || answersIntegerArrayList_02.get(0).length() >= 50
                                        || answersIntegerArrayList_03.get(0).length() >= 50 || answersIntegerArrayList_04.get(0).length() >= 50) {

                                    tv_A_alphabet.setText("A");
                                    tv_B_alphabet.setText("B");
                                    tv_C_alphabet.setText("C");
                                    tv_D_alphabet.setText("D");

                                    /*tv_A_alphabet.setText(answersIntegerArrayList_01.get(0).substring(2));
                                    tv_B_alphabet.setText(answersIntegerArrayList_02.get(0).substring(2));
                                    tv_C_alphabet.setText(answersIntegerArrayList_03.get(0).substring(2));
                                    tv_D_alphabet.setText(answersIntegerArrayList_04.get(0).substring(2));*/

                                    tv_A_alphabet.setTextSize(13);
                                    tv_B_alphabet.setTextSize(13);
                                    tv_C_alphabet.setTextSize(13);
                                    tv_D_alphabet.setTextSize(13);
                                } else {

                                    tv_A_alphabet.setText("A");
                                    tv_B_alphabet.setText("B");
                                    tv_C_alphabet.setText("C");
                                    tv_D_alphabet.setText("D");

                                    /*tv_A_alphabet.setText(answersIntegerArrayList_01.get(0).substring(2));
                                    tv_B_alphabet.setText(answersIntegerArrayList_02.get(0).substring(2));
                                    tv_C_alphabet.setText(answersIntegerArrayList_03.get(0).substring(2));
                                    tv_D_alphabet.setText(answersIntegerArrayList_04.get(0).substring(2));*/
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

    private void startTimer(long countDownIntervalSeconds) {
        long milli = countDownIntervalSeconds + 1000;
        countDownTimer = new CountDownTimer(milli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = millisUntilFinished / 1000;
//                Log.e("Seconds", "" + seconds);
                String s1 = format("%2d", seconds).trim();
                if (s1.length() == 2) {
                    tv_timer_seconds_count_game_one.setText(s1);
                } else {
                    tv_timer_seconds_count_game_one.setText("0" + s1);
                }
                if (seconds == 9) {
                    constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                    tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                } else if (seconds == 3) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                    tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                    tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                    constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_red_alert_bg));
                } else if (seconds == 2) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                } else if (seconds == 1) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                } else if (seconds == 0) {
                    tv_timer_seconds_count_game_one.setText("00");
                }
            }

            @Override
            public void onFinish() {
                tv_timer_seconds_count_game_one.setText("00");
                dialog_fr_timer.show();
                dialog_fr_timer.setCancelable(false);
                tv_2x_txt.setEnabled(true);

                tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                do {
//                    Log.e("imagequestionsIntegerArrayList_onfinish", imagequestionsIntegerArrayList.toString());
                    if (int_count_value < imagequestionsIntegerArrayList.size()) {
                        int n1 = Integer.parseInt(tv_remaining_count_value.getText().toString());
//                        Log.e("onfinish_n1111", "" + n1);
                        int_reaming_page_count_value = n1 + 1;
                        int_count_value = n1;

//                        Log.e("tv_remaining_count_value_current_onfinish", String.valueOf(int_count_value));

                        try {
                            if (int_reaming_page_count_value <= int_number_of_questions) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void run() {
                                        /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
                                        /*if (answersIntegerArrayList_01.toString().equals("empty_option")) {
                                            tv_A_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_A_alphabet.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_02.toString().equals("empty_option")) {
                                            tv_B_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_B_alphabet.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_03.toString().equals("empty_option")) {
//                                            Toast.makeText(Game_Two_Act.this, "OnFinish_if", Toast.LENGTH_SHORT).show();
                                            tv_C_alphabet.setVisibility(View.GONE);
                                        } else {
//                                            Toast.makeText(Game_Two_Act.this, "OnFinish_else", Toast.LENGTH_SHORT).show();
                                            tv_C_alphabet.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_04.toString().equals("empty_option")) {
                                            tv_D_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_D_alphabet.setVisibility(View.VISIBLE);
                                        }*/

                                        tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                                        tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                                        tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                                        tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                                        constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
                                        tv_timer_seconds_count_game_one.setTextColor(getResources().getColor(R.color.white_color));
                                        tv_timer_seconds_txt.setTextColor(getResources().getColor(R.color.white_color));
                                        dialog_fr_timer.dismiss();
                                        tv_remaining_count_value.setText(valueOf(int_reaming_page_count_value));

//                                        Toast.makeText(Game_Act.this, "Onfinish", Toast.LENGTH_SHORT).show();
//                                        Log.e("int_count_value_onfinish", "" + int_count_value);
//                                        Log.e("imagequestionsIntegerArrayList_onfinish", "" + imagequestionsIntegerArrayList.get(int_count_value));

                                        str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
//                                        Log.e("str_session_images_log", str_session_images);
                                        if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                                            File imgFile_01 = new File(imagequestionsIntegerArrayList.get(int_count_value).replace("/ ", ""));
//                                            Log.e("imgFile_01_onFinish", String.valueOf(imgFile_01));
                                            Glide.with(Game_Act.this)
                                                    .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                                                    .into(iv_changing_image);
                                        }



                                        /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*/
                                        /*if (answersIntegerArrayList_01.get(int_count_value).contains("A-empty_option")) {
                                            tv_A_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_A_alphabet.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_02.get(int_count_value).contains("B-empty_option")) {
                                            tv_B_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_B_alphabet.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_03.get(int_count_value).contains("C-empty_option")) {
                                            tv_C_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_C_alphabet.setVisibility(View.VISIBLE);
                                        }
                                        if (answersIntegerArrayList_04.get(int_count_value).contains("D-empty_option")) {
                                            tv_D_alphabet.setVisibility(View.GONE);
                                        } else {
                                            tv_D_alphabet.setVisibility(View.VISIBLE);
                                        }*/


                                        if (answersIntegerArrayList_01.get(0).length() >= 50 || answersIntegerArrayList_02.get(0).length() >= 50
                                                || answersIntegerArrayList_03.get(0).length() >= 50 || answersIntegerArrayList_04.get(0).length() >= 50) {

                                            tv_A_alphabet.setText("A");
                                            tv_B_alphabet.setText("B");
                                            tv_C_alphabet.setText("C");
                                            tv_D_alphabet.setText("D");

                                            /*tv_A_alphabet.setText(answersIntegerArrayList_01.get(int_count_value).substring(2));
                                            tv_B_alphabet.setText(answersIntegerArrayList_02.get(int_count_value).substring(2));
                                            tv_C_alphabet.setText(answersIntegerArrayList_03.get(int_count_value).substring(2));
                                            tv_D_alphabet.setText(answersIntegerArrayList_04.get(int_count_value).substring(2));*/
                                            tv_A_alphabet.setTextSize(13);
                                            tv_B_alphabet.setTextSize(13);
                                            tv_C_alphabet.setTextSize(13);
                                            tv_D_alphabet.setTextSize(13);
                                        } else {

                                            tv_A_alphabet.setText("A");
                                            tv_B_alphabet.setText("B");
                                            tv_C_alphabet.setText("C");
                                            tv_D_alphabet.setText("D");

                                            /*tv_A_alphabet.setText(answersIntegerArrayList_01.get(int_count_value).substring(2));
                                            tv_B_alphabet.setText(answersIntegerArrayList_02.get(int_count_value).substring(2));
                                            tv_C_alphabet.setText(answersIntegerArrayList_03.get(int_count_value).substring(2));
                                            tv_D_alphabet.setText(answersIntegerArrayList_04.get(int_count_value).substring(2));*/
                                        }
                                        tv_remaining_count_value.setText(valueOf(int_reaming_page_count_value));
                                        tv_timer_seconds_count_game_one.setText("00");

                                        assert str_count_down_seconds != null;
                                        str_onclick_answer_selection = "Skip";
                                        str_onclick_string_answer_selection = "Skip";
                                        int int_previous_page = int_reaming_page_count_value - 1;
                                        questionnumber_ArrayList.add(valueOf(int_previous_page));
                                        answerselection_ArrayList.add(str_onclick_answer_selection);
                                        timer_ArrayList.add("0");
                                        _2x_power_up_ArrayList.add("0");

                                        int_correct_ans = 0;
                                        int_correct_ans_notations = 1;
//                                        Log.e("onfinish_01", "" + int_onclcik_2x_power_up);
                                        int int_2x_powerup = 0;
                                        Total_Result_Value_Method(int_2x_powerup, int_correct_ans, str_count_down_seconds, int_skip, int_correct_ans_notations, int_previous_page, str_onclick_string_answer_selection);
                                        str_onclick_time = tv_timer_seconds_count_game_one.getText().toString();
                                        startTimer(milliseconds);
                                        if (str_onclick_answer_selection.equalsIgnoreCase("Skip") && ((int_onclcik_2x_power_up != 0))) {
                                            if (int_2x_onclick_count != 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_2x_txt.setTextColor(getResources().getColor(R.color.colorAmber_900));
                                                tv_2x_txt.setEnabled(true);


                                                int_2x_onclick_or_not = 0;

                                                int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                                                int_2x_onclick_count = int_2x_onclick_count + 1;
                                                int_onclcik_2x_power_up = 1;
                                                tv_2x_power_up.setText(valueOf(int_2x_onclick_count));

                                                tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                tv_2x_txt.setTextColor(getResources().getColor(R.color.colorAmber_900));
                                            } else if (int_2x_onclick_count == 0 && str_onclick_answer_selection.equalsIgnoreCase("Skip")) {
                                                if (int_onclcik_2x_power_up != 0) {
                                                    int_2x_onclick_or_not = 0;
                                                    int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                                                    int_2x_onclick_count = int_2x_onclick_count + 1;
                                                    int_onclcik_2x_power_up = 1;
                                                    tv_2x_power_up.setText(valueOf(int_2x_onclick_count));

                                                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                                                    tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
                                                    tv_2x_txt.setTextColor(getResources().getColor(R.color.colorAmber_900));
                                                    tv_2x_txt.setEnabled(true);
                                                } else {
                                                    tv_2x_power_up.setText(valueOf(int_2x_onclick_count));
                                                    tv_2x_txt.setEnabled(false);

                                                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
                                                    tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
                                                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
                                                }
                                            }
                                        }
                                        int_onclcik_2x_power_up = 0;
                                    }
//                                }, 2300);
                                }, 300);
                            } else {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        str_onclick_answer_selection = "Skip";
                                        str_onclick_string_answer_selection = "Skip";
                                        int int_question_number = int_count_value;
                                        int int_previous_page = int_reaming_page_count_value - 1;
//                                        Log.e("elsefnshprevious", "" + int_previous_page);
                                        questionnumber_ArrayList.add(valueOf(int_previous_page));
                                        answerselection_ArrayList.add(str_onclick_answer_selection);
                                        timer_ArrayList.add("0");
                                        _2x_power_up_ArrayList.add("0");
                                        int_correct_ans = 0;
                                        int_correct_ans_notations = 1;
//                                        Log.e("onfinish_02", "" + int_onclcik_2x_power_up);
                                        int int_2x_powerup = 0;
                                        Total_Result_Value_Method(int_2x_powerup, int_correct_ans, str_count_down_seconds, int_skip, int_correct_ans_notations, int_previous_page, str_onclick_string_answer_selection);

                                        int_onclcik_2x_power_up = 0;
                                        countDownTimer.cancel();
                                        constraintLayout_game.setVisibility(View.GONE);
                                        constraintLayout_just_a_moment.setVisibility(View.GONE);
                                        constraintLayout_end_game.setVisibility(View.VISIBLE);
                                    }
                                }, 2300);
                            }

                        } catch (IndexOutOfBoundsException ari) {
                            ari.printStackTrace();
                        }
                    }
                } while (int_count_value > imagequestionsIntegerArrayList.size());
            }
        }.start();
        performTick(10000);
    }

    @SuppressLint("LongLogTag")
    private void Total_Result_Value_Method(int int_onclcik_2x_power_up,
                                           int int_correct_ans,
                                           String str_difference_time,
                                           int int_skip,
                                           int int_correct_ans_notations,
                                           int int_previous_or_courrent_question_number,
                                           String str_onclick_string_answer_selection) {
        dialog_fr_timer.dismiss();
        int int_sum_total_time = 0;
        int int_sum_total_crct_ans = 0;
        int int_sum_total_wrng_ans = 0;
        int int_sum_total_skip_values = 0;
        int int_difference_onclick_values;
        int int_difference_final_values;
        StringBuilder str_selection_values = new StringBuilder();
//        Log.e("str_difference_time_nr", "" + str_difference_time);


        /*This if loop is used for setting correct and wrong answer for different arraylist values.*/
        if (int_correct_ans_notations == 1) {
            total_onclick_correct_answer_values_ArrayList.add(int_correct_ans);
        }
        if (int_correct_ans_notations == 0) {
            total_onclick_wrong_answer_values_ArrayList.add(int_correct_ans);
        }
        /*This arraylist used for setting time difference value to arraylist*/
        total_onclick_time_ArrayList.add(str_difference_time);

        /*This arraylist used for setting skip values to arraylist*/
        total_skip_values_ArrayList.add(int_skip);
        _2x_power_up_continous_update_to_api_ArrayList.add(valueOf(int_onclcik_2x_power_up));

//        Log.e("_2x_power_up_continous_update_to_api_ArrayList", _2x_power_up_continous_update_to_api_ArrayList.toString());

        /*This arraylist used for setting string_selection values to arraylist*/
        answers_string_velue_selection_ArrayList.add(str_onclick_string_answer_selection);


//        Log.e("crnt_ans_list", total_onclick_correct_answer_values_ArrayList.toString());
//        Log.e("wrng_ans_list", total_onclick_wrong_answer_values_ArrayList.toString());

        /*The following for loops are used getting arrayvalues for addition method*/
        for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
        }
        for (int i = 0; i < total_onclick_correct_answer_values_ArrayList.size(); i++) {
            int_sum_total_crct_ans += total_onclick_correct_answer_values_ArrayList.get(i);
        }
        for (int i = 0; i < total_onclick_wrong_answer_values_ArrayList.size(); i++) {
            int_sum_total_wrng_ans += total_onclick_wrong_answer_values_ArrayList.get(i);
        }

        for (int i = 0; i < total_skip_values_ArrayList.size(); i++) {
            int_sum_total_skip_values += total_skip_values_ArrayList.get(i);
        }

        for (int i = 0; i < answers_string_velue_selection_ArrayList.size(); i++) {
            if (i < answers_string_velue_selection_ArrayList.size() - 1) {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i).concat(","));
            } else {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i));
            }
        }


        int_difference_onclick_values = int_sum_total_crct_ans - int_sum_total_wrng_ans;
        int_difference_final_values = int_difference_onclick_values + int_sum_total_skip_values;
//        Log.e("int_sum_total_time", "" + int_sum_total_time);
//        Log.e("int_sum_total_crct_ans", "" + int_sum_total_crct_ans);
//        Log.e("int_sum_total_wrng_ans", "" + int_sum_total_wrng_ans);
//        Log.e("int_sum_total_skp_vlus", "" + int_sum_total_skip_values);
//        Log.e("diffe_onclick_values", "" + int_difference_onclick_values);
//        Log.e("diffe_final_values", "" + int_difference_final_values);
//        Log.e("str_selection_values", "" + str_selection_values);
//        Log.e("total_onclick_time_ArrayList", total_onclick_time_ArrayList.toString());
//        Log.e("prev_r_crnt_qstn_num", "" + int_previous_or_courrent_question_number);
        if (int_previous_or_courrent_question_number == int_number_of_questions) {
            dialog_fr_timer.dismiss();
            countDownTimer.cancel();
            Glide.with(Game_Act.this).asGif().load(R.drawable.thanks_fr_playing).into(iv_end_game_sucess_message_gif);
            Get_End_Game_Contest_Details();
            int finalInt_sum_total_time = int_sum_total_time;
            String finalStr_selection_values = str_selection_values.toString();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    str_remaining_count_value = tv_remaining_count_value.getText().toString();
                    int n1_1 = Integer.parseInt(str_remaining_count_value);
                    try {
                        dialog_fr_timer.dismiss();
                        countDownTimer.cancel();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("email", str_email);
                        jsonObject.put("contest_id", str_contest_id);
                        jsonObject.put("total_onclick_time", finalInt_sum_total_time);
                        jsonObject.put("contest_answer", finalStr_selection_values);
                        jsonObject.put("onclick_time", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
                        jsonObject.put("contest_2x", _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", ""));

                        jsonObject.put("click_points_correct", total_onclick_correct_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
                        jsonObject.put("click_points_skip", total_skip_values_ArrayList.toString().replace("[", "").replace("]", ""));
                        jsonObject.put("click_points_wrong", total_onclick_wrong_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
                        jsonObject.put("total_onclick_answer_values", int_difference_final_values);
                       Log.e("Total_Json_Values_STB", jsonObject.toString());

//                        Getting_Update_Status_Details();
                        APIInterface apiInterface = Factory.getClient();
                        Call<Category_Model> call = apiInterface.GET_FINAL_RESULT_CALL("application/json", jsonObject.toString(), str_auth_token);
                        call.enqueue(new Callback<Category_Model>() {
                            @Override
                            public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                                if (response.code() == 200) {
                                    if (response.isSuccessful()) {
                                        /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
                                        str_playby = "Coins";
                                        Get_Points_Add_Delete_Details(str_playby);

                                        constraintLayout_end_game.setVisibility(View.VISIBLE);
                                        constraintLayout_game.setVisibility(View.GONE);
                                        constraintLayout_just_a_moment.setVisibility(View.GONE);
                                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                                        countDownTimer.cancel();
                                        Getting_Update_Status_Details();
                                        Get_End_Game_Contest_Details();
                                        constraintLayout_end_game.setBackgroundResource((R.drawable.end_game_grey_bg));
                                    }
                                } else if (response.code() == 401) {
                                    Toast_Message.showToastMessage(Game_Act.this, response.message());
                                } else if (response.code() == 500) {
                                    Toast_Message.showToastMessage(Game_Act.this, response.message());
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
            }, 1200);
        }

    }

    private void Get_Points_Add_Delete_Details(String str_playby) {
        try {
            /*String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
            Cursor cursor = db.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {

                } while (cursor.moveToNext());
            }
            cursor.close();*/
            JSONObject jsonObject = new JSONObject();
            if (str_playby.equalsIgnoreCase("Ads")) {
                jsonObject.put("email", str_email);
                jsonObject.put("wallet", str_mCoinCountText);
                jsonObject.put("playby", str_playby);
//                Log.e("add_dlt_json_value_ad", jsonObject.toString());
            } else if (str_playby.equalsIgnoreCase("Coins")) {
                jsonObject.put("email", str_email);
                if (str_entry_fees.equalsIgnoreCase("Free")) {
                    jsonObject.put("wallet", "0");
                } else {
                    jsonObject.put("wallet", str_entry_fees);
                }
                jsonObject.put("playby", str_playby);
//                Log.e("add_dlt_json_value_coins", jsonObject.toString());
            }

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_Wallet_Point_Delete_Call("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        String str_amount = response.body().current_amt;
                        Navigation_Drawer_Act.tv_points.setText(str_amount);
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
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

    /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live , or else 2 means contest is completed*/
    private void Getting_Update_Status_Details_Initial(String int_play_status) {
        /*play_status==>1 Live or Playing*/
        /*play_status==>2 Completed*/
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", int_play_status);
            jsonObject.put("email", str_email);
//            Log.e("update_response_init", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {

                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
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

    private void Getting_Update_Status_Details() {
        /*play_status==>1 Live or Playing*/
        /*play_status==>2 Completed*/
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", "2");
            jsonObject.put("email", str_email);
//            Log.e("update_response_end", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            constraintLayout_game.setVisibility(View.GONE);
                            constraintLayout_just_a_moment.setVisibility(View.GONE);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            constraintLayout_end_game.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                            Get_End_Game_Contest_Details();
                            constraintLayout_end_game.setBackgroundResource((R.drawable.end_game_grey_bg));
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
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


    private void Get_End_Game_Contest_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("end_game_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_CONTEST_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        try {
//                        Toast.makeText(Game_Act.this, "Else_Response", Toast.LENGTH_SHORT).show();
                            assert response.body() != null;
                            if (response.body().data == null) {
//                            Toast.makeText(Game_Act.this, "Null", Toast.LENGTH_SHORT).show();
                                rv_single_card_end_game_one.setVisibility(View.VISIBLE);
                            } else {
                                rv_single_card_end_game_one.setVisibility(View.VISIBLE);
                            }
                            str_code = response.body().code;
                            str_message = response.body().message;
//                        Log.e("str_code-->", str_code);
//                        Log.e("str_message-->", str_message);
//                        Log.e("str_datum_value-->", String.valueOf(response.body()));


                            rv_single_card_end_game_one.setVisibility(View.VISIBLE);
                            contestAdapter_for_end_game = new ContestAdapter_For_End_Game(Game_Act.this, response.body().data);
                            rv_single_card_end_game_one.setAdapter(contestAdapter_for_end_game);
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void performTick(int millisUntilFinished) {
        tv_timer_seconds_count_game_one.setText(valueOf(Math.round(millisUntilFinished * 0.001f)));
//        Log.e("performclick", "" + Math.round(millisUntilFinished * 0.001f));
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

    private void DBEXPORT() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.spot_the_ballgame" + "/databases/" + "Spottheball.db";
        String backupDBPath = "Spottheball_Demo.db";
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (constraintLayout_end_game.getVisibility() == View.VISIBLE) {
            Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            if (countDownTimer != null) {
                dialog = new Dialog(Game_Act.this);
                dialog.setContentView(R.layout.logout_alert);
                TextView tv_cancel, tv_yes;
                tv_yes = dialog.findViewById(R.id.tv_yes);
                tv_cancel = dialog.findViewById(R.id.tv_cancel);
                dialog.setCancelable(false);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        constraintLayout_end_game.setVisibility(View.GONE);
                        constraintLayout_just_a_moment.setVisibility(View.GONE);
                        constraintLayout_end_game.setVisibility(View.GONE);
                        int n1 = timer_ArrayList.size();

                        /*this following lines are used for setting unanswered questions for "0"*/
                        int int_current_question_num = Integer.parseInt(tv_remaining_count_value.getText().toString());
                        int n3 = int_number_of_questions - int_current_question_num;


//                        Log.e("crnt_question_num", "" + int_current_question_num);
//                        Log.e("int_number_of_questions", "" + int_number_of_questions);
//                        Log.e("differencevalue", "" + n3);

                        int_correct_ans_notations = 1;
//                        Log.e("anslctn_AryLst_sz", "" + answerselection_ArrayList.size());
                        int int_ary_size = answerselection_ArrayList.size();

                        int int_sum_total_time = 0;
//                        Log.e("Before_total_onclick_time_ArrayList_alert_STB", total_onclick_time_ArrayList.toString());
                        for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
                            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
                        }

//                        Log.e("int_sum_total_time_alert_STB", String.valueOf(int_sum_total_time));
//                        Log.e("After_total_onclick_time_ArrayList_alert_STB", total_onclick_time_ArrayList.toString());
                        for (int i = int_ary_size; i < int_number_of_questions; i++) {
//                            Log.e("n333", "" + i);
//                            Log.e("str_skip_loop", "" + str_skip);
                            duplicate_answerselection_ArrayList.add("Skip");
                            duplicate_onclick_time_ArrayList.add(str_count_down_seconds);
                            duplicate_onclick_point_ArrayList.add(str_skip);
                            duplicate_correct_answerselection_ArrayList.add(valueOf(int_correct_ans));
                        }
//                        _2x_power_up_continous_update_to_api_ArrayList.add(String.valueOf(int_onclcik_2x_power_up));
//                        Log.e("int_ary_size_STB", String.valueOf(int_ary_size));
//                        Log.e("_2x_power_up_continous_update_to_api_ArrayListtttt_STB", _2x_power_up_continous_update_to_api_ArrayList.toString());
                        for (int i = int_ary_size; i < int_number_of_questions; i++) {
                            _2x_power_up_continous_update_to_api_ArrayList.add("0");
                        }
//                        Log.e("_2x_power_up_continous_update_to_api_ArrayList_wt_STB", _2x_power_up_continous_update_to_api_ArrayList.toString());
                        String str_duplicate_answerselection = duplicate_answerselection_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_onclick_time = duplicate_onclick_time_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_onclick_point_value = duplicate_onclick_point_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_onclick_correct_answer_point_value = duplicate_correct_answerselection_ArrayList.toString().replace("[", "").replace("]", "");
                        String str_duplicate_2x_power_values = _2x_power_up_continous_update_to_api_ArrayList.toString().replace("[", "").replace("]", "");


//                        Log.e("int_correct_ans_logout_alert_STB", "" + int_correct_ans);
//                        Log.e("str_duplicate_2x_power_values_logout_alert_STB", str_duplicate_2x_power_values);
//                        Log.e("str_seconds_logout_alert_STB", str_count_down_seconds);
//                        Log.e("int_skip_logout_alert_STB", "" + int_skip);
//                        Log.e("str_skip_logout_alert_STB", "" + str_skip);
//                        Log.e("int_correct_ans_notations_logout_alert_STB", "" + int_correct_ans_notations);
//                        Log.e("str_duplicate_answerselection_logout_alert_STB", "" + str_duplicate_answerselection);
//                        Log.e("str_duplicate_onclick_time_logout_alert_STB", "" + str_duplicate_onclick_time);
//                        Log.e("str_duplicate_onclick_point_value_logout_alert_STB", "" + str_duplicate_onclick_point_value);
//                        Log.e("str_duplicate_onclick_correct_answer_point_value_STB", "" + str_duplicate_onclick_correct_answer_point_value);

                        int int_sum_total_time1 = 0;
//                        Log.e("Before_total_onclick_time_ArrayList_alert_02_STB", duplicate_onclick_time_ArrayList.toString());
                        for (int i = 0; i < duplicate_onclick_time_ArrayList.size(); i++) {
                            int_sum_total_time1 += Integer.parseInt(duplicate_onclick_time_ArrayList.get(i));
                        }
//                        Log.e("int_sum_total_time_alert_02_STB", String.valueOf(int_sum_total_time1));
//                        Log.e("After_total_onclick_time_ArrayList_alert_02_STB", duplicate_onclick_time_ArrayList.toString());

                        int int_total_onclick_time = int_sum_total_time + int_sum_total_time1;
//                        Log.e(" int_total_onclick_time_STB", String.valueOf(int_total_onclick_time));


                        Total_Result_Value_Method_Without_End_Game_Screen(
                                int_onclcik_2x_power_up,
                                int_correct_ans,
                                str_count_down_seconds,
                                int_skip,
                                int_correct_ans_notations,
                                str_duplicate_answerselection,
                                str_duplicate_onclick_time,
                                str_duplicate_onclick_point_value,
                                str_duplicate_onclick_correct_answer_point_value,
                                int_total_onclick_time,
                                str_duplicate_2x_power_values);
                        dialog.dismiss();
                    }
                });
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            } else {
                Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void Total_Result_Value_Method_Without_End_Game_Screen(
            int int_onclcik_2x_power_up,
            final int int_correct_ans,
            String str_difference_time,
            int int_skip,
            int int_correct_ans_notations,
            String str_onclick_string_answer_selection,
            String str_duplicate_onclick_time,
            String str_duplicate_onclick_point_value,
            String str_duplicate_onclick_correct_answer_point_value,
            int int_total_onclick_time,
            String str_duplicate_2x_power_values) {
        dialog_fr_timer.dismiss();
        int int_sum_total_time = 0;
        int int_sum_total_crct_ans = 0;
        int int_sum_total_wrng_ans = 0;
        int int_sum_total_skip_values = 0;
        int int_difference_onclick_values;
        int int_difference_final_values;
        StringBuilder str_selection_values = new StringBuilder();
        StringBuilder str_onclick_time_values = new StringBuilder();
        StringBuilder str_onclick_point_values = new StringBuilder();
//        Log.e("str_difference_time_wt", "" + str_difference_time);
//        Log.e("int_correct_ans_wt", "" + int_correct_ans);
//        Log.e("str_onclk_ans_slctn_wt", "" + str_onclick_string_answer_selection);
//        Log.e("str_dupl_onclk_pt_vl_wt", "" + str_duplicate_onclick_point_value);
//        Log.e("str_dupl_onclk_time_wt", "" + str_duplicate_onclick_time);


        /*This if loop is used for setting correct and wrong answer for different arraylist values.*/
        if (int_correct_ans_notations == 1) {
            total_onclick_correct_answer_values_ArrayList.add(int_correct_ans);
        }
        if (int_correct_ans_notations == 0) {
            total_onclick_wrong_answer_values_ArrayList.add(int_correct_ans);
        }
        /*This arraylist used for setting time difference value to arraylist*/
//        total_onclick_time_ArrayList.add(str_difference_time);

//        Log.e("total_onclick_time_ArrayList_without", total_onclick_time_ArrayList.toString());
        /*This arraylist used for setting skip values to arraylist*/
        total_skip_values_ArrayList.add(int_skip);
//        Log.e("total_skip_values_ArrayList_value", total_skip_values_ArrayList.toString());
        /*This arraylist used for setting string_selection values to arraylist*/
        answers_string_velue_selection_ArrayList.add(str_onclick_string_answer_selection);

        /*The following for loops are used getting arrayvalues for addition method*//*
        for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
        }*/

//        Log.e("int_sum_total_time_inside_method", "" + int_sum_total_time);
//        Log.e("BEFORE_total_onclick_time_ArrayList_inside_method", "" + total_onclick_time_ArrayList.toString());
//        total_onclick_time_ArrayList.add(String.valueOf(str_duplicate_onclick_time));
        total_onclick_time_ArrayList.add(str_duplicate_onclick_time);
//        Log.e("AFTER_total_onclick_time_ArrayList_inside_method", "" + total_onclick_time_ArrayList.toString());


        /*for (int i = 0; i < total_onclick_correct_answer_values_ArrayList.size(); i++) {
            int_sum_total_crct_ans += total_onclick_correct_answer_values_ArrayList.get(i);
        }*/

//        Log.e("int_sum_total_time", "" + int_sum_total_time);
        for (int i = 0; i < total_onclick_correct_answer_values_ArrayList.size(); i++) {
            int_sum_total_crct_ans += total_onclick_correct_answer_values_ArrayList.get(i);
        }
        for (int i = 0; i < total_onclick_wrong_answer_values_ArrayList.size(); i++) {
            int_sum_total_wrng_ans += total_onclick_wrong_answer_values_ArrayList.get(i);
        }

        for (int i = 0; i < total_skip_values_ArrayList.size(); i++) {
            int_sum_total_skip_values += total_skip_values_ArrayList.get(i);
        }


        for (int i = 0; i < answers_string_velue_selection_ArrayList.size(); i++) {
            if (i < answers_string_velue_selection_ArrayList.size() - 1) {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i).concat(","));
            } else {
                str_selection_values.append(answers_string_velue_selection_ArrayList.get(i));
            }
        }
        /*total_onclick_time_ArrayList.add(str_duplicate_onclick_time);
        for (int i = 0; i < total_onclick_time_ArrayList.size(); i++) {
            int_sum_total_time += Integer.parseInt(total_onclick_time_ArrayList.get(i));
            if (i < total_onclick_time_ArrayList.size() - 1) {
                str_onclick_time_values.append(total_onclick_time_ArrayList.get(i).concat(","));
            } else {
                str_onclick_time_values.append(total_onclick_time_ArrayList.get(i));
            }
        }*/

//        Log.e("int_sum_total_timell", "" + int_sum_total_time);
        if (int_correct_ans_notations == 1) {
            total_all_onclick_point_values_ArrayList.add(valueOf(int_correct_ans));
            total_all_onclick_point_values_ArrayList.add(valueOf(int_skip));
        } else if (int_correct_ans_notations == 0) {
            total_all_onclick_point_values_ArrayList.add(valueOf(int_correct_ans));
            total_all_onclick_point_values_ArrayList.add(valueOf(int_skip));
        }
//        Log.e("tot_onclk_crct_ans_vlu_aryLst_wt", total_onclick_correct_answer_values_ArrayList.toString());
        total_all_onclick_point_values_ArrayList.add(str_duplicate_onclick_point_value);
        for (int i = 0; i < total_all_onclick_point_values_ArrayList.size(); i++) {
            if (i < total_all_onclick_point_values_ArrayList.size() - 1) {
                str_onclick_point_values.append(total_all_onclick_point_values_ArrayList.get(i).concat(","));
            } else {
                str_onclick_point_values.append(total_all_onclick_point_values_ArrayList.get(i));
            }
        }

        for (int i = 0; i < duplicate_onclick_point_ArrayList.size(); i++) {
            int_duplicate_total_value += Integer.parseInt(duplicate_onclick_point_ArrayList.get(i));
        }
//        Log.e("int_duplicate_total_value_logout_aler_STB", "" + int_duplicate_total_value);
//        Log.e("inside_method_skip_value_logout_alert_STB", "" + duplicate_onclick_point_ArrayList.toString());
//        Log.e("json_skip_value_arraylist_STB", "" + total_skip_values_ArrayList.toString());


        for (int i = 0; i < duplicate_correct_answerselection_ArrayList.size(); i++) {
            int_duplicate_corect_answer_value += Integer.parseInt(duplicate_correct_answerselection_ArrayList.get(i));
        }
//        Log.e("int_duplicate_corect_answer_value_logout_alert", "" + int_duplicate_corect_answer_value);
//        Log.e("duplicate_correct_answerselection_ArrayList_inside_method", "" + duplicate_correct_answerselection_ArrayList.toString());


        int_difference_onclick_values = int_sum_total_crct_ans - int_sum_total_wrng_ans;
        int_difference_final_values = int_difference_onclick_values + int_sum_total_skip_values;
//        Log.e("int_total_onclick_time_wt", "" + int_total_onclick_time);
//        Log.e("sum_total_crct_ans_wt", "" + int_sum_total_crct_ans);
//        Log.e("sum_total_wrng_ans_wt", "" + int_sum_total_wrng_ans);
//        Log.e("sum_total_skp_vlus_wt", "" + int_sum_total_skip_values);
//        Log.e("diff_onclick_values_wt", "" + int_difference_onclick_values);
//        Log.e("diff_final_values_wt", "" + int_difference_final_values);
//        Log.e("str_onclick_point_values_wt", "" + str_onclick_point_values);
//        Log.e("total_onclick_time_ArrayList_wt", String.valueOf(total_onclick_time_ArrayList.size() - 1));
//        Log.e("prev_r_crnt_qstn_num", "" + int_previous_or_courrent_question_number);

        dialog_fr_timer.dismiss();
        countDownTimer.cancel();
        int finalInt_sum_total_time = int_total_onclick_time;
        String finalStr_selection_values = str_selection_values.toString();
        str_remaining_count_value = tv_remaining_count_value.getText().toString();
        int n1_1 = Integer.parseInt(str_remaining_count_value);
        try {
            dialog_fr_timer.dismiss();
            countDownTimer.cancel();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("total_onclick_time", finalInt_sum_total_time);
            jsonObject.put("total_onclick_answer_values", int_difference_final_values);
            jsonObject.put("contest_answer", finalStr_selection_values);
            jsonObject.put("onclick_time", total_onclick_time_ArrayList.toString().replace("[", "").replace("]", ""));
            jsonObject.put("contest_2x", str_duplicate_2x_power_values);
            jsonObject.put("click_points_skip", duplicate_onclick_point_ArrayList.toString().replace("[", "").replace("]", ""));
            jsonObject.put("click_points_wrong", total_onclick_wrong_answer_values_ArrayList.toString().replace("[", "").replace("]", ""));
            jsonObject.put("click_points_correct", duplicate_correct_answerselection_ArrayList.toString().replace("[", "").replace("]", ""));
//            Log.e("json_Value_WT_STB", jsonObject.toString());
            constraintLayout_game.setVisibility(View.GONE);

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_FINAL_RESULT_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {



                            /*This method is used for setting play status value 1 or 2 if play status value is 1 means the contest is live ,
                             or else 2 means contest is completed*/
                            str_playby = "Coins";
                            Get_Points_Add_Delete_Details(str_playby);

//                            Log.e("without_crct_res", "trueeee");
                            constraintLayout_game.setVisibility(View.GONE);
                            countDownTimer.cancel();
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            Getting_Update_Status_Details_Without_End_Game_Screen();
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
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

    @SuppressLint("LongLogTag")
    private void Getting_Update_Status_Details_Without_End_Game_Screen() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", str_contest_id);
            jsonObject.put("play_status", "2");
            jsonObject.put("email", str_email);
//            Log.e("update_json_response_STB", jsonObject.toString());
//            Log.e("str_auth_tokenkkkk_STB", str_auth_token);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_UPDATE_STATUS_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
                            countDownTimer.cancel();
                            Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                            intent.putExtra("refresh_value", "1");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Game_Act.this, response.message());
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

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close_end_game:
                SessionSave.ClearSession("Contest_Value", Game_Act.this);
                SessionSave.ClearSession("Banner_Contest_Value", Game_Act.this);
                Intent intent = new Intent(Game_Act.this, Navigation_Drawer_Act.class);
                startActivity(intent);
                break;
            case R.id.tv_2x_txt:
                n2_new_value = Integer.parseInt(tv_remaining_count_value.getText().toString());
                n1_old_value = n2_new_value;
                int_onclcik_2x_power_up = 1;

//                Log.e("2x_count", "" + int_2x_onclick_count);
                /*if (int_2x_onclick_count == 0) {
                    tv_two_x_game_two.setEnabled(false);
                    tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                } else {*/
                if (int_2x_onclick_or_not == 0) {
                    int_2x_onclick_or_not = 1;
                    int_onclcik_2x_power_up = 1;
//                    Toast.makeText(Game_Two_Act.this, "elseeee", Toast.LENGTH_SHORT).show();
                    is_2x_btn_clicked = true;
//                    Log.e("tv_2x_power_up_minus", tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = int_2x_onclick_count - 1;
                    tv_2x_power_up.setText(valueOf(int_2x_onclick_count));
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal_orange));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));

                    /*if (int_2x_onclick_count == 0) {
                            tv_two_x_game_two.setEnabled(false);
                            tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                        }*/
                } else if (int_2x_onclick_or_not == 1) {
                    int_2x_onclick_or_not = 0;
                    int_onclcik_2x_power_up = 0;
//                    Toast.makeText(Game_Two_Act.this, "ifffff", Toast.LENGTH_SHORT).show();
//                    Log.e("tv_2x_power_up_plus", tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                    int_2x_onclick_count = int_2x_onclick_count + 1;
                    tv_2x_power_up.setText(valueOf(int_2x_onclick_count));
                    tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//                    tv_2x_txt.setTextColor(getResources().getColor(R.color.colorAmber_900));
                    tv_2x_txt.setTextColor(getResources().getColor(R.color.colorAmber_900));
                }

                /*else {
                        int_onclcik_2x_power_up = 1;
                        is_2x_btn_clicked = true;
                        Toast.makeText(Game_Two_Act.this, "elseeee", Toast.LENGTH_SHORT).show();
                        is_2x_btn_clicked = true;
                        Log.e("tv_2x_power_up_minus", tv_2x_power_up.getText().toString());
                        int_2x_onclick_count = Integer.parseInt(tv_2x_power_up.getText().toString());
                        int_2x_onclick_count = int_2x_onclick_count - 1;
                        tv_2x_power_up.setText(String.valueOf(int_2x_onclick_count));
                        tv_two_x_game_two.setBackground(getResources().getDrawable(R.drawable._2x_bg_normal));
                        tv_two_x_game_two.setTextColor(getResources().getColor(R.color.black_color));
                    }*/
                break;
            case R.id.tv_A_alphabet:
//                str_onclick_string_answer_selection = tv_A_alphabet.getText().toString();
                str_onclick_string_answer_selection = "A";
                boolean isCanceled = true;
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_01.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_one.getText().toString();

                int_str_seconds = Integer.parseInt(str_count_down_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);

                str_onclick_2x_powerup = valueOf(int_onclcik_2x_power_up);
                str_remaining_count_value = tv_remaining_count_value.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                int n1 = n1_1 - 1;
                String s11 = Final_Answer_ArrayList.toString();
                /*This condition is used for checking user onclicked value is equal
                 *to api answer value if yes means normal else it means wrong and gives vibration effect
                 * */
//                if (Final_Answer_ArrayList.get(n1).equals(str_onclick_answer_selection)) {
//                tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer));
                tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                tv_A_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                Disable_All_Buttons();

                if (int_onclcik_2x_power_up != 0) {
//                    Log.e("int_onclcik_2x_power_up_if_side_btn_01", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
//                    Log.e("int_onclcik_2x_power_up_else_side_btn_01", "" + int_onclcik_2x_power_up);
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }

                /*str_remaining_count_value = tv_remaining_count_value.getText().toString();
                int n1_11 = Integer.parseInt(str_remaining_count_value);
                Log.e("tv_remaining_count_value_screen_one_method", "" + n1_11);
                str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);

                //            Log.e("str_session_images_log", str_session_images);
                if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                    File imgFile_01 = new File(imagequestionsIntegerArrayList.get(n1_11).replace("/ ", ""));
                    Log.e("imgFile_01_Screen_0ne", String.valueOf(imgFile_01));
                    Glide.with(Game_Act.this)
                            .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                            .into(iv_changing_image);
                }*/

        /*} else{
            tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer_circle));
            tv_A_alphabet.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_A_alphabet.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.onclick_wrong_answer_shake));
            Disable_All_Buttons();
//                    Log.e("wrng_ans_01", "" + int_wrong_ans);
//                    Log.e("wrng_dfrnce_time_01", "" + str_difference_time);
//                    Log.e("wrng_int_skip_01", "" + int_skip);
            if (int_onclcik_2x_power_up != 0) {
                int_skip_else = 0;
                int_correct_ans_notations = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_01_if", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            } else {
                int_skip_else = 0;
                int_correct_ans_notations = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_01_else", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            }
        }*/
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.black_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
                }, 50);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_effect_circle));
                        tv_A_alphabet.setTextColor(getResources().getColor(R.color.black_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);*/

                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1,
                                str_onclick_answer_selection,
                                str_difference_time,
                                str_onclick_2x_powerup,
                                int_2x_onclick_or_not);
                    }
                }, 100);
                break;
            case R.id.tv_B_alphabet:
//                str_onclick_string_answer_selection = tv_B_alphabet.getText().toString();
                str_onclick_string_answer_selection = "B";
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_02.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_one.getText().toString();

                int_str_seconds = Integer.parseInt(str_count_down_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);
                str_onclick_2x_powerup = valueOf(int_onclcik_2x_power_up);
                str_remaining_count_value = tv_remaining_count_value.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                String s12 = Final_Answer_ArrayList.toString();
                int n2 = n1_1 - 1;
                tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer_circle));
                tv_B_alphabet.setTextColor(getResources().getColor(R.color.black_color));
//                if (Final_Answer_ArrayList.get(n2).equals(str_onclick_answer_selection)) {
                if (int_onclcik_2x_power_up != 0) {
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
//                        Log.e("crct_ans_02_if", "" + int_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
//                        Log.e("crct_ans_02_else", "" + int_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }
                Disable_All_Buttons();
                //Created On 23-05-2020
                /*str_remaining_count_value = tv_remaining_count_value.getText().toString();
                int n1_12 = Integer.parseInt(str_remaining_count_value);
                Log.e("tv_remaining_count_value_screen_one_method", "" + n1_12);
                str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);

                //            Log.e("str_session_images_log", str_session_images);
                if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                    File imgFile_01 = new File(imagequestionsIntegerArrayList.get(n1_12).replace("/ ", ""));
                    Log.e("imgFile_01_Screen_0ne", String.valueOf(imgFile_01));
                    Glide.with(Game_Act.this)
                            .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                            .into(iv_changing_image);
                }*/

        /*} else{
            tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer_circle));
            tv_B_alphabet.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_B_alphabet.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.onclick_wrong_answer_shake));
//                    Log.e("wrng_ans_02_wrng", "" + int_wrong_ans);
//                    Log.e("wrng_dfrnce_time", "" + str_difference_time);
//                    Log.e("wrng_int_skip_02_wrng", "" + int_skip);

            if (int_onclcik_2x_power_up != 0) {
                int_correct_ans_notations = 0;
                int_skip_else = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_02_if", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            } else {
                int_skip_else = 0;
                int_correct_ans_notations = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_02_else", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            }
            Disable_All_Buttons();
        }*/

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
//                }, 800);
                }, 50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Created On 23-05-2020
                        /*tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);*/

                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1,
                                str_onclick_answer_selection,
                                str_difference_time,
                                str_onclick_2x_powerup,
                                int_2x_onclick_or_not);
                    }
//                }, 1200);
                }, 100);
                break;
            case R.id.tv_C_alphabet:
//                str_onclick_string_answer_selection = tv_C_alphabet.getText().toString();
                str_onclick_string_answer_selection = "C";
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_03.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_one.getText().toString();
                int_str_seconds = Integer.parseInt(str_count_down_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);
                str_onclick_2x_powerup = valueOf(int_onclcik_2x_power_up);
                str_remaining_count_value = tv_remaining_count_value.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                String s13 = Final_Answer_ArrayList.toString();
                int n3 = n1_1 - 1;
//                if (Final_Answer_ArrayList.get(n3).equals(str_onclick_answer_selection)) {
                tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer_circle));
                tv_C_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                Disable_All_Buttons();
                if (int_onclcik_2x_power_up != 0) {
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }
                //Created On 23-05-2020
                /*str_remaining_count_value = tv_remaining_count_value.getText().toString();
                int n1_13 = Integer.parseInt(str_remaining_count_value);
                Log.e("tv_remaining_count_value_screen_one_method", "" + n1_13);
                str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);

                //            Log.e("str_session_images_log", str_session_images);
                if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                    File imgFile_01 = new File(imagequestionsIntegerArrayList.get(n1_13).replace("/ ", ""));
                    Log.e("imgFile_01_Screen_0ne", String.valueOf(imgFile_01));
                    Glide.with(Game_Act.this)
                            .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                            .into(iv_changing_image);
                }*/

        /*} else{
            tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer_circle));
            tv_C_alphabet.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_C_alphabet.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.onclick_wrong_answer_shake));
//                    Log.e("wrng_ans_03_wrng", "" + int_wrong_ans);
//                    Log.e("wrng_dfrnce_time3", "" + str_difference_time);
//                    Log.e("wrng_int_skip_03_wrng", "" + int_skip);
            if (int_onclcik_2x_power_up != 0) {
                int_skip_else = 0;
                int_correct_ans_notations = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_03_if", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            } else {
                int_skip_else = 0;
                int_correct_ans_notations = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_03_else", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            }
            Disable_All_Buttons();
        }*/
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
//                }, 800);
                }, 50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);*/

                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1,
                                str_onclick_answer_selection,
                                str_difference_time,
                                str_onclick_2x_powerup,
                                int_2x_onclick_or_not);
                    }
//                }, 1200);
                }, 100);
                break;
            case R.id.tv_D_alphabet:
//                str_onclick_string_answer_selection = tv_D_alphabet.getText().toString();
                str_onclick_string_answer_selection = "D";
                countDownTimer.cancel();
                str_onclick_answer_selection = (answersIntegerArrayList_04.get(0).substring(0, 1));
                str_onclick_time = tv_timer_seconds_count_game_one.getText().toString();
                int_str_seconds = Integer.parseInt(str_count_down_seconds);
                int_str_onclick_time = Integer.parseInt(str_onclick_time);
                difference_time = int_str_seconds - int_str_onclick_time;
                str_difference_time = valueOf(difference_time);
                str_final_answer = Final_Answer_ArrayList.get(0);
                str_onclick_2x_powerup = valueOf(int_onclcik_2x_power_up);
                str_remaining_count_value = tv_remaining_count_value.getText().toString();
                n1_1 = Integer.parseInt(str_remaining_count_value);
                String s14 = Final_Answer_ArrayList.toString();
                int n4 = n1_1 - 1;
//                if (Final_Answer_ArrayList.get(n4).equals(str_onclick_answer_selection)) {
                tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_correct_answer_circle));
                tv_D_alphabet.setTextColor(getResources().getColor(R.color.black_color));
                Disable_All_Buttons();
                if (int_onclcik_2x_power_up != 0) {
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                } else {
                    int_skip_else = 0;
                    int_correct_ans_notations = 1;
                    int_correct_ans = Integer.parseInt(str_correct_ans);
                    Total_Result_Value_Method(int_onclcik_2x_power_up, int_correct_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
                }

                //Created On 23-05-2020
                /*str_remaining_count_value = tv_remaining_count_value.getText().toString();
                int n1_14 = Integer.parseInt(str_remaining_count_value);
                Log.e("tv_remaining_count_value_screen_one_method", "" + n1_14);
                str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);

                //            Log.e("str_session_images_log", str_session_images);
                if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                    File imgFile_01 = new File(imagequestionsIntegerArrayList.get(n1_14).replace("/ ", ""));
                    Log.e("imgFile_01_Screen_0ne", String.valueOf(imgFile_01));
                    Glide.with(Game_Act.this)
                            .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                            .into(iv_changing_image);
                }*/

        /*} else{
            tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.onclick_wrong_answer_circle));
            tv_D_alphabet.setTextColor(getResources().getColor(R.color.black_color));
//                    tv_D_alphabet.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.onclick_wrong_answer_shake));
//                    Log.e("wrng_ans_03_wrng", "" + int_wrong_ans);
//                    Log.e("wrng_dfrnce_time3", "" + str_difference_time);
//                    Log.e("wrng_int_skip_03_wrng", "" + int_skip);
            if (int_onclcik_2x_power_up != 0) {
                int_correct_ans_notations = 0;
                int_skip_else = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_04_if", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans * 2, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            } else {
                int_skip_else = 0;
                int_correct_ans_notations = 0;
                int_wrong_ans = Integer.parseInt(str_wrong_ans);
//                        Log.e("wrng_ans_04_else", "" + int_wrong_ans);
                Total_Result_Value_Method(int_wrong_ans, str_difference_time, int_skip_else, int_correct_ans_notations, n1_1, str_onclick_string_answer_selection);
            }
            Disable_All_Buttons();
        }*/

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);
                    }
//                }, 800);
                }, 50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
                        tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));

                        dialog_fr_timer.show();
                        dialog_fr_timer.setCancelable(false);*/


                        int_2x_onclick_or_not = 0;
                        Screen_01_Method(n1_1,
                                str_onclick_answer_selection,
                                str_difference_time,
                                str_onclick_2x_powerup,
                                int_2x_onclick_or_not);
                    }
//                }, 1200);
                }, 100);
                break;

        }

    }

    @SuppressLint("LongLogTag")
    private void Screen_01_Method(int int_reaming_page_count_value,
                                  String str_onclick_answer_selection,
                                  String str_difference_time,
                                  String str_onclick_2x_powerup,
                                  int int_2x_onclick_or_not1) {
        tv_2x_txt.setEnabled(true);
        tv_A_alphabet.setEnabled(true);
        tv_B_alphabet.setEnabled(true);
        tv_C_alphabet.setEnabled(true);
        tv_D_alphabet.setEnabled(true);

        str_remaining_count_value = tv_remaining_count_value.getText().toString();
        int n1_1 = Integer.parseInt(str_remaining_count_value);
        Log.e("tv_remaining_count_value_screen_one_method", "" + n1_1);
//        Log.e("imagequestionsIntegerArrayList_screen_01_n111", "" + imagequestionsIntegerArrayList.get(n1_1));
//        Log.e("imagequestionsIntegerArrayList_screen_01", "" + imagequestionsIntegerArrayList.toString());
        if (n1_1 < int_number_of_questions) {
            if (countDownTimer == null) {
                countDownTimer.start();
            }
            int n2 = n1_1 + 1;
            tv_remaining_count_value.setText(valueOf(n2));

            str_session_images = SessionSave.getSession("All_Image_File", Game_Act.this);
//            Log.e("str_session_images_log", str_session_images);
            if (!(str_session_images.equals("0") || str_session_images.equalsIgnoreCase("No data"))) {
                File imgFile_01 = new File(imagequestionsIntegerArrayList.get(n1_1).replace("/ ", ""));
                Log.e("imgFile_01_Screen_0ne", String.valueOf(imgFile_01));
                Glide.with(Game_Act.this)
                        .load(imgFile_01.getAbsolutePath().replace("/ ", ""))
                        .apply(RequestOptions.placeholderOf(R.drawable.card_loading).error(R.drawable.card_loading))
                        .into(iv_changing_image);
            }



            /*This following code used for if any of 4 option getting -empty_option means we need to hide that button else show the button*//*
            if (answersIntegerArrayList_01.get(n1_1).contains("A-empty_option")) {
                tv_A_alphabet.setVisibility(View.GONE);
            } else {
                tv_A_alphabet.setVisibility(View.VISIBLE);
            }
            if (answersIntegerArrayList_02.get(n1_1).contains("B-empty_option")) {
                tv_B_alphabet.setVisibility(View.GONE);
            } else {
                tv_B_alphabet.setVisibility(View.VISIBLE);
            }
            if (answersIntegerArrayList_03.get(n1_1).contains("C-empty_option")) {
                tv_C_alphabet.setVisibility(View.GONE);
            } else {
                tv_C_alphabet.setVisibility(View.VISIBLE);
            }
            if (answersIntegerArrayList_04.get(n1_1).contains("D-empty_option")) {
                tv_D_alphabet.setVisibility(View.GONE);
            } else {
                tv_D_alphabet.setVisibility(View.VISIBLE);
            }*/

            if (answersIntegerArrayList_01.get(0).length() >= 50 || answersIntegerArrayList_02.get(0).length() >= 50
                    || answersIntegerArrayList_03.get(0).length() >= 50 || answersIntegerArrayList_04.get(0).length() >= 50) {

                tv_A_alphabet.setText("A");
                tv_B_alphabet.setText("B");
                tv_C_alphabet.setText("C");
                tv_D_alphabet.setText("D");

                /*tv_A_alphabet.setText(answersIntegerArrayList_01.get(n1_1).substring(2));
                tv_B_alphabet.setText(answersIntegerArrayList_02.get(n1_1).substring(2));
                tv_C_alphabet.setText(answersIntegerArrayList_03.get(n1_1).substring(2));
                tv_D_alphabet.setText(answersIntegerArrayList_04.get(n1_1).substring(2));
*/
                tv_A_alphabet.setTextSize(13);
                tv_B_alphabet.setTextSize(13);
                tv_C_alphabet.setTextSize(13);
                tv_D_alphabet.setTextSize(13);
            } else {

                tv_A_alphabet.setText("A");
                tv_B_alphabet.setText("B");
                tv_C_alphabet.setText("C");
                tv_D_alphabet.setText("D");

                /*tv_A_alphabet.setText(answersIntegerArrayList_01.get(n1_1).substring(2));
                tv_B_alphabet.setText(answersIntegerArrayList_02.get(n1_1).substring(2));
                tv_C_alphabet.setText(answersIntegerArrayList_03.get(n1_1).substring(2));
                tv_D_alphabet.setText(answersIntegerArrayList_04.get(n1_1).substring(2));*/
            }
            tv_A_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
            tv_B_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
            tv_C_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
            tv_D_alphabet.setBackground(getResources().getDrawable(R.drawable.normal_effect_circle));
            tv_A_alphabet.setTextColor(getResources().getColor(R.color.white_color));
            tv_B_alphabet.setTextColor(getResources().getColor(R.color.white_color));
            tv_C_alphabet.setTextColor(getResources().getColor(R.color.white_color));
            tv_D_alphabet.setTextColor(getResources().getColor(R.color.white_color));

            if (int_onclcik_2x_power_up == 1) {
                int_onclcik_2x_power_up = 0;
            }
            questionnumber_ArrayList.add(valueOf(int_reaming_page_count_value));
            answerselection_ArrayList.add(str_onclick_answer_selection);
            timer_ArrayList.add(str_difference_time);
            _2x_power_up_ArrayList.add(str_onclick_2x_powerup);
        }

        if (int_2x_onclick_count == 0) {
            tv_2x_txt.setEnabled(false);
            tv_2x_txt.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
            constraintLayout_tv_2x_power_up.setBackground(getResources().getDrawable(R.drawable._2x_bg_grey_disable));
            tv_2x_power_up.setTextColor(getResources().getColor(R.color.black_color));
            tv_2x_txt.setTextColor(getResources().getColor(R.color.black_color));
        } else {
            tv_2x_txt.setBackground(getResources().getDrawable(R.drawable.two_x_onclick_bg));
//            tv_2x_txt.setTextColor(getResources().getColor(R.color.new_game_scrn_2x_color));
            tv_2x_txt.setTextColor(getResources().getColor(R.color.colorAmber_900));
            tv_2x_txt.setEnabled(true);
        }
        int_2x_onclick_or_not = int_2x_onclick_or_not1;
        dialog_fr_timer.dismiss();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            constraintLayout_count_down_timer_game_one.startAnimation(AnimationUtils.loadAnimation(Game_Act.this, R.anim.stop_animation_shake));
            constraintLayout_count_down_timer_game_one.setBackground(getResources().getDrawable(R.drawable.timer_circle_bg));
            startTimer(milliseconds);
        }
    }

    private void Disable_All_Buttons() {
        tv_2x_txt.setEnabled(false);
        tv_A_alphabet.setEnabled(false);
        tv_B_alphabet.setEnabled(false);
        tv_C_alphabet.setEnabled(false);
        tv_D_alphabet.setEnabled(false);
    }

    private void Alphabet_Input_Method_For_2X(int int_2_x_count, String
            str_remaining_count_value) {
        Toast_Message.showToastMessage(Game_Act.this, "2_X_Value :" + int_2_x_count + "  Remaining count :" + str_remaining_count_value);
    }

    private void Alphabet_Input_Method() {
        str_remaining_count_value = tv_remaining_count_value.getText().toString();
        str_user_selection_value = str_remaining_count_value + str_alphabet_input_value;
        str_db_concat = str_user_selection_value;
//        Log.e("str_db_concat1111", str_db_concat);
        ArrayList<String> arrayList = new ArrayList<>();
        String select = "select USER_SELECTION_VALUE from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_db_values = cursor.getString(0);
//                Log.e("str_db_values", str_db_values);
                if (!str_db_values.isEmpty()) {
                    arrayList.add(str_db_values);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
//        Log.e("arrayList", arrayList.toString());
        if (arrayList.size() != 0) {
            for (int i = 0; i <= arrayList.size() - 1; i++) {
              /*  if (i == 0) {
                    str_db_concat = arrayList.get(i);
                } else {
              */
                str_db_concat = arrayList.get(i) + "," + str_db_concat;
                // }
            }
//            Log.e("str_db_concat_after", str_db_concat);
        }
       /* else {
            Toast.makeText(this, "hhhh", Toast.LENGTH_SHORT).show();
        }*/
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_SELECTION_VALUE", str_db_concat);
//        Log.e("USER_SELECTIONcontent", contentValues.toString());
        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
        DBEXPORT();
    }


    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void setSnackbarMessage(String status) {
        String internetStatus;
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = getResources().getString(R.string.back_online_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.timer_bg_color);
        } else {
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
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }
}

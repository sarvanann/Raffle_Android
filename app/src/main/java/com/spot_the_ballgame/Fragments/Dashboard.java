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
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spot_the_ballgame.Adapter.AlbumsAdapter;
import com.spot_the_ballgame.Adapter.ContestAdapter;
import com.spot_the_ballgame.Adapter.GridView_Adapter;
import com.spot_the_ballgame.Adapter.View_Pager_Adapter;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.APIService;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Album;
import com.spot_the_ballgame.Model.Carousel_Model;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Toast_Message;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;
import net.mrbin99.laravelechoandroid.channel.SocketIOPresenceChannel;
import net.mrbin99.laravelechoandroid.channel.SocketIOPrivateChannel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Socket;
import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Dashboard extends Fragment implements View.OnClickListener {
    private int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private APIService apiService;
    private String str_categroies;

    //    private ConstraintLayout hidden_layout_main, constraintLayout_filter_txt;
    private int is_Visible = 0;
    private GridView grid_view;
    GridView_Adapter gridView_adapter;

    int int_point_value = 50;
    int int_total_balance_points;
    int int_final_point_value;
    private SQLiteDatabase db;
    private String str_email;
    //    private ConstraintLayout constraintLayout_game_01, constraintLayout_game_02, constraintLayout_game_03, constraintLayout_game_04, constraintLayout_game_05,
    private ConstraintLayout recycler_view_constraint_layout;
    private int int_onclcik_value;

    private FirebaseAnalytics firebaseAnalytics;
    private RecyclerView rv_game_list;
    ArrayList<String> Json_arrayList = new ArrayList<>();
    private String str_code, str_message, str_wallet1, str_wallet2, str_contest_id, str_name, str_price;
    ContestAdapter contestAdapter;


    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    private Snackbar snackbar;
    ShimmerLayout mShimmerViewContainer, shimmer_view_container_for_carosel;
    private SwipeRefreshLayout swip_refresh_layout;

    private AlbumsAdapter adapter;
    private View_Pager_Adapter view_pager_adapter;
    //    private AlbumsAdapter_Date_Contest albumsAdapter_date_contest;
    private List<Album> albumList;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    private String str_auth_token,
            str_wallet_coins,
            str_wallet_rupees,
            str_initial_coins,
            str_reward_point,
            str_min_withdraw_amt,
            str_max_withdraw_amt,
            str_referral_rules,
            str_current_amt;

    private TextView tv_no_data_available_fr_dashboard;
    ViewPager viewPager;
    String str_session_contest_value = "";
    String str_session_banner_contest_value = "";
    private FragmentManager support_FragmentManager;

    ConstraintLayout constraint_layout_no_contest;
    TextView tv_no_contest_for_now_txt, tv_no_internet_txt;
    ImageView gif_image_view;
    CoordinatorLayout constraintLayout_dashboard;

    Bundle bundle;
    String str_refresh_value = "";

    /*This constructor is used for if contest is played when the used click the contest it'll directly move to my_contest page
     * in this project fragment to framgent transision not supported so,
     * we get FragmentManager from Navigation drawer activity and used it in adapter class*/
    public Dashboard(FragmentManager supportFragmentManager) {
        this.support_FragmentManager = supportFragmentManager;
    }

    Echo echo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant", "LongLogTag"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dashboard_new, container, false);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(R.id.app_bar);
        constraint_layout_no_contest = view.findViewById(R.id.constraint_layout_no_contest);
        tv_no_contest_for_now_txt = view.findViewById(R.id.tv_no_contest_for_now_txt);
        gif_image_view = view.findViewById(R.id.gif_image_view);
        constraintLayout_dashboard = view.findViewById(R.id.constraintLayout_dashboard);
        initCollapsingToolbar();
        viewPager = view.findViewById(R.id.viewPager);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        shimmer_view_container_for_carosel = view.findViewById(R.id.shimmer_view_container_for_carosel);
        recycler_view_constraint_layout = view.findViewById(R.id.recycler_view_constraint_layout);
//        constraintLayout_game_01 = view.findViewById(R.id.constraintLayout_game_01);
//        constraintLayout_game_02 = view.findViewById(R.id.constraintLayout_game_02);
//        constraintLayout_game_03 = view.findViewById(R.id.constraintLayout_game_03);
//        constraintLayout_game_04 = view.findViewById(R.id.constraintLayout_game_04);
//        constraintLayout_game_05 = view.findViewById(R.id.constraintLayout_game_05);
        rv_game_list = view.findViewById(R.id.rv_game_list);
        swip_refresh_layout = view.findViewById(R.id.swip_refresh_layout);
//        hidden_layout_main = view.findViewById(R.id.hidden_layout_main);
//        constraintLayout_filter_txt = view.findViewById(R.id.constraintLayout_filter_txt);
        grid_view = view.findViewById(R.id.grid_view);
        tv_no_data_available_fr_dashboard = view.findViewById(R.id.tv_no_data_available_fr_dashboard);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");




        /*constraintLayout_filter_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Visible == 0) {
                    is_Visible = 8;
                } else if (is_Visible == 8) {
                    is_Visible = 0;
                }
                hidden_layout_main.setVisibility(is_Visible);
            }
        });
*/

/*
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getActivity(), albumList);
*/

        ((SimpleItemAnimator) Objects.requireNonNull(rv_game_list.getItemAnimator())).setSupportsChangeAnimations(false);
        rv_game_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_game_list.setHasFixedSize(true);
        rv_game_list.setVisibility(View.VISIBLE);
        Navigation_Drawer_Act.tv_title_txt.setText("");
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.home_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.GONE);
        swip_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    constraint_layout_no_contest.setVisibility(View.GONE);
                    SessionSave.ClearSession("Contest_Value", Objects.requireNonNull(getActivity()));
                    GetContest_Details();
                }
                swip_refresh_layout.setRefreshing(false);

            }
        });

        /*SocketIOPrivateChannel privateChannel = echo.privateChannel("channel1");
        privateChannel.listen("NewComment", new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Event thrown.
                Log.e("sucess_args", "suceess̥");
            }
        });

        privateChannel.listenForWhisper("hello", new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Received !
                Log.e("Received_args", "Received");
            }
        });
        SocketIOPresenceChannel presenceChannel = echo.presenceChannel("presence-channel");
        presenceChannel.here(new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Gets users present in this channel.
                // Called just after connecting to it.
                Log.e("presence_args", "presence");
            }
        });

        presenceChannel.joining(new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Called when new user join the channel.
                Log.e("presence_joing_args", "presence_joing");
            }
        });

        presenceChannel.leaving(new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Called when a user leave the channel
                Log.e("presence_leaving_args", "presence_leaving");
            }
        });

        echo.on(Socket.EVENT_ERROR, new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Callback
                Log.e("EVENT_ERROR_args", "EVENT_ERROR̥");
            }
        });*/

        Laravel_Host_Method();
        //        FullScreenMethod();
        String select = "select EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        DBEXPORT();
//        Log.e("dash_str_email", str_email);
        str_auth_token = SessionSave.getSession("Token_value", getActivity());
        Log.e("str_auth_token", str_auth_token);
        str_session_contest_value = SessionSave.getSession("Contest_Value", getActivity());
        str_session_banner_contest_value = SessionSave.getSession("Banner_Contest_Value", getActivity());
//        Log.e("str_session_banner_contest_value", str_session_banner_contest_value);
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            recycler_view_constraint_layout.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            shimmer_view_container_for_carosel.startShimmerAnimation();
            Get_App_Settings_Details();
            Get_Balance_Details();

            /*
             * Here we use cache concept when application is destroyed
             * Session value gets 'No data' it call 'get_contest' method
             * else if app not destroyed means rv_game_list use the session value to show list of data
             */
            if (str_session_banner_contest_value.equalsIgnoreCase("No data")) {
//                Toast.makeText(getActivity(), "Banner_Null", Toast.LENGTH_SHORT).show();
                prepareAlbums();
            } else {
//                Toast.makeText(getActivity(), "Banner_Non_Null", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Carousel_Model.Banner_Contest>>() {
                }.getType();
                ArrayList<Carousel_Model.Banner_Contest> arrayList = gson.fromJson(str_session_banner_contest_value, type);
                shimmer_view_container_for_carosel.setVisibility(View.GONE);
                shimmer_view_container_for_carosel.stopShimmerAnimation();
                view_pager_adapter = new View_Pager_Adapter(arrayList, getActivity(), support_FragmentManager);
                viewPager.setAdapter(view_pager_adapter);
                viewPager.setPadding(130, 0, 130, 0);
            }


            bundle = getActivity().getIntent().getExtras();
            if (bundle == null) {
                str_refresh_value = null;
            } else {
                Log.e("str_refresh_value", str_refresh_value);
                str_refresh_value = bundle.getString("refresh_value");
                if (Objects.equals(str_refresh_value, "1")) {
                    GetContest_Details();
                }
            }


            /*
             * Here we use cache concept when application is destroyed
             * Session value gets 'No data' it call 'get_contest' method
             * else if app not destroyed means rv_game_list use the session value to show list of data
             */
            if (str_session_contest_value.equalsIgnoreCase("No data")) {
//                Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
                GetContest_Details();
            } else {
//                Toast.makeText(getActivity(), "Non_Null", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Category_Model.Data>>() {
                }.getType();
                ArrayList<Category_Model.Data> arrayList = gson.fromJson(str_session_contest_value, type);
                recycler_view_constraint_layout.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();

                rv_game_list.setVisibility(View.VISIBLE);
                tv_no_data_available_fr_dashboard.setVisibility(View.GONE);
                contestAdapter = new ContestAdapter(getActivity(), arrayList, support_FragmentManager);
                rv_game_list.setAdapter(contestAdapter);
            }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getActivity(), "seheduler", Toast.LENGTH_SHORT).show();
                    GetContest_Details();
                    if (str_session_banner_contest_value.equalsIgnoreCase("No data")) {
                        prepareAlbums();
                    }
                }
            }, 3600000);


//            Get_User_Wallet_Details();
//            GetContest_Details_Using_Volley();
        }




       /* if (rowIDExistEmail(str_email)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("BALANCE", 500);
            Log.e("Cnt_Values_navi", contentValues.toString());
            db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
            DBEXPORT();
        }


        String select = "select EMAIL ,BALANCE from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
                str_balance_points = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DBEXPORT();
        if (str_balance_points == null || str_balance_points.isEmpty()) {
            tv_points.setText(String.valueOf(int_total_balance_points));
        } else {
            tv_points.setText(str_balance_points);
        }
        DBEXPORT();*/


      /*  String select = "select EMAIL ,BALANCE from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
                str_balance_points = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DBEXPORT();

        if (str_balance_points == null || str_balance_points.isEmpty()) {
            tv_points.setText(String.valueOf(int_total_balance_points));
        } else {
            tv_points.setText(str_balance_points);
        }*/


        /*PagerContainer container_new = view.findViewById(R.id.pager_container);
        ViewPager pager = container_new.getViewPager();
        pager.setAdapter(new MyPagerAdapter());
        pager.setClipChildren(false);

        pager.setOffscreenPageLimit(15);
        container_new.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        boolean showTransformer = getActivity().getIntent().getBooleanExtra("showTransformer", false);
        if (showTransformer) {
            new CoverFlow.Builder()
                    .with(pager)
                    .scale(0.3f)
                    .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                    .spaceSize(0f)
                    .build();
        } else {
            pager.setPageMargin(30);
        }*/
        return view;
    }

    private void Laravel_Host_Method() {
        // Setup options
        EchoOptions options = new EchoOptions();
        // Setup host of your Laravel Echo Server
        options.host = "http://datasics.in";
        /*
         * Add headers for authorizing your users (private and presence channels).
         * This line can change matching how you have configured
         * your guards on your Laravel application
         */
//        options.headers.put("Authorization", "Bearer {token}");
        // Create the client
        echo = new Echo(options);
        echo.connect(new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Success connect
                Log.e("Success_connect", "working" + Arrays.toString(args));
            }
        }, new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Error connect
                Log.e("Error_connect", "working" + Arrays.toString(args));
            }
        });
        echo.channel("channel1")
                .listen("NewComment", new EchoCallback() {
                    @Override
                    public void call(Object... args) {
                        // Event thrown.
                        Log.e("echo_call", "sfdsfnkdlsfndksl");
                    }
                });
    }

    private void Get_Balance_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_WALLET_BALALNCE_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_wallet1 = response.body().wallet1;
                            str_wallet2 = response.body().wallet2;
                            str_current_amt = response.body().current_amt;
                            Navigation_Drawer_Act.tv_points.setText(str_current_amt);
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

    private void Get_App_Settings_Details() {
        try {
//            Log.e("auth_token_app_settings", str_auth_token);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_APP_SETTINGS_DETAILS(str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_wallet_coins = response.body().wallet_coins;
                            str_wallet_rupees = response.body().wallet_rupees;
                            str_reward_point = response.body().reward_point;
                            str_initial_coins = response.body().initial_coins;
                            str_min_withdraw_amt = response.body().min_withdraw_amt;
                            str_max_withdraw_amt = response.body().max_withdraw_amt;
                            str_referral_rules = response.body().referral_rules;
                            SessionSave.SaveSession("Referral_Rules_Link", str_referral_rules, getActivity());
                            SessionSave.SaveSession("Wallet_Coins", str_wallet_coins, getActivity());
                            SessionSave.SaveSession("Wallet_Rupees", str_wallet_rupees, getActivity());
                            SessionSave.SaveSession("Reward_Point", str_reward_point, getActivity());
                            SessionSave.SaveSession("Minimum_Withdraw_Amount", str_min_withdraw_amt, getActivity());
                            SessionSave.SaveSession("Maximum_Withdraw_Amount", str_max_withdraw_amt, getActivity());

//                        Log.e("str_wallet_coins", str_wallet_coins);
//                        Log.e("str_wallet_rupees", str_wallet_rupees);
//                        Log.e("str_initial_coins", str_initial_coins);
//                        Log.e("str_reward_point", str_reward_point);
//                        Log.e("str_min_withdraw_amt", str_min_withdraw_amt);
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

    private void Get_User_Wallet_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("wallet_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_WalletDetailsModelCall("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_code = response.body().code;
                            str_message = response.body().message;
                            str_wallet1 = response.body().data.get(0).wallet1;
                            str_wallet2 = response.body().data.get(0).wallet2;
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
    private void GetContest_Details_Using_Volley() {
        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, "http://192.168.0.113/stb-api/index.php/categories/get_contest", null, new com.android.volley.Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> arrayList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_object = (JSONObject) jsonArray.get(i);
                                str_categroies = json_object.getString("categories");
//                                Log.e("str_categroies_json", "" + str_categroies);

                                arrayList.add(str_categroies);
                            }
                            GridView_Adapter booksAdapter = new GridView_Adapter(getActivity(), arrayList);
                            grid_view.setAdapter(booksAdapter);
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


    private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void prepareAlbums() {
        /*int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1,
                R.drawable.album1};

        Album a = new Album("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Album("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Album("Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new Album("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Album("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Album("I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Album("Loud", 11, covers[6]);
        albumList.add(a);

        a = new Album("Legend", 14, covers[7]);
        albumList.add(a);

        a = new Album("Hello", 11, covers[8]);
        albumList.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        albumList.add(a);

        adapter.notifyDataSetChanged();*/

        try {
//            Log.e("str_auth_token_album", str_auth_token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Carousel_Model> call = apiInterface.GET_CAROUSEL_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Carousel_Model>() {
                @Override
                public void onResponse(Call<Carousel_Model> call, Response<Carousel_Model> response) {
                    assert response.body() != null;
                    try {
                        if (response.body().banner_contest != null) {
                            if (response.code() == 200) {
                                if (response.isSuccessful()) {
                                    if (Objects.requireNonNull(response.body()).banner_contest.isEmpty()) {
                                        shimmer_view_container_for_carosel.setVisibility(View.VISIBLE);
                                        shimmer_view_container_for_carosel.startShimmerAnimation();
                                        recycler_view_constraint_layout.setVisibility(View.VISIBLE);
                                        rv_game_list.setVisibility(View.VISIBLE);
                                    } else {
                                        shimmer_view_container_for_carosel.stopShimmerAnimation();
                                        shimmer_view_container_for_carosel.setVisibility(View.GONE);
//                                        Log.e("banner_size", "" + response.body().banner_contest.size());
                                        if ((response.body().banner_contest.size() != 0)) {
                                            Gson gson = new Gson();
                                            String json = gson.toJson(response.body().banner_contest);
                                            SessionSave.SaveSession("Banner_Contest_Value", json, getActivity());
                                            view_pager_adapter = new View_Pager_Adapter(response.body().banner_contest, getActivity(), support_FragmentManager);
                                            viewPager.setAdapter(view_pager_adapter);
                                            viewPager.setPadding(130, 0, 130, 0);
                                        }
                                    }
                                }
                            } else if (response.code() == 401) {
                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                            } else if (response.code() == 500) {
                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                            }
                        } else {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Something went wrong try again album :)");
                        }
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Carousel_Model> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("LongLogTag")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void GetContest_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Log.e("json_getcontest", jsonObject.toString());
            Log.e("str_auth_token_getcontest", str_auth_token);
            Call<Category_Model> call = apiInterface.GET_CONTEST_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    assert response.body() != null;
                    if (response.body().data != null) {
                        if (response.code() == 200) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                try {
//                                    Toast.makeText(getActivity(), "Network_Response", Toast.LENGTH_SHORT).show();
                                    if (Objects.requireNonNull(response.body().data).isEmpty()) {
                                        mShimmerViewContainer.setVisibility(View.VISIBLE);
                                        mShimmerViewContainer.startShimmerAnimation();
                                        gif_image_view.setVisibility(View.GONE);
                                        recycler_view_constraint_layout.setVisibility(View.VISIBLE);
                                        rv_game_list.setVisibility(View.VISIBLE);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                constraint_layout_no_contest.setVisibility(View.VISIBLE);
                                                gif_image_view.setVisibility(View.VISIBLE);
                                                try {
                                                    Glide.with(Objects.requireNonNull(getActivity())).asGif().load(R.drawable.no_contests_gif).into(gif_image_view);
                                                } catch (NullPointerException npe) {
                                                    npe.printStackTrace();
                                                }

                                                mShimmerViewContainer.setVisibility(View.GONE);
                                                mShimmerViewContainer.stopShimmerAnimation();
                                                contestAdapter = new ContestAdapter(getActivity(), response.body().data, support_FragmentManager);
                                                rv_game_list.setAdapter(contestAdapter);
                                            }
                                        }, 2500);
                                    } else {
                                        /*
                                         *Here we use the following concept for saving the data to shared preference.
                                         */
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body().data);
                                        SessionSave.SaveSession("Contest_Value", json, getActivity());

                                        recycler_view_constraint_layout.setVisibility(View.GONE);
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        mShimmerViewContainer.stopShimmerAnimation();
                                        rv_game_list.setVisibility(View.VISIBLE);
                                        str_code = response.body().code;
                                        str_message = response.body().message;
                                        rv_game_list.setVisibility(View.VISIBLE);
                                        tv_no_data_available_fr_dashboard.setVisibility(View.GONE);
                                        contestAdapter = new ContestAdapter(getActivity(), response.body().data, support_FragmentManager);
                                        rv_game_list.setAdapter(contestAdapter);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (response.code() == 401) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        } else if (response.code() == 500) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        }
                    } else {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Something went wrong try again_get_contest :)");
                    }
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {
//                    Log.e("error_Response", "" + t.getMessage());
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
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getActivity().getWindow().getDecorView();
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

    private boolean rowIDExistEmail(String str_email) {
        String select = "select * from LOGINDETAILS ";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(str_email)) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        Bundle params = new Bundle();
        params.putInt("ButtonID", v.getId());
        String str_btnName = "";
        switch (v.getId()) {
            /*case R.id.constraintLayout_game_01:
                str_btnName = "Button1Click";
                Intent intent1 = new Intent(getContext(), Game_Details_Screen_Act.class);
                intent1.putExtra("game_name", "Spot the ball");
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
               *//* if (!(str_balance_points == null || str_balance_points.equals("0"))) {
                    int int_nn = Integer.parseInt(str_balance_points);
                    ContentValues cv = new ContentValues();
                    int_final_point_value = int_nn - int_point_value;
                    cv.put("BALANCE", int_final_point_value);
                    db.update("LOGINDETAILS", cv, "EMAIL='" + str_email + "'", null);
                    Log.e("balance_value", String.valueOf(cv));
                    DBEXPORT();
                    tv_points.setText(String.valueOf(int_final_point_value));
                    Intent intent = new Intent(getContext(), Game_Act.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "less than zero", Toast.LENGTH_SHORT).show();
                }*//*
                break;
            case R.id.constraintLayout_game_02:
                str_btnName = "Button3Click";
                int_onclcik_value = 3;
                Intent intent_01 = new Intent(getContext(), Game_Two_Act.class);
                intent_01.putExtra("int_onclcik_value", String.valueOf(int_onclcik_value));
                intent_01.putExtra("game_name", "World Cricket");
                intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_01);
               *//* tv_points.setText(String.valueOf(int_final_point_value));
                Intent intent2 = new Intent(getContext(), Reward_Video_Act.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);*//*
                break;
            case R.id.constraintLayout_game_03:
                str_btnName = "Button2Click";
                int_onclcik_value = 2;
                Intent intent = new Intent(getContext(), Game_Two_Act.class);
                intent.putExtra("int_onclcik_value", String.valueOf(int_onclcik_value));
                intent.putExtra("game_name", "Movie Buff");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.constraintLayout_game_04:
                str_btnName = "Button4Click";
                int_onclcik_value = 4;
                Intent intent_02 = new Intent(getContext(), Game_Two_Act.class);
                intent_02.putExtra("int_onclcik_value", String.valueOf(int_onclcik_value));
                intent_02.putExtra("game_name", "Ranji Trophy");
                intent_02.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_02);
                break;

            case R.id.constraintLayout_game_05:
                Toast.makeText(getContext(), "In Progress", Toast.LENGTH_SHORT).show();
            *//*    int_onclcik_value = 5;
                Intent intent_03 = new Intent(getContext(), Game_Two_Act.class);
                intent_03.putExtra("int_onclcik_value", String.valueOf(int_onclcik_value));
                intent_03.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_03);
            *//*
                break;*/
        }
//        Log.e("Button_click_logged:", str_btnName);
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
//            Toast.makeText(getApplicationContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
//        mShimmerViewContainer.startShimmerAnimation();
//        shimmer_view_container_for_carosel.startShimmerAnimation();
//        String select = "select BALANCE from LOGINDETAILS";
//        Cursor cursor = db.rawQuery(select, null);
//        if (cursor.moveToFirst()) {
//            do {
//                str_balance_points = cursor.getString(0);
//                Toast.makeText(getContext(), "" + str_balance_points, Toast.LENGTH_SHORT).show();
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        Navigation_Drawer_Act.tv_points.setText(str_balance_points);
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
//                Log.e("internetStatus_else", internetStatus);
                snackbar.show();
                internetConnected = false;
                recycler_view_constraint_layout.setVisibility(View.VISIBLE);
                rv_game_list.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                gif_image_view.setVisibility(View.GONE);
                mShimmerViewContainer.startShimmerAnimation();
                constraint_layout_no_contest.setVisibility(View.GONE);
              /*  Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShimmerViewContainer.setVisibility(View.GONE);
                        constraint_layout_no_contest.setVisibility(View.VISIBLE);
                        tv_no_contest_for_now_txt.setVisibility(View.GONE);
                        tv_no_internet_txt.setVisibility(View.VISIBLE);
                        tv_check_internet_connection.setVisibility(View.VISIBLE);
                        tv_no_internet_txt.setText(R.string.no_internet_txt);
                        Glide.with(Objects.requireNonNull(getActivity())).asGif().load(R.drawable.no_internet_gif).into(gif_image_view);
                    }
                }, 2000);
*/
                shimmer_view_container_for_carosel.setVisibility(View.VISIBLE);
                shimmer_view_container_for_carosel.startShimmerAnimation();
            }
        } else {
            if (!internetConnected) {
//                Log.e("internetStatus_if", internetStatus);
                internetConnected = true;
                snackbar.show();
//                constraint_layout_no_contest.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.GONE);
                shimmer_view_container_for_carosel.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();
                shimmer_view_container_for_carosel.stopShimmerAnimation();
                rv_game_list.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                GetContest_Details();
                prepareAlbums();
                Get_Balance_Details();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        mShimmerViewContainer.stopShimmerAnimation();
        shimmer_view_container_for_carosel.stopShimmerAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        echo.disconnect();
    }
}

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.spot_the_ballgame.Adapter.AlbumsAdapter;
import com.spot_the_ballgame.Adapter.ContestAdapter;
import com.spot_the_ballgame.Adapter.GridView_Adapter;
import com.spot_the_ballgame.DemoData;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.spot_the_ballgame.Interface.APIService.BASE_URL;

public class Dashboard extends Fragment implements View.OnClickListener {
    private int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private APIService apiService;
    private String str_categroies;

    private ConstraintLayout hidden_layout_main, constraintLayout_filter_txt;
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

    FirebaseAnalytics firebaseAnalytics;
    private RecyclerView rv_game_list;
    ArrayList<String> Json_arrayList = new ArrayList<>();
    private String str_code, str_message, str_wallet1, str_wallet2, str_contest_id, str_name, str_price;
    ContestAdapter contestAdapter;


    //This is used for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;

    private ShimmerFrameLayout mShimmerViewContainer, shimmer_view_container_for_carosel;
    SwipeRefreshLayout swip_refresh_layout;

    RecyclerView recycler_view_horizontal;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBarLayout;

    String str_auth_token,
            str_wallet_coins,
            str_wallet_rupees,
            str_initial_coins,
            str_reward_point,
            str_min_withdraw_amt,
            str_max_withdraw_amt,
            str_current_amt;

    TextView tv_no_data_available_fr_dashboard;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dashboard_new, container, false);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(R.id.app_bar);
        initCollapsingToolbar();

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
        recycler_view_horizontal = view.findViewById(R.id.recycler_view_horizontal);
        hidden_layout_main = view.findViewById(R.id.hidden_layout_main);
        constraintLayout_filter_txt = view.findViewById(R.id.constraintLayout_filter_txt);
        grid_view = view.findViewById(R.id.grid_view);
        tv_no_data_available_fr_dashboard = view.findViewById(R.id.tv_no_data_available_fr_dashboard);


        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        constraintLayout_filter_txt.setOnClickListener(new View.OnClickListener() {
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


/*
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getActivity(), albumList);
*/


        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_horizontal.setLayoutManager(horizontalLayoutManager);


        ((SimpleItemAnimator) Objects.requireNonNull(rv_game_list.getItemAnimator())).setSupportsChangeAnimations(false);
        rv_game_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_game_list.setHasFixedSize(true);
        rv_game_list.setVisibility(View.VISIBLE);


//        constraintLayout_game_01.setOnClickListener(this);
//        constraintLayout_game_02.setOnClickListener(this);
//        constraintLayout_game_03.setOnClickListener(this);
//        constraintLayout_game_04.setOnClickListener(this);
//        constraintLayout_game_05.setOnClickListener(this);
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
                    recycler_view_constraint_layout.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.startShimmerAnimation();
                    GetContest_Details();
                }
                swip_refresh_layout.setRefreshing(false);

            }
        });
        //        FullScreenMethod();
        String select = "select EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DBEXPORT();
//        Log.e("dash_str_email", str_email);
        str_auth_token = SessionSave.getSession("Token_value", getActivity());
        Log.e("str_auth_token", str_auth_token);
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
            setupRetrofitAndOkHttp();
        } else {
            recycler_view_constraint_layout.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            shimmer_view_container_for_carosel.startShimmerAnimation();
            setupRetrofitAndOkHttp();
            GetContest_Details();
            Get_App_Settings_Details();
            Get_Balance_Details();
            prepareAlbums();
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
                            Log.e("str_wallet1", str_wallet1);
                            Log.e("str_wallet1", str_wallet2);
                            Log.e("str_current_amt", str_current_amt);
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
            Log.e("auth_token_app_settings", str_auth_token);
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
            Log.e("wallet_json", jsonObject.toString());
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
                            Log.e("str_wallet1", str_wallet1);
                            Log.e("str_wallet2", str_wallet2);
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
                                Log.e("str_categroies_json", "" + str_categroies);

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
            Log.e("str_auth_token_album", str_auth_token);
            APIInterface apiInterface = Factory.getClient();
            Call<Carousel_Model> call = apiInterface.GET_CAROUSEL_DETAILS(str_auth_token);
            call.enqueue(new Callback<Carousel_Model>() {
                @Override
                public void onResponse(Call<Carousel_Model> call, Response<Carousel_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            if (Objects.requireNonNull(response.body()).normal_contest.isEmpty()) {
//                            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
                                shimmer_view_container_for_carosel.setVisibility(View.VISIBLE);
                                shimmer_view_container_for_carosel.startShimmerAnimation();
                                recycler_view_constraint_layout.setVisibility(View.VISIBLE);
                                rv_game_list.setVisibility(View.VISIBLE);
                            } else {
                                shimmer_view_container_for_carosel.stopShimmerAnimation();
                                shimmer_view_container_for_carosel.setVisibility(View.GONE);
                                Log.e("normal_contest", response.body().normal_contest.toString());
                                Log.e("normal_contest_msg", response.body().message);
                                Log.e("normal_contest_sts", response.body().status);
                                adapter = new AlbumsAdapter(getActivity(), Objects.requireNonNull(response.body()).normal_contest);
                                recycler_view_horizontal.setAdapter(adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
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

    private void setupRetrofitAndOkHttp() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File httpCacheDirectory = new File(getCacheDir(), "offlineCache");
        //10 MB
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(provideCacheInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient)
                .baseUrl(BASE_URL)
                .build();
        apiService = retrofit.create(APIService.class);
    }


    private Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                okhttp3.Response originalResponse = chain.proceed(request);
                String cacheControl = originalResponse.header("Cache-Control");
                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")) {
                    CacheControl cc = new CacheControl.Builder()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(cc)
                            .build();
                    return chain.proceed(request);
                } else {
                    return originalResponse;
                }
            }
        };
    }

    private Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                try {
                    return chain.proceed(chain.request());
                } catch (Exception e) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();
                    Request offlineRequest = chain.request().newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                    return chain.proceed(offlineRequest);
                }
            }
        };
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void GetContest_Details() {
        ((SimpleItemAnimator) Objects.requireNonNull(rv_game_list.getItemAnimator())).setSupportsChangeAnimations(false);
        Cache cache = new Cache(getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain)
                            throws IOException {
                        Request request = chain.request();

                        int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                        request = request
                                .newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://192.168.0.113/stb-api/index.php/categories/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
//        APIInterface apiInterface = retrofit.create(APIInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_CONTEST_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            try {
//                            Toast.makeText(getActivity(), "Else_Response", Toast.LENGTH_SHORT).show();
                                assert response.body() != null;
                                if (response.body().data.isEmpty()) {
                                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.startShimmerAnimation();
                                    recycler_view_constraint_layout.setVisibility(View.VISIBLE);
                                    rv_game_list.setVisibility(View.VISIBLE);
                                } else {
                                    recycler_view_constraint_layout.setVisibility(View.GONE);
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    mShimmerViewContainer.stopShimmerAnimation();
                                    rv_game_list.setVisibility(View.VISIBLE);
                                    str_code = response.body().code;
                                    str_message = response.body().message;
                                    Log.e("str_datum_value-->", String.valueOf(response.body().data));
                                    rv_game_list.setVisibility(View.VISIBLE);
                                    tv_no_data_available_fr_dashboard.setVisibility(View.GONE);
                                    contestAdapter = new ContestAdapter(getActivity(), response.body().data);
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
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {
                    Log.e("error_Response", "" + t.getMessage());
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
        Log.e("Button_click_logged:", str_btnName);
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
        mShimmerViewContainer.startShimmerAnimation();
        shimmer_view_container_for_carosel.startShimmerAnimation();
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
            snackbar.getView().setBackgroundResource(R.color.black_color);
//            GetContest_Details();
        } else {
            recycler_view_constraint_layout.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            shimmer_view_container_for_carosel.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            shimmer_view_container_for_carosel.startShimmerAnimation();
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar
                    .make(Objects.requireNonNull(getView()).findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        mShimmerViewContainer.stopShimmerAnimation();
        shimmer_view_container_for_carosel.stopShimmerAnimation();
    }

    private class MyPagerAdapter extends PagerAdapter {
        ImageView imageView;
        TextView text_view;

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_cover, null);
            imageView = view.findViewById(R.id.image_cover);
            text_view = view.findViewById(R.id.text_view);
            imageView.setImageDrawable(getResources().getDrawable(DemoData.covers[position]));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(view);
            text_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "Card_Position_is" + position, Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return DemoData.covers.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}

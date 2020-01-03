package com.spot_the_ballgame.Interface;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Factory {
    //    This is for SK
//    public static final String BASE_URL_MOBILE_APP = "http://192.168.2.3/raffle/api/v1/";

    //This is for skyrand
    public static final String BASE_URL_MOBILE_APP = "http://192.168.31.245/raffle/api/v1/";
    public static final String BASE_HELP_TC_URL_MOBILE_APP = "http://192.168.31.245/raffle/";
    public static final String BASE_URL_FOR_IMAGE_LOCAL_HOST = "http://192.168.31.245/stbadmin/public";


//    public static final String BASE_URL_MOBILE_APP = "http://192.168.0.113/stb-api/index.php/user/";
//    public static final String BASE_URL_MOBILE_APP = "http://192.168.0.99:8083/stb-api/index.php/user/";


    public static APIInterface getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        CookieHandler cookieHandler = new CookieManager();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .addNetworkInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(Factory.BASE_URL_MOBILE_APP)
                .build()
                .create(APIInterface.class);
    }


}

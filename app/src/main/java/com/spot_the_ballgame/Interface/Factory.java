package com.spot_the_ballgame.Interface;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.facebook.FacebookSdk.getCacheDir;

public class Factory {
//    public static final String BASE_URL_MOBILE_APP = "http://testproject.market-makers.in/api/v1/";
//    public static final String BASE_HELP_TC_URL_MOBILE_APP = "http://testproject.market-makers.in/api/v1/";
//    public static final String BASE_URL_FOR_IMAGE_LOCAL_HOST = "http://testproject.market-makers.in/stbadmin/public/";


    //This is for live
//    public static final String BASE_URL_MOBILE_APP = "http://skyrand.in/stb/api/v1/";
//    public static final String BASE_HELP_TC_URL_MOBILE_APP = "http://skyrand.in/stb/api/v1/";
//    public static final String BASE_URL_FOR_IMAGE_LOCAL_HOST = "http://skyrand.in/stbadmin/public/";

    //This is for live
    public static final String BASE_URL_MOBILE_APP = "http://skyrand.in/raffle/api/v1/";
    public static final String BASE_HELP_TC_URL_MOBILE_APP = "http://skyrand.in/stb/api/v1/";
    public static final String BASE_URL_FOR_IMAGE_LOCAL_HOST = "http://skyrand.in/stbadmin/public";


    //This is for SK
//    public static final String BASE_URL_MOBILE_APP = "http://192.168.2.3/raffle/api/v1/";
//    public static final String BASE_HELP_TC_URL_MOBILE_APP = "http://192.168.2.3/raffle/api/v1/";
//    public static final String BASE_URL_FOR_IMAGE_LOCAL_HOST = "http://192.168.2.3/stbadmin/public/";

    //This is for skyrand
    //public static final String BASE_URL_MOBILE_APP = "http://192.168.31.245/raffle/api/v1/";
    //public static final String BASE_HELP_TC_URL_MOBILE_APP = "http://192.168.31.245/raffle/";
    //public static final String BASE_URL_FOR_IMAGE_LOCAL_HOST = "http://192.168.31.245/stbadmin/public";

    public static APIInterface getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(getCacheDir(), cacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(logging)
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

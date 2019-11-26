package com.spot_the_ballgame.Interface;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.facebook.FacebookSdk.getCacheDir;

public class Factory_For_Categories {
    public static final String BASE_URL_MOBILE_APP = "http://192.168.0.113/stb-api/index.php/categories/";

    private static int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static Cache cache = new Cache(getCacheDir(), cacheSize);


    private final static int CACHE_SIZE_BYTES = 1024 * 1024 * 2;

    public static APIInterface getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .cache(new Cache(getCacheDir(), CACHE_SIZE_BYTES))
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(Factory_For_Categories.BASE_URL_MOBILE_APP)
                .build()
                .create(APIInterface.class);
    }
}

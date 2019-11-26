package com.spot_the_ballgame.Interface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Factory {
    public static final String BASE_URL_MOBILE_APP = "http://192.168.0.113/stb-api/index.php/user/";
//    public static final String BASE_URL_MOBILE_APP = "http://192.168.0.99:8083/stb-api/index.php/user/";


    public static APIInterface getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .addInterceptor(logging)
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

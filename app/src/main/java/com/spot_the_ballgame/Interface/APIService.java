package com.spot_the_ballgame.Interface;

import com.spot_the_ballgame.Model.Category_Model;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    String BASE_URL = "https://api.chucknorris.io/jokes/";

    @GET("{path}")
    Observable<Category_Model> getRandomJoke(@Path("path") String path);
}

package com.spot_the_ballgame.Interface;

import com.google.gson.annotations.SerializedName;

public interface ErrorResponse {

    @SerializedName("message")
    public static String message = null;
    @SerializedName("error")
    static Error error = null;

    static String getMessage() {
        return message;
    }

    static Error getError() {
        return error;
    }
}

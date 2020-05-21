package com.spot_the_ballgame.Interface;

import com.spot_the_ballgame.Model.Carousel_Model;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {
    /*
     * This is for signup
     *
     * */
    @POST("signup")
    Call<UserModel> SIGNUP_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("signup")
    Call<UserModel> EMAIL_SIGNIN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("signup")
    Call<UserModel> FACE_BOOK_RESPONSE_CALL(@Header("content-type") String type, @Body String user);


    /*
     * This is for signin
     *
     * */
    @POST("signin")
    Call<UserModel> NORMAL_LOGIN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("signin")
    Call<UserModel> G_MAIL_SIGNIN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("signin")
    Call<UserModel> FACE_BOOK_SIGN_IN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    /*
     * This is for Mobile registration
     *
     * */
    @POST("registermobile")
    Call<UserModel> MOBILE_NUM_REGISTER_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("verifymobile")
    Call<UserModel> MOBILE_NUM_VERIFY_RESPONES_CALL(@Header("content-type") String type, @Body String user);

    @POST("resendcode")
    Call<UserModel> RESEND_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    /*
     * This is for Forgot Password
     *
     * */
    @POST("forgotpassword")
    Call<UserModel> FORGOT_PWD_RESPONSES_CALL(@Header("content-type") String type, @Body String user);

    @POST("forgotpassword")
    Call<UserModel> GET_RESEND_PWD_CALL(@Header("content-type") String type, @Body String user);


    /*
     * This is for Dashboard
     * */
    @POST("get_carousel_data")
    Call<Carousel_Model> GET_CAROUSEL_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @Headers("Cache-Control: public,max-age=86400,s-maxage=86400")
    @POST("get_contest")
    Call<Category_Model> GET_CONTEST_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("app_settings")
    Call<Category_Model> GET_APP_SETTINGS_DETAILS(@Header("Authorization") String authtoken);

    @POST("wallet_balance")
    Call<Category_Model> GET_WALLET_BALALNCE_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    /*
     * This is for contest onclick
     * */
    @POST("get_prize_distribution")
    Call<Category_Model> GET_PRIZE_DISTRINUTION_CALL(@Body String user, @Header("Authorization") String authtoken);

    @POST("get_rules_name")
    Call<Category_Model> GET_RULES_NAME_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("get_ranking")
    Call<Category_Model> GET_RANKING_LIST_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);


    /*
     * This is for profile
     *
     * */
    @POST("user_wallet_details")
    Call<Category_Model> GET_WalletDetailsModelCall(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("new_password_update")
    Call<Category_Model> GET_NEW_PWD_UPDATE_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);


    /*
     * This is for Wallet page
     *
     * */
    @POST("wallet_history")
    Call<Category_Model> GET_WALLET_HISTORY_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("redeem_history")
    Call<Category_Model> GET_REDEEM_HISTORY_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("redeem")
    Call<Category_Model> GET_REDEEM_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    /*
     * This is for Contest History
     *
     * */
    @POST("contest_live")
    Call<Category_Model> GET_CONTEST_LIVE_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("contest_history")
    Call<Category_Model> GET_CONTEST_HISTORY_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("get_played_records")
    Call<Category_Model> GET_PLAYED_RECORD_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    /*
     * This is for user sending played values
     *
     * */
    @POST("contest_result")
    Call<Category_Model> GET_CONTEST_RESULT_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("contest_result")
    Call<Category_Model> GET_FINAL_RESULT_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("update_status")
    Call<Category_Model> GET_UPDATE_STATUS_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    /*
     * This is for rewarded Video
     *
     **/

    @POST("points_add_delete")
    Call<Category_Model> GET_Wallet_Point_Delete_Call(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);


    @POST("verify_referral")
    Call<UserModel> VERIFY_REFERRAL_RESPONSE_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    /*
     * This is for logout
     *
     * */
    @POST("logout")
    Call<Category_Model> GET_LOGOUT_DETAILS(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);

    @POST("get_contest_assets")
    Call<Category_Model> GET_CONTEST_ASSETS_CALL(@Header("content-type") String type, @Body String user, @Header("Authorization") String authtoken);
}

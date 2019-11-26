package com.spot_the_ballgame.Interface;

import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Model.UserModel;
import com.spot_the_ballgame.Model.User_Wallet_Details_Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("sigup")
    Call<UserModel> SIGNUP_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("forgotpassword")
    Call<UserModel> FORGOT_PWD_RESPONSES_CALL(@Header("content-type") String type, @Body String user);

    @POST("registermobile")
    Call<UserModel> MOBILE_NUM_REGISTER_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("verifymobile")
    Call<UserModel> MOBILE_NUM_VERIFY_RESPONES_CALL(@Header("content-type") String type, @Body String user);

    @POST("sigup")
    Call<UserModel> EMAIL_SIGNIN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("sigin")
    Call<UserModel> NORMAL_LOGIN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("sigin")
    Call<UserModel> G_MAIL_SIGNIN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("resendcode")
    Call<UserModel> RESEND_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("sigup")
    Call<UserModel> FACE_BOOK_RESPONSE_CALL(@Header("content-type") String type, @Body String user);

    @POST("sigin")
    Call<UserModel> FACE_BOOK_SIGN_IN_RESPONSE_CALL(@Header("content-type") String type, @Body String user);


    @POST("resend_password")
    Call<UserModel> GET_RESEND_PWD_CALL(@Header("content-type") String type, @Body String user);



    @Headers("Cache-Control: max-age=640000")
    @GET("get_contest")
    Call<Category_Model> GET_CONTEST_CALL();

    @POST("get_prize_distribution")
    Call<Category_Model> GET_PRIZE_DISTRINUTION_CALL(@Body String user);

    @POST("get_rules_name")
    Call<Category_Model> GET_RULES_NAME_CALL(@Header("content-type") String type, @Body String user);

    @POST("new_password_update")
    Call<Category_Model> GET_NEW_PWD_UPDATE_CALL(@Header("content-type") String type, @Body String user);

    @POST("contest_result")
    Call<Category_Model> GET_CONTEST_RESULT_CALL(@Header("content-type") String type, @Body String user);

    @POST("update_status")
    Call<Category_Model> GET_UPDATE_STATUS_CALL(@Header("content-type") String type, @Body String user);

    @POST("user_wallet_details")
    Call<Category_Model> GET_WalletDetailsModelCall(@Header("content-type") String type, @Body String user);

    @POST("points_add_delete")
    Call<User_Wallet_Details_Model> GET_Wallet_Point_Delete_Call(@Header("content-type") String type, @Body String user);

    @POST("wallet_history")
    Call<Category_Model> GET_WALLET_HISTORY_CALL(@Header("content-type") String type, @Body String user);

    @POST("result")
    Call<Category_Model> GET_FINAL_RESULT_CALL(@Header("content-type") String type, @Body String user);
}

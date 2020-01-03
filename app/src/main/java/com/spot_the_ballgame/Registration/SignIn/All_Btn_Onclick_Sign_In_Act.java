package com.spot_the_ballgame.Registration.SignIn;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.UserModel;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Registration.SignUp.All_Btn_OnClick_Sign_Up_Act;
import com.spot_the_ballgame.Splash_Screen_Act;
import com.spot_the_ballgame.Toast_Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.Telephony.Carriers.AUTH_TYPE;

public class All_Btn_Onclick_Sign_In_Act extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    ConstraintLayout constraintLayout_google_signin, constraintLayout_facebook_signin, constraintLayout_email_signin;
    TextView tv_sign_up_in_signin, tv_sign_in_in_signin;
    LoginButton mLoginButton;
    ProgressDialog pd;
    Snackbar snackbar;
    String str_name, str_given_name,
            str_family_name,
            str_email,
            str_id,
            str_sign_up_status,
            str_code,
            fbUserId,
            fbUserFirstName,
            fbUserEmail,
            fbUserProfilePics,
            str_message,
            str_first_name,
            str_last_name,
            str_image,
            str_username;
    String str_intent_source_details = "";

    private static final String EMAIL = "email";
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    int int_selection_value = 0;
    int RC_SIGN_IN = 0;
    Uri int_profile_picture;
    Uri str_photourl;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        setContentView(R.layout.all_btn_onclick_activity_sign_in);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        pd = new ProgressDialog(All_Btn_Onclick_Sign_In_Act.this);
        tv_sign_in_in_signin = findViewById(R.id.tv_sign_in_in_signin);
        tv_sign_up_in_signin = findViewById(R.id.tv_sign_up_in_signin_sign_in);
        constraintLayout_google_signin = findViewById(R.id.constraintLayout_google_signin_sign_in);
        constraintLayout_facebook_signin = findViewById(R.id.constraintLayout_facebook_signin);
        constraintLayout_email_signin = findViewById(R.id.constraintLayout_email_signin);

        mLoginButton = findViewById(R.id.login_button_sign_in);

        tv_sign_up_in_signin.setOnClickListener(this);
        constraintLayout_google_signin.setOnClickListener(this);
        constraintLayout_facebook_signin.setOnClickListener(this);
        constraintLayout_email_signin.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        mLoginButton.setAuthType(AUTH_TYPE);

        checkLoginStatus();
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Callback registration
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    /*Getting FB signin details*/
    private void Get_Facebook_SignIn_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", "facebook");
            jsonObject.put("email", fbUserEmail);
            jsonObject.put("app_id", fbUserId);

//            jsonObject.put("email", "testuserfb@gmail.com");
//            jsonObject.put("app_id", "123456789012345678");
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.FACE_BOOK_SIGN_IN_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        str_code = response.body().status;
                        str_message = response.body().message;

                        if (str_code.equalsIgnoreCase("success")) {
                            pd.dismiss();
                            str_first_name = response.body().datum.first_name;
                            str_last_name = response.body().datum.last_name;
                            str_email = response.body().datum.email;
                            str_image = response.body().datum.image;
                            str_username = response.body().datum.username;
//                        Log.e("str_first_name", str_first_name);
//                        Log.e("str_last_name", str_last_name);
//                        Log.e("str_email", str_email);
//                        Log.e("str_username", str_username);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("STATUS", "1");
                            contentValues.put("SIGNUPSTATUS", "3");
                            db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                            DBEXPORT();

                            Intent intent = new Intent(All_Btn_Onclick_Sign_In_Act.this, Navigation_Drawer_Act.class);
//                        Intent intent = new Intent(All_Btn_Onclick_Sign_In_Act.this, Home_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else if (str_code.equalsIgnoreCase("error")) {
                            pd.dismiss();
                            Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, str_message);
                        } else {
                            pd.dismiss();
                            Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, str_message);
                        }
                    } else if (response.code() == 401) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, response.message());
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, response.message());
                    }

                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up_in_signin_sign_in:
                /*if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {*/
                Intent intent = new Intent(All_Btn_Onclick_Sign_In_Act.this, All_Btn_OnClick_Sign_Up_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.constraintLayout_google_signin_sign_in:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    Sign_In_For_Sign_In_Method();
                /*if (cursor.moveToFirst()) {
                    do {
                        str_sign_up_status = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
                cursor.close();

                if (str_sign_up_status.equals("")) {

                } else {

                }*/

                /*  int_selection_value = 1;
                str_intent_source_details = "gmail";
//                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(All_Btn_Onclick_Sign_In_Act.this, Email_Sign_In_Act.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("selection_value", "1");
                intent1.putExtra("source_details", str_intent_source_details);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
                }
                break;
            case R.id.constraintLayout_facebook_signin:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
//                    Get_Facebook_SignIn_Details();
                    String select = "Select SIGNUPSTATUS FROM LOGINDETAILS";
                    Cursor cursor = db.rawQuery(select, null);
                    int int_cursor_count = cursor.getCount();
                    if (int_cursor_count == 0) {
                        Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, getResources().getString(R.string.pls_reg_email_txt));
                    } else {
                        int_selection_value = 2;
                        str_intent_source_details = "facebook";

                        ArrayList<String> list = new ArrayList<String>();
                        list.add("email");
                        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

                        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        // Application code
                                        if (response.getError() != null) {
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            String jsonresult = String.valueOf(json);
                                            System.out.println("JSON Result" + jsonresult);

                                            fbUserId = json.optString("id");
                                            fbUserFirstName = json.optString("name");
                                            fbUserEmail = json.optString("email");
                                            fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";
//                            callApiForCheckSocialLogin(fbUserId, fbUserFirstName, fbUserEmail, fbUserProfilePics, "fb");

//                                            Log.e("fbUserId", fbUserId);
//                                            Log.e("fbUserFirstName", fbUserFirstName);
//                                            Log.e("fbUserEmail", fbUserEmail);
//                                            Log.e("fbUserProfilePics", fbUserProfilePics);
                                            Get_Facebook_SignIn_Details();
                                        }
                                        Log.v("FaceBook Response :", response.toString());
                                    }
                                });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,email,gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                Log.v("LoginActivity", "cancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
//                                Log.e("Error_exce", String.valueOf(exception));
                            }
                        });

                        cursor.close();
//                        Log.e("cursor_count", String.valueOf(int_cursor_count));
//                Toast.makeText(this, R.string.in_progress_txt, Toast.LENGTH_SHORT).show();
                       /* Intent intent2 = new Intent(All_Btn_Onclick_Sign_In_Act.this, Email_Sign_In_Act.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.putExtra("selection_value", "2");
                        intent2.putExtra("source_details", str_intent_source_details);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
                    }
                }
                break;
            case R.id.constraintLayout_email_signin:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    int_selection_value = 3;
                    str_intent_source_details = "email";

                    String s1 = "";
                    String select = "select EMAIL from LOGINDETAILS where SOURCEDETAILS ='" + str_intent_source_details + "'";
                    Cursor cursor = db.rawQuery(select, null);
                    if (cursor.moveToFirst()) {
                        do {
                            s1 = cursor.getString(0);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    Splash_Screen_Act.str_global_mail_id = s1;
                    Intent intent3 = new Intent(All_Btn_Onclick_Sign_In_Act.this, Email_Sign_In_Act.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent3.putExtra("selection_value", "3");
                    intent3.putExtra("source_details", str_intent_source_details);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
    }

    private void Sign_In_For_Sign_In_Method() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task_sign_in = GoogleSignIn.getSignedInAccountFromIntent(data);
            Handle_SignIn_Result_For_Sign_In_Method(task_sign_in);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void loadUserProfile(final AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG_01", "Facebook graph response: " + response.toString());
                        try {
                            String email_username, email_id, email_name, email_firsrname, email_lastname;
                            if (object.has("email")) {
                                email_username = object.getString("email");
//                                Log.e("email_username", email_username);
                            }
                            email_id = object.getString("id");
                            email_name = object.getString("name");
//                            Log.e("email_id", email_id);
//                            Log.e("email_name", email_name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                //ADD THIS LINES PLEASE
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//                    Log.e("fb_firstname", first_name);
//                    Log.e("fb_lastname", last_name);
//                    Log.e("fb_id", id);
//                    Log.e("fb_image_url", image_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    private void Handle_SignIn_Result_For_Sign_In_Method(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account1 = completedTask.getResult(ApiException.class);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(All_Btn_Onclick_Sign_In_Act.this);
            if (account != null) {
                str_name = account.getDisplayName();
                str_given_name = account.getGivenName();
                str_family_name = account.getFamilyName();
                str_email = account.getEmail();
                str_id = account.getId();
                str_photourl = account.getPhotoUrl();

//                Log.e("str_name", str_name);
//                Log.e("str_given_name", str_given_name);
//                Log.e("str_family_name", str_family_name);
//                Log.e("str_email", str_email);
//                Log.e("str_id", str_id);
//                Log.e("str_photourl", String.valueOf(str_photourl));

                Splash_Screen_Act.str_global_mail_id = str_email;
//                Log.e("skllkdkdount", String.valueOf(Splash_Screen_Act.str_global_mail_id));
                String select = "Select EMAIL FROM LOGINDETAILS where EMAIL ='" + Splash_Screen_Act.str_global_mail_id + "'";
                Cursor cursor = db.rawQuery(select, null);
                int int_cursor_count = cursor.getCount();
                cursor.close();
//                Log.e("cursor_count", String.valueOf(int_cursor_count));

                Get_Google_SignIn_Details();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(All_Btn_Onclick_Sign_In_Act.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    /*Getting Google signin details*/
    private void Get_Google_SignIn_Details() {
        try {
//            Log.e("str_aappp_id", str_id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", "gmail");
            jsonObject.put("email", str_email);
            jsonObject.put("app_id", str_id);

/*
            jsonObject.put("source_detail", "gmail");
            jsonObject.put("email", "testuser@gmail.com");
            jsonObject.put("app_id", "12345678901234567890");
*/

            APIInterface apiInterface = Factory.getClient();

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            Log.e("Json_value_signup_google", jsonObject.toString());
            Call<UserModel> call = apiInterface.G_MAIL_SIGNIN_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_f_name, str_l_name, str_email, str_phone_no, str_username, str_image, str_walet, str_money, str_active, str_verified, str_code, str_message;
                            str_code = response.body().status;
                            str_message = response.body().message;
                            if (str_code.equalsIgnoreCase("success")) {
                                pd.dismiss();
                                str_f_name = response.body().datum.first_name;
                                str_l_name = response.body().datum.last_name;
                                str_email = response.body().datum.email;
                                str_phone_no = response.body().datum.phoneno;
                                str_username = response.body().datum.username;
                                str_image = response.body().datum.image;
                                str_walet = response.body().datum.walet;
                                str_money = response.body().datum.money;
                                str_active = response.body().datum.verified;
                                str_verified = response.body().datum.active;
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("STATUS", "1");
                                contentValues.put("SIGNUPSTATUS", "3");
                                db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                                DBEXPORT();


                                String s1 = "";
                                String select = "select EMAIL from LOGINDETAILS where SOURCEDETAILS ='" + "gmail" + "'";
                                Cursor cursor = db.rawQuery(select, null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        s1 = cursor.getString(0);
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                                Splash_Screen_Act.str_global_mail_id = s1;


                                Intent intent = new Intent(All_Btn_Onclick_Sign_In_Act.this, Navigation_Drawer_Act.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                            Log.e("str_f_name", str_f_name);
//                            Log.e("str_l_name", str_l_name);
//                            Log.e("str_email", str_email);
//                            Log.e("str_phone_no", str_phone_no);
//                            Log.e("str_username", str_username);
//                            Log.e("str_walet", str_walet);
//                            Log.e("str_money", str_money);
//                            Log.e("str_active", str_active);
//                            Log.e("str_verified", str_verified);
                                Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, str_message);
                            } else if (str_code.equalsIgnoreCase("error")) {
                                pd.dismiss();
                                Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, str_message);
                            } else {
                                pd.dismiss();
                                Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, str_message);
                            }
                        }
                    } else if (response.code() == 401) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, response.message());
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_Onclick_Sign_In_Act.this, response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DBEXPORT() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.spot_the_ballgame" + "/databases/" + "Spottheball.db";
        String backupDBPath = "Spottheball.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
       /* GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(All_Btn_OnClick_Sign_Up_Act.this, Mobile_Num_Registration.class));
        }*/
        super.onStart();
    }

    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
    }

    /*This method is used for forced to close and open keyboard*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void softKeyboardVisibility(boolean show) {
        InputMethodManager inputMethodManager = (InputMethodManager) All_Btn_Onclick_Sign_In_Act.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
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

    private void setSnackbarMessage(String status) {
        String internetStatus;
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = getResources().getString(R.string.back_online_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab_sign_in), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.sign_up_txt);
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab_sign_in), internetStatus, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundResource(R.color.red_color_new);
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

    @Override
    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}

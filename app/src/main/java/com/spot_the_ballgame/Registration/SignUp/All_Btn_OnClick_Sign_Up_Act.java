package com.spot_the_ballgame.Registration.SignUp;

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
import android.widget.CheckBox;
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
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Registration.Mobile_Num_Registration;
import com.spot_the_ballgame.Registration.SignIn.All_Btn_Onclick_Sign_In_Act;
import com.spot_the_ballgame.Registration.SignIn.Email_Sign_In_Act;
import com.spot_the_ballgame.SessionSave;
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
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.Telephony.Carriers.AUTH_TYPE;

public class All_Btn_OnClick_Sign_Up_Act extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    ConstraintLayout constraintLayout_google_signup_in_signup,
            constraintLayout_facebook_signup_in_signup,
            constraintLayout_email_signup_in_signup;

    TextView tv_sign_up_in_signup,
            tv_sign_in_in_signup,
            tv_terms_and_conditions;

    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    LoginButton mLoginButton;

    Snackbar snackbar;
    ProgressDialog pd;
    CheckBox checkBox;

    String str_name,
            str_given_name,
            str_family_name,
            str_email,
            str_id,
            str_code,
            str_message,
            str_source_detail,
            str_first_name,
            str_last_name,
            str_app_id,
            str_image,
            str_username,
            fbUserId,
            fbUserFirstName,
            fbUserEmail,
            fbUserProfilePics;
    String str_intent_source_details = "";

    private static final String EMAIL = "email";
    Uri int_profile_picture,
            str_photourl;
    int int_selection_value = 0;
    int RC_SIGN_IN = 0;
    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    public String str_token;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        setContentView(R.layout.all_btn_onclick_activity_sign_up);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar,EMAIL varchar,PHONENO varchar,APPID varchar,WALET double,TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar,USERNAME varchar,ACTIVE int,VERIFIED int,SIGNUPSTATUS int,BALANCE int,USER_SELECTION_VALUE varchar)");
        pd = new ProgressDialog(All_Btn_OnClick_Sign_Up_Act.this);

        constraintLayout_google_signup_in_signup = findViewById(R.id.constraintLayout_google_signup_in_signup);
        constraintLayout_facebook_signup_in_signup = findViewById(R.id.constraintLayout_facebook_signup_in_signup);
        constraintLayout_email_signup_in_signup = findViewById(R.id.constraintLayout_email_signup_in_signup_sign_up);
        tv_sign_up_in_signup = findViewById(R.id.tv_sign_up_in_signup);
        tv_sign_in_in_signup = findViewById(R.id.tv_sign_in_in_signup);
        tv_terms_and_conditions = findViewById(R.id.tv_terms_and_conditions);
        checkBox = findViewById(R.id.checkBox);
        mLoginButton = findViewById(R.id.login_button_sign_up);
        tv_terms_and_conditions.setOnClickListener(this);
        tv_sign_in_in_signup.setOnClickListener(this);
        constraintLayout_google_signup_in_signup.setOnClickListener(this);
        constraintLayout_facebook_signup_in_signup.setOnClickListener(this);
        constraintLayout_email_signup_in_signup.setOnClickListener(this);


        callbackManager = CallbackManager.Factory.create();
        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        mLoginButton.setAuthType(AUTH_TYPE);
        checkLoginStatus();
        // Configure sign-in to request the user's ID, email address, and basic
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

    /*This metod is used registering fb values and send those values to api.*/
    private void Get_Facebook_SignUp_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", "facebook");
            jsonObject.put("first_name", fbUserFirstName);
            jsonObject.put("last_name", fbUserFirstName);
            jsonObject.put("email", fbUserEmail);
            jsonObject.put("app_id", fbUserId);
            jsonObject.put("image", String.valueOf(int_profile_picture));


          /*  jsonObject.put("source_detail", "facebook");
            jsonObject.put("first_name", "test");
            jsonObject.put("last_name", "user");
            jsonObject.put("email", "testuserfb@gmail.com");
            jsonObject.put("app_id", "123456789012345678");
            jsonObject.put("image", "");*/


            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.FACE_BOOK_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    str_code = response.body().status;
                    str_message = response.body().message;

                    if (str_code.equalsIgnoreCase("success")) {
                        pd.dismiss();
                        str_source_detail = response.body().datum.source_detail;
                        str_first_name = response.body().datum.first_name;
                        str_last_name = response.body().datum.last_name;
                        str_email = response.body().datum.email;
                        str_app_id = response.body().datum.app_id;
                        str_image = response.body().datum.image;
                        str_username = response.body().datum.username;

                        str_token = response.body().api_token;
                        SessionSave.SaveSession("Token_value", str_token, All_Btn_OnClick_Sign_Up_Act.this);
//                        Log.e("str_token_signup", str_token);
//                        Log.e("str_source_detail", str_source_detail);
//                        Log.e("str_first_name", str_first_name);
//                        Log.e("str_last_name", str_last_name);
//                        Log.e("str_email", str_email);
//                        Log.e("str_app_id", str_app_id);
//                        Log.e("str_username", str_username);
                        /*if (rowIDExistEmail(str_email) && rowIDExistApp_ID(str_app_id)) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("SOURCEDETAILS", str_source_detail);
                            contentValues.put("EMAIL", str_email);
                            contentValues.put("FIRSTNAME", str_first_name);
                            contentValues.put("APPID", str_app_id);
                            contentValues.put("STATUS", 1);
                            contentValues.put("SIGNUPSTATUS", 1);
                            contentValues.put("BALANCE", 5000);
//                            contentValues.put("USER_SELECTION_VALUE", "");
                            Log.e("Cnt_Values_fb_signup", contentValues.toString());
                            db.insert("LOGINDETAILS", null, contentValues);
                            DBEXPORT();
                        }*/
                        Get_Insert_DB_Sign_Up_Values();
                        Intent intent = (new Intent(All_Btn_OnClick_Sign_Up_Act.this, Mobile_Num_Registration.class));
//                        Intent intent = (new Intent(All_Btn_OnClick_Sign_Up_Act.this, Navigation_Drawer_Act.class));
                        intent.putExtra("source_details", str_source_detail);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else if (str_code.equalsIgnoreCase("error")) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, str_message);
                    } else {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, str_message);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_terms_and_conditions:
                String str_url = Factory.BASE_HELP_TC_URL_MOBILE_APP + "terms_condtions";
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(str_url));
                startActivity(intent1);
                break;

            case R.id.tv_sign_in_in_signup:
                Intent intent = new Intent(All_Btn_OnClick_Sign_Up_Act.this, All_Btn_Onclick_Sign_In_Act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.constraintLayout_google_signup_in_signup:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    if (checkBox.isChecked()) {
                        int_selection_value = 1;
                        signIn();
                    } else {
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, "Please accept terms and condition");
                    }

                }
                break;
            case R.id.constraintLayout_facebook_signup_in_signup:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    if (checkBox.isChecked()) {
                        int_selection_value = 2;
//                    Get_Facebook_SignUp_Details();

                        ArrayList<String> list = new ArrayList<String>();
                        list.add("email");
// LoginManager.getInstance().logInWithReadPermissions(this, list);
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
                                            Get_Facebook_SignUp_Details();
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
                                // App code
                                Log.v("LoginActivity", "cancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                // Log.v("LoginActivity", "" + exception);
//                                Log.e("Error_exce", String.valueOf(exception));
                            }
                        });
                    } else {
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, "Please accept terms and condition");
                    }
                }

                break;
            case R.id.constraintLayout_email_signup_in_signup_sign_up:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    if (checkBox.isChecked()) {
                        int_selection_value = 3;
                        if (int_selection_value == 3) {
                            str_intent_source_details = "email";
                            Intent intent_01 = new Intent(All_Btn_OnClick_Sign_Up_Act.this, Email_Sign_Up_Act.class);
                            intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent_01.putExtra("source_details", str_intent_source_details);
                            startActivity(intent_01);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    } else {
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, "Please accept terms and condition");
                    }
                }
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account1 = completedTask.getResult(ApiException.class);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(All_Btn_OnClick_Sign_Up_Act.this);
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
                Get_Gmail_SignUp_Response_Method();
                Splash_Screen_Act.str_global_mail_id = str_email;
//                Log.e("Gml_glbl_mail_id", Splash_Screen_Act.str_global_mail_id);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(All_Btn_OnClick_Sign_Up_Act.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void Get_Gmail_SignUp_Response_Method() {
        try {
            if (int_selection_value == 1) {
                str_intent_source_details = "gmail";
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", str_intent_source_details);
            jsonObject.put("first_name", str_name);
            jsonObject.put("last_name", str_family_name);
            jsonObject.put("email", str_email);
            jsonObject.put("app_id", str_id);
            jsonObject.put("image", str_photourl);
//            Log.e("json_Value_signup", jsonObject.toString());


            /*jsonObject.put("source_detail", str_intent_source_details);
            jsonObject.put("first_name", "test");
            jsonObject.put("last_name", "user");
            jsonObject.put("email", "testuser@gmail.com");
            jsonObject.put("app_id", "12345678901234567890");
            jsonObject.put("image", "");
            Log.e("json_Value_signup", jsonObject.toString());
*/

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.SIGNUP_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_code = response.body().status;
                            str_message = response.body().message;

//                        Log.e("str_code", str_code);
//                        Log.e("str_message", str_message);
                            if (str_code.equalsIgnoreCase("success")) {
                                pd.dismiss();
                                str_source_detail = response.body().datum.source_detail;
                                str_first_name = response.body().datum.first_name;
                                str_last_name = response.body().datum.last_name;
                                str_email = response.body().datum.email;
                                str_app_id = response.body().datum.app_id;
                                str_image = response.body().datum.image;
                                str_username = response.body().datum.username;

                                str_token = response.body().api_token;
                                SessionSave.SaveSession("Token_value", str_token, All_Btn_OnClick_Sign_Up_Act.this);
//                            Log.e("str_token_signup", str_token);


//                            Log.e("str_source_detail", str_source_detail);
//                            Log.e("str_first_name", str_first_name);
//                            Log.e("str_last_name", str_last_name);
//                            Log.e("str_email", str_email);
//                            Log.e("str_app_id", str_app_id);
//                            Log.e("str_username", str_username);

                                Get_Insert_DB_Sign_Up_Values();
//                            Intent intent = (new Intent(All_Btn_OnClick_Sign_Up_Act.this, Navigation_Drawer_Act.class));
                                Intent intent = (new Intent(All_Btn_OnClick_Sign_Up_Act.this, Mobile_Num_Registration.class));
                                intent.putExtra("source_details", str_source_detail);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else if (str_code.equalsIgnoreCase("error")) {
                                pd.dismiss();
                                Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, str_message);
                            } else {
                                pd.dismiss();
                                Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, str_message);
                            }
                        }
                    } else if (response.code() == 401) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, response.message());
//                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, "Un Authorized");
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, response.message());
//                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, "Un Authorized");
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast_Message.showToastMessage(All_Btn_OnClick_Sign_Up_Act.this, t.getMessage());
//                    Log.e("Failure_Msg", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get_Insert_DB_Sign_Up_Values() {
        String select = "Select * FROM LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        int n1 = cursor.getCount();
        if (n1 > 0) {
//            Toast.makeText(this, "if", Toast.LENGTH_SHORT).show();
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("STATUS", 0);
            db.update("LOGINDETAILS", contentValues1, null, null);
            Insert_Singup_Details();
        } else {
//            Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
            Insert_Singup_Details();
        }
        cursor.close();
        /*if (cursor.moveToFirst()) {
            do {
                int_status_db = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();*/
    }

    private void Insert_Singup_Details() {
        if (rowIDExistEmail(str_email) && rowIDExistApp_ID(str_app_id)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("SOURCEDETAILS", str_source_detail);
            contentValues.put("EMAIL", str_email);
            contentValues.put("FIRSTNAME", str_first_name);
            contentValues.put("APPID", str_app_id);
            contentValues.put("STATUS", 1);
            contentValues.put("SIGNUPSTATUS", 1);
//            contentValues.put("BALANCE", 1000);
//            Log.e("Cnt_Values_gmail_signup", contentValues.toString());
            db.insert("LOGINDETAILS", null, contentValues);
            DBEXPORT();
        }
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

    private boolean rowIDExistApp_ID(String str_app_id) {
        String select = "select * from LOGINDETAILS ";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(str_app_id)) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
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
        InputMethodManager inputMethodManager = (InputMethodManager) All_Btn_OnClick_Sign_Up_Act.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.sign_up_txt);
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
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

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        System.exit(0);
        finish();
    }*/
}

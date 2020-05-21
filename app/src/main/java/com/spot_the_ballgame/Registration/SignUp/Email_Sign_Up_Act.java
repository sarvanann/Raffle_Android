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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.UserModel;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Registration.Forgot_Pwd_Act;
import com.spot_the_ballgame.Registration.Mobile_Num_Registration;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Splash_Screen_Act;
import com.spot_the_ballgame.Toast_Message;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Email_Sign_Up_Act extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    EditText et_email_id_in_email_signin,
            et_pwd_in_signin,
            et_repeat_pwd_in_signin,
            et_name_id_in_name_signin;
    Button btn_login,
            btn_show_hide_crnt_pwd,
            btn_show_hide_repeat_pwd;
    TextView tv_forgot_pwd_in_email_signin;
    Snackbar snackbar;
    ProgressDialog pd;
    String str_source_detail,
            str_firstname,
            str_lastname,
            str_name,
            str_email_id,
            str_repeat_pwd,
            str_username,
            str_referral_code,
            str_pwd,
            str_status,
            str_message;

    public String str_token;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Bundle bundle;
    String str_api_checkbox_selection_value;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        pd = new ProgressDialog(Email_Sign_Up_Act.this);
        bundle = getIntent().getExtras();
        if (bundle == null) {
            str_source_detail = null;
            str_api_checkbox_selection_value = null;
        } else {
            str_source_detail = bundle.getString("source_details");
            str_api_checkbox_selection_value = bundle.getString("int_api_checkbox_selection_value");
        }
//        Log.e("check_box_terms", str_api_checkbox_selection_value);
        et_name_id_in_name_signin = findViewById(R.id.et_name_id_in_name_signin);
        et_email_id_in_email_signin = findViewById(R.id.et_email_id_in_email_signin);
        et_pwd_in_signin = findViewById(R.id.et_pwd_in_signin);
        tv_forgot_pwd_in_email_signin = findViewById(R.id.tv_forgot_pwd_in_email_signin);
        btn_login = findViewById(R.id.btn_login);
        et_repeat_pwd_in_signin = findViewById(R.id.et_repeat_pwd_in_signin);
        btn_show_hide_repeat_pwd = findViewById(R.id.btn_show_hide_repeat_pwd);
        btn_show_hide_crnt_pwd = findViewById(R.id.btn_show_hide_crnt_pwd);
        tv_forgot_pwd_in_email_signin.setOnClickListener(this);
        btn_login.setOnClickListener(this);


        et_pwd_in_signin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_pwd_in_signin.getText().toString().length() == 0)) {
                    btn_show_hide_crnt_pwd.setVisibility(View.VISIBLE);
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                } else {
                    btn_show_hide_crnt_pwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_show_hide_crnt_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_show_hide_crnt_pwd.getText().toString().equals("Show")) {
                    btn_show_hide_crnt_pwd.setText("Hide");
                    et_pwd_in_signin.setTransformationMethod(null);
                    et_pwd_in_signin.setSelection(et_pwd_in_signin.getText().toString().length());
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_crnt_pwd.setText("Show");
                    et_pwd_in_signin.setTransformationMethod(new PasswordTransformationMethod());
                    et_pwd_in_signin.setSelection(et_pwd_in_signin.getText().toString().length());
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                }
            }
        });


        et_repeat_pwd_in_signin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_repeat_pwd_in_signin.getText().toString().length() == 0)) {
                    btn_show_hide_repeat_pwd.setVisibility(View.VISIBLE);
                    btn_show_hide_repeat_pwd.setBackgroundResource((R.drawable.eye_open));
                } else {
                    btn_show_hide_repeat_pwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_show_hide_repeat_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_show_hide_repeat_pwd.getText().toString().equals("Show")) {
                    btn_show_hide_repeat_pwd.setText("Hide");
                    et_repeat_pwd_in_signin.setTransformationMethod(null);
                    et_repeat_pwd_in_signin.setSelection(et_repeat_pwd_in_signin.getText().toString().length());
                    btn_show_hide_repeat_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_repeat_pwd.setText("Show");
                    et_repeat_pwd_in_signin.setTransformationMethod(new PasswordTransformationMethod());
                    et_repeat_pwd_in_signin.setSelection(et_repeat_pwd_in_signin.getText().toString().length());
                    btn_show_hide_repeat_pwd.setBackgroundResource((R.drawable.eye_open));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                et_repeat_pwd_in_signin.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    str_name = et_name_id_in_name_signin.getText().toString();
                    str_email_id = et_email_id_in_email_signin.getText().toString();
                    str_pwd = et_pwd_in_signin.getText().toString();
                    str_repeat_pwd = et_repeat_pwd_in_signin.getText().toString();
                    if (str_name.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.enter_your_name_txt));
                        et_name_id_in_name_signin.requestFocus();
                    } else if (str_name.length() <= 3) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.enter_your_name_warning_txt));
                        et_name_id_in_name_signin.requestFocus();
                    } else if (str_email_id.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.enter_your_email_txt));
                        et_email_id_in_email_signin.requestFocus();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email_id).matches()) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.pls_enter_valid_email_address));
                        et_email_id_in_email_signin.requestFocus();
                    } else if (str_pwd.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.pls_enter_your_pwd));
                        et_pwd_in_signin.requestFocus();
                    } else if (str_pwd.length() <= 6) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.your_pwd_must_be_atleast_6_characters_txt));
                        et_pwd_in_signin.requestFocus();
                    } else if (str_repeat_pwd.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.pls_enter_your_pwd));
                        et_repeat_pwd_in_signin.requestFocus();
                    } else if (!(et_pwd_in_signin.getText().toString().equals(et_repeat_pwd_in_signin.getText().toString()))) {
                        Toast_Message.showToastMessage(Email_Sign_Up_Act.this, getResources().getString(R.string.pwd_not_matching_txt));
                        et_repeat_pwd_in_signin.requestFocus();
                    } else {
                        Get_Email_Sign_Up_Details();
                    }
                }
                break;
            case R.id.tv_forgot_pwd_in_email_signin:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    Intent intent = new Intent(Email_Sign_Up_Act.this, Forgot_Pwd_Act.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("login_type_fr_forgot_pwd", "SignUp");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
    }

    /*This method used for Sending signup values to api */
    private void Get_Email_Sign_Up_Details() {
        try {
            str_name = et_name_id_in_name_signin.getText().toString();
            str_email_id = et_email_id_in_email_signin.getText().toString();
            str_pwd = et_pwd_in_signin.getText().toString();

            Splash_Screen_Act.str_global_mail_id = str_email_id;
//            Log.e("Email_str_global_mail_id", Splash_Screen_Act.str_global_mail_id);

            str_firstname = str_email_id.substring(0, 5);
            str_lastname = str_email_id.substring(5, 8);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", str_source_detail);
            jsonObject.put("first_name", str_name);
            jsonObject.put("last_name", str_lastname);
            jsonObject.put("email", str_email_id);
            jsonObject.put("username", str_firstname);
            jsonObject.put("password", str_pwd);
            jsonObject.put("is_termed", String.valueOf(str_api_checkbox_selection_value));
//            Log.e("email_json_value", jsonObject.toString());

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);
//            Log.e("email_signup_json", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.EMAIL_SIGNIN_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        str_status = response.body().status;
                        str_message = response.body().message;
//                        Log.e("str_message", str_message);
                        if (str_status.equalsIgnoreCase("success")) {
                            pd.dismiss();
                            str_source_detail = response.body().datum.source_detail;
                            str_firstname = response.body().datum.first_name;
                            str_lastname = response.body().datum.last_name;
                            str_email_id = response.body().datum.email;
                            str_username = response.body().datum.username;
                            str_referral_code = response.body().datum.referral_code;
                            SessionSave.SaveSession("Referral_Code_Value", str_referral_code, Email_Sign_Up_Act.this);
//                            Log.e("str_source_detail", str_source_detail);
//                            Log.e("str_first_name", str_firstname);
//                            Log.e("str_last_name", str_lastname);
//                            Log.e("str_email", str_email_id);
//                            Log.e("str_username", str_username);
//                            Log.e("str_pwd", str_pwd);
                            /*if (rowIDExistEmail(str_email_id) && rowIDExistFirst_Name(str_firstname)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("SOURCEDETAILS", str_source_detail);
                                contentValues.put("EMAIL", str_email_id);
                                contentValues.put("FIRSTNAME", str_firstname);
                                contentValues.put("STATUS", 1);
                                contentValues.put("SIGNUPSTATUS", 1);
                                contentValues.put("BALANCE", 5000);
                                contentValues.put("PASSWORD", str_pwd);
//                               contentValues.put("USER_SELECTION_VALUE", "");
                                Log.e("CntValuesemailsignup", contentValues.toString());
                                db.insert("LOGINDETAILS", null, contentValues);
                                DBEXPORT();
                            }*/
                            Get_Insert_DB_Sign_Up_Values();
                            Intent intent = (new Intent(Email_Sign_Up_Act.this, Mobile_Num_Registration.class));
//                            Intent intent = (new Intent(Email_Sign_Up_Act.this, Navigation_Drawer_Act.class));
                            intent.putExtra("source_details", str_source_detail);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else if (str_status.equalsIgnoreCase("error")) {
                            pd.dismiss();
                            Toast_Message.showToastMessage(Email_Sign_Up_Act.this, str_message);
                        } else {
                            pd.dismiss();
                            Toast_Message.showToastMessage(Email_Sign_Up_Act.this, str_message);
                        }
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
    }

    private void Insert_Singup_Details() {
        if (rowIDExistEmail(str_email_id)) {
//        if (rowIDExistEmail(str_email_id) && rowIDExistFirst_Name(str_firstname)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("SOURCEDETAILS", str_source_detail);
            contentValues.put("EMAIL", str_email_id);
//            contentValues.put("FIRSTNAME", str_firstname);
            contentValues.put("STATUS", 1);
            contentValues.put("SIGNUPSTATUS", 1);
//            contentValues.put("BALANCE", 1000);
            contentValues.put("PASSWORD", str_pwd);
//            Log.e("CntValuesemailsignup", contentValues.toString());
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

    private boolean rowIDExistFirst_Name(String str_firstname) {
        String select = "select * from LOGINDETAILS ";
        Cursor cursor = db.rawQuery(select, null);

        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);
//                Log.e("Login_Details", var);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(str_firstname)) {
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
                String var = cursor.getString(3);
//                Log.e("Login_Details", var);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        InputMethodManager inputMethodManager = (InputMethodManager) Email_Sign_Up_Act.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
            snackbar.getView().setBackgroundResource(R.color.timer_bg_color);
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
}

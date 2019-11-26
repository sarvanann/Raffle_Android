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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.UserModel;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Registration.Forgot_Pwd_Act;
import com.spot_the_ballgame.Registration.Update_New_Password_Act;
import com.spot_the_ballgame.Toast_Message;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Email_Sign_In_Act extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    ConstraintLayout constraintLayout_password_in_signin;
    EditText et_email_id_in_signin, et_pwd_in_signin;
    Button btn_login, btn_show_hide_crnt_pwd;
    TextView tv_forgot_pwd_in_signin,
            tv_resend_code,
            tv_dont_receive_otp,
            tv_title_txt;
    ProgressDialog pd;
    Snackbar snackbar;
    String str_email_id,
            str_pwd,
            str_source_details,
            str_sign_up_status,
            str_email_password;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    boolean show;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_in);
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        getSupportActionBar().hide();
        pd = new ProgressDialog(Email_Sign_In_Act.this);
        tv_resend_code = findViewById(R.id.tv_resend_code);
        tv_dont_receive_otp = findViewById(R.id.tv_dont_receive_otp);
        tv_title_txt = findViewById(R.id.tv_title_txt);
        et_email_id_in_signin = findViewById(R.id.et_email_id_in_signin);
        et_pwd_in_signin = findViewById(R.id.et_pwd_in_signin);

        constraintLayout_password_in_signin = findViewById(R.id.constraintLayout_password_in_signin);
        tv_forgot_pwd_in_signin = findViewById(R.id.tv_forgot_pwd_in_signin);
        btn_login = findViewById(R.id.btn_login);
        btn_show_hide_crnt_pwd = findViewById(R.id.btn_show_hide_crnt_pwd);
        tv_forgot_pwd_in_signin.setOnClickListener(this);
        btn_login.setOnClickListener(this);


        et_pwd_in_signin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_pwd_in_signin.getText().toString().length() == 0)) {
//                    Toast.makeText(Email_Sign_In_Act.this, "zero", Toast.LENGTH_SHORT).show();
                    btn_show_hide_crnt_pwd.setVisibility(View.VISIBLE);
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                } else {
//                    Toast.makeText(Email_Sign_In_Act.this, "not_zero", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(Email_Sign_In_Act.this, "show", Toast.LENGTH_SHORT).show();
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_crnt_pwd.setText("Show");
                    et_pwd_in_signin.setTransformationMethod(new PasswordTransformationMethod());
                    et_pwd_in_signin.setSelection(et_pwd_in_signin.getText().toString().length());
//                    Toast.makeText(Email_Sign_In_Act.this, "hide", Toast.LENGTH_SHORT).show();
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                }
            }
        });
        String s1 = "email";
        String select = "select SIGNUPSTATUS ,PASSWORD,SOURCEDETAILS from LOGINDETAILS";
//        String select = "select SIGNUPSTATUS ,PASSWORD,SOURCEDETAILS from LOGINDETAILS where SOURCEDETAILS ='" + s1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        int n1 = cursor.getCount();
        if (n1 > 0) {
            if (cursor.moveToFirst()) {
                do {
                    str_sign_up_status = cursor.getString(0);
                    str_email_password = cursor.getString(1);
                    str_source_details = cursor.getString(2);
                    Log.e("inside_sign_status", str_sign_up_status);
                    Log.e("inside_source_details", str_source_details);
                } while (cursor.moveToNext());
            }
            cursor.close();
            if (str_sign_up_status.equals("4")) {
                Log.e("inside_str_email_password", str_email_password);
                tv_dont_receive_otp.setVisibility(View.VISIBLE);
                tv_resend_code.setVisibility(View.VISIBLE);
                tv_forgot_pwd_in_signin.setVisibility(View.GONE);
                tv_title_txt.setText(R.string.forgot_pwd_title_txt);
            } else {
                tv_forgot_pwd_in_signin.setVisibility(View.VISIBLE);
                tv_dont_receive_otp.setVisibility(View.GONE);
                tv_resend_code.setVisibility(View.GONE);
                tv_title_txt.setText(R.string.e_mail_sign_in_txt);
            }
        } else {
            tv_forgot_pwd_in_signin.setVisibility(View.VISIBLE);
            tv_dont_receive_otp.setVisibility(View.GONE);
            tv_resend_code.setVisibility(View.GONE);
            tv_title_txt.setText(R.string.e_mail_sign_in_txt);
        }


        tv_resend_code.setOnClickListener(this);
//        Log.e("str_sign_up_status", str_sign_up_status);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend_code:
                str_email_id = et_email_id_in_signin.getText().toString();
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    if (str_email_id.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, getResources().getString(R.string.enter_email_for_getting_pwd));
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email_id).matches()) {
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, getResources().getString(R.string.pls_enter_valid_email_address));
                        et_email_id_in_signin.requestFocus();
                    } else {
                        Get_Resend_Code_Details();
                    }
                }
                break;
            case R.id.btn_login:
                et_pwd_in_signin.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    str_email_id = et_email_id_in_signin.getText().toString();
                    str_pwd = et_pwd_in_signin.getText().toString();
                    if (str_email_id.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, getResources().getString(R.string.enter_your_email_txt));
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email_id).matches()) {
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, getResources().getString(R.string.pls_enter_valid_email_address));
                        et_email_id_in_signin.requestFocus();
                    } else if (str_pwd.isEmpty()) {
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, getResources().getString(R.string.pls_enter_your_pwd));
                    } else if (str_pwd.length() <= 6) {
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, getResources().getString(R.string.pls_enter_valid_pwd));
                        et_pwd_in_signin.requestFocus();
                    } else {
                        Get_Email_SignIn_Details();
                    }
                }
                break;
            case R.id.tv_forgot_pwd_in_signin:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    Intent intent = new Intent(Email_Sign_In_Act.this, Forgot_Pwd_Act.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("login_type_fr_forgot_pwd", "SignUp");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
    }

    /*This method is used for getting resend code details*/
    private void Get_Resend_Code_Details() {
        try {
            str_email_id = et_email_id_in_signin.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email_id);
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.GET_RESEND_PWD_CALL("application/json", jsonObject.toString());
            Log.e("resend_json_value", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        assert response.body() != null;
                        String str_msg = response.body().message;
                        Toast_Message.showToastMessage(Email_Sign_In_Act.this, str_msg);
                        str_email_password = response.body().datum.password;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("SOURCEDETAILS", "email");
                        contentValues.put("EMAIL", str_email_id);
                        contentValues.put("STATUS", "1");
                        contentValues.put("PASSWORD", str_email_password);
                        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email_id + "'", null);
                        Log.e("resend_cnt_values", contentValues.toString());
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

    /*This method is used for getting email signin details*/
    private void Get_Email_SignIn_Details() {
        try {
            str_email_id = et_email_id_in_signin.getText().toString();
            str_pwd = et_pwd_in_signin.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", "email");
            jsonObject.put("email", str_email_id);
            jsonObject.put("password", str_pwd);
            APIInterface apiInterface = Factory.getClient();

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            Log.e("Json_value", jsonObject.toString());
            Call<UserModel> call = apiInterface.NORMAL_LOGIN_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        String str_f_name, str_l_name, str_email, str_phone_no, str_username, str_image, str_walet, str_money, str_active, str_verified, str_code, str_message;
                        str_code = response.body().code;
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
                            String select = "Select SIGNUPSTATUS FROM LOGINDETAILS";
                            Cursor cursor = db.rawQuery(select, null);
                            int int_cursor_count = cursor.getCount();
                            Log.e("int_cursor_count_signin", "" + int_cursor_count);
                            int n1 = 0;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("STATUS", "1");
                            contentValues.put("SIGNUPSTATUS", "3");
                            db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                            DBEXPORT();
                            Log.e("email_signin_cntn_values", contentValues.toString());
                            if (str_sign_up_status.equals("4")) {
                                Intent intent = new Intent(Email_Sign_In_Act.this, Update_New_Password_Act.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else {
                                Intent intent = new Intent(Email_Sign_In_Act.this, Navigation_Drawer_Act.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }

//                            Log.e("str_f_name", str_f_name);
//                            Log.e("str_l_name", str_l_name);
//                            Log.e("str_email", str_email);
//                            Log.e("str_phone_no", str_phone_no);
//                            Log.e("str_username", str_username);
//                            Log.e("str_walet", str_walet);
//                            Log.e("str_money", str_money);
//                            Log.e("str_active", str_active);
//                            Log.e("str_verified", str_verified);
//                            Toast_Message.showToastMessage(Email_Sign_In_Act.this, str_message);
                        } else if (str_code.equalsIgnoreCase("error")) {
                            pd.dismiss();
                            Toast_Message.showToastMessage(Email_Sign_In_Act.this, str_message);
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
        InputMethodManager inputMethodManager = (InputMethodManager) Email_Sign_In_Act.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
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
}

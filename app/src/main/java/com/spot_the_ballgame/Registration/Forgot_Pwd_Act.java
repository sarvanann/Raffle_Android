package com.spot_the_ballgame.Registration;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.UserModel;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Registration.SignIn.Email_Sign_In_Act;
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

public class Forgot_Pwd_Act extends AppCompatActivity implements View.OnClickListener {
    Button tv_send_verification_code_in_forgot_pwd;
    EditText et_email_id_in_forgot_pwd;
    String str_et_email_id, str_inent_value;
    String str_id, str_source_detail, str_firstname, str_lastname, str_username, str_email_password, str_code, str_message, str_phone_no, str_token, str_active, str_verified, str_image;

    public static String static_str_email_id;
    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    ProgressDialog pd;
    SQLiteDatabase db;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__pwd_);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        tv_send_verification_code_in_forgot_pwd = findViewById(R.id.tv_send_verification_code_in_forgot_pwd);
        et_email_id_in_forgot_pwd = findViewById(R.id.et_email_id_in_forgot_pwd);
        tv_send_verification_code_in_forgot_pwd.setOnClickListener(this);
        pd = new ProgressDialog(Forgot_Pwd_Act.this);
//        Bundle bundle = getIntent().getExtras();
//        str_inent_value = bundle.getString("login_type_fr_forgot_pwd");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_verification_code_in_forgot_pwd:
                et_email_id_in_forgot_pwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    str_et_email_id = et_email_id_in_forgot_pwd.getText().toString();
                    if (str_et_email_id.isEmpty()) {
                        Toast_Message.showToastMessage(Forgot_Pwd_Act.this, getResources().getString(R.string.enter_your_email_txt));
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(str_et_email_id).matches()) {
                        Toast_Message.showToastMessage(Forgot_Pwd_Act.this, getResources().getString(R.string.pls_enter_valid_email_address));
                        et_email_id_in_forgot_pwd.requestFocus();
                    } else {
                        GetForgot_Pwd_Details();
                    }
                }
               /* Intent intent = new Intent(Forgot_Pwd_Act.this, Mobile_Num_Registration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
                break;
        }

    }

    private void GetForgot_Pwd_Details() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", et_email_id_in_forgot_pwd.getText().toString());

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.FORGOT_PWD_RESPONSES_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        str_code = response.body().code;
                        str_message = response.body().message;
                        Log.e("str_code", str_code);
                        Log.e("str_message", str_message);
                        if (str_code.equalsIgnoreCase("success")) {
                            pd.dismiss();
                            Toast_Message.showToastMessage(Forgot_Pwd_Act.this, str_message);
                            str_id = response.body().datum.id;
                            str_source_detail = response.body().datum.source_detail;
                            str_firstname = response.body().datum.first_name;
                            str_lastname = response.body().datum.last_name;
                            static_str_email_id = response.body().datum.email;
                            str_phone_no = response.body().datum.phoneno;
                            str_image = response.body().datum.image;
                            str_token = response.body().datum.token;
                            str_username = response.body().datum.username;
                            str_email_password = response.body().datum.password;
                            str_active = response.body().datum.active;
                            str_verified = response.body().datum.verified;

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("SOURCEDETAILS", "email");
                            contentValues.put("EMAIL", static_str_email_id);
                            contentValues.put("STATUS", "1");
                            contentValues.put("SIGNUPSTATUS", "4");
                            contentValues.put("PASSWORD", str_email_password);
                            db.update("LOGINDETAILS", contentValues, "EMAIL='" + static_str_email_id + "'", null);
                            DBEXPORT();
                            Log.e("frgt_pwd_cnt_values", contentValues.toString());
//                            Toast_Message.showToastMessage(Forgot_Pwd_Act.this, str_message);
                            Intent intent = new Intent(Forgot_Pwd_Act.this, Email_Sign_In_Act.class);
                            intent.putExtra("str_email_password", str_email_password);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            Log.e("str_id", str_id);
                            Log.e("str_source_detail", str_source_detail);
                            Log.e("str_firstname", str_firstname);
                            Log.e("str_lastname", str_lastname);
                            Log.e("static_str_email_id", static_str_email_id);
                            Log.e("str_phone_no", str_phone_no);
                            Log.e("str_image", String.valueOf(str_image));

                            Log.e("str_token", str_token);
                            Log.e("str_username", str_username);
                            Log.e("str_email_password", str_email_password);
                            Log.e("str_active", str_active);
                            Log.e("str_verified", str_verified);
                            et_email_id_in_forgot_pwd.setText("");
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            pd.dismiss();
                            Toast_Message.showToastMessage(Forgot_Pwd_Act.this, str_message);
                        }
                        if (str_code.equalsIgnoreCase("error")) {
                            pd.dismiss();
                            Toast_Message.showToastMessage(Forgot_Pwd_Act.this, str_message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("Failure_Msg", t.getMessage());
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
        InputMethodManager inputMethodManager = (InputMethodManager) Forgot_Pwd_Act.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
}

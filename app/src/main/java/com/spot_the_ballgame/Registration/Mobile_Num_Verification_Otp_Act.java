package com.spot_the_ballgame.Registration;

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
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;
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

public class Mobile_Num_Verification_Otp_Act extends AppCompatActivity implements View.OnClickListener {
    TextView tv_pincode_national_in_otp, tv_edit_txt;
    EditText et_mobile_number_in_otp, et_otp_01, et_otp_02, et_otp_03, et_otp_04;
    Button btn_send_code_in_otp, btn_resend_code_in_otp;
    String str_phone_num, str_token_value, str_email_id, str_code, str_message, str_firstname, str_id, str_source_detail, str_lastname, str_app_id, str_image, str_walet, str_money, str_username, str_pwd, str_active, str_verified;
    SQLiteDatabase db;
    int int_phone_num, int_token_value;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    ProgressDialog pd;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__num__verification__otp_);
        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        pd = new ProgressDialog(Mobile_Num_Verification_Otp_Act.this);
        String select = "Select SOURCEDETAILS,FIRSTNAME,EMAIL,PHONENO, TOKEN FROM LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_source_detail = cursor.getString(0);
                str_firstname = cursor.getString(1);
                str_email_id = cursor.getString(2);
                str_phone_num = cursor.getString(3);
                str_token_value = cursor.getString(4);

            } while (cursor.moveToNext());
        }
        cursor.close();
//        Log.e("str_source_detail", String.valueOf(str_source_detail));
//        Log.e("int_phone_num", String.valueOf(str_phone_num));
//        Log.e("int_token_value", String.valueOf(str_token_value));
//        Log.e("str_firstname", String.valueOf(str_firstname));
//        Log.e("str_email_id", String.valueOf(str_email_id));

        tv_pincode_national_in_otp = findViewById(R.id.tv_pincode_national_in_otp);
        et_mobile_number_in_otp = findViewById(R.id.et_mobile_number_in_otp);
        et_otp_01 = findViewById(R.id.et_otp_01);
        et_otp_02 = findViewById(R.id.et_otp_02);
        et_otp_03 = findViewById(R.id.et_otp_03);
        et_otp_04 = findViewById(R.id.et_otp_04);
        tv_edit_txt = findViewById(R.id.tv_edit_txt);
        btn_send_code_in_otp = findViewById(R.id.btn_send_code_in_otp);
        btn_resend_code_in_otp = findViewById(R.id.btn_resend_code_in_otp);

//        et_otp_01.setText(str_token_value.substring(0, 1));
//        et_otp_02.setText(str_token_value.substring(1, 2));
//        et_otp_03.setText(str_token_value.substring(2, 3));
//        et_otp_04.setText(str_token_value.substring(3, 4));
        et_mobile_number_in_otp.setFocusable(false);

        et_otp_01.setFocusable(false);
        et_otp_02.setFocusable(false);
        et_otp_03.setFocusable(false);
        et_otp_04.setFocusable(false);

        et_mobile_number_in_otp.setText(str_phone_num);
        et_mobile_number_in_otp.setSelection(et_mobile_number_in_otp.getText().length());

        et_otp_01.setSelection(et_otp_01.getText().length());
        et_otp_02.setSelection(et_otp_02.getText().length());
        et_otp_03.setSelection(et_otp_03.getText().length());
        et_otp_04.setSelection(et_otp_04.getText().length());

        btn_send_code_in_otp.setOnClickListener(this);
        btn_resend_code_in_otp.setOnClickListener(this);
        tv_edit_txt.setOnClickListener(this);

        /*et_otp_01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_otp_01.getText().toString().length() == 0)) {
                    et_otp_02.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_otp_02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_otp_02.getText().toString().length() == 0)) {
                    et_otp_03.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_otp_03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_otp_03.getText().toString().length() == 0)) {
                    et_otp_04.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_otp_04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_otp_04.getText().toString().length() == 0)) {
                    et_otp_04.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private void Get_Mobile_Num_Verification_Respone_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email_id);
            jsonObject.put("phoneno", str_phone_num);
            jsonObject.put("token", str_token_value);

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.MOBILE_NUM_VERIFY_RESPONES_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_code = response.body().status;
                            str_message = response.body().message;
                            if (str_code.equalsIgnoreCase("success")) {
                                pd.dismiss();
                                if (!(rowIDExistEmail(str_email_id)) && !(rowIDExistApp_ID(str_firstname))) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("USERNAME", str_firstname);
                                    contentValues.put("EMAIL", str_email_id);
                                    contentValues.put("SIGNUPSTATUS", 3);
//                                    Log.e("Content_Values_otp", contentValues.toString());
                                    db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email_id + "'", null);
                                    DBEXPORT();
                                }
                                Intent intent = new Intent(Mobile_Num_Verification_Otp_Act.this, Navigation_Drawer_Act.class);
                                SessionSave.SaveSession("carousel_value", "1", Mobile_Num_Verification_Otp_Act.this);
//                            Intent intent = new Intent(Mobile_Num_Verification_Otp_Act.this, Home_Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, str_message);
                        /*} else {
                            Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, str_message);
                        }*/
                            } else if (str_code.equalsIgnoreCase("error")) {
                                pd.dismiss();
                                Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, str_message);
                            }
                        }
                    } else if (response.code() == 401) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, response.message());
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, response.message());
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

    private boolean rowIDExistApp_ID(String str_firstname) {
        String select = "select * from LOGINDETAILS where STATUS ='" + 1 + "'";
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
        String select = "select * from LOGINDETAILS where STATUS ='" + 1 + "'";
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code_in_otp:
                et_mobile_number_in_otp.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    String str_mobile_num = et_mobile_number_in_otp.getText().toString();
                    String str_otp_01 = et_otp_01.getText().toString();
                    String str_otp_02 = et_otp_02.getText().toString();
                    String str_otp_03 = et_otp_03.getText().toString();
                    String str_otp_04 = et_otp_04.getText().toString();
                    if (str_mobile_num.isEmpty()) {
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, getResources().getString(R.string.pls_enter_your_mobile_num_txt));
                    } else if (!(str_mobile_num.length() == 10)) {
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, getResources().getString(R.string.pls_enter_valid_mobile_num_txt));
                    } else if (str_otp_01.isEmpty() && str_otp_02.isEmpty() && str_otp_03.isEmpty() && str_otp_04.isEmpty()) {
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, getResources().getString(R.string.otp_value_not_empty_txt));
                    } else {
                        Get_Mobile_Num_Verification_Respone_Details();
                    }
                }
                break;
            case R.id.btn_resend_code_in_otp:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    et_otp_01.setText("");
                    et_otp_02.setText("");
                    et_otp_03.setText("");
                    et_otp_04.setText("");
                    Get_Resend_Otp_Details();
                }
                break;
            case R.id.tv_edit_txt:
                Intent intent = new Intent(Mobile_Num_Verification_Otp_Act.this, Mobile_Num_Registration.class);
                intent.putExtra("mobile_number", et_mobile_number_in_otp.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private void Get_Resend_Otp_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email_id);
            jsonObject.put("phoneno", str_phone_num);
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.RESEND_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_code = response.body().status;
                            str_message = response.body().message;
                            if (str_code.equalsIgnoreCase("success")) {
                                pd.dismiss();
                                str_id = response.body().datum.id;
                                str_source_detail = response.body().datum.source_detail;
                                str_app_id = response.body().datum.app_id;
                                str_firstname = response.body().datum.first_name;
                                str_lastname = response.body().datum.last_name;
                                str_email_id = response.body().datum.email;
                                str_phone_num = response.body().datum.phoneno;
                                str_image = response.body().datum.image;
                                str_walet = response.body().datum.walet;
                                str_money = response.body().datum.money;
                                str_token_value = response.body().datum.token;
                                str_username = response.body().datum.username;
                                str_pwd = response.body().datum.password;
                                str_active = response.body().datum.active;
                                str_verified = response.body().datum.verified;

//                                Log.e("str_token_value", str_token_value);

                                et_otp_01.setText(str_token_value.substring(0, 1));
                                et_otp_02.setText(str_token_value.substring(1, 2));
                                et_otp_03.setText(str_token_value.substring(2, 3));
                                et_otp_04.setText(str_token_value.substring(3, 4));
//                        Toast.makeText(Mobile_Num_Verification_Otp_Act.this, "" + str_message, Toast.LENGTH_SHORT).show();
                                Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, str_message);
                            } else {
//                        Toast.makeText(Mobile_Num_Verification_Otp_Act.this, "" + str_message, Toast.LENGTH_SHORT).show();
                                Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, str_message);
                            }
                        } else {
                            pd.dismiss();
//                        Toast.makeText(Mobile_Num_Verification_Otp_Act.this, "" + str_message, Toast.LENGTH_SHORT).show();
                            Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, str_message);
                        }
                    } else if (response.code() == 401) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, response.message());
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(Mobile_Num_Verification_Otp_Act.this, response.message());
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
        InputMethodManager inputMethodManager = (InputMethodManager) Mobile_Num_Verification_Otp_Act.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

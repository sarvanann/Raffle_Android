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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Mobile_Num_Registration extends AppCompatActivity implements View.OnClickListener {
    TextView tv_pincode_national;
    EditText et_mobile_number;
    Button btn_send_code;
    String str_id, str_source_detail, str_app_id, str_first_name, str_last_name,
            str_email, str_phoneno, str_image, str_walet, str_money, str_token,
            str_username, str_password, str_active, str_verified, str_code, str_message;
    String str_intent_source_details;
    SQLiteDatabase db;
    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    ProgressDialog pd;
    String str_intent_mobile_number, str_intent_mobile_number_new, str_status, str_phoneno_db;
    String str_auth_token;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile__num__verification);


        getSupportActionBar().hide();
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(SOURCEDETAILS varchar,FIRSTNAME varchar,LASTNAME varchar,EMAIL varchar,PHONENO int,APPID varchar,WALET double,TOKEN int,STATUS int,PASSWORD varchar,IMAGE varchar,BALANCE int,USERNAME varchar,ACTIVE int,VERIFIED int)");
        pd = new ProgressDialog(Mobile_Num_Registration.this);
        tv_pincode_national = findViewById(R.id.tv_pincode_national_in_mobile_num_verification);
        et_mobile_number = findViewById(R.id.et_mobile_number_in_mobile_num_verification);
        btn_send_code = findViewById(R.id.btn_send_code_in_mobile_num_verification);
        btn_send_code.setOnClickListener(this);


        if (et_mobile_number.getText().toString().isEmpty()) {
            et_mobile_number.setText(str_intent_mobile_number_new);
            et_mobile_number.setSelection(et_mobile_number.getText().toString().length());
        }
        String select = "Select SOURCEDETAILS ,EMAIL,SIGNUPSTATUS ,PHONENO FROM LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_intent_source_details = cursor.getString(0);
                str_email = cursor.getString(1);
                str_status = String.valueOf(cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("str_source_details", String.valueOf(str_intent_source_details));
        if (str_status.equalsIgnoreCase("2")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                str_phoneno_db = null;
            } else {
                str_phoneno_db = bundle.getString("mobile_number");
            }
            et_mobile_number.setText(str_phoneno_db);
            et_mobile_number.setSelection(et_mobile_number.getText().toString().length());
        } else {
            et_mobile_number.setText("");
        }
        str_auth_token = SessionSave.getSession("Token_value", Mobile_Num_Registration.this);

        Log.e("str_auth_token_reg", str_auth_token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code_in_mobile_num_verification:
                et_mobile_number.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    if (et_mobile_number.getText().toString().isEmpty()) {
                        Toast_Message.showToastMessage(Mobile_Num_Registration.this, getResources().getString(R.string.pls_enter_your_mobile_num_txt));
                    } else if (!(et_mobile_number.getText().toString().length() == 10)) {
                        Toast_Message.showToastMessage(Mobile_Num_Registration.this, getResources().getString(R.string.pls_enter_valid_mobile_num_txt));
                    } else {
                        str_intent_mobile_number_new = et_mobile_number.getText().toString();
                        Get_Mobile_Num_Details();

                    }
                }

        }
    }

    private void Get_Mobile_Num_Details() {
        try {
            String str_mobile_num = et_mobile_number.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", str_intent_source_details);
            jsonObject.put("email", str_email);
            jsonObject.put("phoneno", str_mobile_num);

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Log.e("Json_Value_mob_reg", jsonObject.toString());
            Call<UserModel> call = apiInterface.MOBILE_NUM_REGISTER_RESPONSE_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code()==200) {
                        if (response.isSuccessful()) {
                            str_code = response.body().status;
                            str_message = response.body().message;
                            if (str_code.equalsIgnoreCase("success")) {
                                pd.dismiss();
                                str_source_detail = response.body().datum.source_detail;
                                str_app_id = response.body().datum.app_id;
                                str_first_name = response.body().datum.first_name;
                                str_last_name = response.body().datum.last_name;
                                str_email = response.body().datum.email;
                                str_phoneno = response.body().datum.phoneno;
                                str_image = response.body().datum.image;
                                str_walet = response.body().datum.walet;
                                str_money = response.body().datum.money;
                                str_token = response.body().datum.token;
                                str_username = response.body().datum.username;
                                str_password = response.body().datum.password;
                                str_active = response.body().datum.active;
                                str_verified = response.body().datum.verified;
                                Log.e("emailll", str_email);


                                ContentValues contentValues = new ContentValues();
                                contentValues.put("PHONENO", str_phoneno);
                                contentValues.put("TOKEN", str_token);
                                contentValues.put("SIGNUPSTATUS", 2);
                                Log.e("Content_Values_mob_reg", contentValues.toString());
                                db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                                DBEXPORT();
                                Intent intent = new Intent(Mobile_Num_Registration.this, Navigation_Drawer_Act.class);
//                            Intent intent = new Intent(Mobile_Num_Registration.this, Mobile_Num_Verification_Otp_Act.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                Toast.makeText(Mobile_Num_Registration.this, "Message" + response.body().message, Toast.LENGTH_SHORT).show();


                            /*if ((!(rowIDExistEmail(str_email)))) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("PHONENO", str_phoneno);
                                contentValues.put("TOKEN", str_token);
                                contentValues.put("SIGNUPSTATUS", 2);
                                Log.e("Content_Values_mob_reg", contentValues.toString());
                                db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                                DBEXPORT();
                                Intent intent = new Intent(Mobile_Num_Registration.this, Mobile_Num_Verification_Otp_Act.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                Toast.makeText(Mobile_Num_Registration.this, "Message" + response.body().message, Toast.LENGTH_SHORT).show();
                            }*/
                            } else {
                                pd.dismiss();
                                Toast_Message.showToastMessage(Mobile_Num_Registration.this, str_message);
                            }
                        }
                    }else if (response.code() == 401) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(Mobile_Num_Registration.this, response.message());
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast_Message.showToastMessage(Mobile_Num_Registration.this, response.message());
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
                String var = cursor.getString(5);
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
        String select = "select * from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(3);
                Log.e("Login_Details_email", var);
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
        InputMethodManager inputMethodManager = (InputMethodManager) Mobile_Num_Registration.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

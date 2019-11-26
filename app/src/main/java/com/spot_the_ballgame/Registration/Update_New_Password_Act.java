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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
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

public class Update_New_Password_Act extends AppCompatActivity implements View.OnClickListener {
    EditText et_old_pwd, et_new_pwd, et_repeat_pwd;
    Button btn_update;

    String str_email_pwd, str_pwd, str_repeat_pwd, str_source_details, str_email;
    SQLiteDatabase db;

    Button btn_show_hide_old_pwd, btn_show_hide_new_pwd, btn_show_hide_repeat_pwd;
    ProgressDialog pd;
    String str_email_password;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_new_password_act_layout);
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        getSupportActionBar().hide();
        pd = new ProgressDialog(Update_New_Password_Act.this);
        et_old_pwd = findViewById(R.id.et_old_pwd);
        et_new_pwd = findViewById(R.id.et_new_pwd);
        et_repeat_pwd = findViewById(R.id.et_repeat_pwd);
        btn_update = findViewById(R.id.btn_update);

        btn_show_hide_old_pwd = findViewById(R.id.btn_show_hide_old_pwd);
        btn_show_hide_new_pwd = findViewById(R.id.btn_show_hide_new_pwd);
        btn_show_hide_repeat_pwd = findViewById(R.id.btn_show_hide_repeat_pwd);


        btn_update.setOnClickListener(this);
        btn_show_hide_old_pwd.setOnClickListener(this);
        btn_show_hide_new_pwd.setOnClickListener(this);
        btn_show_hide_repeat_pwd.setOnClickListener(this);

        String select = "select SOURCEDETAILS,EMAIL,PASSWORD from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_source_details = cursor.getString(0);
                str_email = cursor.getString(1);
                str_email_password = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("update_str_source_details", str_source_details);
        Log.e("update_str_email", str_email);
        Log.e("update_str_email_pwd", str_email_password);
        ContentValues contentValues = new ContentValues();
        contentValues.put("SIGNUPSTATUS", "5");
        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);


        et_old_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_old_pwd.getText().toString().length() == 0)) {
                    btn_show_hide_old_pwd.setVisibility(View.VISIBLE);
                    btn_show_hide_old_pwd.setBackgroundResource((R.drawable.eye_open));
                } else {
                    btn_show_hide_old_pwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_new_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_new_pwd.getText().toString().length() == 0)) {
                    btn_show_hide_new_pwd.setVisibility(View.VISIBLE);
                    btn_show_hide_new_pwd.setBackgroundResource((R.drawable.eye_open));
                } else {
                    btn_show_hide_new_pwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_repeat_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_repeat_pwd.getText().toString().length() == 0)) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_hide_old_pwd:
                if (btn_show_hide_old_pwd.getText().toString().equals("Show")) {
                    btn_show_hide_old_pwd.setText("Hide");
                    et_old_pwd.setTransformationMethod(null);
                    et_old_pwd.setSelection(et_old_pwd.getText().toString().length());
                    btn_show_hide_old_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_old_pwd.setText("Show");
                    et_old_pwd.setTransformationMethod(new PasswordTransformationMethod());
                    et_old_pwd.setSelection(et_old_pwd.getText().toString().length());
                    btn_show_hide_old_pwd.setBackgroundResource((R.drawable.eye_open));
                }
                break;
            case R.id.btn_show_hide_new_pwd:
                if (btn_show_hide_new_pwd.getText().toString().equals("Show")) {
                    btn_show_hide_new_pwd.setText("Hide");
                    et_new_pwd.setTransformationMethod(null);
                    et_new_pwd.setSelection(et_new_pwd.getText().toString().length());
                    btn_show_hide_new_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_new_pwd.setText("Show");
                    et_new_pwd.setTransformationMethod(new PasswordTransformationMethod());
                    et_new_pwd.setSelection(et_new_pwd.getText().toString().length());
                    btn_show_hide_new_pwd.setBackgroundResource((R.drawable.eye_open));
                }
                break;
            case R.id.btn_show_hide_repeat_pwd:
                if (btn_show_hide_repeat_pwd.getText().toString().equals("Show")) {
                    btn_show_hide_repeat_pwd.setText("Hide");
                    et_repeat_pwd.setTransformationMethod(null);
                    et_repeat_pwd.setSelection(et_repeat_pwd.getText().toString().length());
                    btn_show_hide_repeat_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_repeat_pwd.setText("Show");
                    et_repeat_pwd.setTransformationMethod(new PasswordTransformationMethod());
                    et_repeat_pwd.setSelection(et_repeat_pwd.getText().toString().length());
                    btn_show_hide_repeat_pwd.setBackgroundResource((R.drawable.eye_open));
                }
                break;
            case R.id.btn_update:
                et_repeat_pwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                str_email_pwd = et_old_pwd.getText().toString();
                str_pwd = et_new_pwd.getText().toString();
                str_repeat_pwd = et_repeat_pwd.getText().toString();
                /*if (str_pwd.isEmpty()) {
                    Toast_Message.showToastMessage(Update_New_Password_Act.this, getResources().getString(R.string.pls_enter_your_pwd));
                    et_old_pwd.requestFocus();
                } else if (str_pwd.isEmpty()) {
                    Toast_Message.showToastMessage(Update_New_Password_Act.this, getResources().getString(R.string.pwd_not_matching_txt));
                    et_old_pwd.requestFocus();
                }*/
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    if (str_pwd.isEmpty()) {
                        Toast_Message.showToastMessage(Update_New_Password_Act.this, getResources().getString(R.string.pls_enter_your_pwd));
                        et_new_pwd.requestFocus();
                    } else if (str_pwd.length() <= 6) {
                        Toast_Message.showToastMessage(Update_New_Password_Act.this, getResources().getString(R.string.your_pwd_must_be_atleast_6_characters_txt));
                        et_new_pwd.requestFocus();
                    } else if (str_repeat_pwd.isEmpty()) {
                        Toast_Message.showToastMessage(Update_New_Password_Act.this, getResources().getString(R.string.pls_enter_your_pwd));
                        et_repeat_pwd.requestFocus();
                    } else if (!(et_new_pwd.getText().toString().equals(et_repeat_pwd.getText().toString()))) {
                        Toast_Message.showToastMessage(Update_New_Password_Act.this, getResources().getString(R.string.pwd_not_matching_txt));
                        et_repeat_pwd.requestFocus();
                    } else {
                        Get_Update_Password_Details();
                    }
                }
                break;
        }
    }

    private void Get_Update_Password_Details() {
        try {
            str_pwd = et_new_pwd.getText().toString();
            str_repeat_pwd = et_repeat_pwd.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", "email");
            jsonObject.put("email", str_email);
            jsonObject.put("password", str_email_password);
            jsonObject.put("new_password", str_repeat_pwd);

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_NEW_PWD_UPDATE_CALL("application/json", jsonObject.toString());
            Log.e("updatepwd_json_value", jsonObject.toString());
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.isSuccessful()) {
                        pd.dismiss();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("SOURCEDETAILS", "email");
                        contentValues.put("EMAIL", str_email);
                        contentValues.put("STATUS", "1");
                        contentValues.put("SIGNUPSTATUS", "6");
                        contentValues.put("PASSWORD", str_repeat_pwd);
                        db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                        DBEXPORT();
                        Log.e("update_pwd_cntn_values", contentValues.toString());
                        Intent intent = new Intent(Update_New_Password_Act.this, Navigation_Drawer_Act.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

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

    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
    }
}

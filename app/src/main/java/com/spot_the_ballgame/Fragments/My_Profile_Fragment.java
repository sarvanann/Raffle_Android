package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class My_Profile_Fragment extends Fragment implements View.OnClickListener {

    private EditText et_name_profile, et_email_id_profile, et_mobile_num_profile;
    private Button btn_update;
    private TextView tv_change_pwd_dialog_link;
    private Dialog dialog;
    private AdView mAdView;
    private SQLiteDatabase db;

    private String str_first_name, str_source_details, str_phone_no, str_email, str_password, str_repeat_pwd, str_sign_up_status;
    private String str_username, str_phone_num;
    /*This is  for update password alert dialog*/
    private EditText et_pwd_in_change_pwd_dialog, et_repeat_pwd_in_change_pwd_dialog;
    private Button btn_update_change_pwd_dialog, btn_btn_show_hide_crnt_pwd, btn_show_hide_repeat;
    private ProgressDialog pd;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    private Snackbar snackbar;
    private String str_auth_token, str_code, str_message;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);
        db = Objects.requireNonNull(getActivity()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        pd = new ProgressDialog(getActivity());

        et_name_profile = view.findViewById(R.id.et_name_profile);
        et_email_id_profile = view.findViewById(R.id.et_email_id_profile);
        et_mobile_num_profile = view.findViewById(R.id.et_mobile_num_profile);
        btn_update = view.findViewById(R.id.btn_update);
        tv_change_pwd_dialog_link = view.findViewById(R.id.tv_change_pwd_dialog_link);

        btn_update.setOnClickListener(this);
        tv_change_pwd_dialog_link.setOnClickListener(this);
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.profile_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        str_auth_token = SessionSave.getSession("Token_value", getActivity());
//        Log.e("authtoken_profile", str_auth_token);

        String select = "select FIRSTNAME,SOURCEDETAILS,PHONENO,EMAIL,PASSWORD,SIGNUPSTATUS from LOGINDETAILS  where STATUS='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
//                str_first_name = cursor.getString(0);
                str_source_details = cursor.getString(1);
                str_phone_no = cursor.getString(2);
                str_email = cursor.getString(3);
                str_password = cursor.getString(4);
                str_sign_up_status = cursor.getString(5);
            } while (cursor.moveToNext());
        }
        cursor.close();

//        Log.e("str_first_name_profile", str_first_name);
//        Log.e("source_details_profile", str_source_details);
//        Log.e("str_phone_no_profile", str_phone_no);
//        Log.e("str_email_profile", str_email);
//        Log.e("str_password_profile", str_password);
//        Log.e("sign_up_status_profile", str_sign_up_status);


        et_name_profile.setSelection(et_name_profile.getText().toString().length());
        if (str_source_details.equalsIgnoreCase("email")) {
            tv_change_pwd_dialog_link.setVisibility(View.VISIBLE);
        } else {
            tv_change_pwd_dialog_link.setVisibility(View.GONE);
        }
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            Get_User_Wallet_Details();
        }
        return view;
    }

    private void Get_User_Wallet_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
//            Log.e("wallet_json", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_WalletDetailsModelCall("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            if (response.isSuccessful()) {
                                str_username = response.body().data.get(0).username;
                                str_email = response.body().data.get(0).email;
                                str_phone_no = response.body().data.get(0).phoneno;
//                            Log.e("str_username", str_username);
//                            Log.e("str_email", str_email);
//                            Log.e("str_phone_no", str_phone_no);

                                et_name_profile.setText(str_username);
                                et_email_id_profile.setText(str_email);
                                et_mobile_num_profile.setText(str_phone_no);

                            }
                        } else if (response.code() == 401) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        } else if (response.code() == 500) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        }
                    } else {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Something went wrong try again :)");
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
//        Log.e("backStackCnt_profile", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            //Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

        /*  Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
       /* Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                break;
            case R.id.tv_change_pwd_dialog_link:
                if (!isNetworkAvaliable()) {
                    registerInternetCheckReceiver();
                } else {
                    dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.update_alert);
                    dialog.setCancelable(true);

                    et_pwd_in_change_pwd_dialog = dialog.findViewById(R.id.et_pwd_in_change_pwd_dialog);
                    btn_btn_show_hide_crnt_pwd = dialog.findViewById(R.id.btn_btn_show_hide_crnt_pwd);
                    btn_show_hide_repeat = dialog.findViewById(R.id.btn_show_hide_repeat);
                    et_repeat_pwd_in_change_pwd_dialog = dialog.findViewById(R.id.et_repeat_pwd_in_change_pwd_dialog);
                    btn_update_change_pwd_dialog = dialog.findViewById(R.id.btn_update_change_pwd_dialog);
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btn_update_change_pwd_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str_user_enter_pwd = et_pwd_in_change_pwd_dialog.getText().toString();
//                        Log.e("str_user_enter_pwd", str_user_enter_pwd);
                            String str_new_password = et_repeat_pwd_in_change_pwd_dialog.getText().toString();
//                        Log.e("str_new_pwd", str_new_password);
                            if (!isNetworkAvaliable()) {
                                registerInternetCheckReceiver();
                            } else {
                                if (str_user_enter_pwd.isEmpty()) {
                                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.pls_enter_your_pwd));
                                    et_pwd_in_change_pwd_dialog.requestFocus();
                                }/* else if (!str_user_enter_pwd.matches(str_password)) {
                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.pls_check_your_pwd));
                                et_pwd_in_change_pwd_dialog.requestFocus();
                            } */ else if (str_new_password.isEmpty()) {
                                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.pls_enter_your_pwd));
                                    et_repeat_pwd_in_change_pwd_dialog.requestFocus();
                                } else if (str_new_password.length() <= 6) {
                                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.your_pwd_must_be_atleast_6_characters_txt));
                                    et_repeat_pwd_in_change_pwd_dialog.requestFocus();
                                } else {
                                    Get_Update_Password_Details();
                                }
                            }
                        }
                    });

                    et_pwd_in_change_pwd_dialog.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!(et_pwd_in_change_pwd_dialog.getText().toString().length() == 0)) {
//                            Toast.makeText(getContext(), "zero", Toast.LENGTH_SHORT).show();
                                btn_btn_show_hide_crnt_pwd.setVisibility(View.VISIBLE);
                                btn_btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                            } else {
//                            Toast.makeText(getContext(), "not_zero", Toast.LENGTH_SHORT).show();
                                btn_btn_show_hide_crnt_pwd.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    btn_btn_show_hide_crnt_pwd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (btn_btn_show_hide_crnt_pwd.getText().toString().equals("Show")) {
                                btn_btn_show_hide_crnt_pwd.setText("Hide");
                                et_pwd_in_change_pwd_dialog.setTransformationMethod(null);
                                et_pwd_in_change_pwd_dialog.setSelection(et_pwd_in_change_pwd_dialog.getText().toString().length());
//                            Toast.makeText(getContext(), "show", Toast.LENGTH_SHORT).show();
                                btn_btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_hide));
                            } else {
                                btn_btn_show_hide_crnt_pwd.setText("Show");
                                et_pwd_in_change_pwd_dialog.setTransformationMethod(new PasswordTransformationMethod());
                                et_pwd_in_change_pwd_dialog.setSelection(et_pwd_in_change_pwd_dialog.getText().toString().length());
//                            Toast.makeText(getContext(), "hide", Toast.LENGTH_SHORT).show();
                                btn_btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                            }
                        }
                    });


                    et_repeat_pwd_in_change_pwd_dialog.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!(et_repeat_pwd_in_change_pwd_dialog.getText().toString().length() == 0)) {
//                            Toast.makeText(getContext(), "zero", Toast.LENGTH_SHORT).show();
                                btn_show_hide_repeat.setVisibility(View.VISIBLE);
                                btn_show_hide_repeat.setBackgroundResource((R.drawable.eye_open));
                            } else {
//                            Toast.makeText(getContext(), "not_zero", Toast.LENGTH_SHORT).show();
                                btn_show_hide_repeat.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    btn_show_hide_repeat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (btn_show_hide_repeat.getText().toString().equals("Show")) {
                                btn_show_hide_repeat.setText("Hide");
                                et_repeat_pwd_in_change_pwd_dialog.setTransformationMethod(null);
                                et_repeat_pwd_in_change_pwd_dialog.setSelection(et_repeat_pwd_in_change_pwd_dialog.getText().toString().length());
//                            Toast.makeText(getContext(), "show", Toast.LENGTH_SHORT).show();
                                btn_show_hide_repeat.setBackgroundResource((R.drawable.eye_hide));
                            } else {
                                btn_show_hide_repeat.setText("Show");
                                et_repeat_pwd_in_change_pwd_dialog.setTransformationMethod(new PasswordTransformationMethod());
                                et_repeat_pwd_in_change_pwd_dialog.setSelection(et_repeat_pwd_in_change_pwd_dialog.getText().toString().length());
//                            Toast.makeText(getContext(), "hide", Toast.LENGTH_SHORT).show();
                                btn_show_hide_repeat.setBackgroundResource((R.drawable.eye_open));
                            }
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }

    private void Get_Update_Password_Details() {
        try {
            str_password = et_pwd_in_change_pwd_dialog.getText().toString();
            str_repeat_pwd = et_repeat_pwd_in_change_pwd_dialog.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source_detail", "email");
            jsonObject.put("email", str_email);
            jsonObject.put("password", str_password);
            jsonObject.put("new_password", str_repeat_pwd);

            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(true);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);
            Log.e("Json_value_Profile", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_NEW_PWD_UPDATE_CALL("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
//                    if (Objects.requireNonNull(response.body()).data != null) {
                        if (response.code() == 200) {
                            str_code = Objects.requireNonNull(response.body()).status;
                            str_message = response.body().message;
                            Log.e("str_code_profile", str_code);
                            Log.e("str_message_profile", str_message);
                            if (response.isSuccessful()) {
                                if (str_code.equalsIgnoreCase("error")) {
                                    pd.dismiss();
                                    assert response.body() != null;
                                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.body().message);
                                    if (str_message.equalsIgnoreCase("Old password doesn't match.")) {
                                        dialog.show();
                                        dialog.setCancelable(true);
                                    }
                                } else if (str_code.equalsIgnoreCase("success")) {
                                    pd.dismiss();
                                    dialog.dismiss();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("SOURCEDETAILS", "email");
                                    contentValues.put("EMAIL", str_email);
                                    contentValues.put("SIGNUPSTATUS", "6");
                                    contentValues.put("PASSWORD", str_repeat_pwd);
                                    db.update("LOGINDETAILS", contentValues, "EMAIL='" + str_email + "'", null);
                                    DBEXPORT();
//                        Log.e("profile_cntn_values", contentValues.toString());
                                    assert response.body() != null;
                                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.body().message);

                                    /*This is for refreshing current activity*/
                                    // Reload current fragment
                                    FragmentTransaction ft = null;
                                    if (getFragmentManager() != null) {
                                        ft = getFragmentManager().beginTransaction();
                                        ft.detach(My_Profile_Fragment.this).attach(My_Profile_Fragment.this).commit();
                                    }
                                }
                            }
                        } else if (response.code() == 401) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        } else if (response.code() == 500) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                        }
                     /*} else {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Something went wrong try again :)");
                    }*/
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
        getActivity().registerReceiver(broadcastReceiver, internetFilter);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setSnackbarMessage(String status) {
        String internetStatus;
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = getResources().getString(R.string.back_online_txt);
            snackbar = Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.timer_bg_color);
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
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
                Get_User_Wallet_Details();
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
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
    }
}
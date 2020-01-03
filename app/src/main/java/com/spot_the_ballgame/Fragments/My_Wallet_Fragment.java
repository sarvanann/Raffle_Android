package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.spot_the_ballgame.Interface.APIInterface;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Reward_Video_Act;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Toast_Message;
import com.spot_the_ballgame.Transaction_Act;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class My_Wallet_Fragment extends Fragment implements View.OnClickListener {
    private EditText et_rupees, et_paytm_mobile_number_in_wallet;
    private TextView tv_coins, tv_withdraw_amount, tv_wallet_coins, tv_wallet_rupees, tv_info_icon, tv_redeemable_coins;
    //    int int_amount_value = 1;
    private int int_amount_value, int_wallet_coins, int_remainder_value;
    private int int_input_point_value;
    private Button btn_transaction, btn_earn_more, btn_redeem;
    Fragment fragment;
    FragmentTransaction ft;

    private AdView mAdView;
    private String str_phone_num, str_email;
    private String str_auth_token, str_session_wallet_coins, str_session_wallet_rupees, str_session_withdraw_amt;
    boolean show = false;

    Dialog dialog;
    String str_wallet2, str_playby;
    int int_navi_draw_point_amount;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_wallet_fragment, container, false);
        SQLiteDatabase db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        et_rupees = view.findViewById(R.id.et_rupees);
        et_paytm_mobile_number_in_wallet = view.findViewById(R.id.et_paytm_mobile_number_in_wallet);
        tv_coins = view.findViewById(R.id.tv_coins);
        tv_withdraw_amount = view.findViewById(R.id.tv_withdraw_amount);
        tv_wallet_coins = view.findViewById(R.id.tv_wallet_coins);
        tv_wallet_rupees = view.findViewById(R.id.tv_wallet_rupees);
        tv_info_icon = view.findViewById(R.id.tv_info_icon);
        tv_redeemable_coins = view.findViewById(R.id.tv_redeemable_coins);


        btn_redeem = view.findViewById(R.id.btn_redeem);
        btn_transaction = view.findViewById(R.id.btn_transaction);
        btn_earn_more = view.findViewById(R.id.btn_earn_more);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        str_session_wallet_coins = SessionSave.getSession("Wallet_Coins", Objects.requireNonNull(getActivity()));
        str_session_wallet_rupees = SessionSave.getSession("Wallet_Rupees", Objects.requireNonNull(getActivity()));
        str_session_withdraw_amt = SessionSave.getSession("Minimum_Withdraw_Amount", Objects.requireNonNull(getActivity()));

        tv_withdraw_amount.setText("Minimum balance you can withdraw from wallet is : " + str_session_withdraw_amt);
        tv_wallet_coins.setText(str_session_wallet_coins);
        tv_wallet_rupees.setText("Rs." + str_session_wallet_rupees + " = ");
//        Log.e("str_session_wallet_coins_wall", str_session_wallet_coins);
//        Log.e("str_session_wallet_rupees_wall", str_session_wallet_rupees);
//        Log.e("str_session_withdraw_amt_wall", str_session_withdraw_amt);

        btn_earn_more.setOnClickListener(this);
        btn_redeem.setOnClickListener(this);
        btn_transaction.setOnClickListener(this);
        tv_info_icon.setOnClickListener(this);


        Navigation_Drawer_Act.tv_title_txt.setText(R.string.wallet_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        int_navi_draw_point_amount = Integer.parseInt(Navigation_Drawer_Act.tv_points.getText().toString());
        Log.e("int_navi_draw_point_amount", "" + int_navi_draw_point_amount);
        str_auth_token = SessionSave.getSession("Token_value", Objects.requireNonNull(getActivity()));
        Log.e("str_auth_token_wallet", str_auth_token);

        String select = "select PHONENO,EMAIL from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_phone_num = cursor.getString(0);
                str_email = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();


        et_rupees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_rupees.getText().toString().trim().equals("")) {
//                    Toast_Message.showToastMessage(Objects.requireNonNull(getContext()), "Point value is empty");
                    tv_coins.setText("");
                } else {
                    int_input_point_value = Integer.parseInt(et_rupees.getText().toString());
                    int_amount_value = Integer.parseInt(str_session_wallet_rupees);
                    int_wallet_coins = Integer.parseInt(str_session_wallet_coins);
//                    int n1 = (int_input_point_value / int_wallet_coins) * int_amount_value;
                    int n1 = (int_input_point_value / int_amount_value) * int_wallet_coins;
                    int_remainder_value = (int_input_point_value % int_wallet_coins) * int_amount_value;
                    Log.e("int_input_point_value", "" + int_input_point_value);
                    Log.e("int_amount_value", "" + int_amount_value);
                    Log.e("int_wallet_coins", "" + int_wallet_coins);
                    Log.e("input_value", "" + n1);
                    Log.e("remainder_value", "" + int_remainder_value);
                    int n11 = int_input_point_value - int_remainder_value;
                    Log.e("final_value", "" + n11);
                    tv_coins.setText(String.valueOf(n1));


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Get_User_Wallet_Details();
        return view;
    }


    private void Get_User_Wallet_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Log.e("wallet_json_dashboard", jsonObject.toString());
            Call<Category_Model> call = apiInterface.GET_WalletDetailsModelCall("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            str_wallet2 = response.body().data.get(0).wallet2;
                            Log.e("str_wallet2", str_wallet2);
                            tv_redeemable_coins.setText(str_wallet2);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
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
        Log.e("backStackCnt_wallet", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

        /*  Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
*/
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
            case R.id.btn_redeem:
                String str_points, str_amount, str_phone_number;
                str_points = et_rupees.getText().toString();
                str_amount = tv_coins.getText().toString();
                str_phone_number = et_paytm_mobile_number_in_wallet.getText().toString();
                if (str_points.isEmpty()) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Please enter points");
                } else if (str_amount.isEmpty()) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Please enter amount");
                }/* else if (str_phone_number.isEmpty()) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Please enter phone number");
                }*/ else {
                    double nn = Double.parseDouble(tv_redeemable_coins.getText().toString());
                    int int_final_point = Integer.parseInt(tv_coins.getText().toString());
                    Log.e("tv_redeem_amount", "" + nn);
                    Log.e("tv_chaning_amount_wallet", "" + int_final_point);
                    if (nn > int_final_point) {
                        Get_Redeem_Details();
                        softKeyboardVisibility(show);
                    } else {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "You don't have enough balance to redeem");
                    }
                }

                break;
            case R.id.btn_transaction:
                Intent intent = new Intent(getContext(), Transaction_Act.class);
                startActivity(intent);
                break;
            case R.id.btn_earn_more:
                Intent intent_01 = new Intent(getActivity(), Reward_Video_Act.class);
                intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_01);
                break;
            case R.id.tv_info_icon:
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.info_alert);
                dialog.setCancelable(true);
                TextView tv_info_txt;
                Button btn_ok;
                tv_info_txt = dialog.findViewById(R.id.tv_info_txt);
                tv_info_txt.setText("Minimum balance you can withdraw from wallet is : " + str_session_withdraw_amt);
                dialog.setCancelable(false);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btn_ok = dialog.findViewById(R.id.btn_ok);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
        }
    }

    /*This method is used for forced to close and open keyboard*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void softKeyboardVisibility(boolean show) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), 0);
        }
    }

    private void Get_Redeem_Details() {
        try {
           /* JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            jsonObject.put("phoneno", et_paytm_mobile_number_in_wallet.getText().toString());
            jsonObject.put("coins", et_rupees.getText().toString());
            jsonObject.put("amount", tv_coins.getText().toString());
            jsonObject.put("payment_type", "paytm");
            Log.e("wallet_json", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_REDEEM_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    str_email = Objects.requireNonNull(response.body()).message;
                    Toast_Message.showToastMessage(getActivity(), str_email);
                    Log.e("str_message", str_email);
                }

                @Override
                public void onFailure(Call<Category_Model> call, Throwable t) {

                }
            });*/

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", str_email);
                jsonObject.put("phoneno", str_phone_num);
//                jsonObject.put("phoneno", et_paytm_mobile_number_in_wallet.getText().toString());

                /*Here tv_amount_value is equals to COINS*/
                /*Here et_rupees is equals to AMOUNT*/
                jsonObject.put("coins", tv_coins.getText().toString());
                jsonObject.put("amount", et_rupees.getText().toString());
                jsonObject.put("payment_type", "paytm");
                Log.e("wallet_json", jsonObject.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
                //This is for SK
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.2.3/raffle/api/v1/get_prize_distribution", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                //This is for skyrand
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Factory.BASE_URL_MOBILE_APP + "redeem", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            et_rupees.setText("");

                            /*This is for changing the redeemable amount using api*/
                            Get_User_Wallet_Details();
                            Get_Wallet_Balance_Details();


                            JSONObject jsonObject1 = new JSONObject(String.valueOf(response));
                            Log.e("wallet_response", jsonObject1.toString());
                            String str_msg = response.getString("message");
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), str_msg);
                            Log.e("walletmessage", str_msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept", "application/json");
                        headers.put("Authorization", str_auth_token);
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get_Wallet_Balance_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", str_email);
            APIInterface apiInterface = Factory.getClient();
            Call<Category_Model> call = apiInterface.GET_WALLET_BALALNCE_DETAILS("application/json", jsonObject.toString(), str_auth_token);
            call.enqueue(new Callback<Category_Model>() {
                @Override
                public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_amount = Objects.requireNonNull(response.body()).current_amt;
                            Navigation_Drawer_Act.tv_points.setText(str_amount);
                            Log.e("str_amount_nav_wal", str_amount);
                        }
                    } else if (response.code() == 401) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
                    } else if (response.code() == 500) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), response.message());
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
}
package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Reward_Video_Act;
import com.spot_the_ballgame.Toast_Message;
import com.spot_the_ballgame.Transaction_Act;

import java.util.Objects;

public class My_Wallet_Fragment extends Fragment implements View.OnClickListener {
    EditText et_point_wallet;
    TextView tv_amount_wallet;
    int int_amount_value = 1;
    int int_input_point_value;
    Button btn_transaction, btn_earn_more, btn_redeem;
    Fragment fragment;
    FragmentTransaction ft;

    private AdView mAdView;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_wallet_fragment, container, false);
        et_point_wallet = view.findViewById(R.id.et_point_wallet);
        tv_amount_wallet = view.findViewById(R.id.tv_amount_wallet);
        btn_redeem = view.findViewById(R.id.btn_redeem);
        btn_transaction = view.findViewById(R.id.btn_transaction);
        btn_earn_more = view.findViewById(R.id.btn_earn_more);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btn_earn_more.setOnClickListener(this);


        Navigation_Drawer_Act.tv_title_txt.setText(R.string.wallet_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        btn_transaction.setOnClickListener(this);
        et_point_wallet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_point_wallet.getText().toString().trim().equals("")) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getContext()), "Point value is empty");
                } else {
                    int_input_point_value = Integer.parseInt(et_point_wallet.getText().toString());
                    int n1 = (int_input_point_value / 10) * int_amount_value;
                    Log.e("input_value", "" + n1);

                    tv_amount_wallet.setText(String.valueOf(n1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        Log.e("backStackCnt_wallet", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_transaction:
                Intent intent = new Intent(getContext(), Transaction_Act.class);
                startActivity(intent);
                break;
            case R.id.btn_earn_more:
                Intent intent_01 = new Intent(getActivity(), Reward_Video_Act.class);
                intent_01.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_01);
                break;
        }
    }
}
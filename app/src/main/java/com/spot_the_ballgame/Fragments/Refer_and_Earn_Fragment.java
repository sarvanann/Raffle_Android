package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;
import com.spot_the_ballgame.Toast_Message;

import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Refer_and_Earn_Fragment extends Fragment implements View.OnClickListener {
    ConstraintLayout constraintLayout_copy, constraintLayout_share;
    Button btn_referral_policy;
    ClipboardManager clipboardManager;

    String str_referral_Code = "";
    String str_referral_rules_link = "";
    EditText et_referral_code;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refer_and_earn_fragement_layout, container, false);
        constraintLayout_copy = view.findViewById(R.id.constraintLayout_copy);
        constraintLayout_share = view.findViewById(R.id.constraintLayout_share);
        btn_referral_policy = view.findViewById(R.id.btn_referral_policy);
        et_referral_code = view.findViewById(R.id.et_referral_code);

        constraintLayout_copy.setOnClickListener(this);
        constraintLayout_share.setOnClickListener(this);
        btn_referral_policy.setOnClickListener(this);

        str_referral_Code = SessionSave.getSession("Referral_Code_Value", Objects.requireNonNull(getActivity()));
        str_referral_rules_link = SessionSave.getSession("Referral_Rules_Link", Objects.requireNonNull(getActivity()));


        if (!(str_referral_Code.equalsIgnoreCase("No data"))) {
            et_referral_code.setText(str_referral_Code);
            et_referral_code.setSelection(et_referral_code.getText().toString().length());
        } else {
            et_referral_code.setText("");
        }
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.menu_refer_and_earn);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);

        // Get clipboard manager object.
        Object clipboardService = getActivity().getSystemService(CLIPBOARD_SERVICE);
        clipboardManager = (ClipboardManager) clipboardService;
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constraintLayout_copy:
                Log.e("str_referral_Code", str_referral_Code);


                if (et_referral_code.getText().toString().isEmpty()) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Please enter referral code");
                } else {
                    // Create a new ClipData.
                    ClipData clipData = ClipData.newPlainText("Source Text", str_referral_Code);
                    // Set it as primary clip data to copy text to system clipboard.
                    clipboardManager.setPrimaryClip(clipData);
                    // Popup a snackbar.
                    Snackbar snackbar = Snackbar.make(v, "Referral code copied.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.BLACK);
                    snackbar.show();
                    if (str_referral_Code.equalsIgnoreCase("No data")) {
                        et_referral_code.setText("");
                    } else {
                        et_referral_code.setText(str_referral_Code);
                    }
                }

                break;
            case R.id.constraintLayout_share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Referral code is");
                String app_url = str_referral_Code;
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.btn_referral_policy:
                if (!(str_referral_rules_link.equalsIgnoreCase("No data"))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(str_referral_rules_link));
                    startActivity(intent);
                } else {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Referral policy link not available right now");
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

    }
}

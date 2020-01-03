package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;

import java.util.Objects;

public class Share_Fragment extends Fragment implements View.OnClickListener {
    Button btn_share;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragement_layout, container, false);
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.help_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        btn_share = view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        Log.e("backStackCnt_share", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

        /*Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
        /*
        Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
*/
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(Objects.requireNonNull(getActivity()));
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                Bundle bundle = new Bundle();
                i.putExtra(android.content.Intent.EXTRA_TEXT, "extra text that you want to put");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "extra text that you want to put");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ID");
                i.setType("text/plain");
//                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
                i.putExtras(bundle);
                startActivity(Intent.createChooser(i, "Share via"));

                /*  FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(Objects.requireNonNull(getActivity()));
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ID");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
                i.putExtras(bundle);
                startActivity(Intent.createChooser(i, "Share via"));*/
                break;
        }
    }
}
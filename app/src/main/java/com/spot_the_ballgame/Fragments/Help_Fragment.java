package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;

import java.util.Objects;

public class Help_Fragment extends Fragment {
    private AdView mAdView;
    WebView webView;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragement, container, false);
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.help_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);

        mAdView = view.findViewById(R.id.adView);
        webView = view.findViewById(R.id.webview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        webView.loadUrl(Factory.BASE_HELP_TC_URL_MOBILE_APP + "help");
        return view;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
//        Log.e("backStackCnt_help", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }



        /*
        Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
*/
    }
}
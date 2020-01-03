package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;

import java.util.Objects;

public class How_To_Play_Fragment extends Fragment {
    private AdView mAdView;
    private ViewPager carousel_viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout carousel_dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    ConstraintLayout carousel_layout;
    Button btn_skip;


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.how_to_play_fragment, container, false);
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.menu_how_to_play_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        carousel_viewPager = view.findViewById(R.id.carousel_viewPager);
        carousel_dotsLayout = view.findViewById(R.id.carousel_dotsLayout);
        btn_skip = view.findViewById(R.id.btn_skip);
        carousel_layout = view.findViewById(R.id.carousel_layout);
        carousel_layout.setOnClickListener(v -> {
            int s1 = v.getVerticalScrollbarPosition();
        });

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};
        // adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        changeStatusBarColor();
        myViewPagerAdapter = new MyViewPagerAdapter();
        carousel_viewPager.setAdapter(myViewPagerAdapter);
        carousel_viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btn_skip.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Navigation_Drawer_Act.class);
            SessionSave.SaveSession("carousel_value", "5", getContext());
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        return view;
    }


    /*This method is used in carouselview for how to play screen bottom adding dot  */
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        carousel_dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            carousel_dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return carousel_viewPager.getCurrentItem() + i;
    }


    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btn_skip.setVisibility(View.VISIBLE);
            } else {
                // still pages are left
                btn_skip.setVisibility(View.VISIBLE);
                btn_skip.setText(getString(R.string.skip_txt));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 3) {
                Toast.makeText(getContext(), "position" + layouts.length, Toast.LENGTH_SHORT).show();
                btn_skip.setText(R.string.sign_up_txt);
            }
        }
    };


    /*This method is used in carouselview for how to play screen bottom dot color change */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        Log.e("backStackCnt_howptolay", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

    }
}
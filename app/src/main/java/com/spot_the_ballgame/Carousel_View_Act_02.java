package com.spot_the_ballgame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class Carousel_View_Act_02 extends AppCompatActivity {
    private ViewPager carousel_viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout carousel_dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    ConstraintLayout carousel_layout;
    Button btn_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carousel_view_lay);
        getSupportActionBar().hide();
        carousel_viewPager = findViewById(R.id.carousel_viewPager);
        carousel_dotsLayout = findViewById(R.id.carousel_dotsLayout);
        btn_skip = findViewById(R.id.btn_skip);
        carousel_layout = findViewById(R.id.carousel_layout);
        carousel_layout.setOnClickListener(v -> {
            int s1 = v.getVerticalScrollbarPosition();
//            Toast.makeText(Carousel_View_Act_02.this, "Position : " + s1, Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(Carousel_View_Act_02.this, Navigation_Drawer_Act.class);
            SessionSave.SaveSession("carousel_value", "5", Carousel_View_Act_02.this);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
//        FullScreenMethod();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void FullScreenMethod() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            Carousel_View_Act_02.this.getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = Carousel_View_Act_02.this.getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }

    /*This method is used in carouselview for how to play screen bottom adding dot  */
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        carousel_dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
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
//            Toast.makeText(Carousel_View_Act.this, "onPageScrolled" + arg0, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
//            Toast.makeText(Carousel_View_Act.this, "onPageScrollStateChanged" + arg0, Toast.LENGTH_SHORT).show();

            if (arg0 == 3) {
//                Toast.makeText(Carousel_View_Act_02.this, "position" + layouts.length, Toast.LENGTH_SHORT).show();
                btn_skip.setText(R.string.sign_up_txt);
            }
        }
    };


    /*This method is used in carouselview for how to play screen bottom dot color change */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
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
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
}

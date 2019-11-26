package com.spot_the_ballgame;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;
import com.spot_the_ballgame.Fragments.BottomSheetFragment;
import com.spot_the_ballgame.Fragments.Dashboard;
import com.spot_the_ballgame.Fragments.Help_Fragment;
import com.spot_the_ballgame.Fragments.How_To_Play_Fragment;
import com.spot_the_ballgame.Fragments.My_Contest_Fragment;
import com.spot_the_ballgame.Fragments.My_Profile_Fragment;
import com.spot_the_ballgame.Fragments.My_Wallet_Fragment;
import com.spot_the_ballgame.Fragments.Share_Fragment;
import com.spot_the_ballgame.Fragments.Terms_and_Condition_Fragment;
import com.spot_the_ballgame.Registration.SignUp.All_Btn_OnClick_Sign_Up_Act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;

public class Navigation_Drawer_Act extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment;
    FragmentTransaction ft;
    public static TextView tv_title_txt, tv_points;
    NotificationBadge notification_badge;
    int mCartItemCount = 10;

    MenuItem menuItem;
    View actionView;
    TextView textCartItemCount;
    TextView slideshow, gallery;
    public static TextView tv_toolbar_left_arrow;
    SQLiteDatabase db;
    String str_balance_points, str_session_carousel_values;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_navigation__drawer_);
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ImageButton menuRight = findViewById(R.id.menuRight);
        tv_title_txt = findViewById(R.id.tv_title_txt);
        tv_points = findViewById(R.id.tv_points);
        tv_toolbar_left_arrow = findViewById(R.id.tv_toolbar_left_arrow);
        tv_toolbar_left_arrow.setVisibility(View.GONE);
        notification_badge = findViewById(R.id.notification_badge);
        notification_badge.setNumber(1);

        str_session_carousel_values = SessionSave.getSession("carousel_value", Navigation_Drawer_Act.this);
        Log.e("session_carousel_values", str_session_carousel_values);

        if (str_session_carousel_values.equals("1")) {
            Show_Carousel_Layout();
        }
        if (str_session_carousel_values != null && str_session_carousel_values.equals("5")) {
            ShowDashboard();
        }
        menuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        NavigationView navigationView2 = findViewById(R.id.nav_view2);
        navigationView2.setNavigationItemSelectedListener(this);
        db.execSQL("create table if not exists LOGINDETAILS(USERNAME varchar,PASSWORD varchar,STATUS int,IMEI varchar);");

        String select = "select BALANCE from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_balance_points = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DBEXPORT();
        Log.e("dash_balance_points", str_balance_points);

        tv_points.setText(str_balance_points);
//        showBottomSheetDialogFragment();
        tv_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new My_Wallet_Fragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            }
        });

        //These lines should be added in the OnCreate() of your main activity
        gallery = (TextView) MenuItemCompat.getActionView(navigationView2.getMenu().
                findItem(R.id.nav_my_wallet));
        slideshow = (TextView) MenuItemCompat.getActionView(navigationView2.getMenu().
                findItem(R.id.nav_contest));
//This method will initialize the count value
        initializeCountDrawer();

        tv_toolbar_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_toolbar_left_arrow.setVisibility(View.GONE);
//                Navigation_Drawer_Act.this.getSupportFragmentManager().popBackStack();

                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.e("backStackCnt_howptolay", "" + backStackEntryCount);
                if (backStackEntryCount == 1) {
                    Intent intent = new Intent(Navigation_Drawer_Act.this, Navigation_Drawer_Act.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Toast.makeText(Navigation_Drawer_Act.this, "IF", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Navigation_Drawer_Act.this, "ELSE", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
                }
            }
        });
    }

    private void Show_Carousel_Layout() {
        Intent intent = new Intent(Navigation_Drawer_Act.this, Carousel_View_Act_02.class);
        startActivity(intent);
    }

    private void initializeCountDrawer() {
        //Gravity property aligns the text
        gallery.setGravity(Gravity.CENTER_VERTICAL);
        gallery.setTypeface(null, Typeface.BOLD);
        gallery.setTextColor(getResources().getColor(R.color.colorAccent));
        gallery.setText("99+");
        slideshow.setGravity(Gravity.CENTER_VERTICAL);
        slideshow.setTypeface(null, Typeface.BOLD);
        slideshow.setTextColor(getResources().getColor(R.color.colorAccent));
//count is added
        slideshow.setText("7");
    }

    /**
     * showing bottom sheet dialog fragment
     * same layout is used in both dialog and dialog fragment
     */

    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void ShowDashboard() {
        fragment = new Dashboard();
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment ff = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (ff instanceof My_Profile_Fragment) {
                ((My_Profile_Fragment) ff).onBackPressed();
            }
            if (ff instanceof My_Wallet_Fragment) {
                ((My_Wallet_Fragment) ff).onBackPressed();
            }
            if (ff instanceof My_Contest_Fragment) {
                ((My_Contest_Fragment) ff).onBackPressed();
            }
            if (ff instanceof How_To_Play_Fragment) {
                ((How_To_Play_Fragment) ff).onBackPressed();
            }
            if (ff instanceof Help_Fragment) {
                ((Help_Fragment) ff).onBackPressed();
            }
            if (ff instanceof Share_Fragment) {
                ((Share_Fragment) ff).onBackPressed();
            }

            if (ff instanceof Terms_and_Condition_Fragment) {
              /*  ((Terms_and_Condition_Fragment) ff).onBackPressed();
            }
            if (ff instanceof Privacy_Policy_Fragment) {
                */
                ((Terms_and_Condition_Fragment) ff).onBackPressed();
            }

        } else {
            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(a);
                System.exit(0);
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation__drawer__drawer, menu);

        menuItem = menu.findItem(R.id.nav_my_profile);

        actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String text = "";
        if (id == R.id.nav_home) {
            Intent intent = new Intent(Navigation_Drawer_Act.this, Navigation_Drawer_Act.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } else if (id == R.id.nav_my_profile) {
/*
            if (textCartItemCount != null) {
                if (mCartItemCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }

*/

            fragment = new My_Profile_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_my_wallet) {
            fragment = new My_Wallet_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_contest) {
            fragment = new My_Contest_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_how_to_play) {
            fragment = new How_To_Play_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_help) {
            fragment = new Help_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_terms_and_condition) {
            fragment = new Terms_and_Condition_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_share) {
            fragment = new Share_Fragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_logout) {
            final Dialog dialog = new Dialog(Navigation_Drawer_Act.this);
            dialog.setContentView(R.layout.logout_alert);
            TextView tv_yes, tv_no;
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            tv_yes = dialog.findViewById(R.id.tv_yes);
            tv_no = dialog.findViewById(R.id.tv_cancel);
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();
                    cv.put("SIGNUPSTATUS", 0);
                    cv.put("STATUS", 0);
                    db.update("LOGINDETAILS", cv, null, null);
                    Splash_Screen_Act.str_global_mail_id = "";
                    DBEXPORT();
                    finish();
                    Intent intent = new Intent(Navigation_Drawer_Act.this, All_Btn_OnClick_Sign_Up_Act.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    System.exit(0);
                    dialog.dismiss();
                }
            });
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        drawer.closeDrawer(GravityCompat.END);
        return true;
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

}
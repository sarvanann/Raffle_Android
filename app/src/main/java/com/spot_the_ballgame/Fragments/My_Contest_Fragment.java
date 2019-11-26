package com.spot_the_ballgame.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.spot_the_ballgame.Game_Details_Screen_Act_02;
import com.spot_the_ballgame.My_Contest_History;
import com.spot_the_ballgame.Navigation_Drawer_Act;
import com.spot_the_ballgame.R;

import java.util.Objects;

public class My_Contest_Fragment extends Fragment implements View.OnClickListener {
    SQLiteDatabase db;
    String str_db_values, str_balance;
    TextView tv_your_points, tv_your_game_time, tv_your_game_point_values, tv_your_game_title;
    TextView tv_present_contest, tv_history_contest;
    ConstraintLayout constraintLayout_present_contest_layout, constraintLayout_history_contest_layout, constraintLayout_game_1, constraintLayout_game_2, constraintLayout_game_3, constraintLayout_game_1_history, constraintLayout_game_2_history, constraintLayout_game_3_history;
    Intent intent;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_contest_fragment, container, false);
        tv_your_points = view.findViewById(R.id.tv_your_points);
        tv_your_game_time = view.findViewById(R.id.tv_your_game_time);
        tv_your_game_point_values = view.findViewById(R.id.tv_your_game_point_values);
        tv_your_game_title = view.findViewById(R.id.tv_your_game_title);

        tv_present_contest = view.findViewById(R.id.tv_present_contest);
        tv_history_contest = view.findViewById(R.id.tv_history_contest);

        constraintLayout_present_contest_layout = view.findViewById(R.id.constraintLayout_present_contest_layout);
        constraintLayout_history_contest_layout = view.findViewById(R.id.constraintLayout_history_contest_layout);

        constraintLayout_game_1_history = view.findViewById(R.id.constraintLayout_game_1_history);
        constraintLayout_game_2_history = view.findViewById(R.id.constraintLayout_game_2_history);
        constraintLayout_game_3_history = view.findViewById(R.id.constraintLayout_game_3_history);

        constraintLayout_game_1 = view.findViewById(R.id.constraintLayout_game_11);
        constraintLayout_game_2 = view.findViewById(R.id.constraintLayout_game_22);
        constraintLayout_game_3 = view.findViewById(R.id.constraintLayout_game_33);


        Navigation_Drawer_Act.tv_title_txt.setText(R.string.contest_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
        tv_present_contest.setOnClickListener(this);
        tv_history_contest.setOnClickListener(this);
        constraintLayout_game_1_history.setOnClickListener(this);
        constraintLayout_game_2_history.setOnClickListener(this);
        constraintLayout_game_3_history.setOnClickListener(this);

        constraintLayout_game_1.setOnClickListener(this);
        constraintLayout_game_2.setOnClickListener(this);
        constraintLayout_game_3.setOnClickListener(this);

        constraintLayout_present_contest_layout.setVisibility(View.VISIBLE);
        constraintLayout_history_contest_layout.setVisibility(View.GONE);
       /* db = Objects.requireNonNull(Objects.requireNonNull(getActivity()).openOrCreateDatabase("Spottheball.db", Context.MODE_PRIVATE, null));
        String select = "select USER_SELECTION_VALUE  from LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_db_values = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("str_source_details_contest", str_db_values);
        tv_your_game_point_values.setText(str_db_values);*/
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        Log.e("backStackCnt_contest", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }

        /*Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
       /* Intent intent = new Intent(getContext(), Navigation_Drawer_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_present_contest:
                tv_present_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_present_contest.setTextColor(getResources().getColor(R.color.black_color));
                constraintLayout_present_contest_layout.setVisibility(View.VISIBLE);
                constraintLayout_history_contest_layout.setVisibility(View.GONE);
                tv_history_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_history_contest.setTextColor(getResources().getColor(R.color.history_grey_color));
                break;
            case R.id.tv_history_contest:
                constraintLayout_history_contest_layout.setVisibility(View.VISIBLE);
                tv_history_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_history_contest.setTextColor(getResources().getColor(R.color.black_color));
                constraintLayout_present_contest_layout.setVisibility(View.GONE);
                tv_present_contest.setBackground(getResources().getDrawable(R.drawable.black_border_bg_normal));
                tv_present_contest.setTextColor(getResources().getColor(R.color.history_grey_color));
                break;
            case R.id.constraintLayout_game_1_history:
                intent = new Intent(getContext(), My_Contest_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_2_history:
                intent = new Intent(getContext(), My_Contest_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_3_history:
                intent = new Intent(getContext(), My_Contest_History.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;

            case R.id.constraintLayout_game_11:
                intent = new Intent(getContext(), Game_Details_Screen_Act_02.class);
                intent.putExtra("onclick_contest_value", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_22:
                intent = new Intent(getContext(), Game_Details_Screen_Act_02.class);
                intent.putExtra("onclick_contest_value", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.constraintLayout_game_33:
                intent = new Intent(getContext(), Game_Details_Screen_Act_02.class);
                intent.putExtra("onclick_contest_value", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Navigation_Drawer_Act.tv_title_txt.setText(R.string.contest_txt);
        Navigation_Drawer_Act.tv_toolbar_left_arrow.setVisibility(View.VISIBLE);
    }
}
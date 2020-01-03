package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spot_the_ballgame.Game_Details_Screen_Act;
import com.spot_the_ballgame.Game_Two_Act;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ContestAdapter_For_End_Game extends RecyclerView.Adapter<ContestAdapter_For_End_Game.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    private Context mContext;
    private String str_seconds, str_2x_powerup, str_contest_id, str_entry_fees;
    private long lng_seconds;
    private long different_milli_seconds;
    private String str_rule_id;
    private String str_status_onclick;
    private ArrayList<String> stringArrayList = new ArrayList<>();

    String str_imagepath;
    private String str_correct_ans, str_wrong_ans, str_skip;

    public ContestAdapter_For_End_Game(FragmentActivity activity, ArrayList<Category_Model.Data> data) {
        this.mContext = activity;
        this.arrayList = data;
    }

    @NonNull
    @Override
    public ContestAdapter_For_End_Game.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.contest_adapter_details_for_end_game_layout, parent, false);
        return new ContestAdapter_For_End_Game.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ContestAdapter_For_End_Game.ViewHolder holder, final int position) {
        //        Collections.sort(arrayList, new Comparator<Category_Model.Data>() {
//            @Override
//            public int compare(Category_Model.Data obj1, Category_Model.Data obj2) {
//                return obj1.getStart_date_time().compareToIgnoreCase(obj2.getEnd_date_time()); // To compare string values
//            }
//            // return Integer.valueOf(obj1.getId()).compareTo(obj2.getId()); // To compare integer values
//
//
//            // ## Descending order
//            // return obj2.getCompanyName().compareToIgnoreCase(obj1.getCompanyName()); // To compare string values
//            // return Integer.valueOf(obj2.getId()).compareTo(obj1.getId()); // To compare integer values
//
//        });
        String str_status = arrayList.get(position).status;
        String str_categories = arrayList.get(position).categories;
        /*This follwing lines of code are used for if the contest is prediction use this below code.*/
        if (str_categories.equalsIgnoreCase("Prediction")) {
            holder.constraintLayout_country_flag_layout.setVisibility(View.VISIBLE);
            holder.tv_play_with_points_contest_adapter.setVisibility(View.GONE);
            String str_team_a_path, str_team_b_path;

            str_team_a_path = arrayList.get(position).team_a;
            str_team_b_path = arrayList.get(position).team_b;

            Glide.with(mContext).load(str_team_a_path)
                    .thumbnail(Glide.with(mContext).load(str_team_a_path))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_first_country_flag);

            Glide.with(mContext).load(str_team_b_path)
                    .thumbnail(Glide.with(mContext).load(str_team_b_path))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_second_country_flag);

            holder.tv_first_country_txt.setText(arrayList.get(position).team_a_name.toUpperCase());
            holder.tv_second_country_txt.setText(arrayList.get(position).team_b_name.toUpperCase());
        } else {
            holder.constraintLayout_country_flag_layout.setVisibility(View.GONE);
            holder.tv_play_with_points_contest_adapter.setVisibility(View.VISIBLE);
        }

        Log.e("outside", str_status);
        /*If contests are Completed like status ==1 means end_in textview set to status otherwise ends in */
        if (str_status.equalsIgnoreCase("1")) {
            holder.constraintLayout_play_with_points_contest_adapter.setBackgroundResource(R.drawable.played_status_grey_bg);
            holder.tv_play_with_points_contest_adapter.setTextColor(R.color.black_color);
            holder.tv_ends_in_status_txt.setText("Status");
        } else {
            holder.tv_ends_in_status_txt.setText(R.string.ends_in_txt);
        }
        holder.tv_play_with_points_contest_adapter.setText(arrayList.get(position).contest_name);
        holder.tv_prize_pool_contest_adapter.setText(arrayList.get(position).price);
        str_imagepath = arrayList.get(position).categories_image;
        Glide.with(mContext).load(str_imagepath)
                .thumbnail(Glide.with(mContext).load(str_imagepath))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.iv_categoires_image);
        final String t2 = arrayList.get(position).end_date_time;
        stringArrayList.add(t2);
        Log.e("t22222", t2);
        Collections.sort(stringArrayList);
        Log.e("stringArrayList", stringArrayList.toString());

        /*Getting Current Time*/
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strDate = mdformat.format(calendar.getTime());


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date current_system_time = mdformat.parse(strDate);
            Date end_date_api = sdf.parse(t2);
            Log.e("end_date_api", String.valueOf(end_date_api));
            Log.e("parse_cur_system_time", String.valueOf(current_system_time));
            different_milli_seconds = end_date_api.getTime() - current_system_time.getTime();
            Log.e("different_milli_seconds", String.valueOf(different_milli_seconds));
            if (different_milli_seconds < 0) {
                if (str_status.equalsIgnoreCase("1")) {
                    holder.tv_end_time_contest_adapter.setText("Played");
                } else {
                    holder.tv_end_time_contest_adapter.setText("Finished");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("star_status", str_status);
        if (str_status.equalsIgnoreCase("1")) {
            holder.tv_end_time_contest_adapter.setText("Played");
        } else {
            holder.countDownTimer = new CountDownTimer(different_milli_seconds, 1000) {
                @SuppressLint("SetTextI18n")
                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public void onTick(long millisUntilFinished) {
                    lng_seconds = millisUntilFinished;
                    String s1 = String.format("%2d", lng_seconds).trim();
                    @SuppressLint("DefaultLocale") String ss = String.format("%02d:%02d:%02d", lng_seconds / 60, lng_seconds % 60, 0);

                    @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    //System.out.println("HH_MM_SS:: " + hms);


                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = lng_seconds / daysInMilli;
                    lng_seconds = lng_seconds % daysInMilli;

                    if (elapsedDays == 1) {
                        holder.tv_end_time_contest_adapter.setText(elapsedDays + " day left");
                    } else if (elapsedDays > 1) {
                        holder.tv_end_time_contest_adapter.setText(elapsedDays + " days left");
                    } else {
                        holder.tv_end_time_contest_adapter.setText(hms);
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }

        String fees_type = arrayList.get(position).fee_type;
        if (fees_type.equalsIgnoreCase("0")) {
            holder.tv_entry_fee_details.setVisibility(View.VISIBLE);
            holder.tv_entry_fee_details_coin_icon.setVisibility(View.GONE);
            holder.tv_entry_fee_details.setText("Free");
        } else {
            holder.tv_entry_fee_details.setVisibility(View.VISIBLE);
            holder.tv_prize_details_coin_icon.setVisibility(View.VISIBLE);
            holder.tv_entry_fee_details.setText(arrayList.get(position).entry_fee);
        }
        holder.constraintLayout_constest_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_correct_ans = arrayList.get(position).correct_mark;
                str_wrong_ans = arrayList.get(position).wrong_mark;
                str_skip = arrayList.get(position).skip;
                int s1 = holder.getAdapterPosition();
                String str_categories = arrayList.get(position).categories;
                str_imagepath = arrayList.get(position).categories_image;
                str_rule_id = arrayList.get(position).rules_id;
                str_status_onclick = arrayList.get(position).status;
                Log.e("str_status_onclick", str_status_onclick);
                Log.e("str_rule_id", str_rule_id);
                Log.e("rules_name", arrayList.get(position).rules_name);
                if (str_categories.equalsIgnoreCase("Monday") || str_categories.equalsIgnoreCase("Trivia")) {
                    Toast.makeText(mContext, "Rule ID :" + " " + str_status_onclick, Toast.LENGTH_SHORT).show();
                    String fees_type = arrayList.get(position).fee_type;
                    if (fees_type.equalsIgnoreCase("0")) {
                        holder.tv_entry_fee_details.setText("Free");
                    } else {
                        holder.tv_entry_fee_details.setText(arrayList.get(position).entry_fee);
                    }
                    Intent intent1 = new Intent(mContext, Game_Two_Act.class);
                    str_seconds = arrayList.get(position).seconds;
                    str_2x_powerup = arrayList.get(position).powerup_count;
                    str_contest_id = arrayList.get(position).contest_id;
                    str_entry_fees = holder.tv_entry_fee_details.getText().toString();
                    intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                    intent1.putExtra("game_name", holder.tv_play_with_points_contest_adapter.getText().toString());
                    intent1.putExtra("prize_amount", holder.tv_prize_pool_contest_adapter.getText().toString());
                    intent1.putExtra("end_time", holder.tv_end_time_contest_adapter.getText().toString());
                    intent1.putExtra("count_down_seconds", str_seconds);
                    intent1.putExtra("str_2x_powerup", str_2x_powerup);
                    intent1.putExtra("str_contest_id", str_contest_id);
                    intent1.putExtra("str_entry_fees", str_entry_fees);
                    intent1.putExtra("str_rule_id", str_rule_id);
                    intent1.putExtra("str_status_onclick", str_status_onclick);
                    intent1.putExtra("str_imagepath", str_imagepath);

                    intent1.putExtra("str_correct_ans", str_correct_ans);
                    intent1.putExtra("str_wrong_ans", str_wrong_ans);
                    intent1.putExtra("str_skip", str_skip);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent1);
                }
                if (str_categories.equalsIgnoreCase("Spot the ball")) {
                    // Toast.makeText(mContext, "Rule IDSTB :" + " " + str_status_onclick, Toast.LENGTH_SHORT).show();
                    String fees_type = arrayList.get(position).fee_type;
                    if (fees_type.equalsIgnoreCase("0")) {
                        holder.tv_entry_fee_details.setText("Free");
                    } else {
                        holder.tv_entry_fee_details.setText(arrayList.get(position).entry_fee);
                    }
                    str_seconds = arrayList.get(position).seconds;
                    str_2x_powerup = arrayList.get(position).powerup_count;
                    str_contest_id = arrayList.get(position).contest_id;
                    str_entry_fees = holder.tv_entry_fee_details.getText().toString();
                    Log.e("str_secondsdsds",str_seconds);
                    Intent intent1 = new Intent(mContext, Game_Details_Screen_Act.class);
                    intent1.putExtra("game_name", holder.tv_play_with_points_contest_adapter.getText().toString());
                    intent1.putExtra("prize_amount", holder.tv_prize_pool_contest_adapter.getText().toString());
                    intent1.putExtra("end_time", holder.tv_end_time_contest_adapter.getText().toString());
                    intent1.putExtra("count_down_seconds", str_seconds);
                    intent1.putExtra("str_2x_powerup", str_2x_powerup);
                    intent1.putExtra("str_contest_id", str_contest_id);
                    intent1.putExtra("str_entry_fees", str_entry_fees);
                    intent1.putExtra("str_rule_id", str_rule_id);

                    intent1.putExtra("str_imagepath", str_imagepath);
                    intent1.putExtra("str_status_onclick", str_status_onclick);
                    intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                    intent1.putExtra("str_correct_ans", str_correct_ans);
                    intent1.putExtra("str_wrong_ans", str_wrong_ans);
                    intent1.putExtra("str_skip", str_skip);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Category_Model.Data product = arrayList.get(position);
        return Long.parseLong(product.categories);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_play_with_points_contest_adapter, tv_prize_pool_contest_adapter, tv_end_time_contest_adapter,
                tv_entry_fee_details, tv_prize_details_coin_icon,
                tv_entry_fee_details_coin_icon;
        ConstraintLayout constraintLayout_constest_layout, constraintLayout_play_with_points_contest_adapter, constraintLayout_country_flag_layout;
        CountDownTimer countDownTimer;
        ImageView iv_categoires_image, iv_first_country_flag, iv_second_country_flag;

        TextView tv_first_country_txt, tv_second_country_txt, tv_ends_in_status_txt;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            setIsRecyclable(false);
            tv_play_with_points_contest_adapter = itemView.findViewById(R.id.tv_play_with_points_contest_adapter);
            tv_prize_details_coin_icon = itemView.findViewById(R.id.tv_prize_details_coin_icon);
            tv_prize_pool_contest_adapter = itemView.findViewById(R.id.tv_prize_pool_contest_adapter);
            tv_end_time_contest_adapter = itemView.findViewById(R.id.tv_end_time_contest_adapter);
            tv_entry_fee_details = itemView.findViewById(R.id.tv_entry_fee_details);
            constraintLayout_constest_layout = itemView.findViewById(R.id.constraintLayout_game_1);
            constraintLayout_play_with_points_contest_adapter = itemView.findViewById(R.id.constraintLayout_play_with_points_contest_adapter);
            tv_entry_fee_details_coin_icon = itemView.findViewById(R.id.tv_entry_fee_details_coin_icon);
            iv_categoires_image = itemView.findViewById(R.id.iv_categoires_image);
            constraintLayout_country_flag_layout = itemView.findViewById(R.id.constraintLayout_country_flag_layout);

            iv_first_country_flag = itemView.findViewById(R.id.iv_first_country_flag);
            iv_second_country_flag = itemView.findViewById(R.id.iv_second_country_flag);

            tv_first_country_txt = itemView.findViewById(R.id.tv_first_country_txt);
            tv_second_country_txt = itemView.findViewById(R.id.tv_second_country_txt);
            tv_ends_in_status_txt = itemView.findViewById(R.id.tv_ends_in_status_txt);
        }
    }
}

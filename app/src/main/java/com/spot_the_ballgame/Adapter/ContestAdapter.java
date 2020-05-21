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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spot_the_ballgame.Game_Details_Screen_Act;
import com.spot_the_ballgame.Game_Two_Act;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.My_Contest_Live;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.SessionSave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/*In this class i'm handling Dashboard cardviews using recyclerview.Here im getting all values from get_contest.php like seconds,power_up_values,contest_id,entry_fee.*/
public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    private Context mContext;
    private String str_seconds,
            str_2x_powerup,
            str_contest_id,
            str_entry_fees,
            str_user_rank,
            str_winning_prize,
            str_total_onclick_answer_values;
    private long lng_seconds;
    private long elapsedDays;
    String hms;
    private long different_milli_seconds;
    private String str_rule_id;
    private String str_status_onclick;
    private String str_end_game;
    private String str_start_game;
    private String str_categories;
    private String str_status;
    private String str_imagepath;
    private String str_correct_ans;
    private String str_wrong_ans;
    private String str_skip;
    private String str_local_host;
    private String str_question_type;
    private String str_team_a_path;
    private String str_team_b_path;
    private String str_team_a_path_name_txt;
    private String str_team_b_path_name_txt;
    private String fees_type;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    FragmentManager fragmentManager;

    public ContestAdapter(FragmentActivity activity, ArrayList<Category_Model.Data> data, FragmentManager support_FragmentManager) {
        this.mContext = activity;
        this.arrayList = data;
        this.fragmentManager = support_FragmentManager;
    }

    @NonNull
    @Override
    public ContestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.contest_adapter_details_layout, parent, false);
        return new ContestAdapter.ViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ContestAdapter.ViewHolder holder, final int position) {
        /*
         *
         * 0-->Text
         * 1-->Image
         * 2-->Audio
         * 3-->Prediction
         * 4-->STB
         *
         * */
        str_question_type = arrayList.get(position).question_type;
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        str_status = arrayList.get(position).play_status;
        str_categories = arrayList.get(position).categories;

        /*
         *
         * 0-->Text
         * 1-->Image
         * 2-->Audio
         * 3-->Prediction
         * 4-->STB
         *
         * */
        /*This follwing lines of code are used for if the contest is prediction use this below code.*/
//      if (str_categories.equalsIgnoreCase("Prediction")) {
        if (str_question_type.equals("3")) {
            holder.constraintLayout_country_flag_layout.setVisibility(View.VISIBLE);
            holder.tv_play_with_points_contest_adapter.setVisibility(View.GONE);
            str_team_a_path = str_local_host + arrayList.get(position).team_a;
            str_team_b_path = str_local_host + arrayList.get(position).team_b;
            Glide.with(mContext).load(str_team_a_path)
                    .thumbnail(Glide.with(mContext).load(str_team_a_path))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_first_country_flag);

            Glide.with(mContext).load(str_team_b_path)
                    .thumbnail(Glide.with(mContext).load(str_team_b_path))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_second_country_flag);

            try {
                holder.tv_first_country_txt.setText(arrayList.get(position).team_a_name.toUpperCase());
                holder.tv_second_country_txt.setText(arrayList.get(position).team_b_name.toUpperCase());
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } else {
            holder.constraintLayout_country_flag_layout.setVisibility(View.GONE);
            holder.tv_play_with_points_contest_adapter.setVisibility(View.VISIBLE);
        }

//        Log.e("outside", str_status);
        /*If contests are Completed like status ==1 means end_in textview set to status otherwise ends in */
        if (str_status.equalsIgnoreCase("2")) {
            holder.constraintLayout_play_with_points_contest_adapter.setBackgroundResource(R.drawable.played_status_grey_bg);
            holder.tv_play_with_points_contest_adapter.setTextColor(R.color.black_color);
            /*
             *
             * 0-->Text
             * 1-->Image
             * 2-->Audio
             * 3-->Prediction
             * 4-->STB
             *
             * */
//            if (str_categories.equalsIgnoreCase("Prediction")) {
            if (str_question_type.equals("3")) {
                holder.constraintLayout_country_flag_layout.setBackgroundResource(R.drawable.played_status_grey_bg);
                holder.tv_first_country_txt.setTextColor(R.color.black_color);
                holder.tv_second_country_txt.setTextColor(R.color.black_color);
                holder.tv_versus_txt.setTextColor(R.color.black_color);
            }
            holder.tv_ends_in_status_txt.setText("Status");
        } else {
            holder.tv_ends_in_status_txt.setText(R.string.ends_in_txt);
        }
        holder.tv_play_with_points_contest_adapter.setText(arrayList.get(position).contest_name);
        holder.tv_prize_pool_contest_adapter.setText(arrayList.get(position).price);
        str_imagepath = str_local_host + arrayList.get(position).categories_image;

//        Log.e("str_imagepath_adapter", str_imagepath);
        Glide.with(mContext).load(str_imagepath)
                .thumbnail(Glide.with(mContext).load(str_imagepath))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.iv_categoires_image);
        final String t2 = arrayList.get(position).end_date_time;
        stringArrayList.add(t2);
//        Log.e("t22222subvv", t2);
//        Log.e("t22222sub", t2.substring(11, 19));
        Collections.sort(stringArrayList);
//        Log.e("stringArrayList", stringArrayList.toString());

        /*Getting Current Time*/
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strDate = mdformat.format(calendar.getTime());


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date current_system_time = mdformat.parse(strDate);
            Date end_date_api = sdf.parse(t2);
//            Log.e("end_date_api", String.valueOf(end_date_api));
//            Log.e("parse_cur_system_time", String.valueOf(current_system_time));
            different_milli_seconds = end_date_api.getTime() - current_system_time.getTime();
//            Log.e("different_milli_seconds", String.valueOf(different_milli_seconds));
            if (different_milli_seconds < 0) {
                if (str_status.equalsIgnoreCase("2")) {
                    holder.tv_end_time_contest_adapter.setText("Played");
                } else {
                    holder.tv_end_time_contest_adapter.setText("Finished");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.e("star_status", str_status);
        if (str_status.equalsIgnoreCase("2")) {
            holder.tv_end_time_contest_adapter.setText("Played");
        } else {
            holder.countDownTimer = new CountDownTimer(different_milli_seconds, 1000) {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public void onTick(long millisUntilFinished) {
                    lng_seconds = millisUntilFinished;
                    @SuppressLint("DefaultLocale") String s1 = String.format("%2d", lng_seconds).trim();
                    @SuppressLint("DefaultLocale") String ss = String.format("%02d:%02d:%02d", lng_seconds / 60, lng_seconds % 60, 0);

                    hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    //System.out.println("HH_MM_SS:: " + hms);

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    elapsedDays = lng_seconds / daysInMilli;
                    lng_seconds = lng_seconds % daysInMilli;

                    if (elapsedDays == 1) {
                        holder.tv_end_time_contest_adapter.setText(elapsedDays + " day left");
                    } else if (elapsedDays > 1) {
                        holder.tv_end_time_contest_adapter.setText(elapsedDays + " days left");
                    } else {
                        holder.tv_end_time_contest_adapter.setText(hms);
                    }
//                    Log.e("End_TImer7", holder.tv_end_time_contest_adapter.getText().toString());
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }


//        Log.e("End_TImer7_out_side", holder.tv_end_time_contest_adapter.getText().toString());
        String prize_type = arrayList.get(position).prize_type;
//        Log.e("str_prize_type", prize_type);
        if (prize_type.equalsIgnoreCase("Coin")) {
            holder.tv_entry_fee_details_coin_icon.setBackground(mContext.getResources().getDrawable(R.drawable.coin_new));
            holder.tv_prize_details_coin_icon.setBackground(mContext.getResources().getDrawable(R.drawable.coin_new));
        } else if (prize_type.equalsIgnoreCase("Cash") || prize_type.equalsIgnoreCase("Fee")) {
            holder.tv_entry_fee_details_coin_icon.setBackground(mContext.getResources().getDrawable(R.drawable.rupee_indian));
            holder.tv_prize_details_coin_icon.setBackground(mContext.getResources().getDrawable(R.drawable.rupee_indian));
        }
        fees_type = arrayList.get(position).fee_type;
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
                str_status = arrayList.get(position).play_status;
//                Log.e("str_statussss", str_status);
                str_correct_ans = arrayList.get(position).correct_mark;
                str_wrong_ans = arrayList.get(position).wrong_mark;
                str_skip = arrayList.get(position).skip;
                int s1 = holder.getAdapterPosition();
                str_categories = arrayList.get(position).categories;
                str_question_type = arrayList.get(position).question_type;
                str_imagepath = arrayList.get(position).categories_image;
                str_team_a_path = arrayList.get(position).team_a;
                str_team_b_path = arrayList.get(position).team_b;
                str_rule_id = arrayList.get(position).rules_id;
                str_status_onclick = arrayList.get(position).play_status;
                str_end_game = arrayList.get(position).end_date_time;
                str_start_game = arrayList.get(position).start_date_time;
                str_team_a_path_name_txt = arrayList.get(position).team_a_name;
                str_team_b_path_name_txt = arrayList.get(position).team_b_name;
                String prize_type = arrayList.get(position).prize_type;


                String str_contest_name = arrayList.get(position).contest_name;
                str_contest_id = arrayList.get(position).contest_id;

                str_user_rank = arrayList.get(position).user_rank;
                str_total_onclick_answer_values = arrayList.get(position).total_onclick_answer_values;
                str_winning_prize = arrayList.get(position).winning_prize;


                Log.e("str_contest_idddd", str_contest_id);
                if (str_status.equalsIgnoreCase("2")) {
                    /*Fragment fragment = new My_Contest_Fragment();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();*/

                    Intent intent1 = new Intent(mContext, My_Contest_Live.class);
                    intent1.putExtra("contest_id_live", str_contest_id);
                    intent1.putExtra("contest_name_live", str_contest_name);

                    intent1.putExtra("rank_live", str_user_rank);
                    intent1.putExtra("winnings_live", str_winning_prize);
                    intent1.putExtra("ponits_live", str_total_onclick_answer_values);

                    intent1.putExtra("rule_id_live", str_rule_id);
                    intent1.putExtra("categories_live", str_categories);
                    intent1.putExtra("categories_image_live", str_imagepath);
                    intent1.putExtra("str_team_a_path_live", str_team_a_path);
                    intent1.putExtra("str_team_b_path_live", str_team_b_path);
                    intent1.putExtra("str_team_a_path_name_txt_live", str_team_a_path_name_txt);
                    intent1.putExtra("str_team_b_path_name_txt_live", str_team_b_path_name_txt);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(intent1);
                } else {
                    /*
                     *
                     * 0-->Text
                     * 1-->Image
                     * 2-->Audio
                     * 3-->Prediction
                     * 4-->STB
                     *
                     * */
                    /*if (str_categories.equalsIgnoreCase("Trivia_Image")
                            || str_categories.equalsIgnoreCase("Trivia")
                            || str_categories.equalsIgnoreCase("Prediction")) {*/
                    if (str_question_type.equals("1")
                            || str_question_type.equals("0")
                            || str_question_type.equals("3")) {
                        //  Toast.makeText(mContext, "Rule ID :" + " " + str_rule_id, Toast.LENGTH_SHORT).show();
                        String fees_type = arrayList.get(position).fee_type;
                        if (fees_type.equalsIgnoreCase("0")) {
                            holder.tv_entry_fee_details.setText("Free");
                        } else {
                            holder.tv_entry_fee_details.setText(arrayList.get(position).entry_fee);
                        }

                        Intent intent1 = new Intent(mContext, Game_Two_Act.class);
                        SessionSave.ClearSession("ScoreBoard_Value", mContext);
                        SessionSave.ClearSession("ScoreBoard_User_Rank_Value", mContext);
                        str_seconds = arrayList.get(position).seconds;
                        str_2x_powerup = arrayList.get(position).powerup_count;
                        str_contest_id = arrayList.get(position).contest_id;
                        str_entry_fees = holder.tv_entry_fee_details.getText().toString();
                        intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                        intent1.putExtra("game_name", holder.tv_play_with_points_contest_adapter.getText().toString());
                        intent1.putExtra("prize_amount", holder.tv_prize_pool_contest_adapter.getText().toString());
                        intent1.putExtra("end_time", str_end_game);
                        intent1.putExtra("start_time", str_start_game);
                        intent1.putExtra("count_down_seconds", str_seconds);
                        intent1.putExtra("str_2x_powerup", str_2x_powerup);
                        intent1.putExtra("str_contest_id", str_contest_id);
                        intent1.putExtra("str_entry_fees", str_entry_fees);
                        intent1.putExtra("str_rule_id", str_rule_id);
                        intent1.putExtra("str_status_onclick", str_status_onclick);
                        intent1.putExtra("str_imagepath", str_imagepath);
                        intent1.putExtra("str_team_a_path", str_team_a_path);
                        intent1.putExtra("str_team_b_path", str_team_b_path);
                        intent1.putExtra("str_team_a_path_name_txt", str_team_a_path_name_txt);
                        intent1.putExtra("str_team_b_path_name_txt", str_team_b_path_name_txt);
                        intent1.putExtra("str_categories", str_categories);
                        intent1.putExtra("str_question_type", str_question_type);

                        intent1.putExtra("str_correct_ans", str_correct_ans);
                        intent1.putExtra("str_wrong_ans", str_wrong_ans);
                        intent1.putExtra("str_skip", str_skip);
                        intent1.putExtra("prize_type", prize_type);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent1);

                   /* if (str_status.equals("2")) {
                        Intent intent = new Intent(mContext, My_Contest_Live.class);
                        mContext.startActivity(intent);
//                        My_Contest_Fragment my_contest_fragment = new My_Contest_Fragment();
//                        FragmentManager fragmentManager = mContext.getsu;
//                        fragmentManager.beginTransaction().replace(R.id.content_frame, my_contest_fragment).commit();
                    }*/

                        /*
                         *
                         * 0-->Text
                         * 1-->Image
                         * 2-->Audio
                         * 3-->Prediction
                         * 4-->STB
                         *
                         * */
//                    } else if (str_categories.equalsIgnoreCase("Spot the ball")) {
                    } else if (str_question_type.equals("4")) {
                        fees_type = arrayList.get(position).fee_type;
                        if (fees_type.equalsIgnoreCase("0")) {
                            holder.tv_entry_fee_details.setText("Free");
                        } else {
                            holder.tv_entry_fee_details.setText(arrayList.get(position).entry_fee);
                        }
                        str_seconds = arrayList.get(position).seconds;
                        str_2x_powerup = arrayList.get(position).powerup_count;
                        str_contest_id = arrayList.get(position).contest_id;
                        str_entry_fees = holder.tv_entry_fee_details.getText().toString();
//                        Log.e("str_secondsdsds", str_seconds);
                        Intent intent1 = new Intent(mContext, Game_Details_Screen_Act.class);
                        intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                        intent1.putExtra("game_name", holder.tv_play_with_points_contest_adapter.getText().toString());
                        intent1.putExtra("prize_amount", holder.tv_prize_pool_contest_adapter.getText().toString());
                        intent1.putExtra("end_time", str_end_game);
                        intent1.putExtra("count_down_seconds", str_seconds);
                        intent1.putExtra("str_2x_powerup", str_2x_powerup);
                        intent1.putExtra("str_contest_id", str_contest_id);
                        intent1.putExtra("str_entry_fees", str_entry_fees);
                        intent1.putExtra("str_rule_id", str_rule_id);
                        intent1.putExtra("str_status_onclick", str_status_onclick);
                        intent1.putExtra("str_imagepath", str_imagepath);
                        intent1.putExtra("str_team_a_path", str_team_a_path);
                        intent1.putExtra("str_team_b_path", str_team_b_path);
                        intent1.putExtra("str_team_a_path_name_txt", str_team_a_path_name_txt);
                        intent1.putExtra("str_team_b_path_name_txt", str_team_b_path_name_txt);
                        intent1.putExtra("str_categories", str_categories);
                        intent1.putExtra("str_question_type", str_question_type);

                        intent1.putExtra("str_correct_ans", str_correct_ans);
                        intent1.putExtra("str_wrong_ans", str_wrong_ans);
                        intent1.putExtra("str_skip", str_skip);
                        intent1.putExtra("prize_type", prize_type);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent1);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
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
        TextView tv_play_with_points_contest_adapter,
                tv_prize_pool_contest_adapter,
                tv_end_time_contest_adapter,
                tv_entry_fee_details,
                tv_prize_details_coin_icon,
                tv_entry_fee_details_coin_icon;

        ConstraintLayout constraintLayout_constest_layout,
                constraintLayout_play_with_points_contest_adapter,
                constraintLayout_country_flag_layout;

        CountDownTimer countDownTimer;

        ImageView iv_categoires_image,
                iv_first_country_flag,
                iv_second_country_flag;

        TextView tv_first_country_txt,
                tv_second_country_txt,
                tv_ends_in_status_txt,
                tv_versus_txt;

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

            tv_versus_txt = itemView.findViewById(R.id.tv_versus_txt);
        }
    }
}
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
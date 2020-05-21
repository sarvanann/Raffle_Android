package com.spot_the_ballgame.Adapter;/*
package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spot_the_ballgame.Game_Details_Screen_Act;
import com.spot_the_ballgame.Game_Two_Act;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Carousel_Model;
import com.spot_the_ballgame.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

*/
/**
 * Created by Ravi Tamada on 18/05/16.
 *//*

public class AlbumsAdapter_Date_Contest extends RecyclerView.Adapter<AlbumsAdapter_Date_Contest.MyViewHolder> {
    private Context mContext;
    private ArrayList<Carousel_Model.Date_Contest> albumList;


    private String str_rule_id;
    private String str_status_onclick;
    private String str_end_game;
    private ArrayList<String> stringArrayList = new ArrayList<>();

    private String str_imagepath;
    private String str_correct_ans, str_wrong_ans, str_skip;
    private long lng_seconds;
    private long different_milli_seconds;
    private String str_seconds, str_2x_powerup, str_contest_id, str_entry_fees;
    private String str_categories;
    String str_team_a_path;
    String str_local_host;


    public AlbumsAdapter_Date_Contest(FragmentActivity activity, ArrayList<Carousel_Model.Date_Contest> date_contest) {
        this.mContext = activity;
        this.albumList = date_contest;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        str_imagepath = str_local_host + albumList.get(position).image_url;
        Log.e("str_imagepathablbmdate", str_imagepath);
        Glide.with(mContext).load(str_imagepath)
                .thumbnail(Glide.with(mContext).load(str_imagepath))
                .into(holder.thumbnail);




        */
/*Glide.with(mContext).load(str_team_a_path)
                .thumbnail(Glide.with(mContext).load(str_team_a_path))
                .into(holder.thumbnail);
*//*


        String str_status = albumList.get(position).play_status;
        str_categories = albumList.get(position).categories;
        Log.e("str_categories_carrdate", str_categories);

        Log.e("outside", str_status);
        final String t2 = albumList.get(position).end_date_time;
        stringArrayList.add(t2);
        Log.e("t22222subvvdate", t2);
        Log.e("t22222subdate", t2.substring(11, 19));
        Collections.sort(stringArrayList);
        Log.e("stringArrayListdate", stringArrayList.toString());

        */
/*Getting Current Time*//*

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strDate = mdformat.format(calendar.getTime());


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date current_system_time = mdformat.parse(strDate);
            Date end_date_api = sdf.parse(t2);
            Log.e("end_date_apidate", String.valueOf(end_date_api));
            Log.e("parse_cur_system_timedate", String.valueOf(current_system_time));
            different_milli_seconds = end_date_api.getTime() - current_system_time.getTime();
            Log.e("different_milli_secondsdate", String.valueOf(different_milli_seconds));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String fees_type = albumList.get(position).fee_type;

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_correct_ans = albumList.get(position).correct_mark;
                str_wrong_ans = albumList.get(position).wrong_mark;
                str_skip = albumList.get(position).skip;
                int s1 = holder.getAdapterPosition();
                str_categories = albumList.get(position).categories;
                str_imagepath = albumList.get(position).categories_image;
                str_rule_id = albumList.get(position).rules_id;
                str_status_onclick = albumList.get(position).play_status;
                str_end_game = albumList.get(position).end_date_time;
                Log.e("str_status_onclickdate", str_status_onclick);
                Log.e("str_rule_iddate", str_rule_id);
                Log.e("rules_namedate", albumList.get(position).rules_name);
                Log.e("end_date_timedate", albumList.get(position).end_date_time);
                if (str_categories.equalsIgnoreCase("Prediction") || str_categories.equalsIgnoreCase("Trivia")) {
                    //  Toast.makeText(mContext, "Rule ID :" + " " + str_rule_id, Toast.LENGTH_SHORT).show();
                    String fees_type = albumList.get(position).fee_type;
                    if (fees_type.equalsIgnoreCase("0")) {
                        str_entry_fees = "Free";
                    } else {
                        str_entry_fees = albumList.get(position).entry_fee;
                    }
                    Intent intent1 = new Intent(mContext, Game_Two_Act.class);
                    str_seconds = albumList.get(position).seconds;
                    str_2x_powerup = albumList.get(position).powerup_count;
                    str_contest_id = albumList.get(position).contest_id;
                    intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                    intent1.putExtra("game_name", albumList.get(position).contest_name);
                    intent1.putExtra("prize_amount", albumList.get(position).price);
                    intent1.putExtra("end_time", str_end_game);
                    intent1.putExtra("count_down_seconds", str_seconds);
                    intent1.putExtra("str_2x_powerup", str_2x_powerup);
                    intent1.putExtra("str_contest_id", str_contest_id);
                    intent1.putExtra("str_entry_fees", str_entry_fees);
                    intent1.putExtra("str_rule_id", str_rule_id);
                    intent1.putExtra("str_status_onclick", str_status_onclick);
                    intent1.putExtra("str_imagepath", str_imagepath);
                    intent1.putExtra("str_categories", str_categories);

                    intent1.putExtra("str_correct_ans", str_correct_ans);
                    intent1.putExtra("str_wrong_ans", str_wrong_ans);
                    intent1.putExtra("str_skip", str_skip);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent1);
                }
                if (str_categories.equalsIgnoreCase("Spot the ball")) {
                    String fees_type = albumList.get(position).fee_type;
                    if (fees_type.equalsIgnoreCase("0")) {
                        str_entry_fees = "Free";
                    } else {
                        str_entry_fees = albumList.get(position).entry_fee;
                    }
                    str_seconds = albumList.get(position).seconds;
                    str_2x_powerup = albumList.get(position).powerup_count;
                    str_contest_id = albumList.get(position).contest_id;
                    Log.e("str_secondsdsdsdate", str_seconds);
                    Intent intent1 = new Intent(mContext, Game_Details_Screen_Act.class);
                    intent1.putExtra("game_name", albumList.get(position).contest_name);
                    intent1.putExtra("prize_amount", albumList.get(position).price);
                    intent1.putExtra("end_time", str_end_game);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
}
*/

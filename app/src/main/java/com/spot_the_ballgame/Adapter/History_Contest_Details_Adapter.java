package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.My_Contest_History;
import com.spot_the_ballgame.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class History_Contest_Details_Adapter extends RecyclerView.Adapter<History_Contest_Details_Adapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    Context mContext;


    public History_Contest_Details_Adapter(FragmentActivity activity, ArrayList<Category_Model.Data> data) {
        this.arrayList = data;
        this.mContext = activity;
    }

    @NonNull
    @Override
    public History_Contest_Details_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.history_contest_details_layout, parent, false);
        return new History_Contest_Details_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull History_Contest_Details_Adapter.ViewHolder holder, int position) {
        Log.e("winning_prize", arrayList.get(position).winning_prize);
        holder.tv_rank_history_details.setText(arrayList.get(position).user_rank);
        holder.tv_contest_title_history_details.setText(arrayList.get(position).contest_name);
        holder.tv_points_history_details.setText(arrayList.get(position).total_onclick_answer_values);
        holder.tv_winnings_history_details.setText(arrayList.get(position).winning_prize);
//        holder.tv_date_history_details.setText(arrayList.get(position).start_date_time.substring(0, 11));

        String sss = arrayList.get(position).start_date_time.substring(0, 11);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        String targetdatevalue;
        try {
            sourceDate = dateFormat.parse(sss);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("MM/dd/yy");
        targetdatevalue = targetFormat.format(sourceDate);
        holder.tv_date_history_details.setText(targetdatevalue);
        holder.constraintLayout_game_1_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_contest_id = arrayList.get(position).contest_id;
                String str_rank = arrayList.get(position).user_rank;
                String str_winnings = arrayList.get(position).winning_prize;
                String str_rule_id = arrayList.get(position).rules_id;
                String str_contest_name = arrayList.get(position).contest_name;
                String str_points = arrayList.get(position).total_onclick_answer_values;
                String str_date = holder.tv_date_history_details.getText().toString();
                Intent intent = new Intent(mContext, My_Contest_History.class);
                intent.putExtra("contest_id_history", str_contest_id);
                intent.putExtra("contest_name_history", str_contest_name);
                intent.putExtra("rank_history", str_rank);
                intent.putExtra("winnings_history", str_winnings);
                intent.putExtra("date_history", str_date);
                intent.putExtra("rule_id_history", str_rule_id);
                intent.putExtra("points_history", str_points);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);
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
        TextView tv_contest_title_history_details,
                tv_rank_history_details,
                tv_points_history_details,
                tv_winnings_history_details,
                iv_first_country_flag_history_contest,
                tv_first_country_history_contest,
                iv_second_country_flag_history_contest,
                tv_date_history_details,
                tv_second_country_history_contest;
        ImageView iv_categoires_image_history_details;
        ConstraintLayout constraintLayout_country_flag_history_contest_details_layout, constraintLayout_game_1_history;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_contest_title_history_details = itemView.findViewById(R.id.tv_contest_title_history_details);
            tv_rank_history_details = itemView.findViewById(R.id.tv_rank_history_details);
            tv_points_history_details = itemView.findViewById(R.id.tv_points_history_details);
            tv_date_history_details = itemView.findViewById(R.id.tv_date_history_details);
            tv_winnings_history_details = itemView.findViewById(R.id.tv_winnings_history_details);
            iv_first_country_flag_history_contest = itemView.findViewById(R.id.iv_first_country_flag_history_contest);
            tv_first_country_history_contest = itemView.findViewById(R.id.tv_first_country_history_contest);
            iv_second_country_flag_history_contest = itemView.findViewById(R.id.iv_second_country_flag_history_contest);
            tv_second_country_history_contest = itemView.findViewById(R.id.tv_second_country_history_contest);
            iv_categoires_image_history_details = itemView.findViewById(R.id.iv_categoires_image_history_details);
            constraintLayout_game_1_history = itemView.findViewById(R.id.constraintLayout_game_1_history);
            constraintLayout_country_flag_history_contest_details_layout = itemView.findViewById(R.id.constraintLayout_country_flag_history_contest_details_layout);

        }
    }
}


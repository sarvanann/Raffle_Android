package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.My_Contest_Live;
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Live_Contest_Details_Adapter extends RecyclerView.Adapter<Live_Contest_Details_Adapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    private Context mContext;
    private String str_local_host, str_team_a_image_path, str_team_b_image_path, str_image_path;
    private String str_categories, str_team_a_path, str_team_b_path, str_team_a_path_name_txt, str_team_b_path_name_txt;

    public Live_Contest_Details_Adapter(FragmentActivity activity, ArrayList<Category_Model.Data> data) {
        this.arrayList = data;
        this.mContext = activity;
    }

    @NonNull
    @Override
    public Live_Contest_Details_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.live_details_layout, parent, false);
        return new Live_Contest_Details_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Live_Contest_Details_Adapter.ViewHolder holder, int position) {
        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        holder.tv_contest_title_live_details.setText(arrayList.get(position).contest_name);
        holder.tv_current_rank_live_details.setText(arrayList.get(position).user_rank);
        holder.tv_your_points_live_details.setText(arrayList.get(position).total_onclick_answer_values);
        holder.tv_possible_winnings_live_details.setText(arrayList.get(position).winning_prize);



        str_categories = arrayList.get(position).categories;
        if (str_categories.equalsIgnoreCase("Prediction")) {
            holder.constraintLayout_country_flag_live_details_layout.setVisibility(View.VISIBLE);
            holder.tv_contest_title_live_details.setVisibility(View.GONE);

            str_team_a_path = str_local_host + arrayList.get(position).team_a;
            str_team_b_path = str_local_host + arrayList.get(position).team_b;
//            Log.e("str_local_host", str_team_a_path);
//            Log.e("str_local_host_2", str_team_b_path);
            Glide.with(mContext).load(str_team_a_path)
                    .thumbnail(Glide.with(mContext).load(str_team_a_path))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_first_country_flag_live_details);

            Glide.with(mContext).load(str_team_b_path)
                    .thumbnail(Glide.with(mContext).load(str_team_b_path))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_second_country_flag_live_details);

            holder.tv_first_country_live_details.setText(arrayList.get(position).team_a_name.toUpperCase());
            holder.tv_second_country_live_details.setText(arrayList.get(position).team_b_name.toUpperCase());
        } else {
            holder.constraintLayout_country_flag_live_details_layout.setVisibility(View.GONE);
            holder.tv_contest_title_live_details.setVisibility(View.VISIBLE);
        }


        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        str_image_path = str_local_host + arrayList.get(position).categories_image;
//        Log.e("str_image_path_live", str_image_path);
        Glide.with(mContext).load(str_image_path)
                .thumbnail(Glide.with(mContext).load(str_image_path))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.iv_categoires_image_live_details);

        holder.constraintLayout_game_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_rule_id = arrayList.get(position).rules_id;
                String str_contest_name = arrayList.get(position).contest_name;
                String str_user_rank = arrayList.get(position).user_rank;
                String str_winning_prize = arrayList.get(position).winning_prize;
                String str_total_onclick_answer_values = arrayList.get(position).total_onclick_answer_values;
                String str_contest_id = arrayList.get(position).contest_id;
                str_categories = arrayList.get(position).categories;
                str_image_path = arrayList.get(position).categories_image;
                str_team_a_path = arrayList.get(position).team_a;
                str_team_b_path = arrayList.get(position).team_b;
                str_team_a_path_name_txt = arrayList.get(position).team_a_name;
                str_team_b_path_name_txt = arrayList.get(position).team_b_name;
//                Log.e("str_rule_id", str_rule_id);
//                Log.e("str_contest_name", str_contest_name);
//                Log.e("str_user_rank", str_user_rank);
//                Log.e("str_winning_prize", str_winning_prize);
//                Log.e("str_total_onclick_answer_values", str_total_onclick_answer_values);
//                Log.e("str_contest_id", str_contest_id);
//                Log.e("str_categories", str_categories);

                Intent intent = new Intent(mContext, My_Contest_Live.class);
                intent.putExtra("contest_id_live", str_contest_id);
                intent.putExtra("contest_name_live", str_contest_name);
                intent.putExtra("rank_live", str_user_rank);
                intent.putExtra("winnings_live", str_winning_prize);
                intent.putExtra("ponits_live", str_total_onclick_answer_values);
                intent.putExtra("rule_id_live", str_rule_id);
                intent.putExtra("categories_live", str_categories);
                intent.putExtra("categories_image_live", str_image_path);
                intent.putExtra("str_team_a_path_live", str_team_a_path);
                intent.putExtra("str_team_b_path_live", str_team_b_path);
                intent.putExtra("str_team_a_path_name_txt_live", str_team_a_path_name_txt);
                intent.putExtra("str_team_b_path_name_txt_live", str_team_b_path_name_txt);

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
        TextView tv_contest_title_live_details,
                tv_current_rank_live_details,
                tv_your_points_live_details,
                tv_possible_winnings_live_details,
                tv_first_country_live_details,
                tv_second_country_live_details,
                tv_first_country_txt,
                tv_second_country_txt;
        ImageView iv_categoires_image_live_details, iv_first_country_flag_live_details,
                iv_second_country_flag_live_details;
        ConstraintLayout constraintLayout_country_flag_live_details_layout, constraintLayout_game_11;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_contest_title_live_details = itemView.findViewById(R.id.tv_contest_title_live_details);
            tv_current_rank_live_details = itemView.findViewById(R.id.tv_current_rank_live_details);
            tv_your_points_live_details = itemView.findViewById(R.id.tv_your_points_live_details);
            tv_possible_winnings_live_details = itemView.findViewById(R.id.tv_possible_winnings_live_details);
            iv_first_country_flag_live_details = itemView.findViewById(R.id.iv_first_country_flag_live_details);
            tv_first_country_live_details = itemView.findViewById(R.id.tv_first_country_live_details);
            iv_second_country_flag_live_details = itemView.findViewById(R.id.iv_second_country_flag_live_details);
            tv_second_country_live_details = itemView.findViewById(R.id.tv_second_country_live_details);
            iv_categoires_image_live_details = itemView.findViewById(R.id.iv_categoires_image_live_details);
            constraintLayout_country_flag_live_details_layout = itemView.findViewById(R.id.constraintLayout_country_flag_live_details_layout);
            constraintLayout_game_11 = itemView.findViewById(R.id.constraintLayout_game_11);


        }
    }
}

package com.spot_the_ballgame.Adapter;

import android.content.Context;
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
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Live_Contest_Details_Adapter extends RecyclerView.Adapter<Live_Contest_Details_Adapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    Context mContext;


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
        holder.tv_contest_title_live_details.setText(arrayList.get(position).contest_name);
        holder.tv_current_rank_live_details.setText(arrayList.get(position).user_rank);
        holder.tv_your_points_live_details.setText(arrayList.get(position).total_onclick_answer_values);
        holder.tv_possible_winnings_live_details.setText(arrayList.get(position).winning_prize);
        holder.constraintLayout_game_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Game_Two_Act.class);
//                mContext.startActivity(intent);
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
                iv_first_country_flag_live_details,
                tv_first_country_live_details,
                iv_second_country_flag_live_details,
                tv_second_country_live_details;
        ImageView iv_categoires_image_live_details;
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

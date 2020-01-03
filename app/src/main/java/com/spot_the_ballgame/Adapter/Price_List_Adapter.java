package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Game_Details_Screen_Act;
import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Price_List_Adapter extends RecyclerView.Adapter<Price_List_Adapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    private Context context;
    private String str_prize_amount, str_rank;

    public Price_List_Adapter(Game_Details_Screen_Act game_details_screen_act, String prize_amount, String rank) {
        this.context = game_details_screen_act;
        this.str_prize_amount = prize_amount;
        this.str_rank = rank;

    }

    @NonNull
    @Override
    public Price_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.prize_list_adapter_details_layout, parent, false);
        return new Price_List_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Price_List_Adapter.ViewHolder holder, int position) {
        holder.tv_rank_in_prize_list.setText(str_rank);
        holder.tv_prize_amount_prize_list.setText(str_prize_amount);
    }

    @Override
    public int getItemCount() {
        return str_rank.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_prize_amount_prize_list, tv_rank_in_prize_list;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_prize_amount_prize_list = itemView.findViewById(R.id.tv_prize_amount_prize_list);
            tv_rank_in_prize_list = itemView.findViewById(R.id.tv_rank_in_prize_list);
        }
    }
}

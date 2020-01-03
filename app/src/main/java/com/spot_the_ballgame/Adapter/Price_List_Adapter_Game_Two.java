package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Game_Two_Act;
import com.spot_the_ballgame.Model.Winnings_Model;
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Price_List_Adapter_Game_Two extends RecyclerView.Adapter<Price_List_Adapter_Game_Two.ViewHolder> {
    private ArrayList<Winnings_Model> arrayList;
    private Context context;
    private String str_prize_amount, str_rank;

    public Price_List_Adapter_Game_Two(Game_Two_Act game_two_act, ArrayList<Winnings_Model> toString) {
        this.context = game_two_act;
        this.arrayList = toString;
    }

    public Price_List_Adapter_Game_Two(Game_Two_Act game_two_act, String prize_amount, String rank) {
        this.context = game_two_act;
        this.str_prize_amount = prize_amount;
        this.str_rank = rank;

    }


    @NonNull
    @Override
    public Price_List_Adapter_Game_Two.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.prize_list_adapter_details_layout, parent, false);
        return new Price_List_Adapter_Game_Two.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Price_List_Adapter_Game_Two.ViewHolder holder, int position) {
        Log.e("rankkk",arrayList.get(position).getRank());
        holder.tv_rank_in_prize_list.setText(arrayList.get(position).getRank());
        holder.tv_prize_amount_prize_list.setText(arrayList.get(position).getPrize_amount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_prize_amount_prize_list, tv_rank_in_prize_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_prize_amount_prize_list = itemView.findViewById(R.id.tv_prize_amount_prize_list);
            tv_rank_in_prize_list = itemView.findViewById(R.id.tv_rank_in_prize_list);
        }
    }
}

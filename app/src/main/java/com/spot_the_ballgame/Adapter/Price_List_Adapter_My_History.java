package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Model.Winnings_Model;
import com.spot_the_ballgame.My_Contest_History;
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Price_List_Adapter_My_History extends RecyclerView.Adapter<Price_List_Adapter_My_History.ViewHolder> {
    private ArrayList<Winnings_Model> arrayList;
    private Context context;
    private String str_prize_amount, str_rank;


    public Price_List_Adapter_My_History(My_Contest_History my_contest_history, ArrayList<Winnings_Model> winnings_model) {
        this.context = my_contest_history;
        this.arrayList = winnings_model;
    }


    @NonNull
    @Override
    public Price_List_Adapter_My_History.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.prize_list_adapter_details_layout, parent, false);
        return new Price_List_Adapter_My_History.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Price_List_Adapter_My_History.ViewHolder holder, int position) {
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
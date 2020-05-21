package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Game_Details_Screen_Act;
import com.spot_the_ballgame.Model.Winnings_Model;
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Price_List_Adapter extends RecyclerView.Adapter<Price_List_Adapter.ViewHolder> {
    private ArrayList<Winnings_Model> arrayList;
    private Context context;

    public Price_List_Adapter(Game_Details_Screen_Act game_details_screen_act, ArrayList<Winnings_Model> winning_arrayList) {
        this.context = game_details_screen_act;
        this.arrayList = winning_arrayList;
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
//        Log.e("rankkk", arrayList.get(position).getRank_short());
//        holder.tv_rank_in_prize_list.setText(arrayList.get(position).getRank());
        holder.tv_rank_in_prize_list.setText(arrayList.get(position).getRank_short());
        holder.tv_prize_amount_prize_list.setText(arrayList.get(position).getPrize_amount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

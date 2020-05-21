package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
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

public class Ranking_List_Adapter extends RecyclerView.Adapter<Ranking_List_Adapter.ViewHolder> {
    Context context;
    private ArrayList<Category_Model.Data> arrayList;

    public Ranking_List_Adapter(Game_Details_Screen_Act game_details_screen_act, ArrayList<Category_Model.Data> data) {
        this.context = game_details_screen_act;
        this.arrayList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.ranking_list_adapter_details_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*String s = arrayList.get(position).first_name;
        String[] parts = s.split("@");
        String s1 = parts[0];*/
        holder.tv_rank_user_name.setText(arrayList.get(position).first_name);
        holder.tv_user_rank.setText(position + 1 + "\n Rank");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_rank, tv_rank_user_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_rank = itemView.findViewById(R.id.tv_user_rank);
            tv_rank_user_name = itemView.findViewById(R.id.tv_rank_user_name);
        }
    }
}

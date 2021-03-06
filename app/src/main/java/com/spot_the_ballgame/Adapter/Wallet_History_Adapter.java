package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.R;
import com.spot_the_ballgame.Transaction_Act;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Wallet_History_Adapter extends RecyclerView.Adapter<Wallet_History_Adapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    private Context context;

    public Wallet_History_Adapter(Transaction_Act transaction_act, ArrayList<Category_Model.Data> data) {
        this.arrayList = data;
        this.context = transaction_act;
    }

    @NonNull
    @Override
    public Wallet_History_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.wallet_history_adapter_layout, parent, false);
        return new Wallet_History_Adapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Wallet_History_Adapter.ViewHolder holder, int position) {
        String str_date = arrayList.get(position).date;
        String str_time = arrayList.get(position).time;
        String str_playby = arrayList.get(position).playby;
        String str_values_in = arrayList.get(position).values_in;
        String str_values_out = arrayList.get(position).values_out;
//        Log.e("str_date", str_date);
//        Log.e("str_playby_wlt_hist", str_playby);
        if (str_values_in != null) {
//            Log.e("str_values_in", str_values_in);
        }
        if (str_values_out != null) {
//            Log.e("str_values_out", str_values_out);
        }


        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");
        Date date = null;
        try {
            date = inputFormat.parse(str_date);
            String outputDateStr = outputFormat.format(date);
            holder.tv_date_txt.setText(outputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_play_via.setText(str_playby);

        if (arrayList.get(position).playby.equalsIgnoreCase("Coins")) {
            if (str_values_in.equals("0")
                    || str_values_out.equals("0")
                    || str_values_in.equalsIgnoreCase("Free")
                    || str_values_out.equalsIgnoreCase("Free")) {
                holder.tv_negative_points_values.setVisibility(View.GONE);
                holder.tv_plus_icon.setVisibility(View.GONE);
                holder.tv_points_values.setVisibility(View.VISIBLE);
                holder.tv_plus_icon.setText("");
                holder.tv_points_values.setText("Free");
                holder.tv_plus_icon.setTextColor(context.getResources().getColor(R.color.button_pressed));
                holder.tv_points_values.setTextColor(context.getResources().getColor(R.color.button_pressed));
            } else {
                holder.tv_negative_points_values.setVisibility(View.GONE);
                holder.tv_plus_icon.setVisibility(View.VISIBLE);
                holder.tv_points_values.setVisibility(View.VISIBLE);
                holder.tv_plus_icon.setText(" - ");
                holder.tv_points_values.setText(str_values_out);
                holder.tv_plus_icon.setTextColor(context.getResources().getColor(R.color.new_ent_time_txt_color));
                holder.tv_points_values.setTextColor(context.getResources().getColor(R.color.new_ent_time_txt_color));
            }
        }

        if (arrayList.get(position).playby.equalsIgnoreCase("Ads")) {
            holder.tv_points_values.setVisibility(View.GONE);
            holder.tv_plus_icon.setVisibility(View.VISIBLE);
            holder.tv_negative_points_values.setVisibility(View.VISIBLE);
            holder.tv_negative_points_values.setText(str_values_in);
            holder.tv_plus_icon.setText(" + ");
            holder.tv_negative_points_values.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
            holder.tv_plus_icon.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
        }


        if (arrayList.get(position).playby.equalsIgnoreCase("Referral Coins")
                || arrayList.get(position).playby.equalsIgnoreCase("Coin created for refer user")) {

            holder.tv_points_values.setVisibility(View.GONE);

            holder.tv_plus_icon.setVisibility(View.VISIBLE);
            holder.tv_play_via.setVisibility(View.VISIBLE);
            holder.tv_negative_points_values.setVisibility(View.VISIBLE);

            holder.tv_negative_points_values.setText(str_values_in);
            holder.tv_play_via.setText("Ref Bonus");
            holder.tv_plus_icon.setText(" + ");

            holder.tv_negative_points_values.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
            holder.tv_plus_icon.setTextColor(context.getResources().getColor(R.color.timer_bg_color));

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date_txt, tv_play_via, tv_points_values, tv_negative_points_values, tv_plus_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date_txt = itemView.findViewById(R.id.tv_date_txt);
            tv_play_via = itemView.findViewById(R.id.tv_play_via);
            tv_points_values = itemView.findViewById(R.id.tv_points_values_coins);
            tv_negative_points_values = itemView.findViewById(R.id.tv_negative_points_values);
            tv_plus_icon = itemView.findViewById(R.id.tv_plus_icon);
        }
    }
}

package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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

public class Redeem_History_Adapter extends RecyclerView.Adapter<Redeem_History_Adapter.ViewHolder> {
    private ArrayList<Category_Model.Data> arrayList;
    private Context context;
    private ArrayList<String> stringArrayList = new ArrayList<>();

    public Redeem_History_Adapter(Transaction_Act transaction_act, ArrayList<Category_Model.Data> data) {
        this.arrayList = data;
        this.context = transaction_act;
    }

    @NonNull
    @Override
    public Redeem_History_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.redeem_history_adapter_layout, parent, false);
        return new Redeem_History_Adapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Redeem_History_Adapter.ViewHolder holder, int position) {
        holder.tv_redeem_history_status.setText(arrayList.get(position).request_status);
        if (arrayList.get(position).request_status.equalsIgnoreCase("error")) {
            holder.tv_redeem_history_status.setTextColor(context.getResources().getColor(R.color.new_ent_time_txt_color));
        } else if (arrayList.get(position).request_status.equalsIgnoreCase("request")) {
            holder.tv_redeem_history_status.setTextColor(context.getResources().getColor(R.color.golden_yellow_color));
        } else if (arrayList.get(position).request_status.equalsIgnoreCase("pending")) {
            holder.tv_redeem_history_status.setTextColor(context.getResources().getColor(R.color.blue_color));
        } else if (arrayList.get(position).request_status.equalsIgnoreCase("success")) {
            holder.tv_redeem_history_status.setTextColor(context.getResources().getColor(R.color.green_color));
        } else if (arrayList.get(position).request_status.equalsIgnoreCase("declined")) {
            holder.tv_redeem_history_status.setTextColor(context.getResources().getColor(R.color.new_ent_time_txt_color));
        }



        holder.tv_redeem_history_amount.setText(arrayList.get(position).amount);
        holder.tv_redeem_history_points.setText(arrayList.get(position).coins);
//        holder.tv_redeem_history_date.setText(arrayList.get(position).created_at);
        String str_date = arrayList.get(position).created_at;

        final String t2 = arrayList.get(position).created_at;
        stringArrayList.add(t2);
        Log.e("t22222sub", t2);
        Log.e("t22222sub", t2.substring(11, 19));
        holder.tv_redeem_history_time.setText(t2.substring(11, 19));
        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");

        Date date = null;
        try {
            date = inputFormat.parse(str_date);
            String outputDateStr = outputFormat.format(date);
            holder.tv_redeem_history_date.setText(outputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*@SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String mytime = formatter.format(str_date);
        Log.e("myTime", mytime);
        Date time1 = null;
        try {
            time1 = formatter.parse(str_date);
            holder.tv_redeem_history_time.setText((CharSequence) time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_redeem_history_date, tv_redeem_history_time, tv_redeem_history_points, tv_redeem_history_amount, tv_redeem_history_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_redeem_history_date = itemView.findViewById(R.id.tv_redeem_history_date);
            tv_redeem_history_time = itemView.findViewById(R.id.tv_redeem_history_time);
            tv_redeem_history_points = itemView.findViewById(R.id.tv_redeem_history_points);
            tv_redeem_history_status = itemView.findViewById(R.id.tv_redeem_history_status);
            tv_redeem_history_amount = itemView.findViewById(R.id.tv_redeem_history_amount);
            tv_redeem_history_status = itemView.findViewById(R.id.tv_redeem_history_status);
        }
    }
}
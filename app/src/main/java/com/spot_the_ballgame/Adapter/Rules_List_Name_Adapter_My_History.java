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

import java.util.ArrayList;

public class Rules_List_Name_Adapter_My_History extends RecyclerView.Adapter<Rules_List_Name_Adapter_My_History.ViewHolder> {
    Context context;
    private ArrayList<Category_Model.Data> arrayList;
    private ArrayList<String[]> comma_arrayList = new ArrayList<>();

    public Rules_List_Name_Adapter_My_History(Context game_two_act, ArrayList<Category_Model.Data> data) {
        this.context = game_two_act;
        this.arrayList = data;
    }

    @NonNull
    @Override
    public Rules_List_Name_Adapter_My_History.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.rules_name_list_adapter_details_layout, parent, false);
        return new Rules_List_Name_Adapter_My_History.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Rules_List_Name_Adapter_My_History.ViewHolder holder, int position) {
        String s1 = arrayList.get(position).rules;
//        Log.e("s1_value", s1);
        String[] s2 = s1.split(",");
        comma_arrayList.add(s2);
//        Log.e("comma_arrayList", "" + s2.length);
//        Log.e("detail_comma_arrayList", "" + Arrays.toString(s2));
        for (int i = 0; i < s2.length; i++) {
//            Log.e("RULES_LIST", arrayList.get(position).rules);
//            Toast.makeText(context, "Length" + arrayList.get(position).rules, Toast.LENGTH_SHORT).show();
//            holder.tv_rules_serial_number.setText(String.valueOf(i));
//            Log.e("s2_of_i", "" + s2[i]);
//            Log.e("s2_of_length", "" + i);
            int n1 = i;
            int n2 = n1 + 1;
            holder.tv_rules_name.append(n2 + " ." + s2[i]);
            holder.tv_rules_name.append("\n");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rules_name, tv_rules_serial_number;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rules_name = itemView.findViewById(R.id.tv_rules_name);
            tv_rules_serial_number = itemView.findViewById(R.id.tv_rules_serial_number);
        }
    }
}
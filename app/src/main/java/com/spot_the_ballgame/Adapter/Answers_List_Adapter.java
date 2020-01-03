package com.spot_the_ballgame.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spot_the_ballgame.Model.Category_Model;
import com.spot_the_ballgame.My_Contest_History;
import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class Answers_List_Adapter extends RecyclerView.Adapter<Answers_List_Adapter.ViewHolder> {
    Context context;
    private ArrayList<Category_Model.Data> arrayList;
    private ArrayList<String> arrayList_contest_answer;
    private ArrayList<String[]> comma_arrayList = new ArrayList<>();

    public Answers_List_Adapter(My_Contest_History my_contest_history, ArrayList<Category_Model.Data> data) {
        this.context = my_contest_history;
        this.arrayList = data;
    }

    @NonNull
    @Override
    public Answers_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.answers_list_adapter_details_layout, parent, false);
        return new Answers_List_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Answers_List_Adapter.ViewHolder holder, int position) {
        String str_answer = arrayList.get(position).answer;

        String str_option_a = arrayList.get(position).option_a;
        String str_option_b = arrayList.get(position).option_b;
        String str_option_c = arrayList.get(position).option_c;
        String str_option_d = arrayList.get(position).option_d;


        holder.tv_questions.setText(arrayList.get(position).question);
        holder.tv_option_a.setText(arrayList.get(position).option_a);
        holder.tv_option_b.setText(arrayList.get(position).option_b);
        holder.tv_option_c.setText(arrayList.get(position).option_c);
        holder.tv_option_d.setText(arrayList.get(position).option_d);

        if (str_answer.matches(holder.tv_option_a.getText().toString().substring(0, 1))) {
            holder.tv_option_a.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
        } else if (str_answer.matches(holder.tv_option_b.getText().toString().substring(0, 1))) {
            holder.tv_option_b.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
        } else if (str_answer.matches(holder.tv_option_c.getText().toString().substring(0, 1))) {
            holder.tv_option_c.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
        } else if (str_answer.matches(holder.tv_option_d.getText().toString().substring(0, 1))) {
            holder.tv_option_d.setTextColor(context.getResources().getColor(R.color.timer_bg_color));
        }
        Log.e("holder_contest_answer", holder.tv_your_crct_ans.getText().toString());
        Log.e("question_ans", "" + arrayList.get(position).question);
        Log.e("option_a_ans", "" + arrayList.get(position).option_a.substring(0, 1));
        Log.e("option_b_ans", "" + arrayList.get(position).option_b.substring(0, 1));
        Log.e("option_c_ans", "" + arrayList.get(position).option_c.substring(0, 1));
        Log.e("option_d_ans", "" + arrayList.get(position).option_d.substring(0, 1));

        try {
            String s1 = arrayList.get(position).contest_answer;
//            Log.e("s1_value", s1);
            String[] s2 = s1.split(",");
            comma_arrayList.add(s2);
            holder.tv_your_crct_ans.append(s2[position]);
        } catch (IndexOutOfBoundsException ibe) {
            ibe.printStackTrace();
        }
        Log.e("comma_arrayList_ans", holder.tv_your_crct_ans.getText().toString());


        /*if (holder.tv_your_crct_ans.getText().toString().equalsIgnoreCase("Skip")) {
            if (holder.tv_time.getText().toString().isEmpty()){
                holder.tv_time.setText("bgjkfnjdfn");
                holder.tv_time.setText("");
            }
        }*/
        try {
            String s11 = arrayList.get(position).onclick_time;
//            Log.e("s11_value", s11);
            String[] s2 = s11.split(",");
            comma_arrayList.add(s2);
            holder.tv_time.append(s2[position]);
        } catch (IndexOutOfBoundsException ibe) {
            ibe.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_questions, tv_option_a, tv_option_b, tv_option_c, tv_option_d, tv_your_crct_ans, tv_point, tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_questions = itemView.findViewById(R.id.tv_questions);
            tv_option_a = itemView.findViewById(R.id.tv_option_a);
            tv_option_b = itemView.findViewById(R.id.tv_option_b);
            tv_option_c = itemView.findViewById(R.id.tv_option_c);
            tv_option_d = itemView.findViewById(R.id.tv_option_d);
            tv_your_crct_ans = itemView.findViewById(R.id.tv_your_crct_ans);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}

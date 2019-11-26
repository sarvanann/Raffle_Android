package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.spot_the_ballgame.R;

import java.util.ArrayList;

public class GridView_Adapter extends BaseAdapter {
    private ArrayList arrayList;
    private Context context;

    public GridView_Adapter(FragmentActivity jsonObjectListener, ArrayList str_categroies) {
        this.context = jsonObjectListener;
        this.arrayList = str_categroies;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_view_text_layout, null);
            TextView textView = convertView.findViewById(R.id.textwwww);
            textView.setText((Integer) arrayList.get(position));
        }
        return convertView;
    }
}

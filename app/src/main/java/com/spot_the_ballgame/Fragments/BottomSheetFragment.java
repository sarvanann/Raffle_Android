package com.spot_the_ballgame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.spot_the_ballgame.R;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    public BottomSheetFragment() {
        // Required empty public constructor
    }

    Button btn_update_bottom_sheet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        btn_update_bottom_sheet = view.findViewById(R.id.btn_update_bottom_sheet);
        btn_update_bottom_sheet.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_bottom_sheet:
//                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

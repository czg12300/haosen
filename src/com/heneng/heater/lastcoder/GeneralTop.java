package com.heneng.heater.lastcoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.heneng.heater.R;

public class GeneralTop extends Fragment {

    Button ReturnBtn;
    TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_top, container, false);

        titleView = (TextView) view.findViewById(R.id.generalTop_title);
        ReturnBtn = (Button) view.findViewById(R.id.generalTop_return);
        ReturnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return view;
    }

    public void BindData(String title, String returnText) {
        if (title != null)
            titleView.setText(title);
        if (returnText != null)
            ReturnBtn.setText(returnText);
    }

}

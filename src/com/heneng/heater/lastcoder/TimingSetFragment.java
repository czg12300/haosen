package com.heneng.heater.lastcoder;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.baseClass.BaseFragment;
import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.model.MonitorInfo;
import com.heneng.heater.lastcoder.model.NowState;
import com.heneng.heater.models.ResultGetNowData;


public class TimingSetFragment extends BaseFragment {

    View view;
    RelativeLayout box;
    ResultGetNowData Data;
    int itemWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frament_timingset, container,
                false);
        InitPageView();

        box.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    if (Data != null) {
                        TimingSetDialogFragment tsDialog = new TimingSetDialogFragment(Data);
                        tsDialog.setCancelable(false);
                        tsDialog.show(getFragmentManager(), "timingset");
                    }

                }
            }
        });

        int marginLeft = 0, marginTop = 0;
        for (int i = 0; i < 24; i++) {
            android.widget.RelativeLayout.LayoutParams paras =
                    new android.widget.RelativeLayout.LayoutParams(itemWidth, itemWidth);
            paras.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paras.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            paras.setMargins(marginLeft, marginTop, 0, 0);

            TextView txt = new TextView(getActivity());
            txt.setText(i + "");
//			txt.setTextColor(color.timingset_item_font1);
            txt.setLayoutParams(paras);
            txt.setGravity(Gravity.CENTER);
            box.addView(txt);
            if ((i + 1) % 6 == 0) {
                marginLeft = 0;
                marginTop += itemWidth;
                if (i != 23) {
                    txt.setBackgroundResource(R.drawable.timingset_item2_1);
                } else {
                    txt.setBackgroundResource(R.drawable.timingset_item4_1);
                }
            } else {
                if (i < 18) {
                    txt.setBackgroundResource(R.drawable.timingset_item1_1);
                } else {
                    txt.setBackgroundResource(R.drawable.timingset_item3_1);
                }
                marginLeft += itemWidth;
            }
        }
        return view;
    }

    private void InitPageView() {
        box = (RelativeLayout) view.findViewById(R.id.fragment_timingset_box);
        itemWidth = CommonFunction.dip2px(getActivity(), 26);
        if (MonitorInfo.Width == 360) return;

        if (MonitorInfo.Width <= 320) {
            itemWidth = CommonFunction.dip2px(getActivity(), 21);
        } else if (MonitorInfo.Width >= 480) {
            itemWidth = CommonFunction.dip2px(getActivity(), 32);
        }
    }

    public void pushCallback(ResultGetNowData data) {

        BindData(data);

    }
    @Override
    public void BindData(ResultGetNowData data) {
        Data = data;
        if (box != null && data.getHeatingTimer().length() == 24) {
            int c = box.getChildCount();
            Resources res = getResources();
            for (int i = 0; i < c; i++) {
                TextView txt = (TextView) box.getChildAt(i);
                String val = data.getHeatingTimer().substring(i, i + 1);
                txt.setTag(val);

                String resIdx = (!val.equals("1")) ? "1" : "2";
                int resid = 0;
                if ((i + 1) % 6 == 0) {
                    if (i != 23) {
                        resid = res.getIdentifier("timingset_item2_" + resIdx, "drawable", getActivity().getPackageName());
                    } else {
                        resid = res.getIdentifier("timingset_item4_" + resIdx, "drawable", getActivity().getPackageName());
                    }
                } else {
                    if (i < 18) {
                        resid = res.getIdentifier("timingset_item1_" + resIdx, "drawable", getActivity().getPackageName());
                    } else {
                        resid = res.getIdentifier("timingset_item3_" + resIdx, "drawable", getActivity().getPackageName());
                    }
                }
                txt.setBackgroundResource(resid);
            }
        }
    }

}

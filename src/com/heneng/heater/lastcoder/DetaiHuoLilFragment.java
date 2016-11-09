package com.heneng.heater.lastcoder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.baseClass.BaseFragment;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.model.MonitorInfo;
import com.heneng.heater.lastcoder.model.NowState;
import com.heneng.heater.models.ResultGetNowData;


public class DetaiHuoLilFragment extends BaseFragment {

    ResultGetNowData Data = null;
    double itemWidth = 0;
    ImageView huoliImg;
    ImageView huoliBgImg;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frament_huoliimg, container,
                false);
        InitPageView();
        huoliImg = (ImageView) view.findViewById(R.id.frament_huoliNumImg);
        itemWidth = (double) CommonFunction.dip2px(getActivity(), 128) / 100;
        return view;
    }

    private void InitPageView() {

        huoliImg = (ImageView) view.findViewById(R.id.frament_huoliNumImg);
        huoliBgImg = (ImageView) view.findViewById(R.id.frament_huoliBg);


        if (MonitorInfo.Width == 360) return;

        RelativeLayout.LayoutParams paras1 = (RelativeLayout.LayoutParams) huoliImg.getLayoutParams();
        RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) huoliBgImg.getLayoutParams();

        if (MonitorInfo.Width <= 320) {
            paras1.width = CommonFunction.dip2px(getActivity(), 118);
            paras1.height = CommonFunction.dip2px(getActivity(), 40);
            paras1.setMargins(paras1.leftMargin, CommonFunction.dip2px(getActivity(), 10), 0, 0);
        } else if (MonitorInfo.Width >= 480) {
            paras2.width = CommonFunction.dip2px(getActivity(), 220);
            paras2.height = CommonFunction.dip2px(getActivity(), 80);

            paras1.width = CommonFunction.dip2px(getActivity(), 170);
            paras1.height = CommonFunction.dip2px(getActivity(), 58);
            paras1.setMargins(CommonFunction.dip2px(getActivity(), 25), CommonFunction.dip2px(getActivity(), 11), 0, 0);

        }

        itemWidth = (double) huoliImg.getWidth() / 100;
    }


    @Override
    public void BindData(ResultGetNowData data) {
        Data = data;

        RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) huoliImg.getLayoutParams();
        int target = (int) Math.round(Integer.parseInt(data.getFire()) * itemWidth);
        paras.width = target;
        huoliImg.setLayoutParams(paras);
    }

}

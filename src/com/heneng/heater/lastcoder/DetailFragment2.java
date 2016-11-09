package com.heneng.heater.lastcoder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.baseClass.BaseFragment;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.model.MonitorInfo;
import com.heneng.heater.lastcoder.model.NowState;
import com.heneng.heater.models.ResultGetNowData;

import java.text.SimpleDateFormat;


public class DetailFragment2 extends BaseFragment {
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_lay2, container, false);

        TempFragment tf = (TempFragment) getChildFragmentManager().findFragmentById(R.id.detail_fragment_tempset2);
        if (tf != null) {
            tf.SetConflictScrollView((ScrollView) view.findViewById(R.id.detail_scrollView2));
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (MonitorInfo.Width == 360) return;

        //顶部
        ImageView p1Img1 = (ImageView) view.findViewById(R.id.detail_p1_img2);
        RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) p1Img1.getLayoutParams();
        TextView temp2 = (TextView) view.findViewById(R.id.detail_temp2);
        RelativeLayout.LayoutParams parasTt = (RelativeLayout.LayoutParams) temp2.getLayoutParams();
        //温度设定部分
        View tempF = view.findViewById(R.id.detail_fragment_tempset2);
        RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) tempF.getLayoutParams();
        RelativeLayout tempBox = (RelativeLayout) view.findViewById(R.id.detail_tempSetBox2);
        LayoutParams parasTBox = tempBox.getLayoutParams();
        //天气部分
        LinearLayout weatherBox = (LinearLayout) view.findViewById(R.id.detail_weatherBox);
        LayoutParams parasTsBox = weatherBox.getLayoutParams();
        View weather = view.findViewById(R.id.detail_weather);
        LinearLayout.LayoutParams parasTs = (LinearLayout.LayoutParams) weather.getLayoutParams();
        //燃气火力部分
        RelativeLayout huoliBox = (RelativeLayout) view.findViewById(R.id.detail_huoliBox2);
        LayoutParams parasHlBox = huoliBox.getLayoutParams();
        TextView huoliTxt2 = (TextView) view.findViewById(R.id.detail_huoliNumTxt2);
        RelativeLayout.LayoutParams parasHlTxt = (RelativeLayout.LayoutParams) huoliTxt2.getLayoutParams();
        View huoliImg2 = view.findViewById(R.id.detail_huoliImg2);
        RelativeLayout.LayoutParams parasHlImg = (RelativeLayout.LayoutParams) huoliImg2.getLayoutParams();

        if (MonitorInfo.Width <= 320) {
            //顶部
            paras.width = (int) Math.round(paras.width * MonitorInfo.WidthRate);
            paras.height = (int) Math.round(paras.height * MonitorInfo.WidthRate);
            temp2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) Math.round(temp2.getTextSize() * MonitorInfo.WidthRate));
            //温度设定部分
            paras2.setMargins(0, CommonFunction.dip2px(getActivity(), 45), 0, 0);
            parasTBox.height = CommonFunction.dip2px(getActivity(), 320);
            //定时设置部分
            parasTsBox.height = CommonFunction.dip2px(getActivity(), 155);
            //燃气火力部分
            parasHlBox.height = CommonFunction.dip2px(getActivity(), 160);
            parasHlImg.setMargins(0, CommonFunction.dip2px(getActivity(), 90), 0, 0);
        }
        if (MonitorInfo.Width >= 480) {
            //顶部
            paras.width = (int) Math.round(paras.width * MonitorInfo.WidthRate);
            paras.height = (int) Math.round(paras.height * MonitorInfo.WidthRate);
            temp2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) Math.round(temp2.getTextSize() * MonitorInfo.WidthRate));
            parasTt.height = (int) Math.round(parasTt.height * MonitorInfo.WidthRate);
            //温度设定部分
            paras2.setMargins(0, CommonFunction.dip2px(getActivity(), 60), 0, 0);
            parasTBox.height = CommonFunction.dip2px(getActivity(), 490);
            //定时设置部分
            parasTsBox.height = CommonFunction.dip2px(getActivity(), 240);
            parasTs.setMargins(0, CommonFunction.dip2px(getActivity(), 65), 0, 0);
            //燃气火力部分
            parasHlBox.height = CommonFunction.dip2px(getActivity(), 245);
            parasHlTxt.setMargins(0, CommonFunction.dip2px(getActivity(), 60), 0, 0);
            parasHlTxt.height = CommonFunction.dip2px(getActivity(), 60);
            huoliTxt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            parasHlImg.setMargins(0, CommonFunction.dip2px(getActivity(), 130), 0, 0);
        }
    }
    public void pushCallback(final ResultGetNowData data) {
        initView(data);

        final TempFragment tfrag1 = (TempFragment) getChildFragmentManager().findFragmentById(R.id.detail_fragment_tempset2);
        if (tfrag1 != null) {
            if (tfrag1.isVisible()) {
                tfrag1.pushCallback(data, 2);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tfrag1.pushCallback(data, 2);
                    }
                }, 1000);
            }
        }
        final DetaiHuoLilFragment firefrag1 = (DetaiHuoLilFragment) getChildFragmentManager().findFragmentById(R.id.detail_huoliImg2);
        if (firefrag1 != null) {
            if (firefrag1.isVisible()) {
                firefrag1.pushCallback(data);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        firefrag1.pushCallback(data);
                    }
                }, 1000);
            }
        }
        final WeatherFragemnt weatherFragemnt = (WeatherFragemnt) getChildFragmentManager().findFragmentById(R.id.detail_weather);
        if (weatherFragemnt != null) {
            if (weatherFragemnt.isVisible()) {
                weatherFragemnt.pushCallback(data);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        weatherFragemnt.pushCallback(data);
                    }
                }, 1000);
            }
        }

    }
    @Override
    public void BindData(final ResultGetNowData data) {
        initView(data);

        final TempFragment tfrag1 = (TempFragment) getChildFragmentManager().findFragmentById(R.id.detail_fragment_tempset2);
        if (tfrag1 != null) {
            if (tfrag1.isVisible()) {
                tfrag1.BindData(data, 2);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tfrag1.BindData(data, 2);
                    }
                }, 1000);
            }
        }
        final DetaiHuoLilFragment firefrag1 = (DetaiHuoLilFragment) getChildFragmentManager().findFragmentById(R.id.detail_huoliImg2);
        if (firefrag1 != null) {
            if (firefrag1.isVisible()) {
                firefrag1.BindData(data);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        firefrag1.BindData(data);
                    }
                }, 1000);
            }
        }
        final WeatherFragemnt weatherFragemnt = (WeatherFragemnt) getChildFragmentManager().findFragmentById(R.id.detail_weather);
        if (weatherFragemnt != null) {
            if (weatherFragemnt.isVisible()) {
                weatherFragemnt.BindData(data);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        weatherFragemnt.BindData(data);
                    }
                }, 1000);
            }
        }
    }

    private void initView(ResultGetNowData data) {
        FragmentActivity f = getActivity();
        if (f != null) {
            TextView temp2 = (TextView) f.findViewById(R.id.detail_temp2);
            temp2.setText(data.getCaiNuanTemp() + "℃");
            TextView huoli = (TextView) f.findViewById(R.id.detail_huoliNumTxt2);
            huoli.setText(data.getFire() + "%");
            TextView date = (TextView) f.findViewById(R.id.detail_tv_date2);
//            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            date.setText(data.getDate());
        }
    }

}

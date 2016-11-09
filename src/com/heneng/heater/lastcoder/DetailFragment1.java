package com.heneng.heater.lastcoder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
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


public class DetailFragment1 extends BaseFragment {

    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_lay1, container, false);

        TempFragment tf = (TempFragment) getChildFragmentManager().findFragmentById(R.id.detail_fragment_tempset1);
        if (tf != null) {
            tf.SetConflictScrollView((ScrollView) view.findViewById(R.id.detail_scrollView1));
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (MonitorInfo.Width == 360) return;

        ImageView p1Img1 = (ImageView) view.findViewById(R.id.detail_p1_img1);
        RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) p1Img1.getLayoutParams();
        TextView temp1 = (TextView) view.findViewById(R.id.detail_temp1);
        RelativeLayout.LayoutParams parasTt = (RelativeLayout.LayoutParams) temp1.getLayoutParams();

        View tempF = view.findViewById(R.id.detail_fragment_tempset1);
        RelativeLayout.LayoutParams paras2 = (RelativeLayout.LayoutParams) tempF.getLayoutParams();
        RelativeLayout tempBox = (RelativeLayout) view.findViewById(R.id.detail_tempSetBox);
        LayoutParams parasTBox = tempBox.getLayoutParams();

        ImageView flowBg = (ImageView) view.findViewById(R.id.detail_flowBg);
        RelativeLayout.LayoutParams paras3 = (RelativeLayout.LayoutParams) flowBg.getLayoutParams();
        RelativeLayout flowBox = (RelativeLayout) view.findViewById(R.id.detail_flowBox);
        LayoutParams parasFBox = flowBox.getLayoutParams();
        TextView flowNum = (TextView) view.findViewById(R.id.detail_flowNumberTxt);
        RelativeLayout.LayoutParams parasFNum = (RelativeLayout.LayoutParams) flowNum.getLayoutParams();

        RelativeLayout huoliBox = (RelativeLayout) view.findViewById(R.id.detail_huoliBox);
        LayoutParams parasHlBox = huoliBox.getLayoutParams();
        TextView huoliTxt1 = (TextView) view.findViewById(R.id.detail_huoliNumTxt1);
        RelativeLayout.LayoutParams parasHlTxt = (RelativeLayout.LayoutParams) huoliTxt1.getLayoutParams();
        View huoliImg1 = view.findViewById(R.id.detail_huoliImg1);
        RelativeLayout.LayoutParams parasHlImg = (RelativeLayout.LayoutParams) huoliImg1.getLayoutParams();

        if (MonitorInfo.Width <= 320) {
            paras.width = (int) Math.round(paras.width * MonitorInfo.WidthRate);
            paras.height = (int) Math.round(paras.height * MonitorInfo.WidthRate);
            temp1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) Math.round(temp1.getTextSize() * MonitorInfo.WidthRate));

            paras2.setMargins(0, CommonFunction.dip2px(getActivity(), 45), 0, 0);
            parasTBox.height = CommonFunction.dip2px(getActivity(), 320);

            parasFBox.height = CommonFunction.dip2px(getActivity(), 155);
            paras3.width = CommonFunction.dip2px(getActivity(), 115);
            paras3.height = CommonFunction.dip2px(getActivity(), 115);
            parasFNum.setMargins(0, CommonFunction.dip2px(getActivity(), 22), 0, 0);

            parasHlBox.height = CommonFunction.dip2px(getActivity(), 160);
            parasHlImg.setMargins(0, CommonFunction.dip2px(getActivity(), 90), 0, 0);
        } else if (MonitorInfo.Width >= 480) {
            paras.width = (int) Math.round(paras.width * MonitorInfo.WidthRate);
            paras.height = (int) Math.round(paras.height * MonitorInfo.WidthRate);
            temp1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) Math.round(temp1.getTextSize() * MonitorInfo.WidthRate));
            parasTt.height = (int) Math.round(parasTt.height * MonitorInfo.WidthRate);

            paras2.setMargins(0, CommonFunction.dip2px(getActivity(), 60), 0, 0);
            parasTBox.height = CommonFunction.dip2px(getActivity(), 490);

            parasFBox.height = CommonFunction.dip2px(getActivity(), 240);
            paras3.width = CommonFunction.dip2px(getActivity(), 180);
            paras3.height = CommonFunction.dip2px(getActivity(), 180);
            parasFNum.height = CommonFunction.dip2px(getActivity(), 60);
            parasFNum.setMargins(0, CommonFunction.dip2px(getActivity(), 50), 0, 0);
            flowNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);

            parasHlBox.height = CommonFunction.dip2px(getActivity(), 245);
            parasHlTxt.setMargins(0, CommonFunction.dip2px(getActivity(), 60), 0, 0);
            parasHlTxt.height = CommonFunction.dip2px(getActivity(), 60);
            huoliTxt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            parasHlImg.setMargins(0, CommonFunction.dip2px(getActivity(), 130), 0, 0);
        }
    }

    public void pushCallback(final ResultGetNowData data) {

        initView(data);

        final TempFragment tfrag1 = (TempFragment) getChildFragmentManager().findFragmentById(R.id.detail_fragment_tempset1);
        if (tfrag1 != null) {
            if (tfrag1.isVisible()) {
                tfrag1.pushCallback(data, 1);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tfrag1.pushCallback(data, 1);
                    }
                }, 1000);
            }
        }
        final DetaiHuoLilFragment firefrag1 = (DetaiHuoLilFragment) getChildFragmentManager().findFragmentById(R.id.detail_huoliImg1);
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

    }
    public void BindData(final ResultGetNowData data) {
        initView(data);

        final TempFragment tfrag1 = (TempFragment) getChildFragmentManager().findFragmentById(R.id.detail_fragment_tempset1);
        if (tfrag1 != null) {
            if (tfrag1.isVisible()) {
                tfrag1.BindData(data, 1);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tfrag1.BindData(data, 1);
                    }
                }, 1000);
            }
        }
        final DetaiHuoLilFragment firefrag1 = (DetaiHuoLilFragment) getChildFragmentManager().findFragmentById(R.id.detail_huoliImg1);
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
    }

    private void initView(ResultGetNowData data) {
        FragmentActivity f = getActivity();
        if (f != null) {
            TextView temp1 = (TextView) f.findViewById(R.id.detail_temp1);
            temp1.setText(data.getWeiYuTemp()+ "℃");
            TextView flow = (TextView) f.findViewById(R.id.detail_flowNumberTxt);
            flow.setText(data.getFlow());
            TextView huoli = (TextView) f.findViewById(R.id.detail_huoliNumTxt1);
            huoli.setText(data.getFire() + "%");
            TextView date1 = (TextView) f.findViewById(R.id.detail_tv_date1);
            date1.setText(data.getDate());
        }
    }
}

package com.heneng.heater.lastcoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.MonitorInfo;


public class MainFragment2 extends Fragment {

    ImageView GuideBtn1, GuideBtn2, GuideBtn3, GuideBtn4, GuideBtn5, GuideBtn6;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_lay2, container,
                false);
        InitPageView();

        GuideBtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    GoWebPage(AppParams.WebSite + "/WEBAPP/Introduction.htm");
                }
            }
        });
        GuideBtn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    GoWebPage(AppParams.WebSite + "/WEBAPP/Repair.htm");
                }
            }
        });
        GuideBtn5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    GoWebPage(AppParams.WebSite + "/WEBAPP/TroublesTips.htm");
                }
            }
        });
        GuideBtn6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    GoWebPage(AppParams.WebSite + "/WEBAPP/ContactInfo.htm");
                }
            }
        });

        return view;
    }

    private void GoWebPage(String url) {
        Uri uri = Uri.parse("browser_info://process");
        Intent intent = new Intent("com.heneng.heater.BrowserActivity", uri);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void InitPageView() {
        GuideBtn1 = (ImageView) view.findViewById(R.id.main2_guidebtn1);
        GuideBtn2 = (ImageView) view.findViewById(R.id.main2_guidebtn2);
        GuideBtn3 = (ImageView) view.findViewById(R.id.main2_guidebtn3);
        GuideBtn4 = (ImageView) view.findViewById(R.id.main2_guidebtn4);
        GuideBtn5 = (ImageView) view.findViewById(R.id.main2_guidebtn5);
        GuideBtn6 = (ImageView) view.findViewById(R.id.main2_guidebtn6);
        if (MonitorInfo.Width == 360) return;

        int btnWidth = CommonFunction.dip2px(getActivity(), 150);
        if (MonitorInfo.Width <= 320) {
            btnWidth = CommonFunction.dip2px(getActivity(), 130);
        } else if (MonitorInfo.Width >= 480) {
            btnWidth = CommonFunction.dip2px(getActivity(), 200);
        }
        TableRow.LayoutParams paras = (TableRow.LayoutParams) GuideBtn1.getLayoutParams();
        paras.width = btnWidth;
        paras.height = btnWidth;
        GuideBtn1.setLayoutParams(paras);
        GuideBtn2.setLayoutParams(paras);
        GuideBtn3.setLayoutParams(paras);
        GuideBtn4.setLayoutParams(paras);
        GuideBtn5.setLayoutParams(paras);
        GuideBtn6.setLayoutParams(paras);
    }

}

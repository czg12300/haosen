package com.heneng.heater.lastcoder;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.model.UserInfo;


public class MainTop extends Fragment {

    public ImageView TopAddBtn;
    public ImageView TopRefreshBtn;

    Animation refreshAnim;
    int runningRefresh = 0;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_top, container, false);

        TopRefreshBtn = (ImageView) view.findViewById(R.id.imgTopRefresh);
        TopRefreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    if (refreshAnim.hasStarted()) {
                        TopRefreshBtn.clearAnimation();
                    }
                    TopRefreshBtn.startAnimation(refreshAnim);


                }
            }
        });

        TopAddBtn = (ImageView) view.findViewById(R.id.imgTopAdd);
        TopAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    Uri uri = Uri.parse("barcode_info://process");
                    Intent intent = new Intent("com.heneng.heater.BarcodeActivity", uri);
                    Bundle bundle = new Bundle();
                    //由于使用新进程打开需要额外携带用户信息
                    bundle.putString("userID", UserInfo.ID + "");
                    bundle.putString("userCode", UserInfo.UserCode);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        initAnimation();

        return view;
    }

    public void ChangeBtnView(int idx) {

        if (idx == 1) {
            view.setVisibility(View.VISIBLE);
            TopRefreshBtn.setVisibility(View.VISIBLE);
            TopAddBtn.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void initAnimation() {

        refreshAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate);
        refreshAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                runningRefresh++;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runningRefresh--;
            }
        });
    }

}

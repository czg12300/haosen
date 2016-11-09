package com.heneng.heater.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.MyViewPager;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultErrorList;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.models.ResultReportFault;
import com.heneng.heater.models.ResultReportFaultList;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.NetUtils;
import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/20.
 */
public class RepairFragment extends BaseNetFragment implements View.OnClickListener {
    @InjectView(R.id.repair_im_cursor)
    ImageView im_cursor;
    @InjectView(R.id.repair_tv_send)
    TextView tv_send;
    @InjectView(R.id.repair_tv_progress)
    TextView tv_progress;
    @InjectView(R.id.repair_tv_evaluation)
    TextView tv_evaluation;
    @InjectView(R.id.repair_vp)
    MyViewPager vp_content;


    List<BaseFragment> _fragments;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ArrayList<TextView> _menus;

    RepairSendFragment _sendFragment;
    RepairProgressFragment _progressFragment;
    RepairEvaluationFragment _evaluationFragment;

    ResultReportFault.DataEntity _reportFault;


    private BroadcastReceiver _receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.BROCAST_JUMP_TO_REPAIRPROGRESS.equals(intent.getAction())) {
                vp_content.setCurrentItem(1);
                _reportFault = (ResultReportFault.DataEntity) intent.getSerializableExtra(Constants.KEY_EXTRA_RESULTREPORTFAULT);
                _progressFragment.setResultReportFault(_reportFault);
                _evaluationFragment.setReportFault(_reportFault);
            } else if (Constants.BROCAST_JUMP_TO_REPAIREVALUATION.equals(intent.getAction())) {
                vp_content.setCurrentItem(2);
                _reportFault = (ResultReportFault.DataEntity) intent.getSerializableExtra(Constants.KEY_EXTRA_RESULTREPORTFAULT);
                _progressFragment.setResultReportFault(_reportFault);
                _evaluationFragment.setReportFault(_reportFault);
            } else if (Constants.BROCAST_JUMP_TO_REPAIRSEND.equals(intent.getAction())) {
                vp_content.setCurrentItem(0);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.a1_fragment_repair, null);
        ButterKnife.inject(this, _view);


        initViewPager();
        InitCursor();
        initMenu();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROCAST_JUMP_TO_REPAIREVALUATION);
        filter.addAction(Constants.BROCAST_JUMP_TO_REPAIRPROGRESS);
        filter.addAction(Constants.BROCAST_JUMP_TO_REPAIRSEND);
        mContext.registerReceiver(_receiver, filter);



        return _view;
    }

    @OnClick(R.id.repair_im_new)
    void onClickNew() {
        _app._chooseDevice = null;
        _reportFault = null;
        vp_content.setCurrentItem(0);
        _sendFragment.resetData();
//        vp_content.setCurrentItem(2);
    }

    @OnClick(R.id.repair_im_refress)
    void onClickRefress() {
        checkStatus();
    }

    private void checkStatus() {
        if(_app._chooseDevice == null)return;
//        Map<String, Object> paramsMap = getParamsMap();
//        paramsMap.put("uc", UserInfo.UserCode);
//        paramsMap.put("eid", _app._chooseDevice.getEID());
//        paramsMap.put("clientMac", PushManager.getInstance().getClientid(mContext));
//        LogUtils.e("UC:" + UserInfo.UserCode);
//        NetUtils.executPost(Constants.CODE_GET_ERRORLIST, "Get_ErrorList", paramsMap, ResultErrorList.class);
        Map<String, Object> paramsMap1 = getParamsMap();
        paramsMap1.put("uc", UserInfo.UserCode);
        paramsMap1.put("user", UserInfo.ID);
        paramsMap1.put("eid", _app._chooseDevice.getEID());

        NetUtils.executPost(mContext,Constants.CODE_GET_GETREPORTFAULT, "GetReportFault", paramsMap1, ResultReportFault.class);
//        NetUtils.executGet(Constants.CODE_GET_GETNOWDATA, _app._chooseDevice.getInterface(), "GetNowData", paramsMap, ResultGetNowData.class);
        showLoading();
    }

    private void initMenu() {
        _menus = new ArrayList<>();
        _menus.add(tv_send);
        _menus.add(tv_progress);
        _menus.add(tv_evaluation);
        tv_send.setOnClickListener(this);
        tv_progress.setOnClickListener(this);
        tv_evaluation.setOnClickListener(this);
        tv_send.setSelected(true);
    }

    public void initSendFragment() {
        vp_content.setCurrentItem(0);
        _reportFault = null;
        if (_sendFragment != null) {
            _sendFragment.initData();
        }
    }

    private void initViewPager() {
        _fragments = new ArrayList<>();
        _sendFragment = new RepairSendFragment();
        _fragments.add(_sendFragment);
        _progressFragment = new RepairProgressFragment();
        _fragments.add(_progressFragment);
        _evaluationFragment = new RepairEvaluationFragment();
        _fragments.add(_evaluationFragment);
        vp_content.setNoScroll(true);
        MyViewpagerAdapter adapter = new MyViewpagerAdapter(getFragmentManager());
        vp_content.setAdapter(adapter);
        vp_content.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitCursor() {
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = 0;//(screenW / 3 - bmpW) / 2;
        bmpW = screenW / _fragments.size();
        ViewGroup.LayoutParams lparams = im_cursor.getLayoutParams();
        lparams.width = bmpW;
        im_cursor.setLayoutParams(lparams);
    }

    @Override
    public void onClick(View view) {
        int index = _menus.indexOf(view);
        if (index != 0) {
            //没有选择设备不能前往修理界面
            if (_app._chooseDevice == null) {
                Toast.makeText(mContext, "请先选择设备", Toast.LENGTH_SHORT).show();
                return;
            }
            if (_reportFault == null) {
                Toast.makeText(mContext, "未查询到该设备维修单", Toast.LENGTH_SHORT).show();
                return;
            }
            return;
        } else {
            if (_reportFault != null) {
                Toast.makeText(mContext, "这个设备已经提交了维修单", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        vp_content.setCurrentItem(index);
    }

    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_GET_GETREPORTFAULT){
            closeLoading();
        }
    }


    class MyViewpagerAdapter extends FragmentPagerAdapter {


        public MyViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return _fragments.get(position);
        }

        @Override
        public int getCount() {
            return _fragments.size();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(_receiver);
        ButterKnife.reset(this);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageSelected(final int arg0) {
//            LogUtils.e("onPageSelected:" + currIndex + "----" + arg0);
            int one = offset * 2 + bmpW;
            Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    for (int i = 0; i < _menus.size(); i++) {
                        if (arg0 != i) {
                            _menus.get(i).setSelected(false);
                        } else {
                            _menus.get(i).setSelected(true);
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            im_cursor.startAnimation(animation);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

}

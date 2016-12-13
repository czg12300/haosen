package com.heneng.heater.lastcoder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.heneng.heater.R;
import com.heneng.heater.lastcoder.baseClass.BaseFragment;
import com.heneng.heater.lastcoder.common.MyFragmentPagerAdapter;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultErrorDescription;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.utils.ReceiverActions;
import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailActivity extends FragmentActivity implements NetUtils.NetCallbackListener {

    private String eid;
    public String refererPage;//从哪个页面进入这一页
    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private BaseFragment[] pageFragmentArray;//切换的页面数组


    LoadingFragment loadingDialog = null;


    public AppAplication _app;

    Runnable _timeoutCmdRunnable = new Runnable() {

        @Override
        public void run() {
            CloseLoadingDetail();
            Toast.makeText(DetailActivity.this, "控制命令超时", Toast.LENGTH_SHORT).show();
            refressDataNow();
        }
    };


    private boolean _canSendCmd = true;


    public boolean sendCmd(int code, String method, Map<String, Object> params, Class<? extends BaseModel> clazz, boolean needInterface) {

        if (!_canSendCmd) {
//            Toast.makeText(this, "您操作太频繁了,请稍后再试", Toast.LENGTH_SHORT).show();
            return false;
        }

        LogUtils.e("发送指令时间：" + new Date().toLocaleString());

        _canSendCmd = false;
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this != null) {
                    _canSendCmd = true;
                }
            }
        }, 1000);

        if (!"1".equals(_app._chooseDevice.getOnLine())) {
            Toast.makeText(this, "这个设备不在线", Toast.LENGTH_SHORT).show();
            return false;
        }

        _handler.removeCallbacks(_getNowDataRunnable);
        _handler.postDelayed(_getNowDataRunnable, 20000);
        _handler.removeCallbacks(_timeoutCmdRunnable);
        _handler.postDelayed(_timeoutCmdRunnable, 12000);

        if (needInterface) {
            NetUtils.executGet(this, code, _app._chooseDevice.getInterface(), method, params, clazz);
        } else {
            NetUtils.executGet(this, code, method, params, clazz);
        }
        return true;
    }

    public boolean sendCmd(int code, String method, Map<String, Object> params, Class<ResultDetail> clazz) {
        return sendCmd(code, method, params, clazz, true);
    }

    private BroadcastReceiver mReceiver;


    private void setReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(ReceiverActions.ACTION_FINISH_SMART_CONFIG, intent.getAction())) {
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ReceiverActions.ACTION_FINISH_SMART_CONFIG);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setReceiver();

        //记录用户最后控制过哪个设备
        SharedPreferences sharedPreferences = getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString("userbarcode", eid);
        editor.commit();

        InitTextView();
        InitImageView();
        InitViewPager();
        //GetDetailDataHandler(false);

        _app = (AppAplication) getApplication();


    }


    BroadcastReceiver _receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.BROCAST_PUSH_STATE)) {
                String msg = (String) intent.getSerializableExtra(Constants.KEY_EXTRA_PUSH_STATE);
                Gson gson = new Gson();
                ResultGetNowData state = gson.fromJson(msg, ResultGetNowData.class);
                pushCallback(state);
            }
        }
    };


    public Map<String, Object> getParamsMap() {
        return new HashMap<>();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetUtils.unregistCallbackListener(this);
        _handler.removeCallbacks(_getNowDataRunnable);
        _handler.removeCallbacks(_timeoutCmdRunnable);
        unregisterReceiver(_receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetUtils.registNetCallbackListener(this);
        refressDataNow();
        IntentFilter filter = new IntentFilter(Constants.BROCAST_PUSH_STATE);
        registerReceiver(_receiver, filter);
    }


    public void refressDataNow() {
        _handler.removeCallbacks(_getNowDataRunnable);
        _handler.post(_getNowDataRunnable);
    }

    Handler _handler = new Handler();

    Runnable _getNowDataRunnable = new Runnable() {
        @Override
        public void run() {
            getNowData();
            _handler.postDelayed(_getNowDataRunnable, 20000);
        }
    };


    private boolean isPushGetNowData;

    void getNowData() {

        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("clientMac", PushManager.getInstance().getClientid(this));
        LogUtils.e("clientMac:" + PushManager.getInstance().getClientid(this));
        NetUtils.executGet(this, Constants.CODE_GET_GETNOWDATA, _app._chooseDevice.getInterface(), "GetNowData", paramsMap, ResultGetNowData.class);


    }


    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_GET_GETNOWDATA) {
            if (!isPushGetNowData) {

                ResultGetNowData data = (ResultGetNowData) model;
                if (data != null) {
                    binddata(data);
                }
            } else {
                LogUtils.e("push请求的，不刷新");
            }
            isPushGetNowData = false;
        }
    }


    public void pushCallback(ResultGetNowData data) {
        if (data == null) return;
        refressDataNow();
        _app._chooseDevice.setOnLine(data.getOnLine());

        isPushGetNowData = true;
        _handler.removeCallbacks(_timeoutCmdRunnable);
        DetailTop dt = (DetailTop) getSupportFragmentManager().findFragmentById(R.id.detail_title);


        if (dt != null) {
            dt.pushCallback(data);
        }
        StateIcoFragment si = (StateIcoFragment) getSupportFragmentManager().findFragmentById(R.id.detail_StateIco);
        if (si != null) {
            si.pushCallback(data);
        }

        for (BaseFragment f : pageFragmentArray) {
            f.pushCallback(data);
        }

    }


    private boolean _isFirstShowError;

    private void binddata(ResultGetNowData data) {
        if (data == null) return;
        if (!"0".equals(data.getError()) && "1".equals(data.getOnLine()) & !_isFirstShowError) {
            _isFirstShowError = true;
            Map<String, Object> params = getParamsMap();
            params.put("uc", UserInfo.UserCode);
            params.put("errCode", data.getError());
            NetUtils.executGet(this, Constants.CODE_GET_ERROR_DESCRIPTION, "Com_ErrorDescription", params, ResultErrorDescription.class);
        }
        _app._chooseDevice.setOnLine(data.getOnLine());

        DetailTop dt = (DetailTop) getSupportFragmentManager().findFragmentById(R.id.detail_title);
        if (dt != null) {
            dt.BindData(data);
        }
        StateIcoFragment si = (StateIcoFragment) getSupportFragmentManager().findFragmentById(R.id.detail_StateIco);
        if (si != null) {
            si.BindData(data);
        }

        for (BaseFragment f : pageFragmentArray) {
            f.BindData(data);
        }
    }

    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.detail_menu1);
        t2 = (TextView) findViewById(R.id.detail_menu2);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.detailPager);
        listViews = new ArrayList<View>();

        BaseFragment fragment1 = new DetailFragment1();
        BaseFragment fragment2 = new DetailFragment2();
        pageFragmentArray = new BaseFragment[]{fragment1, fragment2};
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), pageFragmentArray);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(2);

        //mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    //
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.detail_cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = 0;//(screenW / 2 - bmpW) / 2;
        bmpW = screenW / 2;
        LayoutParams lparams = cursor.getLayoutParams();
        lparams.width = bmpW;
        cursor.setLayoutParams(lparams);
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    ;

    TextView targetItem = null;
    TextView sourceItem = null;

    //
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;
        int two = one * 2;

        @Override
        public void onPageSelected(int arg0) {
            //Log.d("onPageSelected", ""+offset+" "+one+" "+two);
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                        sourceItem = t2;
                    }
                    targetItem = t1;
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                        sourceItem = t1;
                    }
                    targetItem = t2;
                    break;
            }
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    sourceItem.setTextColor(getResources().getColor(R.color.label_text_color));
                    targetItem.setTextColor(getResources().getColor(R.color.bottomGuide_textColor2));
                }
            });

            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public void ShowLoadingDetail() {
        if (loadingDialog == null) {
            try {
                if (!isFinishing()) {
                    loadingDialog = new LoadingFragment();
                    loadingDialog.setCancelable(false);
                    loadingDialog.show(getSupportFragmentManager(), "Loading");
                }
            } catch (Exception e) {
                e.printStackTrace();
                CloseLoadingDetail();
            }
        }
    }

    public void CloseLoadingDetail() {
        try {
            if (loadingDialog != null && loadingDialog.isVisible() && !isFinishing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

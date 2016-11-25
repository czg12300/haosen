package com.heneng.heater.lastcoder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.heneng.heater.R;
import com.heneng.heater.fragments.DevicesFragment;
import com.heneng.heater.fragments.MessagesFragment;
import com.heneng.heater.fragments.RepairFragment;
import com.heneng.heater.fragments.SettingFragment;
import com.heneng.heater.lastcoder.common.MyFragmentPagerAdapter;
import com.heneng.heater.lastcoder.common.MyViewPager;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultUpdate;
import com.heneng.heater.utils.CommonUtils;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.CrashHandler;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;

import org.androidannotations.annotations.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity implements NetUtils.NetCallbackListener {


    private MyViewPager mPager;//页卡内容
    private LinearLayout mainBox;//页卡内容
    private List<View> listViews;// Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2, t3, t4;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    private static final int MENU_SIZE = 4;//菜单数量

    private ArrayList<TextView> menus;


    DevicesFragment listFragment = null;

    Fragment[] fragmentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!AppAplication.isGoLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);

        mainBox = (LinearLayout) findViewById(R.id.main_box);

//        CrashHandler.getInstance().init(this);


        checkUpdate();

        InitTextView();
        InitImageView();
        InitViewPager();


    }

    private void InitTextView() {
        menus = new ArrayList<>();
        t1 = (TextView) findViewById(R.id.main_menu1);
        t2 = (TextView) findViewById(R.id.main_menu2);
        t3 = (TextView) findViewById(R.id.main_menu3);
        t4 = (TextView) findViewById(R.id.main_menu4);
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        t4.setOnClickListener(new MyOnClickListener(3));
        t1.setSelected(true);

        setDrawableTop(R.drawable.menu_device_selector, t1);
        setDrawableTop(R.drawable.menu_message_selector, t2);
        setDrawableTop(R.drawable.menu_service_selector, t3);
        setDrawableTop(R.drawable.menu_setting_selector, t4);

        menus.add(t1);
        menus.add(t2);
        menus.add(t3);
        menus.add(t4);
    }

    private void setDrawableTop(int drawbleRes, TextView tv) {
        Drawable devices = getResources().getDrawable(drawbleRes);
        int size = (int) getResources().getDimension(R.dimen.main_menu_drawable_size);
        devices.setBounds(0, 0, size, size);
        tv.setCompoundDrawables(null, devices, null, null);
    }

    private void InitViewPager() {
        mPager = (MyViewPager) findViewById(R.id.mainPager);
        mPager.setNoScroll(true);

        AppAplication app = (AppAplication) getApplication();

        listViews = new ArrayList<View>();

        listFragment = new DevicesFragment();
        Fragment fragment1 = listFragment;

        Fragment messageFragment = new MessagesFragment();
        Fragment repairFragment = new RepairFragment();
        Fragment settingFragment = new SettingFragment();

        fragmentArray = new Fragment[]{fragment1, messageFragment, repairFragment, settingFragment};
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentArray);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(MENU_SIZE);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.main_cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = 0;//(screenW / 3 - bmpW) / 2;
        bmpW = screenW / MENU_SIZE;
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

    @Override
    protected void onPause() {
        super.onPause();
        NetUtils.unregistCallbackListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetUtils.registNetCallbackListener(this);
        AppAplication app = (AppAplication) getApplication();
        if (app._isJumpToReapirFragment) {
            mPager.setCurrentItem(2);
            app._isJumpToReapirFragment = false;
        }
    }

    void checkUpdate() {

//        LogUtils.e("检查更新");

        Map<String, Object> params = new HashMap<>();
        params.put("uc", UserInfo.UserCode);
        params.put("type", "1");
        NetUtils.executPost(this,Constants.CODE_GET_UPDATE, "GetVersionInfo", params, ResultUpdate.class);

    }

    @Override
    public void callback(int code, BaseModel model) {
        if (model == null) return;
        if (Constants.CODE_GET_UPDATE == code) {
            try {
                SharedPreferences.Editor edit = getSharedPreferences("uinfo", Context.MODE_PRIVATE).edit();
                edit.putLong(Constants.KEY_SP_LAST_CHECK_UPDATE_TIME, System.currentTimeMillis());
                edit.putBoolean(Constants.KEY_SP_CAN_UPDATE, true);

                final ResultUpdate result = (ResultUpdate) model;
                int localVersion = CommonUtils.getLocalVersionCode(this);

                int remoteVersion = Integer.parseInt(result.getVersion());
                int forceVersion = Integer.parseInt(result.getForceUpdate());

            LogUtils.e("收到更新:本地版本:"+localVersion+"---远端版本:"+remoteVersion+"---强制版本:"+forceVersion);

                if (localVersion <= forceVersion && forceVersion != 0) {
                    //强制更新
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LogUtils.e("强制更新");
                    builder.setTitle("更新");
                    builder.setMessage("软件版本太低,为保证您能正常使用软件,请更新到最新版");
                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NetUtils.startDownLoad(MainActivity.this, result.getAppPath(), result.getVersion(),true);
                        }
                    });
                    builder.setNeutralButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            System.exit(0);
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                } else if (localVersion < remoteVersion) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //有升级
//                LogUtils.e("有升级");
                    edit.putBoolean(Constants.KEY_SP_CAN_UPDATE, true);
                    builder.setTitle("有新版本");
                    builder.setMessage("版本：" + result.getName() + "\n描述：" + Html.fromHtml(result.getDescription()).toString());
                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                        NetUtils.startWebsite(MainActivity.this, result.getAppPath());
                            NetUtils.startDownLoad(MainActivity.this, result.getAppPath(), result.getVersion(),false);
                        }
                    });
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                } else {
                    //无升级

                    edit.putBoolean(Constants.KEY_SP_CAN_UPDATE, false);
                }

                edit.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;


        @Override
        public void onPageSelected(final int arg0) {
            mainBox.clearFocus();

            if (arg0 == 2) {
                RepairFragment fragment = (RepairFragment) fragmentArray[2];
                fragment.initSendFragment();
            }
            Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);


            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

//                    LogUtils.e(currIndex + "--" + arg0);

                    menus.get(currIndex).setSelected(false);

                    menus.get(currIndex).setTextColor(getResources().getColor(R.color.bottomGuide_textColor1));
                    menus.get(arg0).setTextColor(getResources().getColor(R.color.bottomGuide_textColor2));

                    menus.get(arg0).setSelected(true);
                    currIndex = arg0;

                }
            });


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
}

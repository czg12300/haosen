package com.heneng.heater.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultGetNowDataDRL;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.HeaderView;
import com.igexin.sdk.PushManager;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/11/20.
 */
public class WaterHeaterActivity extends BaseNetActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener {
    @InjectView(R.id.water_hearter_im_menu_more)
    ImageView im_menu_more;
    @InjectView(R.id.water_hearter_im_menu_power)
    ImageView im_menu_power;

    @InjectView(R.id.water_hearter_tv_info)
    TextView tv_info;
    @InjectView(R.id.water_hearter_tv_curtemperature)
    TextView tv_cur_temp;
    @InjectView(R.id.water_hearter_tv_water_stay)
    TextView tv_water_stay;
    @InjectView(R.id.water_hearter_tv_heart_time)
    TextView tv_heat_time;
    @InjectView(R.id.water_hearter_tv_temcontrol_title)
    TextView tv_temcontrol;
    @InjectView(R.id.water_hearter_im_temcontrol)
    ImageView im_temp_scale;
    @InjectView(R.id.water_hearter_tv_cursor)
    TextView tv_temp_cursor;
    @InjectView(R.id.water_hearter_tv_night_heat)
    TextView tv_night_heat;

    @InjectView(R.id.water_hearter_tv_bathnum)
    TextView tv_bathnum;
    @InjectView(R.id.water_hearter_tv_voice)
    TextView tv_voice;
    @InjectView(R.id.water_hearter_tv_date)
    TextView tv_date;
    @InjectView(R.id.water_hearter_tv_mode)
    TextView tv_model;

    PopupWindow _popWindow;
    @InjectView(R.id.water_timepicker_tv_time)
    TextView timepicker_tv_time;
    @InjectView(R.id.water_timepicker_tp_start)
    TimePicker timepicker_tp_start;
    @InjectView(R.id.water_timepicker_tp_stop)
    TimePicker timepicker_tp_stop;
    @InjectView(R.id.water_timepicker_tv_save)
    TextView timepicker_tv_save;
    @InjectView(R.id.water_hearter_rl_timepicker)
    LinearLayout timepicker_rl;
    @InjectView(R.id.water_bathnum_tv_two)
    TextView bathnum_tv_two;
    @InjectView(R.id.water_bathnum_tv_one)
    TextView bathnum_tv_one;
    @InjectView(R.id.water_bathnum_tv_three)
    TextView bathnum_tv_three;
    @InjectView(R.id.water_bathnum_tv_ok)
    TextView bathnum_tv_ok;
    @InjectView(R.id.water_bathnum_tv_cancel)
    TextView bathnum_tv_cancel;
    @InjectView(R.id.water_rl_humancount)
    RelativeLayout bathnum_rl;
    @InjectView(R.id.water_capacity_tv_full)
    TextView capacity_tv_full;
    @InjectView(R.id.water_capacity_tv_half)
    TextView capacity_tv_half;
    @InjectView(R.id.water_capacity_tv_ok)
    TextView capacity_tv_ok;
    @InjectView(R.id.water_capacity_tv_cancel)
    TextView capacity_tv_cancel;
    @InjectView(R.id.water_rl_capacity)
    RelativeLayout capacity_rl;
    @InjectView(R.id.water_mode_tv_nomal)
    TextView mode_tv_nomal;
    @InjectView(R.id.water_mode_tv_midtemp)
    TextView mode_tv_midtemp;
    @InjectView(R.id.water_mode_tv_moringbath)
    TextView mode_tv_morningbath;
    @InjectView(R.id.water_mode_tv_fastheat)
    TextView mode_tv_fastheat;
    @InjectView(R.id.water_mode_tv_ok)
    TextView mode_tv_ok;
    @InjectView(R.id.water_mode_tv_cancel)
    TextView mode_tv_cancel;
    @InjectView(R.id.water_rl_mode)
    RelativeLayout mode_rl;
    @InjectView(R.id.water_rl_member)
    RelativeLayout member_rl;
    @InjectView(R.id.water_mode_tv_member1)
    TextView mode_tv_m1;
    @InjectView(R.id.water_mode_tv_member2)
    TextView mode_tv_m2;
    @InjectView(R.id.water_mode_tv_member3)
    TextView mode_tv_m3;
    @InjectView(R.id.water_mode_tv_membercan)
    TextView mode_tv_mc;
    private int _startHour, _stratMinu, _stopHuor, _stourMinu;


    public int _tempMax = 120;
    public int _tempMin = 35;
    public int moveMin;
    public int moveMax;
    public float moveItem;
    public int _curTemp;


    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_water_hearter);
        ButterKnife.inject(this);


        initViewAndListener();


    }

    @Override
    protected void onPause() {
        super.onPause();
        _handler.removeCallbacks(_getNowDataRunnable);
        unregisterReceiver(_receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.BROCAST_PUSH_STATE);
        registerReceiver(_receiver, filter);
        refressDataNow(false);
    }

    BroadcastReceiver _receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.BROCAST_PUSH_STATE)) {
                String msg = (String) intent.getSerializableExtra(Constants.KEY_EXTRA_PUSH_STATE);
                Gson gson = new Gson();
                ResultGetNowDataDRL state = gson.fromJson(msg, ResultGetNowDataDRL.class);
                pushCallback(state);
            }
        }
    };

    public void pushCallback(ResultGetNowDataDRL data) {
        refressDataNow(false);
        if (data == null) return;
        _handler.removeCallbacks(_timeoutCmdRunnable);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initViewAndListener() {


        header.setLeftListener(new HeaderView.onClickLeftListener() {
            @Override
            public void onLeftClick(View view) {
                goBack();
            }
        });

        im_menu_more.setOnClickListener(this);
        im_menu_power.setOnClickListener(this);
        tv_night_heat.setOnClickListener(this);

        tv_bathnum.setOnClickListener(this);
        tv_voice.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_model.setOnClickListener(this);


        //更多
        View popView = View.inflate(mBaseActivity, R.layout.a1_detail_menu, null);

        TextView tv_edit = (TextView) popView.findViewById(R.id.detail_menu_edit);
        tv_edit.setOnClickListener(this);
        TextView tv_refress = (TextView) popView.findViewById(R.id.detail_menu_refress);
        tv_refress.setOnClickListener(this);
        TextView tv_repair = (TextView) popView.findViewById(R.id.detail_menu_repair);
        tv_repair.setOnClickListener(this);
        TextView tv_smartconfig = (TextView) popView.findViewById(R.id.detail_menu_smartconfig);
        tv_smartconfig.setOnClickListener(this);

        setDrawableLeft(R.drawable.submenu_edit, tv_edit);
        setDrawableLeft(R.drawable.submenu_refress, tv_refress);
        setDrawableLeft(R.drawable.submenu_repair, tv_repair);
        setDrawableLeft(R.drawable.submenu_smartconfig, tv_smartconfig);


        ViewGroup.LayoutParams params = popView.getLayoutParams();
        int width = (int) getResources().getDimension(R.dimen.detail_top_menu_width);
        int height = (int) getResources().getDimension(R.dimen.detail_top_menu_height);
//        LogUtils.e("width:" + width + "---- height:" + height);

        _popWindow = new PopupWindow(width, height);
        _popWindow.setFocusable(true);
        _popWindow.setBackgroundDrawable(new BitmapDrawable());
        _popWindow.setContentView(popView);

        //时间选择
        timepicker_tp_start.setIs24HourView(true);
        timepicker_tp_start.setOnTimeChangedListener(this);
        timepicker_tp_stop.setIs24HourView(true);
        timepicker_tp_stop.setOnTimeChangedListener(this);
        timepicker_tv_save.setOnClickListener(this);
        //变容
        capacity_tv_cancel.setOnClickListener(this);
        capacity_tv_ok.setOnClickListener(this);
        capacity_tv_full.setOnClickListener(this);
        capacity_tv_half.setOnClickListener(this);

        //洗涮人数
        bathnum_tv_cancel.setOnClickListener(this);
        bathnum_tv_ok.setOnClickListener(this);
        bathnum_tv_one.setOnClickListener(this);
        bathnum_tv_three.setOnClickListener(this);
        bathnum_tv_two.setOnClickListener(this);

        //模式设置
        mode_tv_cancel.setOnClickListener(this);
        mode_tv_ok.setOnClickListener(this);
        mode_tv_nomal.setOnClickListener(this);
        mode_tv_morningbath.setOnClickListener(this);
        mode_tv_midtemp.setOnClickListener(this);
        mode_tv_fastheat.setOnClickListener(this);

        //晨浴人数设置
        mode_tv_m1.setOnClickListener(this);
        mode_tv_m2.setOnClickListener(this);
        mode_tv_m3.setOnClickListener(this);
        mode_tv_mc.setOnClickListener(this);

        //温度调整
        moveMin = CommonFunction.dip2px(mBaseActivity, 4);
        moveMax = CommonFunction.dip2px(mBaseActivity, 164);
        moveItem = (float) (moveMax - moveMin) / (float) (_tempMax - _tempMin);
        tv_temp_cursor.setOnTouchListener(new View.OnTouchListener() {
            public int lastY;


            /*
                  * 根据位置返回温度值
                  */
            private int getTempByPosition(int post) {
                if (post < moveMin) post = moveMin;
                if (post > moveMax) post = moveMax;
                int temp = _tempMax - Math.round((post - moveMin) / moveItem);

                if (temp < _tempMin)
                    temp = _tempMin;
                else if (temp > _tempMax)
                    temp = _tempMax;
                return temp;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("tempfragment", "ACTION_DOWN");
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //Log.d("tempfragment", "ACTION_MOVE");
                        int dy = (int) event.getRawY() - lastY;
                        RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) tv_temp_cursor.getLayoutParams();
                        int target = paras.topMargin + dy;

//                        LogUtils.e(tv_temp_cursor.getHeight() + "---" + im_temp_scale.getHeight() + "---target:" + target + "---max:" + moveMax + "---min:" + moveMin);

                        if (target < moveMin) {
                            target = moveMin;
                        } else if (target > moveMax) {
                            target = moveMax;
                        }
                        paras.setMargins(paras.leftMargin, target, 0, 0);
                        tv_temp_cursor.setLayoutParams(paras);
                        _curTemp = getTempByPosition(target);
                        tv_temp_cursor.setText("当前温度\n" + _curTemp + "℃");
//                        SetTempNum(TempVal + "");
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        //Log.d("tempfragment", "ACTION_CANCEL");
                    case MotionEvent.ACTION_UP:
                        submitTemp(_curTemp);

                        break;

                }

                return true;
            }
        });
    }

    private void goBack() {
        if (timepicker_rl.getVisibility() == View.VISIBLE) {
            timepicker_rl.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        if (view.getId() == R.id.water_timepicker_tp_start) {
            _startHour = view.getCurrentHour();
            _stratMinu = view.getCurrentMinute();
        } else {
            _stopHuor = view.getCurrentHour();
            _stourMinu = view.getCurrentMinute();
        }
        timepicker_tv_time.setText(String.format("%02d", _startHour) + ":" + String.format("%02d", _stratMinu) + "-" + String.format("%02d", _stopHuor) + ":" + String.format("%02d", _stourMinu));
    }

    private void setDrawableLeft(int drawbleRes, TextView tv) {
        Drawable devices = getResources().getDrawable(drawbleRes);
        int size = (int) getResources().getDimension(R.dimen.main_submenu_drawable_size);
        devices.setBounds(0, 0, size, size);
        tv.setCompoundDrawables(devices, null, null, null);
    }


    Runnable _timeoutCmdRunnable = new Runnable() {

        @Override
        public void run() {
//            closeLoading();
            Toast.makeText(mBaseActivity, "命令操作超时", Toast.LENGTH_SHORT).show();
            refressDataNow(false);
        }
    };

    public boolean sendCmd(int code, String method, Map<String, Object> params, Class<ResultDetail> clazz) {
        if (_app._chooseDevice.getOnLine().equals("0")) {
            Toast.makeText(this, "这个设备不在线", Toast.LENGTH_SHORT).show();
            return false;
        }


//        showLoading();

        _handler.removeCallbacks(_getNowDataRunnable);
        _handler.postDelayed(_getNowDataRunnable, 30000);
        _handler.postDelayed(_timeoutCmdRunnable, 10000);

        NetUtils.executGet(mBaseActivity, code, _app._chooseDevice.getInterface(), method, params, clazz);
        return true;
    }

    public void refressDataNow(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        _handler.removeCallbacks(_getNowDataRunnable);
        _handler.post(_getNowDataRunnable);
    }

    Runnable _getNowDataRunnable = new Runnable() {
        @Override
        public void run() {
            getNowData();
            _handler.postDelayed(_getNowDataRunnable, 20000);
        }
    };


    private void getNowData() {
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("clientMac", PushManager.getInstance().getClientid(mBaseActivity));

        NetUtils.executGet(mBaseActivity, Constants.CODE_GET_GETNOWDATA_DRL, _app._chooseDevice.getInterface(), "GetNowData_DRL", paramsMap, ResultGetNowDataDRL.class);
//        showLoading();
    }

    public void refressView(ResultGetNowDataDRL nowDate) {
        if (nowDate == null) return;
        tv_info.setText("当前状态：" + nowDate.getTextData());
        tv_cur_temp.setText(nowDate.getWaterOutTemp() + "℃");

        tv_water_stay.setText(nowDate.getSurplusWater());
        tv_heat_time.setText(nowDate.getHeaterTimer());


        im_menu_power.setSelected("1".equals(nowDate.getOnOff()));

        int temp = Integer.parseInt(nowDate.getTempSet());
        setTempCursorPositionByTemp(temp);

        switch (nowDate.getNumber()) {
            case "1":
                selectBathnumOne();
                break;
            case "2":
                selectBathnumTwo();
                break;
            default:
                selectBathnumThree();
                break;
        }

        switch (nowDate.getGear()) {
            case "0":
                selectCapacityHalf();
                break;
            default:
                selectCapacityFull();
                break;
        }

        timepicker_tp_start.setCurrentHour(Integer.parseInt(nowDate.getTimerHour()));
        timepicker_tp_start.setCurrentMinute(Integer.parseInt(nowDate.getTimerMin()));
        timepicker_tp_stop.setCurrentHour(Integer.parseInt(nowDate.getTimerHour2()));
        timepicker_tp_stop.setCurrentHour(Integer.parseInt(nowDate.getTimerMin2()));


        if ("1".equals(nowDate.getNormal())) selectModeNomal();
        if ("1".equals(nowDate.getMidTemp())) selectModeMidTemp();
        if ("1".equals(nowDate.getMorningBar())) selectModeMorningBath();

        tv_night_heat.setSelected("1".equals(nowDate.getNightHeating()));
        tv_voice.setSelected("1".equals(nowDate.getVoice()));

    }

    /**
     * 通过温度值设定温度游标的位置
     *
     * @param temp
     */
    private void setTempCursorPositionByTemp(int temp) {
        _curTemp = temp;
        RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) tv_temp_cursor.getLayoutParams();
        int target = moveMax - Math.round((temp - _tempMin) * moveItem);
        paras.setMargins(paras.leftMargin, target, 0, 0);
        tv_temp_cursor.setText("当前温度\n" + temp + "℃");
        tv_temp_cursor.setLayoutParams(paras);
    }


    @Override
    public void callback(int code, BaseModel model) {
        closeLoading();
        if (code == Constants.CODE_GET_GETNOWDATA_DRL) {

            ResultGetNowDataDRL nowData = (ResultGetNowDataDRL) model;
            refressView(nowData);


        } else if (code == Constants.CODE_SET_ONOFF) {
            setState((ResultDetail) model, im_menu_power, R.drawable.hearter_selector_power);
        } else if (code == R.id.water_hearter_tv_night_heat) {
            setState((ResultDetail) model, tv_night_heat, R.drawable.hearter_selector_icon);
        }else if (code == R.id.water_hearter_tv_voice) {
            setState((ResultDetail) model, tv_voice, R.drawable.hearter_selector_icon);
        } else if (code == R.id.water_hearter_tv_date) {
            setState((ResultDetail) model, tv_date, R.drawable.hearter_selector_icon);
        } else if (code == R.id.water_hearter_tv_bathnum) {
            setState((ResultDetail) model, tv_bathnum, R.drawable.hearter_selector_icon);
        }
    }

    private void setState(ResultDetail result, View view, int resId) {
        if (result == null) {
            return;
        }
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
        } else {
            view.setBackgroundResource(resId);
        }

        if ("1".equals(result.getResult())) {
            view.setSelected(!view.isSelected());
//            Toast.makeText(mBaseActivity, "指令发送成功", Toast.LENGTH_SHORT).show();
        } else {
            view.setSelected(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.water_hearter_im_menu_more:
                if (_popWindow.isShowing()) {
                    _popWindow.dismiss();
                } else {
                    _popWindow.showAsDropDown(v);
                }
                break;
            case R.id.water_hearter_im_menu_power:
                switchPowerState();
                break;
            case R.id.water_hearter_tv_night_heat:
                postState(v, "Com_SetNightHeating_DRL");
                break;

            case R.id.water_capacity_tv_half:
                selectCapacityHalf();
                break;
            case R.id.water_capacity_tv_full:
                selectCapacityFull();
                break;
            case R.id.water_capacity_tv_cancel:
                capacity_rl.setVisibility(View.GONE);
                break;
            case R.id.water_capacity_tv_ok:
                submitCapacity();
                break;
            case R.id.water_hearter_tv_bathnum:
                bathnum_rl.setVisibility(View.VISIBLE);
                break;
            case R.id.water_bathnum_tv_one:
                selectBathnumOne();
                break;
            case R.id.water_bathnum_tv_two:
                selectBathnumTwo();
                break;
            case R.id.water_bathnum_tv_three:
                selectBathnumThree();
                break;
            case R.id.water_bathnum_tv_ok:
                submitBathNum();
                break;
            case R.id.water_bathnum_tv_cancel:
                bathnum_rl.setVisibility(View.GONE);
                break;
            case R.id.water_hearter_tv_voice:
                postState(v, "Com_SetVoice_DRL");
                break;
            case R.id.water_hearter_tv_date:
                timepicker_rl.setVisibility(View.VISIBLE);
                break;
            case R.id.water_hearter_tv_mode:
                mode_rl.setVisibility(View.VISIBLE);
                break;
            case R.id.water_mode_tv_nomal:
                selectModeNomal();
                break;
            case R.id.water_mode_tv_midtemp:
                selectModeMidTemp();
                break;
            case R.id.water_mode_tv_moringbath:
                member_rl.setVisibility(View.VISIBLE);

                break;
            case R.id.water_mode_tv_fastheat:
                selectModeFastHeat();
                break;
            case R.id.water_mode_tv_ok:
                submitMode();
                break;
            case R.id.water_mode_tv_cancel:
                mode_rl.setVisibility(View.GONE);
                break;
            case R.id.detail_menu_refress:
                refressDataNow(true);
                _popWindow.dismiss();
                break;
            case R.id.water_timepicker_tv_save:
                submitDateTime();
                break;

            case R.id.detail_menu_edit:
                Intent intent1 = new Intent();
                intent1.setClass(mBaseActivity, EditDeviceInfoActivity.class);
                startActivity(intent1);
                _popWindow.dismiss();
                break;
            case R.id.detail_menu_smartconfig:
                Intent intent = new Intent();
                intent.setClass(mBaseActivity, SmartConfigWifiActivity.class);
                startActivity(intent);
                _popWindow.dismiss();
                break;
            case R.id.detail_menu_repair:
                _app._isJumpToReapirFragment = true;
                finish();
                break;
            case R.id.water_mode_tv_membercan:
                member_rl.setVisibility(View.GONE);


        }
    }


    private void selectModeFastHeat() {
        mode_tv_nomal.setSelected(false);
        mode_tv_midtemp.setSelected(false);
        mode_tv_morningbath.setSelected(false);
        mode_tv_fastheat.setSelected(true);
    }

    private void selectModeMorningBath() {
        mode_tv_nomal.setSelected(false);
        mode_tv_midtemp.setSelected(false);
        mode_tv_morningbath.setSelected(true);
        mode_tv_fastheat.setSelected(false);
    }

    private void selectModeMidTemp() {
        mode_tv_nomal.setSelected(false);
        mode_tv_midtemp.setSelected(true);
        mode_tv_morningbath.setSelected(false);
        mode_tv_fastheat.setSelected(false);
    }

    private void selectModeNomal() {
        mode_tv_nomal.setSelected(true);
        mode_tv_midtemp.setSelected(false);
        mode_tv_morningbath.setSelected(false);
        mode_tv_fastheat.setSelected(false);
    }

    private void selectBathnumThree() {
        bathnum_tv_one.setSelected(false);
        bathnum_tv_two.setSelected(false);
        bathnum_tv_three.setSelected(true);
    }

    private void selectBathnumTwo() {
        bathnum_tv_one.setSelected(false);
        bathnum_tv_two.setSelected(true);
        bathnum_tv_three.setSelected(false);
    }

    private void selectBathnumOne() {
        bathnum_tv_one.setSelected(true);
        bathnum_tv_two.setSelected(false);
        bathnum_tv_three.setSelected(false);
    }

    private void selectCapacityFull() {
        capacity_tv_full.setSelected(true);
        capacity_tv_half.setSelected(false);
    }

    private void selectCapacityHalf() {
        capacity_tv_half.setSelected(true);
        capacity_tv_full.setSelected(false);
    }

    private void submitMode() {
        String mode = "00";
        if (mode_tv_midtemp.isSelected()) {
            mode = "01";
        } else if (mode_tv_morningbath.isSelected()) {
            mode = "02";
        } else if (mode_tv_fastheat.isSelected()) {
            mode = "03";
        }
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("num", mode);
//        NetUtils.executPost(R.id.water_hearter_tv_bathnum, _app._chooseDevice.getInterface(), "EleStove_SetBathNum", paramsMap, ResultDetail.class);
        if (sendCmd(R.id.water_hearter_tv_bathnum, "Com_SetWorkMode_DRL", paramsMap, ResultDetail.class)) {
            tv_model.setBackgroundResource(R.drawable.hearter_icon_yellow);
        }

        mode_rl.setVisibility(View.GONE);
    }

    private void submitTemp(float temp) {

        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("temp", temp);
        LogUtils.e("设置温度值：" + temp);
//        NetUtils.executPost(R.id.water_hearter_tv_capacity,_app._chooseDevice.getInterface() ,"EleStove_SetCapacity", paramsMap, ResultDetail.class);
        if (sendCmd(R.id.water_hearter_im_temcontrol, "Com_SetTemp_DRL", paramsMap, ResultDetail.class)) {

        }

    }

    private void submitCapacity() {
        String onoff = capacity_tv_half.isSelected() ? "1" : "0";
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("onoff", onoff);
        LogUtils.e("设置容量值：" + onoff);
//        NetUtils.executPost(R.id.water_hearter_tv_capacity,_app._chooseDevice.getInterface() ,"EleStove_SetCapacity", paramsMap, ResultDetail.class);
//        if (sendCmd(R.id.water_hearter_tv_capacity, "Com_SetGear_DRL", paramsMap, ResultDetail.class)) {
//            tv_capacity.setBackgroundResource(R.drawable.hearter_icon_yellow);
//        }
        capacity_rl.setVisibility(View.GONE);
    }

    private void submitBathNum() {
        String bathNum = "1";
        if (bathnum_tv_two.isSelected()) {
            bathNum = "2";
        } else if (bathnum_tv_three.isSelected()) {
            bathNum = "3";
        }
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("num", bathNum);
//        NetUtils.executPost(R.id.water_hearter_tv_bathnum, _app._chooseDevice.getInterface(), "EleStove_SetBathNum", paramsMap, ResultDetail.class);
        if (sendCmd(R.id.water_hearter_tv_bathnum, "Com_SetNumber_DRL", paramsMap, ResultDetail.class)) {
            tv_bathnum.setBackgroundResource(R.drawable.hearter_icon_yellow);
        }

        bathnum_rl.setVisibility(View.GONE);
    }

    private void submitDateTime() {
        timepicker_rl.setVisibility(View.GONE);
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("temp", _curTemp);
        paramsMap.put("on_hour", String.format("%02d", _startHour));
        paramsMap.put("on_minute", String.format("%02d", _stratMinu));
        paramsMap.put("off_hour", String.format("%02d", _stopHuor));
        paramsMap.put("off_minute", String.format("%02d", _stourMinu));
//        NetUtils.executPost(mBaseActivity,R.id.water_hearter_tv_date, _app._chooseDevice.getInterface(), "EleStove_SetOrderTime", paramsMap, ResultDetail.class);
        if (sendCmd(R.id.water_hearter_tv_date, "Com_SetHeaterTimerPredict_DRL", paramsMap, ResultDetail.class)) {
//            tv_bathnum.setBackgroundResource(R.drawable.hearter_icon_yellow);
        }
    }

    private void postState(View view, String method) {
        String onoff = view.isSelected() ? "0" : "1";
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("onoff", onoff);
//        NetUtils.executPost(view.getId(), _app._chooseDevice.getInterface(), method, paramsMap, ResultDetail.class);
        if (sendCmd(view.getId(), method, paramsMap, ResultDetail.class)) {
            view.setBackgroundResource(R.drawable.hearter_icon_yellow);
        }

    }

    /**
     * 切换开关状态
     */
    private void switchPowerState() {
        String onoff = im_menu_power.isSelected() ? "0" : "1";
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("onoff", onoff);
//        NetUtils.executPost(Constants.CODE_SET_ONOFF, _app._chooseDevice.getInterface(), "EleStove_SetOnOff", paramsMap, ResultDetail.class);

        if (sendCmd(Constants.CODE_SET_ONOFF, "Com_SetOnOff_DRL", paramsMap, ResultDetail.class)) {
            im_menu_power.setImageResource(R.drawable.hearter_power_stating);
        }

    }


}

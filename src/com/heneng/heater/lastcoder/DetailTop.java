package com.heneng.heater.lastcoder;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.heneng.heater.R;

import com.heneng.heater.activitys.EditDeviceInfoActivity;
import com.heneng.heater.activitys.SmartConfigWifiActivity;
import com.heneng.heater.fragments.BaseNetFragment;
import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;

import org.json.JSONObject;

import java.util.Map;


public class DetailTop extends BaseNetFragment implements OnClickListener {

    ImageView ReturnBtn;
    ImageView OnOffBtn;
    ImageView im_online;

    TextView equiName;
    ResultGetNowData Data = null;

    Animation refreshAnim;
    int runningRefresh = 0;
    DetailActivity act;

    //After add
    PopupWindow _popWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_top, container, false);
        act = (DetailActivity) getActivity();
        equiName = (TextView) view.findViewById(R.id.detailTop_equiName);
        equiName.setText(_app._chooseDevice.getEname());
        ReturnBtn = (ImageView) view.findViewById(R.id.detailTop_return);
        im_online = (ImageView) view.findViewById(R.id.detailTop_online);
        ReturnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), MainActivity.class);
                //startActivity(intent);
//                act.ShowLoadingDetail();

                act.finish();

//                getActivity().finish();
            }
        });


        OnOffBtn = (ImageView) view.findViewById(R.id.detailTop_onOff);
        OnOffBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!CheckFastClick.IsFastClick() && !act.CheckIsRunningTask(false)) {
//                    act.CheckIsRunningDataTask(true);
//                    AsyncOnOffTask asy = new AsyncOnOffTask();
//                    act.oneTask = asy;
//                    asy.execute();
//                }
                if (Data != null) {

                    int onoff = OnOffBtn.isSelected() ? 0 : 1;
                    Map<String, Object> params = getParamsMap();
                    params.put("uc", UserInfo.UserCode);
                    params.put("user", UserInfo.ID);
                    params.put("eid", _app._chooseDevice.getEID());
                    params.put("onoff", onoff);
//                    NetUtils.executGet(Constants.CODE_SET_ONOFF, "Com_SetOnOff", params, ResultDetail.class);

                    if (act.sendCmd(Constants.CODE_SET_ONOFF, "Com_SetOnOff", params, ResultDetail.class)) {
                        OnOffBtn.setSelected(!OnOffBtn.isSelected());
                    }

                }
            }
        });


        initAnimation();

        //After add
        View popView = View.inflate(act, R.layout.a1_detail_menu, null);

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
        int width = (int) act.getResources().getDimension(R.dimen.detail_top_menu_width);
        int height = (int) act.getResources().getDimension(R.dimen.detail_top_menu_height);
//        LogUtils.e("width:" + width + "---- height:" + height);

        _popWindow = new PopupWindow(width, height);
        _popWindow.setFocusable(true);
        _popWindow.setBackgroundDrawable(new BitmapDrawable());
        _popWindow.setContentView(popView);


        view.findViewById(R.id.detailTop_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_popWindow.isShowing()) {
                    _popWindow.dismiss();
                } else {
                    _popWindow.showAsDropDown(view);
                }


            }
        });


        return view;
    }


    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_SET_ONOFF) {
            ResultDetail result = (ResultDetail) model;
            if ("1".equals(result.getResult())) {

//                OnOffBtn.setSelected(!OnOffBtn.isSelected());
//                Toast.makeText(act, "指令发送成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(act, result.getInfo(), Toast.LENGTH_SHORT).show();
            }
            act.CloseLoadingDetail();
//            act.refressDataNow();
        }
    }


    private void setDrawableLeft(int drawbleRes, TextView tv) {
        Drawable devices = getResources().getDrawable(drawbleRes);
        int size = (int) getResources().getDimension(R.dimen.main_submenu_drawable_size);
        devices.setBounds(0, 0, size, size);
        tv.setCompoundDrawables(devices, null, null, null);
    }


    private void clickSmartConfig() {
//        if (!CheckFastClick.IsFastClick()) {
//            Intent intent = new Intent(getActivity(), SmartConfigActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("eid", Data.getEID());
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
    }

    private void clickRefress() {
//        if (!CheckFastClick.IsFastClick()) {
//            act.GetDetailDataHandler(true);
//        }
        act.refressDataNow();
    }

    public void pushCallback(ResultGetNowData data) {

        BindData(data);

    }

    public void BindData(ResultGetNowData data) {
        if (data != null) {
            Data = data;
            OnOffBtn.setSelected("1".equals(Data.getOnoff()));
            im_online.setSelected("1".equals(Data.getOnLine()));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_menu_refress:
                clickRefress();
                break;

            case R.id.detail_menu_edit:
                Intent intent1 = new Intent();
                intent1.setClass(act, EditDeviceInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.detail_menu_smartconfig:
                Intent intent = new Intent();
                intent.setClass(act, SmartConfigWifiActivity.class);
                startActivity(intent);

                break;
            case R.id.detail_menu_repair:
                AppAplication app = (AppAplication) getActivity().getApplication();
                app._isJumpToReapirFragment = true;
                getActivity().finish();
                break;

        }
        _popWindow.dismiss();
    }


//    class AsyncOnOffTask extends AsyncTask<Void, Void, PostResult> {
//
//
//        public AsyncOnOffTask() {
//            super();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            act.ShowLoadingDetail();
//            OnOffBtn.setImageResource(R.drawable.waiting);
//        }
//
//        @Override
//        protected PostResult doInBackground(Void... params) {
//            PostResult result = new PostResult();
//            try {
//                if (Data != null) {
//                    int _onoff = "1".equals(Data.getOnoff()) ? 0 : 1;
////					String strUrl = ServerInfo.WebApi+"/AppHandler.ashx?Method=SettingCommand&uc="
////							+UserInfo.UserCode+"&eid="+Data.EID+"&minor_type=22&message="+_onoff;
//                    String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=Com_SetOnOff&uc="
//                            + UserInfo.UserCode + "&eid=" + Data.getEID() + "&user=" + UserInfo.ID + "&onoff=" + _onoff;
//                    String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
//                    if (!jstr.equals("")) {
//                        JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
//                        String _r = jsObj.getString("Result");
//                        String _info = jsObj.getString("Info");
//                        if (_r.equals("1")) {
//                            int postcount = 0;
//                            while (true) {
//                                strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=GetSettingResult&uc="
//                                        + UserInfo.UserCode + "&eid=" + Data.getEID() + "&setID=" + _info;
//                                jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
//                                if (!jstr.equals("")) {
//                                    jsObj = GetRemoteData.ParseJson(jstr, getActivity());
//                                    int _r2 = Integer.parseInt(jsObj.getString("Result"));
//                                    if (_r2 == 2) {
//                                        result.Result = "1";
//                                        Data.setOnLine("1");
//                                        break;
//                                    } else if (_r2 == 3) {
//                                        result.Result = "0";
//                                        result.Info = jsObj.getString("Info");
//                                        break;
//                                    }
//                                }
//                                postcount++;
//                                if (postcount > AppParams.ExcuteCommandWait) {
//                                    result.Result = "0";
//                                    result.Info = "命令执行长时间没有响应";
//                                    break;
//                                }
//                                Thread.sleep(1000);
//                            }
//                        } else {
//                            result.Result = "0";
//                            result.Info = _info;
//                        }
//                    }
//                } else {
//                    result.Result = "0";
//                    result.Info = "设备还没有连上服务器，暂时不能对设备进行控制。";
//                }
//            } catch (Exception e) {
//                result.Result = "0";
//                result.Info = "数据获取异常:" + e.getMessage();
//
//            } finally {
//                return result;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(PostResult result) {
//            super.onPostExecute(result);
//            if (result.Result.equals("0")) {
//                Toast.makeText(getActivity(), result.Info, Toast.LENGTH_SHORT).show();
//            }
//
//            int icoImg = R.drawable.close;
//            if (Data != null) {
//                icoImg = "1".equals(Data.getOnoff()) ? R.drawable.open : R.drawable.close;
//                OnOffBtn.setImageResource(icoImg);
//            }
//
//            act.oneTask = null;
//            act.GetDetailDataHandler(false);
//        }
//    }


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

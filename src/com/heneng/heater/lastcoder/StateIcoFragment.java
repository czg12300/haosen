package com.heneng.heater.lastcoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.fragments.BaseNetFragment;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.model.MonitorInfo;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultErrorDescription;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.NetUtils;

import java.util.Map;


public class StateIcoFragment extends BaseNetFragment {
    ImageView btn_dongji;
    ImageView btn_xiaji;
    ImageView btn_weiyu;
    ImageView btn_cainuan;
    ImageView btn_fangdong;
    ImageView btn_dingshi;
    ImageView btn_error;
    ResultGetNowData Data = null;
    DetailActivity act;
    AlertDialog _alertDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = (DetailActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frament_stateico, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_dongji = (ImageView) view.findViewById(R.id.fragment_stateico1);
        btn_xiaji = (ImageView) view.findViewById(R.id.fragment_stateico2);
        btn_weiyu = (ImageView) view.findViewById(R.id.fragment_stateico3);
        btn_cainuan = (ImageView) view.findViewById(R.id.fragment_stateico4);
        btn_fangdong = (ImageView) view.findViewById(R.id.fragment_stateico5);
        btn_dingshi = (ImageView) view.findViewById(R.id.fragment_stateico6);
        btn_error = (ImageView) view.findViewById(R.id.fragment_stateico7);

//        if (MonitorInfo.Width == 360) return;

        int btnWidth = CommonFunction.dip2px(getActivity(), 42);
        if (MonitorInfo.Width <= 320) {
            btnWidth = CommonFunction.dip2px(getActivity(), 40);
        } else if (MonitorInfo.Width >= 480) {
            btnWidth = CommonFunction.dip2px(getActivity(), 50);
        }
        LinearLayout.LayoutParams paras = (LinearLayout.LayoutParams) btn_dongji.getLayoutParams();
        paras.width = btnWidth;
        paras.height = btnWidth;
        btn_dongji.setLayoutParams(paras);
        btn_xiaji.setLayoutParams(paras);
        btn_weiyu.setLayoutParams(paras);
        btn_cainuan.setLayoutParams(paras);
        btn_fangdong.setLayoutParams(paras);
        btn_dingshi.setLayoutParams(paras);
        btn_error.setLayoutParams(paras);

        btn_dongji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btn_dongji.isSelected()) return;

                Map<String, Object> params = getParamsMap();
                params.put("uc", UserInfo.UserCode);
                params.put("user", UserInfo.ID);
                params.put("eid", _app._chooseDevice.getEID());
                params.put("mode", "2");
//                NetUtils.executGet(Constants.CODE_SET_MODE_DONGJI, _app._chooseDevice.getInterface(), "Com_SetMode", params, ResultDetail.class);
                if (act.sendCmd(Constants.CODE_SET_MODE_DONGJI, "Com_SetMode", params, ResultDetail.class)) {
                    btn_dongji.setImageResource(R.drawable.state_ico1_2);
                    btn_xiaji.setSelected(false);
                }


            }
        });
        btn_xiaji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!CheckFastClick.IsFastClick() && !act.CheckIsRunningTask(false)) {
//                    act.CheckIsRunningDataTask(true);
//                    String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=Com_SetMode&uc="
//                            + UserInfo.UserCode + "&eid=" + Data.EID + "&user=" + UserInfo.ID + "&mode=1";
//                    //AsyncStateTask asy=new AsyncStateTask((ImageView)v,2,23,1,0);
//                    AsyncStateTask asy = new AsyncStateTask((ImageView) v, 2, strUrl);
//                    act.oneTask = asy;
//                    asy.execute();
//                }
                if(btn_xiaji.isSelected()) return;
                Map<String, Object> params = getParamsMap();
                params.put("uc", UserInfo.UserCode);
                params.put("user", UserInfo.ID);
                params.put("eid", _app._chooseDevice.getEID());
                params.put("mode", "1");
                if (act.sendCmd(Constants.CODE_SET_MODE_XIAJI, "Com_SetMode", params, ResultDetail.class)) {
                    btn_xiaji.setImageResource(R.drawable.state_ico2_2);
                    btn_dongji.setSelected(false);
                }
//                NetUtils.executGet(Constants.CODE_SET_MODE_XIAJI, _app._chooseDevice.getInterface(), "Com_SetMode", params, ResultDetail.class);

            }
        });
        btn_dingshi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!CheckFastClick.IsFastClick() && !act.CheckIsRunningTask(false)) {
//                    act.CheckIsRunningDataTask(true);
//                    int msg = (int) ((Data.DingShi == 1) ? 2 : 1);
//                    String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=Com_SetTimeOnOff&uc="
//                            + UserInfo.UserCode + "&eid=" + Data.EID + "&user=" + UserInfo.ID + "&onoff=" + msg;
//                    //AsyncStateTask asy=new AsyncStateTask((ImageView)v,6,33,msg,1);
//                    AsyncStateTask asy = new AsyncStateTask((ImageView) v, 6, strUrl);
//                    act.oneTask = asy;
//                    asy.execute();
//                }


                int onoff = "1".endsWith(Data.getDingShi()) ? 2 : 1;

                Map<String, Object> params = getParamsMap();
                params.put("uc", UserInfo.UserCode);
                params.put("user", UserInfo.ID);
                params.put("eid", _app._chooseDevice.getEID());
                params.put("onoff", onoff);
//                NetUtils.executGet(Constants.CODE_SET_TIMEONOFF, _app._chooseDevice.getInterface(), "Com_SetTimeOnOff", params, ResultDetail.class);
                if (act.sendCmd(Constants.CODE_SET_TIMEONOFF, "Com_SetTimeOnOff", params, ResultDetail.class)) {
                    btn_dingshi.setImageResource(R.drawable.state_ico6_2);
                }

            }
        });

        btn_error.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!"0".equals(Data.getError())) {

                    Map<String, Object> params = getParamsMap();
                    params.put("uc", UserInfo.UserCode);
                    params.put("user", UserInfo.ID);
                    params.put("errCode", Data.getError());

//                NetUtils.executGet(Constants.CODE_GET_ERROR_DESCRIPTION, _app._chooseDevice.getInterface(), "Com_ErrorDescription", params, ResultErrorDescription.class);
                    act.sendCmd(Constants.CODE_GET_ERROR_DESCRIPTION, "Com_ErrorDescription", params, ResultErrorDescription.class, false);
                } else {
                    Toast.makeText(mContext, "设备运转正常", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        _ViewCreated = true;
//        OpenErrWin();
    }

    public void pushCallback(ResultGetNowData data) {

        BindData(data);

    }

    public void BindData(ResultGetNowData data) {
        if (data == null) return;
        Data = data;
        btn_dongji.setImageResource(R.drawable.selector_state_dongji);
        btn_dongji.setSelected("1".equals(data.getDongJi()));
        btn_xiaji.setImageResource(R.drawable.selector_state_xiaji);
        btn_xiaji.setSelected(!btn_dongji.isSelected());

        int icoImg = "1".equals(data.getWeiYu()) ? R.drawable.state_ico3_1 : R.drawable.state_ico3_3;
        btn_weiyu.setImageResource(icoImg);
        icoImg = "1".equals(data.getCaiNuan()) ? R.drawable.state_ico4_1 : R.drawable.state_ico4_3;
        btn_cainuan.setImageResource(icoImg);
        icoImg = "1".equals(data.getFangDong()) ? R.drawable.state_ico5_1 : R.drawable.state_ico5_3;
        btn_fangdong.setImageResource(icoImg);
        icoImg = "1".equals(data.getDingShi()) ? R.drawable.state_ico6_1 : R.drawable.state_ico6_3;
        btn_dingshi.setImageResource(icoImg);
        icoImg = "0".equals(data.getError()) ? R.drawable.state_ico7_3 : R.drawable.state_ico7_1;
        btn_error.setImageResource(icoImg);

    }


    //设备报错时,处理函数
//    public void OpenErrWin() {
//        if (Data != null && Data.Error > 0 && _OpenErrWin == false) {
//            AsyncErrorTask errorTask = new AsyncErrorTask(btn_error);
//            errorTask.execute();
//        }
//    }


    @Override
    public void callback(int code, BaseModel model) {

        if (code == Constants.CODE_SET_MODE_DONGJI) {
            ResultDetail result = (ResultDetail) model;
            if ("1".equals(result.getResult())) {
                btn_dongji.setImageResource(R.drawable.selector_state_dongji);
                btn_dongji.setSelected(true);
//                Toast.makeText(act, "指令发送成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(act, result.getInfo(), Toast.LENGTH_SHORT).show();
            }
//            act.refressDataNow();
        } else if (code == Constants.CODE_SET_MODE_XIAJI) {
            ResultDetail result = (ResultDetail) model;
            if ("1".equals(result.getResult())) {
                btn_xiaji.setImageResource(R.drawable.selector_state_xiaji);
                btn_xiaji.setSelected(true);
            } else {
                Toast.makeText(act, result.getInfo(), Toast.LENGTH_SHORT).show();
            }
//            act.refressDataNow();
        } else if (code == Constants.CODE_SET_TIMEONOFF) {
            ResultDetail result = (ResultDetail) model;
            if ("1".equals(result.getResult())) {
                btn_dingshi.setImageResource(R.drawable.state_ico6_1);
//                Toast.makeText(act, "指令发送成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(act, result.getInfo(), Toast.LENGTH_SHORT).show();
            }
//            act.refressDataNow();
        } else if (code == Constants.CODE_GET_REESET) {
            ResultDetail result = (ResultDetail) model;
            if ("1".equals(result.getResult())) {
                btn_error.setImageResource(R.drawable.state_ico7_1);
//                Toast.makeText(act, "指令发送成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(act, result.getInfo(), Toast.LENGTH_SHORT).show();
            }
//            act.refressDataNow();
        } else if (code == Constants.CODE_GET_ERROR_DESCRIPTION) {
            act.CloseLoadingDetail();

            if (model == null) {
                btn_error.setImageResource(R.drawable.state_ico7_3);
                Toast.makeText(act, "未查询到详细故障信息", Toast.LENGTH_SHORT).show();
                return;
            }


            ResultErrorDescription result = (ResultErrorDescription) model;
            if (_alertDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage(result.getErrtext() + "(" + result.getErrShotText() + ")");
                builder.setCancelable(false);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        _alertDialog = null;
                    }
                });
                builder.setPositiveButton("复位设备", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> params = getParamsMap();
                        params.put("uc", UserInfo.UserCode);
                        params.put("eid", _app._chooseDevice.getEID());
                        params.put("user", UserInfo.ID);
                        if (act.sendCmd(Constants.CODE_GET_REESET, "Com_Reset", params, ResultDetail.class)) {
                            btn_error.setImageResource(R.drawable.state_ico7_2);
                        }
                        dialog.dismiss();
                        _alertDialog = null;
                    }
                });
                _alertDialog = builder.create();
                _alertDialog.show();
            }
        }

    }

//    private class AsyncErrorTask extends AsyncTask<Void, Void, ErrorResult> {
//        ImageView _source;
//
//        public AsyncErrorTask(ImageView source) {
//            this._source = source;
//        }
//
//        @Override
//        protected ErrorResult doInBackground(Void... params) {
//            try {
//                String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=Com_ErrorDescription&uc="
//                        + UserInfo.UserCode + "&errCode=" + Data.Error;
//                String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
//                if (!TextUtils.isEmpty(jstr)) {
//                    return new Gson().fromJson(jstr, ErrorResult.class);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ErrorResult result) {
//            super.onPostExecute(result);
//            if (result != null) {
////                LogUtils.e("Again");
//                if (getActivity() == null) {
//                    return;
//                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("提示");
//                builder.setMessage(result.errtext + "(" + result.errShotText + ")");
//                builder.setCancelable(false);
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        _OpenErrWin = false;
//                        dialog.dismiss();
//                    }
//                });
//                builder.setPositiveButton("复位设备", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        act.CheckIsRunningDataTask(true);
//                        String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=Com_Reset&uc="
//                                + UserInfo.UserCode + "&eid=" + Data.EID + "&user=" + UserInfo.ID;
//                        AsyncStateTask asy = new AsyncStateTask(_source, 7, strUrl);
//                        act.oneTask = asy;
//                        asy.execute();
//                        _OpenErrWin = false;
//                        dialog.dismiss();
//                    }
//                });
//                _OpenErrWin = true;
//
//                builder.create().show();
//            }
//        }
//    }
//
//    private class AsyncStateTask extends AsyncTask<Void, Void, PostResult> {
//
//        ImageView _source;
//        int _stateIdx;
//        //int _mt=0,_msg=0,_esp=0;
//        String _commandUrl;
//
//        Resources res = getResources();
//
//        /*
//         * source源按钮
//         * stateIdx表示按钮索引
//         * mt表示minor_type,请求参数
//         * msg表示message,请求参数
//         */
//        public AsyncStateTask(ImageView source, int stateIdx, String commandUrl) {
//            //public AsyncStateTask(ImageView source,int stateIdx,int mt,int msg,int esp) {
//            super();
//            _source = source;
//            _stateIdx = stateIdx;
////	        _mt=mt;
////	        _msg=msg;
////	        _esp=esp;
//            _commandUrl = commandUrl;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            act.ShowLoadingDetail();
//            if (Data != null) {
//                int i = res.getIdentifier("state_ico" + _stateIdx + "_2", "drawable", getActivity().getPackageName());
//                _source.setImageResource(i);
//            }
//        }
//
//        @Override
//        protected PostResult doInBackground(Void... params) {
//            PostResult result = new PostResult();
//            try {
//                if (Data != null) {
//
////					String strUrl = "http://121.40.147.163/AppHandler.ashx?Method=SettingCommand&uc="
////							+UserInfo.UserCode+"&eid="+Data.EID+"&minor_type="+_mt+"&message="+_msg+"&Especially="+_esp;
//                    String strUrl = _commandUrl;
//                    String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
//                    if (!TextUtils.isEmpty(jstr)) {
//                        JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
//                        String _r = jsObj.getString("Result");
//                        String _info = jsObj.getString("Info");
//                        if (_r.equals("1")) {
//                            int postcount = 0;
//                            while (true) {
//                                strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=GetSettingResult&uc="
//                                        + UserInfo.UserCode + "&eid=" + Data.EID + "&setID=" + _info;
//                                jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
//                                if (!jstr.equals("")) {
//                                    jsObj = GetRemoteData.ParseJson(jstr, getActivity());
//                                    int _r2 = Integer.parseInt(jsObj.getString("Result"));
//                                    if (_r2 == 2) {
//                                        result.Result = "1";
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
//                // e.printStackTrace();
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
//            act.oneTask = null;
//            act.GetDetailDataHandler(false);
//        }
//    }

}

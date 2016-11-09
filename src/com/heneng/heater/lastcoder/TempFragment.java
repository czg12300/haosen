package com.heneng.heater.lastcoder;

import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.fragments.BaseNetFragment;
import com.heneng.heater.lastcoder.baseClass.BaseFragment;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.MonitorInfo;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.utils.Constants;

import java.util.Map;


public class TempFragment extends BaseNetFragment {

    View rootView;
    ImageView bgImg;
    ImageView cursor;
    TextView tempTxt;
    DetailActivity act;

    int lastY;//手开始滑动时的位置
    int moveMax; //移动的最低位置
    int moveMin; //移动的最顶位置    
    int tempMax = 60; //最高温度
    int tempMin = 30; //最低温度
    float moveItem = 0;

    boolean dragStart = false;
    ScrollView conflictSV = null; //避免事件冲突的ScrollView
    int TempVal;//温度值
    int TempType;//温度类型(1为卫浴，2为采暖)
    ResultGetNowData Data = null;
//    int MinorType;//请求命令类型

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frament_temp, container, false);
        act = (DetailActivity) getActivity();
        return rootView;
    }

    /*
     * 根据不同屏幕初始化界面控件
     */
    private void InitPageView() {
        //游标移动的范围为10dp到225dp
        moveMax = CommonFunction.dip2px(getActivity(), 225);
        moveMin = CommonFunction.dip2px(getActivity(), 10);
        moveItem = (float) (moveMax - moveMin) / (float) (tempMax - tempMin);

        if (MonitorInfo.Width == 360) return;

        RelativeLayout.LayoutParams parasImg = (RelativeLayout.LayoutParams) bgImg.getLayoutParams();
        RelativeLayout.LayoutParams parasCursor = (RelativeLayout.LayoutParams) cursor.getLayoutParams();

        if (MonitorInfo.Width <= 320) {
//			parasImg.width=(int)Math.round(parasImg.width*MonitorInfo.WidthRate );
//			parasImg.height=(int)Math.round(parasImg.height*MonitorInfo.WidthRate );			
//			bgImg.setLayoutParams(parasImg);

            parasCursor.width = (int) Math.round(parasCursor.width * MonitorInfo.WidthRate);
            parasCursor.height = (int) Math.round(parasCursor.height * MonitorInfo.WidthRate);
            //cursor.setLayoutParams(parasCursor);

            //moveMax=(int)Math.round(moveMax*MonitorInfo.WidthRate );
            //moveMin=(int)Math.round(moveMin*MonitorInfo.WidthRate );
            moveMax = CommonFunction.dip2px(getActivity(), 188);
            moveMin = CommonFunction.dip2px(getActivity(), 5);
            moveItem = (float) (moveMax - moveMin) / (float) (tempMax - tempMin);
        } else if (MonitorInfo.Width >= 480) {

            parasImg.width = CommonFunction.dip2px(getActivity(), 220);
            parasImg.height = CommonFunction.dip2px(getActivity(), 380);
            //bgImg.setLayoutParams(parasImg);

            parasCursor.setMargins(CommonFunction.dip2px(getActivity(), 15), 0, 0, 0);

            moveMax = CommonFunction.dip2px(getActivity(), 295);
            moveMin = CommonFunction.dip2px(getActivity(), 17);
            moveItem = (float) (moveMax - moveMin) / (float) (tempMax - tempMin);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tempTxt = (TextView) getView().findViewById(R.id.fragment_tempset_txt);
        bgImg = (ImageView) rootView.findViewById(R.id.fragment_tempset_img);
        cursor = (ImageView) rootView.findViewById(R.id.fragment_tempset_cursor);
        cursor.setOnTouchListener(new CursorOnTouchListener());

        InitPageView();
    }

    public void BindData(ResultGetNowData data, int tType) {
        if (data == null) {
            return;
        }

        TempType = tType;
        Data = data;
        int wendu = 0;
        if (TempType == 1) {
            //卫浴温度设定
            if (Data.getWeiYuSetTemp() != null) {
                wendu = Integer.parseInt(Data.getWeiYuSetTemp());
            }
//            MinorType = 24;
            tempMax = 60;
            bgImg.setImageResource(R.drawable.wenduji1);
        } else if (TempType == 2) {
            //采暖温度设定
            if (data.getCaiNuanSetTemp() != null) {
                wendu = Integer.parseInt(data.getCaiNuanSetTemp());
            }

//            MinorType = 25;
            tempMax = 80;
            bgImg.setImageResource(R.drawable.wenduji2);
        }

        //数据范围35-60 对应 225-10
        tempTxt.setText(wendu + "℃");
        TempVal = wendu;
        if (TempVal < tempMin)
            TempVal = tempMin;
        else if (TempVal > tempMax)
            TempVal = tempMax;

        InitPageView();

        RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
        int target = moveMax - Math.round((TempVal - tempMin) * moveItem);
        paras.setMargins(paras.leftMargin, target, 0, 0);
        cursor.setLayoutParams(paras);
        //Log.d("BindData", "BindData:"+target+"");
    }

    /*
     * 设置避免事件冲突的ScrollView
     */
    public void SetConflictScrollView(ScrollView sv) {
        conflictSV = sv;
    }

    /*
     * 根据位置返回温度值
     */
    private int GetTempByPosition(int post) {
        if (post < moveMin) post = moveMin;
        if (post > moveMax) post = moveMax;
        int temp = tempMax - Math.round((post - moveMin) / moveItem);

        if (temp < tempMin)
            temp = tempMin;
        else if (temp > tempMax)
            temp = tempMax;
        return temp;
    }

    @Override
    public void callback(int code, BaseModel model) {

    }

    /*
     * 温度游标移动事件
     */
    class CursorOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //if(!dragStart)return true;
            int action = event.getAction();
            //Log.d("tempfragment", "ACTION:"+action);

            if (conflictSV != null) {
                if (action == MotionEvent.ACTION_UP) {
                    conflictSV.requestDisallowInterceptTouchEvent(false);
                } else {
                    conflictSV.requestDisallowInterceptTouchEvent(true);
                }
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //Log.d("tempfragment", "ACTION_DOWN");
                    lastY = (int) event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    //Log.d("tempfragment", "ACTION_MOVE");
                    int dy = (int) event.getRawY() - lastY;
                    RelativeLayout.LayoutParams paras = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
                    int target = paras.topMargin + dy;

                    if (target < moveMin) {
                        target = moveMin;
                    } else if (target > moveMax) {
                        target = moveMax;
                    }
                    paras.setMargins(paras.leftMargin, target, 0, 0);
                    cursor.setLayoutParams(paras);
                    TempVal = GetTempByPosition(target);
                    tempTxt.setText(TempVal + "℃");
                    SetTempNum(TempVal + "");
                    lastY = (int) event.getRawY();
                    break;

                case MotionEvent.ACTION_CANCEL:
                    //Log.d("tempfragment", "ACTION_CANCEL");
                case MotionEvent.ACTION_UP:
                    //Log.d("tempfragment", "ACTION_UP");
                    if (tempNumDial != null) {
                        tempNumDial.dismiss();
                        tempNumDial = null;
                    }
                    //结束移动发送设定命令

//                    AsyncSetTempTask asySetTemp = new AsyncSetTempTask(TempVal);
////                    act.oneTask = asySetTemp;
//                    asySetTemp.execute();

                    Map<String, Object> paramsMap = getParamsMap();
//                    String strUrl ="http://"+  ((AppAplication)getActivity().getApplication())._chooseDevice.getInterface()+ "/AppHandler.ashx?Method=" + method + "&uc="
//                            + UserInfo.UserCode + "&eid=" + Data.getEID() + "&user=" + UserInfo.ID + "&temp=" + this._temp;
                    paramsMap.put("uc", UserInfo.UserCode);
                    paramsMap.put("eid", _app._chooseDevice.getEID());
                    paramsMap.put("user", UserInfo.ID);
                    paramsMap.put("temp", TempVal);
                    String method = (TempType == 1 ? "Com_SetBathTemp" : "Com_SetHeatingTemp");
                    act.sendCmd(Constants.CODE_SET_TEMPERATURE, method, paramsMap, ResultDetail.class);

                    break;

            }
            return true;
        }

    }

    public void pushCallback(ResultGetNowData data, int tType) {

        BindData(data, tType);

    }
//    class AsyncSetTempTask extends AsyncTask<Void, Void, PostResult> {
//        private int _temp;
//
//        public AsyncSetTempTask(int temp) {
//            super();
//            this._temp = temp;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            act.sendCmd(Constants.CODE_SET_MODE_XIAJI, "Com_SetMode", params, ResultDetail.class);
//        }
//
//        @Override
//        protected PostResult doInBackground(Void... params) {
//            PostResult result = new PostResult();
//            try {
//                if (Data != null) {
////					String strUrl = ServerInfo.WebApi+"/AppHandler.ashx?Method=SettingCommand&uc="
////							+UserInfo.UserCode+"&eid="+Data.EID+"&minor_type="+MinorType+"&message="+this._temp;
//
//                    String strUrl ="http://"+  ((AppAplication)getActivity().getApplication())._chooseDevice.getInterface()+ "/AppHandler.ashx?Method=" + method + "&uc="
//                            + UserInfo.UserCode + "&eid=" + Data.getEID() + "&user=" + UserInfo.ID + "&temp=" + this._temp;
//                    String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
//                    if (!jstr.equals("")) {
//                        JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
//                        String _r = jsObj.getString("Result");
//                        String _info = jsObj.getString("Info");
//                        if (_r.equals("1")) {
//                            //获取执行结果
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
//            act.refressDataNow();
//        }
//    }

    TempNumDialogFragment tempNumDial;

    public void SetTempNum(String temp) {
        if (tempNumDial == null) {
            tempNumDial = new TempNumDialogFragment();
            tempNumDial.show(getFragmentManager(), "tempnum");
        }
        tempNumDial.SetTemp(temp);
    }
}

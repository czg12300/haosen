package com.heneng.heater.lastcoder;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.ResultGetNowData;

import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class TimingSetDialogFragment extends DialogFragment {

    RelativeLayout box;
    ResultGetNowData Data = null;
    DetailActivity act;

    public TimingSetDialogFragment(ResultGetNowData data) {
        Data = data;
    }

    @Override
    public void onStart() {
        super.onStart();
        //控制dialog背景透明度
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.20f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //控制fragment背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.frament_timingset_dialog, container);
        act = (DetailActivity) getActivity();

        box = (RelativeLayout) view.findViewById(R.id.fragment_timingsetDialog_box);
        //{{生成表格项     
        int itemWidth = CommonFunction.dip2px(getActivity(), 40);
        int marginLeft = 0, marginTop = 0;
        Resources res = getResources();
        for (int i = 0; i < 24; i++) {
            android.widget.RelativeLayout.LayoutParams paras =
                    new android.widget.RelativeLayout.LayoutParams(itemWidth, itemWidth);
            paras.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paras.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            paras.setMargins(marginLeft, marginTop, 0, 0);

            TextView txt = new TextView(getActivity());
            txt.setText(i + "");
//            txt.setTextColor(color.timingset_item_font1);
            txt.setLayoutParams(paras);
            txt.setGravity(Gravity.CENTER);
            txt.setOnClickListener(itemClickListener);
            box.addView(txt);

            String val = Data.getHeatingTimer().substring(i, i + 1);
            txt.setTag(val);

            String resIdx = (!val.equals("1")) ? "1" : "2";
            int resid = 0;
            if ((i + 1) % 6 == 0) {
                marginLeft = 0;
                marginTop += itemWidth;
                if (i != 23) {
                    resid = res.getIdentifier("timingset_item2_" + resIdx, "drawable", getActivity().getPackageName());
                } else {
                    resid = res.getIdentifier("timingset_item4_" + resIdx, "drawable", getActivity().getPackageName());
                }
            } else {
                if (i < 18) {
                    resid = res.getIdentifier("timingset_item1_" + resIdx, "drawable", getActivity().getPackageName());
                } else {
                    resid = res.getIdentifier("timingset_item3_" + resIdx, "drawable", getActivity().getPackageName());
                }
                marginLeft += itemWidth;
            }
            txt.setBackgroundResource(resid);

/*			if((i+1)%6==0){
                marginLeft=0;
				marginTop+=itemWidth;
				if(i!=23){
					txt.setBackgroundResource(R.drawable.timingset_item2_1);
				}
				else{
					txt.setBackgroundResource(R.drawable.timingset_item4_1);			
				}
			}
			else{		
				if(i<18){
					txt.setBackgroundResource(R.drawable.timingset_item1_1);		
				}
				else {
					txt.setBackgroundResource(R.drawable.timingset_item3_1);		
				}
				marginLeft+=itemWidth;
			}*/
        }

        Button btnCancel = (Button) view.findViewById(R.id.timingsetDialog_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();  //关闭对话框
            }
        });

        Button btnSubmit = (Button) view.findViewById(R.id.timingsetDialog_submit);
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                AsyncSetTask asy = new AsyncSetTask();
//
//                asy.execute();

            }
        });
        return view;
    }
    public void pushCallback(ResultGetNowData data) {

        BindData(data);

    }
    //数据绑定
    public void BindData(ResultGetNowData data) {
        Data = data;
    }

    private void BindControl() {
        if (box != null && Data.getHeatingTimer().length() == 24) {
            int c = box.getChildCount();
            Resources res = getResources();
            for (int i = 1; i < c; i++) {
                TextView txt = (TextView) box.getChildAt(i);
                String val = Data.getHeatingTimer().substring(i, i + 1);
                txt.setTag(val);

                String resIdx = (!val.equals("1")) ? "1" : "2";
                int resid = 0;
                if ((i + 1) % 6 == 0) {
                    if (i != 23) {
                        resid = res.getIdentifier("timingset_item2_" + resIdx, "drawable", getActivity().getPackageName());
                    } else {
                        resid = res.getIdentifier("timingset_item4_" + resIdx, "drawable", getActivity().getPackageName());
                    }
                } else {
                    if (i < 18) {
                        resid = res.getIdentifier("timingset_item1_" + resIdx, "drawable", getActivity().getPackageName());
                    } else {
                        resid = res.getIdentifier("timingset_item3_" + resIdx, "drawable", getActivity().getPackageName());
                    }
                }
                txt.setBackgroundResource(resid);
            }
        }
    }

    OnClickListener itemClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TextView txt = (TextView) v;
            int i = Integer.parseInt(txt.getText().toString());
            String tag = (String) txt.getTag();
            //Toast.makeText(getActivity(), tag, Toast.LENGTH_SHORT).show();
            Resources res = getResources();
            String resIdx = (!tag.equals("1")) ? "2" : "1";
            int resid = 0;
            if ((i + 1) % 6 == 0) {
                if (i != 23) {
                    resid = res.getIdentifier("timingset_item2_" + resIdx, "drawable", getActivity().getPackageName());
                } else {
                    resid = res.getIdentifier("timingset_item4_" + resIdx, "drawable", getActivity().getPackageName());
                }
            } else {
                if (i < 18) {
                    resid = res.getIdentifier("timingset_item1_" + resIdx, "drawable", getActivity().getPackageName());
                } else {
                    resid = res.getIdentifier("timingset_item3_" + resIdx, "drawable", getActivity().getPackageName());
                }
            }
            txt.setBackgroundResource(resid);
            txt.setTag((!tag.equals("1")) ? "1" : "0");
        }
    };


//    class AsyncSetTask extends AsyncTask<Void, Void, PostResult> {
//
//        String setVal;
//
//        public AsyncSetTask() {
//            super();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            act.sendCmd(Constants.CODE_SET_MODE_XIAJI, "Com_SetMode", params, ResultDetail.class);
//
//            //获取设定的值
//            int c = box.getChildCount();
//            setVal = "";
//            for (int i = 1; i < c; i++) {
//                Object val = ((TextView) box.getChildAt(i)).getTag();
//                setVal += val == null ? "0" : val.toString();
//            }
//            Log.d("AsyncSetTask", "AsyncSetTask:" + setVal);
//        }
//
//        @Override
//        protected PostResult doInBackground(Void... params) {
//            PostResult result = new PostResult();
//            try {
//                if (Data != null) {
//                    //按命令格式排序
////					StringBuffer set1 = new StringBuffer(setVal.substring(0, 8));
////					set1 = set1.reverse();
////					StringBuffer set2 = new StringBuffer(setVal.substring(8, 16));
////					set2 = set2.reverse();
////					StringBuffer set3 = new StringBuffer(setVal.substring(16, 24));
////					set3 = set3.reverse();
//
//                    //采暖定时使能设定
////					String strUrl = ServerInfo.WebApi+"/AppHandler.ashx?Method=SettingCommand&uc="
////							+UserInfo.UserCode+"&eid="+Data.EID+"&minor_type=27&Especially=1&message="+set1+set2+set3;
//                    String strUrl = "http://"+ ((AppAplication)getActivity().getApplication())._chooseDevice.getInterface() + "/AppHandler.ashx?Method=Com_SetHeatingTime&uc="
//                            + UserInfo.UserCode + "&eid=" + Data.getEID() + "&user=" + UserInfo.ID + "&value=" + setVal;
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
//
//            act.refressDataNow();
//            TimingSetDialogFragment.this.dismiss();
//        }
//    }

}

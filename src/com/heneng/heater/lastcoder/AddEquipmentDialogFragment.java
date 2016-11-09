package com.heneng.heater.lastcoder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.heneng.heater.R;

import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;

import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class AddEquipmentDialogFragment extends DialogFragment {

    String Barcode;
    String Ename;
    String UserID;
    String UserCode;
    BarcodeActivity act;
    EditText etBarcode = null;
    EditText etEname = null;

    public AddEquipmentDialogFragment(String barcode, String ename, String userID, String userCode) {
        Barcode = barcode;
        Ename = ename;
        UserID = userID;
        UserCode = userCode;
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

        View view = inflater.inflate(R.layout.frament_addequipment_dialog, container);
        act = (BarcodeActivity) getActivity();

        etBarcode = (EditText) view.findViewById(R.id.addEquipmentDialog_barcode);
        etBarcode.setText(Barcode);
        etEname = (EditText) view.findViewById(R.id.addEquipmentDialog_ename);
        etEname.setText(Ename);

        //{{事件绑定
        Button btnCancel = (Button) view.findViewById(R.id.addEquipmentDialog_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                act.ContinuePreview();
                dismiss();  //关闭对话框
            }
        });

        Button btnSubmit = (Button) view.findViewById(R.id.addEquipmentDialog_submit);
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    Ename = etEname.getText().toString().trim();
                    if (Ename.equals("")) {
                        Toast.makeText(getActivity(), "请填写设备名称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AsyncSetTask asy = new AsyncSetTask();
                    asy.execute();
                }
            }
        });
        //}}

        return view;
    }

//	@Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		BindControl();
//    }

    // {{执行设定操作
    class AsyncSetTask extends AsyncTask<Void, Void, PostResult> {

        public AsyncSetTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            //异步线程执行前的UI操作
            act.ShowLoadingBarcode();
        }

        @Override
        protected PostResult doInBackground(Void... params) {
            // 开始执行异步线程
            PostResult result = new PostResult();
            try {

                //将条形码和用户绑定
                String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=SetUserEquiment&uc="
                        + UserCode + "&user=" + UserID + "&eid=" + Barcode + "&ename=" + Ename;
                //Log.d("barcordadd", "barcordadd:"+strUrl);
                String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
                if (!jstr.equals("")) {
                    JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
                    String _r = jsObj.getString("Result");
                    String _info = jsObj.getString("Info");
                    if (_r.equals("1")) {
                        result.Result = "1";

                        //检测是否在线，不在线则需要smartconfig操作
                        String strUrl2 = AppParams.WebApi + "/AppHandler.ashx?Method=GetNowData&uc="
                                + UserCode + "&eid=" + Barcode;
                        //Log.d("barcordadd", "barcordadd:"+strUrl2);
                        String jstr2 = GetRemoteData.ConnServerForResult(strUrl2, getActivity());
                        if (!jstr2.equals("")) {
                            JSONObject jsObj2 = GetRemoteData.ParseJson(jstr2, getActivity());
                            String _online = jsObj2.getString("OnLine");
                            if (!_online.equals("1")) {
                                //设备不在线
                                result.Result = "2";
                            }
                        } else {
                            //没有正常返回数据
                            result.Result = "2";
                        }
                    } else {
                        result.Result = "0";
                        result.Info = _info;
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                result.Result = "0";
                result.Info = "数据获取异常:" + e.getMessage();

            } finally {
                return result;
            }
        }

        // 完成异步操作
        @Override
        protected void onPostExecute(PostResult result) {
            // 异步线程执行后的UI操作
            super.onPostExecute(result);
            if (result.Result.equals("0")) {
                Toast.makeText(getActivity(), result.Info, Toast.LENGTH_SHORT).show();
            } else if (result.Result.equals("2")) {
                //设备不在线，需要smartconfig
//                Intent intent = new Intent(getActivity(), SmartConfigActivity.class);
//                Bundle bundle = new Bundle();//携带数据
//                bundle.putString("eid", Barcode);
//                intent.putExtras(bundle);//附带上额外的数据
//                startActivity(intent);
            }

            act.CloseLoadingBarcode();
            AddEquipmentDialogFragment.this.dismiss();
            act.finish();
        }
    }
    // }}

}

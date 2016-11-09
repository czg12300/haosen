package com.heneng.heater.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.heneng.heater.lastcoder.AppAplication;
import com.heneng.heater.models.PushMsg;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

import java.util.Date;

public class YHPushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
//    public static StringBuilder payloadData = new StringBuilder();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        LogUtils.e("onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                LogUtils.e("第三方回执接口调用" + (result ? "成功" : "失败"));
//                LogUtils.e("推送回调时间：" + new Date().toLocaleString());

                if (payload != null) {
                    String data = new String(payload);

                    LogUtils.e("推送回调数据 : " + data);
                    try {
                        Gson gson = new Gson();
                        JSONObject obj = new JSONObject(data);
                        String type = obj.getString("type");

                        String strMsg = obj.getString("message");

//                        LogUtils.e(type+"===="+strMsg);


                        Intent pIntent = new Intent();

                        if ("msg".equals(type)) {
                            //信息
                            PushMsg msg = gson.fromJson(strMsg, PushMsg.class);
                            if (PushMsg.MessageType.NEWS.equals(msg.getMtype())) {
                                //新闻类
                                pIntent.setAction(Constants.BROCAST_PUSH_MSG);


                            } else if (PushMsg.MessageType.SYSTEM.equals(msg.getMtype())) {
                                //系统提示类
                                pIntent.setAction(Constants.BROCAST_PUSH_SYSTEM);

                            } else if (PushMsg.MessageType.ALARM.equals(msg.getMtype())) {
                                //警报类
                                pIntent.setAction(Constants.BROCAST_PUSH_ALARM);

                            }
                            pIntent.putExtra(Constants.KEY_EXTRA_PUSH_MSG, msg);
                        } else {

                            //设备状态
//                            ResultGetNowData state = gson.fromJson(strMsg, ResultGetNowData.class);
                            pIntent.setAction(Constants.BROCAST_PUSH_STATE);
                            pIntent.putExtra(Constants.KEY_EXTRA_PUSH_STATE, strMsg);

                        }
                        context.sendBroadcast(pIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("非法Json的推送数据：" + data);
                    }


//                    payloadData.append(data);
//                    payloadData.append("\n");

//                    if (GetuiSdkDemoActivity.tLogView != null) {
//                        GetuiSdkDemoActivity.tLogView.append(data + "\n");
//                    }
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
//                String cid = bundle.getString("clientid");
//                LogUtils.e("cid:" + cid);
//                if (GetuiSdkDemoActivity.tView != null) {
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                }
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 * 
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }
}

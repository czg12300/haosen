package com.heneng.heater.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.LoginActivity;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.services.DownloadService;
import com.heneng.heater.views.DownloadDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.logging.LogRecord;

/**
 * Created by Yihao Huang on 2015/9/18.
 */
public class NetUtils {


    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    @SuppressLint("DefaultLocale")
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!extraInfo.trim().equals("")) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = Constants.NETTYPE_CMNET;
                } else {
                    netType = Constants.NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = Constants.NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Context context) {
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    public static void startWebsite(Context context, String websiteUrl) {
        Uri uri = Uri.parse(websiteUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


    public static void startDownLoad(Context context, String url, String version, boolean isForce) {

        Dialog downloadDialog = new DownloadDialog(context, url, version, isForce);
        downloadDialog.setCancelable(false);
        downloadDialog.show();


    }


    private static final int WHAT_NET_RESULT = 101;
    static Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            for (NetCallbackListener listener : mListeners) {
                if (listener == null) return;
                listener.callback(msg.what, (BaseModel) msg.obj);
            }
        }
    };


    public interface NetCallbackListener {
        void callback(int code, BaseModel model);
    }

    private static List<NetCallbackListener> mListeners = new ArrayList<NetCallbackListener>();

    public static boolean registNetCallbackListener(NetCallbackListener listener) {
        if (!mListeners.contains(listener)) {
            return mListeners.add(listener);
        }
        return false;
    }

    public static boolean unregistCallbackListener(NetCallbackListener listener) {
        return mListeners.remove(listener);
    }


    private static ExecutorService executorServer = Executors.newCachedThreadPool();
    private static AlertDialog _dialog;


    public static boolean isNetWork(final Context context) {
        if (getNetworkType(context) == 0) {
            if (_dialog == null) {
                _dialog = new AlertDialog.Builder(context).setTitle("提示").setIcon(R.drawable.ic_launcher).setMessage("网络不通，请先设置网络").setPositiveButton("设置网络", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _dialog.dismiss();
                        openSetting(context);
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _dialog.dismiss();
                        _dialog = null;
                        System.exit(0);
                    }
                }).create();
                _dialog.setCancelable(false);
                _dialog.show();
            }
            return false;
        }
        return true;
    }


    public static void executGet(final Context context, final int code, String serverSite, String method, Map<String, Object> params, final Class<? extends BaseModel> clazz) {
        if (!isNetWork(context)) {
            return;
        }

        String strParams = "";
        for (String key : params.keySet()) {
            strParams += "&" + key + "=" + params.get(key);
        }
        final String url;
        if (TextUtils.isEmpty(serverSite)) {
            url = Constants.WebApi + "/AppHandler.ashx?Method=" + method + strParams;
        } else {
            url = "http://" + serverSite + "/AppHandler.ashx?Method=" + method + strParams;
        }
        LogUtils.e("Get请求URL:" + url);

        executorServer.execute(new Runnable() {
            @Override
            public void run() {
                String result = get(url);


                handleResult(context, result, code, clazz);


            }
        });
    }

    public static void executGet(Context context, final int code, String method, Map<String, Object> params, final Class<? extends BaseModel> clazz) {
        executGet(context, code, null, method, params, clazz);
    }

    public static void executPost(final Context context, final int code, String serverSite, String method, final Map<String, Object> params, final Class<? extends BaseModel> clazz) {
        if (!isNetWork(context)) {
            return;
        }
        final String url;
        if (TextUtils.isEmpty(serverSite)) {
            url = Constants.WebApi + "/AppHandler.ashx?Method=" + method;
        } else {
            url = "http://" + serverSite + "/AppHandler.ashx?Method=" + method;
        }
        LogUtils.e("Post请求URL:" + url);
        executorServer.execute(new Runnable() {
            @Override
            public void run() {

                String result = post(url, params);
                handleResult(context, result, code, clazz);
            }
        });
    }

    public static void executPost(Context context, final int code, String method, final Map<String, Object> params, final Class<? extends BaseModel> clazz) {
        executPost(context, code, null, method, params, clazz);
    }

    private static void handleResult(final Context context, String result, int code, Class<? extends BaseModel> clazz) {
        try {
            Gson gson = new Gson();
            LogUtils.e(code + "---返回数据:" + result);
            if (result.contains("用户校验码过期")) {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(context).setIcon(R.drawable.ic_launcher).setTitle("提示").setMessage("用户登录过期，请重新登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                if (context instanceof Activity) {
                                    ((Activity) context).finish();
                                }
                            }
                        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0);
                            }
                        });
                    }
                });
            } else {
                BaseModel model = gson.fromJson(result, clazz);
                Message msg = Message.obtain();
                msg.what = code;
                msg.obj = model;
                _handler.sendMessage(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = code;
            _handler.sendMessage(msg);
        }
    }

    private static String post(String url, Map<String, Object> params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;


        HttpPost post = postForm(url, params);

        body = invoke(httpclient, post);

        httpclient.getConnectionManager().shutdown();

        return body;
    }

    private static String get(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;

        HttpGet get = new HttpGet(url);
        body = invoke(httpclient, get);

        httpclient.getConnectionManager().shutdown();

        return body;
    }


    private static String invoke(DefaultHttpClient httpclient,
                                 HttpUriRequest httpost) {

        HttpResponse response = sendRequest(httpclient, httpost);
        String body = paseResponse(response);

        return body;
    }

    private static String paseResponse(HttpResponse response) {
        if (response == null) return "";
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static HttpResponse sendRequest(DefaultHttpClient httpclient,
                                            HttpUriRequest httpost) {
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, Object> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key) + ""));
        }

        try {


            httpost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }


}

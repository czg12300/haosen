package com.heneng.heater.lastcoder.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class GetRemoteData {


    public static String ConnServerForResult(String strUrl, Context context) {
        String strResult = "";
        // HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();

        try {
            // HttpGet对象
            HttpGet httpRequest = new HttpGet(strUrl);

            // 获得HttpResponse对象
            HttpResponse httpResponse = httpClient.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的数据
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }

        } catch (ClientProtocolException e) {
            Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } catch (IOException e) {
            Toast.makeText(context, "IO错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
        return strResult;
    }

    // 普通Json数据解析
    public static JSONObject ParseJson(String strResult, Context context) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(strResult);

        } catch (JSONException e) {
            Toast.makeText(context, "数据格式错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return jsonObj;
    }

}

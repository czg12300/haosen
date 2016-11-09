package com.heneng.heater.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.heneng.heater.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/1/18.
 */
public class DownloadService extends Service {

    private long _progress;
    private long _max = Long.MAX_VALUE;
    private String _fileName;
    public String _dir = Environment.getExternalStorageDirectory() + "/HaoSen/";
    private boolean _stop;

    public static final String ACTION_BROCAST_DOWNLOAD = "download";
    public static final String ACTION_BROCAST_STOP = "downloadstop";
    public static final String ACTION_BROCAST_DOWNLOAD_PROGRESS = "downloadprogress";
    public static final String EXTRA_DOWNLOAD_MAX = "downloadmax";
    public static final String EXTRA_DOWNLOAD_PROGRESS = "downloadprogressint";
    public static final String EXTRA_DOWNLOAD_PERCENT = "downloadprecent";
    public static final String EXTRA_DOWNLOAD_URL = "downloadprecent";
    public static final String EXTRA_DOWNLOAD_VERSION = "downloadversion";

    Handler _handler = new Handler();
    Runnable refressViewRunable = new Runnable() {
        @Override
        public void run() {
            int percentProgress = (int) (_progress * 100 / _max);

            Intent intent = new Intent(ACTION_BROCAST_DOWNLOAD_PROGRESS);
            intent.putExtra(EXTRA_DOWNLOAD_MAX, _max);
            intent.putExtra(EXTRA_DOWNLOAD_PROGRESS, _progress);
            intent.putExtra(EXTRA_DOWNLOAD_PERCENT, percentProgress);
            sendBroadcast(intent);


            if (_progress >= _max) {
                LogUtils.e("安装2");
                installAPK();

            } else {
                _handler.postDelayed(refressViewRunable, 100);
            }
        }
    };


    private void installAPK() {
        String filePath = _dir + _fileName;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(ACTION_BROCAST_DOWNLOAD);
        filter.addAction(ACTION_BROCAST_STOP);
        registerReceiver(_receiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(_receiver);
    }

    BroadcastReceiver _receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("收到广播" + intent.getAction());
            if (ACTION_BROCAST_DOWNLOAD.equals(intent.getAction())) {
                String url = intent.getStringExtra(EXTRA_DOWNLOAD_URL);
                String version = intent.getStringExtra(EXTRA_DOWNLOAD_VERSION);
                if (url == null || "".equals(url)) {
                    throw new IllegalArgumentException("url都不传，下载个毛毛");
                }
                download(url, version);
            } else if (ACTION_BROCAST_STOP.equals((intent.getAction()))) {
                _stop = true;
            }
        }
    };


    public void download(final String urlStr, final String version) {
        _stop = false;
        _progress = 0;
        _handler.removeCallbacks(refressViewRunable);
        _handler.post(refressViewRunable);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //另起线程执行下载，安卓最新sdk规范，网络操作不能再主线程。
                _fileName = "haosen" + version + ".apk";

                URL url = null;
                HttpURLConnection urlcon = null;
                try {
                    url = new URL(urlStr);

                    urlcon = (HttpURLConnection) url.openConnection();
                    _max = urlcon.getContentLength();
//                    urlcon.setRequestProperty("Range", "bytes=" + startPos + "-" + urlcon.getContentLength());


                    File file = new File(_dir);
                    if (!file.exists()) {
                        file.mkdirs();
                        //创建文件夹
                        Log.d("log", _dir);
                    }
                    //获取文件全名

                    file = new File(_dir + _fileName);


                    FileOutputStream fos = null;
                    try {
                        InputStream is = urlcon.getInputStream();
                        //创建文件
                        if (file.exists()) {
                            file.delete();
                        }
                        fos = new FileOutputStream(file, true);

                        byte[] buf = new byte[1024];

                        int len = -1;

                        while ((len = is.read(buf)) != -1 && !_stop) {
                            fos.write(buf, 0, len);
                            //同步更新数据
                            _progress += len;
                        }

                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }


}

package com.heneng.heater.activitys;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.esptouch.EsptouchTask;
import com.heneng.heater.esptouch.IEsptouchListener;
import com.heneng.heater.esptouch.IEsptouchResult;
import com.heneng.heater.esptouch.IEsptouchTask;
import com.heneng.heater.esptouch.demo_activity.EspWifiAdminSimple;
import com.heneng.heater.esptouch.task.EsptouchTaskParameter;
import com.heneng.heater.lastcoder.DetailActivity;
import com.heneng.heater.lastcoder.MainActivity;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.ReceiverActions;
import com.heneng.heater.utils.TCPUtils;
import com.heneng.heater.utils.UDPUtils;
import com.heneng.heater.utils.WifiConnect;
import com.heneng.heater.views.HeaderView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/9.
 */
public class SmartConfigActivity extends BaseActivity {
    @InjectView(R.id.smartconfig_tv_count)
    TextView tv_count;
    private static final String TAG = "TAG";
    private EspWifiAdminSimple mWifiAdmin;

    private int count = 300;
    AsyncTask<String, Void, List<IEsptouchResult>> _configTask;

    Thread _listenThread;
    ApThread _apThread;
    String _ssid;
    String _psw;
    WifiManager.MulticastLock _multicastLock;
    private static final String SUCCESS_MSG_PART = "success";
    static final int WHAT_GET_SOCKET = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_GET_SOCKET) {
                String messgae = (String) msg.obj;
                LogUtils.e("收到8899端口信息：" + messgae);

                if (messgae != null && messgae.toLowerCase().contains(SUCCESS_MSG_PART)) {
                    showMsg("配置成功");
                    stopAllConfig();
                    sendBroadcast(new Intent(ReceiverActions.ACTION_FINISH_SMART_CONFIG));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openActivity(DetailActivity.class);
                        }
                    }, 100);

                }
            }

        }
    };
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(ReceiverActions.ACTION_FINISH_SMART_CONFIG, intent.getAction())) {
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ReceiverActions.ACTION_FINISH_SMART_CONFIG);
        registerReceiver(mReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        stopAllConfig();
    }

    private void stopAllConfig() {
        stopListenPort();
        stopCount();
        stopAp();
        stopConfig();
    }

    private void showMsg(final String msg) {

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(mBaseActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_smartconfig);
        ButterKnife.inject(this);

        header.setRightListener(new HeaderView.onClickRightListener() {
            @Override
            public void onRightClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
                builder.setMessage("是否停止").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopAllConfig();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
                builder.show();

            }
        });

        mWifiAdmin = new EspWifiAdminSimple(this);

        WifiInfo info = mWifiAdmin.getConnectionInfo();
        Intent intent = getIntent();
        _ssid = intent.getStringExtra(Constants.KEY_EXTRA_SSID);
        _psw = intent.getStringExtra(Constants.KEY_EXTRA_WIFI_PSW);

        _multicastLock = ((WifiManager) getSystemService(WIFI_SERVICE)).createMulticastLock("multicastlock");


        smartConfig(_ssid, _psw);
        startAp();
        listenPort();
        toSetCount();

    }


    /**
     * SmarConfig配置入口
     *
     * @param ssid
     * @param psw
     */
    private void smartConfig(String ssid, String psw) {
        String apBssid = mWifiAdmin.getWifiConnectedBssid();
        String isSsidHiddenStr = "NO";
        String taskResultCountStr = "1";
        _configTask = new EsptouchAsyncTask3().execute(ssid, apBssid, psw,
                isSsidHiddenStr, taskResultCountStr);

    }

    /**
     * 监听端口
     */
    private void listenPort() {
        stopListenPort();
        _listenThread = new Thread(new Runnable() {

            @Override
            public void run() {
                _multicastLock.acquire();
                UDPUtils.listenPort(8899, new UDPUtils.UDPListener() {
                            private boolean isSuccess = false;

                            @Override
                            public void onCompleted(String msg) {
                                if (msg != null && msg.toLowerCase().contains(SUCCESS_MSG_PART) && !isSuccess) {
                                    Message message = mHandler.obtainMessage();
                                    message.what = WHAT_GET_SOCKET;
                                    message.obj = msg;
                                    mHandler.sendMessage(message);
                                    isSuccess = true;
                                }
                            }
                        }
                );
                _multicastLock.release();
            }
        }

        );
        _listenThread.start();
    }

    private void stopListenPort() {
        if (_listenThread != null && _listenThread.isAlive()) {
            _listenThread.interrupt();
        }
    }


    void toSetCount() {
        if (count == 0 || tv_count == null) {
            stopConfig();
            stopAp();
            return;
        }
        tv_count.setText((count--) + "");
        _handler.postDelayed(_countRunnable, 1000);
    }


    class ApThread extends Thread {
        boolean isRun = true;

        public void close() {
            isRun = false;
        }

        @Override
        public void run() {

            String SSID = "HS_WifiStove";
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiConnect wifi = new WifiConnect(wifiManager);

            int netID = wifi.Connect(SSID, "", WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);
            while (netID == -1 && count > 0) {
                netID = wifi.Connect(SSID, "", WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (connect(SSID, wifiManager, netID)) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                byte[] msgByte = new byte[64];
                msgByte[1] = 0x00;
                byte[] ssid = _ssid.getBytes();
                for (int i = 0; i < ssid.length; i++) {
                    msgByte[i + 1] = ssid[i];
                }
                byte[] psw = _psw.getBytes();
                for (int i = 0; i < psw.length; i++) {
                    msgByte[i + 33] = psw[i];
                }

//                    listenPort();

                LogUtils.e("AP发送数据：" + new String(msgByte));
                TCPUtils.sendMsg(Constants.AP_IP, Constants.AP_PORT, msgByte);

                SystemClock.sleep(1000);

                wifiManager.disableNetwork(netID);

//                    netID = wifi.Connect(_ssid, _psw, WifiConnect.WifiCipherType.WIFICIPHER_WPA);
                WifiConfiguration wifiConfiguration = wifi.IsExsits(_ssid);

                if (wifiConfiguration != null) {

                    netID = wifiConfiguration.networkId;
                    wifiManager.enableNetwork(netID, true);

                } else {
                    LogUtils.e("没有这个网络ID:" + _ssid);
                }


            }


        }

        private boolean connect(String SSID, WifiManager wifiManager, int netID) {
            int count = 0;
            boolean bRet = false;
            boolean result = false;
            while (!bRet && isRun) {
                count++;
                LogUtils.e("连接" + SSID + "次数：" + count);
                wifiManager.startScan();
                List<ScanResult> results = wifiManager.getScanResults();
                for (ScanResult scanResult : results) {
                    if (scanResult.SSID.equals(SSID)) {

                        LogUtils.e("扫描到" + SSID + "，开始连接");
                        bRet = wifiManager.enableNetwork(netID, true);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count = 0;
            WifiInfo info = null;
            while (isRun) {
                count++;
                SystemClock.sleep(1000);
                info = wifiManager.getConnectionInfo();
                LogUtils.e("当前连接SSID:" + info.getSSID() + "-----连接WIFI次数：" + count);
                if (info != null && info.getSSID().equals("\"" + SSID + "\"")) {
                    LogUtils.e("已经连接好WIFI");
                    result = true;
                    break;
                }

            }
            return result;
        }
    }


    /**
     * 开始AP模式
     */
    private void startAp() {
        _apThread = new ApThread();
        _apThread.start();
    }

    public void stopAp() {
        if (_apThread != null && _apThread.isAlive()) {
            _apThread.close();
            _apThread.interrupt();

        }
    }


    Runnable _countRunnable = new Runnable() {
        @Override
        public void run() {
            toSetCount();
        }
    };


    void stopConfig() {

        if (_configTask != null && !_configTask.isCancelled()) {
            _configTask.cancel(true);
        }
    }

    private void stopCount() {
        _handler.removeCallbacks(_countRunnable);
        count = 0;
    }

//    private class EsptouchAsyncTask2 extends AsyncTask<String, Void, IEsptouchResult> {
//
//
//        private IEsptouchTask mEsptouchTask;
//        // without the lock, if the user tap confirm and cancel quickly enough,
//        // the bug will arise. the reason is follows:
//        // 0. task is starting created, but not finished
//        // 1. the task is cancel for the task hasn't been created, it do nothing
//        // 2. task is created
//        // 3. Oops, the task should be cancelled, but it is running
//        private final Object mLock = new Object();
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected IEsptouchResult doInBackground(String... params) {
//            synchronized (mLock) {
//                String apSsid = params[0];
//                String apBssid = params[1];
//                String apPassword = params[2];
//                String isSsidHiddenStr = params[3];
//                boolean isSsidHidden = false;
//                if (isSsidHiddenStr.equals("YES")) {
//                    isSsidHidden = true;
//                }
//                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
//                        isSsidHidden, mBaseActivity);
//            }
//            IEsptouchResult result = mEsptouchTask.executeForResult();
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(IEsptouchResult result) {
//
//        }
//    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is success to connected to the wifi ";
                LogUtils.e(text);
                Message message = mHandler.obtainMessage();
                message.what = WHAT_GET_SOCKET;
                message.obj = text;
                mHandler.sendMessage(message);
            }

        });
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

//        private ProgressDialog mProgressDialog;

        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                String apSsid = params[0];
                String apBssid = params[1];
                String apPassword = params[2];
                String isSsidHiddenStr = params[3];
                String taskResultCountStr = params[4];
                boolean isSsidHidden = false;
                if (isSsidHiddenStr.equals("YES")) {
                    isSsidHidden = true;
                }
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
                        isSsidHidden, mBaseActivity);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = "
                                + resultInList.getBssid()
                                + ",InetAddress = "
                                + resultInList.getInetAddress()
                                .getHostAddress() + "\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                    LogUtils.e(sb.toString());
                } else {
                    LogUtils.e("Esptouch fail");
                }
            }
        }
    }
}

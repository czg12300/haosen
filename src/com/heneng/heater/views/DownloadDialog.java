package com.heneng.heater.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.services.DownloadService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/22.
 */
public class DownloadDialog extends Dialog {
    @InjectView(R.id.download_tv_progress)
    TextView tv_progress;
    @InjectView(R.id.download_pb)
    ProgressBar pb;
    @InjectView(R.id.download_tv_background)
    TextView tv_bgdownload;
    @InjectView(R.id.download_tv_cancel)
    TextView tv_cancel;


    private String _url, _version;
    private Context _context;

    private boolean isForce;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //这里就一条消息
            int progress = msg.arg1;
            pb.setProgress(progress);
            tv_progress.setText("下载进度:" + progress + "%");
            if (progress >= 100) {
                dismiss();
            }
        }
    };


    public DownloadDialog(Context context, String url, String version, boolean isForce) {
        super(context);
        _url = url;
        _version = version;
        _context = context;
        this.isForce = isForce;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_activity_download);
        ButterKnife.inject(this);


        setTitle("下载");


        Intent intent = new Intent();
        intent.setAction(DownloadService.ACTION_BROCAST_DOWNLOAD);
        intent.putExtra(DownloadService.EXTRA_DOWNLOAD_URL, _url);
        intent.putExtra(DownloadService.EXTRA_DOWNLOAD_VERSION, _version);
        _context.sendBroadcast(intent);


        setCancelable(false);

        if (isForce) {
            tv_bgdownload.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_BROCAST_DOWNLOAD_PROGRESS);
        _context.registerReceiver(_receiver, filter);
    }

    @OnClick(R.id.download_tv_background)
    void onBackground() {
        dismiss();
    }

    @OnClick(R.id.download_tv_cancel)
    void onCancel() {


        AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("提示").setIcon(R.drawable.ic_launcher).setMessage("是否真的取消更新下载").setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isForce) {
                            System.exit(0);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(DownloadService.ACTION_BROCAST_STOP);
                            _context.sendBroadcast(intent);
                            dismiss();
                            DownloadDialog.this.dismiss();

                        }
                    }
                }
        ).

                setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }

                ).

                create();

        dialog.show();


    }


    BroadcastReceiver _receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_BROCAST_DOWNLOAD_PROGRESS.equals(intent.getAction())) {
                int precent = intent.getIntExtra(DownloadService.EXTRA_DOWNLOAD_PERCENT, 0);
                Message message = handler.obtainMessage();
                message.arg1 = precent;
                message.sendToTarget();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        _context.unregisterReceiver(_receiver);
    }
}

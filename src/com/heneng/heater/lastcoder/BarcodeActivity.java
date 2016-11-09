package com.heneng.heater.lastcoder;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.zxing.camera.CameraManager;
import com.heneng.heater.lastcoder.zxing.decoding.CaptureActivityHandler;
import com.heneng.heater.lastcoder.zxing.decoding.InactivityTimer;
import com.heneng.heater.lastcoder.zxing.view.ViewfinderView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

public class BarcodeActivity extends FragmentActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private TextView txtResult;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    ImageView RefreshBtn;
    String Barcode, Ename, UserID, UserCode;
    BarcodeActivity act;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        act = this;

        Bundle extBundle = getIntent().getExtras();
        UserID = extBundle.getString("userID");
        UserCode = extBundle.getString("userCode");

        ImageView ReturnBtn = (ImageView) findViewById(R.id.barcodeTop_return);
        ReturnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.quitSynchronously();
                    handler = null;
                }
                CameraManager.get().closeDriver();

                System.exit(0);
                //finish();
            }
        });

        // 初始化 CameraManager
        CameraManager.init(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        txtResult = (TextView) findViewById(R.id.txtResult);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        inactivityTimer.shutdown();

        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        //viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();
//		txtResult.setText(obj.getBarcodeFormat().toString() + ":"
//				+ obj.getText());
        Barcode = obj.getText();

        AsyncCheckTask asy = new AsyncCheckTask();
        asy.execute();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    // *
    // * When the beep has finished playing, rewind to queue up another one.
    // *
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    ////////////////////////////////////////////////////////////////////////////

    // 多次扫描
    public void ContinuePreview() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);
        if (handler != null)
            handler.restartPreviewAndDecode();
    }


    //关于loading
    LoadingFragment loadingDialog = null;

    public void ShowLoadingBarcode() {
        if (loadingDialog == null) {
            //IsRunningLoading=true;
            loadingDialog = new LoadingFragment();
            loadingDialog.setCancelable(false);
            loadingDialog.show(getSupportFragmentManager(), "Loading");
        }
    }

    public void CloseLoadingBarcode() {
        if (loadingDialog != null) {
            //IsRunningLoading=false;
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    class AsyncCheckTask extends AsyncTask<Void, Void, PostResult> {

        public AsyncCheckTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            ShowLoadingBarcode();
        }

        @Override
        protected PostResult doInBackground(Void... params) {
            PostResult result = new PostResult();
            try {
                //检查设备是否存在
                String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=CheckEquimentExist&uc="
                        + UserCode + "&eid=" + Barcode;
                String jstr = GetRemoteData.ConnServerForResult(strUrl, act);
                if (!jstr.equals("")) {
                    JSONObject jsObj = GetRemoteData.ParseJson(jstr, act);
                    String _r = jsObj.getString("Result");
                    String _info = jsObj.getString("Info");
                    if (_r.equals("1")) {
                        result.Result = "1";
                    } else {
                        result.Result = "0";
                    }
                    result.Info = _info;
                }

            } catch (Exception e) {
                result.Result = "0";
                result.Info = "数据获取异常:" + e.getMessage();

            } finally {
                return result;
            }
        }

        @Override
        protected void onPostExecute(PostResult result) {
            super.onPostExecute(result);
            CloseLoadingBarcode();
            if (result.Result.equals("0")) {
                //Toast.makeText(act, result.Info, Toast.LENGTH_SHORT).show();
                dialog(result.Info);
                //act.finish();
            } else {
                Ename = result.Info;
                AddEquipmentDialogFragment dialog = new AddEquipmentDialogFragment(Barcode, Ename, UserID, UserCode);
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "barcodeset");
            }
        }
    }

    protected void dialog(String msg) {
        AlertDialog.Builder builder = new Builder(BarcodeActivity.this);
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ContinuePreview();
            }
        });

        builder.create().show();
    }

}
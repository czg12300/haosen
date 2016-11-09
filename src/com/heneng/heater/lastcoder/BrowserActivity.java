package com.heneng.heater.lastcoder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.CheckFastClick;


public class BrowserActivity extends Activity {

    String Url;
    WebView Browser;
    ImageView RefreshBtn;
    LinearLayout container;

    // 刷新按钮动画相关
    Animation refreshAnim;
    int runningRefresh = 0; // 是否正在播放刷新动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        container = (LinearLayout) findViewById(R.id.container);

        ImageView ReturnBtn = (ImageView) findViewById(R.id.browserTop_return);
        ReturnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RefreshBtn = (ImageView) findViewById(R.id.browserTop_refresh);
        RefreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    if (refreshAnim.hasStarted()) {
                        RefreshBtn.clearAnimation();
                    }
                    RefreshBtn.startAnimation(refreshAnim);
                    Browser.reload();
                }
            }
        });
        initAnimation();

        Bundle extBundle = getIntent().getExtras();
        Url = extBundle.getString("url");
        Browser = (WebView) findViewById(R.id.browser_webView1);
        Browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings bs = Browser.getSettings();
        bs.setSupportMultipleWindows(false);
        bs.setCacheMode(WebSettings.LOAD_DEFAULT);
        Browser.loadUrl(Url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        container.removeAllViews();
        Browser.removeAllViews();
        Browser.destroy();
    }

    private void initAnimation() {

        refreshAnim = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        refreshAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                runningRefresh++;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runningRefresh--;
            }
        });
    }

}

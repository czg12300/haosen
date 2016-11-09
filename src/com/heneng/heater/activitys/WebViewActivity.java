package com.heneng.heater.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.heneng.heater.R;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.views.HeaderView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by huangqland on 15/9/19.
 */
public class WebViewActivity extends BaseActivity {

    @InjectView(R.id.webview_wb_content)
    WebView wv_content;
    @InjectView(R.id.webview_ll_container)
    LinearLayout ll_container;
    private String _url;
    private String _title;


    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_webview);
        ButterKnife.inject(this);
        Bundle extBundle = getIntent().getExtras();
        _url = extBundle.getString(Constants.KEY_EXTRA_URL);
        _title = extBundle.getString(Constants.KEY_EXTRA_TITLE);
        header.setTitle(_title);
        wv_content.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings bs = wv_content.getSettings();
        bs.setSupportMultipleWindows(false);
        bs.setCacheMode(WebSettings.LOAD_DEFAULT);
        wv_content.loadUrl(_url);
    }


    @Override
    protected void onDestroy() {
        wv_content.destroy();
        super.onDestroy();
    }


}

package com.heneng.heater.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.activitys.AccountActivity;
import com.heneng.heater.activitys.UpdateActivity;
import com.heneng.heater.activitys.WebViewActivity;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultUpdate;
import com.heneng.heater.utils.CommonUtils;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.HeaderView;
import com.heneng.heater.views.SettingItem;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yihao Huang on 2015/9/18.
 */
public class SettingFragment extends BaseFragment {
    @InjectView(R.id.header)
    HeaderView view_header;

    @InjectView(R.id.setting_item_account)
    SettingItem item_account;
    @InjectView(R.id.setting_item_update)
    SettingItem item_update;
    @InjectView(R.id.setting_item_story)
    SettingItem item_story;
    @InjectView(R.id.setting_item_contact)
    SettingItem item_contact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.a1_fragment_settings, null);
        ButterKnife.inject(this, _view);
        item_account.setSubTitle(UserInfo.Name);


        return _view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

    @OnClick(R.id.setting_item_account)
    public void toAccount() {
        openActivity(AccountActivity.class);
    }

    @OnClick(R.id.setting_item_story)
    public void toStory() {

        Bundle extra = new Bundle();
        extra.putString(Constants.KEY_EXTRA_URL, Constants.WebSite + "/WEBAPP/Introduction.htm");
        extra.putString(Constants.KEY_EXTRA_TITLE, "品牌故事");
        openActivityWithParams(WebViewActivity.class, extra);


    }

    @OnClick(R.id.setting_item_update)
    public void toUpdate() {
        openActivity(UpdateActivity.class);
    }

    @OnClick(R.id.setting_item_contact)
    public void contactUs() {
        Bundle extra = new Bundle();
        extra.putString(Constants.KEY_EXTRA_URL, Constants.WebSite + "/WEBAPP/ContactInfo.htm");
        extra.putString(Constants.KEY_EXTRA_TITLE, "联系我们");
        openActivityWithParams(WebViewActivity.class, extra);
    }


}

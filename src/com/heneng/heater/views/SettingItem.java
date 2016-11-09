package com.heneng.heater.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heneng.heater.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/9/18.
 */
public class SettingItem extends RelativeLayout {
    @InjectView(R.id.setting_item_icon)
    ImageView im_icon;
    @InjectView(R.id.setting_item_title)
    TextView tv_title;
    @InjectView(R.id.setting_item_subtitle)
    TextView tv_subtitle;
    @InjectView(R.id.setting_item_accessory)
    ImageView im_accessory;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        tv_title.setText(title);
    }

    public String getSubTitle() {
        return subTitle;

    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        tv_subtitle.setText(subTitle);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        im_icon.setVisibility(View.VISIBLE);
        im_icon.setImageResource(icon);
    }

    public int getAccessory() {
        return accessory;

    }

    public void setAccessory(int accessory) {
        this.accessory = accessory;
        im_accessory.setImageResource(accessory);
    }

    private String title = "Title", subTitle = "";
    private int icon = 0, accessory = R.drawable.arrow;


    public SettingItem(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.a1_view_setting_item, this);
        ButterKnife.inject(view);
        if (icon == 0) {
            im_icon.setImageBitmap(null);
            im_icon.setVisibility(View.GONE);
        } else {
            im_icon.setVisibility(View.VISIBLE);
            im_icon.setImageResource(icon);
        }

        im_accessory.setImageResource(accessory);
        tv_title.setText(title);
        tv_subtitle.setText(subTitle);

    }


    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);

        title = a.getString(R.styleable.SettingItem_settingItemTitle);
        subTitle = a.getString(R.styleable.SettingItem_settingItemSubTitle);
        icon = a.getResourceId(R.styleable.SettingItem_settingItemIcon, 0);
        accessory = a.getResourceId(R.styleable.SettingItem_settingItemAccessory, R.drawable.arrow);

        init(context);
    }
}

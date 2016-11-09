package com.heneng.heater.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetailMessage;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.DateUtil;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/9/20.
 */
public class DetaiMessageActivity extends BaseNetActivity implements NetUtils.NetCallbackListener {
    @InjectView(R.id.message_tv_title)
    TextView tv_title;
    @InjectView(R.id.message_tv_time)
    TextView tv_time;
    @InjectView(R.id.message_im_image)
    ImageView im_image;
    @InjectView(R.id.message_tv_content)
    TextView tv_content;


    private String _msgid;


    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_detail_message);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        _msgid = intent.getStringExtra(Constants.KEY_EXTRA_MSGID);


        initData();

    }

    private void initData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("uc", UserInfo.UserCode);
        params.put("msgid", _msgid);
        LogUtils.e("获取ID："+_msgid);
        NetUtils.executGet(mBaseActivity,Constants.CODE_GET_DETAIL_MESSAGE, "GetClientMessageDetail", params, ResultDetailMessage.class);
        showLoading();
    }


    @Override
    public void callback(int code, BaseModel model) {
        closeLoading();
        if (code == Constants.CODE_GET_DETAIL_MESSAGE) {
            ResultDetailMessage msg = (ResultDetailMessage) model;
            tv_title.setText(msg.getTitle());

            tv_content.setText(Html.fromHtml(msg.getContent()).toString());

//            LogUtils.e(Html.fromHtml(msg.getContent()).toString());

            Picasso.with(mBaseActivity).load(msg.getImage()).into(im_image);

            try {
                tv_time.setText("发布时间："+DateUtil.dateFormat(msg.getSendTime(), "yyyy-MM-dd"));
            } catch (ParseException e) {
                LogUtils.e("详细消息页，日期格式错误");
                e.printStackTrace();
            }

        }
    }
}

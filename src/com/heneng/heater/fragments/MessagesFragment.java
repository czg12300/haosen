package com.heneng.heater.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.activitys.DetaiMessageActivity;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultMessage;
import com.heneng.heater.models.ResultMessages;
import com.heneng.heater.models.ResultUnReadMessage;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.DateUtil;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.listview.RefreshableListView;
import com.heneng.heater.views.listview.extend.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/20.
 */
public class MessagesFragment extends BaseFragment implements NetUtils.NetCallbackListener {
    @InjectView(R.id.messages_lv)
    PullToRefreshListView lv_meassages;
    @InjectView(R.id.message_tv_menu_new)
    TextView tv_new;

    int _maxMsgid;

    private List<ResultMessage> _messages = new ArrayList<>();
    private MessageAdapter _adapter;
    private int _curDirction = 0;

    Handler _handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.a1_fragment_messages, null);
        ButterKnife.inject(this, _view);
        initData("0", _curDirction);
        initView();
        NetUtils.registNetCallbackListener(this);
        return _view;

    }

    Runnable unReadMessageRunable = new Runnable() {
        @Override
        public void run() {

            HashMap<String, Object> params = new HashMap<>();
            params.put("uc", UserInfo.UserCode);
            params.put("msgid", _maxMsgid);
            NetUtils.executGet(mContext,Constants.CODE_GET_UNREAD_MESSAGE, "GetUnReadMessage", params, ResultUnReadMessage.class);
            _handler.postDelayed(unReadMessageRunable, 60000);
        }
    };


    void getUnReadNum() {
        _handler.removeCallbacks(unReadMessageRunable);
        _handler.post(unReadMessageRunable);
    }


    private void initView() {

        lv_meassages.setOnUpdateTask(new RefreshableListView.OnPullUpUpdateTask() {
            @Override
            public void onUpdateStart() {
                LogUtils.e("onUpdateStart--setOnUpdateTask");
                initNewData();
            }
        });

        lv_meassages.setOnPullUpUpdateTask(new RefreshableListView.OnPullUpUpdateTask() {
            @Override
            public void onUpdateStart() {
                LogUtils.e("onUpdateStart--setOnPullUpUpdateTask");
                if (_messages.size() > 0) {
                    initData(_messages.get(_messages.size() - 1).getID(), -1);
                }
            }
        });

        lv_meassages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle extra = new Bundle();
                extra.putString(Constants.KEY_EXTRA_MSGID, _messages.get(i - 1).getID());
                openActivityWithParams(DetaiMessageActivity.class, extra);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnReadNum();
    }

    @Override
    public void onPause() {
        super.onPause();
        _handler.removeCallbacks(unReadMessageRunable);
    }

    @OnClick(R.id.message_im_menu_refress)
    void initNewData() {
        if (_messages.size() > 0) {
            initData(_messages.get(0).getID(), 1);
        }

    }


    void initData(String msgid, int direction) {

//        LogUtils.e("请求:" + msgid + "----" + direction);
        showLoading();
        if (_adapter == null) {
            _adapter = new MessageAdapter();
            lv_meassages.setAdapter(_adapter);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("uc", UserInfo.UserCode);
        params.put("user", UserInfo.ID);
        params.put("msgid", msgid);
        params.put("direction", direction + "");
        NetUtils.executGet(mContext,Constants.CODE_GET_MESSAGE, "GetClientMessageList", params, ResultMessages.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        NetUtils.unregistCallbackListener(this);
    }


    @Override
    public void callback(int code, BaseModel model) {


        if (code == Constants.CODE_GET_MESSAGE) {
            closeLoading();
            if (model instanceof ResultMessages) {
                ResultMessages messages = (ResultMessages) model;
                if (messages.getData() != null) {

                    for (ResultMessage msg : messages.getData()) {
                        if (!_messages.contains(msg)) {

                            _messages.add(msg);
//                            LogUtils.e(msg.getID());
                            int msgid = Integer.parseInt(msg.getID());
                            if (msgid > _maxMsgid) {
                                _maxMsgid = msgid;
                            }
                        }
                    }
                    getUnReadNum();
                    Collections.sort(_messages, new Comparator<ResultMessage>() {
                        @Override
                        public int compare(ResultMessage lhs, ResultMessage rhs) {

                            int id1 = Integer.parseInt(lhs.getID());
                            int id2 = Integer.parseInt(rhs.getID());


                            return id2 - id1;
                        }
                    });
                    _adapter.notifyDataSetChanged();
                } else {
                    LogUtils.e("获取信息数据为空。");
                }
            }
        } else if (code == Constants.CODE_GET_UNREAD_MESSAGE) {
            if (model == null) return;
            ResultUnReadMessage msgNum = (ResultUnReadMessage) model;
            if (TextUtils.isEmpty(msgNum.getUnReadNum()) || "0".equals(msgNum.getUnReadNum())) {
                tv_new.setText("");
            } else {
                tv_new.setText("(" + msgNum.getUnReadNum() + ")");
            }
        }
    }


    class MessageAdapter extends BaseAdapter {

        SimpleDateFormat _formatter = new SimpleDateFormat(Constants.FORMAT_SEVER_TIME);


        @Override
        public int getCount() {
            return _messages.size();
        }

        @Override
        public Object getItem(int i) {
            return _messages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder hodler;
            if (view == null) {
                view = View.inflate(mContext, R.layout.a1_messages_item, null);
                hodler = new ViewHolder(view);
                view.setTag(hodler);
            } else {
                hodler = (ViewHolder) view.getTag();
            }

            ResultMessage message = _messages.get(i);
            Picasso.with(mContext).setLoggingEnabled(true);
            Picasso.with(mContext).load(message.getSmallImage()).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(hodler.im_shortcut);
            String strTime = "1分钟前";
            try {

                Date time = _formatter.parse(message.getSendTime());
                strTime = DateUtil.getIntervalTimeFromNow(time.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }

//            LogUtils.e("msgID:" + message.getID() + "----message:" + message.getTitle());

            hodler.tv_time.setText(strTime);
            hodler.tv_title.setText(message.getTitle());
            hodler.tv_content.setText(message.getIntro());

            return view;
        }

        /**
         * This class contains all butterknife-injected Views & Layouts from layout file 'a1_messages_item.xml'
         * for easy to all layout elements.
         *
         * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
         */

    }

    static class ViewHolder {
        @InjectView(R.id.messages_item_tv_time)
        TextView tv_time;
        @InjectView(R.id.messages_item_im_shortcut)
        ImageView im_shortcut;
        @InjectView(R.id.messages_item_tv_title)
        TextView tv_title;
        @InjectView(R.id.messages_item_tv_content)
        TextView tv_content;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}

package com.heneng.heater.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heneng.heater.R;
import com.heneng.heater.activitys.WaterHeaterActivity;
import com.heneng.heater.lastcoder.DetailActivity;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultDeviceList;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.models.ResultMessages;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.sdlv.Menu;
import com.heneng.heater.views.sdlv.MenuItem;
import com.heneng.heater.views.sdlv.SlideAndDragListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/14.
 */
public class DevicesFragment extends BaseNetFragment implements View.OnClickListener {

    @InjectView(R.id.device_lv_content)
    SlideAndDragListView lv_content;
    @InjectView(R.id.device_im_add)
    ImageView im_add;
    @InjectView(R.id.device_im_refress)
    ImageView im_refress;


    private List<ResultDeviceList.Device> _devices;
    private MyAdapter _adapter;

    private ResultDeviceList.Device _chooseDelDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.a1_fragment_devices, null);
        ButterKnife.inject(this, view);

        im_add.setOnClickListener(this);
        im_refress.setOnClickListener(this);
        _devices = new ArrayList<>();

        Menu menu = new Menu((int) getResources().getDimension(R.dimen.devices_item_menu_height), new ColorDrawable(Color.RED), true);
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.devices_item_menu_height))
                .setIcon(getResources().getDrawable(R.drawable.delete_btn))
                .build());
        lv_content.setMenu(menu);

        _adapter = new MyAdapter();
        lv_content.setAdapter(_adapter);

        lv_content.setOnListItemClickListener(new SlideAndDragListView.OnListItemClickListener() {
            @Override
            public void onListItemClick(View v, int position) {
                _app._chooseDevice = _devices.get(position);
                if (_app._chooseDevice.getEquiType().equals("电热水器")) {
                    openActivity(WaterHeaterActivity.class);
                } else {
                    openActivity(DetailActivity.class);
                }
            }
        });

        lv_content.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                delDevice(_devices.get(itemPosition));
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        showLoading();
        Map<String, Object> params = getParamsMap();
        params.put("uc", UserInfo.UserCode);
        params.put("user", UserInfo.ID);
        NetUtils.executGet(mContext, Constants.CODE_GET_BASELIST, "GetBaseList", params, ResultDeviceList.class);

    }

    @Override
    public void callback(int code, BaseModel model) {

        if (code == Constants.CODE_GET_BASELIST) {
            closeLoading();
            ResultDeviceList result = (ResultDeviceList) model;
            initListView();
            _app._deviceList = result.getData();
            _devices = result.getData();
        } else if (code == Constants.CODE_DEL_USER_EQUIMENT) {
            closeLoading();
            if (model == null) return;
            ResultDetail result = (ResultDetail) model;
            if ("1".equals(result)) {
                if (_chooseDelDevice != null) {
                    _devices.remove(_chooseDelDevice);
                    _adapter.notifyDataSetChanged();
                }
            }
            Toast.makeText(mContext, result.getInfo(), Toast.LENGTH_SHORT).show();

        }
    }

    private void initListView() {
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_im_add:

                addDevice();


                break;
            case R.id.device_im_refress:
                if (!isLoading()) {
                    initData();
                }
                break;
        }
    }

    /**
     * 点击添加设备
     */
    private void addDevice() {
        Uri uri = Uri.parse("barcode_info://process");
        Intent intent = new Intent("com.heneng.heater.BarcodeActivity", uri);
        Bundle bundle = new Bundle();
        //由于使用新进程打开需要额外携带用户信息
        bundle.putString("userID", UserInfo.ID + "");
        bundle.putString("userCode", UserInfo.UserCode);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return _devices.size();
        }

        @Override
        public Object getItem(int position) {
            return _devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = View.inflate(mContext, R.layout.a1_fragment_device_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            final ResultDeviceList.Device device = _devices.get(position);

            holder.tv_name.setText(device.getEname());
            //ICON
            Picasso.with(mContext).load(device.getImage()).into(holder.im_icon);

            int onlineImg;
            int onlineVal = Integer.parseInt(device.getOnLine());
            switch (onlineVal) {
                default:
                    onlineImg = R.drawable.online0;
                    break;
                case 1:
                    onlineImg = R.drawable.online1;
                    break;
            }
            holder.im_online.setImageResource(onlineImg);


            int powerImg;
            int powerVal = Integer.parseInt(device.getOnoff());
            switch (powerVal) {
                default:
                    holder.im_power.setSelected(false);
                    break;
                case 1:
                    holder.im_power.setSelected(true);
                    break;
            }


            holder.im_power.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchPowerStae((ImageView) v, device);
                }
            });


            return view;
        }

    }


    BroadcastReceiver _receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.BROCAST_PUSH_STATE)) {
                String msg = (String) intent.getSerializableExtra(Constants.KEY_EXTRA_PUSH_STATE);
                Gson gson = new Gson();
                ResultGetNowData state = gson.fromJson(msg, ResultGetNowData.class);
                pushCallback(state);
            }
        }
    };

    public void pushCallback(ResultGetNowData data) {

        for (ResultDeviceList.Device device : _devices) {
            if (device.getEID().equals(data.getEID())) {
                device.setOnLine(data.getOnLine());
            }
        }
        _adapter.notifyDataSetChanged();

    }

    /**
     * 删除设备
     *
     * @param device
     */
    private void delDevice(final ResultDeviceList.Device device) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        LogUtils.e("强制更新");
        builder.setTitle("删除");
        builder.setMessage("确定移除设备" + device.getEname() + "吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _chooseDelDevice = device;
                Map<String, Object> params = getParamsMap();
                params.put("uc", UserInfo.UserCode);
                params.put("user", UserInfo.ID);
                params.put("eid", device.getEID());
                showLoading();
                NetUtils.executGet(mContext, Constants.CODE_DEL_USER_EQUIMENT, "DelUserEquiment", params, ResultDetail.class);
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.setCancelable(false);
        builder.create().show();


    }

    /**
     * 切换设备电源状态
     *
     * @param v
     * @param device
     */
    private void switchPowerStae(ImageView v, ResultDeviceList.Device device) {
        _app._chooseDevice = device;
        int onoff = "1".equals(device.getOnoff()) ? 0 : 1;
        Map<String, Object> params = getParamsMap();
        params.put("uc", UserInfo.UserCode);
        params.put("user", UserInfo.ID);
        params.put("eid", device.getEID());
        params.put("onoff", onoff);
        if ("0".equals(device.getOnLine())) {
            Toast.makeText(mContext, "这个设备不在线", Toast.LENGTH_SHORT).show();
        } else {
            NetUtils.executGet(mContext, Constants.CODE_SET_ONOFF, "Com_SetOnOff", params, ResultDetail.class);
            v.setSelected(!v.isSelected());
        }

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'a1_fragment_device_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {


        @InjectView(R.id.device_item_im_icon)
        ImageView im_icon;
        @InjectView(R.id.device_item_tv_name)
        TextView tv_name;
        @InjectView(R.id.device_item_im_online)
        ImageView im_online;
        @InjectView(R.id.device_item_im_power)
        ImageView im_power;
        @InjectView(R.id.device_item_rl_content)
        RelativeLayout rl_content;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

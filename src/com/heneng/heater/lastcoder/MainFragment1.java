package com.heneng.heater.lastcoder;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.heneng.heater.R;
import com.heneng.heater.activitys.WaterHeaterActivity;
import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.common.MySimpleAdapter;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.utils.LogUtils;


public class MainFragment1 extends Fragment {

    ListView lvBaseList = null;
    MySimpleAdapter adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();

    View lvItemContentView = null;
    RelativeLayout lvItemBtnBox = null;
    RelativeLayout.LayoutParams lvItemParams1 = null;
    RelativeLayout lvItemShowBox = null;
    RelativeLayout.LayoutParams lvItemParams2 = null;

    int lvBtnBoxState = 0;
    int lvBtnBoxWidth = 0;
    int lvCurTouchIdx = -1;

    float lvStartX = 0;
    float lvStartY = 0;
    //boolean lvItemMoving=false;
    boolean lvIsItemAnimation = false;

    boolean lvIsScrolling = false;
    boolean lvEnableScroll = true;
    boolean lvToolClickable = false;

    private Date lastLoadDate;
    private List<String> onoffEquiment;
    private LoadingFragment loadingDialog;
    private Executor executor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date lastLoadDate = Calendar.getInstance().getTime();
        onoffEquiment = new ArrayList<>();
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_lay1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvBtnBoxWidth = CommonFunction.dip2px(getActivity(), 80);

        lvBaseList = (ListView) view.findViewById(R.id.lvBaseList);
        lvBaseList.setOverScrollMode(View.OVER_SCROLL_NEVER);

        List<Map<String, Object>> eventList = new ArrayList<Map<String, Object>>();
        eventList.add(null);
        eventList.add(null);
        Map<String, Object> event3 = new HashMap<String, Object>();
        event3.put("event", "onclick");
        event3.put("listener", new View.OnClickListener() {
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick()) {
                    int idx = Integer.parseInt(v.getTag().toString());
                    String _eid = datalist.get(idx).get("vlistitem_eid").toString();
                    if (!onoffEquiment.contains(_eid)) {
                        onoffEquiment.add(_eid);
                        ShowLoading();
                        AsyncOnOffTask asyonff = new AsyncOnOffTask(idx);
                        asyonff.executeOnExecutor(executor);
                    }
                }
            }
        });

        eventList.add(event3);
        Map<String, Object> event4 = new HashMap<String, Object>();
        event4.put("event", "onclick");
        event4.put("listener", new View.OnClickListener() {
            public void onClick(View v) {
                if (!lvToolClickable) return;
                //Log.d("delClick", "delClick");
                if (!CheckFastClick.IsFastClick()) {
                    int idx = Integer.parseInt(v.getTag().toString());
                    ShowLoading();

                    AsyncDelEquiTask asyDel = new AsyncDelEquiTask(idx);
                    asyDel.executeOnExecutor(executor);

                }
            }
        });
        eventList.add(event4);

        adapter = new MySimpleAdapter(getActivity(), datalist, R.layout.baselist_item,
                new String[]{"vlistitem_name", "vlistitem_img",
                        "vlistitem_ico", "vlistitem_del_img", "vlistitem_online_img"}, new int[]{R.id.vlistitem_name,
                R.id.vlistitem_img, R.id.vlistitem_ico, R.id.vlistitem_delbtn, R.id.vlistitem_online_img}, eventList);
        lvBaseList.setAdapter(adapter);

        lvBaseList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (lvBtnBoxState == 1 || lvIsItemAnimation) return;
                Log.d("itemonTouch", "itemonTouch:onItemClick");
                if (!CheckFastClick.IsFastClick()) {
                    ShowLoading();
                    int idx = arg2;
                    String _eid = datalist.get(idx).get("vlistitem_eid")
                            .toString();
                    String _name = datalist.get(idx).get("vlistitem_name")
                            .toString();

                    AppAplication app = (AppAplication) getActivity().getApplication();




                    Intent intent;

//                    if (datalist.get(idx).get("vlistitem_img").equals(R.drawable.ele2)) {
//                        intent = new Intent(getActivity(), WaterHeaterActivity.class);
//                    } else {
                    intent = new Intent(getActivity(), DetailActivity.class);
//                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("eid", _eid);
                    bundle.putString("equiName", _name);
                    bundle.putInt("etype", Integer.parseInt(datalist.get(idx).get("vlistitem_img").toString()));
                    bundle.putString("refererPage", "main1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

        });

        lvBaseList.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (lvBtnBoxState == 1) {
                    lvEnableScroll = false;
                    lvBtnBoxState = 0;
                    DoLvItemTransAnim(-1, 300);
                    return;
                }

                if (scrollState == 0) {
                    Handler th = new Handler();
                    Runnable TimingUpdateRunnable = new Runnable() {
                        public void run() {
                            lvIsScrolling = false;
                        }
                    };
                    th.postDelayed(TimingUpdateRunnable, 200);
                    lvItemContentView = null;
                } else {
                    lvIsScrolling = true;
                }
                Log.d("onScroll", "onScroll:" + scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

        lvBaseList.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (lvIsScrolling) return false;
                if (lvIsItemAnimation || !lvEnableScroll) return true;
                //lvItemMoving=true;
                Log.d("itemonTouch", "itemonTouch");

                if (lvItemContentView == null) {
                    Log.d("itemonTouch", "itemonTouch:init content");
                    int index = lvBaseList.pointToPosition((int) event.getX(), (int) event.getY());
                    if (index >= 0) {
                        if (lvCurTouchIdx != index && lvBtnBoxState == 1) {
                            lvBtnBoxState = 0;
                            DoLvItemTransAnim(-1, 300);
                            return true;
                        }
                        lvCurTouchIdx = index;

                        int firstVisible = lvBaseList.getFirstVisiblePosition();
                        lvItemContentView = lvBaseList.getChildAt(index - firstVisible);
                        lvItemBtnBox = (RelativeLayout) lvItemContentView.findViewById(R.id.vlistitem_btnbox);
                        lvItemParams1 = (RelativeLayout.LayoutParams) lvItemBtnBox.getLayoutParams();
                        lvItemShowBox = (RelativeLayout) lvItemContentView.findViewById(R.id.vlistitem_showbox);
                        lvItemParams2 = (RelativeLayout.LayoutParams) lvItemShowBox.getLayoutParams();


                        lvItemShowBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_lvbaselist));

//			            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			            	Log.d("itemonTouch", "itemonTouch:ACTION_DOWN");
                        lvStartX = event.getX();
                        lvStartY = event.getY();
//	                    	lvStartMargin=lvItemParams1.leftMargin;
//	                    	lvStartMargin2=lvItemParams2.leftMargin;


//			            }
                    }

                } else {
                    Log.d("itemonTouch", "itemonTouch:after content");

                    if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {// �ɿ�����
                        lvItemShowBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_lvbaselist));
                        lvItemContentView = null;

//		            	Handler th=new Handler();
//		                Runnable TimingUpdateRunnable=new Runnable(){
//		        	        public void run() {
//		        	        	lvItemMoving=false;
//		        	        }
//		                };
//		                th.postDelayed(TimingUpdateRunnable, 100);


                        Log.d("itemonTouch", "itemonTouch:ACTION_CANCEL;margin:" + lvItemParams1.leftMargin);

                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {


                        float juli = event.getX() - lvStartX;
                        float juliY = event.getY() - lvStartY;
                        Log.d("juli", "juli:" + juli + " " + juliY);
                        if (Math.abs(juliY) >= 5) return false;

                        if (Math.abs(juli) <= 5) {
                            return false;
                        }
                        if (juli > 0 && lvBtnBoxState == 1) return true;
                        else if (juli < 0 && lvBtnBoxState == 0) return false;
                        else if (juli == 0) return false;


                        lvBaseList.clearFocus();
                        if (juli > 0) {
                            lvBtnBoxState = 1;
                            lvItemBtnBox.setVisibility(View.VISIBLE);
                            DoLvItemTransAnim(1, 300);
                            return true;
                        } else if (juli < 0) {
                            lvBtnBoxState = 0;
                            DoLvItemTransAnim(-1, 300);
                            return true;
                        }

                        //Log.d("itemMove", "itemMove:"+m);
                        return false;

                    } else {
                        Log.d("itemonTouch", "itemonTouch:other");
                    }
                }

                return false;
            }
        });


        final Handler TimingUpdateHandler = new Handler();
        Runnable TimingUpdateRunnable = new Runnable() {
            public void run() {
                if (!pageIsPause) {
                    timingLoadData();
                }
                TimingUpdateHandler.postDelayed(this, 20000);
            }
        };
        TimingUpdateHandler.postDelayed(TimingUpdateRunnable, 20000);

        GetListDataHandler(true);
    }

    boolean firstOnResume = true;
    boolean pageIsPause = false;

    @Override
    public void onPause() {
        super.onPause();
        pageIsPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d("", "MainFragment1:onResume-"+firstOnResume);
        CloseLoading();
        if (!firstOnResume) {
            GetListDataHandler(false);
        }
        firstOnResume = false;
        pageIsPause = false;
    }


    public void InitLvState() {
        if (lvCurTouchIdx >= 0 && lvBtnBoxState == 1) {
            lvBtnBoxState = 0;
            DoLvItemTransAnim(-1, 0);
        }
    }

    public void GetListDataHandler(Boolean showLoading) {
        if (showLoading) {
            ShowLoading();
        }
        AsyncUpdateDatasTask task = new AsyncUpdateDatasTask();
        task.executeOnExecutor(executor);
    }

    class AsyncUpdateDatasTask extends AsyncTask<Void, Void, PostResult> {

        @Override
        protected PostResult doInBackground(Void... params) {
            PostResult r = new PostResult();
            List<Map<String, Object>> result = getListData(r);
            int len1 = result.size() > datalist.size() ? datalist.size() : result.size();
            int len2 = result.size() <= datalist.size() ? datalist.size() : result.size();
            if (datalist.size() > result.size()) {
                for (int k = len2 - 1; k >= len1; k--) {
                    LogUtils.e("移除："+datalist.get(k).get("vlistitem_name").toString());
                //    datalist.remove(k);

                }
                len2 = len1;
            }

            for (int i = 0; i < len1; i++) {
                Map<String, Object> map = datalist.get(i);
                // map.put("vlistitem_id",result.get(i).get("ID"));
                // map.put("vlistitem_eid",result.get(i).get("EID"));
                // map.put("vlistitem_wsid",result.get(i).get("Wsid"));
                // map.put("vlistitem_ename",result.get(i).get("Ename"));
                // map.put("vlistitem_equitype",result.get(i).get("EquiType"));
                // map.put("vlistitem_onoff",result.get(i).get("Onoff"));


                map.put("vlistitem_name", result.get(i).get("vlistitem_name"));
                map.put("vlistitem_img", result.get(i).get("vlistitem_img"));
                map.put("vlistitem_ico", result.get(i).get("vlistitem_ico"));
                map.put("vlistitem_eid", result.get(i).get("vlistitem_eid"));
                map.put("vlistitem_onoff", result.get(i).get("vlistitem_onoff"));
                map.put("vlistitem_online_val", result.get(i).get("vlistitem_online_val"));
                map.put("vlistitem_online_img", result.get(i).get("vlistitem_online_img"));
                map.put("vlistitem_del_img", result.get(i).get("vlistitem_del_img"));

            }
            if (len2 > len1)
                LogUtils.e("得到结果");
            datalist.addAll(result.subList(len1, len2));

            return r;
        }

        @Override
        protected void onPostExecute(PostResult result) {
            super.onPostExecute(result);
            CloseLoading();
            if (result.Result.equals("0")) {
                Toast.makeText(getActivity(), result.Info, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            lastLoadDate = Calendar.getInstance().getTime();
        }
    }

    private List<Map<String, Object>> getListData(PostResult result) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            //String strUrl =
            //ServerInfo.WebApi+"/AppHandler.ashx?Method=GetBaseList&uc=1234&user=1";
            String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=GetBaseList&uc="
                    + UserInfo.UserCode + "&user=" + UserInfo.ID;
            String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
            if (!jstr.equals("")) {
                JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
                JSONArray arr = jsObj.getJSONArray("Data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject item = ((JSONObject) arr.opt(i));
                    Map<String, Object> map = new HashMap<String, Object>();

                    int img = R.drawable.equiment;
                    int _eleType = Integer.parseInt(item.getString("EleType"));
                    switch (_eleType) {
                        case 1:
                            img = R.drawable.ele1;
                            break;
                        case 2:
                            img = R.drawable.ele2;
                            break;
                        case 3:
                            img = R.drawable.ele3;
                            break;
                    }

                    int ico = R.drawable.close;
                    int _onoff = Integer.parseInt(item.getString("Onoff"));
                    switch (_onoff) {
                        case 0:
                            ico = R.drawable.close;
                            break;
                        case 1:
                            ico = R.drawable.open;
                            break;
                    }

                    int _onlineImg = R.drawable.online0;
                    int _onlineVal = Integer.parseInt(item.getString("OnLine"));
                    switch (_onlineVal) {
                        case 0:
                            _onlineImg = R.drawable.online0;
                            break;
                        case 1:
                            _onlineImg = R.drawable.online1;
                            break;
                    }

                    String _eid = item.getString("EID");
                    if (onoffEquiment.contains(_eid))
                        ico = R.drawable.waiting;

                    map.put("vlistitem_name", item.getString("Ename"));
                    map.put("vlistitem_img", img);
                    map.put("vlistitem_ico", ico);
                    map.put("vlistitem_eid", _eid);
                    map.put("vlistitem_onoff", _onoff);
                    map.put("vlistitem_online_val", _onlineVal);
                    map.put("vlistitem_online_img", _onlineImg);
                    map.put("vlistitem_del_img", R.drawable.delete_btn);

                    list.add(map);
                }
            }
            result.Result = "1";
        } catch (Exception e) {
            result.Result = "0";
            result.Info = "数据获取异常:" + e.getMessage();
        } finally {
            return list;
        }

    }

    private void timingLoadData() {
        if (onoffEquiment != null && onoffEquiment.size() > 0)
            return;
        if (lastLoadDate != null) {
            long t = (new Date()).getTime() - lastLoadDate.getTime();
            if (t >= 10000) {
                GetListDataHandler(false);
            }
        }
    }

    class AsyncOnOffTask extends AsyncTask<Void, Void, PostResult> {
        private int controlIdx;
        private String eid;
        private ImageView itemIco;

        public AsyncOnOffTask(int idx) {
            super();
            this.controlIdx = idx;
            eid = datalist.get(controlIdx).get("vlistitem_eid").toString();
        }

        @Override
        protected void onPreExecute() {
            itemIco = (ImageView) lvBaseList.getChildAt(controlIdx).findViewById(R.id.vlistitem_ico);
            itemIco.setImageResource(R.drawable.waiting);
        }

        @Override
        protected PostResult doInBackground(Void... params) {
            PostResult result = new PostResult();
            try {
                if (controlIdx >= 0) {
                    String onoff = datalist.get(controlIdx).get("vlistitem_onoff").toString();
                    onoff = onoff.equals("1") ? "0" : "1";

//					String strUrl = ServerInfo.WebApi+"/AppHandler.ashx?Method=SettingCommand&uc="
//							+UserInfo.UserCode+"&eid="+eid+"&minor_type=22&message="+onoff;
                    String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=Com_SetOnOff&uc="
                            + UserInfo.UserCode + "&eid=" + eid + "&user=" + UserInfo.ID + "&onoff=" + onoff;
                    String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
                    if (!jstr.equals("")) {
                        JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
                        String _r = jsObj.getString("Result");
                        String _info = jsObj.getString("Info");
                        if (_r.equals("1")) {
                            int postcount = 0;
                            while (true) {
                                strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=GetSettingResult&uc="
                                        + UserInfo.UserCode + "&eid=" + eid + "&setID=" + _info;
                                jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
                                if (!jstr.equals("")) {
                                    jsObj = GetRemoteData.ParseJson(jstr, getActivity());
                                    int _r2 = Integer.parseInt(jsObj.getString("Result"));
                                    if (_r2 == 2) {
                                        result.Result = "1";
                                        datalist.get(controlIdx).put("vlistitem_onoff", onoff);
                                        break;
                                    } else if (_r2 == 3) {
                                        result.Result = "0";
                                        result.Info = jsObj.getString("Info");
                                        break;
                                    }
                                }
                                postcount++;
                                if (postcount > AppParams.ExcuteCommandWait) {
                                    result.Result = "0";
                                    result.Info = "命令执行长时间没有响应";
                                    break;
                                }
                                Thread.sleep(1000);
                            }
                        } else {
                            result.Result = "0";
                            result.Info = _info;
                        }
                    }
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
            if (result.Result.equals("0")) {

                Toast.makeText(getActivity(), result.Info, Toast.LENGTH_SHORT).show();

            }

            String onoff = datalist.get(controlIdx).get("vlistitem_onoff").toString();
            int icoImg = onoff.equals("1") ? R.drawable.open : R.drawable.close;
            datalist.get(controlIdx).put("vlistitem_ico", icoImg);
            itemIco.setImageResource(icoImg);

            if (onoffEquiment.contains(eid))
                onoffEquiment.remove(eid);

            GetListDataHandler(true);
        }
    }

    class AsyncDelEquiTask extends AsyncTask<Void, Void, PostResult> {
        private int controlIdx;
        private String eid;

        public AsyncDelEquiTask(int idx) {
            super();
            this.controlIdx = idx;
            eid = datalist.get(controlIdx).get("vlistitem_eid").toString();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected PostResult doInBackground(Void... params) {
            PostResult result = new PostResult();
            try {
                if (controlIdx >= 0) {
                    String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=DelUserEquiment&uc="
                            + UserInfo.UserCode + "&user=" + UserInfo.ID + "&eid=" + eid;
                    String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
                    if (!jstr.equals("")) {
                        JSONObject jsObj = GetRemoteData.ParseJson(jstr, getActivity());
                        String _r = jsObj.getString("Result");
                        String _info = jsObj.getString("Info");
                        if (_r.equals("1")) {
                            result.Result = "1";
                            result.Info = _info;
                        } else {
                            result.Result = "0";
                            result.Info = _info;
                        }
                    }
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
            if (result.Result.equals("0")) {
                Toast.makeText(getActivity(), result.Info, Toast.LENGTH_SHORT).show();
            } else {
                InitLvState();
            }
            GetListDataHandler(true);
        }
    }

    public void ShowLoading() {
        if (loadingDialog == null) {
            try {
                if (!isDetached()) {
                    loadingDialog = new LoadingFragment();
                    loadingDialog.setCancelable(false);
                    loadingDialog.show(getChildFragmentManager(), "Loading");
                }
            } catch (Exception e) {
                e.printStackTrace();
                CloseLoading();
            }
        }
    }

    public void CloseLoading() {
        try {
            if (loadingDialog != null && loadingDialog.isVisible() && !isDetached()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DoLvItemTransAnim(final int dirction, int playtime) {
        lvIsItemAnimation = true;
        lvEnableScroll = false;

        TranslateAnimation tAnim = null;
        TranslateAnimation tAnim2 = null;
        if (dirction > 0) {
            tAnim = new TranslateAnimation(0, lvBtnBoxWidth, 0, 0);
        } else if (dirction < 0) {
            tAnim = new TranslateAnimation(0, -lvBtnBoxWidth, 0, 0);
        } else {
            return;
        }

        tAnim.setDuration(playtime);
        lvItemShowBox.startAnimation(tAnim);

        lvItemShowBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_lvbaselist2));

        tAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lvIsItemAnimation = false;
                //lvItemMoving=false;
                lvItemContentView = null;
                lvEnableScroll = true;

                if (dirction > 0) {
//					lvItemParams1.setMargins(0,0,0,0);
//					lvItemBtnBox.setLayoutParams(lvItemParams1);
                    lvItemParams2.setMargins(lvBtnBoxWidth, 0, -lvBtnBoxWidth, 0);
                    lvItemShowBox.setLayoutParams(lvItemParams2);
                    lvToolClickable = true;

                } else if (dirction < 0) {
//					lvItemParams1.setMargins(-lvBtnBoxWidth,0,0,0);
//					lvItemBtnBox.setLayoutParams(lvItemParams1);
                    lvItemParams2.setMargins(0, 0, 0, 0);
                    lvItemShowBox.setLayoutParams(lvItemParams2);
                    lvToolClickable = false;
                    lvItemBtnBox.setVisibility(View.GONE);
                }
                lvItemShowBox.clearAnimation();

            }
        });
    }
}

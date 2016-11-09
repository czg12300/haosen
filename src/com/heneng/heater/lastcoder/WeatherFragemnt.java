package com.heneng.heater.lastcoder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heneng.heater.R;
import com.heneng.heater.lastcoder.baseClass.BaseFragment;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.NowState;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.lastcoder.model.WeatherResult;
import com.heneng.heater.models.ResultGetNowData;


/**
 * Created by KUANGJH on 2015-8-10.
 */
public class WeatherFragemnt extends BaseFragment {

    TextView weather_temp;
    TextView weather_ham;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weather_temp = (TextView) view.findViewById(R.id.weather_temp);
        weather_ham = (TextView) view.findViewById(R.id.weather_ham);
    }
    public void pushCallback(ResultGetNowData data) {

        BindData(data);

    }
    @Override
    public void BindData(ResultGetNowData data) {
        if (data != null) {
            AsyncWeatherTask weatherTask = new AsyncWeatherTask();
            weatherTask.execute(data);
        }
    }

    private class AsyncWeatherTask extends AsyncTask<ResultGetNowData, Void, WeatherResult> {

        @Override
        protected WeatherResult doInBackground(ResultGetNowData... params) {
            try {
                ResultGetNowData data = params[0];
                String strUrl ="http://"+ ((AppAplication)getActivity().getApplication())._chooseDevice.getInterface()+ "/AppHandler.ashx?Method=Com_CityWeather&uc="
                        + UserInfo.UserCode + "&eid=" + data.getEID();
                String jstr = GetRemoteData.ConnServerForResult(strUrl, getActivity());
                if (!TextUtils.isEmpty(jstr)) {
                    return new Gson().fromJson(jstr, WeatherResult.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherResult result) {
            super.onPostExecute(result);
            if (result != null) {
                weather_temp.setText(result.temp + "℃");
                weather_ham.setText(result.hum + "%");
            }
        }
    }
}

package com.heneng.heater.models;

import java.util.List;

/**
 * Created by Administrator on 2015/9/20.
 */
public class ResultMessages extends BaseModel {

    public String getUnreceived() {
        return Unreceived;
    }

    public void setUnreceived(String unreceived) {
        Unreceived = unreceived;
    }

    public String getLogined() {
        return Logined;
    }

    public void setLogined(String logined) {
        Logined = logined;
    }

    public List<ResultMessage> getData() {
        return Data;
    }

    public void setData(List<ResultMessage> data) {
        Data = data;
    }

    /**
     * Data : ["记录内容","记录内容","记录内容"]
     * Unreceived : 未接收新消息数量
     * Logined : 0/1
     */

    private String Unreceived;
    private String Logined;
    private List<ResultMessage> Data;

    @Override
    public String toString() {
        return "Messages{" +
                "Unreceived='" + Unreceived + '\'' +
                ", Logined='" + Logined + '\'' +
                ", Data=" + Data +
                '}';
    }
}

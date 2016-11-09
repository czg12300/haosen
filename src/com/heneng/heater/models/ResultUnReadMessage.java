package com.heneng.heater.models;

/**
 * Created by Administrator on 2016/1/21.
 */
public class ResultUnReadMessage extends BaseModel {

    /**
     * UnReadNum : ”未读消息数目”
     * Logined : 0/1
     */

    private String UnReadNum;
    private String Logined;

    public void setUnReadNum(String UnReadNum) {
        this.UnReadNum = UnReadNum;
    }

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public String getUnReadNum() {
        return UnReadNum;
    }

    public String getLogined() {
        return Logined;
    }
}

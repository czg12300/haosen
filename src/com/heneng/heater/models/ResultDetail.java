package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ResultDetail extends BaseModel {


    /**
     * Result : 0/1
     * Info : 执行信息
     * Logined : 0/1
     */

    private String Result;
    private String Info;
    private String Logined;

    public void setResult(String Result) {
        this.Result = Result;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public String getResult() {
        return Result;
    }

    public String getInfo() {
        return Info;
    }

    public String getLogined() {
        return Logined;
    }
}

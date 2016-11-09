package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ResultCommon extends BaseModel {


    /**
     * Result : 0/1
     * Info : 执行信息
     */

    private String Result;
    private String Info;

    public void setResult(String Result) {
        this.Result = Result;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public String getResult() {
        return Result;
    }

    public String getInfo() {
        return Info;
    }

    @Override
    public String toString() {
        return "CodeResult{" +
                "Result='" + Result + '\'' +
                ", Info='" + Info + '\'' +
                '}';
    }
}

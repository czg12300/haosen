package com.heneng.heater.models;

/**
 * Created by Administrator on 2016/1/19.
 */
public class ResultErrorDescription extends BaseModel {

    /**
     * errCode : 错误码
     * errtext : 错误详述
     * errShotText : 错误简述
     * Logined : 0/1
     */

    private String errCode;
    private String errtext;
    private String errShotText;
    private String Logined;

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrtext(String errtext) {
        this.errtext = errtext;
    }

    public void setErrShotText(String errShotText) {
        this.errShotText = errShotText;
    }

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrtext() {
        return errtext;
    }

    public String getErrShotText() {
        return errShotText;
    }

    public String getLogined() {
        return Logined;
    }
}

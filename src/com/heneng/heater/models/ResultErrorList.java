package com.heneng.heater.models;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ResultErrorList extends BaseModel{

    /**
     * Data : [{"errCode":"错误码","errtext":"错误详述","errShotText":"错误简述"}]
     * Logined : 0/1
     */

    private String Logined;
    /**
     * errCode : 错误码
     * errtext : 错误详述
     * errShotText : 错误简述
     */

    private List<DataEntity> Data;

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public String getLogined() {
        return Logined;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public static class DataEntity {
        private String errCode;
        private String errtext;
        private String errShotText;

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public void setErrtext(String errtext) {
            this.errtext = errtext;
        }

        public void setErrShotText(String errShotText) {
            this.errShotText = errShotText;
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
    }
}

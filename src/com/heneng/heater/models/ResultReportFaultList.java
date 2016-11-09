package com.heneng.heater.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/10/27.
 */
public class ResultReportFaultList extends BaseModel implements Serializable {


    /**
     * Data : [{"ID":"记录ID","EID":"设备条码","Ename":"设备名","ErrorCode":"错误码","ClientName":"客户名","ErrorDescription":"故障描述","CreateTime":"报障时间","Step":"处理进度"}]
     * Logined : 0/1
     */

    private String Logined;
    /**
     * ID : 记录ID
     * EID : 设备条码
     * Ename : 设备名
     * ErrorCode : 错误码
     * ClientName : 客户名
     * ErrorDescription : 故障描述
     * CreateTime : 报障时间
     * Step : 处理进度
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

    public static class DataEntity implements Serializable{
        private String ID;
        private String EID;
        private String Ename;
        private String ErrorCode;
        private String ClientName;
        private String ErrorDescription;
        private String CreateTime;
        private String Step;

        public void setID(String ID) {
            this.ID = ID;
        }

        public void setEID(String EID) {
            this.EID = EID;
        }

        public void setEname(String Ename) {
            this.Ename = Ename;
        }

        public void setErrorCode(String ErrorCode) {
            this.ErrorCode = ErrorCode;
        }

        public void setClientName(String ClientName) {
            this.ClientName = ClientName;
        }

        public void setErrorDescription(String ErrorDescription) {
            this.ErrorDescription = ErrorDescription;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setStep(String Step) {
            this.Step = Step;
        }

        public String getID() {
            return ID;
        }

        public String getEID() {
            return EID;
        }

        public String getEname() {
            return Ename;
        }

        public String getErrorCode() {
            return ErrorCode;
        }

        public String getClientName() {
            return ClientName;
        }

        public String getErrorDescription() {
            return ErrorDescription;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public String getStep() {
            return Step;
        }
    }
}

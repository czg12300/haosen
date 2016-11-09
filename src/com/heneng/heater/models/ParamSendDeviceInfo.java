package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/12/24.
 */
public class ParamSendDeviceInfo extends BaseModel {


    /**
     * EID : 设备条码
     * Ename : 设备名
     * ClientName : 机主姓名
     * ClientPhone : 机主电话
     */

    private String EID;
    private String Ename;
    private String ClientName;
    private String ClientPhone;

    public void setEID(String EID) {
        this.EID = EID;
    }

    public void setEname(String Ename) {
        this.Ename = Ename;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public void setClientPhone(String ClientPhone) {
        this.ClientPhone = ClientPhone;
    }

    public String getEID() {
        return EID;
    }

    public String getEname() {
        return Ename;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getClientPhone() {
        return ClientPhone;
    }
}

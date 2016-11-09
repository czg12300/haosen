package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ParamSendError extends BaseModel {


    /**
     * Eid : 设备条码
     * ErrorCode : 错误码
     * Address : 设备所在地
     * ClientID : 报障客户编号
     * ClientName : 客户名称
     * ClientPhone : 客户电话
     * ErrorDescription : 故障描述
     */

    private String Eid;
    private String ErrorCode;
    private String Address;
    private String ClientID;
    private String ClientName;
    private String ClientPhone;
    private String ErrorDescription;

    public void setEid(String Eid) {
        this.Eid = Eid;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setClientID(String ClientID) {
        this.ClientID = ClientID;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public void setClientPhone(String ClientPhone) {
        this.ClientPhone = ClientPhone;
    }

    public void setErrorDescription(String ErrorDescription) {
        this.ErrorDescription = ErrorDescription;
    }

    public String getEid() {
        return Eid;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public String getAddress() {
        return Address;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getClientPhone() {
        return ClientPhone;
    }

    public String getErrorDescription() {
        return ErrorDescription;
    }
}

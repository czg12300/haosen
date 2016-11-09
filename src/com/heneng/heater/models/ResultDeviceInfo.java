package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/12/24.
 */
public class ResultDeviceInfo extends BaseModel {

    /**
     * ID : 记录ID
     * EID : 设备条码
     * Wsid : 硬件号
     * Ename : 设备名
     * Agent : 代理商名称
     * Position : 设备所在省份名称
     * Position2 : 设备所在城市名称
     *  Position3 : 设备所在县区名称
     * Address : 设备详细地址
     * ClientName : 机主姓名
     * ClientPhone : 机主电话
     * Logined : 0/1
     */

    private String ID;
    private String EID;
    private String Wsid;
    private String Ename;
    private String Agent;
    private String Position;
    private String Position2;
    private String Position3;
    private String Address;
    private String ClientName;
    private String ClientPhone;
    private String Logined;


    public String getEID() {
        return EID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }

    public String getWsid() {
        return Wsid;
    }

    public void setWsid(String wsid) {
        Wsid = wsid;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getAgent() {
        return Agent;
    }

    public void setAgent(String agent) {
        Agent = agent;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getPosition2() {
        return Position2;
    }

    public void setPosition2(String position2) {
        Position2 = position2;
    }

    public String getPosition3() {
        return Position3;
    }

    public void setPosition3(String position3) {
        Position3 = position3;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientPhone() {
        return ClientPhone;
    }

    public void setClientPhone(String clientPhone) {
        ClientPhone = clientPhone;
    }

    public String getLogined() {
        return Logined;
    }

    public void setLogined(String logined) {
        Logined = logined;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

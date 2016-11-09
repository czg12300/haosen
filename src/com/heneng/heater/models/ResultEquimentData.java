package com.heneng.heater.models;

/**
 * Created by Administrator on 2016/1/22.
 */
public class ResultEquimentData extends BaseModel {

    /**
     * ID : 记录ID
     * EID : 设备条码
     * Wsid : 硬件号
     * Ename : 设备名
     * Agent : 代理商名称
     * Position : 设备所在省份名称
     * Position2 : 设备所在城市名称
     * Position3 : 设备所在县区名称
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

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }

    public void setWsid(String Wsid) {
        this.Wsid = Wsid;
    }

    public void setEname(String Ename) {
        this.Ename = Ename;
    }

    public void setAgent(String Agent) {
        this.Agent = Agent;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public void setPosition2(String Position2) {
        this.Position2 = Position2;
    }

    public void setPosition3(String Position3) {
        this.Position3 = Position3;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public void setClientPhone(String ClientPhone) {
        this.ClientPhone = ClientPhone;
    }

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public String getID() {
        return ID;
    }

    public String getEID() {
        return EID;
    }

    public String getWsid() {
        return Wsid;
    }

    public String getEname() {
        return Ename;
    }

    public String getAgent() {
        return Agent;
    }

    public String getPosition() {
        return Position;
    }

    public String getPosition2() {
        return Position2;
    }

    public String getPosition3() {
        return Position3;
    }

    public String getAddress() {
        return Address;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getClientPhone() {
        return ClientPhone;
    }

    public String getLogined() {
        return Logined;
    }
}

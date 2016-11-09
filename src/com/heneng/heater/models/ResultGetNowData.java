package com.heneng.heater.models;

/**
 * Created by huangqland on 15/9/28.
 */
public class ResultGetNowData  extends BaseModel{


    /**
     * ID : 记录ID
     * EID : 设备条码
     * Wsid : 硬件号
     * Ename : 设备名
     * EquiType : 设备类型
     * Onoff : 开关状态(0或1)
     * WeiYuTemp : 卫浴温度
     * CaiNuanTemp : 采暖温度
     * WeiYuSetTemp : 卫浴设定温度
     * CaiNuanSetTemp : 采暖设定温度
     * HeatingTimer : 采暖定时使能设置
     * Flow : 热水流量
     * Fire : 燃气火力
     * DongJi : 冬季模式
     * XiaJi : 夏季模式
     * WeiYu : 卫浴运行
     * CaiNuan : 采暖运行
     * FangDong : 防冻运行
     * DingShi : 定时模式
     * Error : 故障状态
     * Date : 数据时间
     * OnLine : 是否在线(0或1)
     * Logined : 0/1
     */

    private String ID;
    private String EID;
    private String Wsid;
    private String Ename;
    private String EquiType;
    private String Onoff;
    private String WeiYuTemp;
    private String CaiNuanTemp;
    private String WeiYuSetTemp;
    private String CaiNuanSetTemp;
    private String HeatingTimer;
    private String Flow;
    private String Fire;
    private String DongJi;
    private String XiaJi;
    private String WeiYu;
    private String CaiNuan;
    private String FangDong;
    private String DingShi;
    private String Error;
    private String Date;
    private String OnLine;
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

    public void setEquiType(String EquiType) {
        this.EquiType = EquiType;
    }

    public void setOnoff(String Onoff) {
        this.Onoff = Onoff;
    }

    public void setWeiYuTemp(String WeiYuTemp) {
        this.WeiYuTemp = WeiYuTemp;
    }

    public void setCaiNuanTemp(String CaiNuanTemp) {
        this.CaiNuanTemp = CaiNuanTemp;
    }

    public void setWeiYuSetTemp(String WeiYuSetTemp) {
        this.WeiYuSetTemp = WeiYuSetTemp;
    }

    public void setCaiNuanSetTemp(String CaiNuanSetTemp) {
        this.CaiNuanSetTemp = CaiNuanSetTemp;
    }

    public void setHeatingTimer(String HeatingTimer) {
        this.HeatingTimer = HeatingTimer;
    }

    public void setFlow(String Flow) {
        this.Flow = Flow;
    }

    public void setFire(String Fire) {
        this.Fire = Fire;
    }

    public void setDongJi(String DongJi) {
        this.DongJi = DongJi;
    }

    public void setXiaJi(String XiaJi) {
        this.XiaJi = XiaJi;
    }

    public void setWeiYu(String WeiYu) {
        this.WeiYu = WeiYu;
    }

    public void setCaiNuan(String CaiNuan) {
        this.CaiNuan = CaiNuan;
    }

    public void setFangDong(String FangDong) {
        this.FangDong = FangDong;
    }

    public void setDingShi(String DingShi) {
        this.DingShi = DingShi;
    }

    public void setError(String Error) {
        this.Error = Error;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public void setOnLine(String OnLine) {
        this.OnLine = OnLine;
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

    public String getEquiType() {
        return EquiType;
    }

    public String getOnoff() {
        return Onoff;
    }

    public String getWeiYuTemp() {
        return WeiYuTemp;
    }

    public String getCaiNuanTemp() {
        return CaiNuanTemp;
    }

    public String getWeiYuSetTemp() {
        return WeiYuSetTemp;
    }

    public String getCaiNuanSetTemp() {
        return CaiNuanSetTemp;
    }

    public String getHeatingTimer() {
        return HeatingTimer;
    }

    public String getFlow() {
        return Flow;
    }

    public String getFire() {
        return Fire;
    }

    public String getDongJi() {
        return DongJi;
    }

    public String getXiaJi() {
        return XiaJi;
    }

    public String getWeiYu() {
        return WeiYu;
    }

    public String getCaiNuan() {
        return CaiNuan;
    }

    public String getFangDong() {
        return FangDong;
    }

    public String getDingShi() {
        return DingShi;
    }

    public String getError() {
        return Error;
    }

    public String getDate() {
        return Date;
    }

    public String getOnLine() {
        return OnLine;
    }

    public String getLogined() {
        return Logined;
    }
}

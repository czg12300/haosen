package com.heneng.heater.lastcoder.model;

import java.util.Date;

public class NowState {
    public long ID;
    public String EID;
    public String Wsid;
    public String Ename;
    public String EquiType;
    public int Onoff = -1;
    public int WeiYuTemp;
    public int CaiNuanTemp;
    public int WeiYuSetTemp;
    public int CaiNuanSetTemp;
    public String HeatingTimer;
    public Float Flow;
    public int Fire;
    public int DongJi = 0;
    public int XiaJi = 0;
    public int WeiYu = 0;
    public int CaiNuan = 0;
    public int FangDong = 0;
    public int DingShi = 0;
    public int Error = 0;
    public Date Date;
    public int Online = 0;
}

package com.heneng.heater.models;

import java.util.List;

/**
 * Created by Administrator on 2016/1/14.
 */
public class ResultDeviceList extends BaseModel {


    /**
     * Data : [{"ID":"1","EID":"201408121236241234","Wsid":"E201408121236241234","EName":"开关1","EquiType":"两用炉","Onoff":"0","EleType":"1","OnLine":"1","Image":"http://192.168.1.1/1.jpg","ErrorCode":"1212","Interface":"127.0.0.1:9999"}]
     * Logined : 0/1
     */

    private String Logined;
    /**
     * ID : 1
     * EID : 201408121236241234
     * Wsid : E201408121236241234
     * EName : 开关1
     * EquiType : 两用炉
     * Onoff : 0
     * EleType : 1
     * OnLine : 1
     * Image : http://192.168.1.1/1.jpg
     * ErrorCode : 1212
     * Interface : 127.0.0.1:9999
     */

    private List<Device> Data;

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public void setData(List<Device> Data) {
        this.Data = Data;
    }

    public String getLogined() {
        return Logined;
    }

    public List<Device> getData() {
        return Data;
    }

    public static class Device {
        private String ID;
        private String EID;
        private String Wsid;
        private String Ename;
        private String EquiType;
        private String Onoff;
        private String EleType;
        private String OnLine;
        private String Image;
        private String ErrorCode;
        private String Interface;

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

        public void setEleType(String EleType) {
            this.EleType = EleType;
        }

        public void setOnLine(String OnLine) {
            this.OnLine = OnLine;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public void setErrorCode(String ErrorCode) {
            this.ErrorCode = ErrorCode;
        }

        public void setInterface(String Interface) {
            this.Interface = Interface;
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

        public String getEleType() {
            return EleType;
        }

        public String getOnLine() {
            return OnLine;
        }

        public String getImage() {
            return Image;
        }

        public String getErrorCode() {
            return ErrorCode;
        }

        public String getInterface() {
            return Interface;
        }
    }
}

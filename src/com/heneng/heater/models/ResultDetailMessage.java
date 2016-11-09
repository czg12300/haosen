package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/20.
 */
public class ResultDetailMessage extends BaseModel {

    /**
     * ID : 记录ID
     * Title : 标题
     * Image : 图片地址
     * Content : 内容
     * SendTime : 消息时间
     * State : 消息状态
     * Logined : 0/1
     */

    private String ID;
    private String Title;
    private String Image;
    private String Content;
    private String SendTime;
    private String State;
    private String Logined;

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }

    public void setState(String State) {
        this.State = State;
    }

    public void setLogined(String Logined) {
        this.Logined = Logined;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getImage() {
        return Image;
    }

    public String getContent() {
        return Content;
    }

    public String getSendTime() {
        return SendTime;
    }

    public String getState() {
        return State;
    }

    public String getLogined() {
        return Logined;
    }
}

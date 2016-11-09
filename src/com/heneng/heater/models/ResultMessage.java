package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/20.
 */
public class ResultMessage extends BaseModel {


    /**
     * ID : 记录ID
     * Title : 标题
     * SmallImage : 缩略图地址
     * SendTime : 消息时间
     * State : 消息状态
     */

    private String ID;
    private String Title;
    private String SmallImage;
    private String SendTime;
    private String Intro;
    private String State;

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setSmallImage(String SmallImage) {
        this.SmallImage = SmallImage;
    }

    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getSmallImage() {
        return SmallImage;
    }

    public String getSendTime() {
        return SendTime;
    }

    public String getState() {
        return State;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultMessage that = (ResultMessage) o;
        return ID.equals(that.ID);

    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID='" + ID + '\'' +
                ", Title='" + Title + '\'' +
                ", SmallImage='" + SmallImage + '\'' +
                ", SendTime='" + SendTime + '\'' +
                ", State='" + State + '\'' +
                '}';
    }

    public String getIntro() {
        return Intro;
    }

    public void setIntro(String intro) {
        Intro = intro;
    }
}

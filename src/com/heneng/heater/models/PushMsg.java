package com.heneng.heater.models;

/**
 * Created by Administrator on 2016/1/22.
 */
public class PushMsg extends BaseModel {


    public enum MessageType {
        NEWS("news"), SYSTEM("system"), ALARM("alarm");
        public String value;

        MessageType(String value) {
            this.value = value;
        }

        public boolean equals(String value) {
            return value.equals(this.value);
        }
    }

    /**
     * title : 标题
     * image : 图片
     * time : 时间
     * sender : 发送人
     * mtype : 类型
     * content : 消息内容
     */

    private String title;
    private String image;
    private String time;
    private String sender;
    private String mtype;
    private String content;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public String getMtype() {
        return mtype;
    }

    public String getContent() {
        return content;
    }
}

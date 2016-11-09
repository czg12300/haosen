package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ParamUser {


    /**
     * LoginName : 登录名(即手机号码)
     * NewPwd : 新密码
     */

    private String LoginName;
    private String NewPwd;

    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }

    public void setNewPwd(String NewPwd) {
        this.NewPwd = NewPwd;
    }

    public String getLoginName() {
        return LoginName;
    }

    public String getNewPwd() {
        return NewPwd;
    }
}

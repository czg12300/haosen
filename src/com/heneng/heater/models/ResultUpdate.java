package com.heneng.heater.models;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ResultUpdate extends BaseModel {


    /**
     * Name  : 版本名称
     * Version  : 当前版本号
     * VersionDate  : 更新日期
     * ForceUpdate : 强制更新版本号
     * AppPath : App路径
     * Description : 版本更新描述
     */

    private String Name;
    private String Version;
    private String VersionDate;
    private String ForceUpdate;
    private String AppPath;
    private String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getVersionDate() {
        return VersionDate;
    }

    public void setVersionDate(String versionDate) {
        VersionDate = versionDate;
    }

    public String getForceUpdate() {
        return ForceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        ForceUpdate = forceUpdate;
    }

    public String getAppPath() {
        return AppPath;
    }

    public void setAppPath(String appPath) {
        AppPath = appPath;
    }
}

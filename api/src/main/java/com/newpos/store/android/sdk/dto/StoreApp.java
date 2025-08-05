package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @ClassName : App
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/8/1-9:47
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class StoreApp {
    public String appFile;
    public String appHash;
    public long appId;
    public long appDefId;
    public long appSize;
    public long diffSize;
    public long createTime;
    public long downCount;
    public String feeType;
    public String iconFile;
    public String packageName;
    public String progName;
    public String appSimpleDesc;
    public int starValue;
    public String subscribeSignId;
    public int verCode;
    public String verName;
    public String developer;
    public String partnerFullName;
    public String partnerType;
    public List<String> permsAndroid;
    public List<String> permsCustom;
    public List<String> screenShots;

    public String auditNote;
    public String areaDescription;
    public String areaId;

    @NonNull
    @Override
    public String toString() {
        return "App{" +
                "appFile='" + appFile + '\'' +
                ", appHash='" + appHash + '\'' +
                ", appId=" + appId +
                ", appDefId=" + appDefId +
                ", appSize=" + appSize +
                ", diffSize=" + diffSize +
                ", createTime=" + createTime +
                ", downCount=" + downCount +
                ", feeType='" + feeType + '\'' +
                ", iconFile='" + iconFile + '\'' +
                ", packageName='" + packageName + '\'' +
                ", progName='" + progName + '\'' +
                ", appSimpleDesc='" + appSimpleDesc + '\'' +
                ", starValue=" + starValue +
                ", subscribeSignId='" + subscribeSignId + '\'' +
                ", verCode=" + verCode +
                ", verName='" + verName + '\'' +
                ", developer='" + developer + '\'' +
                ", partnerFullName='" + partnerFullName + '\'' +
                ", partnerType='" + partnerType + '\'' +
                ", permsAndroid=" + permsAndroid +
                ", permsCustom=" + permsCustom +
                ", screenShots=" + screenShots +
                ", auditNote='" + auditNote + '\'' +
                ", areaDescription='" + areaDescription + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }
}

package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : ParamDownloadRequesst
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/22-16:06
 * @Version : 1.0
 * @Description :
 * @website : https://www.newpostech.com/
 */
public class ParamDownloadRequest {
    private String packageName;
    private int versionCode;
    private String saveFilePath;
    private String serialNumber;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "ParamDownloadRequest{" +
                "packageName='" + packageName + '\'' +
                ", versionCode=" + versionCode +
                ", saveFilePath='" + saveFilePath + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}

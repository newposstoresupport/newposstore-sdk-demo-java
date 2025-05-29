package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : AppResponse
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/25-16:00
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppResponse {
    public String appId;
    public JsonArray attachFiles;
    public String iconFile;
    public String packageName;
    public String progName;
    public int verCode;
    public String verName;

    @NonNull
    @Override
    public String toString() {
        return "AppResponse{" +
                "appId='" + appId + '\'' +
                ", attachFiles=" + attachFiles +
                ", iconFile='" + iconFile + '\'' +
                ", packageName='" + packageName + '\'' +
                ", progName='" + progName + '\'' +
                ", verCode=" + verCode +
                ", verName='" + verName + '\'' +
                '}';
    }
}

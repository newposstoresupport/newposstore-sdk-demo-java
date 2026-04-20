package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @ClassName : ParamDownV2Result
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/23-17:23
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamDownV2Result {
    public String appId;
    public String packageName;
    public int verCode;
    public String verName;
    public List<AttachFile> attachFiles;
    public List<String> dirs;

    @NonNull
    @Override
    public String toString() {
        return "ParamDownV2Result{" +
                "appId='" + appId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", verCode=" + verCode +
                ", verName='" + verName + '\'' +
                ", attachFiles='" + attachFiles + '\'' +
                ", dirs='" + dirs + '\'' +
                '}';
    }
}

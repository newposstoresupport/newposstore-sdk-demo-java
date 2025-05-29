package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * @ClassName : ParamDownloadResponse
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/15-16:44
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamDownloadResponse {
    public List<AttachFile> attachFiles;
    public String appId;
    public String packageName;
    public int verCode;
    public String verName;

    @NonNull
    @Override
    public String toString() {
        return "ParamDownloadResponse{" +
                "attachFiles=" + attachFiles +
                ", appId='" + appId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", verCode=" + verCode +
                ", verName='" + verName + '\'' +
                '}';
    }
}

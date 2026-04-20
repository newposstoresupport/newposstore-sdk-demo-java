package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @ClassName : ParamDownQueryV2Data
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/23-17:10
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamDownQueryV2Data {
    public String appId;

    /**
     * AttachFile
     */
    public JsonArray attachFiles;

    public JsonObject param;

    public JsonObject wrapped;

    @NonNull
    @Override
    public String toString() {
        return "ParamDownQueryV2Data{" +
                "appId='" + appId + '\'' +
                ", attachFiles=" + attachFiles +
                ", param=" + param +
                ", wrapped=" + wrapped +
                '}';
    }
}

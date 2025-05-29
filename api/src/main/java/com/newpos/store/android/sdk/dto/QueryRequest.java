package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : QueryRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/18-15:56
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class QueryRequest {
    public JsonArray applications;

    @NonNull
    @Override
    public String toString() {
        return "QueryRequest{" +
                "applications=" + applications +
                '}';
    }
}

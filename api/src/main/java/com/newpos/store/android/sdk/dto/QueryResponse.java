package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : QueryResponse
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/25-15:57
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class QueryResponse extends BaseResponse {
    public JsonArray data;
    public int total;

    @NonNull
    @Override
    public String toString() {
        return "QueryResponse{" +
                "code=" + code +
                ", msg=" + msg +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}

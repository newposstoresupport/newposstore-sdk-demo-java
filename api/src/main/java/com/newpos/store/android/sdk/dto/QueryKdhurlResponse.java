package com.newpos.store.android.sdk.dto;

import com.google.gson.JsonObject;

public class QueryKdhurlResponse extends BaseResponse {
    public JsonObject data;

    @Override
    public String toString() {
        return "QueryKdhurlResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

}

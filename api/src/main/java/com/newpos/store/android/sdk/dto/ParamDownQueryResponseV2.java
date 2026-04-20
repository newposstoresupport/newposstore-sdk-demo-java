package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : ParamDownQueryResponseV2
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/23-17:09
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamDownQueryResponseV2 extends BaseResponse {
    /**
     * ParamDownQueryV2Data
     */
    public JsonArray data;

    public int total;

    @NonNull
    @Override
    public String toString() {
        return "ParamDownQueryResponseV2{" +
                "data=" + data +
                "total=" + total +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

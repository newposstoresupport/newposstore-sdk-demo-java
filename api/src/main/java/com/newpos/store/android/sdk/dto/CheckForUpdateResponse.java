package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : CheckForUpdateResponse
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-16:49
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CheckForUpdateResponse extends BaseResponse {
    public JsonArray data;
    public int total;
    public String cursor;

    @NonNull
    @Override
    public String toString() {
        return "CheckForUpdateResponse{" +
                "data=" + data +
                ", total=" + total +
                ", cursor='" + cursor + '\'' +
                '}';
    }
}

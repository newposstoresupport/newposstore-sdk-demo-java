package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : CheckForUpdateRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-16:47
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CheckForUpdateRequest {
    public JsonArray applications;

    @NonNull
    @Override
    public String toString() {
        return "CheckForUpdateRequest{" +
                "applications=" + applications +
                '}';
    }
}

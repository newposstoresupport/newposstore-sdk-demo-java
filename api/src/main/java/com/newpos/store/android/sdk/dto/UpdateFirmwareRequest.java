package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;

/**
 * @ClassName : UpdateFirmwareRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/6/29-15:07
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class UpdateFirmwareRequest {
    private JsonArray progress;

    @NonNull
    @Override
    public String toString() {
        return "UpdateFirmwareRequest{" +
                "progress=" + progress +
                '}';
    }
}

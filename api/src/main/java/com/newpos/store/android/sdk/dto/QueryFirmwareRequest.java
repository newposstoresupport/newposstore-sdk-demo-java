package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : QueryFrimwareRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/6/29-15:01
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class QueryFirmwareRequest {
    public Firmware firmware;

    @NonNull
    @Override
    public String toString() {
        return "QueryFirmwareRequest{" +
                "firmware=" + firmware +
                '}';
    }
}

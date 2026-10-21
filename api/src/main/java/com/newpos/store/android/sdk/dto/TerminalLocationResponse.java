package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * 终端谷歌/百度定位结果。与商店 {@code getTerminalLocation} 成功 JSON 对齐。
 */
public class TerminalLocationResponse extends BaseResponse {
    public String provider;
    public String lat;
    public String lon;
    public String address;
    public Long time;

    @NonNull
    @Override
    public String toString() {
        return "TerminalLocationResponse{" +
                "provider='" + provider + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", address='" + address + '\'' +
                ", time=" + time +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

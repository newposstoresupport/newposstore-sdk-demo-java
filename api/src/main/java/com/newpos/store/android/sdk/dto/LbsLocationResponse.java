package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : LbsLocationResponse
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/15-16:45
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class LbsLocationResponse extends BaseResponse {
    public String address;
    public String lon;
    public String lat;
    public String radius;

    @NonNull
    @Override
    public String toString() {
        return "LbsLocationResponse{" +
                "address='" + address + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", radius='" + radius + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

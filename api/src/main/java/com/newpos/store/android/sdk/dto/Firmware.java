package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : Firmware
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/6/29-15:02
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class Firmware {
    public String version;
    public String custom;
    public String firmwareId;

    @NonNull
    @Override
    public String toString() {
        return "Firmware{" +
                "version='" + version + '\'' +
                ", custom='" + custom + '\'' +
                ", firmwareId='" + firmwareId + '\'' +
                '}';
    }
}

package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : FirmwareInfo
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/6/29-15:03
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class FirmwareInfo {
    public String debug;
    public Long fullSize;
    public Long diffSize;
    public String hash;
    public String releaseDate;
    public String releaseNote;
    public String url;
    public String version;

    @NonNull
    @Override
    public String toString() {
        return "FirmwareInfo{" +
                "debug='" + debug + '\'' +
                ", fullSize=" + fullSize +
                ", diffSize=" + diffSize +
                ", hash='" + hash + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseNote='" + releaseNote + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

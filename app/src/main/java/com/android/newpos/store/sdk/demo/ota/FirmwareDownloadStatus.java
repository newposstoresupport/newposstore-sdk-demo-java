package com.android.newpos.store.sdk.demo.ota;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.app.DownloadStatus;
import com.newpos.store.android.sdk.dto.FirmwareInfo;
import com.newpos.store.android.sdk.dto.StoreApp;

/**
 * @ClassName : FirmwareDownloadStatus
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/8/5-16:55
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class FirmwareDownloadStatus {
    public FirmwareInfo firmwareInfo;
    public String percent;
    public String speed;
    public int sofar;
    public int total;
    public DownloadStatus downloadStatus;

    @NonNull
    @Override
    public String toString() {
        return "FirmwareDownloadStatus{" +
                "firmwareInfo=" + firmwareInfo +
                ", percent='" + percent + '\'' +
                ", speed='" + speed + '\'' +
                ", sofar='" + sofar + '\'' +
                ", total='" + total + '\'' +
                ", downloadStatus=" + downloadStatus +
                '}';
    }
}

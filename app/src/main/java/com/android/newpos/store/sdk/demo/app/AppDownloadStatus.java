package com.android.newpos.store.sdk.demo.app;

import androidx.annotation.NonNull;

import com.newpos.store.android.sdk.dto.StoreApp;

/**
 * @ClassName : AppDownloadStatus
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/8/5-16:55
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppDownloadStatus {
    public StoreApp storeApp;
    public String pack;
    public String percent;
    public DownloadStatus downloadStatus;

    @NonNull
    @Override
    public String toString() {
        return "AppDownloadStatus{" +
                "pack='" + pack + '\'' +
                ", percent='" + percent + '\'' +
                ", downloadStatus=" + downloadStatus +
                '}';
    }
}

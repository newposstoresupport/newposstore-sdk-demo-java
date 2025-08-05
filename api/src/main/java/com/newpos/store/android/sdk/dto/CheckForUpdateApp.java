package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : CheckForUpdateApp
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/8/1-9:39
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CheckForUpdateApp {
    public String packageName;
    public String verName;
    public int verCode;

    @NonNull
    @Override
    public String toString() {
        return "CheckForUpdateRequest{" +
                "packageName='" + packageName + '\'' +
                ", verName='" + verName + '\'' +
                ", verCode=" + verCode +
                '}';
    }
}

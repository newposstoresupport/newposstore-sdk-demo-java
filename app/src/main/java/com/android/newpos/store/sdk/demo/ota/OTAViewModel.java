package com.android.newpos.store.sdk.demo.ota;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;

/**
 * @ClassName : OTAViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:39
 * @Version : 1.0
 * @Description :
 * @website : https://www.newpostech.com/
 */
public class OTAViewModel extends BaseViewModel {
    public OTAViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getTitle() {
        return "OTA Upgrade";
    }
}

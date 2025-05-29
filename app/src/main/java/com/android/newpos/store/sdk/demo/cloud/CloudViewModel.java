package com.android.newpos.store.sdk.demo.cloud;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;

/**
 * @ClassName : CloudViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/29-16:09
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CloudViewModel extends BaseViewModel {

    public CloudViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getTitle() {
        return "Cloud Message";
    }
}
package com.android.newpos.store.sdk.demo.common;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;

/**
 * @ClassName : CommonViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-17:04
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class CommonViewModel extends BaseViewModel {
    public CommonViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getTitle() {
        return "Common Functions";
    }
}

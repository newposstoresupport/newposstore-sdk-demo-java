package com.android.newpos.store.sdk.demo.rki;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;

/**
 * @ClassName : RkiViewMoel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/29-10:34
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class RkiViewModel extends BaseViewModel {
    public RkiViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getTitle() {
        return "RKI";
    }
}

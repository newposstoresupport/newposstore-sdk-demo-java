package com.android.newpos.store.sdk.demo.register;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;

/**
 * @ClassName : RegisterViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/26-14:08
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class RegisterViewModel extends BaseViewModel {
    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getTitle() {
        return "Registration";
    }
}

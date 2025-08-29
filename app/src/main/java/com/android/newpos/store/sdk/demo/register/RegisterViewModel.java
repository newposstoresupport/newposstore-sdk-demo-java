package com.android.newpos.store.sdk.demo.register;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.MainApplication;
import com.android.newpos.store.sdk.demo.app.InitCallback;
import com.android.newpos.store.sdk.demo.app.LoadingDialogManage;
import com.android.newpos.store.sdk.demo.app.LoadingOption;
import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

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


    public void register(){
        //同步写异步
        showLoading(new LoadingOption("Initializing..."));
        addSubscribe(Observable.just(true)
                .observeOn(Schedulers.io())
                .subscribe(b -> {
                    MainApplication.getInstance().initStoreSdk(AppUtils.getClientId(), this::dismissLoading);
                }, this::showError)
        );
    }
}

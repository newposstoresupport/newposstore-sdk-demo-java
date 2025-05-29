package com.android.newpos.store.sdk.demo.inquirer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.SPreference;

/**
 * @ClassName : AppInquirerViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/25-14:52
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppInquirerViewModel extends BaseViewModel {

    public static final String key = "key_ready";

    private final MutableLiveData<Boolean> status;
    private final MutableLiveData<Boolean> appStatus;

    public AppInquirerViewModel(@NonNull Application application) {
        super(application);
        status = new MutableLiveData<>();
        appStatus = new MutableLiveData<>();
    }

    @Override
    public String getTitle() {
        return "App Inquirer";
    }

    public MutableLiveData<Boolean> getStatus(){
        return status;
    }

    public MutableLiveData<Boolean> getAppStatus(){
        return appStatus;
    }

    public void queryStatus() {
        status.postValue(BaseApi.getInstance().isRegisterInquirer());
    }

    public boolean isReadyToUpdate(){
        //TODO Here you can customize your own logic, when the application is idle and busy
        return SPreference.I().getBoolean(key);
    }

    public void updateAppStatus(boolean ready){
        BaseLog.d("updateAppStatus:"+ready);
        SPreference.I().putBoolean(key, ready);
        appStatus.postValue(ready);
    }
}

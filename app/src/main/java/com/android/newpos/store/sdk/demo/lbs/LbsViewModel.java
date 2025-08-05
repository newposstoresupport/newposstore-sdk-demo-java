package com.android.newpos.store.sdk.demo.lbs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName : LbsViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-12:40
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class LbsViewModel extends BaseViewModel {

    public final MutableLiveData<String> mLocation;

    public LbsViewModel(@NonNull Application application) {
        super(application);
        mLocation = new MutableLiveData<>();
    }

    public void getLocation(){
        mLocation.postValue("Retrieving location information...");
        LbsLocationRequest lbsLocationRequest = new LbsLocationRequest();
        lbsLocationRequest.setOutput("json");
        lbsLocationRequest.setMnc("0");
        lbsLocationRequest.setCi("46407687");
        lbsLocationRequest.setAppid("OTA_LBS");
        lbsLocationRequest.setMcc("460");
        lbsLocationRequest.setLac("9763");
        lbsLocationRequest.setRadio("LTE");

        addSubscribe(Observable.just(lbsLocationRequest)
                .observeOn(Schedulers.io())
                .map(request -> StoreSdk.getInstance().lbsAbility().getLocation(request, false))

                .subscribe(response -> {
                    if(response == null){
                        mLocation.postValue("get location failed, please check log!");
                        return;
                    }

                    mLocation.postValue(BaseUtils.toJson(response));
                }, this::showError)
        );
    }

    @Override
    protected void showError(Throwable throwable) {
        super.showError(throwable);
        mLocation.postValue("");
    }

    @Override
    public String getTitle() {
        return "LBS Location";
    }
}
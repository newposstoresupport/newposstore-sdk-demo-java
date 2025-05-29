package com.android.newpos.store.sdk.demo.lbs;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;

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
    public LbsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getLocation(){
        LbsLocationRequest lbsLocationRequest = new LbsLocationRequest();
        lbsLocationRequest.setOutput("json");
        lbsLocationRequest.setMnc("0");
        lbsLocationRequest.setCi("46407687");
        lbsLocationRequest.setAppid("OTA_LBS");
        lbsLocationRequest.setMcc("460");
        lbsLocationRequest.setLac("9763");
        lbsLocationRequest.setRadio("LTE");
//        getService().execute(()-> StoreSdk.getInstance().lbsAbility()
//                .requestLocation(lbsLocationRequest, false));
    }

    @Override
    public String getTitle() {
        return "LBS Location";
    }
}
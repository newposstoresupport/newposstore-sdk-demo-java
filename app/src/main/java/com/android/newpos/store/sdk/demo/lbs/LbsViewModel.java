package com.android.newpos.store.sdk.demo.lbs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.app.LoadingOption;
import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LbsViewModel extends BaseViewModel {


    public final MutableLiveData<String> mLocation;
    public final MutableLiveData<String> logs;

    public LbsViewModel(@NonNull Application application) {
        super(application);
        mLocation = new MutableLiveData<>();
        logs = new MutableLiveData<>();
    }

    public void getLocation(){
        mLocation.postValue("Retrieving location information...");
        appendLog("Retrieving location information...");
        /*{
            "ci": "46407687",
            "lac": "9763",
            "mcc": "460",
            "mnc": "0",
            "output": "json",
            "radio": "LTE",
            "terminal": {
                "lang": "en-US",
                "model": "NEW9830",
                "sn": "H3R000300000112",
                "vendor": "NEWPOS",
                "cid": 8888,
                "os": 0,
                "debug": "1",
                "sdkVer": 33
            }
        }*/
        LbsLocationRequest lbsLocationRequest = new LbsLocationRequest();
        lbsLocationRequest.setOutput("json");
        lbsLocationRequest.setMnc("0");
        lbsLocationRequest.setCi("46407687");
        lbsLocationRequest.setAppid("270203b4858e70879a69fedaba23da6c");
        lbsLocationRequest.setMcc("460");
        lbsLocationRequest.setLac("9763");
        lbsLocationRequest.setRadio("LTE");

        showLoading(new LoadingOption("Retrieving location information..."));
        addSubscribe(Observable.just(lbsLocationRequest)
                .observeOn(Schedulers.io())
                .map(request -> StoreSdk.getInstance().lbsAbility().getLocation(request, false))

                .subscribe(response -> {
                    dismissLoading();

                    if (response == null) {
                        String failMsg = "Get location failed, please check log!";
                        mLocation.postValue(failMsg);
                        appendLog(failMsg);
                        return;
                    }
                    String json = BaseUtils.toJson(response);
                    mLocation.postValue(json);
                    appendLog("Locating successfully: " + json);

                }, throwable -> {
                    dismissLoading();
                    showError(throwable);
                })
        );
    }

    public void appendLog(String msg){
        logs.postValue(msg);
    }

    @Override
    protected void showError(Throwable throwable) {
        super.showError(throwable);
        mLocation.postValue("");
        appendLog("LBS Error: " + throwable.getMessage());
    }

    @Override
    public String getTitle() {
        return "LBS Location";
    }
}
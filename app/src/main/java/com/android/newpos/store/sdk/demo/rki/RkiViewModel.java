package com.android.newpos.store.sdk.demo.rki;


import static com.android.newpos.store.sdk.demo.base.AppUtils.showToast;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.app.LoadingOption;
import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.QueryKdhurlRequest;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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
    public final MutableLiveData<String> mKdh;


    public RkiViewModel(@NonNull Application application) {
        super(application);
        mKdh = new MutableLiveData<>();
    }

    public void bind(){
        showLoading(new LoadingOption("Binding RKI Service..."));
        addSubscribe(Observable.just(true)
                .observeOn(Schedulers.io())
                .subscribe(b -> {
                    boolean bind = StoreSdk.getInstance().rkiAbility().bindRkiService();
                    if(!bind){
                        showToast("RKI Service is not available, please check if the RKI app is installed.");
                    }else{
                        showToast("RKI Service is bind successfully.");
                    }
                    dismissLoading();
                }, e -> {
                    dismissLoading();
                    showError(e);
                }));
    }

    public void download() {
        showLoading(new LoadingOption("Retrieving KDH information..."));
        mKdh.postValue("Retrieving KDH information...");
        QueryKdhurlRequest kdhRequest = new QueryKdhurlRequest();
        // 异步执行
        addSubscribe(Observable.just(kdhRequest)
                .observeOn(Schedulers.io())
                .map(req -> StoreSdk.getInstance().rkiAbility().getKdhUrl(req))
                .subscribe(response -> {
                    dismissLoading();
                    Log.d("RkiViewModel", "Download >>> KDH Response: " + BaseUtils.toJson(response));
                    if (response == null) {
                        mKdh.postValue("get KDH failed, please check response!");
                        return;
                    }
                    JsonObject jsonObject = response.data;
                    if(jsonObject == null){
                        Log.d("RkiViewModel", "Download >>> KDH Response.data: " + jsonObject);
                        mKdh.postValue("get KDH failed, please check response data!");
                        return;
                    }
                    if(!jsonObject.has("kdhUrl")){
                        mKdh.postValue("get KDH failed, please check response data, kdhUrl not found!");
                        return;
                    }
                    JsonElement element = jsonObject.get("kdhUrl");
                    String kdhUrl = element.getAsString();
                    Log.d("KdhUrl", "KDH URL: " + kdhUrl);
                    showToast(StoreSdk.getInstance().rkiAbility().downloadCustomerKeys(
                            AppUtils.getClientId(),
                            kdhUrl,
                            "",
                            (code, message, keyList) -> {
                                Log.e("IRkiCallback", "onDownload:" + code + "," + message + "," + keyList);
                                mKdh.postValue("Download result: " +  code + "," + message + "," + keyList);
                            }).getMessage());
                }, e -> {
                    dismissLoading();
                    showError(e);
                })
        );
    }

    @Override
    public String getTitle() {
        return "RKI";
    }
}

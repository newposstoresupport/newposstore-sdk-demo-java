package com.android.newpos.store.sdk.demo.param;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.ability.ParamAbility;
import com.newpos.store.android.sdk.dto.AppResponse;
import com.newpos.store.android.sdk.dto.ParamDownloadRequest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName : ParamViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-09:40
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamViewModel extends BaseViewModel {

    private final MutableLiveData<String> mInfo;

    public ParamViewModel(@NonNull Application application) {
        super(application);
        this.mInfo = new MutableLiveData<>();
    }

    @Override
    public String getTitle() {
        return "Download Parameters";
    }

    public MutableLiveData<String> getInfo(){
        return mInfo;
    }

    private List<AppResponse> appResponseList= null;

    public void queryParamFile(){
        addSubscribe(Observable.just(true)
                .observeOn(Schedulers.io())
                .subscribe(n -> {
                    appResponseList = StoreSdk.getInstance().paramAbility().queryParamsList();
                    if(appResponseList.isEmpty()){
                        mInfo.postValue("There is no parameter file under the application. Please go to the cloud platform to upload it.");
                    }else {
                        StringBuilder builder = new StringBuilder();
                        for (AppResponse appResponse : appResponseList){
                            builder.append(appResponse.toString()).append("\n");
                        }
                        mInfo.postValue(builder.toString());
                    }
                }, this::showError)
        );
    }

    public void downloadParamFile(){
        addSubscribe(Observable.just(true)
                .observeOn(Schedulers.io())
                .subscribe(n -> {
                    if(appResponseList == null || appResponseList.isEmpty()){
                        mInfo.postValue("There is no parameter list, please query first!");
                        return;
                    }
                    ParamAbility paramAbility = StoreSdk.getInstance().paramAbility();
                    ParamDownloadRequest paramDownloadRequest = new ParamDownloadRequest();
                    paramDownloadRequest.setPackageName(getApplication().getPackageName());
                    paramDownloadRequest.setSaveFilePath(getApplication().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                    paramDownloadRequest.setVersionCode(1);
                    paramDownloadRequest.setSerialNumber(paramAbility.getSerialNumber());
                    paramAbility.downloadParamToPath(paramDownloadRequest, appResponseList.get(0));
                }, this::showError)
        );
    }
}
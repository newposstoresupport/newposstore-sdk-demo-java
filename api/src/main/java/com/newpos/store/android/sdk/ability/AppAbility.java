package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.StoreApp;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.CheckForUpdateApp;
import com.newpos.store.android.sdk.dto.CheckForUpdateRequest;
import com.newpos.store.android.sdk.dto.CheckForUpdateResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @ClassName : AppAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-16:45
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppAbility extends BaseAbility {
    public AppAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
    }

    /**
     * Get the application by the specified package name
     * @return List<StoreApp>
     */
    public List<StoreApp> getStoreApps(List<String> packageNameList) throws BaseException {
        if(packageNameList == null || packageNameList.isEmpty()){
            return null;
        }

        List<CheckForUpdateApp> apps = new ArrayList<>();
        for (String packageName : packageNameList){
            CheckForUpdateApp app = new CheckForUpdateApp();
            app.packageName = packageName;
            apps.add(app);
        }

        return getAppFromServer(apps);
    }

    /**
     * Check for new versions
     * @param packageName Target application package name
     * @return StoreApp
     * @throws BaseException
     */
    public StoreApp checkForUpdate(String packageName) throws BaseException {
        if(TextUtils.isEmpty(packageName)){
            return null;
        }

        Context context = BaseApi.getInstance().getContext();
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        CheckForUpdateApp app = new CheckForUpdateApp();
        app.packageName = packageName;
        app.verCode = packageInfo.versionCode;
        app.verName = packageInfo.versionName;
        List<CheckForUpdateApp> list = new ArrayList<>();
        list.add(app);

        List<StoreApp> appFromServer = getAppFromServer(list);
        if(appFromServer == null  || appFromServer.isEmpty()){
            return null;
        }

        return appFromServer.get(0);
    }

    private List<StoreApp> getAppFromServer(List<CheckForUpdateApp> list) throws BaseException {
        CheckForUpdateRequest request = new CheckForUpdateRequest();
        request.applications = BaseUtils.buildJsonArray(list);
        String response = BaseApi.getInstance().checkAppUpdate(request);

        if(TextUtils.isEmpty(response)){
            return null;
        }

        CheckForUpdateResponse checkForUpdateResponse = BaseUtils.toObject(response, CheckForUpdateResponse.class);
        if(checkForUpdateResponse == null){
            return null;
        }

        if(!Objects.equals(checkForUpdateResponse.code, API_SUCCESS)){
            throw new BaseException(checkForUpdateResponse.msg+"["+checkForUpdateResponse.code+"]");
        }
        if(checkForUpdateResponse.data == null){
            return null;
        }

        return BaseUtils.toObject(checkForUpdateResponse.data, StoreApp.class);
    }

    public void installUpdate(){

    }
}

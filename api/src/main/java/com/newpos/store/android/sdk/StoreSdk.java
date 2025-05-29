package com.newpos.store.android.sdk;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;

import com.newpos.store.android.sdk.ability.CloudMessageAbility;
import com.newpos.store.android.sdk.ability.GoInsightAbility;
import com.newpos.store.android.sdk.ability.LbsAbility;
import com.newpos.store.android.sdk.ability.OtaAbility;
import com.newpos.store.android.sdk.ability.ParamAbility;
import com.newpos.store.android.sdk.ability.RkiAbility;
import com.newpos.store.android.sdk.ability.UpgradeAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.AuthenticationInfo;
import com.newpos.store.android.sdk.dto.AuthenticationRequest;
import com.newpos.store.android.sdk.dto.EAbility;
import com.newpos.store.android.sdk.listener.IApiCallback;
import com.newpos.store.android.sdk.listener.IStoreCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : StoreSdk
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-8:35
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class StoreSdk {
    private static final String TAG = StoreSdk.class.getSimpleName();
    public static boolean isDebug = true;
    private static StoreSdk store;
    private final Semaphore semaphore;
    private AppElements appElements;
    private CloudMessageAbility cloudMessageAbility;
    private GoInsightAbility goInsightAbility;
    private LbsAbility lbsAbility;
    private ParamAbility paramAbility;
    private UpgradeAbility upgradeAbility;
    private OtaAbility otaAbility;
    private RkiAbility rkiAbility;

    public static StoreSdk getInstance(){
        if(null == store){
            synchronized (StoreSdk.class){
                if(null == store){
                    store = new StoreSdk();
                }
            }
        }
        return store;
    }

    private StoreSdk(){
        semaphore = new Semaphore(2);
    }

    public String getVersion(){
        //return "1.0.0";//初版
        return "1.0.1";//增加兼容动态接口
    }

    /**
     * Set whether the SDK outputs logs
     * @param d true output, otherwise no output
     */
    public void setDebug(boolean d){
        isDebug = d;
    }

    /**
     * Initialize SDK
     * @param context {@link Context}
     * @param elements Three elements of an application {@link AppElements}
     * @param callback Initialization callback function {@link IStoreCallback}
     */
    public void init(Context context, AppElements elements, IStoreCallback callback){
        init(context, elements, null, callback);
    }


    /**
     * Initialize SDK
     * @param context {@link Context}
     * @param elements Three elements of an application {@link AppElements}
     * @param request Initialize request parameters {@link AuthenticationRequest}
     * @param callback Initialization callback function {@link IStoreCallback}
     */
    public void init(Context context, AppElements elements, AuthenticationRequest request, IStoreCallback callback){
        if(semaphore.availablePermits() != 1){
            checkAppElements(elements);
            this.appElements = elements;
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                BaseLog.d("e:"+e);
            }

            BaseApi.getInstance().init(context, elements, request, new IApiCallback() {
                @Override
                public void initSuccess(AuthenticationInfo ai) {
                    initAbilities(ai);
                    callback.initSuccess();
                    semaphore.release(1);
                }

                @Override
                public void initFailed(RemoteException remoteException) {
                    callback.initFailed(remoteException);
                    semaphore.release(1);
                }
            });
        }
    }

    public AppElements getAppElements(){
        return appElements;
    }

    /**
     * Initialize the application query
     * When your app is about to be upgraded and installed, you can receive a callback
     * You can tell the Store Core whether can be upgraded
     * @param context {@link Context}
     * @param inquirer Application query callback {@link IAppInquirer}
     */
    public void initAppInquirer(Context context, IAppInquirer inquirer){
        BaseApi.getInstance().registerInquirer(context, inquirer);
    }

    /**
     * Cloud Messaging Capability Center
     * @return {@link CloudMessageAbility}
     */
    public CloudMessageAbility cloudMessageAbility(){
        if (cloudMessageAbility == null) {
            acquireSemaphore();
            if (cloudMessageAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return cloudMessageAbility;
    }

    /**
     * Go Insight Capability Center
     * @return {@link GoInsightAbility}
     */
    public GoInsightAbility goInsightAbility(){
        if (goInsightAbility == null) {
            acquireSemaphore();
            if (goInsightAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return goInsightAbility;
    }

    /**
     * Remote Positioning Capability Center
     * @return {@link LbsAbility}
     */
    public LbsAbility lbsAbility(){
        if (lbsAbility == null) {
            acquireSemaphore();
            if (lbsAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return lbsAbility;
    }

    /**
     * Parameters Management Capability Center
     * @return {@link ParamAbility}
     */
    public ParamAbility paramAbility(){
        if (paramAbility == null) {
            acquireSemaphore();
            if (paramAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return paramAbility;
    }

    /**
     * Upgrade Capability Center
     * @return {@link UpgradeAbility}
     */
    public UpgradeAbility upgradeAbility(){
        if (upgradeAbility == null) {
            acquireSemaphore();
            if (upgradeAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return upgradeAbility;
    }

    /**
     * OTA Management Capability Center
     * @return {@link OtaAbility}
     */
    public OtaAbility otaAbility(){
        if (otaAbility == null) {
            acquireSemaphore();
            if (otaAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return otaAbility;
    }

    /**
     * Remote Key Injection Management Competence Center
     * @return {@link RkiAbility}
     */
    public RkiAbility rkiAbility(){
        if (rkiAbility == null) {
            acquireSemaphore();
            if (rkiAbility == null) {
                throw new IllegalStateException("Not initialized");
            }
        }
        return rkiAbility;
    }

    /**
     * Open the details of an application in the App Market
     */
    public void openAppDetail(){

    }

    /**
     * Open the App Market download list
     */
    public void openDownloadList(){

    }

    /**
     * Open the OTA update page
     */
    public void openOtaUpdate(){

    }

    /**
     * Initialize the capabilities of the application
     * @param ai {@link AuthenticationInfo}
     */
    private void initAbilities(AuthenticationInfo ai){
        Map<EAbility, List<String>> map = checkAbilities(ai);
        if(map.containsKey(EAbility.Lbs)){
            lbsAbility = new LbsAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.Lbs));
        }
        if(map.containsKey(EAbility.Cloud_Message)){
            cloudMessageAbility = new CloudMessageAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.Cloud_Message));
        }
        if(map.containsKey(EAbility.Go_Insight)){
            goInsightAbility = new GoInsightAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.Go_Insight));
        }
        if(map.containsKey(EAbility.Param_Download)){
            paramAbility = new ParamAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.Param_Download));
        }
        if(map.containsKey(EAbility.Upgrade_)){
            upgradeAbility = new UpgradeAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.Upgrade_));
        }
        if(map.containsKey(EAbility.OTA)){
            otaAbility = new OtaAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.OTA));
        }
        if(map.containsKey(EAbility.RKI)){
            rkiAbility = new RkiAbility(ai.getBaseUrl(), appElements, ai.getSerialNo(), map.get(EAbility.RKI));
        }
    }

    /**
     * Check the three elements of application
     * @param elements {@link AppElements}
     */
    private void checkAppElements(AppElements elements){
        if(elements == null){
            throw new IllegalArgumentException("elements is null");
        }
        if(TextUtils.isEmpty(elements.getAppId())){
            throw new IllegalArgumentException("AppId is null");
        }
        if(TextUtils.isEmpty(elements.getAppKey())){
            throw new IllegalArgumentException("AppKey is null");
        }
        if(TextUtils.isEmpty(elements.getAppSecret())){
            throw new IllegalArgumentException("AppSecret is null");
        }
    }

    /**
     * acquire semaphore
     */
    private void acquireSemaphore() {
        try {
            BaseLog.d("acquireSemaphore api try acquire 2");
            Long startTime = System.currentTimeMillis();
            semaphore.tryAcquire(2, 5, TimeUnit.SECONDS);
            BaseLog.d("tryAcquire cost Time:" + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (semaphore.availablePermits() == 0) {
            semaphore.release(2);
            BaseLog.d("acquireSemaphore api release acquire 2");
        }
    }

    /**
     * Check application capabilities
     * @param ai {@link AuthenticationInfo}
     * @return map Available capabilities
     */
    private Map<EAbility, List<String>> checkAbilities(AuthenticationInfo ai){
        Map<EAbility, List<String>> map = new HashMap();
        EAbility[] abilities = EAbility.values();
        for (EAbility ability : abilities){
            List<String> list = new ArrayList<>();
            String start = ability.getVal();
            String[] apis = ai.getApiPaths();
            if(apis == null || apis.length == 0){
                continue;
            }
            for (String str : apis){
                if(str.toLowerCase().startsWith(start)){
                    list.add(str);
                }
            }
            map.put(ability, list);
        }
        return map;
    }
}

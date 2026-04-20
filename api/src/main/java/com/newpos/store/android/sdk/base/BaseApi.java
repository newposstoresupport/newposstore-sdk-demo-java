package com.newpos.store.android.sdk.base;

import static com.newpos.store.android.sdk.Constant.ERROR_MARKET_AUTHENTICATION;
import static com.newpos.store.android.sdk.Constant.ERROR_MARKET_NOT_INSTALLED;
import static com.newpos.store.android.sdk.base.UrlConstant.LBS_CELL_QUERY;
import static com.newpos.store.android.sdk.base.UrlConstant.LBS_WIFI_QUERY;
import static com.newpos.store.android.sdk.base.UrlConstant.PARAM_DOWN_QUERY;
import static com.newpos.store.android.sdk.base.UrlConstant.PARAM_DOWN_VERIFY;
import static com.newpos.store.android.sdk.base.UrlConstant.PARAM_TASK_QUERY;
import static com.newpos.store.android.sdk.base.UrlConstant.QUERY_APP_CONFIG;
import static com.newpos.store.android.sdk.base.UrlConstant.QUERY_APP_UPDATE;
import static com.newpos.store.android.sdk.base.UrlConstant.QUERY_KDH_URL;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.Toast;

import com.newpos.store.android.sdk.AidlConstant;
import com.newpos.store.android.sdk.IAppInquirer;
import com.newpos.store.android.sdk.IStoreClient;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.AppQuery;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.AuthenticationInfo;
import com.newpos.store.android.sdk.dto.AuthenticationRequest;
import com.newpos.store.android.sdk.dto.CheckForUpdateRequest;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;
import com.newpos.store.android.sdk.dto.ParamDownQueryRequestV2;
import com.newpos.store.android.sdk.dto.ParamTask;
import com.newpos.store.android.sdk.dto.ParamVerify;
import com.newpos.store.android.sdk.dto.ParamVerifyRequestV2;
import com.newpos.store.android.sdk.dto.PatchType;
import com.newpos.store.android.sdk.dto.QueryKdhurlRequest;
import com.newpos.store.android.sdk.dto.QueryRequest;
import com.newpos.store.android.sdk.listener.IApiCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName : BaseApi
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-9:31
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class BaseApi {
    private static final String TAG = "BaseApi";
    private Context mContext;
    private IStoreClient storeClient;

    private volatile boolean registerInquirer = false;

    private ServiceConnection serviceConnection;
    private IBinder.DeathRecipient deathRecipient;
    private IBinder remoteBinder;

    private static final class BaseApiHolder {
        static final BaseApi baseApi = new BaseApi();
    }

    public static BaseApi getInstance(){
        return BaseApiHolder.baseApi;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isRegisterInquirer(){
        return registerInquirer;
    }

    public void init(Context c, final AppElements elements, final IApiCallback callback) {
        init(c, elements, null, callback);
    }

    public void init(Context c, final AppElements elements, AuthenticationRequest authenticationRequest, final IApiCallback callback) {
        this.mContext = c;
        if (deathRecipient == null) {
            deathRecipient = new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    if (remoteBinder != null) {
                        try { remoteBinder.unlinkToDeath(this, 0); } catch (Exception ignore) {}
                        remoteBinder = null;
                    }
                    storeClient = null;
                    registerInquirer = false;
                    BaseLog.e("Store service has died!");
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        BaseLog.d("Rebinding to Store service...");
                        bindStoreService(elements, authenticationRequest, callback);
                    }, 2000);
                }
            };
        }

        // 2. ServiceConnection
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    remoteBinder = service;
                    try {
                        service.linkToDeath(deathRecipient, 0);
                    } catch (RemoteException e) {
                        BaseLog.e("linkToDeath error: " + e.getMessage());
                    }
                    try {
                        storeClient = IStoreClient.Stub.asInterface(service);
                        handleAuthentication(c, elements, authenticationRequest, callback);
                    } catch (Exception e) {
                        BaseLog.e("get info from store error", e);
                        callback.initFailed(new RemoteException(e.getMessage()));
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    BaseLog.e("onServiceDisconnected");
                    storeClient = null;
                    registerInquirer = false;
                }
            };
            bindStoreService(elements, authenticationRequest, callback);
        } else {
            if (storeClient != null) {
                handleAuthentication(c, elements, authenticationRequest, callback);
            }
        }
    }


    private void handleAuthentication(Context c, AppElements elements, AuthenticationRequest authenticationRequest, IApiCallback callback) {
        try {
            AuthenticationInfo ai;
            if (BaseUtils.is0116NewStore(c)) {
                BaseLog.w("handleAuthentication >>> NewStore Core version is too low, please upgrade NEWSTORE CLIENT!");
                Toast.makeText(mContext, "NewStore version is too low, please upgrade NEWSTORE CLIENT!", Toast.LENGTH_LONG).show();
                ai = storeClient.getAuthenticationInfo(elements);
            } else {
                ai = storeClient.getAuthenticationInfoEx(elements, authenticationRequest);
            }
            BaseLog.d("ai:" + ai);

            //for demo start
            String[] apiPaths = ai.getApiPaths();
            for (String path: apiPaths){
                BaseLog.d("path:"+path);
            }

            String[] apiCerts = ai.getApiCerts();
            int index = 0;
            for (String cert: apiCerts){
                BaseLog.d("cert:"+cert);
                BaseUtils.saveToFile(mContext, "cert"+index, cert);
                index ++;
            }
            //for demo end

            if (ai == null) {
                BaseLog.e(ERROR_MARKET_AUTHENTICATION);
                callback.initFailed(new RemoteException(ERROR_MARKET_AUTHENTICATION));
                return;
            }
            if (TextUtils.isEmpty(ai.getBaseUrl())) {
                BaseLog.d(ai.getMessage());
                callback.initFailed(new RemoteException(ai.getMessage()));
                return;
            }
            callback.initSuccess(ai);
        } catch (RemoteException e) {
            BaseLog.e("get info from store error", e);
            callback.initFailed(e);
        }
    }

    private void bindStoreService(AppElements elements, AuthenticationRequest authenticationRequest, IApiCallback callback) {
        Intent intent = new Intent(AidlConstant.INTENT_ACTION);
        intent.setPackage(AidlConstant.NEWSTORE_PACKAGE_NAME);
        intent.putExtra(AidlConstant.CALLING_PKG, mContext.getPackageName());
        try {
            boolean bindResult = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (!bindResult) {
                callback.initFailed(new RemoteException(ERROR_MARKET_NOT_INSTALLED));
            }
        } catch (Exception e) {
            BaseLog.e("bindService error:" + e.getMessage());
            callback.initFailed(new RemoteException(e.getMessage()));
        }
    }

    public AuthenticationInfo getAuthenticationInfo(){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        try {
            return storeClient.getAuthenticationInfo(StoreSdk.getInstance().getAppElements());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationRequest request){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        if(BaseUtils.is0116NewStore(getContext())){
            BaseLog.w("getAuthenticationInfo >>> NewStore Core version is too low, please upgrade NEWSTORE CLIENT!");
            Toast.makeText(mContext, "NewStore version is too low, please upgrade NEWSTORE CLIENT!", Toast.LENGTH_LONG).show();
            return getAuthenticationInfo();
        }

        try {
            return storeClient.getAuthenticationInfoEx(StoreSdk.getInstance().getAppElements(), request);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerInquirer(Context context, IAppInquirer inquirer){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return;
        }

        try {
            registerInquirer = storeClient.initAppInquirer(context.getPackageName(), inquirer);
            BaseLog.d("registerInquirer:"+registerInquirer);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public byte[] doEncryptionDecryption(byte[] srcData, int mode){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        try {
            return storeClient.doEncryptionDecryption(srcData, mode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String queryAppAppendix(PatchType patchType){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        QueryRequest queryRequest = new QueryRequest();

        List<AppQuery> appQueries = new ArrayList<>();
        AppQuery appQuery = new AppQuery();
        appQuery.packageName = mContext.getPackageName();
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(appQuery.packageName, 0);
            appQuery.verCode = packageInfo.versionCode;
            appQuery.verName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ignore) {}

        AttachFile attachFile = new AttachFile();
        attachFile.patchType = patchType.ordinal();
        List<AttachFile> attachFiles = new ArrayList<>();
        attachFiles.add(attachFile);
        appQuery.attachFiles = BaseUtils.buildJsonArray(attachFiles);

        appQueries.add(appQuery);
        queryRequest.applications = BaseUtils.buildJsonArray(appQueries);
        String request = BaseUtils.toJson(queryRequest);
        BaseLog.d("request:"+request);
        if(BaseUtils.is0116NewStore(mContext)){
            try {
                return storeClient.queryAppConfig(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }

        return requestWithCheck(QUERY_APP_CONFIG, request);
    }

    public String requestLocation(LbsLocationRequest locationRequest, boolean wifi) {
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        String request = BaseUtils.toJson(locationRequest);
        BaseLog.d("request:"+request);

        return requestWithCheck(wifi ? LBS_WIFI_QUERY:LBS_CELL_QUERY, request);
    }

    public String checkAppUpdate(CheckForUpdateRequest checkForUpdateRequest){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        String request = BaseUtils.toJson(checkForUpdateRequest);
        BaseLog.d("request:"+request);

        return requestWithCheck(QUERY_APP_UPDATE, request);
    }

    public String queryKdhurl(QueryKdhurlRequest queryKdhurlRequest){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }
        String request = BaseUtils.toJson(queryKdhurlRequest);
        BaseLog.d("request:"+request);

        return requestWithCheck(QUERY_KDH_URL, request);
    }

    public String paramTaskQuery(){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        QueryRequest queryRequest = new QueryRequest();

        List<AppQuery> appQueries = new ArrayList<>();
        AppQuery appQuery = new AppQuery();
        appQuery.packageName = mContext.getPackageName();
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(appQuery.packageName, 0);
            appQuery.verCode = packageInfo.versionCode;
            appQuery.verName = packageInfo.versionName;
            appQuery.attachFiles = null;
        } catch (PackageManager.NameNotFoundException ignore) {}
        appQueries.add(appQuery);
        queryRequest.applications = BaseUtils.buildJsonArray(appQueries);
        String request = BaseUtils.toJson(queryRequest);
        BaseLog.d("request:"+request);

        return requestWithCheck(PARAM_TASK_QUERY, request);
    }

    public String paramDownQuery(ParamTask task){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        String request = BaseUtils.toJson(new ParamDownQueryRequestV2(task));
        BaseLog.d("request:"+request);

        return requestWithCheck(PARAM_DOWN_QUERY, request);
    }

    public String paramDownVerify(ParamVerify paramVerify){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        String request = BaseUtils.toJson(new ParamVerifyRequestV2(paramVerify));
        BaseLog.d("request:"+request);

        return requestWithCheck(PARAM_DOWN_VERIFY, request);
    }

    private String requestWithCheck(String url, String request){
        if(BaseUtils.is0116NewStore(mContext)){
            Toast.makeText(mContext, "NewStore version is too low, please upgrade NEWSTORE CLIENT!", Toast.LENGTH_LONG).show();
            BaseLog.w("requestLocation >>> NewStore Core version is too low, please upgrade NEWSTORE CLIENT!");
            return null;
        }

        try {
            String response = storeClient.dynamicRequest(url, null, request);
            BaseLog.d("response:"+response);
            return  response;
        }catch (RemoteException e){
            e.printStackTrace();
        }

        return null;
    }
}

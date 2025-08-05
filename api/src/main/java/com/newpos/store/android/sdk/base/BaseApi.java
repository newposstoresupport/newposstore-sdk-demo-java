package com.newpos.store.android.sdk.base;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;
import static com.newpos.store.android.sdk.Constant.ERROR_MARKET_AUTHENTICATION;
import static com.newpos.store.android.sdk.Constant.ERROR_MARKET_NOT_INSTALLED;
import static com.newpos.store.android.sdk.base.UrlConstant.LBS_CELL_QUERY;
import static com.newpos.store.android.sdk.base.UrlConstant.LBS_WIFI_QUERY;
import static com.newpos.store.android.sdk.base.UrlConstant.QUERY_APP_CONFIG;
import static com.newpos.store.android.sdk.base.UrlConstant.QUERY_APP_UPDATE;

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
import com.newpos.store.android.sdk.dto.LbsLocationResponse;
import com.newpos.store.android.sdk.dto.PatchType;
import com.newpos.store.android.sdk.dto.QueryRequest;
import com.newpos.store.android.sdk.dto.QueryResponse;
import com.newpos.store.android.sdk.listener.IApiCallback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

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

    public void init(Context c, final AppElements elements, AuthenticationRequest authenticationRequest, final IApiCallback callback){
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
                        // 注册死亡通知监听
                        service.linkToDeath(deathRecipient, 0);
                    } catch (RemoteException e) {
                        BaseLog.e("linkToDeath error: " + e.getMessage());
                    }
                    try {
                        storeClient = IStoreClient.Stub.asInterface(service);
                        AuthenticationInfo ai = null;
                        if (BaseUtils.is0116NewStore(c)) {
                            ai = storeClient.getAuthenticationInfo(elements);
                        } else {
                            ai = storeClient.getAuthenticationInfoEx(elements, authenticationRequest);
                        }
                        BaseLog.d("ai:" + ai);
                        if (ai == null) {
                            BaseLog.e(ERROR_MARKET_AUTHENTICATION);
                            callback.initFailed(new RemoteException(ERROR_MARKET_AUTHENTICATION));
                            mContext.unbindService(this);
                            return;
                        }
                        if (TextUtils.isEmpty(ai.getBaseUrl())) {
                            BaseLog.d(ai.getMessage());
                            callback.initFailed(new RemoteException(ai.getMessage()));
                            mContext.unbindService(this);
                            return;
                        }
                        callback.initSuccess(ai);
                    } catch (RemoteException e) {
                        BaseLog.e("get info from store error", e);
                        callback.initFailed(e);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    BaseLog.e("onServiceDisconnected");
                    storeClient = null;
                    registerInquirer = false;
                }
            };
        }
        bindStoreService(elements, authenticationRequest, callback);
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

        //TODO 使用newstore测试
//        {
//            //生产
//            appQuery.packageName = "com.newpos.store.android.app";
//            appQuery.verName = "1.0.20250116";
//            appQuery.verCode = 2;
//
//            //开发
//            appQuery.verName = "1.0.20241218";
//            appQuery.verCode = 1;
//        }

        AttachFile attachFile = new AttachFile();
        attachFile.patchType = patchType.ordinal();
        List<AttachFile> attachFiles = new ArrayList<>();
        attachFiles.add(attachFile);
        appQuery.attachFiles = BaseUtils.buildJsonArray(attachFiles);

        appQueries.add(appQuery);
        queryRequest.applications = BaseUtils.buildJsonArray(appQueries);
        String request = BaseUtils.toJson(queryRequest);
        BaseLog.d("request:"+request);
        try {
            String response = "";
            if(BaseUtils.is0116NewStore(mContext)){
                response = storeClient.queryAppConfig(request);
            }else {
                response = storeClient.dynamicRequest(QUERY_APP_CONFIG, null, request);
            }
            BaseLog.d("response:"+response);
            return response;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String requestLocation(LbsLocationRequest locationRequest, boolean wifi) {
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        if(BaseUtils.is0116NewStore(mContext)){
            BaseLog.w("NewStore Core version is too low, please upgrade NEWSTORE CLIENT!");
            return null;
        }

        String request = BaseUtils.toJson(locationRequest);
        BaseLog.d("request:"+request);
        try {
            String response = storeClient.dynamicRequest(wifi ? LBS_WIFI_QUERY:LBS_CELL_QUERY, null, request);
            BaseLog.d("response:"+response);
            return response;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String downloadFile(String url, String filePath) throws IOException {
        BaseLog.d("downloadFile: "+url+","+filePath);
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        InputStream inputStream = response.body().byteStream();
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        fileOutputStream.flush();

        return filePath;

//        if(storeClient == null){
//            BaseLog.e("please init firstly!");
//            return null;
//        }
//
//        try {
//            return storeClient.downloadFile(url, filePath);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public String checkAppUpdate(CheckForUpdateRequest checkForUpdateRequest){
        if(storeClient == null){
            BaseLog.e("please init firstly!");
            return null;
        }

        String request = BaseUtils.toJson(checkForUpdateRequest);
        BaseLog.d("request:"+request);
        try {
            String response = storeClient.dynamicRequest(QUERY_APP_UPDATE, null, request);
            BaseLog.d("response:"+response);
            return response;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    private final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
}

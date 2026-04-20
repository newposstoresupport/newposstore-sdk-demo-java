package com.android.newpos.store.sdk.demo.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.base.SSLParams;
import com.newpos.store.android.sdk.dto.AppResponse;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.ParamDownQueryV2Data;
import com.newpos.store.android.sdk.dto.ParamDownV2Result;
import com.newpos.store.android.sdk.dto.ParamDownloadRequest;
import com.newpos.store.android.sdk.dto.ParamDownloadResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @ClassName : DownloadFileManager//TODO 调整
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:45
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class DownloadFileManager {

    private void okHttpSingleton() {}
    private static class Holder {

        private static final SSLParams SSL_PARAMS = getSslSocketFactory(null);

        private static final OkHttpClient INSTANCE = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(SSL_PARAMS.sSLSocketFactory, SSL_PARAMS.trustManager)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    private static OkHttpClient getInstance() {
        return Holder.INSTANCE;
    }

    public static String downloadFile(String url, String filePath) throws IOException {
        BaseLog.d("downloadFile: "+url+","+filePath);
        Request request = new Request.Builder().url(url).build();
        Response response = getInstance().newCall(request).execute();
        InputStream inputStream = response.body().byteStream();
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        fileOutputStream.flush();
        return filePath;
    }


    public static ParamDownloadResponse downloadParamToPath(ParamDownloadRequest downloadRequest, AppResponse appResponse) throws BaseException {
        BaseLog.d("downloadParamToPath>>"+downloadRequest);
        BaseLog.d("downloadParamToPath>>"+appResponse);
        if(downloadRequest == null){
            throw new IllegalArgumentException("downloadRequest is null!");
        }
        if(TextUtils.isEmpty(downloadRequest.getPackageName())){
            throw new IllegalArgumentException("packageName is null!");
        }
        if(TextUtils.isEmpty(downloadRequest.getSaveFilePath())){
            throw new IllegalArgumentException("saveFilePath is null!");
        }
        AppResponse localResponse = null;
        if(appResponse == null){
            List<AppResponse> appResponseList = StoreSdk.getInstance().paramAbility().queryParamsList();
            if(appResponseList == null || appResponseList.isEmpty()){
                BaseLog.e("Parameter file query failed, please check.");
                return null;
            }
            localResponse = appResponseList.get(0);
        }else {
            localResponse = appResponse;
        }

        if(localResponse == null){
            BaseLog.e("parameters files is empty, please config.");
            return null;
        }

        if(!Objects.equals(localResponse.packageName, downloadRequest.getPackageName())){
            BaseLog.e("Package name does not match");
            return null;
        }

        ParamDownloadResponse paramDownloadResponse = new ParamDownloadResponse();
        paramDownloadResponse.appId = localResponse.appId;
        paramDownloadResponse.packageName = localResponse.packageName;
        paramDownloadResponse.verCode = localResponse.verCode;
        paramDownloadResponse.verName = localResponse.verName;
        paramDownloadResponse.attachFiles = new ArrayList<>();
        String saveFilePath = downloadRequest.getSaveFilePath();
        List<AttachFile> attachFiles = BaseUtils.toObject(localResponse.attachFiles, AttachFile.class);
        for (AttachFile file: attachFiles){
            String fileName = "file_" + System.currentTimeMillis() + "_" + new Random().nextInt(10000);
            try {
                file.filePath = downloadFile(file.patchUrl, saveFilePath+"/"+fileName);
            } catch (IOException e) {
                BaseLog.e("download "+file.patchUrl+" failed!");
            }
            paramDownloadResponse.attachFiles.add(file);
        }

        return paramDownloadResponse;
    }

    public static List<ParamDownV2Result> downloadParamToPathV2(Context context, List<ParamDownQueryV2Data> v2DataList) throws PackageManager.NameNotFoundException {
        List<ParamDownV2Result> v2ResultList = new ArrayList<>();
        String packageName = context.getPackageName();
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        String saveFilePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        for (ParamDownQueryV2Data v2Data : v2DataList){
            ParamDownV2Result v2Result = new ParamDownV2Result();
            v2Result.attachFiles = new ArrayList<>();
            v2Result.dirs = new ArrayList<>();
            v2Result.packageName = packageName;
            v2Result.verCode = packageInfo.versionCode;
            v2Result.verName = packageInfo.versionName;
            JsonObject paramJsonObject = v2Data.param;
            JsonObject wrapperJsonObject = v2Data.wrapped;
            List<AttachFile> attachFiles = BaseUtils.toObject(v2Data.attachFiles, AttachFile.class);

            //download params
            for (AttachFile attachFile : attachFiles){
                String name = "param_file_v2_" + System.currentTimeMillis() + "_" + new Random().nextInt(10000);
                String fileName = name+".zip";
                try {
                    String zipFile = downloadFile(attachFile.patchUrl, saveFilePath+"/"+fileName);
                    BaseLog.d("downloadFile zipFile:"+zipFile);
                    boolean unzipResult = AppUtils.unzipFiles(zipFile, saveFilePath+"/"+name);
                    BaseLog.d("downloadFile unzipResult:"+unzipResult);
                    String dir = saveFilePath + "/" + name;
                    attachFile.fileDir = dir;
                    v2Result.dirs.add(dir);
                } catch (IOException e) {
                    BaseLog.e("download "+attachFile.patchUrl+" failed!");
                }
                v2Result.attachFiles.add(attachFile);
            }

            //check file and replace value
            BaseLog.d("v2Result:"+v2Result);
            BaseLog.d("paramJsonObject:"+paramJsonObject);
            BaseLog.d("wrapperJsonObject:"+wrapperJsonObject);
            List<AttachFile> paramsFiles = v2Result.attachFiles;
            if(paramsFiles.isEmpty()){
                BaseLog.w("after downloading, params files is empty!");
                return v2ResultList;
            }

            boolean replaceResult = AppUtils.replaceFiles(paramsFiles, paramJsonObject, wrapperJsonObject);
            BaseLog.d("replaceResult:"+replaceResult);

            //add result to list
            v2ResultList.add(v2Result);
        }

        return v2ResultList;
    }


    public static SSLParams getSslSocketFactory(Context context){
        SSLParams sslParams = new SSLParams();
        InputStream inputStream = null;
        InputStream deviceInputStream = null;
        InputStream persistStream = null;
        InputStream serverStream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, "123456".toCharArray());

            //TODO 需要增加自定义证书，否则三方私有化或者修改了证书的将无法下载
            TrustManagerFactory customTmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            customTmf.init(keyStore);
            TrustManager[] customTm = customTmf.getTrustManagers();

            TrustManagerFactory systemTmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            systemTmf.init((KeyStore) null);
            TrustManager[] systemTm = systemTmf.getTrustManagers();

            X509TrustManager x509TrustManager = new MySafeTrustManager(
                    (X509TrustManager) systemTm[0], (X509TrustManager) customTm[0]);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = x509TrustManager;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException ignore) {}
            }if(deviceInputStream != null){
                try {
                    deviceInputStream.close();
                } catch (IOException ignore) {}
            }if(persistStream != null){
                try {
                    persistStream.close();
                } catch (IOException ignore) {}
            }if(serverStream != null){
                try {
                    serverStream.close();
                } catch (IOException ignore) {}
            }
        }

        return sslParams;
    }
}

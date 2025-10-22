package com.android.newpos.store.sdk.demo.base;

import android.text.TextUtils;

import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppResponse;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.ParamDownloadRequest;
import com.newpos.store.android.sdk.dto.ParamDownloadResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class DownloadFile {

    private void okHttpSingleton() {}
    private static class Holder {
        private static final OkHttpClient INSTANCE = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
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
            localResponse = StoreSdk.getInstance().paramAbility().queryParamsList().get(0);
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
}

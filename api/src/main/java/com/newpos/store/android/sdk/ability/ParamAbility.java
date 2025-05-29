package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.text.TextUtils;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.AppResponse;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.ParamDownloadRequest;
import com.newpos.store.android.sdk.dto.ParamDownloadResponse;
import com.newpos.store.android.sdk.dto.PatchType;
import com.newpos.store.android.sdk.dto.QueryResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @ClassName : ParamAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:34
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamAbility extends BaseAbility {
    public ParamAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
    }

    /**
     * Query application query parameter file list
     * @return {@link List<AppResponse>}
     */
    public List<AppResponse> queryParamsList() throws BaseException {
        BaseLog.d("queryParamsList>>");
        String response = BaseApi.getInstance().queryAppAppendix(PatchType.PARAMS);
        QueryResponse queryResponse = BaseUtils.toObject(response, QueryResponse.class);
        BaseLog.d("queryResponse:"+queryResponse);
        if(!Objects.equals(queryResponse.code, API_SUCCESS)){
            throw new BaseException(queryResponse.msg+"["+queryResponse.code+"]");
        }
        return BaseUtils.toObject(queryResponse.data, AppResponse.class);
    }

    /**
     * Download Parameters
     * @param downloadRequest Download Request {@link ParamDownloadRequest}
     * @param appResponse Application parameter file configuration response {@link AppResponse}
     * @return Download results {@link ParamDownloadResponse}
     * @throws BaseException
     */
    public ParamDownloadResponse downloadParamToPath(ParamDownloadRequest downloadRequest, AppResponse appResponse) throws BaseException {
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
            localResponse = queryParamsList().get(0);
        }else {
            localResponse = appResponse;
        }
//        if(!Objects.equals(localResponse.packageName, downloadRequest.getPackageName())){
//            BaseLog.e("Package name does not match");
//            return null;
//        }

        ParamDownloadResponse paramDownloadResponse = new ParamDownloadResponse();
        paramDownloadResponse.appId = localResponse.appId;
        paramDownloadResponse.packageName = localResponse.packageName;
        paramDownloadResponse.verCode = localResponse.verCode;
        paramDownloadResponse.verName = localResponse.verName;
        String saveFilePath = downloadRequest.getSaveFilePath();
        List<AttachFile> attachFiles = BaseUtils.toObject(localResponse.attachFiles, AttachFile.class);
        for (AttachFile file: attachFiles){
            String fileName = "file_" + System.currentTimeMillis() + "_" + new Random().nextInt(10000);
            try {
                file.filePath = BaseApi.getInstance().downloadFile(file.patchUrl, saveFilePath+"/"+fileName);
            } catch (IOException e) {
                BaseLog.e("download "+file.patchUrl+" failed!");
            }
        }

        return paramDownloadResponse;
    }

    public ParamDownloadResponse downloadLastSuccessToPath(ParamDownloadRequest downloadRequest){
        //TODO
        return null;
    }
}

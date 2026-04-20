package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.AppResponse;
import com.newpos.store.android.sdk.dto.PatchType;
import com.newpos.store.android.sdk.dto.QueryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(TextUtils.isEmpty(response)){
            return new ArrayList<>();
        }
        QueryResponse queryResponse = BaseUtils.toObject(response, QueryResponse.class);
        if(queryResponse == null){
            return new ArrayList<>();
        }
        BaseLog.d("queryResponse:"+queryResponse);
        if(!Objects.equals(queryResponse.code, API_SUCCESS)){
            throw new BaseException(queryResponse.msg+"["+queryResponse.code+"]");
        }
        List<AppResponse> list = BaseUtils.toObject(queryResponse.data, AppResponse.class);
        Context context = BaseApi.getInstance().getContext();
        for (AppResponse appResponse: list){
            String packageName = context.getPackageName();
            appResponse.packageName = packageName;
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                appResponse.verCode = packageInfo.versionCode;
                appResponse.verName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException ignore) {}
        }
        return list;
    }
}

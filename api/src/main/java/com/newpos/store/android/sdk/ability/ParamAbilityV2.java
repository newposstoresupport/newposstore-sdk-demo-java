package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.AttachFile;
import com.newpos.store.android.sdk.dto.BaseResponse;
import com.newpos.store.android.sdk.dto.ParamDownQueryResponseV2;
import com.newpos.store.android.sdk.dto.ParamDownQueryV2Data;
import com.newpos.store.android.sdk.dto.ParamDownV2Result;
import com.newpos.store.android.sdk.dto.ParamTask;
import com.newpos.store.android.sdk.dto.ParamVerify;
import com.newpos.store.android.sdk.dto.QueryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : ParamAbilityV2
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:34
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamAbilityV2 extends BaseAbility {
    public ParamAbilityV2(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
    }

    /**
     * Query backend configured parameters task
     * @return List<ParamTask>
     * @throws BaseException BaseException
     */
    public List<ParamTask> queryParamTask() throws BaseException {
        BaseLog.d("queryParamTask>>");
        String response = BaseApi.getInstance().paramTaskQuery();
        if(TextUtils.isEmpty(response)){
            return new ArrayList<>();
        }

        QueryResponse queryResponse = BaseUtils.toObject(response, QueryResponse.class);
        if(queryResponse == null){
            return new ArrayList<>();
        }
        BaseLog.d("queryParamTask:"+queryResponse);
        if(!Objects.equals(queryResponse.code, API_SUCCESS)){
            throw new BaseException(queryResponse.msg+"["+queryResponse.code+"]");
        }
        return BaseUtils.toObject(queryResponse.data, ParamTask.class);
    }

    /**
     * Download parameters
     * @param task ParamTask
     * @return List<ParamDownV2Result>
     * @throws BaseException BaseException
     */
    public List<ParamDownQueryV2Data> queryParamDown(ParamTask task) throws BaseException {
        BaseLog.d("queryParamDown>>"+task);
        String response = BaseApi.getInstance().paramDownQuery(task);
        if(TextUtils.isEmpty(response)){
            return new ArrayList<>();
        }

        ParamDownQueryResponseV2 downV2 = BaseUtils.toObject(response, ParamDownQueryResponseV2.class);
        if(downV2 == null){
            return new ArrayList<>();
        }
        BaseLog.d("queryParamDown:"+downV2);
        if(!Objects.equals(downV2.code, API_SUCCESS)){
            throw new BaseException(downV2.msg+"["+downV2.code+"]");
        }

        List<ParamDownQueryV2Data> v2DataList = BaseUtils.toObject(downV2.data, ParamDownQueryV2Data.class);
        BaseLog.d("queryParamDown v2DataList:"+v2DataList);
        return v2DataList;
    }

    /**
     * Notification of download results
     * @param paramVerify ParamVerify
     * @return boolean
     * @throws BaseException BaseException
     */
    public boolean paramCallResult(ParamVerify paramVerify) throws BaseException {
        BaseLog.d("queryParamTask>>");
        String response = BaseApi.getInstance().paramDownVerify(paramVerify);
        if(TextUtils.isEmpty(response)){
            return false;
        }

        BaseResponse baseResponse = BaseUtils.toObject(response, BaseResponse.class);
        if(baseResponse == null){
            return false;
        }
        BaseLog.d("baseResponse:"+baseResponse);
        if(!Objects.equals(baseResponse.code, API_SUCCESS)){
            throw new BaseException(baseResponse.msg+"["+baseResponse.code+"]");
        }
        return true;
    }
}

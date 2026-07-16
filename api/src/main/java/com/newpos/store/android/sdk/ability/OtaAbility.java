package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.text.TextUtils;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.FirmwareInfo;
import com.newpos.store.android.sdk.dto.QueryFirmwareRequest;
import com.newpos.store.android.sdk.dto.QueryFirmwareResponse;
import com.newpos.store.android.sdk.dto.UpdateFirmwareRequest;
import com.newpos.store.android.sdk.dto.UpdateFirmwareResponse;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName : OtaAbilities
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:29
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class OtaAbility extends BaseAbility {
    public OtaAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
        for (String api : apis){
            BaseLog.d("OtaAbility:api:"+api);
        }
    }

    /**
     * Check for new firmware
     * @param request QueryFirmwareRequest
     * @return FirmwareInfo
     * @throws BaseException
     */
    public FirmwareInfo queryFirmware(QueryFirmwareRequest request) throws BaseException {
        BaseLog.d("queryFirmware>>");
        String response = BaseApi.getInstance().queryFirmware(request);
        if(TextUtils.isEmpty(response)){
            return null;
        }

        QueryFirmwareResponse queryResponse = BaseUtils.toObject(response, QueryFirmwareResponse.class);
        if(queryResponse == null){
            return null;
        }
        BaseLog.d("queryFirmware:"+queryResponse);
        if(!Objects.equals(queryResponse.code, API_SUCCESS)){
            throw new BaseException(queryResponse.msg+"["+queryResponse.code+"]");
        }
        return queryResponse.data;
    }

    /**
     * Firmware update progress
     * @param request UpdateFirmwareRequest
     * @return UpdateFirmwareResponse
     * @throws BaseException
     */
    public UpdateFirmwareResponse updateFirmwareProgress(UpdateFirmwareRequest request) throws BaseException {
        BaseLog.d("updateFirmwareProgress>>");
        String response = BaseApi.getInstance().updateFirmwareProgress(request);
        if(TextUtils.isEmpty(response)){
            return null;
        }

        UpdateFirmwareResponse firmwareResponse = BaseUtils.toObject(response, UpdateFirmwareResponse.class);
        if(firmwareResponse == null){
            return null;
        }
        BaseLog.d("updateFirmwareProgress:"+firmwareResponse);
        if(!Objects.equals(firmwareResponse.code, API_SUCCESS)){
            throw new BaseException(firmwareResponse.msg+"["+firmwareResponse.code+"]");
        }
        return firmwareResponse;
    }
}

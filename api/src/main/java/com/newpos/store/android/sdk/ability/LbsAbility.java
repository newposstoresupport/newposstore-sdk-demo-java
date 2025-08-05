package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.text.TextUtils;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;
import com.newpos.store.android.sdk.dto.LbsLocationResponse;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName : LbsAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/15-16:42
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class LbsAbility extends BaseAbility {
    public LbsAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
        for (String api : apis){
            BaseLog.d("LbsAbility:api:"+api);
        }
    }

    public LbsLocationResponse getLocation(LbsLocationRequest locationRequest, boolean wifi) throws BaseException {
        String response = BaseApi.getInstance().requestLocation(locationRequest, wifi);
        if(TextUtils.isEmpty(response)){
            return null;
        }

        LbsLocationResponse locationResponse = BaseUtils.toObject(response, LbsLocationResponse.class);
        if(locationResponse == null){
            return null;
        }

        if(!Objects.equals(locationResponse.code, API_SUCCESS)){
            throw new BaseException(locationResponse.msg+"["+locationResponse.code+"]");
        }

        return locationResponse;
    }
}

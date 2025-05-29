package com.newpos.store.android.sdk.ability;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;
import com.newpos.store.android.sdk.dto.LbsLocationResponse;

import java.util.List;

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

//    public LbsLocationResponse requestLocation(LbsLocationRequest locationRequest, boolean wifi){
//        BaseRequest<LbsLocationRequest> baseRequest = new BaseRequest<>();
//        baseRequest.setRequestBean(locationRequest);
//        baseRequest.setApi(getLocationApi(wifi));
//        return (LbsLocationResponse) requestHttp(baseRequest, LbsLocationResponse.class);
//    }

    public String getLocationApi(boolean wifi){
        List<String> apis = getApis();
        for (String api : apis){
            if(wifi){
                if(api.contains("wifi")){
                    return api;
                }
            }else {
                return api;
            }
        }
        return null;
    }
}

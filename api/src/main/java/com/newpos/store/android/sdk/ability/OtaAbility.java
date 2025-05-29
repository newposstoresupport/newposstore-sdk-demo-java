package com.newpos.store.android.sdk.ability;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.dto.AppElements;

import java.util.List;

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
}

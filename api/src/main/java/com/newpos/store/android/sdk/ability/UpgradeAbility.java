package com.newpos.store.android.sdk.ability;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.dto.AppElements;

import java.util.List;

/**
 * @ClassName : UpgradeAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:35
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class UpgradeAbility extends BaseAbility {

    public UpgradeAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
    }
}

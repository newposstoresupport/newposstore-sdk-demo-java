package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : ParamVerifyRequestV2
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/23-17:30
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamVerifyRequestV2 {
    public ParamVerify param;

    public ParamVerifyRequestV2() {
    }

    public ParamVerifyRequestV2(ParamVerify param) {
        this.param = param;
    }

    @NonNull
    @Override
    public String toString() {
        return "ParamVerifyRequestV2{" +
                "param=" + param +
                '}';
    }
}

package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : ParamDownQueryRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/23-17:01
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamDownQueryRequestV2 {
    public ParamTask param;

    public ParamDownQueryRequestV2() {
    }

    public ParamDownQueryRequestV2(ParamTask param) {
        this.param = param;
    }

    @NonNull
    @Override
    public String toString() {
        return "ParamDownQueryRequest{" +
                "param=" + param +
                '}';
    }
}

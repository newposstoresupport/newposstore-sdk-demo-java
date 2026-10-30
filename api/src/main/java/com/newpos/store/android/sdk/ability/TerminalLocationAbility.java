package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.TerminalLocationResponse;

import java.util.List;
import java.util.Objects;

/**
 * 经商店 AIDL 取终端谷歌/百度定位（含地址）。与云端基站/WiFi 的 {@link LbsAbility} 分开。
 * <p>
 * Binder 同步等待，最坏约 20 秒。不要在主线程调用。
 * </p>
 */
public class TerminalLocationAbility extends BaseAbility {

    public TerminalLocationAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
    }

    /**
     * 取终端定位。
     *
     * @param provider {@code google} / {@code baidu}；空或省略则跟商店设置页图商
     * @return 成功结果；请求失败或无法解析时返回 {@code null}
     * @throws BaseException 商店返回非成功码
     */
    public TerminalLocationResponse getLocation(String provider) throws BaseException {
        JsonObject body = new JsonObject();
        if (!TextUtils.isEmpty(provider) && !TextUtils.isEmpty(provider.trim())) {
            body.addProperty("provider", provider.trim());
        }
        String response = BaseApi.getInstance().getTerminalLocation(body.toString());
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        TerminalLocationResponse locationResponse = BaseUtils.toObject(response, TerminalLocationResponse.class);
        if (locationResponse == null) {
            return null;
        }

        if (!Objects.equals(locationResponse.code, API_SUCCESS)) {
            throw new BaseException(locationResponse.msg + "[" + locationResponse.code + "]");
        }

        return locationResponse;
    }
}

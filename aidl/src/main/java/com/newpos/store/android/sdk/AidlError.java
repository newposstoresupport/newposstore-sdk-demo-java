package com.newpos.store.android.sdk;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName : AidlError
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/10/9-11:01
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public enum AidlError {
    /**
     * AIDL错误枚举
     */
    NETWORK_UNAVAILABLE("NSC00", "Network Unavailable"),
    LOAD_PACKAGE_FAIL("NSC01", "Can Not Load Package"),
    INVALID_APP_ELEMENTS("NSC02", "Invalid App Elements"),
    INVALID_APP_ELEMENTS_CONTENT("NSC03", "Invalid App Elements Content"),
    REQUEST_URL_JSON_NULL("NSC04", "Request URL or JSON is NULL"),
    REQUEST_SERVER_EXCEPTION("NSC05", "Request Server Exception"),
    RESPONSE_NULL("NSC06", "Server Response Data is Empty"),
    APP_ELEMENTS_ILLEGAL("NSC07", "App Elements Are Illegal"),
    UNSUPPORTED_ABILITIES("NSC08", "Unsupported Any Abilities"),
    ;

    private final String code;
    private final String description;

    AidlError(String code, String des) {
        this.code = code;
        this.description = des;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return "AidlError{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            jsonObject.put("msg", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}

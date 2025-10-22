package com.newpos.store.android.sdk.dto;

public enum RkiCode {
    BIND_ERROR(1, "rki service is null, please bindRkiService first!"),
    INVOKE_ERROR(2, "setOption is error!"),
    INVOKE_SUCCESS(3, "INVOKE SUCCESS!");

    private final int code;
    private final String message;

    RkiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter 方法
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

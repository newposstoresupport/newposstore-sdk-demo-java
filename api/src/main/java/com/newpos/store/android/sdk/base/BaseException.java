package com.newpos.store.android.sdk.base;

import java.io.IOException;

/**
 * @ClassName : BaseException
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/4/30-12:34
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class BaseException extends IOException {
    public String msg;

    public BaseException(String message) {
        super(message);
        this.msg = message;
    }
}

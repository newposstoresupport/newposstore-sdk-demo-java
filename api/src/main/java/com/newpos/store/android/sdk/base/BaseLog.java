package com.newpos.store.android.sdk.base;

import android.util.Log;

import com.newpos.store.android.sdk.StoreSdk;

import java.util.Locale;

/**
 * @ClassName : BaseLog
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-9:15
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class BaseLog {
    private static final String LOG_PREFIX = "StoreSDK_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private static String makeLogTag(StackTraceElement caller) {
        if (caller == null) {
            return makeLogTag("service");
        }
        String className = caller.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        return String.format(Locale.CHINA, "%s.%s(L:%d)",
                className,
                caller.getMethodName(),
                caller.getLineNumber());
    }

    /**
     * debug信息
     * @param msg
     */
    public static void d(String msg) {
        if (StoreSdk.isDebug) {
            String tag = makeLogTag(getCallerStackTraceElement());
            Log.d(tag, msg);
        }
    }

    public static void w(String msg) {
        if (StoreSdk.isDebug) {
            String tag = makeLogTag(getCallerStackTraceElement());
            Log.w(tag, msg);
        }
    }

    /**
     * error信息
     * @param msg
     */
    public static void e(String msg) {
        String tag = makeLogTag(getCallerStackTraceElement());
        Log.e(tag, msg);
    }

    public static void e(String msg, Throwable throwable){
        String tag = makeLogTag(getCallerStackTraceElement());
        Log.e(tag, msg, throwable);
    }
}

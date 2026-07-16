package com.android.newpos.store.sdk.demo.base;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过反射访问 ROM 侧 {@code com.pos.device} API（SDKManager / DevConfig）。
 * <p>
 * 不依赖公开的 {@code sdk.jar}，在非 Newpos ROM 或类缺失时安全降级。
 */
public final class RomDeviceReflect {
    private static final String TAG = "RomDeviceReflect";
    private static final String SDK_MANAGER = "com.pos.device.SDKManager";
    private static final String SDK_MANAGER_CALLBACK = "com.pos.device.SDKManagerCallback";
    private static final String DEV_CONFIG = "com.pos.device.config.DevConfig";
    private static final String DEV_CONFIG_CUSTOMER = "com.pos.device.config.DevConfig$CUSTOMER";

    private RomDeviceReflect() {
    }

    /**
     * 反射调用 {@code SDKManager.init(Context, SDKManagerCallback)}。
     *
     * @return true 表示调用成功；类不存在或调用失败返回 false
     */
    public static boolean initSdkManager(Context context) {
        if (context == null) {
            return false;
        }
        try {
            Class<?> managerClass = Class.forName(SDK_MANAGER);
            Class<?> callbackClass = Class.forName(SDK_MANAGER_CALLBACK);
            Object callback = Proxy.newProxyInstance(
                    callbackClass.getClassLoader(),
                    new Class<?>[]{callbackClass},
                    (proxy, method, args) -> null
            );
            Method init = managerClass.getMethod("init", Context.class, callbackClass);
            init.invoke(null, context.getApplicationContext(), callback);
            return true;
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "ROM SDK not present, skip SDKManager.init");
            return false;
        } catch (Throwable t) {
            Log.e(TAG, "SDKManager.init via reflection failed", t);
            return false;
        }
    }

    /** 对应 {@code DevConfig.CUSTOMER.NAME}，失败返回空串。 */
    public static String getCustomerName() {
        Object value = getStaticField(DEV_CONFIG_CUSTOMER, "NAME");
        return value == null ? "" : String.valueOf(value);
    }

    /** 对应 {@code DevConfig.getFirmwareId()}，失败返回空串。 */
    public static String getFirmwareId() {
        return invokeStaticString(DEV_CONFIG, "getFirmwareId");
    }

    /** 对应 {@code DevConfig.getFirmwareVersion()}，失败返回空串。 */
    public static String getFirmwareVersion() {
        return invokeStaticString(DEV_CONFIG, "getFirmwareVersion");
    }

    private static Object getStaticField(String className, String fieldName) {
        try {
            return Class.forName(className).getField(fieldName).get(null);
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "ROM class missing: " + className);
            return null;
        } catch (Throwable t) {
            Log.e(TAG, "getStaticField failed: " + className + "#" + fieldName, t);
            return null;
        }
    }

    private static String invokeStaticString(String className, String methodName) {
        try {
            Object result = Class.forName(className).getMethod(methodName).invoke(null);
            return result == null ? "" : String.valueOf(result);
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "ROM class missing: " + className);
            return "";
        } catch (Throwable t) {
            Log.e(TAG, "invokeStaticString failed: " + className + "#" + methodName, t);
            return "";
        }
    }
}

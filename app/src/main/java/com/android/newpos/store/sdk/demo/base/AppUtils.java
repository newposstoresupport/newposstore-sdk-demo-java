package com.android.newpos.store.sdk.demo.base;

import com.tencent.mmkv.MMKV;

/**
 * @ClassName : AppUtils
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/26-16:48
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppUtils {
    private static final MMKV mmkv = MMKV.defaultMMKV();
    private static final String CLIENT_ID = "clientId";

    public static void putClientId(String clientId){
        mmkv.encode(CLIENT_ID, clientId);
    }

    public static String getClientId(){
        return mmkv.decodeString(CLIENT_ID);
    }
}

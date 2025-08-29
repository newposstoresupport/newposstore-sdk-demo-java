package com.android.newpos.store.sdk.demo.base;

import android.util.Log;
import android.widget.Toast;

import com.android.newpos.store.sdk.demo.MainApplication;
import com.tencent.mmkv.MMKV;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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

    public static void showToast(String message) {
        Disposable d= Observable.just( true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_SHORT).show(),
                throwable -> {
                    Log.e("showToast", "Error while showing toast: " + throwable.getMessage(), throwable);
                }
                );
    }
    public static void showToast(int msgId) {
        showToast(MainApplication.getContext().getString(msgId));
    }
}

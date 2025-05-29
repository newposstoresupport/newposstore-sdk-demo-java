package com.newpos.store.android.sdk.listener;

import android.os.RemoteException;

/**
 * @ClassName : StoreCallback
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:21
 * @Version : 1.0
 * @Description :
 * @website : https://www.newpostech.com/
 */
public interface IStoreCallback {
    void initSuccess();

    void initFailed(RemoteException remoteException);
}

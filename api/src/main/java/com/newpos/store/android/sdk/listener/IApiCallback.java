package com.newpos.store.android.sdk.listener;

import android.os.RemoteException;

import com.newpos.store.android.sdk.dto.AuthenticationInfo;

/**
 * @ClassName : IApiCallback
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-16:38
 * @Version : 1.0
 * @Description :
 * @website : https://www.newpostech.com/
 */
public interface IApiCallback {
    void initSuccess(AuthenticationInfo ai);

    void initFailed(RemoteException remoteException);
}

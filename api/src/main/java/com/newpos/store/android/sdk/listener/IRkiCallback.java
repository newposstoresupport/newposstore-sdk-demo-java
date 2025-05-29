package com.newpos.store.android.sdk.listener;

import com.newpos.rki.RKIKey;

import java.util.List;

/**
 * @ClassName : IRkiCallback
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/29-10:17
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public interface IRkiCallback {
    void onDownload(int code, String message, List<RKIKey> keyList);
}

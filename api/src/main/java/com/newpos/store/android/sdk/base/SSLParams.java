package com.newpos.store.android.sdk.base;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @ClassName : SSLParams
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/12-13:49
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class SSLParams {
    public SSLSocketFactory sSLSocketFactory;
    public X509TrustManager trustManager;
}

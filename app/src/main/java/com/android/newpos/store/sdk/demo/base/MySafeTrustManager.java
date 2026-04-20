package com.android.newpos.store.sdk.demo.base;

import android.text.TextUtils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @ClassName : MySafeTrustManager
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/4/15-10:54
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class MySafeTrustManager implements X509TrustManager {

    private final X509TrustManager system;
    private final X509TrustManager custom;

    public MySafeTrustManager(X509TrustManager system, X509TrustManager custom) {
        this.system = system;
        this.custom = custom;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // do nothing
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        if (chain == null || chain.length == 0 || TextUtils.isEmpty(authType)) {
            throw new CertificateException("Invalid certificate chain or authType!");
        }

        try {
            // 先走自定义证书
            custom.checkServerTrusted(chain, authType);
        } catch (Exception e) {
            // fallback 到系统证书
            system.checkServerTrusted(chain, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] systemIssuers = system.getAcceptedIssuers();
        X509Certificate[] customIssuers = custom.getAcceptedIssuers();

        X509Certificate[] result = new X509Certificate[systemIssuers.length + customIssuers.length];

        System.arraycopy(systemIssuers, 0, result, 0, systemIssuers.length);
        System.arraycopy(customIssuers, 0, result, systemIssuers.length, customIssuers.length);

        return result;
    }
}
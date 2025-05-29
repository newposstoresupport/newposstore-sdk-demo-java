package com.newpos.store.android.sdk.base;

import com.newpos.store.android.sdk.dto.AppElements;

import java.util.List;

/**
 * @ClassName : BaseAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:36
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class BaseAbility {
    private final String baseUrl;
    private final AppElements appElements;
    private final String serialNumber;
    private final List<String> apis;


    public BaseAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis){
        this.baseUrl = baseUrl;
        this.appElements = elements;
        this.serialNumber = serialNumber;
        this.apis = apis;
    }

    public String getBaseUrl(){
        return this.baseUrl;
    }

    public String getSerialNumber(){
        return this.serialNumber;
    }

    public AppElements getAppElements(){
        return this.appElements;
    }

    public List<String> getApis() {
        return apis;
    }
}

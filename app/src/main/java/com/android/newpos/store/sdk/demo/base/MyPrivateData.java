package com.android.newpos.store.sdk.demo.base;

import androidx.annotation.NonNull;

/**
 * @ClassName : MyPrivateData
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/27-9:05
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class MyPrivateData {
    private String remark;
    private String clientId;

    public MyPrivateData(){

    }

    public MyPrivateData(String remark, String clientId) {
        this.remark = remark;
        this.clientId = clientId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public String toString() {
        return "MyPrivateData{" +
                "remark='" + remark + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}

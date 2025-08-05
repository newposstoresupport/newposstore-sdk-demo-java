package com.newpos.store.android.sdk.dto;

/**
 * @ClassName : EAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/26-16:46
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public enum EAbility {//TODO 待重构
    Lbs("lbs"),
    Param_Download("param"),
    Cloud_Message("cloud"),
    Go_Insight("go_insight"),
    Upgrade_("update"),
    OTA("ota"),
    RKI("rki"),
    APP("app");

    private String val;

    private EAbility(String val){
        this.val = val;
    }

    public String getVal(){
        return this.val;
    }
}

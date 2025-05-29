package com.newpos.store.android.sdk.dto;

/**
 * @ClassName : LbsLocationRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/22-16:19
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class LbsLocationRequest {
    private String output;
    private String mnc;
    private String ci;
    private String appid;
    private String mcc;
    private String lac;
    private String radio;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    @Override
    public String toString() {
        return "LbsLocationRequest{" +
                "output='" + output + '\'' +
                ", mnc='" + mnc + '\'' +
                ", ci='" + ci + '\'' +
                ", appid='" + appid + '\'' +
                ", mcc='" + mcc + '\'' +
                ", lac='" + lac + '\'' +
                ", radio='" + radio + '\'' +
                '}';
    }
}

package com.newpos.store.android.sdk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @ClassName : TerminalInfo
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:39
 * @Version : 1.0
 * @Description : 终端信息，@Deprecated，建议使用@{@link AuthenticationInfo}
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
@Deprecated
public class TerminalInfo implements Parcelable {
    private String serialNo;
    private String modelName;
    private String factoryName;
    private String deviceId;
    private String imei;
    private String macAddress;
    private String androidId;
    private String statusCode;

    public TerminalInfo() {
    }

    public TerminalInfo(String serialNo) {
        this.serialNo = serialNo;
    }

    protected TerminalInfo(Parcel in) {
        serialNo = in.readString();
        modelName = in.readString();
        factoryName = in.readString();
        deviceId = in.readString();
        imei = in.readString();
        macAddress = in.readString();
        androidId = in.readString();
        statusCode = in.readString();
    }

    public static final Creator<TerminalInfo> CREATOR = new Creator<TerminalInfo>() {
        @Override
        public TerminalInfo createFromParcel(Parcel in) {
            return new TerminalInfo(in);
        }

        @Override
        public TerminalInfo[] newArray(int size) {
            return new TerminalInfo[size];
        }
    };

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(serialNo);
        dest.writeString(modelName);
        dest.writeString(factoryName);
        dest.writeString(deviceId);
        dest.writeString(imei);
        dest.writeString(macAddress);
        dest.writeString(androidId);
        dest.writeString(statusCode);
    }

    @Override
    public String toString() {
        return "TerminalInfo{" +
                "serialNo='" + serialNo + '\'' +
                ", modelName='" + modelName + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", imei='" + imei + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", androidId='" + androidId + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}

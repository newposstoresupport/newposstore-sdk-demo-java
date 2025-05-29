package com.newpos.store.android.sdk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @ClassName : AuthenticationInfo
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-10:25
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AuthenticationInfo implements Parcelable {
    public String code;
    public String msg;

    private int type;

    private String token;
    private String message;

    private String baseUrl;

    private String username;
    private String password;

    private String[] apiPaths;

    private String serialNo;
    private String modelName;
    private String factoryName;
    private String deviceId;
    private String imei;
    private String macAddress;
    private String androidId;
    private String statusCode;

    private String registerId;

    public AuthenticationInfo() {
    }

    protected AuthenticationInfo(Parcel in) {
        code = in.readString();
        msg = in.readString();
        type = in.readInt();
        token = in.readString();
        message = in.readString();
        baseUrl = in.readString();
        username = in.readString();
        password = in.readString();
        apiPaths = in.createStringArray();
        serialNo = in.readString();
        modelName = in.readString();
        factoryName = in.readString();
        deviceId = in.readString();
        imei = in.readString();
        macAddress = in.readString();
        androidId = in.readString();
        statusCode = in.readString();
        registerId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msg);
        dest.writeInt(type);
        dest.writeString(token);
        dest.writeString(message);
        dest.writeString(baseUrl);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeStringArray(apiPaths);
        dest.writeString(serialNo);
        dest.writeString(modelName);
        dest.writeString(factoryName);
        dest.writeString(deviceId);
        dest.writeString(imei);
        dest.writeString(macAddress);
        dest.writeString(androidId);
        dest.writeString(statusCode);
        dest.writeString(registerId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenticationInfo> CREATOR = new Creator<AuthenticationInfo>() {
        @Override
        public AuthenticationInfo createFromParcel(Parcel in) {
            return new AuthenticationInfo(in);
        }

        @Override
        public AuthenticationInfo[] newArray(int size) {
            return new AuthenticationInfo[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getApiPaths() {
        return apiPaths;
    }

    public void setApiPaths(String[] apiPaths) {
        this.apiPaths = apiPaths;
    }

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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthenticationInfo{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", token='" + token + '\'' +
                ", message='" + message + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", modelName='" + modelName + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", imei='" + imei + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", androidId='" + androidId + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", registerId='" + registerId + '\'' +
                '}';
    }
}

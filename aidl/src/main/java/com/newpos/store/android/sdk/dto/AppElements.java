package com.newpos.store.android.sdk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @ClassName : AppElements
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-11:18
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppElements implements Parcelable {
    private String AppId;
    private String AppKey;
    private String AppSecret;

    public AppElements() {
    }

    public AppElements(String appId, String appKey, String appSecret) {
        AppId = appId;
        AppKey = appKey;
        AppSecret = appSecret;
    }

    protected AppElements(Parcel in) {
        AppId = in.readString();
        AppKey = in.readString();
        AppSecret = in.readString();
    }

    public static final Creator<AppElements> CREATOR = new Creator<AppElements>() {
        @Override
        public AppElements createFromParcel(Parcel in) {
            return new AppElements(in);
        }

        @Override
        public AppElements[] newArray(int size) {
            return new AppElements[size];
        }
    };

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getAppKey() {
        return AppKey;
    }

    public void setAppKey(String appKey) {
        AppKey = appKey;
    }

    public String getAppSecret() {
        return AppSecret;
    }

    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(AppId);
        dest.writeString(AppKey);
        dest.writeString(AppSecret);
    }

    @Override
    public String toString() {
        return "AppElements{" +
                "AppId='" + AppId + '\'' +
                ", AppKey='" + AppKey + '\'' +
                ", AppSecret='" + AppSecret + '\'' +
                '}';
    }
}

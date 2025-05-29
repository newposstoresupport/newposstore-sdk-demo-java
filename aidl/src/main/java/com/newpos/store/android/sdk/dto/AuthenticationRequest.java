package com.newpos.store.android.sdk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @ClassName : AuthenticationRequest
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/26-17:14
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AuthenticationRequest implements Parcelable {
    /**
     * Reserved fields for extension use
     */
    private String remark;
    /**
     * The clientID set by the RKI client application
     */
    private String clientId;

    public AuthenticationRequest(){

    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    protected AuthenticationRequest(Parcel in) {
        remark = in.readString();
        clientId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(remark);
        dest.writeString(clientId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenticationRequest> CREATOR = new Creator<AuthenticationRequest>() {
        @Override
        public AuthenticationRequest createFromParcel(Parcel in) {
            return new AuthenticationRequest(in);
        }

        @Override
        public AuthenticationRequest[] newArray(int size) {
            return new AuthenticationRequest[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "remark='" + remark + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}

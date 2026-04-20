package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : ParamVerify
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/23-17:29
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamVerify {
    public String pushTaskId;
    public String messageId;
    public String state;
    public String result;

    @NonNull
    @Override
    public String toString() {
        return "ParamVerify{" +
                "pushTaskId='" + pushTaskId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", state='" + state + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}

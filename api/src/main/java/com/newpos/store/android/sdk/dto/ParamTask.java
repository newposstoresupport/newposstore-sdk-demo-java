package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : ParamTask
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/22-16:27
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class ParamTask {
    public String pushTaskId;
    public String messageId;

    @NonNull
    @Override
    public String toString() {
        return "ParamTask{" +
                "pushTaskId='" + pushTaskId + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}

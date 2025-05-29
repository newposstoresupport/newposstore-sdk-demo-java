package com.newpos.store.android.sdk.dto;

import com.google.gson.JsonArray;

/**
 * @ClassName : QueryApp
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/18-15:59
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppQuery {
    public String packageName;
    public int verCode;
    public String verName;
    public JsonArray attachFiles;
}

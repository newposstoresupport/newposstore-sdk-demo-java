package com.newpos.store.android.sdk.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.newpos.store.android.sdk.AidlConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : BaseUtils
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-11:40
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class BaseUtils {

    public static final int VERSION_0116 = 2;

    public static <T> String toJson(T t){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(String.class, new StringNullAdapter())
                .create();
        return gson.toJson(t);
    }

    public static <T> T toObject(String json, Class<T> cls){
        Gson gson = new Gson();
        return gson.fromJson(json, cls);
    }

    public static <T> List<T> toObject(JsonArray json, Class<T> cls){
        List<T> list = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement element : json){
            list.add(gson.fromJson(element, cls));
        }
        return list;
    }

    public static <T> JsonArray buildJsonArray(List<T> list){
        JsonArray array = new JsonArray();
        for (T t : list){
            JsonObject jsonObject = new JsonParser()
                    .parse(toJson(t))
                    .getAsJsonObject();
            array.add(jsonObject);
        }
        return array;
    }

    public static boolean is0116NewStore(Context context){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(AidlConstant.NEWSTORE_PACKAGE_NAME, 0);
            long versionCode = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageInfo.getLongVersionCode();
            }else {
                versionCode = packageInfo.versionCode;
            }
            BaseLog.d("versionCode:"+versionCode);
            BaseLog.d("versionName:"+packageInfo.versionName);
            return versionCode <= VERSION_0116;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}

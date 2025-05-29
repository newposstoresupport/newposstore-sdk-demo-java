package com.newpos.store.android.sdk.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
}

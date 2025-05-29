package com.newpos.store.android.sdk.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * @ClassName : SPreference
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-8:57
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class SPreference {
    private static final String SP_NAME = "store_sdk_sp";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final class PreferenceHolder {
        static final SPreference preference = new SPreference();
    }

    public static SPreference I(){
        return PreferenceHolder.preference;
    }

    public void init(Context context){
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean putString(String key, String value){
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key, null);
    }

    public boolean putInt(String key, int value){
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key){
        return sharedPreferences.getInt(key, -1);
    }

    public int getInt(String key, int defaultValue){
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean putLong(String key, long value){
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key){
        return sharedPreferences.getLong(key, -1);
    }

    public long getLong(String key, long defaultValue){
        return sharedPreferences.getLong(key, defaultValue);
    }

    public boolean putFloat(String key, float value){
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float getFloat(String key){
        return sharedPreferences.getFloat(key, -1);
    }

    public float getFloat(String key, float defaultValue){
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public boolean putBoolean(String key, boolean value){
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue){
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean putObject(String key, Object o){
        Gson gson = new Gson();
        editor.putString(key, gson.toJson(o));
        return editor.commit();
    }

    public <T> T getObject(String key, Class<T> clz){
        String value = getString(key);
        if(TextUtils.isEmpty(value)){
            return null;
        }
        return new Gson().fromJson(value, clz);
    }

    public boolean remove(String key){
        return editor.remove(key).commit();
    }

    public boolean clear(){
        return editor.clear().commit();
    }
}

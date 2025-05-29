package com.newpos.store.android.sdk.base;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @ClassName : StringNullAdapter
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/9/12-11:17
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class StringNullAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        //attention 字段为null或者""不进行序列化
        if (value == null || value.isEmpty()){
            out.nullValue();
            return;
        }
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
//            if (in.peek() == JsonToken.NULL){
//                in.nextNull();
//                return "";
//            }
        return in.nextString();
    }
}

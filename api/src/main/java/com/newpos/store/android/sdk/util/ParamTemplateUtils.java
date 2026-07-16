package com.newpos.store.android.sdk.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 参数模板占位符替换工具。
 * 占位符格式：#{变量名}，按字面量匹配，变量名可含正则特殊字符（如 *）。
 */
public final class ParamTemplateUtils {

    private ParamTemplateUtils() {
    }

    /**
     * 将 content 中所有 #{key} 替换为 values 中对应字符串值。
     */
    public static String replacePlaceholders(String content, JsonObject values) {
        if (content == null || content.isEmpty() || values == null || values.size() == 0) {
            return content;
        }
        String result = content;
        for (String key : values.keySet()) {
            if (key == null) {
                continue;
            }
            JsonElement element = values.get(key);
            String value = element == null || element.isJsonNull() ? "" : element.getAsString();
            result = result.replace(buildPlaceholder(key), value);
        }
        return result;
    }

    static String buildPlaceholder(String key) {
        return "#{" + key + "}";
    }
}

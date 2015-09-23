package com.dennisxu.lib.core.entity;

import android.text.TextUtils;

import com.dennisxu.lib.core.exception.ParseException;

/**
 * 返回数据实体类
 *
 * @author xuwei19
 * @date 2014年11月26日 上午11:50:21
 */
public class Entity<T> implements KeepAttr{
    public Entity<T> fromJson(String jsonStr) throws ParseException {
        return this;
    }

    public static class JSONType {
        public static final int JSON_TYPE_ERROR = -1;
        public static final int JSON_TYPE_OBJECT = 0;
        public static final int JSON_TYPE_ARRAY = 1;

        private static int getJSONType(String json) {
            if (TextUtils.isEmpty(json))
                return JSON_TYPE_ERROR;

            char[] strChar = json.substring(0, 1).toCharArray();
            char firstChar = strChar[0];
            if ('{' == firstChar) {
                return JSON_TYPE_OBJECT;
            } else if ('[' == firstChar) {
                return JSON_TYPE_ARRAY;
            } else {
                return JSON_TYPE_ERROR;
            }
        }

        public static boolean isJsonOject(String json){
            return getJSONType(json) == JSON_TYPE_OBJECT;
        }

        public static boolean isJsonArray(String json){
            return getJSONType(json) == JSON_TYPE_ARRAY;
        }
    }
}

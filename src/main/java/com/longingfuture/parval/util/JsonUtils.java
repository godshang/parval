package com.longingfuture.parval.util;

public class JsonUtils {

    public static enum JsonType {
        UNKNOWN,

        OBJECT,

        ARRAY;
    }

    public static JsonType getJsonType(byte[] src) {
        if (src == null || src.length == 0) {
            return JsonType.UNKNOWN;
        }
        byte b = src[0];
        switch (b) {
        case '{':
            return JsonType.OBJECT;
        case '[':
            return JsonType.ARRAY;
        }
        return JsonType.UNKNOWN;
    }

}

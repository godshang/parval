package com.longingfuture.parval.common;

import java.util.HashMap;
import java.util.Map;

public class ErrorConstant {

    private final static Map<Integer, String> msgMap = new HashMap<Integer, String>();

    public final static int UNKNOW_ERROR = -1;
    public final static int READ_DATA_ERROR = 1;
    public final static int FORMAT_ERROR = 2;

    static {
        addToMsgMap(UNKNOW_ERROR, "未知错误");
        addToMsgMap(READ_DATA_ERROR, "读取数据失败");
        addToMsgMap(FORMAT_ERROR, "数据格式错误");
    }

    protected static void addToMsgMap(Integer code, String msg) {
        if (msgMap.get(code) != null) {
            throw new IllegalArgumentException("code already existed in msgMap. code:" + code + ",oldsmg:"
                    + msgMap.get(code) + ",newmsg=" + msg);
        }
        msgMap.put(code, msg);
    }

    public static String getMsg(Integer code) {
        return msgMap.get(code);
    }
}

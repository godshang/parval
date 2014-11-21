package com.longingfuture.parval.util;

import java.math.BigDecimal;

public class ValidateUtils {

    /**
     * 判断是否为全角字符或中文字符
     * 
     * @param c
     * @return
     */
    public static boolean isChineseOrFullWidthCharacter(char c) {
        if (("" + c).getBytes().length > 1) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串的字节数是否符合要求
     * 
     * @param string
     * @param min
     * @param max
     * @return
     */
    public static boolean isStringBytesLengthRight(String string, int min, int max) {
        int count = 0;
        if (string != null) {
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (isChineseOrFullWidthCharacter(chars[i])) {
                    count += 2;
                } else {
                    count += 1;
                }
            }
        }
        if (count >= min && count <= max) {
            return true;
        }
        return false;
    }

    /**
     * 判断金额是否在给定区间内
     * @param num
     * @param min
     * @param max
     * @return
     */
    public static boolean isNumberRight(BigDecimal num, BigDecimal min, BigDecimal max) {
        if (num.compareTo(min) >= 0 && num.compareTo(max) <= 0) {
            return true;
        }
        return false;
    }

}

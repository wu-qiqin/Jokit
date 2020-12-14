package com.hujinwen.utils;

/**
 * Created by hu-jinwen on 2020/7/22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 字符串驼峰转下划线
     */
    public static String humpToUnderline(String str) {
        if (isBlank(str)) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     */
    public static String underlineToHump(String str) {
        if (isBlank(str)) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        boolean needChange = false;
        for (char c : str.toCharArray()) {
            if (needChange) {
                sb.append(Character.toUpperCase(c));
                needChange = false;
                continue;
            }
            if (c == '_') {
                needChange = true;
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

}

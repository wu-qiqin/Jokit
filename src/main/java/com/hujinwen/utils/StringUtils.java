package com.hujinwen.utils;

import java.util.List;

/**
 * Created by hu-jinwen on 2020/7/22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final String NUMBER_STR = "0123456789";

    public static final char[] NUMBER_CHAR_ARRAY = NUMBER_STR.toCharArray();

    public static final List<Character> NUMBER_CHAR_LIST = ArrayUtils.asList(NUMBER_CHAR_ARRAY);

    public static final String UPPER_LETTER_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final char[] UPPER_LETTER_CHAR_ARRAY = UPPER_LETTER_STR.toCharArray();

    public static final List<Character> UPPER_LETTER_CHAR_LIST = ArrayUtils.asList(UPPER_LETTER_CHAR_ARRAY);

    public static final String LOWER_LETTER_STR = "abcdefghijklmnopqrstuvwxyz";

    public static final char[] LOWER_LETTER_CHAR_ARRAY = LOWER_LETTER_STR.toCharArray();

    public static final List<Character> LOWER_LETTER_CHAR_LIST = ArrayUtils.asList(LOWER_LETTER_CHAR_ARRAY);


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

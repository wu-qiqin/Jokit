package com.hujinwen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by hujinwen on 2019/2/13
 * <p>
 * 字节长度转换工具
 */
public class ByteLenUtils {
    private static final List<BytePattern> PATTERN_LIST = new ArrayList<>();

    static {
        PATTERN_LIST.add(new BytePattern(Pattern.compile("(?<NUM>\\d+(\\.\\d+)?)G"), ByteUnit.GB));
        PATTERN_LIST.add(new BytePattern(Pattern.compile("(?<NUM>\\d+(\\.\\d+)?)M"), ByteUnit.MB));
        PATTERN_LIST.add(new BytePattern(Pattern.compile("(?<NUM>\\d+(\\.\\d+)?)K"), ByteUnit.KB));
        PATTERN_LIST.add(new BytePattern(Pattern.compile("(?<NUM>\\d+(\\.\\d+)?)B"), ByteUnit.Byte));
    }

    /**
     * 字符串转化为Byte
     */
    public static long toByte(String str) {
        return (long) extractLength(str);
    }

    /**
     * 字符串转化为KB
     * (截取两位小数)
     */
    public static double toKb(String str) {
        double length = extractLength(str);
        return (long) (length / ByteUnit.KB * 100) / 100.0;
    }

    /**
     * 字符串转化为MB
     * (截取两位小数)
     */
    public static double toMb(String str) {
        double length = extractLength(str);
        return (long) (length / ByteUnit.MB * 100) / 100.0;
    }

    /**
     * 字符串转化为GB
     * (截取两位小数)
     */
    public static double toGb(String str) {
        double length = extractLength(str);
        return (long) (length / ByteUnit.GB * 100) / 100.0;
    }

    /**
     * 字符串转化为tb
     * (截取两位小数)
     */
    public static double toTb(String str) {
        double length = extractLength(str);
        return (long) (length / ByteUnit.TB * 100) / 100.0;
    }

    /**
     * 抽取内容长度
     */
    private static double extractLength(String str) {
        double result = 0L;
        for (BytePattern bytePattern : PATTERN_LIST) {
            Matcher matcher = bytePattern.pattern.matcher(str);
            if (matcher.find()) {
                result = Double.parseDouble(matcher.group("NUM")) * bytePattern.unit;
                break;
            }
        }
        return result;
    }

    private static class BytePattern {
        final Pattern pattern;
        final Long unit;

        BytePattern(Pattern pattern, Long unit) {
            this.pattern = pattern;
            this.unit = unit;
        }
    }

    private static class ByteUnit {
        static final Long Byte = 1L;
        static final Long KB = 1024L;
        static final Long MB = 1024L * 1024L;
        static final Long GB = MB * 1024L;
        static final Long TB = GB * 1024L;
    }

}

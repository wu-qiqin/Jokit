package com.hujinwen.utils;

import com.hujinwen.entity.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joe on 2020/5/13
 * <p>
 * 时间自动抽取工具类
 */
public class TimeUtils {
    private static final Logger logger = LogManager.getLogger(TimeUtils.class);

    private static ThreadLocal<SimpleTimeFormat> timeFormatHolder = ThreadLocal.withInitial(SimpleTimeFormat::new);

    private static List<TimeFormat> normalFormat = new ArrayList<>();

    static {  // TODO *** 规则添加、精准时间放前面，分类放好 ***
        // 时分秒
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+时\\d+分\\d+秒"), "hh时mm分ss秒"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+:\\d+:\\d+"), "hh:mm:ss"));
        // 时分
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+h\\s*\\d+mn"), "hh'h'mm'mn'"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+小时\\d+分钟"), "hh小时mm分钟"));
        // 分秒
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+分钟\\s*\\d+\\s*秒"), "mm分钟ss秒"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+:\\d+"), "mm:ss"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+'\\d+\""), "mm'ss\""));
        // 分
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+\\s*分"), "mm分"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+\\s*m"), "mm'm'"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+\\s*Mi"), "mm'Mi'"));
        // 秒
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+\\s*秒"), "ss秒"));
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+(\\.\\d+)?"), "ss"));
        // 毫秒
        normalFormat.add(new TimeFormat(Pattern.compile("\\d+ms"), "SS'ms'"));
    }

    /**
     * 时间转化
     *
     * @param timeStr 原始时间格式
     */
    public static String convertTime(String timeStr) {
        return convertTime(timeStr, Constants.TimeFormat.DEFAULT_FORMAT);
    }

    /**
     * 时间转化
     *
     * @param timeStr 原始时间格式转换成秒
     */
    public static String convertTimeSeconds(String timeStr) {
        return convertTime(timeStr, "ss");
    }

    /**
     * 时间转化
     *
     * @param timeStr       原始时间字符串
     * @param targetPattern 输出格式
     */
    public static String convertTime(String timeStr, String targetPattern) {
        SimpleTimeFormat timeFormat = timeFormatHolder.get();
        Long timestamp = extractTime(timeStr);
        if (timestamp == null) {
            return "";
        }
        timeFormat.applyPattern(targetPattern);
        return timeFormat.format(timestamp);
    }

    /**
     * 时间转化
     *
     * @param timestamp 待转化时间戳（毫秒）
     */
    public static String convertTime(Long timestamp) {
        return convertTime(timestamp, Constants.TimeFormat.DEFAULT_FORMAT);
    }

    /**
     * 时间转化
     *
     * @param timestamp     待转化时间戳（毫秒）
     * @param targetPattern 输出格式
     */
    public static String convertTime(Long timestamp, String targetPattern) {
        SimpleTimeFormat timeFormat = timeFormatHolder.get();
        timeFormat.applyPattern(targetPattern);
        return timeFormat.format(timestamp);
    }

    /**
     * 时间抽取
     *
     * @param timeStr 原始时间字符串
     * @return 抽取到的时间戳
     */
    public static Long extractTime(String timeStr) {
        Long result = null;
        SimpleTimeFormat timeFormat = timeFormatHolder.get();

        for (TimeFormat timePattern : normalFormat) {
            Matcher matcher = timePattern.regExp.matcher(timeStr);
            if (matcher.find()) {
                timeFormat.applyPattern(timePattern.pattern);
                result = timeFormat.parse(matcher.group(0));
                break;
            }
        }
        return result;
    }

    private static class TimeFormat {
        // 抽取正则表达式
        private Pattern regExp;
        // 时间格式pattern
        private String pattern;

        TimeFormat(Pattern regExp, String pattern) {
            this.regExp = regExp;
            this.pattern = pattern;
        }
    }
}

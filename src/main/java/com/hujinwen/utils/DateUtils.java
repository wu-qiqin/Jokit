package com.hujinwen.utils;

import com.hujinwen.entity.TwoTuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by joe on 2018/9/28
 * <p>
 * 日期自动抽取工具类
 */
public class DateUtils {
    private static final Logger logger = LogManager.getLogger(DateUtils.class);

    private static final ThreadLocal<SimpleDateFormat> dateFormatHolder = ThreadLocal.withInitial(SimpleDateFormat::new);

    private static final ThreadLocal<Calendar> calendarHolder = ThreadLocal.withInitial(Calendar::getInstance);

    private static final List<DateFormat> DATE_FORMAT_LIST = new ArrayList<>();

    private static final List<DateFormat> BEFORE_AFTER_FORMAT_LIST = new ArrayList<>();

    private static final List<DateFormat> EXACT_DATE_FORMAT_LIST = new ArrayList<>();

    private static final Pattern TIME_STAMP_PATTERN = Pattern.compile("\\d{10,13}");

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {  // FIXME +++++> 年份不可写成\\d{2,4}，会导致结果错误 <+++++
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "yyyy-MM-dd HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}"), "yyyy-MM-dd'T'HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "yyyy-MM-dd HH:mm"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}"), "yyyy-MM-dd"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}-\\d{1,2}/\\d{1,2}"), "yyyy-MM/dd"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}[\\s\\p{Zs}]+\\d{4}-\\d{1,2}"), "dd yyyy-MM"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}-\\d{1,2}"), "yyyy-MM"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{2}-\\d{1,2}-\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "yy-MM-dd HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{2}-\\d{1,2}-\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "yy-MM-dd HH:mm"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{2}-\\d{1,2}-\\d{1,2}"), "yy-MM-dd"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}-\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "MM-dd HH:mm"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}-\\d{1,2}"), "MM-dd"));

        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}\\.\\d{1,2}\\.\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "yyyy.MM.dd HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}\\.\\d{1,2}\\.\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "yyyy.MM.dd HH:mm"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}\\.\\d{1,2}\\.\\d{1,2}"), "yyyy.MM.dd"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.\\d{4}"), "MM.dd.yyyy"));

        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "yyyy年MM月dd日 HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日[\\s\\p{Zs}]+\\d{1,2}时\\d{1,2}分\\d{1,2}"), "yyyy年MM月dd日 HH时mm分ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日[\\s\\p{Zs}]+\\d{1,2}时\\d{1,2}分\\d{1,2}"), "yyyy年MM月dd日 HH时mm分ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}:\\d{1,2}"), "yyyy年MM月dd日HH:mm"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "yyyy年MM月dd日 HH:mm"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}"), "yyyy年MM月dd"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}月\\d{1,2}日晚\\d{1,2}点\\d{1,2}分"), "MM月dd日晚HH点mm分"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}月\\d{1,2}日[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "MM月dd日 HH:mm"));

        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "yyyy/MM/dd HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "MM/dd/yyyy HH:mm:ss"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}"), "yyyy/MM/dd"));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{1,2}/\\d{1,2}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}"), "MM/dd HH:mm"));

        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d+\\.\\d+, \\d{4} \\d{2}:\\d{2}:\\d{2}"), "MM.dd, yyyy HH:mm:ss"));

        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("\\d{4}\\d{1,2}\\d{1,2}"), "yyyyMMdd"));

        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Mon|Tue|Wed|Thu|Fri|Sat|Sun) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec) \\d{2} \\d{2}:\\d{2}:\\d{2} [A-Za-z]+ \\d{4}"), "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Mon|Tue|Wed|Thu|Fri|Sat|Sun), \\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)-\\d{4} \\d{2}:\\d{2}:\\d{2} [a-zA-Z]+"), "EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.US));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Mon|Tue|Wed|Thu|Fri|Sat|Sun), \\d{2} (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec) \\d{4} \\d{2}:\\d{2}:\\d{2} [a-zA-Z]+"), "EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Mon|Tue|Wed|Thu|Fri|Sat|Sun), \\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)-\\d{2} \\d{2}:\\d{2}:\\d{2} [a-zA-Z]+"), "EEE, dd-MMM-yy HH:mm:ss zzz", Locale.US));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)[a-z]+\\.\\d{1,2},[\\s\\p{Zs}]+\\d{4}[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}:\\d{1,2}"), "MMM.dd, yyyy HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)[a-z]+[\\s\\p{Zs}]+\\d{1,2},[\\s\\p{Zs}]*\\d{4}[\\s\\p{Zs}]+at[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}[\\s\\p{Zs}]*(AM|PM|am|pm)"), "MMM dd, yyyy 'at' hh:mm a", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec)[a-z]+[\\s\\p{Zs}]+\\d{1,2}[\\s\\p{Zs}]+at[\\s\\p{Zs}]+\\d{1,2}:\\d{1,2}[\\s\\p{Zs}]*(AM|PM|am|pm)"), "MMM dd 'at' hh:mm a", Locale.ENGLISH));

        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*秒钟?前"), Calendar.SECOND));
        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*分钟前"), Calendar.MINUTE));
        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*小时前"), Calendar.HOUR_OF_DAY));
        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*天前"), Calendar.DAY_OF_YEAR));
        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*周前"), Calendar.WEEK_OF_MONTH));
        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*个?月前"), Calendar.MONTH));
        BEFORE_AFTER_FORMAT_LIST.add(new DateFormat(Pattern.compile("(\\d+)\\s*年前"), Calendar.YEAR));

        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("今天\\s?(\\d{2}:\\d{2})"), "yyyy-MM-dd HH:mm", ExactDate.TODAY));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("今天"), "yyyy-MM-dd", ExactDate.TODAY));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("昨天\\s?(\\d{2}:\\d{2})"), "yyyy-MM-dd HH:mm", ExactDate.YESTERDAY));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("昨天"), "yyyy-MM-dd", ExactDate.YESTERDAY));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("明天\\s?(\\d{2}:\\d{2})"), "yyyy-MM-dd HH:mm", ExactDate.TOMORROW));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("明天"), "yyyy-MM-dd", ExactDate.TOMORROW));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("前天\\s?(\\d{2}:\\d{2})"), "yyyy-MM-dd HH:mm", ExactDate.AFTER_YESTERDAY));
        EXACT_DATE_FORMAT_LIST.add(new DateFormat(Pattern.compile("前天"), "yyyy-MM-dd", ExactDate.AFTER_YESTERDAY));
    }

    /**
     * 从字符串中提取规范时间（根据默认格式）
     */
    public static String convertTime(long timestamp) {
        return convertTime(String.valueOf(timestamp));
    }

    /**
     * 从字符串中提取规范时间（根据默认格式）
     */
    public static String convertTime(String timeStr) {
        return convertTime(timeStr, true);
    }

    /**
     * 从字符串中提取规范时间（根据默认格式）
     */
    public static String convertTime(String timeStr, boolean allowedFuture) {
        return convertTime(timeStr, DEFAULT_FORMAT, allowedFuture);
    }

    /**
     * 从字符串中提取规范的时间（根据所给时间格式）
     */
    public static String convertTime(String timeStr, String timeFormat, boolean allowedFuture) {
        return convert(timeStr, timeFormat, allowedFuture);
    }

    /**
     * 解析字符串，转化为时间
     */
    public static Date parse(String timeStr, String dateFormat) {
        return parse(timeStr, dateFormat, null);
    }

    private static Date parse(String timeStr, String dateFormat, SimpleDateFormat simpleDateFormat) {
        Date date = null;
        try {
            if (simpleDateFormat == null) {
                simpleDateFormat = dateFormatHolder.get();
                simpleDateFormat.applyPattern(dateFormat);
            }
            date = simpleDateFormat.parse(timeStr);
        } catch (Exception e) {
            logger.error(MessageFormat.format("+++++>> Error while parse time string! str --> {0}\t\tformat --> {1}", timeStr, dateFormat), e);
        }
        return date;
    }

    /**
     * 字符串转时间
     */
    private static String convert(String timeStr, String dateFormat, boolean allowedFuture) {
        Date date = extractDate(timeStr);
        if (date != null && (allowedFuture || date.before(new Date()))) {
            if (dateFormat == null) {
                dateFormat = DEFAULT_FORMAT;
            }
            return format(date, dateFormat);
        }
        return "";
    }

    /**
     * 使用默认格式 格式化 Date
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    /**
     * 根据 指定格式 格式化 Date
     */
    public static String format(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = dateFormatHolder.get();
        simpleDateFormat.applyPattern(dateFormat);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据指定格式格式化 时间字符串
     *
     * @param timeStr      时间字符串
     * @param sourceFormat 时间字符串格式
     * @param targetFormat 期望结果格式
     */
    public static String format(String timeStr, String sourceFormat, String targetFormat) {
        Date date = parse(timeStr, sourceFormat);
        return format(date, targetFormat);
    }

    /**
     * 从字符串中 抽取 Date
     */
    public static Date extractDate(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return null;
        }
        Date date;
        date = extractTimestamp(timeStr);
        if (date == null) {
            date = extractNormal(timeStr);
        }
        if (date == null) {
            date = extractBeforeAfter(timeStr);
        }
        if (date == null) {
            date = extractExactDate(timeStr);
        }
        return date;
    }

    /**
     * 正常模式提取
     */
    private static Date extractNormal(String timeStr) {
        if (StringUtils.isEmpty(timeStr)) {
            return null;
        }
        final TwoTuple<Matcher, DateFormat> normalMatcher = findNormalMatcher(timeStr);

        if (normalMatcher == null) {
            return null;
        }
        final Matcher matcher = normalMatcher.first;
        final DateFormat dateFormat = normalMatcher.second;

        Date date;
        if (dateFormat.locale != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.timeFormat, dateFormat.locale);
            date = parse(matcher.group(0), dateFormat.timeFormat, simpleDateFormat);
        } else {
            date = parse(matcher.group(0), dateFormat.timeFormat);
        }
        // 没有年份的时间统一成当前年份
        if (date != null && !dateFormat.timeFormat.contains("yy")) {
            Calendar calendar = calendarHolder.get();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            calendar.setTime(date);
            calendar.set(Calendar.YEAR, year);
            date = calendar.getTime();
        }
        return date;
    }

    /**
     * 几年前、几个月前、几天前、几小时前、几分钟前 日期提取
     */
    private static Date extractBeforeAfter(String timeStr) {
        Date date = null;
        for (DateFormat dateFormat : BEFORE_AFTER_FORMAT_LIST) {
            Matcher matcher = dateFormat.regExp.matcher(timeStr);
            if (matcher.find()) {
                Calendar calendar = calendarHolder.get();
                calendar.setTime(new Date());
                calendar.add(dateFormat.calendarField, Integer.parseInt(MessageFormat.format("-{0}", matcher.group(1))));
                beforeAfterTimeHandle(calendar, dateFormat.calendarField);
                date = calendar.getTime();
                break;
            }
        }
        return date;
    }

    /**
     * 从确切的日期字符串 提取日期
     */
    private static Date extractExactDate(String timeStr) {
        Date date = null;
        for (DateFormat dateFormat : EXACT_DATE_FORMAT_LIST) {
            Matcher matcher = dateFormat.regExp.matcher(timeStr);
            if (matcher.find()) {
                Calendar calendar = calendarHolder.get();
                calendar.setTime(new Date());
                switch (dateFormat.exactDate) {
                    case YESTERDAY:
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case TOMORROW:
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case AFTER_YESTERDAY:
                        calendar.add(Calendar.DAY_OF_MONTH, -2);
                        break;
                    default:
                        break;
                }
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String timeStr2 = MessageFormat.format("{0}-{1}-{2} {3}",
                        String.valueOf(calendar.get(Calendar.YEAR)),
                        month < 10 ? "0" + month : month,
                        day < 10 ? "0" + day : day,
                        matcher.groupCount() > 0 ? matcher.group(1) : "").trim();
                date = parse(timeStr2, dateFormat.timeFormat);
                break;
            }
        }
        return date;
    }

    /**
     * 时间戳提取
     */
    private static Date extractTimestamp(String timeStr) {
        Date date = null;
        Matcher matcher = TIME_STAMP_PATTERN.matcher(timeStr);
        if (matcher.find()) {
            long timestamp = Long.parseLong(matcher.group(0));
            if (timestamp < 10000000000L) {
                timestamp *= 1000;
            }
            date = new Date(timestamp);
        }
        return date;
    }

    /**
     * 找到匹配上的matcher（正常模式）
     */
    private static TwoTuple<Matcher, DateFormat> findNormalMatcher(String timeStr) {
        for (DateFormat dateFormat : DATE_FORMAT_LIST) {
            Matcher matcher = dateFormat.regExp.matcher(timeStr);
            if (matcher.find()) {
                return new TwoTuple<>(matcher, dateFormat);
            }
        }
        return null;
    }

    /**
     * 几天前、几小时前……时间特殊处理
     */
    private static void beforeAfterTimeHandle(Calendar calendar, int timeField) {
        switch (timeField) {
            case Calendar.DAY_OF_YEAR:
            case Calendar.WEEK_OF_MONTH:
            case Calendar.MONTH:
            case Calendar.YEAR:
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.HOUR_OF_DAY:
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.MINUTE:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
        }
    }

    /**
     * 自动递（增/减）日期中的时间字符串
     *
     * @param dateStr 包含时间的字符串
     * @param field   参考Calender Field，例：Calendar.DAY_OF_MONTH
     * @param offset  偏移量（正整数 / 负整数）
     */
    public static String addDate(String dateStr, int field, int offset) {
        final TwoTuple<Matcher, DateFormat> normalMatcher = findNormalMatcher(dateStr);

        if (normalMatcher == null) {
            return "";
        }
        final Matcher matcher = normalMatcher.first;
        final DateFormat dateFormat = normalMatcher.second;

        final Date currentDate = parse(matcher.group(0), dateFormat.timeFormat);

        final Calendar calendar = calendarHolder.get();
        calendar.setTime(currentDate);
        calendar.add(field, offset);

        String newDateStr = format(calendar.getTime(), dateFormat.timeFormat);
        return dateStr.substring(0, matcher.start(0)) + newDateStr + dateStr.substring(matcher.end(0));
    }

    /**
     * 计算Date的偏移量（几天前/后，几年前/后，几月前/后）
     *
     * @param date   需要计算的日期
     * @param field  参考Calender Field，例：Calendar.DAY_OF_MONTH
     * @param offset 偏移量（正整数 / 负整数）
     */
    public static Date addDate(Date date, int field, int offset) {
        Calendar calendar = calendarHolder.get();
        calendar.setTime(date);
        calendar.add(field, offset);
        return calendar.getTime();
    }

    /**
     * 计算几天前、几天后
     *
     * @Deprecated 建议使用 addDate
     */
    @Deprecated
    public static Date addDay(Date date, int offset) {
        return addDate(date, Calendar.DAY_OF_MONTH, offset);
    }

    /**
     * 计算几周前、几周后
     *
     * @Deprecated 建议使用 addDate
     */
    @Deprecated
    public static Date addWeek(Date date, int offset) {
        return addDate(date, Calendar.WEEK_OF_MONTH, offset);
    }

    /**
     * 日期格式类
     */
    private static class DateFormat {

        private Pattern regExp;  // 时间正则
        private String timeFormat;  // 时间格式
        private Locale locale;  // 区域
        private ExactDate exactDate;  // 确切的日期
        private int calendarField;  // calendar时间单位

        private DateFormat(Pattern regExp, String timeFormat) {
            this.regExp = regExp;
            this.timeFormat = timeFormat;
        }

        private DateFormat(Pattern regExp, String timeFormat, Locale locale) {
            this(regExp, timeFormat);
            this.locale = locale;
        }

        private DateFormat(Pattern regExp, int calendarField) {
            this.regExp = regExp;
            this.calendarField = calendarField;
        }

        private DateFormat(Pattern regExp, String timeFormat, ExactDate exactDate) {
            this(regExp, timeFormat);
            this.exactDate = exactDate;
        }

    }

    private enum ExactDate {
        TODAY, YESTERDAY, TOMORROW, AFTER_YESTERDAY
    }

}

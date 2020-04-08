package com.hujinwen.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

/**
 * Created by joe on 2020/4/8
 * <p>
 * 随机数相关工具类
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();

    /**
     * 生成36位 uuid（随机不重复的字符串）
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成32位 uuid（随机不重复的字符串）
     */
    public static String uuid32() {
        return uuid().replace("-", "");
    }

    /**
     * 从数组中随机选择
     */
    public static <T> T randomChoice(T[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return null;
        }
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * 从list中随机选择
     */
    public static <T> T randomChoice(List<T> list) {
        if (ObjectUtils.isEmpty(list)) {
            return null;
        }
        return list.get(RANDOM.nextInt(list.size()));
    }

    public static <T> T randomChoice(Iterable<T> iterable) {
        final Iterator<T> iterator = iterable.iterator();
        final Set<T> cacheSet = new HashSet<>();
        while (iterator.hasNext()) {
            T next = iterator.next();
            cacheSet.add(next);
        }
        return randomChoice(cacheSet);
    }

    /**
     * 从collection中随机选择一个
     */
    public static <T> T randomChoice(Collection<T> collection) {
        if (ObjectUtils.isEmpty(collection)) {
            return null;
        }
        final int index = RANDOM.nextInt(collection.size());
        int i = 0;
        for (T t : collection) {
            if (i++ == index) {
                return t;
            }
        }
        return null;
    }
}

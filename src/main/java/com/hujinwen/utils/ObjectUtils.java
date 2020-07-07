package com.hujinwen.utils;

import java.io.IOException;

/**
 * Created by joe on 2020/7/7
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /**
     * 深拷贝
     * （这里使用Jackson转换来做拷贝）
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T obj) throws IOException {
        final String objContent = JsonUtils.toString(obj);
        return (T) JsonUtils.toObj(objContent, obj.getClass());
    }

}

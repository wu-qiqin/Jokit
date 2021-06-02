package com.hujinwen.utils;

import java.util.Objects;

/**
 * Created by hu-jinwen on 2021/6/2
 */
public class PathUtils {

    /**
     * 获取resource绝对路径
     */
    public static String getResourcePath() {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = contextCL == null ? FileUtils.class.getClassLoader() : contextCL;
        return Objects.requireNonNull(loader.getResource("")).getPath();
    }

}

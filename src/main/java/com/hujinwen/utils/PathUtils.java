package com.hujinwen.utils;

import java.util.Objects;

/**
 * Created by hu-jinwen on 2021/6/2
 */
public class PathUtils {

    /**
     * 获取resource绝对路径
     *
     * @Deprecated FIXME 将被废弃，打成jar包后，该方法将会出问题
     */
    @Deprecated
    public static String getResourcePath() {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = contextCL == null ? FileUtils.class.getClassLoader() : contextCL;
        return Objects.requireNonNull(loader.getResource("")).getPath();
    }

}

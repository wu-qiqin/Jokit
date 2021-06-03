package com.hujinwen.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by hu-jinwen on 2021/6/2
 */
public class ClassUtils extends org.apache.commons.lang3.ClassUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 斜线 pattern
     */
    private static final Pattern SLASH_PATTERN = Pattern.compile("[/\\\\]");

    public static Class<?>[] scanClass() {
        return scanClass(PathUtils.getResourcePath());
    }

    public static Class<?>[] scanClass(ClassFilter classFilter) {
        return scanClass(PathUtils.getResourcePath(), classFilter);
    }

    public static Class<?>[] scanClass(File rootFile) {
        return scanClass(rootFile, null);
    }

    public static Class<?>[] scanClass(File rootFile, ClassFilter classFilter) {
        return scanClass(rootFile, rootFile.getPath(), new HashSet<>(), classFilter);
    }

    public static Class<?>[] scanClass(String rootPath) {
        return scanClass(rootPath, null);
    }

    public static Class<?>[] scanClass(String rootPath, ClassFilter classFilter) {
        return scanClass(new File(rootPath), rootPath, new HashSet<>(), classFilter);
    }


    /**
     * 扫描class
     *
     * @param pathFile
     * @param rootPath
     * @param classNames
     * @param classFilter
     */
    private static Class<?>[] scanClass(File pathFile, String rootPath, Set<Class<?>> classNames, ClassFilter classFilter) {
        if (pathFile == null) {
            return classNames.toArray(new Class<?>[0]);
        }
        if (pathFile.isDirectory()) {
            final File[] files = pathFile.listFiles();
            if (files == null) {
                return classNames.toArray(new Class[0]);
            }
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    scanClass(childFile, rootPath, classNames, classFilter);
                } else {
                    if (childFile.getName().endsWith(".class")) {
                        final String classAbsPath = childFile.getPath();
                        String className0 = SLASH_PATTERN.matcher(classAbsPath.substring(rootPath.length())).replaceAll(".");
                        String className = className0.substring(0, className0.length() - 6);
                        Class<?> clazz;
                        try {
                            clazz = getClass(className);
                        } catch (ClassNotFoundException e) {
                            logger.error("Failed to scan and load class: {}", className, e);
                            continue;
                        }
                        if (classFilter != null && !classFilter.accept(clazz)) {
                            continue;
                        }
                        classNames.add(clazz);
                    }
                }
            }
        }
        return classNames.toArray(new Class[0]);
    }

    public interface ClassFilter {

        /**
         * 扫描项目中所有类时，对类属性的过滤
         */
        boolean accept(Class<?> clazz);
    }

}

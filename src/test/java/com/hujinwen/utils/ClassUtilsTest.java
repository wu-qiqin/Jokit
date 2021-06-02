package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

class ClassUtilsTest {

    @Test
    void test1() {
        final String resourcePath = PathUtils.getResourcePath();
        final Class<?>[] classes = ClassUtils.scanClass(resourcePath);
        System.out.println();
    }

}
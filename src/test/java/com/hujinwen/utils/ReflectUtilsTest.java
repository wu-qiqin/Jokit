package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

class ReflectUtilsTest {


    @Test
    void forceSet() throws NoSuchFieldException, IllegalAccessException {
        final TestClass test = new TestClass();

        ReflectUtils.forceSet(test, "testName", "TestName");
        System.out.println();
    }


    class TestClass {

        private final String testName = "";

    }


}
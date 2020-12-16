package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class ReflectUtilsTest {


    @Test
    void forceSet() throws NoSuchFieldException, IllegalAccessException {
        final TestClass test = new TestClass();

        ReflectUtils.forceSet(test, "testName", "TestName");
        System.out.println();
    }


    @Test
    void forceInvoke() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final TestClass testClass = new TestClass();
//        final Object name = ReflectUtils.forceInvoke(testClass, "getName");
        final Object name = ReflectUtils.forceInvoke(testClass, "getName", new Class[]{String.class}, new Object[]{""});
        System.out.println();
    }


    class TestClass {

        private final String testName = "TestName1";


        private String getName(String name) {
            return testName;
        }


    }


}
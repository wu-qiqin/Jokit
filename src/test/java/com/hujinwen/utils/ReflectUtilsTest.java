package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    class A extends B {

        public void aMethod() {

        }

        @Override
        public void bAbstractMethod() {

        }
    }

    abstract class B implements C {

        public void bMethod() {

        }

        @Override
        public void cMethod() {

        }

        public abstract void bAbstractMethod();

    }

    interface C {
        public void cMethod();
    }

    public static void main(String[] args) throws NoSuchMethodException {
        final Method[] methods = A.class.getMethods();
        final Method[] declaredMethods = A.class.getDeclaredMethods();
        final Method cMethod = A.class.getMethod("cMethod");
        final Method[] declaredMethods1 = ReflectUtils.getDeclaredMethods(A.class);

        System.out.println();
    }


}
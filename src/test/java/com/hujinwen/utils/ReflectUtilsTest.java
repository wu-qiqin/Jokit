package com.hujinwen.utils;

import com.hujinwen.entity.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ReflectUtilsTest {

    /**
     * 查找get set方法
     */
    @Test
    void findGetSetMethod() {
        final Field[] declaredFields = ReflectUtils.getDeclaredFields(Student.class);

        for (Field field : declaredFields) {
            final Method setMethod = ReflectUtils.findSetMethod(Student.class, field);
            final Method getMethod = ReflectUtils.findGetMethod(Student.class, field);
            System.out.println(setMethod.getName());
            System.out.println(getMethod.getName());
        }
    }




}
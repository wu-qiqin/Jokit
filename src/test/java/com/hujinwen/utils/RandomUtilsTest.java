package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

class RandomUtilsTest {

    @Test
    void test() {
        final String uuid = RandomUtils.uuid();
        final String str = RandomUtils.randomChoice(new HashSet<String>());
        final String str2 = RandomUtils.randomChoice(new String[22]);
    }

    @Test
    void test2() {
        final String s = RandomUtils.randomStr(32);
        System.out.println();
    }

}
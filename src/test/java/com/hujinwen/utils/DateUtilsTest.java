package com.hujinwen.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

class DateUtilsTest {
    private static final Logger logger = LogManager.getLogger(DateUtilsTest.class);

    @Test
    void test() {
        final String str = DateUtils.convertTime("10天前");
        final String str2 = DateUtils.convertTime("2020年3月4日");
        final String str3 = DateUtils.convertTime("昨天");
        final String str4 = DateUtils.convertTime("1996/09/09 23:12:12");
        System.out.println();
    }


}
package com.hujinwen.tools;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

class SimpleTimeFormatTest {

    @Test
    void test1() {
        final SimpleTimeFormat simpleTimeFormat = new SimpleTimeFormat();
        simpleTimeFormat.applyPattern("mm分ss秒");
        final Long time_ms = simpleTimeFormat.parse("23分12秒");

        simpleTimeFormat.applyPattern("ss秒");
        final String format = simpleTimeFormat.format(time_ms);
        System.out.println();

    }

}
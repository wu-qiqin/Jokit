package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

    /**
     * 驼峰和下划线相互转换测试
     */
    @Test
    void humpUnderlineChangeTest() {
        String name = "computerName";
        final String underlineName = StringUtils.humpToUnderline(name);
        final String humpName = StringUtils.underlineToHump(underlineName);
        System.out.println();
    }

}
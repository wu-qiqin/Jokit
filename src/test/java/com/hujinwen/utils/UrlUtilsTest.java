package com.hujinwen.utils;

import org.junit.jupiter.api.Test;

class UrlUtilsTest {

    @Test
    void test() {
        final String result = UrlUtils.absUrl("https://www.baidu.com/s?wd=%22%E6%90%9C%E7%B4%A2%22", "/s?wd=%22%E6%90%9C%E7%B4%A2%22&pn=10&oq=%22%E6%90%9C%E7%B4%A2%22&rsv_page=1");
        System.out.println();
    }

}
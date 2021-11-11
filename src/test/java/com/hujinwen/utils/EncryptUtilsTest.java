package com.hujinwen.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

class EncryptUtilsTest {

    @Test
    void test1() throws UnsupportedEncodingException, JsonProcessingException {

        final String result = EncryptUtils.md5("hello".getBytes(StandardCharsets.UTF_8), false);
        System.out.println();

    }

}
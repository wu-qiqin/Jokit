package com.hujinwen.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    void test() throws IOException {
        final JsonNode jsonNode = JsonUtils.toJsonNode("");

        final Object obj = JsonUtils.toObj("", Object.class);

        final String objStr = JsonUtils.toString(new Object());
    }

}
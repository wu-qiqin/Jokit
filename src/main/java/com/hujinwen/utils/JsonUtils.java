package com.hujinwen.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by joe on 2020/4/8
 * <p>
 * json相关的工具类
 */
public class JsonUtils {
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);

    private static final ObjectMapper OM = new ObjectMapper();

    public static ObjectNode newObjectNode() {
        return OM.createObjectNode();
    }

    public static ArrayNode newArrayNode() {
        return OM.createArrayNode();
    }

}

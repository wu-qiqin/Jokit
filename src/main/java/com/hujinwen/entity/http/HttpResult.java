package com.hujinwen.entity.http;

import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by joe on 2020/4/11
 * <p>
 * http结果保存对象
 */
public class HttpResult {
    private static final Logger logger = LogManager.getLogger(HttpResult.class);

    private Long contentLength;

    /**
     * 抽取字段
     */
    public void extractField(HttpClientContext context) {
        System.out.println();
    }

    /**
     * 抽取字段
     */
    public void extractField(ClassicHttpResponse response) {
        try {
            final Header header = response.getHeader(HttpConstants.CONTENT_LENGTH);
            if (header != null) {
                contentLength = Long.parseLong(header.getValue());
            }
        } catch (ProtocolException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public long getContentLength() {
        return contentLength;
    }

}

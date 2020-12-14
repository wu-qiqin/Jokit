package com.hujinwen.entity.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hu-jinwen on 2020/4/11
 * <p>
 * http结果保存对象
 */
public class HttpResult {
    private static final Logger logger = LogManager.getLogger(HttpResult.class);

    private int respCode;

    private Long contentLength;

    private String contentEncoding;

    private String contentType;

    /**
     * 抽取字段
     */
    public void extractField(HttpClientContext context) {
    }

    /**
     * 抽取字段
     */
    public void extractField(ClassicHttpResponse response) {
        try {
            final HttpEntity respEntity = response.getEntity();
            // Content-Length
            long contentLength = respEntity.getContentLength();
            if (contentLength < 0) {
                final Header header = response.getHeader(HttpConstants.CONTENT_LENGTH);
                if (header != null) {
                    contentLength = Long.parseLong(header.getValue());
                }
            }
            this.contentLength = contentLength;
            // Content-Type
            String contentType = respEntity.getContentType();
            if (StringUtils.isBlank(contentType)) {
                // TODO 补充处理
            }
            this.contentType = contentType;
            // Content-Encoding
            String contentEncoding = respEntity.getContentEncoding();
            if (StringUtils.isBlank(contentEncoding)) {
                // TODO 补充处理
            }
            this.contentEncoding = contentEncoding;
            // http resp code
            this.respCode = response.getCode();
        } catch (ProtocolException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public long getContentLength() {
        return contentLength;
    }

    public int getRespCode() {
        return respCode;
    }
}

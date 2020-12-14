package com.hujinwen.exceptions.minio;

/**
 * Created by hu-jinwen on 2020/3/20
 * <p>
 * MinIO put object exception
 */
public class MinioPutObjectException extends Exception {
    public MinioPutObjectException() {
    }

    public MinioPutObjectException(String message) {
        super(message);
    }

    public MinioPutObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioPutObjectException(Throwable cause) {
        super(cause);
    }

    public MinioPutObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

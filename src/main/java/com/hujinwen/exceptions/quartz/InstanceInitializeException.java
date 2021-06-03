package com.hujinwen.exceptions.quartz;

/**
 * Created by hu-jinwen on 2021/6/2
 * <p>
 * 缺少构造方法
 */
public class InstanceInitializeException extends RuntimeException {

    public InstanceInitializeException() {
        super();
    }

    public InstanceInitializeException(String message) {
        super(message);
    }

    public InstanceInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceInitializeException(Throwable cause) {
        super(cause);
    }

    protected InstanceInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

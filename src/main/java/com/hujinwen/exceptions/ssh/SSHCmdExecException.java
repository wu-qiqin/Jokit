package com.hujinwen.exceptions.ssh;

/**
 * Created by joe on 2020/7/27
 * <p>
 * ssh命令执行异常（执行失败）
 */
public class SSHCmdExecException extends Exception {
    public SSHCmdExecException() {
        super();
    }

    public SSHCmdExecException(String message) {
        super(message);
    }

    public SSHCmdExecException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHCmdExecException(Throwable cause) {
        super(cause);
    }

    protected SSHCmdExecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

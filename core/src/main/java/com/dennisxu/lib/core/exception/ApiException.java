package com.dennisxu.lib.core.exception;

/**
 * 处理服务器返回的错误信息
 * 
 * @author: xuyang
 * @date: 2014-8-24 上午10:24:50
 */
public class ApiException extends Exception {

    private static final long serialVersionUID = 6501425440406106625L;

    public APiErrorMessage APiErrorMessage;

    public ApiException() {
        super();
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(APiErrorMessage APiErrorMessage) {
        super(APiErrorMessage.errorMsg);
        this.APiErrorMessage = APiErrorMessage;
    }

    public ApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }
}

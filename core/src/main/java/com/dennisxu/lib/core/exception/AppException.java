package com.dennisxu.lib.core.exception;

/**
 * 处理通用一场，数据库、其他等等
 * 
 * @author: xuyang
 * @date: 2014-8-24 上午10:25:05
 */
public class AppException extends Exception {

    private static final long serialVersionUID = 3642565371520395270L;

    public AppException() {
        super();
    }

    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public AppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }

}

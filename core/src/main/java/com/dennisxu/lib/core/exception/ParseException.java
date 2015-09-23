package com.dennisxu.lib.core.exception;

/**
 * 处理解析时发生的异常
 * 
 * @author: xuyang
 * @date: 2014-8-24 上午10:25:05
 */
public class ParseException extends Exception {

    private static final long serialVersionUID = 8829409374640437472L;

    public ParseException() {
        super();
    }

    public ParseException(String detailMessage) {
        super(detailMessage);
    }

    public ParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseException(Throwable throwable) {
        super(throwable);
    }

}

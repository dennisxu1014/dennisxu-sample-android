package com.dennisxu.lib.core.net;
/**
 * @Description  NetworkExecption汇总了网络模块的所有异常 
 * 使用原则
 * 1.尽量保留原异常的信息，使用NetworkExecption(int type, Throwable throwable)将原始异常保留。
 * 2.不在网络模块指定异常对应的文案，逐步弃用public NetworkExecption(String message)。
 * @author xuyang5
 * @date 2014-1-26 上午11:38:02
 */
public class NetworkExecption extends Exception {

	private static final long serialVersionUID = 1L;

	public static final int UNKNOWN_EXCEPTION = -1;// 未指明异常
	public static final int IO_EXCEPTION = 0;// IO异常
	public static final int RESPONSE_EXCEPTION = 1;// 服务端未正常相应
	public static final int UNSUPPORTEDENCODING_EXCEPTION = 2;// 不支持的编码
	public static final int NOSIGNAL_EXCEPTION = 3;// 无信号
	public static final int MALFORMEDURL_EXCEPTION = 4;// URL格式
	public static final int SSLSOCKET_EXCEPTION = 5;// SSLsocket相关异常
	public static final int SOCKET_TIMEOUT_EXCEPTION = 6;// 超时异常
	public static final int UNKNOWN_HOST_EXCEPTION = 7;//找不到服务器

	private int statusCode;
	private int type = UNKNOWN_EXCEPTION;

	public NetworkExecption(int type, String message) {
		super(message);
		this.type = type;
	}

	public NetworkExecption(String message) {//逐步弃用，不再在网络模块内指定文案
		super(message);
	}

	public NetworkExecption(int type, Throwable throwable) {
		super(throwable);
	}

	public NetworkExecption(int type, String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getType() {
		return type;
	}

	/**
	 * 设置异常类型
	 * @param exceptionType
	 */
	public void setType(int exceptionType) {
		this.type = exceptionType;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}

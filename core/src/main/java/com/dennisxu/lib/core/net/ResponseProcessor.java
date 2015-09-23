package com.dennisxu.lib.core.net;
/**
 * 
 * @Description 响应结果处理器接口，可根据需要实现本接口，将响应处理为不同类型
 * @author xuyang5
 * @date 2014-1-26 下午2:04:55
 * @param <T>
 */
interface ResponseProcessor<T> {
	public T processResponse(ResponseWrapper response,IDownloadState callback) throws NetworkExecption;
}

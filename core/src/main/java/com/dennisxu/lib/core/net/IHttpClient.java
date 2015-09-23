package com.dennisxu.lib.core.net;


import com.dennisxu.lib.core.config.Configuration;

/**
 * @author xuyang5
 * @Description
 * @date 2014-1-26 上午10:58:00
 */
interface IHttpClient {

    Configuration getConfiguration();

    void setConfiguration(Configuration configuration);

    /**
     * 使用Get方法获取网络数据
     *
     * @param url
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpGet(String url) throws NetworkExecption;

    /**
     * 使用Get方法获取网络数据(带Get参数)
     *
     * @param url
     * @param getParams
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpGet(String url, Params getParams) throws NetworkExecption;

    /**
     * 使用Get方法获取网络数据（带Get参数，可配置Header）
     *
     * @param url
     * @param getParams
     * @param headers
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpGet(String url, Params getParams, Params headers) throws NetworkExecption;

    /**
     * 使用Get方法获取网络数据（带Get参数，可配置Header）
     *
     * @param url
     * @param getParams
     * @param headers
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpGet(String url, Params getParams, Params headers, NetHttpOptions netHttpOptions)
            throws NetworkExecption;

    /**
     * 使用Post方法获取网络数据（带Post参数）
     *
     * @param url
     * @param postParams
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPost(String url, Params postParams) throws NetworkExecption;

    /**
     * 使用Post方法获取网络数据（带Get,Post参数）
     *
     * @param url
     * @param getParams
     * @param postParams
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams) throws NetworkExecption;

    /**
     * 使用Post方法获取网络数据（带Get,Post参数）
     *
     * @param url
     * @param getParams
     * @param postParams
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams, NetHttpOptions netHttpOptions)
            throws NetworkExecption;

    /**
     * 使用Post方法获取网络数据（带Get,Post参数,可配置Header）
     *
     * @param url
     * @param getParams
     * @param postParams
     * @param headers
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams, Params headers)
            throws NetworkExecption;

    /**
     * 使用Post方法获取网络数据（带Get,Post参数(已拼接成串),可配置Header）
     *
     * @param url
     * @param getParams
     * @param str
     * @param headers
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPost(String url, Params getParams, String str, Params headers) throws NetworkExecption;

    /**
     * 使用Post方法获取网络数据（带Get,Post参数(已拼接成串),可配置Header）
     *
     * @param url
     * @param getParams
     * @param str
     * @param headers
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPost(String url, Params getParams, String str, Params headers,
                                    NetHttpOptions netHttpOptions) throws NetworkExecption;

    /**
     * openUrlPostStream的逻辑
     *
     * @param url
     * @param getParams
     * @param postParams
     * @param headers
     * @param callback
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpPostStream(String url, Params getParams, Params postParams, Params headers,
                                          IDownloadState callback, NetHttpOptions netHttpOptions) throws NetworkExecption;

    /**
     * 上传(上传参数的规则)
     *
     * @param url
     * @param getParams
     * @param postParams
     * @param headers
     * @param callback
     * @return
     * @throws NetworkExecption
     */
    public ResponseWrapper httpUpload(String url, Params getParams, Params postParams, Params headers,
                                      IDownloadState callback) throws NetworkExecption;
}

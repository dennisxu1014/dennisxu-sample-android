package com.dennisxu.lib.core.net;

import android.os.Bundle;

import com.dennisxu.lib.core.util.Pair;
import com.dennisxu.lib.core.config.Configuration;
import com.dennisxu.lib.core.util.LogUtil;

import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.zip.GZIPInputStream;

/**
 * BaiduBandHttpEngine 为网络模块和BaiduBand其它部分的接口类
 *
 * @author: xuyang
 * @date: 2014-8-24 下午3:17:49
 * @date: 2014-11-27 xuwei fix
 */
public class HttpEngine {
    private static final String TAG = HttpEngine.class.getCanonicalName();
    private static HttpEngine instance;
    private IHttpClient mHttpClient;

    /**
     * 根据type构造不同的HTTP实现
     *
     * @param type
     */
    private HttpEngine(Type type) {
        switch (type) {
            case ApacheHttpClient:
                mHttpClient = HttpClientApacheImpl.getInstance();
                break;
            // case HttpURLConnection:
            // mHttpClient = HttpURLConnectionImpl.getInstance(context);
            // break;
            // case Socket:
            // mHttpClient = HttpSocketImpl.getInstance(context);
            // break;
            // case SocketChannel:
            // mHttpClient = HttpClientNIOImpl.getInstance(context);
            // break;
            default:
                mHttpClient = HttpClientApacheImpl.getInstance();
        }
    }

    public synchronized static HttpEngine getInstance() {
        if (instance == null) {
            instance = new HttpEngine(Type.ApacheHttpClient);
        }
        return instance;
    }

    public void setConfiguration(Configuration configuration) {
        mHttpClient.setConfiguration(configuration);
    }

    /**
     * HTTP响应处理器，此处将Inputstream处理为文本 *
     */
    private ResponseProcessor<String> mStringProcessor = new ResponseProcessor<String>() {
        @Override
        public String processResponse(ResponseWrapper response, IDownloadState callback) throws NetworkExecption {
            if (response == null) {
                return null;
            }
            ByteArrayBuffer buffer = new ByteArrayBuffer(32 * 1024);
            InputStream in = null;
            try {
                in = response.getContent();
                if (response.getHeader() == null) {
                    LogUtil.e(TAG, "header is null");
                }

                String encoding = response.getHeader().get("Content-Encoding");
                if (encoding != null && encoding.contains("gzip")) {
                    LogUtil.d(TAG, "gzip");
                    in = new GZIPInputStream(in);
                } else {
                    LogUtil.d(TAG, "no gzip");
                }

                int temp;
                byte[] bytes = new byte[8096];
                while ((temp = in.read(bytes)) != -1) {
                    buffer.append(bytes, 0, temp);
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                throw new NetworkExecption(NetworkExecption.SOCKET_TIMEOUT_EXCEPTION, "网络连接超时", e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new NetworkExecption(NetworkExecption.IO_EXCEPTION, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response != null) {
                    response.closeConnection();
                }
            }
            byte[] result = buffer.toByteArray();
            String data = new String(result);
            LogUtil.d("xuyang", data);
            return data;
        }
    };

    /**
     * 使用HTTP Get方法获取网络数据
     *
     * @param url
     * @return 响应文本
     * @throws NetworkExecption
     */
    public String httpGet(String url) throws NetworkExecption {
        return httpGet(url, null);
    }

    /**
     * 使用HTTP Get方法获取网络数据(带Get参数)
     *
     * @param url
     * @param params Get参数
     * @return 响应文本
     * @throws NetworkExecption
     */
    public String httpGet(String url, Params params) throws NetworkExecption {
        return httpGet(url, params, null, null);
    }

    /**
     * 使用HTTP Get方法获取网络数据(带Get参数)
     *
     * @param url
     * @param getParams Get参数
     * @return 响应文本
     * @throws NetworkExecption
     */
    public String httpGet(String url, Params getParams, NetHttpOptions netHttpOptions) throws NetworkExecption {
        return httpGet(url, getParams, null, netHttpOptions);
    }

    /**
     * 使用HTTP Get方法获取网络数据(带Get参数和进度回调)
     *
     * @param url
     * @param params
     * @param callback 下载进度回调
     * @return 响应文本
     * @throws NetworkExecption
     */
    public String httpGet(String url, Params params, IDownloadState callback, NetHttpOptions netHttpOptions)
            throws NetworkExecption {
        LogUtil.d(TAG, "Get:" + url + " \n " + params.toString());
        final Params headers = new Params().addPair("Accept-Encoding", "gzip,deflate");
        // 需要压缩
        ResponseWrapper response = mHttpClient.httpGet(url, params, headers, netHttpOptions);
        return mStringProcessor.processResponse(response, callback);
    }

    public ResponseWrapper httpGet2(String url, Params params, IDownloadState callback, NetHttpOptions netHttpOptions)
            throws NetworkExecption {
        LogUtil.d(TAG, "Get:" + url + " \n ");

        final Params headers = new Params().addPair("Accept-Encoding", "gzip,deflate");
        // 需要压缩
        if (callback != null) {
            callback.onStart(url);
        }
        ResponseWrapper response = mHttpClient.httpGet(url, params, headers, netHttpOptions);
        return response;
    }
    /**
     * 使用HTTP Post方法获取网络数据(带Post参数)
     *
     * @param url
     * @param postParams
     * @return 响应文本
     * @throws NetworkExecption
     */
    public String httpPost(String url, Params postParams) throws NetworkExecption {
        return httpPost(url, null, postParams, null);
    }

    public String httpPost(String url, Params postParams, NetHttpOptions netHttpOptions) throws NetworkExecption {
        return httpPost(url, null, postParams, netHttpOptions);
    }

    public String httpPost(String url, Params getParams, Params postParams) throws NetworkExecption {
        return httpPost(url, getParams, postParams, null);
    }

    /**
     * 使用HTTP Post方法获取网络数据(带Get,Post参数)
     *
     * @param url
     * @param getParams
     * @param postParams
     * @return 响应文本
     * @throws NetworkExecption
     */
    public String httpPost(String url, Params getParams, Params postParams, NetHttpOptions netHttpOptions)
            throws NetworkExecption {
        LogUtil.d(TAG, "Post:" + url + ",  \n " + postParams.toString());
        LogUtil.d(TAG, "Post:" + printHttpRequest(url, postParams));
        Bundle headers = new Bundle();// 需要压缩
        headers.putString("Accept-Encoding", "gzip,deflate");
        ResponseWrapper response = mHttpClient.httpPost(url, getParams, postParams, netHttpOptions);
        return mStringProcessor.processResponse(response, null);
    }

    /**
     * 上传一段文本
     *
     * @param url
     * @param postText
     * @return
     * @throws NetworkExecption
     */
    public String httpPost(String url, String postText) throws NetworkExecption {
        ResponseWrapper response = mHttpClient.httpPost(url, null, postText, null);
        return mStringProcessor.processResponse(response, null);
    }

    private static String printHttpRequest(String url, Params params) {
        StringBuilder sb = new StringBuilder(url);

        if (params != null) {
            for (Pair<String, String> param : params.mParams) {
                if (param != null) {
                    sb.append("&" + param.first + "=" + param.second);
                }
            }
        }
        return sb.toString();
    }
}

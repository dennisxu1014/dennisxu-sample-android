package com.dennisxu.lib.core.net;

import android.text.TextUtils;

import com.dennisxu.lib.core.util.Pair;
import com.dennisxu.lib.core.config.Configuration;
import com.dennisxu.lib.core.util.LogUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.HashMap;

/**
 * @author xuyang5
 * @Description
 * @date 2014-1-24 下午6:25:03
 */
class HttpClientApacheImpl implements IHttpClient {
    static final String TAG = HttpClientApacheImpl.class.getSimpleName();
    private static boolean debug = true;

    private static IHttpClient instance;
    private Configuration mConfiguration;

    private HttpClientApacheImpl() {
    }

    public static IHttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClientApacheImpl.class) {
                if (instance == null) {
                    instance = new HttpClientApacheImpl();
                }
            }
        }
        return instance;
    }

    private HttpClient getHttpClient(NetHttpOptions netHttpOptions) throws NetworkExecption {
       /* HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params,"UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpProtocolParams.setUserAgent(params, System.getProperty("http.agent"));*/
        // scheme配置
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", getSSlSocketFactoryEx(), 443));

        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 30000);
        httpParams.setParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8192);
        httpParams.setParameter(HttpConnectionParams.SO_TIMEOUT, 30000);
        httpParams.setParameter(HttpConnectionParams.TCP_NODELAY, true);

        if (netHttpOptions != null) {
            if (netHttpOptions.connection_timeout > 0) {
                httpParams.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, netHttpOptions.connection_timeout);
            }
            if (netHttpOptions.so_timeout > 0) {
                httpParams.setParameter(HttpConnectionParams.SO_TIMEOUT, 60000);
            }
        }

        // FIXME DEFAULT_PROXY配置
        NetworkState state = getConfiguration().getNetWorkState();
        if (state == NetworkState.NOTHING) {// 没信号
            throw new NetworkExecption(NetworkExecption.NOSIGNAL_EXCEPTION, "网络异常");
        } else if (state == NetworkState.MOBILE) {// 移动网络
            APNWrapper wrapper = getConfiguration().getAPNWrapper();
            if (!TextUtils.isEmpty(wrapper.proxy)) {
                httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, new HttpHost(wrapper.proxy, wrapper.port));
            }
        }

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParams);
        return httpClient;
    }

    @Override
    public Configuration getConfiguration() {
        return mConfiguration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.mConfiguration = configuration;
    }

    @Override
    public ResponseWrapper httpGet(String url) throws NetworkExecption {
        return httpGet(url, null, null);
    }

    @Override
    public ResponseWrapper httpGet(String url, Params getParams) throws NetworkExecption {
        return httpGet(url, getParams, null);
    }

    @Override
    public ResponseWrapper httpGet(String url, Params getParams, Params headers) throws NetworkExecption {
        return httpGet(url, getParams, headers, null);
    }

    @Override
    public ResponseWrapper httpGet(String url, Params getParams, Params headers, NetHttpOptions netHttpOptions)
            throws NetworkExecption {
        ResponseWrapper rw = null;

        try {
            HttpClient httpClient = getHttpClient(netHttpOptions);
            url = NetworkUtils.getCompleteUrl(url, getParams);
            LogUtil.d("xuyang", "Get CompleteUrl:" + url);

            HttpGet request = new HttpGet(url);
            setupHeaders(request, headers);// 装载请求头

            HttpResponse response = httpClient.execute(request);
            checkResponse(response);

            rw = httpResponseToWrapper(response, httpClient);
        } catch (SocketTimeoutException e) {
            throw new NetworkExecption(NetworkExecption.SOCKET_TIMEOUT_EXCEPTION, e);
        } catch (ConnectTimeoutException e) {
            throw new NetworkExecption(NetworkExecption.SOCKET_TIMEOUT_EXCEPTION, e);
        } catch (IllegalStateException e) {
            throw new NetworkExecption(NetworkExecption.UNKNOWN_EXCEPTION, e);
        } catch (UnknownHostException e) {
            throw new NetworkExecption(NetworkExecption.UNKNOWN_HOST_EXCEPTION, e);
        } catch (IOException e) {
            throw new NetworkExecption(NetworkExecption.IO_EXCEPTION, e);
        }
        return rw;
    }

    @Override
    public ResponseWrapper httpPost(String url, Params postParams) throws NetworkExecption {
        return httpPost(url, null, postParams);
    }

    @Override
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams) throws NetworkExecption {
        return httpPost(url, getParams, postParams, null, null, null);
    }

    @Override
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams, Params headers)
            throws NetworkExecption {
        return httpPost(url, getParams, postParams, headers, null, null);
    }

    @Override
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams, NetHttpOptions netHttpOptions)
            throws NetworkExecption {
        return httpPost(url, getParams, postParams, null, null, netHttpOptions);
    }

    @Override
    public ResponseWrapper httpPost(String url, Params getParams, String str, Params headers) throws NetworkExecption {
        return httpPost(url, getParams, str, headers, null);
    }

    public ResponseWrapper httpPost(String url, Params getParams, String str, Params headers,
                                    NetHttpOptions netHttpOptions) throws NetworkExecption {
        // TODO 此方法待测试,应该是可以复用的
        ResponseWrapper rw = null;

        try {
            HttpClient httpClient = getHttpClient(netHttpOptions);
            url = NetworkUtils.getCompleteUrl(url, getParams);
            HttpPost request = new HttpPost(url);
            setupHeaders(request, headers);// 装载请求头
            StringEntity stringEntity = new StringEntity(str);
            request.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(request);

            printAllHeaders(request.getAllHeaders());
            printAllHeaders(response.getAllHeaders());

            checkResponse(response);

            rw = httpResponseToWrapper(response, httpClient);
        } catch (SocketTimeoutException e) {
            throw new NetworkExecption(NetworkExecption.SOCKET_TIMEOUT_EXCEPTION, e);
        } catch (IOException e) {
            throw new NetworkExecption(NetworkExecption.IO_EXCEPTION, e);
        }
        return rw;
    }

    // @Override
    public ResponseWrapper httpPost(String url, Params getParams, Params postParams, Params headers,
                                    IDownloadState callback, NetHttpOptions netHttpOptions) throws NetworkExecption {
        ResponseWrapper rw = null;

        try {
            HttpClient httpClient = getHttpClient(netHttpOptions);
            url = NetworkUtils.getCompleteUrl(url, getParams);
            LogUtil.d("xuyang", "Post CompleteUrl:" + url);
            HttpPost request = new HttpPost(url);
            MultipartEntity multipartContent = null;
            if (callback != null) {
                multipartContent = new CustomMultiPartEntity(callback);
            } else {
                multipartContent = new MultipartEntity();
            }
            LogUtil.i("xuyang", "before setupMultipartEntity");
            setupMultipartEntity(multipartContent, postParams);
            // 装载MultipartEntity
            request.setEntity(multipartContent);
            HttpResponse response = httpClient.execute(request);

            printAllHeaders(request.getAllHeaders());
            printAllHeaders(response.getAllHeaders());

            checkResponse(response);
            rw = httpResponseToWrapper(response, httpClient);
        } catch (SocketTimeoutException e) {
            throw new NetworkExecption(NetworkExecption.SOCKET_TIMEOUT_EXCEPTION, e);
        } catch (UnsupportedEncodingException e) {
            throw new NetworkExecption(NetworkExecption.UNSUPPORTEDENCODING_EXCEPTION, e);
        } catch (IOException e) {
            throw new NetworkExecption(NetworkExecption.IO_EXCEPTION, e);
        }
        return rw;
    }

    @Override
    public ResponseWrapper httpUpload(String url, Params getParams, Params postParams, Params headers,
                                      IDownloadState callback) throws NetworkExecption {// TODO
        // 应该能复用httpPost的代码
        return httpPost(url, getParams, postParams, headers, callback, null);
        // ResponseWrapper rw = null;
        //
        // try {
        // HttpClient httpClient = getHttpClient();// TODO 上传的超时时间应该特别设置
        // HttpPost request = new HttpPost(url);
        // MultipartEntity multipartContent = new MultipartEntity();
        // setupMultipartEntity(multipartContent, postParams);
        // // 装载MultipartEntity
        // request.setEntity(multipartContent);
        //
        // HttpResponse response = httpClient.execute(request);
        // checkResponse(response);
        // rw = httpResponseToWrapper(response, httpClient);
        // } catch (UnsupportedEncodingException e1) {
        // throw new NetworkExecption(
        // NetworkExecption.UNSUPPORTEDENCODING_EXCEPTION, e1);
        // } catch (IOException e) {
        // throw new NetworkExecption(NetworkExecption.IO_EXCEPTION, e);
        // }
        // return rw;
    }

    private void checkResponse(HttpResponse response) throws NetworkExecption {
        int code = response.getStatusLine().getStatusCode();
        if (code != HttpStatus.SC_OK) {
            NetworkExecption ex =
                    new NetworkExecption(NetworkExecption.RESPONSE_EXCEPTION, "Server error,Code:" + code);
            throw ex;
        }
    }

    private ResponseWrapper httpResponseToWrapper(HttpResponse response, HttpClient client)
            throws IllegalStateException, IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        Header[] headers = response.getAllHeaders();
        ResponseWrapper rw =
                new ResponseWrapper.Builder().setCode(statusLine.getStatusCode())
                        .setVersion(statusLine.getProtocolVersion().toString()).setContent(entity.getContent())
                        .setHeaders(headToHashMap(headers)).setConnection(client).build();

        return rw;
    }

    private static HashMap<String, String> headToHashMap(Header[] headers) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }
        return map;
    }

    /**
     * 装载请求头
     *
     * @param request
     * @param headers
     */
    private void setupHeaders(HttpRequestBase request, Params headers) {
        if (headers == null) {
            return;
        }
        if (headers.exists()) {
            for (Pair<String, String> head : headers.list()) {
                request.addHeader(head.first, head.second);
            }
        }
    }

    /**
     * 装载混合类型参数到Post的Entity
     *
     * @param multipartContent
     * @param bundle
     * @throws UnsupportedEncodingException
     */
    private void setupMultipartEntity(MultipartEntity multipartContent, Params bundle)
            throws UnsupportedEncodingException {/*
                                                  * for (final String key : bundle.keySet()) { if
                                                  * (NetworkUtils.TYPE_FILE_NAME.equals(key) ||
                                                  * NetworkUtils.GZIP_FILE_NAME.equals(key)) { LogUtil.d("xuyang",
                                                  * "in if TYPE_FILE_NAME"); Object object = bundle.get(key); if (object
                                                  * != null || object instanceof Params) { Params mFilePathParams =
                                                  * ((Params) object); LogUtil.d("xuyang",
                                                  * "in if object instanceof Params"); for (final String
                                                  * mFilePathParamsKey : mFilePathParams.keySet()) { LogUtil.d("xuyang",
                                                  * "....mFilePathParamsKey:" + mFilePathParamsKey); File mFile = new
                                                  * File(mFilePathParams.getString(mFilePathParamsKey)); if
                                                  * (mFile.exists()) { FileBody bin; if
                                                  * (NetworkUtils.TYPE_FILE_NAME.equals(key)) { // Log.e(TAG, //
                                                  * mFilePathParamsKey // + ":" // + mFilePathParams //
                                                  * .getString(mFilePathParamsKey)); LogUtil.d("xuyang",
                                                  * "setupMultipartEntity"); LogUtil.d("xuyang",
                                                  * mFile.getAbsolutePath()); bin = new FileBody(mFile, "image/jpeg"); }
                                                  * else { bin = new FileBody(mFile, "application/zip"); }
                                                  * multipartContent.addPart(mFilePathParamsKey, bin); } } } } else {
                                                  * String value = String.valueOf(bundle.get(key)); StringBody
                                                  * mStringBody = null; try { mStringBody = new StringBody(value == null
                                                  * ? "" : value, Charset.forName(HTTP.UTF_8)); } catch
                                                  * (UnsupportedEncodingException e) { e.printStackTrace(); }
                                                  * multipartContent.addPart(URLEncoder.encode(key, "UTF_8"),
                                                  * mStringBody); } }
                                                  */
    }

    /**
     * 获取一个SSlSocketFactoryEx对象
     *
     * @return
     * @throws NetworkExecption
     */
    private SSLSocketFactory getSSlSocketFactoryEx() throws NetworkExecption {
        KeyStore mKeyStore = null;
        SSLSocketFactory mSslSocketFactory = null;
        try {
            mKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            mKeyStore.load(null, null);
            mSslSocketFactory = new SSLSocketFactoryEx(mKeyStore);
        } catch (Exception e) {
            throw new NetworkExecption(NetworkExecption.SSLSOCKET_EXCEPTION, e);
        }
        mSslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return mSslSocketFactory;
    }

    private void printAllHeaders(Header[] headers) {
        if (!debug)
            return;
        // for (Header header : headers) {
        // Log.i(TAG, header.getName() + ":" + header.getValue());
        // }
    }

    /**
     * 扩展MultipartEntity 添加上传进度
     */
    public static class CustomMultiPartEntity extends MultipartEntity {

        private final IDownloadState listener;

        public CustomMultiPartEntity(final IDownloadState listener) {
            super();
            this.listener = listener;
        }

        public CustomMultiPartEntity(final HttpMultipartMode mode, final IDownloadState listener) {
            super(mode);
            this.listener = listener;
        }

        public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset,
                                     final IDownloadState listener) {
            super(mode, boundary, charset);
            this.listener = listener;
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            super.writeTo(new CountingOutputStream(outstream, this.listener, getContentLength()));
        }

        public static class CountingOutputStream extends FilterOutputStream {

            private final IDownloadState listener;
            private long transferred;
            private long mContentLength;

            public CountingOutputStream(final OutputStream out, final IDownloadState listener, final long contentLength) {
                super(out);
                this.listener = listener;
                this.transferred = 0;
                this.mContentLength = contentLength;
            }

            public void write(byte[] b, int off, int len) throws IOException {
                out.write(b, off, len);
                out.flush();
                if (len > 0) {
                    this.transferred += len;

                    this.listener.onProgressChanged((int)(this.transferred * 100 / mContentLength));
                }
            }

            public void write(int b) throws IOException {
                out.write(b);
                out.flush();
                this.transferred++;
            }
        }
    }

    @Override
    public ResponseWrapper httpPostStream(String url, Params getParams, Params postParams, Params headers,
                                          IDownloadState callback, NetHttpOptions netHttpOptions) throws NetworkExecption {
        ResponseWrapper rw = null;

        try {
            HttpClient httpClient = getHttpClient(netHttpOptions);
            url = NetworkUtils.getCompleteUrl(url, getParams);

            HttpPost request = new HttpPost(url);

            InputStreamEntity entity = buildInputStreamEntity(postParams, callback);

            // MultipartEntity multipartContent = null;
            // if (callback != null) {
            // multipartContent = new CustomMultiPartEntity(callback);
            // } else {
            // multipartContent = new MultipartEntity();
            // }
            // setupMultipartEntity(multipartContent, postParams);
            // 装载MultipartEntity
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);

            printAllHeaders(request.getAllHeaders());

            printAllHeaders(response.getAllHeaders());

            checkResponse(response);
            rw = httpResponseToWrapper(response, httpClient);
        } catch (UnsupportedEncodingException e1) {
            throw new NetworkExecption(NetworkExecption.UNSUPPORTEDENCODING_EXCEPTION, e1);
        } catch (IOException e) {
            throw new NetworkExecption(NetworkExecption.IO_EXCEPTION, e);
        }
        return rw;
    }

    // 此方法只在openUrlPostStream中调用了
    // FIXME
    private static InputStreamEntity buildInputStreamEntity(Params params, IDownloadState listener) {

        if (!params.exists()) {
            return null;
        }

        /*
         * for (Pair<String, Object> pair : params.list()) { // 只从参数中过滤出了第一个byte[]类型就return了 if (pair.second != null &&
         * pair.second instanceof byte[]) { byte[] bytesParams = (byte[]) paramsObj;
         * 
         * InputStream is = new ByteArrayInputStream(bytesParams); InputStreamEntity entity = new
         * CustomInputStreamEntity(is, bytesParams.length, listener);
         * 
         * return entity; } }
         */
        return null;// 如果没有byte[]就return null
    }
    // ////////以下部分为外部关闭连接的预留方法，未完成，保留代码
    // private Map<String, Queue<HttpRequestBase>> mRequestMap;
    // mRequestMap = new HashMap<String, Queue<HttpRequestBase>>();
    // public void abortRequest(String url) {
    // HttpRequestBase request = getRequest(url);
    // if (request != null) {
    // request.abort();
    // LogAssist.i(Developer.WANGBO, Module.NET_CORE, "abortRequest url:" +
    // url);
    // }
    // }
    //
    // private void addRequestToMap(String url, HttpRequestBase request) {
    // synchronized (mLock) {
    // Queue<HttpRequestBase> queue = mRequestMap.get(url);
    // if (queue == null) {
    // queue = new LinkedList<HttpRequestBase>();
    // }
    // queue.offer(request);
    // mRequestMap.put(url, queue);
    // printMapInfo();
    // }
    // }
    //
    // private void removeRequest(String url, HttpRequestBase request) {
    // synchronized (mLock) {
    // Queue<HttpRequestBase> queue = mRequestMap.get(url);
    // if (queue != null && queue.size() > 0 && request != null) {
    // queue.remove(request);
    // if (queue.size() == 0) {
    // mRequestMap.remove(url);
    // }
    // }
    // }
    // }
    //
    // private HttpRequestBase getRequest(String url) {
    // synchronized (mLock) {
    // Queue<HttpRequestBase> queue = mRequestMap.get(url);
    // if (queue != null && queue.size() > 0) {
    // HttpRequestBase request = queue.poll();
    // if (queue.size() == 0) {
    // mRequestMap.remove(url);
    // }
    // return request;
    // }
    // }
    // return null;
    // }
    //
    // private void printMapInfo() {
    // if (!debug)
    // return;
    // LogAssist.i(Developer.WANGBO, Module.NET_CORE, "map size:" +
    // mRequestMap.size());
    // Set<String> set = mRequestMap.keySet();
    // for (String key : set) {
    // Queue<HttpRequestBase> queue = mRequestMap.get(key);
    // LogAssist.i(Developer.WANGBO, Module.NET_CORE, "queue size" +
    // queue.size() + "   url:" + key);
    // }
    // }
    // ////////以上部分为外部关闭连接的预留方法，未完成，保留代码

    // /**
    // * 装载普通post参数到Entity
    // *
    // * @param request
    // * @param bundle
    // * @throws NetworkExecption
    // */
    // private void setupEntity(HttpEntityEnclosingRequest request, Params
    // bundle)
    // throws NetworkExecption {
    // ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    //
    // for (String key : bundle.keySet()) {
    // params.add(new BasicNameValuePair(key, String.valueOf(bundle
    // .get(key))));
    // }
    // if (params.size() > 0) {
    // try {
    // request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    // } catch (UnsupportedEncodingException e) {
    // throw new NetworkExecption(
    // NetworkExecption.UNSUPPORTEDENCODING_EXCEPTION, e);
    // }
    // }
    // }

}

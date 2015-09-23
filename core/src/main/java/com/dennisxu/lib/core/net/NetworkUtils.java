package com.dennisxu.lib.core.net;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dennisxu.lib.core.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Description 网络模块工具类,包含了网络状态获取，URL拼接，VPN获取等工具方法
 * @author xuyang5
 * @date 2014-1-26 下午1:34:05
 */
public class NetworkUtils {
    // 请求头中的代理设置
    public static String USER_AGENT =
            "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.1.4) Gecko/20091111 Gentoo Firefox/3.5.4";
    public static final String TYPE_FILE_NAME = "TYPE_FILE_NAME";
    public static final String GZIP_FILE_NAME = "GZIP_FILE_NAME";

    public enum NetworkState {
        MOBILE, WIFI, NOTHING;
    }

    /**
     * 获取当前网络状态.
     * 
     * @param context
     * @return
     */
    public static NetworkState getNetworkState(Context context) {
        if (context == null){
            return NetworkState.NOTHING;
        }
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return NetworkState.NOTHING;
        } else {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NetworkState.MOBILE;
            } else {
                return NetworkState.WIFI;
            }
        }
    }

    /**
     * 判断当前网络是否为wifi
     * 
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        if (context == null)
        {
            return false;
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前是否是连网状态
     * 
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        if (context == null)
        {
            return false;
        }
        NetworkState networkState = getNetworkState(context);
        return networkState != NetworkState.NOTHING;
    }

    /**
     * 通过拼接get参数（Bundle类型）获得完整的URL
     * 
     * @param url
     * @param getParams
     * @return
     */
    public static String getCompleteUrl(String url, Params getParams) {
        String params = encodeUrl(getParams);
        return appendUrlParams(url, params);
    }

    /**
     * 通过拼接get参数（String类型）获得完整的URL
     * 
     * @param url
     * @param params 已经拼接完成的参数串
     * @return
     */
    public static String appendUrlParams(String url, String params) {
        if (TextUtils.isEmpty(params)) {
            return url;
        }

        final String HASH_KEY = "#";
        String browserAction = null;
        if (url.contains(HASH_KEY)) {
            int hashKeyPos = url.indexOf(HASH_KEY);
            browserAction = url.substring(hashKeyPos);
            url = url.substring(0, hashKeyPos);
        }

        if (url.indexOf('?') != -1 && url.indexOf('?') != url.length() - 1) {
            url = url + "&" + params;
        } else {
            url = url + "?" + params;
        }

        if (!TextUtils.isEmpty(browserAction)) {
            url += browserAction;
        }
        return url;
    }

    /**
     * 将Bundle中存储的参数编码为String类型的串
     * 
     * @param params
     * @return
     */
    public static String encodeUrl(Params params) {
        if (params == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Pair<String, String> param : params.list()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(urlEncode(param.first) + "=" + urlEncode(String.valueOf(param.second)));
        }
        return sb.toString();
    }

    private static String urlEncode(String in) {
        if (TextUtils.isEmpty(in)) {
            return "";
        }

        try {
            return URLEncoder.encode(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static final String NO_APN = "N/A";

    /**
     * APN包装类
     **/
    public static class APNWrapper {
        public String name;
        public String apn;
        public String proxy;
        public int port;

        public String getApn() {
            return apn;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }

        public String getProxy() {
            return proxy;
        }

        APNWrapper() {
        }
    }

    /**
     * 获取APN
     * 
     * @param mContext
     * @return
     */
    @SuppressWarnings("deprecation")
    public static APNWrapper getAPN(Context mContext) {

        APNWrapper wrapper = new APNWrapper();

        Cursor cursor = null;
        try {
            cursor =
                    mContext.getContentResolver().query(PREFERRED_APN_URI,
                            new String[] { "name", "apn", "proxy", "port" }, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            // 为了解决在4.2系统上禁止非系统进程获取apn相关信息，会抛出安全异常
            // java.lang.SecurityException: No permission to write APN settings
        }
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                wrapper.name = cursor.getString(0) == null ? "" : cursor.getString(0).trim();
                wrapper.apn = cursor.getString(1) == null ? "" : cursor.getString(1).trim();
            }
            cursor.close();
        }
        if (TextUtils.isEmpty(wrapper.apn)) {
            ConnectivityManager conManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = conManager.getActiveNetworkInfo();
            if (info != null) {
                wrapper.apn = info.getExtraInfo();
            }
        }
        if (TextUtils.isEmpty(wrapper.apn)) {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            wrapper.apn = telephonyManager.getNetworkOperatorName();
        }
        if (TextUtils.isEmpty(wrapper.apn)) {
            wrapper.name = NO_APN;
            wrapper.apn = NO_APN;
        }
        wrapper.proxy = android.net.Proxy.getDefaultHost();
        wrapper.proxy = TextUtils.isEmpty(wrapper.proxy) ? "" : wrapper.proxy;
        wrapper.port = android.net.Proxy.getDefaultPort();
        wrapper.port = wrapper.port > 0 ? wrapper.port : 80;
        return wrapper;
    }
}

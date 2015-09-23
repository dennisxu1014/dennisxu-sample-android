package com.dennisxu.lib.core.net;

import android.os.Bundle;
import android.text.TextUtils;

import com.dennisxu.lib.core.util.MD5;
import com.dennisxu.lib.core.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Params 参数
 * 
 * @author xuwei19
 * @date 2014年11月28日 下午12:22:50
 */
public class Params{
    List<Pair<String, String>> mParams;

    public Params() {
        mParams = new ArrayList<Pair<String, String>>();
    }

    public Params addPair(String key, String value) {
        mParams.add(Pair.create(key, value));
        return this;
    }

    public boolean exists() {
        return mParams.size() > 0;
    }

    public List<Pair<String, String>> list() {
        return mParams;
    }

    /**
     * 装在通用参数
     * @param sid
     * @param versionName
     * @param netToken
     */
    public void fillCommonParams(String sid, String versionName, String netToken) {
        // getParams.remove("sign"); // 防止把sign加入到MD5�?
        // ------------------ 添加公共参数 ------------------
        // 服务端需要在url保留这些参数
        if (!TextUtils.isEmpty(sid)) {
            addPair("sid", sid);
        }
        addPair("app_version", versionName);
        addPair("timestamp", String.valueOf(System.currentTimeMillis()));
        addPair("location", "1.0.0");

        String signKey = getSignKey(netToken);
        addPair("sign", signKey);
    }

    private String getSignKey(String netToken) {
        TreeMap<String, String> paramMap = new TreeMap<String, String>();
        for (Pair<String, String> pair : list()) {
            paramMap.put(pair.first, pair.second);
        }
        StringBuffer sb = new StringBuffer();
        for (String key : paramMap.keySet()) {
            sb.append(key + "=" + paramMap.get(key));
        }
        sb.append(netToken);
        return MD5.md5(sb.toString());
    }

    public static Params create(Bundle bundle) {
        Params params = new Params();
        Set<String> set = bundle.keySet();
        for (String key : set) {
            params.addPair(key, (String) bundle.get(key));
        }
        return params;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Pair<String, String> pair : mParams) {
            sb.append("key:" + pair.first + " value:" + pair.second + "\n");
        }
        return sb.toString();
    }
}

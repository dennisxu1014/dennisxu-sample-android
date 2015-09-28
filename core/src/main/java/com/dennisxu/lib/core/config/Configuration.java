package com.dennisxu.lib.core.config;

import com.dennisxu.lib.core.net.APNWrapper;
import com.dennisxu.lib.core.net.NetworkState;

/**
 * Configure 全局配置
 *
 * @author xuwei19
 * @date 2014年11月26日 下午12:22:50
 */
public interface Configuration {
    boolean isDebugable();

    String getHost();

    String getVersionName();

    int getVersionCode();

    String getNetToken();

    NetworkState getNetWorkState();

    /**
     * 获取APN
     *
     * @return APNWrapper
     */
    APNWrapper getAPNWrapper();

    /**
     * 是否是WIFI环境
     *
     * @return
     */
    boolean isWifi();

    /**
     * 判断当前是否是连网状态
     *
     * @return
     */
    boolean isNetConnected();
}

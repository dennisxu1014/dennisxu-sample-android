package com.dennisxu.sample.common.ui;

import android.os.Bundle;

/**
 * fragment与activity通信接口
 *
 * @author: xuyang
 * @date: 2014-8-24 上午9:59:24
 */
public interface IFragmentNotification {

    public void beforeFragmentView();

    public void afterFragmentView();

    /**
     * fragment与Activity通信回调
     *
     * @param tag         通讯的类型
     * @param simpleParam 简单参数
     * @param extra       额外参数
     */
    public void onFragmentNotificated(String tag, String simpleParam, Bundle extra);
}

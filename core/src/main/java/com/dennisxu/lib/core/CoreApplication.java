package com.dennisxu.lib.core;

import android.app.Application;

import com.dennisxu.lib.core.util.LogUtil;

/**
 * Created by xuyang on 15/9/24.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("dennisxu","core application oncreate");

        //动态加载



    }
}

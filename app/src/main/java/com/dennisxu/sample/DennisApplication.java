package com.dennisxu.sample;

import android.app.Application;

import com.dennisxu.lib.core.controller.IController;
import com.dennisxu.sample.common.utils.CrashHandler;

/**
 * Created by xuyang on 15/9/28.
 */
public class DennisApplication extends Application {
    private IController mController;

    @Override
    public void onCreate() {
        super.onCreate();


        if (!BuildConfig.DEBUG) {
            // 设置该CrashHandler为程序的默认处理器
            CrashHandler.getInstance().init(getApplicationContext());
        }


    }

    public IController getController() {
        return mController;
    }

}

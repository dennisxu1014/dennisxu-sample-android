package com.dennisxu.lib.core.exception;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;

/**
 * 异常捕获
 * 
 * @author xuyang
 * 
 */
public class AppCrashHandler implements UncaughtExceptionHandler {

    private static final String EXCEPTION_LOG_FILE_PATH = "crash.log";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    public AppCrashHandler(Context context) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 收集设备异常信息
    }

}

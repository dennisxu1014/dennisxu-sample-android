package com.dennisxu.lib.core.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;


public abstract class MainUINotifier implements Notifier {

    private Context mContext;
    private Handler mHandler;
    private volatile boolean mCancel;

    public MainUINotifier(Context context) {
        super();
        this.mContext = context;
    }

    public abstract void onUiNotify(Object... args);

    @Override
    public final void onNotify(final Object... args) {
        getHandler().post(new Runnable() {

            @Override
            public void run() {
                if (!isCancel()) {
                    onUiNotify(args);
                }
            }
        });
    }

    private Handler getHandler() {
        return mHandler == null ? mHandler = new Handler(Looper.getMainLooper()) : mHandler;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public boolean isCancel() {
        return mCancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.mCancel = cancel;
    }
}

package com.dennisxu.sample.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.dennisxu.lib.core.controller.IController;
import com.dennisxu.lib.core.util.LogUtil;
import com.dennisxu.sample.BuildConfig;
import com.dennisxu.sample.DennisApplication;

/**
 * Created by xuyang on 15/9/28.
 */
public abstract class AbstractBaseActivity extends FragmentActivity implements IFragmentNotification {
    protected static final String TAG = "tag_activity_lifecycle";
    protected DennisApplication mApplication;
    protected boolean isDestroyed;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mApplication = (DennisApplication) getApplicationContext();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        printLeftCycle("onSaveInstanceState");
    }

    @Override
    public void onFragmentNotificated(String tag, String simpleParam, Bundle extra) {
    }

    @Override
    public void beforeFragmentView() {
    }

    @Override
    public void afterFragmentView() {
    }

    public void init() {
        setContentView(getLayoutId());
        initView();
        registerListener();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void registerListener();

    public abstract void initBusiness();

    public IController getController() {
        return mApplication.getController();
    }

    @Override
    protected void onStart() {
        super.onStart();
        printLeftCycle("onStart");
        initBusiness();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        printLeftCycle("onRestart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        printLeftCycle("onNewIntent");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        printLeftCycle("onActivityResult");
    }

    @Override
    protected void onResume() {
        super.onResume();
        printLeftCycle("onResume");
        /**
         * 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加
         */
//        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        printLeftCycle("onPause");
        /**
         * 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加
         */
//        StatService.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        printLeftCycle("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        printLeftCycle("onDestroy");
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    private void printLeftCycle(String methodName) {
        if (BuildConfig.DEBUG)
            LogUtil.i(TAG, ">> " + getClass().getSimpleName() + "*********>> " + methodName + " <<********* <<");
    }

    public String tag() {
        return getClass().getSimpleName();
    }

    public final void printLog(String log) {
        if (BuildConfig.DEBUG)
            LogUtil.d("tag_" + tag(), " >> -----------> " + log + " <------------ <<");
    }

}

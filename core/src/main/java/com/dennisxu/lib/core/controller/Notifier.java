package com.dennisxu.lib.core.controller;


public interface Notifier {
    void onNotify(Object... args);

    boolean isCancel();

    void setCancel(boolean cancel);
}
package com.dennisxu.lib.core.common;


import com.dennisxu.lib.core.controller.IController;
import com.dennisxu.lib.core.controller.Notifier;
import com.dennisxu.lib.core.net.Params;


public abstract class AbstractCommand implements ICommand {
    private IController mController;
    private Params mParams;
    private Notifier mNotifier;
    private volatile boolean isCancel = false;

    public AbstractCommand(IController controller) {
        this(controller, null);
    }

    public AbstractCommand(IController controller, Params params) {
        this(controller, params, null);
    }

    public AbstractCommand(IController controller, Params params, Notifier notifier) {
        this.mController = controller;
        this.mParams = params;
        this.mNotifier = notifier;

        if (mNotifier != null) {
            mNotifier.setCancel(isCancel);
        }
    }

    @Override
    public boolean isCancel() {
        return isCancel;
    }

    @Override
    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;

        if (mNotifier != null) {
            mNotifier.setCancel(isCancel);
        }
    }

    @Override
    public final String getName() {
        return getClass().getCanonicalName();
    }

    @Override
    public final IController getController() {
        return mController;
    }

    @Override
    public final Params getParams() {
        return mParams;
    }

    @Override
    public final void setParams(Params params) {
        this.mParams = params;
    }

    @Override
    public final Notifier getNotifier() {
        return mNotifier;
    }

    @Override
    public final void setNotifier(Notifier notifier) {
        this.mNotifier = notifier;
    }
}

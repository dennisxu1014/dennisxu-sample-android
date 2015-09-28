package com.dennisxu.lib.core.common;


import com.dennisxu.lib.core.controller.IController;
import com.dennisxu.lib.core.controller.Notifier;
import com.dennisxu.lib.core.net.Params;


public interface ICommand {

    String getName();

    IController getController();

    void execute();

    Params getParams();

    void setParams(Params params);

    Notifier getNotifier();

    void setNotifier(Notifier notifier);

    void setCancel(boolean isCancel);

    boolean isCancel();
}

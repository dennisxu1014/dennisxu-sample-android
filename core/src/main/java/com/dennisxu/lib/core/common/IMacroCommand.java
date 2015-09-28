package com.dennisxu.lib.core.common;

import com.dennisxu.lib.core.controller.IController;


public interface IMacroCommand {
    IController getController();

    void execute(ICommand command);

    void executeDelayed(ICommand command, long delayMillis);

    void executeAsync(ICommand command);

    void executeAsyncDelayed(ICommand command, long delayMillis);

}

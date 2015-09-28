package com.dennisxu.lib.core.controller;

import android.content.res.Configuration;

import com.dennisxu.lib.core.common.IMacroCommand;
import com.dennisxu.lib.core.common.MacroCommand;


public class Controller implements IController {
    private MacroCommand mMacroCommand;

    private Configuration mConfiguration;

    public Controller(Configuration configuration) {
        this.mConfiguration = configuration;
    }

    @Override
    public IMacroCommand getMacroCommand() {
        return mMacroCommand == null ? mMacroCommand = new MacroCommand(this) : mMacroCommand;
    }

    @Override
    public Configuration getConfiguration() {
        return mConfiguration;
    }

}

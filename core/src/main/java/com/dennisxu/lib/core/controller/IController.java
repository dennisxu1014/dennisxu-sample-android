package com.dennisxu.lib.core.controller;

import android.content.res.Configuration;

import com.dennisxu.lib.core.common.IMacroCommand;


public interface IController {
    IMacroCommand getMacroCommand();

    Configuration getConfiguration();
}

package com.dennisxu.lib.core.common;

import com.dennisxu.lib.core.controller.IController;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class MacroCommand implements IMacroCommand {

    private ExecutorService mPool;
    private Timer mTimer;
    private IController mController;

    public MacroCommand(IController controller) {
        this.mController = controller;
        mPool = Executors.newCachedThreadPool();
        mTimer = new Timer();
    }

    @Override
    public void execute(ICommand command) {
        command.setCancel(false);
        command.execute();
    }

    @Override
    public void executeDelayed(final ICommand command, long delayMillis) {
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                execute(command);
            }
        }, delayMillis);
    }

    @Override
    public void executeAsync(final ICommand command) {
        command.setCancel(false);
        mPool.execute(new Runnable() {
            @Override
            public void run() {
                command.execute();
            }
        });
    }

    @Override
    public void executeAsyncDelayed(final ICommand command, long delayMillis) {
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                executeAsync(command);
            }
        }, delayMillis);
    }

    @Override
    public IController getController() {
        return mController;
    }
}

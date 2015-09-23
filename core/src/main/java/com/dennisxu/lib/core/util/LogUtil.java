package com.dennisxu.lib.core.util;


import com.dennisxu.lib.core.BuildConfig;

/**
 * @author xuyang
 * 
 */
public final class LogUtil {

    /**
     * Priority constant for the println method Value 0 : disable all log , Value 7 : enable the log in every priority
     */
//    private static final int LOG_LEVEL = BuildConfig.DEBUG == true ? 7 : 0;
    private static final int LOG_LEVEL = 7;
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    /**
     * Send a {@link #VERBOSE} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        if (LOG_LEVEL >= android.util.Log.VERBOSE) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.v(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.VERBOSE) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.v(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link #DEBUG} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
//        if (LOG_LEVEL >= android.util.Log.DEBUG) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.d(tag, msg);
//        }
//        return 0;
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.DEBUG) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.d(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send an {@link #INFO} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        if (LOG_LEVEL >= android.util.Log.INFO) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.i(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.INFO) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.i(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link #WARN} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        if (LOG_LEVEL >= android.util.Log.WARN) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.w(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.WARN) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.w(tag, msg, tr);
        }
        return 0;
    }

    /*
     * Send a {@link #WARN} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     * log call occurs.
     * 
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.WARN) {
            // LogFileHelper.getInstance().writeLog(tag, tr);
            return android.util.Log.w(tag, tr);
        }
        return 0;
    }

    /**
     * Send an {@link #ERROR} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        if (LOG_LEVEL >= android.util.Log.ERROR) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }

    public static void e(Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.ERROR) {
            if (tr != null) {
                tr.printStackTrace();
            }
        }
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     *            log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL >= android.util.Log.ERROR) {
            // LogFileHelper.getInstance().writeLog(tag, msg);
            return android.util.Log.e(tag, msg, tr);
        }
        return 0;
    }

}

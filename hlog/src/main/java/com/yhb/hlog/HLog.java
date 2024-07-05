package com.yhb.hlog;

import android.content.Context;
import android.graphics.Bitmap;
import com.yhb.hlog.callback.HLogCallback;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.config.LogMode;
import com.yhb.hlog.controller.LogManager;
import com.yhb.hlog.crash.HLogCrashHandler;
import java.io.File;
import java.util.Date;
import java.util.List;

/**日志工具（对外使用）*/
public class HLog {

    /**TAG*/
    private static final String TAG = "HLog";

    /**日志管理器*/
    private static LogManager logManager;

    /**初始化*/
    public static void initialize(Context applicationContext, LogConfig logConfig){
        HLogCrashHandler.initialize(logConfig);
        logManager = new LogManager(applicationContext, logConfig);
    }

    /**错误*/
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }
    public static void e(String tag, String msg, HLogCallback callback) {
        logManager.write(LogMode.ERROR, tag, msg, null, callback);
    }
    public static void e(Bitmap bitmap) {
        e(bitmap, null);
    }
    public static void e(Bitmap bitmap, HLogCallback callBack) {
        logManager.write(LogMode.ERROR, null, null, bitmap, callBack);
    }

    /**警告*/
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }
    public static void w(String tag, String msg, HLogCallback callback) {
        logManager.write(LogMode.WARNING, tag, msg, null, callback);
    }
    public static void w(Bitmap bitmap) {
        w(bitmap, null);
    }
    public static void w(Bitmap bitmap, HLogCallback callBack) {
        logManager.write(LogMode.WARNING, null, null, bitmap, callBack);
    }

    /**成功*/
    public static void s(String tag, String msg) {
        s(tag, msg, null);
    }
    public static void s(String tag, String msg, HLogCallback callback) {
        logManager.write(LogMode.SUCCESS, tag, msg, null, callback);
    }
    public static void s(Bitmap bitmap) {
        s(bitmap, null);
    }
    public static void s(Bitmap bitmap, HLogCallback callBack) {
        logManager.write(LogMode.SUCCESS, null, null, bitmap, callBack);
    }

    /**普通*/
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }
    public static void i(String tag, String msg, HLogCallback callback) {
        logManager.write(LogMode.INFO, tag, msg, null, callback);
    }
    public static void i(Bitmap bitmap) {
        i(bitmap, null);
    }
    public static void i(Bitmap bitmap, HLogCallback callBack) {
        logManager.write(LogMode.INFO, null, null, bitmap, callBack);
    }

    /**查找*/
    public static List<File> find() {
        return find(null, null);
    }
    public static List<File> find(LogMode logMode) {
        return find(logMode, null);
    }
    public static List<File> find(Date date) {
        return find(null, date);
    }
    public static List<File> find(LogMode logMode, Date date) {
        return logManager.find(logMode, date);
    }

    /**清除*/
    public static void clear() {
        clear(null, null);
    }
    public static void clear(LogMode logMode) {
        clear(logMode, null);
    }
    public static void clear(Date date) {
        clear(null, date);
    }
    public static void clear(LogMode logMode, Date date) {
        logManager.clear(logMode, date);
    }

}
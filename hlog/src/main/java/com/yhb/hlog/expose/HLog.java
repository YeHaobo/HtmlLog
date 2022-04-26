package com.yhb.hlog.expose;

import android.content.Context;
import android.graphics.Bitmap;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.crash.CrashHandler;
import com.yhb.hlog.controller.LogController;
import com.yhb.hlog.config.type.LogType;
import java.io.File;
import java.util.Date;
import java.util.List;

/**日志工具*/
public class HLog{
    public static final String TAG = "HLog";
    private static LogController logController;
    public static void initialize(Context applicationContext, LogConfig logConfig){
        logController = new LogController(applicationContext,logConfig);
        if(logConfig.isRecordCrash()) CrashHandler.initialize(logConfig.getCrashCallBack());
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }
    public static void e(String tag, String msg, LogCallBack callback) {
        logController.write(LogType.ERROR,tag,msg,null,callback);
    }
    public static void e(Bitmap bitmap) {
        e(bitmap, null);
    }
    public static void e(Bitmap bitmap, LogCallBack callBack) {
        logController.write(LogType.ERROR,"ERROR-IMG",null,bitmap,callBack);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }
    public static void w(String tag, String msg, LogCallBack callback) {
        logController.write(LogType.WARNING,tag,msg,null,callback);
    }
    public static void w(Bitmap bitmap) {
        w(bitmap, null);
    }
    public static void w(Bitmap bitmap, LogCallBack callBack) {
        logController.write(LogType.WARNING,"WARNING-IMG",null,bitmap,callBack);
    }

    public static void s(String tag, String msg) {
        s(tag, msg, null);
    }
    public static void s(String tag, String msg, LogCallBack callback) {
        logController.write(LogType.SUCCESS,tag,msg,null,callback);
    }
    public static void s(Bitmap bitmap) {
        s(bitmap, null);
    }
    public static void s(Bitmap bitmap, LogCallBack callBack) {
        logController.write(LogType.SUCCESS,"SUCCESS-IMG",null,bitmap,callBack);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }
    public static void i(String tag, String msg, LogCallBack callback) {
        logController.write(LogType.INFO,tag,msg,null,callback);
    }
    public static void i(Bitmap bitmap) {
        i(bitmap, null);
    }
    public static void i(Bitmap bitmap, LogCallBack callBack) {
        logController.write(LogType.INFO,"INFO-IMG",null,bitmap,callBack);
    }

    public static List<File> getLogFile() {
        return getLogFile(null,null);
    }
    public static List<File> getLogFile(Date date) {
        return getLogFile(null,date);
    }
    public static List<File> getLogFile(String logType) {
        return getLogFile(logType,null);
    }
    public static List<File> getLogFile(String logType, Date date) {
        return logController.logFile(logType,date);
    }

    public static boolean clearLogFile() {
        return clearLogFile(null,null);
    }
    public static boolean clearLogFile(Date date) {
        return clearLogFile(null,date);
    }
    public static boolean clearLogFile(String logType) {
        return clearLogFile(logType,null);
    }
    public static boolean clearLogFile(String logType, Date date) {
        return logController.logClear(logType,date);
    }
    
}
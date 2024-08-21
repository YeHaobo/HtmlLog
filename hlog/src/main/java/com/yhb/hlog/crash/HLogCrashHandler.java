package com.yhb.hlog.crash;

import android.os.Looper;

import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.HLog;
import com.yhb.hlog.callback.HLogCallback;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**未捕获异常处理器*/
public class HLogCrashHandler implements Thread.UncaughtExceptionHandler{

    /**TAG*/
    private static final String TAG = "HLogCrashHandler";

    /**初始化*/
    public static void initialize(LogConfig config){
        if(config.crash()){
            HLogCrashHandler hLogCrashHandler = new HLogCrashHandler(config.crashCallback());
            Thread.setDefaultUncaughtExceptionHandler(hLogCrashHandler);
        }
    }

    /**异常捕获回调*/
    private HLogCrashCallback crashCallback;
    /**系统默认异常处理*/
    private Thread.UncaughtExceptionHandler defaultHandler;

    /**实例*/
    private HLogCrashHandler(HLogCrashCallback crashCallback) {
        this.crashCallback = crashCallback;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**异常捕获*/
    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        String carshInfo = "HLog caught exception: ";
        if(throwable == null){
            carshInfo = carshInfo + "Throwable is null";
        }else{
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.close();
            carshInfo = carshInfo + "\n" + writer.toString();
        }
        HLog.e(TAG, carshInfo, new HLogCallback(Looper.myLooper()) {
            @Override
            public void onCallback(File logFile) {
                if(crashCallback == null || !crashCallback.onCrashCallback(thread, throwable, logFile)){
                    if(defaultHandler != null) defaultHandler.uncaughtException(thread, throwable);
                }
            }
        });
    }

}
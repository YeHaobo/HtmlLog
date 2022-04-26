package com.yhb.hlog.crash;

import com.yhb.hlog.expose.HLog;
import com.yhb.hlog.expose.LogCallBack;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**异常崩溃*/
public class CrashHandler implements Thread.UncaughtExceptionHandler{

    /**单例模式*/
    volatile private static CrashHandler crashHandler;
    public static void initialize(CrashCallBack callBack){
        if(crashHandler == null) crashHandler = new CrashHandler(callBack);
    }

    /**崩溃回调*/
    private CrashCallBack crashCallBack;
    /**系统默认异常处理*/
    private Thread.UncaughtExceptionHandler defaultHandler;

    /**实例*/
    private CrashHandler(CrashCallBack crashCallBack) {
        this.crashCallBack = crashCallBack;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**异常回调*/
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(e != null) handleException(e);
        if(defaultHandler != null)defaultHandler.uncaughtException(t, e);
    }

    /**异常处理*/
    private void handleException(Throwable e){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.close();
        HLog.e("CRASH", writer.toString(), new LogCallBack() {
            @Override
            public void callBack(File file) {
                if(crashCallBack != null) crashCallBack.callBack(file);
            }
        });
        //等待日志异步写入
        try{
            Thread.sleep(3000);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
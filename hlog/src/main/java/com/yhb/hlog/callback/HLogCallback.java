package com.yhb.hlog.callback;

import android.os.Handler;
import android.os.Looper;
import java.io.File;

/**
 * 日志内容异步写入完成回调
 * 写入执行在HLog线程
 * 回调线程可自定义
 */
public abstract class HLogCallback {

    /**回调（线程可自定义）*/
    public abstract void onCallback(File logFile);

    /**回调线程*/
    private Handler handler;

    /**构造*/
    public HLogCallback() {
        this(null);
    }

    /**构造（默认回调在主线程）*/
    public HLogCallback(Looper looper) {
        this.handler = new Handler(looper != null ? looper : Looper.getMainLooper());
    }

    /**切换线程回调*/
    public void exeCallback(final File logFile) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onCallback(logFile);
            }
        });
    }

}
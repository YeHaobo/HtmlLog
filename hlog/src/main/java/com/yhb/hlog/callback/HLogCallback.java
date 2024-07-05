package com.yhb.hlog.callback;

import java.io.File;

/**
 * 日志内容异步写入完成回调
 * 写入执行在HLog线程
 * 回调线程可自定义
 */
public abstract class HLogCallback {

    /**回调（线程可自定义）*/
    public abstract void onCallback(File logFile);

    /**回调线程的Looper模式*/
    private HLogLooper logLooper;

    /**构造（默认回调在主线程）*/
    public HLogCallback() {
        this(HLogLooper.MAIN);
    }

    /**构造（自定义回调线程模式）*/
    public HLogCallback(HLogLooper logLooper) {
        this.logLooper = logLooper;
    }

    /**获取线程模式*/
    public HLogLooper logLooper() {
        return logLooper;
    }

}
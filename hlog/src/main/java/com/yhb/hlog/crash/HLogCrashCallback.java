package com.yhb.hlog.crash;

import java.io.File;

/**异常捕获回调*/
public interface HLogCrashCallback {

    /**
     * 返回true时表示无需系统处理，返回false时则表示需要系统处理（即停止运行或崩溃）
     * 回调在HLog子线程中
     */
    boolean onCrashCallback(Thread t, Throwable e, File logFile);

}
package com.yhb.hlog.callback;

/**日志回调所在Looper*/
public enum HLogLooper {

    /**主线程，回调在主线程（默认）*/
    MAIN,

    /**调用线程，回调在当前调用线程*/
    POSTING,

    /**HLog线程，回调在HLog内部子线程*/
    HLOG;

}
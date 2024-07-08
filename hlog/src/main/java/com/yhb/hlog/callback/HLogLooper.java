package com.yhb.hlog.callback;

/**日志回调所在Looper*/
public enum HLogLooper {

    /**主线程，回调在主线程（默认）*/
    MAIN,

    /**调用线程，在调用线程回调（注意：若调用线程的Looper.myLooper()为空则会使用新的子线程回调）*/
    POSTING,

    /**HLog线程，回调在HLog内部子线程*/
    HLOG;

}
package com.yhb.hlog.callback;

/**日志回调所在Looper*/
public enum HLogLooper {

    /**主线程，回调在主线程（默认）*/
    MAIN,

    /**调用线程，在调用线程回调（注意：调用线程必须已经开始循环（即已执行Looper.prepare()），否则将不会回调）*/
    POSTING,

    /**HLog线程，回调在HLog内部子线程*/
    HLOG;

}
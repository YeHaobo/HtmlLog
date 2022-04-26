package com.yhb.hlog.crash;

import java.io.File;

/**异常崩溃回调*/
public interface CrashCallBack {
    void callBack(File file);
}
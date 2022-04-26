package com.yhb.hlog.expose;

import java.io.File;

/**异步写入回调接口*/
public interface LogCallBack {
    void callBack(File file);
}
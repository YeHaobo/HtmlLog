package com.yhb.hlog.config;

/**日志文件类型*/
public enum FileType {

    /**纯文本*/
    TXT(".txt"),

    /**html*/
    HTML(".html");

    /**后缀名*/
    private String postfix;

    /**构造*/
    FileType(String postfix) {
        this.postfix = postfix;
    }

    /**后缀名*/
    public String postfix() {
        return postfix;
    }

}
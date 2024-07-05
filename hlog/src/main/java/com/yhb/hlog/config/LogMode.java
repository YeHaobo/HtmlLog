package com.yhb.hlog.config;

/**日志写入类型*/
public enum LogMode {

    /**正常*/
    INFO("info"),

    /**成功*/
    SUCCESS("success"),

    /**警告*/
    WARNING("warning"),

    /**错误*/
    ERROR("error");

    /**译文*/
    private String mean;

    /**构造*/
    LogMode(String mean) {
        this.mean = mean;
    }

    /**译文*/
    public String mean() {
        return mean;
    }

}
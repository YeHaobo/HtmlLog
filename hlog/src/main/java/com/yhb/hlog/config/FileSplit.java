package com.yhb.hlog.config;

/**日志文件分批保存类型*/
public enum FileSplit {

    /**仅使用一个日志文件*/
    ONE("one"),

    /**依据类型分批日志文件*/
    MODE("mode"),

    /**依据日期分批日志文件*/
    DAY("day"),

    /**先依据日期分批后，再依据类型分批日志文件*/
    DAY_MODE("dayMode"),

    /**先依据类型分批后，再依据日期分批日志文件*/
    MODE_DAY("modeDay");

    /**分批文件夹名称*/
    private String splitName;

    /**构造*/
    FileSplit(String splitName) {
        this.splitName = splitName;
    }

    /**分批文件夹名称*/
    public String splitName() {
        return splitName;
    }

}
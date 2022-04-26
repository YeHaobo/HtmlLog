package com.yhb.hlog.config.type;

/**保存类型*/
public interface SaveType {
    /**仅使用一个日志文件*/
    int ONLY_ONE = 0;

    /**依据类型分批日志文件*/
    int SPLIT_TYPE = 1;

    /**依据日期分批日志文件*/
    int SPLIT_DAY = 2;

    /**依据日期和类型分批日志文件*/
    int SPLIT_DAY_AND_TYPE = 3;

    /**依据类型和日期分批日志文件*/
    int SPLIT_TYPE_AND_DAY = 4;

}
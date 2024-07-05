package com.yhb.hlog.config;

import android.os.Environment;
import android.util.Size;
import com.yhb.hlog.R;
import com.yhb.hlog.crash.HLogCrashCallback;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**日志配置类*/
public class LogConfig {

    /**是否启用Debug模式，该模式下logcat打印日志内容*/
    private boolean debug = true;

    /**是否启用崩溃/异常主动记录*/
    private boolean crash = true;
    /**崩溃/异常主动回调接口*/
    private HLogCrashCallback crashCallback = null;

    /**文件类型*/
    private FileType fileType = FileType.HTML;
    /**文件分批保存类型*/
    private FileSplit fileSplit = FileSplit.DAY;
    /**文件编码格式*/
    private String fileCharset = "UTF-8";
    /**文件保存根路径*/
    private String fileRootPath = Environment.getExternalStorageDirectory().getPath() + "/HtmlLog";
    /**文件保留天数（单个日志记录无效）*/
    private int fileMaxDay = 7;

    /**日期格式（日志文件/文件夹使用此命名）*/
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    /**时间戳格式（日志内部的单条日志头部使用此时间格式）*/
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**文字颜色（错误）*/
    private int fontColorError = R.color.error;
    /**文字颜色（警告）*/
    private int fontColorWarning = R.color.warning;
    /**文字颜色（成功）*/
    private int fontColorSuccess = R.color.success;
    /**文字颜色（普通）*/
    private int fontColorInfo = R.color.info;
    /**文字大小（单位：px）*/
    private int fontSize = 13;
    /**文字粗细*/
    private int fontWeight = 600;
    /**文字边距（单位：px）*/
    private int fontMargin = 0;

    /**图片属性*/
    private ImgAttr imgAttr = ImgAttr.CONTAIN;
    /**图片大小（单位：px，长宽为0时，对应长宽自适应）*/
    private Size imgSize = new Size(360,0);
    /**图片边距（单位：px）*/
    private int imgMargin = 0;

    /**私有构造*/
    private LogConfig(){}

    /**创建*/
    public static LogConfig Create(){
        return new LogConfig();
    }

    /**************************信息配置********************************/
    public LogConfig debug(boolean enable){
        this.debug = enable;
        return this;
    }

    public LogConfig crash(boolean enable) {
        this.crash = enable;
        return this;
    }
    public LogConfig crashCallback(HLogCrashCallback callback){
        this.crashCallback = callback;
        return this;
    }

    public LogConfig fileType(FileType type) {
        this.fileType = type;
        return this;
    }
    public LogConfig fileSplit(FileSplit split) {
        this.fileSplit = split;
        return this;
    }
    public LogConfig fileCharset(String charset){
        this.fileCharset = charset;
        return this;
    }
    public LogConfig fileRootPath(String path) {
        this.fileRootPath = path;
        return this;
    }
    public LogConfig fileMaxDay(int count) {
        this.fileMaxDay = count;
        return this;
    }

    public LogConfig dateFormat(String pattern) {
        this.dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return this;
    }
    public LogConfig timestampFormat(String pattern) {
        this.timestampFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return this;
    }

    public LogConfig fontColorError(int resId) {
        this.fontColorError = resId;
        return this;
    }
    public LogConfig fontColorWarning(int resId) {
        this.fontColorWarning = resId;
        return this;
    }
    public LogConfig fontColorSuccess(int resId) {
        this.fontColorSuccess = resId;
        return this;
    }
    public LogConfig fontColorInfo(int resId) {
        this.fontColorInfo = resId;
        return this;
    }
    public LogConfig fontSize(int px) {
        this.fontSize = px;
        return this;
    }
    public LogConfig fontWeight(int weight) {
        this.fontWeight = weight;
        return this;
    }
    public LogConfig fontMargin(int px) {
        this.fontMargin = px;
        return this;
    }

    public LogConfig imgAttr(ImgAttr attr) {
        this.imgAttr = attr;
        return this;
    }
    public LogConfig imgSize(Size size) {
        this.imgSize = size;
        return this;
    }
    public LogConfig imgMargin(int px) {
        this.imgMargin = px;
        return this;
    }
    /**************************************************************/


    /****************************配置信息获取************************/
    public boolean debug() {
        return debug;
    }

    public boolean crash() {
        return crash;
    }
    public HLogCrashCallback crashCallback() {
        return crashCallback;
    }

    public FileType fileType() {
        return fileType;
    }
    public FileSplit fileSplit() {
        return fileSplit;
    }
    public String fileCharset(){
        return fileCharset;
    }
    public String fileRootPath() {
        return fileRootPath;
    }
    public int fileMaxDay() {
        return fileMaxDay;
    }

    public SimpleDateFormat dateFormat() {
        return dateFormat;
    }
    public SimpleDateFormat timestampFormat() {
        return timestampFormat;
    }

    public int fontColorError() {
        return fontColorError;
    }
    public int fontColorWarning() {
        return fontColorWarning;
    }
    public int fontColorSuccess() {
        return fontColorSuccess;
    }
    public int fontColorInfo() {
        return fontColorInfo;
    }
    public int fontSize() {
        return fontSize;
    }
    public int fontWeight() {
        return fontWeight;
    }
    public int fontMargin() {
        return fontMargin;
    }

    public ImgAttr imgAttr() {
        return imgAttr;
    }
    public Size imgSize() {
        return imgSize;
    }
    public int imgMargin() {
        return imgMargin;
    }

}
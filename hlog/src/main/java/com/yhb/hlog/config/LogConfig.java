package com.yhb.hlog.config;

import android.os.Environment;
import android.util.Size;
import com.yhb.hlog.R;
import com.yhb.hlog.config.type.SaveType;
import com.yhb.hlog.config.type.ImgType;
import com.yhb.hlog.config.type.FileType;
import com.yhb.hlog.crash.CrashCallBack;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**日志类型配置*/
public class LogConfig {
    /**日志 文件/文件夹 命名格式（yyyy-MM-dd）*/
    private final SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    /**日志中的时间格式（yyyy-MM-dd HH:mm:ss）*/
    private SimpleDateFormat logTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

    /**是否启用 崩溃/异常 主动记录*/
    private boolean recordCrash = true;
    /**崩溃主动回调接口*/
    private CrashCallBack crashCallBack;

    /**是否开启Debug模式，该模式下logcat将会打印日志内容*/
    private boolean isDebug = true;

    /**文件类型*/
    private String fileType = FileType.HTML;
    /**文件分批方式*/
    private int saveType = SaveType.SPLIT_DAY;
    /**文件路径*/
    private String rootPath = Environment.getExternalStorageDirectory() + "/HtmlLog/";
    /**保留天数（单位：天，单个日志记录无效）*/
    private int maxDay = 7;

    /**编码格式*/
    private String charsetName = "UTF-8";

    /**错误颜色*/
    private int errorColor = R.color.error;
    /**警告颜色*/
    private int warningColor = R.color.warning;
    /**成功颜色*/
    private int successColor = R.color.success;
    /**普通颜色*/
    private int infoColor = R.color.info;
    /**文字大小(单位：px)*/
    private int txtSize = 13;
    /**文字粗细*/
    private int txtWeight = 600;
    /**文字边距（单位：px）*/
    private int txtMargin = 0;
    /**图片展示类型*/
    private String imgType = ImgType.CONTAIN;
    /**图片大小（单位：px，长宽为0时，对应长宽自适应）*/
    private Size imgSize = new Size(360,0);
    /**图片边距（单位：px）*/
    private int imgMargin = 0;

    /**私有构造方法*/
    private LogConfig(){}

    /**创建配置*/
    public static LogConfig Create(){
        return new LogConfig();
    }

    /**信息配置*/
    public LogConfig logTimeFormat(SimpleDateFormat logTimeFormat) {
        this.logTimeFormat = logTimeFormat;
        return this;
    }

    public LogConfig recordCrash(boolean recordCrash) {
        this.recordCrash = recordCrash;
        return this;
    }
    public LogConfig crashCallBack(CrashCallBack crashCallBack){
        this.crashCallBack = crashCallBack;
        return this;
    }

    public LogConfig isDebug(boolean isDebug){
        this.isDebug = isDebug;
        return this;
    }

    public LogConfig fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
    public LogConfig saveType(int saveType) {
        this.saveType = saveType;
        return this;
    }
    public LogConfig rootPath(String rootPath) {
        this.rootPath = ("/".equals(rootPath.substring(rootPath.length() - 1))) ? rootPath : (rootPath + "/");
        return this;
    }
    public LogConfig maxDay(int maxDay) {
        this.maxDay = maxDay;
        return this;
    }

    public LogConfig charsetName(String charsetName){
        this.charsetName = charsetName;
        return this;
    }

    public LogConfig errorColor(int errorColor) {
        this.errorColor = errorColor;
        return this;
    }
    public LogConfig warningColor(int warningColor) {
        this.warningColor = warningColor;
        return this;
    }
    public LogConfig successColor(int successColor) {
        this.successColor = successColor;
        return this;
    }
    public LogConfig infoColor(int infoColor) {
        this.infoColor = infoColor;
        return this;
    }

    public LogConfig txtSize(int txtSize) {
        this.txtSize = txtSize;
        return this;
    }
    public LogConfig txtWeight(int txtWeight) {
        this.txtWeight = txtWeight;
        return this;
    }
    public LogConfig txtMargin(int txtMargin) {
        this.txtMargin = txtMargin;
        return this;
    }

    public LogConfig imgType(String imgType) {
        this.imgType = imgType;
        return this;
    }
    public LogConfig imgSize(Size imgSize) {
        this.imgSize = imgSize;
        return this;
    }
    public LogConfig imgMargin(int imgMargin) {
        this.imgMargin = imgMargin;
        return this;
    }


    /**配置信息获取*/
    public SimpleDateFormat getFileNameFormat() {
        return fileNameFormat;
    }
    public SimpleDateFormat getLogTimeFormat() {
        return logTimeFormat;
    }

    public boolean isRecordCrash() {
        return recordCrash;
    }
    public CrashCallBack getCrashCallBack() {
        return crashCallBack;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getFileType() {
        return fileType;
    }
    public int getSaveType() {
        return saveType;
    }
    public String getRootPath() {
        return rootPath + saveType + "/";
    }
    public int getMaxDay() {
        return maxDay;
    }

    public String getCharsetName(){
        return charsetName;
    }

    public int getErrorColor() {
        return errorColor;
    }
    public int getWarningColor() {
        return warningColor;
    }
    public int getSuccessColor() {
        return successColor;
    }
    public int getInfoColor() {
        return infoColor;
    }

    public int getTxtSize() {
        return txtSize;
    }
    public int getTxtWeight() {
        return txtWeight;
    }
    public int getTxtMargin() {
        return txtMargin;
    }

    public String getImgType() {
        return imgType;
    }
    public Size getImgSize() {
        return imgSize;
    }
    public int getImgMargin() {
        return imgMargin;
    }

}
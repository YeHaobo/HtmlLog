package com.yhb.hlog.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Size;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.config.LogMode;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**日志信息转换器*/
public class LogInfoParser {

    /**日志写入的线程名*/
    private static final String TAG = "LogInfoParser";

    /**application上下文*/
    private Context context;
    /**配置信息*/
    private LogConfig config;

    /**构造*/
    public LogInfoParser(Context applicationContext, LogConfig logConfig) {
        this.context = applicationContext;
        this.config = logConfig;
    }

    /**logcat文本获取*/
    public String logcat(String msg, Bitmap bitmap){
        if(bitmap != null){
            String size = Formatter.formatFileSize(context, bitmap.getByteCount());
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            return "write bitmap [width=" + width + " height=" + height + " size=" + size + "]";
        }
        return msg == null ? "" : msg;
    }

    /**日志文本转换*/
    public String parse(LogMode logMode, String tag, String msg, Bitmap bitmap){
        switch (config.fileType()){
            case TXT:
                if(msg != null) return txt(logMode, tag, msg);
            case HTML:
                if(msg != null) return html(logMode, tag, msg);
                if(bitmap != null) return img(bitmap);
            default: return "";
        }
    }

    /**普通*/
    private String txt(LogMode logMode, String tag, String msg){
        String timestamp = config.timestampFormat().format(new Date(System.currentTimeMillis()));
        String mode = logMode.mean();
        return timestamp + " [" + mode + "] " + tag + ": " + msg;
    }

    /**标签*/
    private String html(LogMode logMode, String tag, String msg){
        int colorResId;
        switch (logMode) {
            case ERROR: colorResId = config.fontColorError(); break;
            case INFO: colorResId = config.fontColorInfo(); break;
            case SUCCESS: colorResId = config.fontColorSuccess(); break;
            case WARNING: colorResId = config.fontColorWarning(); break;
            default: colorResId = config.fontColorInfo(); break;
        }
        int color = context.getResources().getColor(colorResId);
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        String rgb = "color:rgb(" + red + "," + green + "," + blue + ");";
        String size = "font-size:" + config.fontSize() + "px;";
        String weight = "font-weight:" + config.fontWeight() + ";";
        String margin = "margin:" + config.fontMargin() + "px;";
        String space = "white-space:pre-wrap;";
        String style = "style=\"" + rgb + size + weight + margin + space + "\"";
        String txt = txt(logMode, tag, msg);
        return "<p " + style + ">" + txt + "</p>";
    }

    /**图片*/
    private String img(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String img = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String src = "src='data:image/png;base64," + img +"' ";
        Size size = config.imgSize();
        String width = "width:" + (size.getWidth() > 0 ? (size.getWidth() + "px;") : "auto;");
        String height = "height:" + (size.getHeight() > 0 ? (size.getHeight() + "px;") : "auto;");
        String fit = "object-fit:" + config.imgAttr().name() +";";
        String margin = "margin:" + config.imgMargin() + "px;";
        String style = "style=\"" + width + height + fit + margin + "\"";
        return "<img " + style + src + "></img>";
    }

}
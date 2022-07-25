package com.yhb.hlog.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Size;
import com.yhb.hlog.expose.LogCallBack;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.permission.PermissionUtil;
import com.yhb.hlog.config.type.FileType;
import com.yhb.hlog.config.type.LogType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;

/**日志控制类*/
public class LogController {
    /**日志写入的线程名*/
    private static final String NAME = "HLog";
    /**application上下文*/
    private Context applicationContext;
    /**配置信息*/
    private LogConfig logConfig;
    /**文件控制器*/
    private FileController fileController;
    /**Handler*/
    private Handler handler;

    /**构造*/
    public LogController(Context applicationContext, LogConfig logConfig) {
        this.applicationContext = applicationContext;
        this.logConfig = logConfig;
        this.fileController = new FileController(logConfig);
        HandlerThread handlerThread = new HandlerThread(NAME);
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper());
    }

    /**普通文本*/
    private String txt(String logType, String tag, String msg){
        String time = logConfig.getLogTimeFormat().format(new Date(System.currentTimeMillis()));
        return time + " [" + logType + "] " + tag + ": " + msg;
    }

    /**标签文本*/
    private String html(String logType, String tag, String msg){
        int res = logConfig.getInfoColor();
        switch (logType) {
            case LogType.ERROR: res = logConfig.getErrorColor();break;
            case LogType.INFO: res = logConfig.getInfoColor();break;
            case LogType.SUCCESS: res = logConfig.getSuccessColor();break;
            case LogType.WARNING: res = logConfig.getWarningColor();break;
            default: break;
        }
        int color = applicationContext.getResources().getColor(res);
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        String rgb = "color:rgb(" + red + "," + green + "," + blue + ");";

        String size = "font-size:" + logConfig.getTxtSize() + "px;";
        String weight = "font-weight:" + logConfig.getTxtWeight() + ";";
        String margin = "margin:" + logConfig.getTxtMargin() + "px;";
        String space = "white-space:pre-wrap;";

        String style = "style=\"" + rgb + size + weight + margin + space + "\"";

        return "<p " + style + ">" + txt(logType, tag, msg) + "</p>";
    }

    /**图片*/
    private String img(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String img = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String src = "src='data:image/png;base64," + img +"' ";

        Size size = logConfig.getImgSize();
        String width = "width:" + (size.getWidth() > 0 ? (size.getWidth() + "px;") : "auto;");
        String height = "height:" + (size.getHeight() > 0 ? (size.getHeight() + "px;") : "auto;");
        String fit = "object-fit:" + logConfig.getImgType() +";";
        String margin = "margin:" + logConfig.getImgMargin() + "px;";

        String style = "style=\"" + width + height + fit + margin + "\"";

        return "<img " + style + src + "></img>";
    }

    /**写入*/
    public void write(final String logType, final String tag, final String msg, final Bitmap bitMap, final LogCallBack callBack){
        if(!PermissionUtil.hasPermission(applicationContext)) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String str = "";
                switch (logConfig.getFileType()){
                    case FileType.TXT:
                        if(msg != null) str = txt(logType,tag,msg);
                        break;
                    case FileType.HTML:
                        if (msg != null) str = html(logType, tag, msg);
                        if (bitMap != null) str = img(bitMap);
                        break;
                    default: return;
                }
                if(!str.isEmpty()) fileController.write(logType, str + "\n", callBack);
            }
        };
        handler.post(runnable);
    }

    /**获取日志*/
    public List<File> logFile(String logType, Date date){
        return fileController.find(logType,date);
    }

    /**清除日志*/
    public boolean logClear(String logType, Date date){
        return fileController.delete(logType, date);
    }

}
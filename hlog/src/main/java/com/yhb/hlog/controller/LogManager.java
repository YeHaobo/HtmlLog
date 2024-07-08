package com.yhb.hlog.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.config.LogMode;
import com.yhb.hlog.callback.HLogCallback;
import com.yhb.hlog.permission.PermissionUtil;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.List;

/**日志管理器*/
public class LogManager {

    /**TAG*/
    private static final String TAG = "LogManager";
    /**子线程名*/
    private static final String NAME = "HLog";
    /**主进程上下文*/
    private Context context;
    /**日志配置信息*/
    private LogConfig config;
    /**日志文件提供者*/
    private LogFileProvider fileProvider;
    /**日志信息转换器*/
    private LogInfoParser infoParser;
    /**异步线程handler*/
    private Handler hlogHandler;

    /**构造*/
    public LogManager(Context applicationContext, LogConfig logConfig) {
        this.context = applicationContext;
        this.config = logConfig;
        this.fileProvider = new LogFileProvider(config);
        this.infoParser = new LogInfoParser(context, config);
        HandlerThread handlerThread = new HandlerThread(NAME);
        handlerThread.start();
        this.hlogHandler = new Handler(handlerThread.getLooper());
    }

    /**写日志*/
    public void write(final LogMode logMode, final String tag, final String msg, final Bitmap bitmap, final HLogCallback callback){
        if(config.debug()){
            Log.e(tag, infoParser.logcat(msg, bitmap));
        }
        if(!PermissionUtil.hasPermission(context)){
            Log.e(TAG, "HLog file read and write permission denied");
            return;
        }
        final Looper postingLooper = Looper.myLooper();//调用线程looper
        hlogHandler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    final File logFile = fileProvider.writeFile(logMode);
                    String logInfo = infoParser.parse(logMode, tag, msg, bitmap) + "\n";
                    RandomAccessFile raf = new RandomAccessFile(logFile, "rwd");
                    raf.seek(raf.length());
                    raf.write(logInfo.getBytes(config.fileCharset()));
                    raf.close();
                    if(callback == null) return;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            callback.onCallback(logFile);
                        }
                    };
                    switch (callback.logLooper()){
                        case MAIN: new Handler(Looper.getMainLooper()).post(runnable);break;
                        case POSTING: if(postingLooper != null) new Handler(postingLooper).post(runnable);else new Thread(runnable).start();break;
                        case HLOG: hlogHandler.post(runnable);break;
                        default: throw new Exception("HLogLooper " + callback.logLooper().name() + " is unknown");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG,"HLog writing error: " + e.toString());
                }
            }
        });
    }

    /**获取日志*/
    public List<File> find(LogMode logMode, Date date){
        return fileProvider.find(logMode, date);
    }

    /**清除日志*/
    public void clear(LogMode logMode, Date logDate){
        fileProvider.delete(logMode, logDate);
    }

}
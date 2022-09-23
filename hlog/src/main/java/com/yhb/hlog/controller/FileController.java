package com.yhb.hlog.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.yhb.hlog.expose.LogCallBack;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.expose.HLog;
import com.yhb.hlog.config.type.SaveType;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**文件控制器*/
public class FileController {
    /**配置*/
    private LogConfig logConfig;

    /**主/UI 线程Handler*/
    private Handler mainHandler;

    /**构造*/
    public FileController(LogConfig logConfig) {
        this.logConfig = logConfig;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /**文件夹创建*/
    private File createDir(String dirPath){
        File dirFile = new File(dirPath);
        if(!dirFile.exists())dirFile.mkdirs();
        return dirFile;
    }

    /**文件创建*/
    private File createFile(String logPath){
        File logFile = new File(logPath);
        try{
            if(!logFile.exists()) logFile.createNewFile();
            return logFile;
        }catch (Exception e){
            Log.e(HLog.TAG,"Description Failed to create a log file");
            e.printStackTrace();
            return null;
        }
    }

    /**数量控制*/
    private boolean examineFile(File dirFile) {
        if(!dirFile.exists())return false;
        try{
            File[] files = dirFile.listFiles();
            if(files == null || files.length <= logConfig.getMaxDay())return true;
            Calendar cdOut = Calendar.getInstance();
            Calendar cd = Calendar.getInstance();
            File outFile = files[0];
            for(int i = 1; i < files.length; i++){
                String cdOutName = outFile.isDirectory() ? outFile.getName() : outFile.getName().substring(0, outFile.getName().indexOf("."));
                String cdName = files[i].isDirectory() ? files[i].getName() : files[i].getName().substring(0, files[i].getName().indexOf("."));
                cdOut.setTime(logConfig.getFileNameFormat().parse(cdOutName));
                cd.setTime(logConfig.getFileNameFormat().parse(cdName));
                if(cd.before(cdOut)){
                    File t = outFile;
                    outFile = files[i];
                    files[i] = t;
                }
            }
            clearDir(outFile);
            return examineFile(dirFile);
        }catch (Exception e){
            Log.e(HLog.TAG,"Failed to convert folder name");
            e.printStackTrace();
            return false;
        }
    }

    /**删除文件夹及内部所有文件*/
    private void clearDir(File dirFile){
        if(!dirFile.exists())return;
        if(dirFile.isDirectory()){
            File[] files = dirFile.listFiles();
            if(files != null){
                for(File file : files){
                    clearDir(file);
                }
            }
        }
        dirFile.delete();
    }

    /**准备写入日志*/
    private File beforeWrite(String logType){
        File rootFile = createDir(logConfig.getRootPath());//根文件夹创建
        String logPath;//文件名获取
        switch (logConfig.getSaveType()){
            case SaveType.ONLY_ONE: //...HtmlLog/0/log.txt
                logPath = logConfig.getRootPath() + "log" + logConfig.getFileType();
                break;
            case SaveType.SPLIT_TYPE: //...HtmlLog/1/success.txt
                logPath = logConfig.getRootPath() + logType + logConfig.getFileType();
                break;
            case SaveType.SPLIT_DAY: //...HtmlLog/2/2022-04-24.txt
                if(!examineFile(rootFile)) return null;
                logPath = logConfig.getRootPath() + logConfig.getFileNameFormat().format(new Date(System.currentTimeMillis())) + logConfig.getFileType();
                break;
            case SaveType.SPLIT_DAY_AND_TYPE: //...HtmlLog/3/2022-04-24/success.txt
                String dateDirPath = logConfig.getRootPath() + logConfig.getFileNameFormat().format(new Date(System.currentTimeMillis())) + "/";
                createDir(dateDirPath);
                if(!examineFile(rootFile)) return null;
                logPath = dateDirPath + logType + logConfig.getFileType();
                break;
            case SaveType.SPLIT_TYPE_AND_DAY: //...HtmlLog/4/success/2022-04-24.txt
                String typeDirPath = logConfig.getRootPath() + logType + "/";
                File typeDirFile = createDir(typeDirPath);
                if(!examineFile(typeDirFile)) return null;
                logPath = typeDirPath + logConfig.getFileNameFormat().format(new Date(System.currentTimeMillis())) + logConfig.getFileType();
                break;
            default: return null;
        }
        return createFile(logPath);
    }

    /**写入日志（需要子线程中执行）*/
    public void write(String logType, String msg, final LogCallBack callBack) {
        try{
            final File file = beforeWrite(logType);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(raf.length());
            raf.write(msg.getBytes(logConfig.getCharsetName()));
            raf.close();
            if(callBack != null){
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.callBack(file);
                    }
                });
            }
        }catch (Exception e){
            Log.e(HLog.TAG,"Log writing failure " + e.toString());
            e.printStackTrace();
        }
    }

    /**获取日志文件*/
    public List<File> find(String logType, Date date){
        List<File> fileList = new ArrayList<>();
        switch (logConfig.getSaveType()){
            case SaveType.ONLY_ONE: //...HtmlLog/0/log.txt
                fileList.add(new File(logConfig.getRootPath() + "log" + logConfig.getFileType()));
                break;
            case SaveType.SPLIT_TYPE: //...HtmlLog/1/success.txt
                if(logType == null){
                    File[] files = new File(logConfig.getRootPath()).listFiles();
                    if(files == null)break;
                    fileList.addAll(Arrays.asList(files));
                }
                else{
                    fileList.add(new File(logConfig.getRootPath() + logType + logConfig.getFileType()));
                }
                break;
            case SaveType.SPLIT_DAY: //...HtmlLog/2/2022-04-24.txt
                if(date == null){
                    File[] files = new File(logConfig.getRootPath()).listFiles();
                    if(files == null)break;
                    fileList.addAll(Arrays.asList(files));
                }
                else{
                    fileList.add(new File(logConfig.getRootPath() + logConfig.getFileNameFormat().format(date) + logConfig.getFileType()));
                }
                break;
            case SaveType.SPLIT_DAY_AND_TYPE: //...HtmlLog/3/2022-04-24/success.txt
                if(logType == null && date == null){
                    File[] dateFiles = new File(logConfig.getRootPath()).listFiles();
                    if(dateFiles == null)break;
                    for(File dateFile : dateFiles){
                        File[] files = dateFile.listFiles();
                        if(files == null) continue;
                        fileList.addAll(Arrays.asList(files));
                    }
                }
                else if(logType == null){
                    File[] files = new File(logConfig.getRootPath() + logConfig.getFileNameFormat().format(date) + "/").listFiles();
                    if(files == null)break;
                    fileList.addAll(Arrays.asList(files));
                }
                else if(date == null){
                    File[] dateFiles = new File(logConfig.getRootPath()).listFiles();
                    if(dateFiles == null)break;
                    for(File dateFile : dateFiles){
                        fileList.add(new File(dateFile.getPath() + "/" + logType + logConfig.getFileType()));
                    }
                }
                else{
                    fileList.add(new File(logConfig.getRootPath() + logConfig.getFileNameFormat().format(date) + "/" + logType + logConfig.getFileType()));
                }
                break;
            case SaveType.SPLIT_TYPE_AND_DAY: //...HtmlLog/4/success/2022-04-24.txt
                if(logType == null && date == null){
                    File[] typeFiles = new File(logConfig.getRootPath()).listFiles();
                    if(typeFiles == null)break;
                    for(File typeFile : typeFiles){
                        File[] files = typeFile.listFiles();
                        if(files == null)continue;
                        fileList.addAll(Arrays.asList(files));
                    }
                }
                else if(logType == null){
                    File[] typeFiles = new File(logConfig.getRootPath()).listFiles();
                    if(typeFiles == null)break;
                    for(File file : typeFiles){
                        fileList.add(new File(file.getPath() + "/" + logConfig.getFileNameFormat().format(date) + logConfig.getFileType()));
                    }
                }
                else if(date == null){
                    File[] files = new File(logConfig.getRootPath() + logType + "/").listFiles();
                    if(files == null)break;
                    fileList.addAll(Arrays.asList(files));
                }
                else{
                    fileList.add(new File(logConfig.getRootPath() + logType + "/" + logConfig.getFileNameFormat().format(date) + logConfig.getFileType()));
                }
                break;
        }
        for(int i = fileList.size()-1; i >= 0; i--){
            if(!fileList.get(i).exists()) fileList.remove(i);
        }
        if(fileList.size() <= 0) Log.e(HLog.TAG,"There is no log now");
        return fileList;
    }

    /**删除日志*/
    public boolean delete(String logType, Date date){
        List<File> fileList = find(logType,date);
        if(fileList.size() <= 0)return false;
        for(File file : fileList){
            clearDir(file);
        }
        return true;
    }

}
package com.yhb.hlog.controller;

import android.util.Log;
import com.yhb.hlog.config.LogMode;
import com.yhb.hlog.config.LogConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**日志文件提供者*/
public class LogFileProvider {

    /**TAG*/
    private static final String TAG = "LogFileProvider";

    /**配置*/
    private LogConfig config;

    /**构造*/
    public LogFileProvider(LogConfig logConfig) {
        this.config = logConfig;
    }

    /**文件夹创建*/
    private File createFolder(String path){
        File folder = new File(path);
        if(!folder.exists()) folder.mkdirs();
        return folder;
    }

    /**日志文件创建*/
    private File createFile(String path){
        try{
            File file = new File(path);
            if(!file.exists()) file.createNewFile();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "HLog file create error: " + e.getMessage());
            return null;
        }
    }

    /**删除文件/文件夹*/
    private void deleteFile(File file){
        if(!file.exists()) return;
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for(File f : files){
                    deleteFile(f);
                }
            }
        }
        file.delete();
    }

    /**文件/文件夹数量控制*/
    private void examineFile(File file) {
        try{
            if(!file.exists()) return;
            File[] files = file.listFiles();
            if(files == null || files.length <= config.fileMaxDay()) return;
            Calendar cdOut = Calendar.getInstance();
            Calendar cdSave = Calendar.getInstance();
            File outFile = files[0];
            for(int i = 1; i < files.length; i++){
                String cdOutName = outFile.isDirectory() ? outFile.getName() : outFile.getName().substring(0, outFile.getName().lastIndexOf("."));
                String cdSaveName = files[i].isDirectory() ? files[i].getName() : files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
                cdOut.setTime(config.dateFormat().parse(cdOutName));
                cdSave.setTime(config.dateFormat().parse(cdSaveName));
                if(cdSave.before(cdOut)){
                    File t = outFile;
                    outFile = files[i];
                    files[i] = t;
                }
            }
            deleteFile(outFile);
            examineFile(file);
        }catch (Exception e){
            Log.e(TAG, "HLog examine file count error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**当前写入的文件*/
    public File writeFile(LogMode logMode){
        String splitPath = config.fileRootPath() + "/" + config.fileSplit().splitName();
        File splitFolder = createFolder(splitPath);//先创建文件夹
        String logPath;
        switch (config.fileSplit()){
            case ONE: //...HtmlLog/one/hlog.txt
                logPath = splitPath + "/" + "hlog" + config.fileType().postfix();
                break;
            case MODE: //...HtmlLog/mode/success.txt
                logPath = splitPath + "/" + logMode.mean() + config.fileType().postfix();
                break;
            case DAY: //...HtmlLog/day/2022-04-24.txt
                examineFile(splitFolder);
                logPath = splitPath + "/" + config.dateFormat().format(new Date(System.currentTimeMillis())) + config.fileType().postfix();
                break;
            case DAY_MODE: //...HtmlLog/dayMode/2022-04-24/success.txt
                examineFile(splitFolder);
                String dayPath = splitPath + "/" + config.dateFormat().format(new Date(System.currentTimeMillis()));
                createFolder(dayPath);
                logPath = dayPath + "/" + logMode.mean() + config.fileType().postfix();
                break;
            case MODE_DAY: //...HtmlLog/modeDay/success/2022-04-24.txt
                String modePath = splitPath + "/" + logMode.mean();
                File modeFolder = createFolder(modePath);
                examineFile(modeFolder);
                logPath = modePath + "/" + config.dateFormat().format(new Date(System.currentTimeMillis())) + config.fileType().postfix();
                break;
            default:
                return null;
        }
        return createFile(logPath);
    }

    /**获取日志文件*/
    public List<File> find(LogMode logMode, Date logDate){
        List<File> fileList = new ArrayList<>();
        String splitPath = config.fileRootPath() + "/" + config.fileSplit().splitName();
        switch (config.fileSplit()){
            case ONE: //...HtmlLog/one/hlog.txt
                fileList.add(new File(splitPath + "/" + "hlog" + config.fileType().postfix()));
                break;
            case MODE: //...HtmlLog/mode/success.txt
                if(logMode != null) {
                    fileList.add(new File(splitPath + "/" + logMode.mean() + config.fileType().postfix()));
                }else{
                    File[] logFiles = new File(splitPath).listFiles();
                    if(logFiles == null) break;
                    fileList.addAll(Arrays.asList(logFiles));
                }
                break;
            case DAY: //...HtmlLog/day/2022-04-24.txt
                if(logDate != null){
                    fileList.add(new File(splitPath + "/" + config.dateFormat().format(logDate) + config.fileType().postfix()));
                }else{
                    File[] logFiles = new File(splitPath).listFiles();
                    if(logFiles == null)break;
                    fileList.addAll(Arrays.asList(logFiles));
                }
                break;
            case DAY_MODE: //...HtmlLog/dayMode/2022-04-24/success.txt
                if(logMode != null && logDate != null){
                    fileList.add(new File(splitPath + "/" + config.dateFormat().format(logDate) + "/" + logMode.mean() + config.fileType().postfix()));
                }else if(logMode != null){
                    File[] datefolders = new File(splitPath).listFiles();
                    if(datefolders == null)break;
                    for(File datefolder : datefolders){
                        fileList.add(new File(datefolder.getPath() + "/" + logMode.mean() + config.fileType().postfix()));
                    }
                }else if(logDate != null){
                    File[] logFiles = new File(splitPath + "/" + config.dateFormat().format(logDate)).listFiles();
                    if(logFiles == null)break;
                    fileList.addAll(Arrays.asList(logFiles));
                }else{
                    File[] datefolders = new File(splitPath).listFiles();
                    if(datefolders == null)break;
                    for(File datefolder : datefolders){
                        File[] logFiles = datefolder.listFiles();
                        if(logFiles == null) continue;
                        fileList.addAll(Arrays.asList(logFiles));
                    }
                }
                break;
            case MODE_DAY: //...HtmlLog/modeDay/success/2022-04-24.txt
                if(logMode != null && logDate != null){
                    fileList.add(new File(splitPath + "/" + logMode.mean() + "/" + config.dateFormat().format(logDate) + config.fileType().postfix()));
                }else if(logMode != null){
                    File[] logFiles = new File(splitPath + "/" + logMode.mean()).listFiles();
                    if(logFiles == null)break;
                    fileList.addAll(Arrays.asList(logFiles));
                }else if(logDate != null){
                    File[] modeFolders = new File(splitPath).listFiles();
                    if(modeFolders == null)break;
                    for(File modeFolder : modeFolders){
                        fileList.add(new File(modeFolder.getPath() + "/" + config.dateFormat().format(logDate) + config.fileType().postfix()));
                    }
                }else{
                    File[] modeFolders = new File(splitPath).listFiles();
                    if(modeFolders == null)break;
                    for(File modeFolder : modeFolders){
                        File[] logFiles = modeFolder.listFiles();
                        if(logFiles == null)continue;
                        fileList.addAll(Arrays.asList(logFiles));
                    }
                }
                break;
        }
        for(int i = fileList.size()-1; i >= 0; i--){
            if(!fileList.get(i).exists()){
                fileList.remove(i);
            }
        }
        return fileList;
    }

    /**删除日志*/
    public void delete(LogMode logMode, Date logDate){
        List<File> fileList = find(logMode, logDate);
        for(File file : fileList){
            deleteFile(file);
        }
    }

}
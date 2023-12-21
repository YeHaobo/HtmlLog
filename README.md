# HtmlLog
Android日志记录工具

1、分类型记录应用日志，目录结构分时间、类型、时间类型、类型时间。  
2、日志文件可以是txt和html格式，html格式下可以穿插图片。  
3、包含按条件查找指定日志文件，进行查找删除操作。  
4、可开启崩溃主动记录，在应用崩溃时自动记录当前发生的异常并回调。   

|HTML类型|TXT类型|
|:---:|----|
|![](/HTML.png "HTML类型")|![](/TXT.png "TXT类型")|

***

### 依赖
（1）在Project的build.gradle文件中添加
```java
  allprojects {
    repositories {
      ... ...
      maven { url 'https://jitpack.io' }
      ... ...
    }
  }
```
（2）在app的build.gradle文件中添加
```java
  dependencies {
    ... ...
    implementation 'com.android.support:support-v4:28.0.0'//v4 AndroidX项目不用添加
    implementation 'com.github.YeHaobo:HtmlLog:2.7'//HtmlLog
    ... ...
  }
```

### 权限
（1）Android6.0+注意权限的动态获取
```java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

### 初始化
（1）在当前项目的 **application** 中初始化
```java
    @Override
    public void onCreate() {
        super.onCreate();
        ... ...
        //实例配置
        LogConfig config = LogConfig
                .Create()
                .fileType(FileType.TXT)
                .recordCrash(true)
                .saveType(SaveType.SPLIT_DAY)
                .imgSize(new Size(150,0));
        //初始化HtmlLog
        HLog.initialize(getApplicationContext(), config);
        ... ...
    }
```
_注意：initialize(Context,LogConfig);中的 **Context** 必须为 **applicationContext** ,否则可能引发内存泄漏。_

| 方法 | 作用 | 参数说明 |
| :-----------------------------: | :--------------------: | :-----: |
| logTimeFormat(SimpleDateFormat) | 日志时间格式 | yyyy-MM-dd（默认） |
| recordCrash(boolean) | 是否开启崩溃自动记录 | true（默认） |
| crashCallBack(CrashCallBack) | 应用崩溃回调 | CrashCallBack</br>null（默认） |
| isDebug | 是否开启Debug模式，</br>关闭后Logcat将不会打印日志内容 | true（默认） |
| fileType(FileType) | 文件类型 | TXT</br>HTML（默认） |
| saveType(SaveType) | 文件存储类型 | ONLY_ONE单个文件</br>SPLIT_TYPE分类型</br>SPLIT_DAY分日期（默认）</br>SPLIT_DAY_AND_TYPE分日期分类型</br>SPLIT_TYPE_AND_DAY分类型分日期 |
| rootPath(String) | 存储路径 | /storage/sdcard0/HtmlLog/（默认） |
| maxDay(int) | 日志保留天数 | 7（默认）必须大于0 |
| charsetName(String) | 编码格式 | UTF-8（默认） |
| errorColor(int) | error类型文本颜色 | #F56C6C（默认） |
| warningColor(int) | warning类型文本颜色 | #E6A23C（默认） |
| successColor(int) | success类型文本颜色 | #67C23A（默认） |
| infoColor(int) | info类型文本颜色 | #909399（默认） |
| txtSize(int) | 字体大小 | 13（默认） |
| txtWeight(int) | 字体粗细 | 600（默认） |
| txtMargin(int) | 文本边距 | 0（默认） |
| imgType(String) | 图片展示类型 | NONE宽高不变</br>CONTAIN缩放图片,长边完全显示</br>FILL完全适应</br>COVER缩放图片，短边完全显示</br>SCALE_DOWN宽高小于设置使用NONE否则CONTAIN |
| imgSize(Size) | 图片大小 | Size(360,0)(默认)<br/>若宽或高为0时将启用自动判断 |
| imgMargin(int) | 图片边距 |  0（默认） |

_注意：若使用FileType.TXT时，图片写入和日志文本样式的配置将会失效_

### 记录日志
```java
  //INFO类型日志
  HLog.i("INFO","This is INFO message");

  //SUCCESS类型日志
  HLog.s("SUCCESS","This is SUCCESS message");

  //WARING类型日志
  HLog.w("WARING","This is WARING message");

  //ERROR类型日志
  HLog.e("ERROR","This is ERROR message");

  //插入图片
  HLog.i(BitmapFactory.decodeResource(getResources(),R.mipmap.log));
```

### 获取日志
```java
  //获取所有日志文件
  List<File> fileList1 = HLog.getLogFile();

  //获取Date这一天的所有日志文件
  List<File> fileList2 = HLog.getLogFile(new Date(System.currentTimeMillis()));

  //获取LogType.ERROR类型的所有日志文件
  List<File> fileList3 = HLog.getLogFile(LogType.ERROR);

  //获取Date这一天内LogType.ERROR类型的所有日志文件
  List<File> fileList4 = HLog.getLogFile(LogType.ERROR,new Date(System.currentTimeMillis()));
```

### 清除日志
```java
  //清除所有日志文件
  boolean isSuccess1 = HLog.clearLogFile();

  //清除Date这一天的所有日志文件
  boolean isSuccess2 = HLog.clearLogFile(new Date(System.currentTimeMillis()));

  //清除LogType.ERROR类型的所有日志文件
  boolean isSuccess3 = HLog.clearLogFile(LogType.ERROR);

  //清除Date这一天内LogType.ERROR类型的所有日志文件
  boolean isSuccess4 = HLog.clearLogFile(LogType.ERROR,new Date(System.currentTimeMillis()));
```

### 异步回调
```java
  HLog.i(BitmapFactory.decodeResource(getResources(), R.mipmap.log), new LogCallBack() {
    @Override
    public void callBack(File file) {

      //当前回调在 主/UI 线程
      //file:写入日志的文件

    }
  });
  ... ...
  LogConfig config = LogConfig.Create().crashCallBack(new CrashCallBack() {
    @Override
    public void callBack(File file) {
    
      //当前回调在 主/UI 线程
      //file:应用崩溃异常写入的文件
      
    }
  });
```

### 问题及其他
**细节请下载项目查阅，项目内注释齐全**



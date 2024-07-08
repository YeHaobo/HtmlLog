# HtmlLog
Android日志记录工具

1、日志文件可根据时间和类型分批，也可组合时间和类型进行分批。  
2、日志文件可以是txt和html格式，html格式下可以穿插图片。  
3、日志的文件写入提供回调方法，回调时提供线程切换。  
3、包含按条件查找指定日志文件，进行查找和删除。  
4、可开启主动记录未捕获的异常，自动记录异常并回调。   

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
    implementation 'com.android.support:support-v4:28.0.0'//v4 AndroidX项目无需添加
    implementation 'com.github.YeHaobo:HtmlLog:3.0'//HtmlLog
    ... ...
  }
```

### 权限
（1）Android6.0+注意权限的动态获取
```java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>6.0+必须
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>6.0+必须
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>11.0+版本按需添加授权
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
                .debug(true)
                .crash(true)
                .fileType(FileType.HTML)
                .fileSplit(FileSplit.DAY)
                .fileMaxDay(30)
                .imgSize(new Size(360, 0));
        //初始化HtmlLog
        HLog.initialize(getApplicationContext(), config);
        ... ...
    }
```
_注意：initialize(Context,LogConfig);中的 **Context** 必须为 **applicationContext** ,否则可能引发内存泄漏。_

| 方法 | 作用 | 参数说明 |
| :-----------------------------: | :--------------------: | :-----: |
| debug(boolean) | 是否启用Debug模式，该模式下logcat打印日志内容 | true（默认） |
| crash(boolean) | 是否启用崩溃/异常主动记录 | true（默认） |
| crashCallback(HLogCrashCallback) | 崩溃/异常主动回调接口 | null（默认） |
| fileType(FileType) | 文件类型 | TXT</br>HTML（默认） |
| fileSplit(FileSplit) | 文件分批保存类型 | ONE：仅使用一个日志文件</br>MODE：依据类型分批日志文件</br>DAY：依据日期分批日志文件（默认）</br>DAY_MODE：先依据日期分批后，再依据类型分批日志文件</br>MODE_DAY：先依据类型分批后，再依据日期分批日志文件 |
| fileCharset(String) | 编码格式 | UTF-8（默认） |
| fileRootPath(String) | 存储路径 | /storage/sdcard0/HtmlLog/（默认） |
| fileMaxDay(int) | 日志保留天数 | 7（默认）必须大于0 |
| dateFormat(String) | 日期格式 | yyyy-MM-dd（默认） |
| timestampFormat(String) | 时间戳格式 | yyyy-MM-dd HH:mm:ss（默认） |
| fontColorError(int) | error类型文本颜色 | #F56C6C（默认） |
| fontColorWarning(int) | warning类型文本颜色 | #E6A23C（默认） |
| fontColorSuccess(int) | success类型文本颜色 | #67C23A（默认） |
| fontColorInfo(int) | info类型文本颜色 | #909399（默认） |
| fontSize(int) | 字体大小 | 13（默认） |
| fontWeight(int) | 字体粗细 | 600（默认） |
| fontMargin(int) | 文本边距 | 0（默认） |
| imgAttr(ImgAttr) | 图片展示类型 | NONE：保持图片宽高不变</br>CONTAIN：保持纵横比缩放图片，使图片的长边能完全显示出来</br>COVER：保持纵横比缩放图片，使图片的短边能完全显示出来，长边可能展示不完全</br>FILL：不保持纵横比缩放图片，使图片完全适应</br>SCALE_DOWN：当图片实际宽高小于所设置的图片宽高时，显示效果与none一致；否则显示效果与contain一致 |
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
  HLog.i(BitmapFactory.decodeResource(getResources(),R.mipmap.img));
```

### 获取日志
```java
  //获取所有日志文件
  List<File> fileList1 = HLog.find();

  //获取Date这一天的所有日志文件
  List<File> fileList2 = HLog.find(new Date(System.currentTimeMillis()));

  //获取LogType.ERROR类型的所有日志文件
  List<File> fileList3 = HLog.find(LogType.ERROR);

  //获取Date这一天内LogType.ERROR类型的所有日志文件
  List<File> fileList4 = HLog.find(LogType.ERROR,new Date(System.currentTimeMillis()));
```

### 清除日志
```java
  //清除所有日志文件
  boolean isSuccess1 = HLog.clear();

  //清除Date这一天的所有日志文件
  boolean isSuccess2 = HLog.clear(new Date(System.currentTimeMillis()));

  //清除LogType.ERROR类型的所有日志文件
  boolean isSuccess3 = HLog.clear(LogType.ERROR);

  //清除Date这一天内LogType.ERROR类型的所有日志文件
  boolean isSuccess4 = HLog.clear(LogType.ERROR,new Date(System.currentTimeMillis()));
```

### 日志写入回调
#### 默认回调主线程
```java
  HLog.i(BitmapFactory.decodeResource(getResources(), R.mipmap.img), new HLogCallback() {
    @Override
    public void onCallback(File logFile) {
      //当前回调在 主/UI 线程
      //file:日志文件
    }
  });
```

#### 使用HLogLooper切换线程
```java
  HLog.i(BitmapFactory.decodeResource(getResources(), R.mipmap.img), new HLogCallback(HLogLooper.POSTING) {
    @Override
    public void onCallback(File logFile) {
      //当前回调在调用线程
      //file:日志文件
    }
  });
```
**HLogLooper.MAIN**: 回调在主线程（默认）  
**HLogLooper.POSTING**: 在调用线程回调（注意：若调用线程的Looper.myLooper()为空则会使用新的子线程回调）  
**HLogLooper.HLOG**: 回调在HLog内部子线程  
  
### 异常/崩溃捕获回调
```java
    HLogCrashCallback crashCallback = new HLogCrashCallback() {
        @Override
        public boolean onCrashCallback(Thread t, Throwable e, File logFile) {
            //执行捕获异常/崩溃后的操作
            //回调在HLog子线程中
            return true;//返回true时表示无需系统处理，返回false时则表示需要系统处理（即停止运行或崩溃）
        }
    };
```

### 问题及其他
**（1）出现无法记录日志或应用崩溃时请检查是否授予了完整的权限，另外android11以上的版本请检查 “所有文件访问权限” 是否需要和申请**  
**（2）项目细节解释请下载项目查阅，项目内注释齐全**  



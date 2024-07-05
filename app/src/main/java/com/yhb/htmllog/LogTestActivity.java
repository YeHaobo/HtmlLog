package com.yhb.htmllog;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Toast;
import com.yhb.hlog.callback.HLogLooper;
import com.yhb.hlog.config.FileSplit;
import com.yhb.hlog.config.FileType;
import com.yhb.hlog.config.ImgAttr;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.HLog;
import com.yhb.hlog.callback.HLogCallback;
import com.yhb.hlog.crash.HLogCrashCallback;
import java.io.File;
import java.util.Date;
import java.util.List;

/**HLog测试*/
public class LogTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_test);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private HLogCrashCallback crashCallback = new HLogCrashCallback() {
        @Override
        public boolean onCrashCallback(Thread t, Throwable e, File logFile) {
            Log.e("33333",Thread.currentThread().getName());
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LogConfig config = LogConfig.Create()
                .debug(true)
                .crash(true)
                .crashCallback(crashCallback)
                .fileType(FileType.HTML)
                .fileSplit(FileSplit.MODE_DAY)
                .fileCharset("GBK")
                .fileRootPath(Environment.getExternalStorageDirectory().getPath() + "/HtmlLog")
                .fileMaxDay(3)
                .dateFormat("yyyy-MM-dd")
                .timestampFormat("yyyy-MM-dd HH:mm:ss")
                .fontColorError(R.color.error)
                .fontColorWarning(R.color.warning)
                .fontColorSuccess(R.color.success)
                .fontColorInfo(R.color.info)
                .fontSize(15)
                .fontWeight(600)
                .fontMargin(0)
                .imgAttr(ImgAttr.SCALE_DOWN)
                .imgSize(new Size(0,0))
                .imgMargin(0);

        HLog.initialize(getApplicationContext(), config);

//        HLog.clear();

        HLog.i("info","This is INFO msg");
        HLog.s("success","This is SUCCESS msg");
        HLog.w("waring","This is WARING msg");
        HLog.e("error","This is ERROR msg");

//        HLog.e(BitmapFactory.decodeResource(getResources(), R.mipmap.img));

        HandlerThread handlerThread = new HandlerThread("yhb123456");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("1111",Thread.currentThread().getName());
                HLog.s("success", "Test callback", new HLogCallback(HLogLooper.POSTING) {
                    @Override
                    public void onCallback(File logFile) {
                        Log.e("22222",Thread.currentThread().getName());
                        Toast.makeText(LogTestActivity.this,"成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        List<File> fileList = HLog.find(new Date(System.currentTimeMillis()));

    }

}
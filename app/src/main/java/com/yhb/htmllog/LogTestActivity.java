package com.yhb.htmllog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.expose.HLog;
import com.yhb.hlog.expose.LogCallBack;
import java.io.File;

/**HLog测试*/
public class LogTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_test);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HLog.s("success", "Test callback", new LogCallBack() {
                    @Override
                    public void callBack(File file) {
                        Log.e("12133",Thread.currentThread().getName());
                        Toast.makeText(LogTestActivity.this,"成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        HLog.initialize(getApplicationContext(), LogConfig.Create());
        HLog.i("info","This is INFO msg");
        HLog.s("success","This is SUCCESS msg");
        HLog.w("waring","This is WARING msg");
        HLog.e("error","This is ERROR msg");
        Log.e("12133",Thread.currentThread().getName());
        HLog.s("success", "Test callback", new LogCallBack() {
            @Override
            public void callBack(File file) {
                Log.e("12133",Thread.currentThread().getName());
                Toast.makeText(LogTestActivity.this,"成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
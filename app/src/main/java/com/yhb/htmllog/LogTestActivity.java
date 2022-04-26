package com.yhb.htmllog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.yhb.hlog.config.LogConfig;
import com.yhb.hlog.expose.HLog;

/**HLog测试*/
public class LogTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HLog.initialize(getApplicationContext(), LogConfig.Create());
        HLog.i("info","This is INFO msg");
        HLog.s("success","This is SUCCESS msg");
        HLog.w("waring","This is WARING msg");
        HLog.e("error","This is ERROR msg");
    }

}
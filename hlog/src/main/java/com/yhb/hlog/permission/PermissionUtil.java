package com.yhb.hlog.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.yhb.hlog.expose.HLog;

/**权限*/
public class PermissionUtil {

    /**需要的权限*/
    private static String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**判断是否缺少权限*/
    public static boolean hasPermission(Context context) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                Log.e(HLog.TAG,"No read/write permissions for files");
                return false;
            }
        }
        return true;
    }

}
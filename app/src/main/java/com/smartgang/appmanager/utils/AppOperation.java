package com.smartgang.appmanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by zhangxingang on 2017/8/25.
 */

public class AppOperation {
    public static void uninstallApk(Context context, String apk, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + apk));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        ((Activity)context).startActivityForResult(intent, requestCode);
    }

    public static Boolean checkPermissions(Context context) {
        boolean res = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ((Activity)context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                res = true;
            }
        }
        return res;
    }
}

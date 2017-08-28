package com.smartgang.appmanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhangxingang on 2017/8/25.
 */

public class UtilsApp {
    public static File getDefaultAppFolder() {
        return new File(Environment.getExternalStorageDirectory() + "/AppManager");
    }

    public static boolean copyFile(AppInfo appInfo) {
        boolean res = false;
        File srcFile = new File(appInfo.getSource());
        File desFile = getDesFilePath(appInfo);
        try {
                FileUtils.copyFile(srcFile, desFile);
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static File getDesFilePath(AppInfo appInfo) {
        return new File(getDefaultAppFolder() + "/" + appInfo.getAPK() + "_" + appInfo.getVersion() + ".apk");
    }
}

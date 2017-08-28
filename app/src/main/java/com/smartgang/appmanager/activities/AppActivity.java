package com.smartgang.appmanager.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartgang.appmanager.R;
import com.smartgang.appmanager.utils.AppInfo;
import com.smartgang.appmanager.utils.AppOperation;
import com.smartgang.appmanager.utils.UtilsApp;

/**
 * Created by zhangxingang on 2017/8/22.
 */


public class AppActivity extends AppCompatActivity {
    private final static int UNINSTALL_REQUEST_CODE = 1;
    private Context mContext;
    private Activity mActivity;
    private AppInfo mAppInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        this.mContext = this;
        this.mActivity = (Activity) mContext;
        getInitConfiguration();
        setScreenElements();

    }

    private void setScreenElements() {
        TextView header = (TextView) findViewById(R.id.app_item_header);
        ImageView icon = (ImageView) findViewById(R.id.app_item_icon);
        ImageView icon_googleplay = (ImageView) findViewById(R.id.app_item_googleplay);
        TextView name = (TextView) findViewById(R.id.app_item_name);
        TextView version = (TextView) findViewById(R.id.app_item_version);
        TextView apk = (TextView) findViewById(R.id.app_item_apk);
        CardView googleplay = (CardView) findViewById(R.id.app_item_card);
        CardView start = (CardView) findViewById(R.id.app_item_start);
        CardView extract = (CardView) findViewById(R.id.app_item_extra);
        CardView uninstall = (CardView) findViewById(R.id.app_item_uninstall);
//        CardView cache = (CardView) findViewById(R.id.cache_card);
//        CardView clearData = (CardView) findViewById(R.id.clear_data_card);
        icon.setImageDrawable(mAppInfo.getIcon());
        name.setText(mAppInfo.getName());
        version.setText(mAppInfo.getVersion());
        apk.setText(mAppInfo.getAPK());

        apk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(mAppInfo.getAPK());
                    startActivity(intent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //UtilsDialog.showSnackbar(activity, String.format(getResources().getString(R.string.dialog_cannot_open), appInfo.getName()), null, null, 2).show();
                }
            }
        });
        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppOperation.checkPermissions(mContext)) {
                    UtilsApp.copyFile(mAppInfo);
                }
            }
        });
        uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppOperation.uninstallApk(mContext, mAppInfo.getAPK(), UNINSTALL_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (UNINSTALL_REQUEST_CODE == requestCode) {
            if (RESULT_OK == resultCode) {
                Log.i("App Operation", "Uninstall successful");
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
            } else if (RESULT_CANCELED == resultCode) {
                Log.i("App Operation", "Uninstall cancel");
            }
        }
    }

    private void getInitConfiguration() {
        String appName = getIntent().getStringExtra("app_name");
        String appApk = getIntent().getStringExtra("app_apk");
        String appVersion = getIntent().getStringExtra("app_version");
        String appSource = getIntent().getStringExtra("app_source");
        String appData = getIntent().getStringExtra("app_data");
        Bitmap bitmap = getIntent().getParcelableExtra("app_icon");
        Drawable appIcon = new BitmapDrawable(getResources(), bitmap);
        Boolean appIsSystem = getIntent().getExtras().getBoolean("app_isSystem");
        mAppInfo = new AppInfo(appName, appApk, appVersion, appSource, appData, appIcon, appIsSystem);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
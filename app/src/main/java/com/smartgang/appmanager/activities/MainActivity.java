package com.smartgang.appmanager.activities;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.smartgang.appmanager.R;
import com.smartgang.appmanager.adapters.AppAdapter;
import com.smartgang.appmanager.adapters.AppAdapterGrid;
import com.smartgang.appmanager.listeners.RecyclerViewScrollListener;
import com.smartgang.appmanager.utils.AppInfo;
import com.smartgang.appmanager.utils.AppOperation;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class MainActivity extends AppCompatActivity {
    private PullToRefreshView mPullToRefreshView;
    private ListView mListView;
    private List<String> mListDataString;
    private RecyclerView mRecyclerView;
    private static VerticalRecyclerViewFastScroller fastScroller;
    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mSystemAppInfoList;
    private AppAdapter mAppAdapter;
    private AppAdapterGrid mAppAdapterGrid;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        AppOperation.checkPermissions(mContext);
        initData();
        initRefreshViewResource();
        initRecyclerView();
        new getAppInfoTask().execute();
    }

    private void initData() {
        mAppInfoList = new ArrayList<>();
        mSystemAppInfoList = new ArrayList<>();
    }

    private void initRefreshViewResource() {
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setAdapter(null);
                new getAppInfoTask().execute();
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //三秒后将下拉刷新的状态变为刷新完成
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    }


    class getAppInfoTask extends AsyncTask<Void, String, Void> {
        public getAppInfoTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAppInfoList.clear();
            mRecyclerView.setAdapter(null);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //fastScroller.setVisibility(View.VISIBLE);
            mAppAdapter = new AppAdapter(mContext, mAppInfoList);
            mAppAdapterGrid = new AppAdapterGrid(mContext, mAppInfoList);
            mRecyclerView.setAdapter(mAppAdapterGrid);
            //fastScroller.setRecyclerView(mRecyclerView);

            //fastScroller.setR
            //mRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
            //mRecyclerView.addOnScrollListener(fastScroller.getOnScrollListener());
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAppInfo();
            return null;
        }
    }

    private void getAppInfo() {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packageInfoList) {
            if (!packageManager.getApplicationLabel(packageInfo.applicationInfo).equals("") && !packageInfo.packageName.equals("")) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    try {
                        AppInfo info = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                packageInfo.packageName,
                                packageInfo.versionName,
                                packageInfo.applicationInfo.sourceDir,
                                packageInfo.applicationInfo.dataDir,
                                packageManager.getApplicationIcon(packageInfo.applicationInfo),
                                false);
                        mAppInfoList.add(info);
                    } catch (OutOfMemoryError e){
                        //TODO Workaround to avoid FC on some devices (OutOfMemoryError). Drawable should be cached before.
                        AppInfo info = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                packageInfo.packageName,
                                packageInfo.versionName,
                                packageInfo.applicationInfo.sourceDir,
                                packageInfo.applicationInfo.dataDir,
                                getResources().getDrawable(R.drawable.ic_android, null),
                                false);
                        mAppInfoList.add(info);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else {//is system
                    try {
                        AppInfo info = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                packageInfo.packageName,
                                packageInfo.versionName,
                                packageInfo.applicationInfo.sourceDir,
                                packageInfo.applicationInfo.dataDir,
                                packageManager.getApplicationIcon(packageInfo.applicationInfo),
                                true);
                        mSystemAppInfoList.add(info);
                    } catch (OutOfMemoryError e){
                        //TODO Workaround to avoid FC on some devices (OutOfMemoryError). Drawable should be cached before.
                        AppInfo info = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                packageInfo.packageName,
                                packageInfo.versionName,
                                packageInfo.applicationInfo.sourceDir,
                                packageInfo.applicationInfo.dataDir,
                                getResources().getDrawable(R.drawable.ic_android, null),
                                true);
                        mSystemAppInfoList.add(info);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }//system app
            }
        }
    }
}

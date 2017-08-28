package com.smartgang.appmanager.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartgang.appmanager.R;
import com.smartgang.appmanager.activities.AppActivity;
import com.smartgang.appmanager.utils.AppInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxingang on 2017/8/23.
 */

public class AppAdapterGrid extends RecyclerView.Adapter<AppAdapterGrid.AppViewHolder> implements Filterable {
    private Context mContext;
    private List<AppInfo> mAppInfoList;

    public AppAdapterGrid(Context context, List<AppInfo> list) {
        this.mContext = context;
        this.mAppInfoList = list;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppViewHolder holder = new AppViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.apk_item_grid, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppInfo info = mAppInfoList.get(position);
        if (!info.isSystem()) {
            holder.imIcon.setImageDrawable(info.getIcon());
            holder.tvName.setText(info.getName());
        }
        setButtonEvent(holder, info);
    }

    private void setButtonEvent(AppViewHolder holder, final AppInfo appInfo) {
        final CardView card = holder.cvCard;
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AppActivity.class);
                intent.putExtra("app_name", appInfo.getName());
                intent.putExtra("app_apk", appInfo.getAPK());
                intent.putExtra("app_version", appInfo.getVersion());
                intent.putExtra("app_source", appInfo.getSource());
                intent.putExtra("app_data", appInfo.getData());
                Bitmap bitmap = ((BitmapDrawable) appInfo.getIcon()).getBitmap();

                int size = bitmap.getByteCount();
                while (size > 450000) {
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.6f, 0.6f);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    size = bitmap.getByteCount();
                }

                intent.putExtra("app_icon", bitmap);
                intent.putExtra("app_isSystem", appInfo.isSystem());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    String transitionName = mContext.getResources().getString(R.string.transition_app_icon);
//
//                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, card, transitionName);
//                    mContext.startActivity(intent, transitionActivityOptions.toBundle());
//                } else {
//                    mContext.startActivity(intent);
//                    //activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
//                }
                mContext.startActivity(intent);
            }
        });
        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imIcon;
        TextView tvName;
        CardView cvCard;

        public AppViewHolder(View v) {
            super(v);
            imIcon = (ImageView) v.findViewById(R.id.apk_item_grid_icon);
            tvName = (TextView) v.findViewById(R.id.apk_item_grid_name);
            cvCard = (CardView) v.findViewById(R.id.apk_item_grid_card);
        }
    }
}

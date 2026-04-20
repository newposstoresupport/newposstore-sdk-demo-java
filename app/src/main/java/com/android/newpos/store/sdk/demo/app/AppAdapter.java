package com.android.newpos.store.sdk.demo.app;

import android.text.TextUtils;
import android.widget.TextView;

import com.android.desert.baserecyle.BaseQuickAdapter;
import com.android.desert.baserecyle.BaseViewHolder;
import com.android.newpos.store.sdk.demo.R;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.StoreApp;

import java.util.List;

/**
 * @ClassName : AppAdapter
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/8/5-14:57
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppAdapter extends BaseQuickAdapter<AppDownloadStatus, BaseViewHolder> {

    private AppItemListener appItemListener;

    public AppAdapter(int layoutResId, List<AppDownloadStatus> data) {
        super(layoutResId, data);
    }

    public void setListener(AppItemListener listener){
        this.appItemListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AppDownloadStatus appDownloadStatus) {
        StoreApp storeApp = appDownloadStatus.storeApp;
        baseViewHolder.setText(R.id.text, storeApp.progName);
        baseViewHolder.setText(R.id.version, storeApp.verName);
        baseViewHolder.setText(R.id.size, BaseUtils.formatSize(storeApp.appSize));

        TextView textView = baseViewHolder.getView(R.id.percent);
        if(appDownloadStatus.downloadStatus == DownloadStatus.START){
            textView.setText("pending");
        }else if(appDownloadStatus.downloadStatus == DownloadStatus.DOWNLOADING){
            textView.setText(appDownloadStatus.percent);
        }else if(appDownloadStatus.downloadStatus == DownloadStatus.DOWNLOADED){
            textView.setText("downloaded");
        }else if(appDownloadStatus.downloadStatus == DownloadStatus.INSTALLING){
            textView.setText("installing");
        }else if(appDownloadStatus.downloadStatus == DownloadStatus.INSTALLED){
            textView.setText("installed");
        }else if(appDownloadStatus.downloadStatus == DownloadStatus.PAUSED){
            textView.setText("paused");
        }else if(appDownloadStatus.downloadStatus == DownloadStatus.ERROR){
            textView.setText("error");
        }

        baseViewHolder.getView(R.id.btn).setOnClickListener(v -> {
            if(appItemListener != null){
                appItemListener.onClick(storeApp);
            }
        });
    }
}

package com.android.newpos.store.sdk.demo.app;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.dto.StoreApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName : AppViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/7/31-17:23
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AppViewModel extends BaseViewModel {

    private String FILE_SAVEPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getTitle() {
        return "App Management";
    }

    private MutableLiveData<List<AppDownloadStatus>> appList = new MutableLiveData<>();

    public MutableLiveData<List<AppDownloadStatus>> getAppList(){
        return appList;
    }

    /**
     * Get the application by the specified package name
     */
    /**
     * Get the application by the specified package name
     */
    public void getAppsByPackageName(){
        showLoading(new LoadingOption("Loading apps..."));
        addSubscribe(Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(b -> {
                    List<String> packList = new ArrayList<>();
                    //note you need to modify the packagename with your own app
                    packList.add("com.desert.android.appping");
                    //packList.add("com.core.softSignTest111");
                    List<StoreApp> storeApps = StoreSdk.getInstance().appAbility().getStoreApps(packList);
                    if(storeApps == null || storeApps.isEmpty()){
                        throw new BaseException("No application found");
                    }

                    List<AppDownloadStatus> appDownloadStatuses = new ArrayList<>();
                    for (StoreApp storeApp : storeApps){
                        AppDownloadStatus downloadStatus = new AppDownloadStatus();
                        downloadStatus.storeApp = storeApp;
                        downloadStatus.pack = storeApp.packageName;
                        downloadStatus.downloadStatus = DownloadStatus.START;
                        downloadStatus.percent = "0%";
                        appDownloadStatuses.add(downloadStatus);
                    }
                    appList.postValue(appDownloadStatuses);
                    dismissLoading();
                }, e -> {
                    dismissLoading();
                    showError(e);
                })
        );
    }

    /**
     * Check for new app versions
     */
    public void checkForUpdates(String packageName){
        showLoading(new LoadingOption("Checking for updates..."));
        addSubscribe(Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    StoreApp newApp = StoreSdk.getInstance().appAbility().checkForUpdate(packageName);
                    if(newApp == null){
                        throw new BaseException("check failed!");
                    }
                    Toast.makeText(getApplication(),
                            getApplication().getString(R.string.start_download, newApp.toString()),
                            Toast.LENGTH_SHORT).show();
                    dismissLoading();
                }, e -> {
                    dismissLoading();
                    showError(e);
                })
        );
    }

    /**
     * Download version
     * @param storeApp StoreApp
     */
    public void downloadForUpdates(StoreApp storeApp){
        //note this loading dialog is redundant
        //showLoading(new LoadingOption("Downloading app..."));
        String storagePath = FileDownloadUtils.generateFilePath(FILE_SAVEPATH,
                FileDownloadUtils.md5(FileDownloadUtils.formatString("%s:%s",
                        storeApp.packageName, storeApp.verCode)));
        System.out.println(storagePath);
        System.out.println(storeApp.appFile);
        addSubscribe(Observable.fromCallable(FileDownloader.getImpl()
                        .create(storeApp.appFile)
                        .setPath(storagePath, false)
                        .setAutoRetryTimes(3)
                        .setCallbackProgressTimes(500)
                        .setMinIntervalUpdateSpeed(500)
                        .setCallbackProgressMinInterval(500)
                        .setListener(new AppStatusManager(storeApp.packageName))::start)
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Toast.makeText(getApplication(),
                            getApplication().getString(R.string.start_download, storeApp.progName),
                            Toast.LENGTH_SHORT).show();
                    dismissLoading();
                }, e -> {
                    dismissLoading();
                    showError(e);
                })
        );
    }


    private static final String TAG = "AppDownloader";
    private final class AppStatusManager extends FileDownloadListener {
       private String pack;
       public AppStatusManager(String packageName){
            this.pack = packageName;
       }

       @Override
       protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
           Log.w(TAG, "pending:"+soFarBytes+","+totalBytes);
           List<AppDownloadStatus> appDownloads = appList.getValue();
           assert appDownloads != null;
           for (AppDownloadStatus status : appDownloads){
                if(Objects.equals(status.pack, pack)){
                    status.downloadStatus = DownloadStatus.START;
                    status.percent = "pending";
                }
           }
       }

       @Override
       protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
           Log.w(TAG, "progress:"+soFarBytes+","+totalBytes);
           List<AppDownloadStatus> appDownloads = appList.getValue();
           assert appDownloads != null;
           for (AppDownloadStatus status : appDownloads){
               if(Objects.equals(status.pack, pack)){
                   status.downloadStatus = DownloadStatus.DOWNLOADING;
                   float percent = soFarBytes / (float) totalBytes;
                   status.percent = String.valueOf(percent * 100).concat("%");
               }
           }
           appList.postValue(appDownloads);
       }

       @Override
       protected void completed(BaseDownloadTask task) {
           Log.w(TAG, "completed:"+task);
           List<AppDownloadStatus> appDownloads = appList.getValue();
           assert appDownloads != null;
           for (AppDownloadStatus status : appDownloads){
               if(Objects.equals(status.pack, pack)){
                   status.downloadStatus = DownloadStatus.DOWNLOADED;
                   status.percent = "100%";
                   status.localFilePath = task.getTargetFilePath();
               }
           }
           appList.postValue(appDownloads);
       }

       @Override
       protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
           Log.w(TAG, "paused:"+soFarBytes+","+totalBytes);
           List<AppDownloadStatus> appDownloads = appList.getValue();
           assert appDownloads != null;
           for (AppDownloadStatus status : appDownloads){
               if(Objects.equals(status.pack, pack)){
                   status.downloadStatus = DownloadStatus.PAUSED;
                   float percent = soFarBytes / (float) totalBytes;
                   status.percent = String.valueOf(percent * 100).concat("%");
               }
           }
           appList.postValue(appDownloads);
       }

       @Override
       protected void error(BaseDownloadTask task, Throwable e) {
           e.printStackTrace();
           Log.e(TAG, "error:"+task+","+e);
           List<AppDownloadStatus> appDownloads = appList.getValue();
           assert appDownloads != null;
           for (AppDownloadStatus status : appDownloads){
               if(Objects.equals(status.pack, pack)){
                   status.downloadStatus = DownloadStatus.ERROR;
               }
           }
           appList.postValue(appDownloads);
       }

       @Override
       protected void warn(BaseDownloadTask task) {
           Log.w(TAG, "error:"+task);
       }
   }

   public void installApk(AppDownloadStatus app){
       AppUtils.showToast(R.string.not_support);
        //TODO:  due to some resons ,this part of source code need to be comment
        /*if(app == null || app.downloadStatus != DownloadStatus.DOWNLOADED){
            AppUtils.showToast(R.string.downloadtask_not_finish);
            return;
        }
        File apk = new File(app.localFilePath);
        if(TextUtils.isEmpty(app.localFilePath) || !apk.exists()){
            AppUtils.showToast(R.string.file_not_exist);
            return;
        }
        int result = SystemManager.installApp(app.localFilePath);
        if(result == 0){
            AppUtils.showToast(R.string.install_success);
        }else{
            AppUtils.showToast("install fail,code:" + result);
        }*/
   }
}

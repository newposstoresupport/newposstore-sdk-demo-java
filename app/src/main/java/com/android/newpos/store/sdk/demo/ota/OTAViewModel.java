package com.android.newpos.store.sdk.demo.ota;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.newpos.store.sdk.demo.R;
import com.android.newpos.store.sdk.demo.app.DownloadStatus;
import com.android.newpos.store.sdk.demo.app.LoadingOption;
import com.android.newpos.store.sdk.demo.base.BaseViewModel;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.dto.Firmware;
import com.newpos.store.android.sdk.dto.FirmwareInfo;
import com.newpos.store.android.sdk.dto.QueryFirmwareRequest;
import com.android.newpos.store.sdk.demo.base.RomDeviceReflect;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName : OTAViewModel
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/28-10:39
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class OTAViewModel extends BaseViewModel {

    private FirmwareInfo firmwareInfo;
    private final MutableLiveData<String> mInfo;
    private final MutableLiveData<FirmwareDownloadStatus> statusMutableLiveData;

    public OTAViewModel(@NonNull Application application) {
        super(application);
        mInfo = new MutableLiveData<>();
        statusMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<FirmwareDownloadStatus> getStatusMutableLiveData(){
        return statusMutableLiveData;
    }

    public MutableLiveData<String> getInfo() {
        return mInfo;
    }

    @Override
    public String getTitle() {
        return "OTA Upgrade";
    }

    public void queryFirmware(){
        showLoading(new LoadingOption("Querying firmware information..."));

        addSubscribe(Observable.just(true)
                .observeOn(Schedulers.io())
                .subscribe(n -> {
                    QueryFirmwareRequest firmwareRequest = new QueryFirmwareRequest();
                    Firmware firmware = new Firmware();
                    firmware.custom = RomDeviceReflect.getCustomerName();
                    firmware.firmwareId = RomDeviceReflect.getFirmwareId();
                    firmware.version = RomDeviceReflect.getFirmwareVersion();
                    firmwareRequest.firmware = firmware;
                    firmwareInfo = StoreSdk.getInstance().otaAbility().queryFirmware(firmwareRequest);
                    if(firmwareInfo == null){
                        mInfo.postValue("There is no new firmware. Please go to the cloud platform to upload it.");
                    }else {
                        mInfo.postValue(firmwareInfo.toString());
                    }

                    //TODO simulate
//                    firmwareInfo = new FirmwareInfo();
//                    firmwareInfo.url = "https://cdn.ns.newposp.com/newstore/tms/firmware/14413cfd17dfaa9e4087ef75cb71de8d_1778825018969.img?r=2055167680958799874&s=1784020366-197-7853956d33d48e6494c96f36ad3f238a092929e4&u=1&t=1&i=2044675183376818176";
//                    firmwareInfo.debug = "0";
//                    firmwareInfo.fullSize = 691580973L;
//                    firmwareInfo.hash = "647D5AD98583682195A48031C7B8081E6009B2F594A020C9AB54A147FCC8EBEC";
//                    firmwareInfo.releaseDate = "2026-05-15";
//                    firmwareInfo.releaseNote = "临时版本，仅供升级下载测试。";
//                    firmwareInfo.version = "13.5.11";
//                    mInfo.postValue(firmwareInfo.toString());

                    dismissLoading();
                }, throwable -> {
                    dismissLoading();
                    showError(throwable);
                })
        );
    }

    public void downloadFirmware(){

        if(firmwareInfo == null || TextUtils.isEmpty(firmwareInfo.url) ||
            TextUtils.isEmpty(firmwareInfo.hash) || TextUtils.isEmpty(firmwareInfo.version)){
            mInfo.postValue("There is no firmware, please query first!");
            return;
        }

        showLoading(new LoadingOption("Downloading firmware file..."));
        String storagePath = FileDownloadUtils.generateFilePath(FileDownloadUtils.getDefaultSaveRootPath(),
                FileDownloadUtils.md5(FileDownloadUtils.formatString("%s:%s",
                        firmwareInfo.hash, firmwareInfo.version)));
        System.out.println(storagePath);
        System.out.println(firmwareInfo.url);
        //TODO 用户可以选用自己的下载器实现
        //TODO Users can use their own downloader to make it work.
        addSubscribe(Observable.fromCallable(FileDownloader.getImpl()
                        .create(firmwareInfo.url)
                        .setPath(storagePath, false)
                        .setAutoRetryTimes(3)
                        .setCallbackProgressTimes(500)
                        .setMinIntervalUpdateSpeed(500)
                        .setCallbackProgressMinInterval(500)
                        .setListener(new FirmwareStatusManager())::start)
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Toast.makeText(getApplication(),
                            getApplication().getString(R.string.start_download_firm, firmwareInfo.version),
                            Toast.LENGTH_SHORT).show();
                    dismissLoading();
                }, e -> {
                    dismissLoading();
                    showError(e);
                })
        );
    }

    private static final String TAG = "FirmwareDownloader";
    private final class FirmwareStatusManager extends FileDownloadListener {
        public FirmwareStatusManager(){}

        private void postValue(DownloadStatus downloadStatus, String percent, String speed, int sofar, int total){
            FirmwareDownloadStatus status = new FirmwareDownloadStatus();
            status.firmwareInfo = firmwareInfo;
            status.percent = percent;
            status.speed = speed;
            status.sofar = sofar;
            status.total = total;
            status.downloadStatus = downloadStatus;
            statusMutableLiveData.postValue(status);
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.w(TAG, "pending:"+soFarBytes+","+totalBytes);
            postValue(DownloadStatus.START, "pending", "0 KB/s", soFarBytes, totalBytes);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.w(TAG, "progress:"+soFarBytes+","+totalBytes);
            float percent = soFarBytes / (float) totalBytes;
            postValue(DownloadStatus.DOWNLOADING,
                    String.valueOf(percent * 100).concat("%"),
                    task.getSpeed()+" KB/s", soFarBytes, totalBytes);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            Log.w(TAG, "completed:"+task);
            postValue(DownloadStatus.DOWNLOADED, "100%", task.getSpeed()+" KB/s", -1, -1);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.w(TAG, "paused:"+soFarBytes+","+totalBytes);
            float percent = soFarBytes / (float) totalBytes;
            postValue(DownloadStatus.PAUSED, String.valueOf(percent * 100).concat("%"),
                    task.getSpeed()+" KB/s", soFarBytes, totalBytes);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            e.printStackTrace();
            Log.e(TAG, "error:"+task+","+e);
            //TODO 用户需要自行处理超时策略
            //TODO The user needs to handle the timeout policy on their own
            postValue(DownloadStatus.ERROR, "", e.getMessage(), -1, -1);
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            Log.w(TAG, "error:"+task);
        }
    }
}

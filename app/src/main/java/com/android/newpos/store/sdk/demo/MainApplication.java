package com.android.newpos.store.sdk.demo;

import static com.liulishuo.filedownloader.util.FileDownloadUtils.formatString;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.android.newpos.store.sdk.demo.inquirer.AppInquirerViewModel;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DefaultIdGenerator;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.newpos.store.android.sdk.IAppInquirer;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.SPreference;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.AuthenticationRequest;
import com.newpos.store.android.sdk.listener.IStoreCallback;
import com.tencent.mmkv.MMKV;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName : MainApplication
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/28-16:40
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class MainApplication extends Application {
    //TODO step 1
    // make sure to replace with your own appid & appkey & appsecret
    private static final String AppId = "32292cc7e05a2b86ddea1d6746210283";
    private static final String AppKey = "c3ff54daf66bbbd2e8d8dbf08172a5aa";
    private static final String AppSecret = "a8e2188e9474692ea6a538f0a4f955ab";

    private static MainApplication mainApplication;

    public static MainApplication getInstance(){
        return mainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MainApplication>>onCreate");
        mainApplication = this;
        MMKV.initialize(this);
        initDownloader();
        initStoreSdk(AppUtils.getClientId());
        SPreference.I().init(getApplicationContext());
    }

    public void initStoreSdk(String clientId){
        //TODO step 2
        // download newpos store sdk from github
        // and init StoreSdk by appid & appkey & appsecret
        AppElements elements = new AppElements();
        elements.setAppId(AppId);
        elements.setAppKey(AppKey);
        elements.setAppSecret(AppSecret);

        AuthenticationRequest request = null;
        if(!TextUtils.isEmpty(clientId)){
            request = new AuthenticationRequest();
            request.setRemark(null);
            request.setClientId(clientId);
        }

        //TODO Recommendation: Initialize the interface only once in the application
        StoreSdk.getInstance().init(getApplicationContext(), elements, request, new IStoreCallback() {
            @Override
            public void initSuccess() {
                Toast.makeText(getApplicationContext(), R.string.init_success, Toast.LENGTH_SHORT).show();
                initAppInquirer();
            }

            @Override
            public void initFailed(RemoteException remoteException) {
                String message = remoteException.getMessage() + "\n";
                message += getString(R.string.newstore_lost);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                //showDialog(message);
            }
        });
    }

    private void initAppInquirer(){
        //TODO step 3
        // init app inquirer to Listen to your application update status and management policies
        StoreSdk.getInstance().initAppInquirer(getApplicationContext(), new IAppInquirer.Stub() {
            @Override
            public boolean onReadyToUpdate() throws RemoteException {
                boolean isReady = SPreference.I().getBoolean(AppInquirerViewModel.key);
                System.out.println("app is ready to update:"+isReady);
                return isReady;
            }
        });
    }

    private void showDialog(String message){
        Disposable d = Observable.just(true)
                .observeOn(Schedulers.io())
                .doOnNext(b -> {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        while (!Settings.canDrawOverlays(getApplicationContext())){
                            Disposable dd = Observable.just(true).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(s -> Toast.makeText(getApplicationContext() ,
                                            R.string.allow_overlay ,
                                            Toast.LENGTH_SHORT).show());
                            String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
                            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    AlertDialog dialog = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogTheme)
                            .setTitle(R.string.result_title)
                            .setMessage(message)
                            .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss()).create();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    }else {
                        Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                    dialog.show();
                }, Throwable::printStackTrace);

    }

    private void initDownloader(){
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                )).idGenerator(new DefaultIdGenerator(){
                    @Override
                    public int transOldId(int oldId, String url, String path, boolean pathAsDirectory) {
                        return generateId(url, path, pathAsDirectory);
                    }

                    @Override
                    public int generateId(String url, String path, boolean pathAsDirectory) {
                        return FileDownloadUtils.md5(formatString("path:%s", path)).hashCode();
                    }
                });
        FileDownloadUtils.setDefaultSaveRootPath(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
    }
}

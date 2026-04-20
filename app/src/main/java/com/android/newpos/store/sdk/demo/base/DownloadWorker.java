package com.android.newpos.store.sdk.demo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.newpos.store.sdk.demo.MainApplication;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.ability.ParamAbilityV2;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.dto.ParamDownQueryV2Data;
import com.newpos.store.android.sdk.dto.ParamDownV2Result;
import com.newpos.store.android.sdk.dto.ParamTask;
import com.newpos.store.android.sdk.dto.ParamVerify;
import com.newpos.store.android.sdk.dto.ParamVerifyResult;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName : DownloadWorker 后台下载任务demo
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2026/1/27-16:53
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class DownloadWorker extends Worker {

    private static final String TAG = DownloadWorker.class.getSimpleName();
    public static final String KEY_MESSAGE_ID = "msgId";
    public static final String KEY_PATH_DIR = "pathDir";
    public static final String KEY_RESULT = "workerResult";
    public static final String ACTION_DOWNLOAD_FINISH = "android.action.DOWNLOAD_PARAM_FINISH";

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        BaseLog.w("DownloadWorker doWork..."+Thread.currentThread().getName());
        Context context = getApplicationContext();

        CountDownLatch latch = new CountDownLatch(1);
        MainApplication.getInstance().initStoreSdk(AppUtils.getClientId(), latch::countDown);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }
        BaseLog.d("init store sdk finish.");

        String msgId = getInputData().getString(KEY_MESSAGE_ID);
        BaseLog.d("DownloadWorker msgId："+msgId);
        try {
            ParamAbilityV2 paramAbilityV2 = StoreSdk.getInstance().paramAbilityV2();
            List<ParamTask> paramTasks = paramAbilityV2.queryParamTask();
            Log.d(TAG, "paramTasks:"+paramTasks);
            if(paramTasks == null || paramTasks.isEmpty()){
                throw new BaseException("no param tasks!");
            }
            //TODO 注意要处理好循环,防止后台增加循环
            for (ParamTask paramTask: paramTasks){
                List<ParamDownQueryV2Data> v2Data = paramAbilityV2.queryParamDown(paramTask);
                if(v2Data == null || v2Data.isEmpty()){
                    callback(paramTask, false);
                    Log.e(TAG, "download param attached files failed!"+paramTask);
                    continue;
                }
                List<ParamDownV2Result> v2ResultList = DownloadFileManager
                        .downloadParamToPathV2(getApplicationContext(), v2Data);
                if(v2ResultList.isEmpty()){
                    callback(paramTask, false);
                    Log.e(TAG, "download param attached files failed!"+paramTask);
                    continue;
                }

                callback(paramTask, true);

                Log.e(TAG, "download param success, app can init.....");
                AppUtils.showToast("download param success, app can init.....");
                Intent intent = new Intent();
                intent.setAction(ACTION_DOWNLOAD_FINISH);
                intent.putExtra(KEY_RESULT, true);
                intent.putExtra(KEY_PATH_DIR, v2ResultList.get(0).dirs.get(0));
                context.sendBroadcast(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
            String error = "download param failed, please check it !";
            if(e instanceof BaseException){
                BaseException baseException = (BaseException) e;
                error = baseException.msg;
            }
            AppUtils.showToast(error);
            Intent intent = new Intent();
            intent.setAction(ACTION_DOWNLOAD_FINISH);
            intent.putExtra(KEY_RESULT, false);
            context.sendBroadcast(intent);
            return Result.failure();
        }
        return Result.success();
    }

    private void callback(ParamTask task, boolean success){
        ParamVerify paramVerify = new ParamVerify();
        paramVerify.messageId = task.messageId;
        paramVerify.pushTaskId = task.pushTaskId;
        paramVerify.result = success ? "success" : "failed";
        paramVerify.state = success ? ParamVerifyResult.SUCCESS : ParamVerifyResult.FAIL;
        ParamAbilityV2 paramAbilityV2 = StoreSdk.getInstance().paramAbilityV2();
        boolean verifyResult = false;
        try {
            verifyResult = paramAbilityV2.paramCallResult(paramVerify);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        Log.w(TAG, "verifyResult:"+verifyResult);
    }
}

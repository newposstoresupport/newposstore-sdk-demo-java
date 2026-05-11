package com.android.newpos.store.sdk.demo;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.android.newpos.store.sdk.demo.base.AppUtils.showToast;
import static com.android.newpos.store.sdk.demo.base.DownloadWorker.KEY_MESSAGE_ID;
import static com.newpos.store.android.sdk.Constant.CLOUD_MESSAGE_TYPE_DOWN_PARAM;
import static com.newpos.store.android.sdk.Constant.CLOUD_MESSAGE_TYPE_NOTIFICATION;
import static com.newpos.store.android.sdk.Constant.CLOUD_MESSAGE_TYPE_RKI_DOWN_CUSTOMER_KEYS;
import static com.newpos.store.android.sdk.Constant.CM_DATA;
import static com.newpos.store.android.sdk.Constant.CM_MSGID;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.newpos.store.sdk.demo.base.AppUtils;
import com.newpos.store.android.sdk.Constant;
import com.newpos.store.android.sdk.StoreSdk;
import com.newpos.store.android.sdk.base.BaseLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName : CloudMessageReceiver
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/7/19-15:25
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 *
 * am broadcast -a com.newstore.action.CLOUD_MESSAGE_ARRIVED -n com.android.newpos.store.sdk.demo/.CloudMessageReceiver
 */
public class CloudMessageReceiver extends BroadcastReceiver {
    private static final String TAG = CloudMessageReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:"+intent.getAction());

        if(Constant.ACTION_CLOUD_MESSAGE_ARRIVED.equals(intent.getAction())){
            handleMessage(context, intent);
        }
    }

    public static final String CM_CHANNEL_ID = "cloud message demo id";
    public static final String CM_CHANNEL_NAME = "Cloud Message Demo Name";

    private void handleMessage(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return;
        }
        String data = bundle.getString(CM_DATA);
        BaseLog.d("handleMessage>data:"+data);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            String cmd = jsonObject.getString("cmd");
            if(TextUtils.isEmpty(cmd)){
                return;
            }
            if(CLOUD_MESSAGE_TYPE_NOTIFICATION.equals(cmd)){
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("detail");
                boolean sound = bundle.getBoolean(Constant.CM_SOUND);
                boolean bubble = bundle.getBoolean(Constant.CM_BADGE);
                sendNotificationMessage(context, title, content, sound, bubble);
            }
            if(CLOUD_MESSAGE_TYPE_DOWN_PARAM.equals(cmd)){
                String msgId = bundle.getString(CM_MSGID);
                //TODO You can choose to download in the foreground or in the background.

                //TODO 直接后台下载任务demo
                //Download the task demo directly in the background
                AppUtils.startDownloadWorker(context, msgId);

                //TODO 启动前台下载任务demo
                //Launch the foreground download task demo
                //sendNotificationParamDownload(context, msgId);
            }
            if(CLOUD_MESSAGE_TYPE_RKI_DOWN_CUSTOMER_KEYS.equals(cmd)){
                String messageId = bundle.getString(CM_MSGID);
                JSONObject config = jsonObject.getJSONObject("config");
                String kdhUrl = config.getString("kdhUrl");
                if(TextUtils.isEmpty(kdhUrl)){
                    return;
                }
                showToast(StoreSdk.getInstance().rkiAbility()
                        .downloadCustomerKeys(AppUtils.getClientId(), kdhUrl, messageId, (code, message, keyList)
                                -> Log.e("IRkiCallback", "onDownload:"+code+","+message+","+keyList)
                        ).getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationMessage(Context context, String title, String content, boolean sound, boolean bubble){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context, CM_CHANNEL_ID, CM_CHANNEL_NAME, sound, bubble);
        }
        NotificationCompat.Builder builder = createBuilder(context, title, content, CM_CHANNEL_ID, false);
        builder.setSmallIcon(R.mipmap.demo);

        int large = R.mipmap.demo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //TODO You need to be compatible with the notification bar icon
            //large = R.mipmap.ic_launcher;
        }
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), large));
        int notifyId = (int) System.currentTimeMillis();
        getNotificationManager(context).notify(notifyId, builder.build());
    }

    private void sendNotificationParamDownload(Context context, String msg){
        String channel_id = "Cloud Download Parameters";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context, channel_id, "download param", true, true);
        }

        NotificationCompat.Builder builder = createBuilder(context,
                "parameter download notification",
                "Click the notification to download automatically with one click",
                channel_id, false);
        builder.setSmallIcon(R.mipmap.demo);

        int large = R.mipmap.demo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //TODO You need to be compatible with the notification bar icon
            //large = R.mipmap.ic_launcher;
        }
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), large));

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_MESSAGE_ID, msg);
        PendingIntent pi = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.setContentIntent(pi);

        int notifyId = (int) System.currentTimeMillis();
        getNotificationManager(context).notify(notifyId, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static NotificationChannel createChannel(Context context, String id, String name, boolean sound, boolean bubble){
        NotificationManager notificationManager = getNotificationManager(context);
        if (notificationManager == null) {
            return null;
        }
        NotificationChannel channel = new NotificationChannel(id, name,
                sound ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_MIN);
        channel.setLightColor(Color.GREEN);
        channel.enableLights(true);
        channel.setShowBadge(bubble);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channel;
    }

    public static NotificationCompat.Builder createBuilder(Context context, String title, String content, String channel, boolean onGoing){
        return new NotificationCompat
                .Builder(context, channel)
                .setSmallIcon(R.mipmap.demo)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setNumber(99)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOngoing(onGoing);
    }

    @Nullable
    public static NotificationManager getNotificationManager(Context context) {
        return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
    }
}

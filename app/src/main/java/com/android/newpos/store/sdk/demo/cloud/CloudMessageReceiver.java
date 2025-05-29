package com.android.newpos.store.sdk.demo.cloud;

import static com.newpos.store.android.sdk.Constant.CLOUD_MESSAGE_TYPE_NOTIFICATION;
import static com.newpos.store.android.sdk.Constant.CLOUD_MESSAGE_TYPE_RKI_DOWN_CUSTOMER_KEYS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.newpos.store.sdk.demo.R;
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
        String data = bundle.getString(Constant.CM_DATA);
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
                sendNotification(context, title, content, sound, bubble);
            }
            if(CLOUD_MESSAGE_TYPE_RKI_DOWN_CUSTOMER_KEYS.equals(cmd)){
                JSONObject config = jsonObject.getJSONObject("config");
                String kdhUrl = config.getString("kdhUrl");
                if(TextUtils.isEmpty(kdhUrl)){
                    return;
                }
                StoreSdk.getInstance().rkiAbility()
                        .downloadCustomerKeys(AppUtils.getClientId(), kdhUrl, (code, message, keyList)
                                -> Toast.makeText(context, "onDownload:"+code+","+message+","+keyList, Toast.LENGTH_SHORT).show());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

    }

    public void sendNotification(Context context, String title, String content, boolean sound, boolean bubble){
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static NotificationChannel createChannel(Context context, String id, String name, boolean sound, boolean bubble){
        NotificationManager notificationManager = getNotificationManager(context);
        if (notificationManager == null) return null;
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

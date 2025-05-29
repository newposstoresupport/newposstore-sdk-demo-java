package com.newpos.store.android.sdk;

/**
 * @ClassName : Constant
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/3/21-8:48
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public interface Constant {
    String NEWSTORE_APP_DETAIL = "com.newpos.store.android.app.AppDetailActivity";
    String NEWSTORE_DOWNLOAD_LIST = "com.newpos.store.android.app.DownloadListActivity";
    String NEWSTORE_OTA_UPDATE = "com.newpos.store.android.app.OtaUpdateActivity";

    //Client authentication related error messages
    String ERROR_MARKET_NOT_INSTALLED = "Bind store failed, STORE client may not installed!";
    String ERROR_MARKET_TERMINAL_INFO = "Get terminal info from STORE client error,please check!";
    String ERROR_MARKET_AUTHENTICATION = "AUTHENTICATION from STORE client error,please check!";

    //core service related constant values
    String ACTION_BIND_STORE_SERVICE = "com.newpos.store.android.app.ACTION_STORE";
    String PERMISSION_BIND_STORE_SERVICE = "store.permission.BIND_STORE_SERVICE";

    //update service related constant values
    String ACTION_BIND_UPDATE_SERVICE = "com.newpos.update.android.app.ACTION_UPDATE";
    String PERMISSION_BIND_UPDATE_SERVICE = "update.permission.BIND_UPDATE_SERVICE";

    //Cloud message related constant values
    String ACTION_CLOUD_MESSAGE_ARRIVED = "com.newstore.action.CLOUD_MESSAGE_ARRIVED";
    String ACTION_CLOUD_MESSAGE_CLICKED = "com.newstore.action.CLOUD_MESSAGE_CLICKED";
    String PERMISSION_RECEIVE_CLOUD_MESSAGE = "store.permission.RECEIVE_CLOUD_MESSAGE";
    String CM_DATA = "cm_data";
    String CM_SOUND = "cm_sound";
    String CM_BADGE = "cm_badge";

    String API_SUCCESS = "0000";

    //cloud message type
    /**
     * notification message
     */
    String CLOUD_MESSAGE_TYPE_NOTIFICATION = "A001";
    /**
     * RKI download customer keys
     */
    String CLOUD_MESSAGE_TYPE_RKI_DOWN_CUSTOMER_KEYS = "A0FA";
    /**
     * RKI download ca certificate
     */
    String CLOUD_MESSAGE_TYPE_RKI_DOWN_CA = "A0FB";
}

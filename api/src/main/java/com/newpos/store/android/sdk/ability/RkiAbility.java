package com.newpos.store.android.sdk.ability;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.newpos.store.android.sdk.Constant.API_SUCCESS;
import static com.newpos.store.android.sdk.base.UrlConstant.QUERY_APP_CONFIG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.newpos.rki.ICallBack;
import com.newpos.rki.IRKIService;
import com.newpos.rki.RKIKey;
import com.newpos.rki.RKIOption;
import com.newpos.store.android.sdk.IStoreClient;
import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.QueryKdhurlRequest;
import com.newpos.store.android.sdk.dto.QueryKdhurlResponse;
import com.newpos.store.android.sdk.dto.RkiCode;
import com.newpos.store.android.sdk.listener.IRkiCallback;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName : RkiAbility
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2025/5/29-9:11
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class RkiAbility extends BaseAbility {

    private IRKIService rkiService;
    public RkiAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
    }

    public IRKIService getRkiService(){
        return rkiService;
    }

    public boolean bindRkiService(){
        Context context = BaseApi.getInstance().getContext();
        Intent intent = new Intent("com.newpos.rki.core.rkiservice");
        intent.setPackage("com.newpos.rki");
        boolean bind = context.bindService(intent, connection, BIND_AUTO_CREATE);
        BaseLog.d("bindRkiService result:"+bind);
        return bind;
    }

    public QueryKdhurlResponse getKdhUrl(QueryKdhurlRequest queryKdhurlRequest) throws BaseException {

        String response = BaseApi.getInstance().queryKdhurl(queryKdhurlRequest);
        if(TextUtils.isEmpty(response)){
            return null;
        }
        QueryKdhurlResponse queryKdhurlResponse = BaseUtils.toObject(response, QueryKdhurlResponse.class);
        if(queryKdhurlResponse == null){
            return null;
        }

        if(!Objects.equals(queryKdhurlResponse.code, API_SUCCESS)){
            throw new BaseException(queryKdhurlResponse.msg+"["+queryKdhurlResponse.code+"]");
        }

        return queryKdhurlResponse;

    }

    public RkiCode downloadCustomerKeys(String clientId, String kdhUrl, String messageId, IRkiCallback callback){
        BaseLog.e("downloadCustomerKeys>>");
        IRKIService irkiService = getRkiService();
        if(irkiService == null){
            BaseLog.e("rki service is null!");
            return RkiCode.BIND_ERROR;
        }
        try {
            RKIOption rkiOption = new RKIOption(kdhUrl, clientId, messageId);
            if(!rkiService.setOption(rkiOption)){
                BaseLog.e("setOption is error!");
                return RkiCode.INVOKE_ERROR;
            }
            irkiService.download(new ICallBack.Stub() {
                    @Override
                public void onCall(int code, String message, List<RKIKey> keyList) throws RemoteException {
                    if(callback != null){
                        callback.onDownload(code, message, keyList);
                    }
                    StringBuilder stringBuilder = new StringBuilder(message);
                    if(keyList != null){
                        stringBuilder.append("\n");
                        for(RKIKey key : keyList){
                            stringBuilder.append("KeyName: ").append(key.getName())
                                    .append(",type: ").append(key.getType())
                                    .append(",Index: ").append(key.getIndex())
                                    .append(",userID: ").append(key.getUserID())
                                    .append(",csn: ").append(key.getCsn())
                                    .append(",clientID: ").append(key.getClientId())
                                    .append(",KCV: ").append(key.getKcv()).append("\n");

                        }
                        BaseLog.d("onCall:"+stringBuilder);
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return RkiCode.INVOKE_SUCCESS;
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            rkiService = IRKIService.Stub.asInterface(service);
            try {
                Log.d("RKI", "RKI Support Status: "+(rkiService.isSupportRKI() ? "Support" : "UnSupport"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            rkiService = null;
        }
    };
}

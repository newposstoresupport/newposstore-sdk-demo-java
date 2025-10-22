package com.android.newpos.store.sdk.demo.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CellInfoUtil {
    private static final String TAG = "CellInfoUtil";
    private Context mContext;
    private TelephonyManager mTelephonyManager;

    public CellInfoUtil(Context context) {
        this.mContext = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取所有可用的基站信息
     */
    public List<CellInfoModel> getAllCellInfo() {
        List<CellInfoModel> cellInfoList = new ArrayList<>();
        
        // 检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) 
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "需要ACCESS_FINE_LOCATION权限");
                return cellInfoList;
            }
        }

        try {
            // 获取基站信息列表
            List<CellInfo> infos = mTelephonyManager.getAllCellInfo();
            if (infos == null || infos.isEmpty()) {
                Log.d(TAG, "未获取到基站信息");
                return cellInfoList;
            }

            // 解析不同类型的基站信息
            for (CellInfo info : infos) {
                if (info instanceof CellInfoGsm) {
                    // GSM基站
                    CellInfoGsm gsmInfo = (CellInfoGsm) info;
                    CellIdentityGsm identity = gsmInfo.getCellIdentity();
                    CellSignalStrengthGsm strength = gsmInfo.getCellSignalStrength();
                    
                    CellInfoModel model = new CellInfoModel();
                    model.type = "GSM";
                    model.cellId = identity.getCid();
                    model.lac = identity.getLac();
                    model.mcc = identity.getMcc();
                    model.mnc = identity.getMnc();
                    model.signalStrength = strength.getDbm();
                    model.asuLevel = strength.getAsuLevel();
                    model.isRegistered = info.isRegistered();
                    
                    cellInfoList.add(model);
                    
                } else if (info instanceof CellInfoCdma) {
                    // CDMA基站
                    CellInfoCdma cdmaInfo = (CellInfoCdma) info;
                    CellIdentityCdma identity = cdmaInfo.getCellIdentity();
                    CellSignalStrengthCdma strength = cdmaInfo.getCellSignalStrength();
                    
                    CellInfoModel model = new CellInfoModel();
                    model.type = "CDMA";
                    model.cellId = identity.getBasestationId();
                    model.lac = identity.getNetworkId();
                    model.systemId = identity.getSystemId();
                    model.signalStrength = strength.getDbm();
                    model.asuLevel = strength.getAsuLevel();
                    model.isRegistered = info.isRegistered();
                    
                    cellInfoList.add(model);
                    
                } else if (info instanceof CellInfoLte) {
                    // LTE基站(4G)
                    CellInfoLte lteInfo = (CellInfoLte) info;
                    CellIdentityLte identity = lteInfo.getCellIdentity();
                    CellSignalStrengthLte strength = lteInfo.getCellSignalStrength();
                    
                    CellInfoModel model = new CellInfoModel();
                    model.type = "LTE";
                    model.cellId = identity.getCi();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        model.earfcn = identity.getEarfcn();
                    }
                    model.pci = identity.getPci();
                    model.tac = identity.getTac();
                    model.mcc = identity.getMcc();
                    model.mnc = identity.getMnc();
                    model.signalStrength = strength.getDbm();
                    model.asuLevel = strength.getAsuLevel();
                    model.isRegistered = info.isRegistered();
                    
                    cellInfoList.add(model);
                    
                } else if (info instanceof CellInfoWcdma) {
                    // WCDMA基站
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        CellInfoWcdma wcdmaInfo = (CellInfoWcdma) info;
                        CellIdentityWcdma identity = wcdmaInfo.getCellIdentity();
                        CellSignalStrengthWcdma strength = wcdmaInfo.getCellSignalStrength();
                        
                        CellInfoModel model = new CellInfoModel();
                        model.type = "WCDMA";
                        model.cellId = identity.getCid();
                        model.lac = identity.getLac();
                        model.psc = identity.getPsc();
                        model.mcc = identity.getMcc();
                        model.mnc = identity.getMnc();
                        model.signalStrength = strength.getDbm();
                        model.asuLevel = strength.getAsuLevel();
                        model.isRegistered = info.isRegistered();
                        
                        cellInfoList.add(model);
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (info instanceof CellInfoNr && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 5G基站
                        CellInfoNr nrInfo = (CellInfoNr) info;
                        CellIdentityNr identity = (CellIdentityNr) nrInfo.getCellIdentity();
                        CellSignalStrengthNr strength = (CellSignalStrengthNr) nrInfo.getCellSignalStrength();

                        CellInfoModel model = new CellInfoModel();
                        model.type = "5G NR";
                        model.cellId = Math.toIntExact(identity.getNci());
                        model.pci = identity.getPci();
                        model.tac = identity.getTac();
                        model.signalStrength = strength.getDbm();
                        model.asuLevel = strength.getAsuLevel();
                        model.isRegistered = info.isRegistered();

                        cellInfoList.add(model);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取基站信息失败: " + e.getMessage());
        }
        
        return cellInfoList;
    }
    
    /**
     * 基站信息数据模型
     */
    public static class CellInfoModel {
        public String type;          // 基站类型(GSM, CDMA, LTE, WCDMA, 5G NR)
        public int cellId;           // 基站ID
        public int lac;              // 位置区域码
        public int mcc;              // 移动国家代码
        public int mnc;              // 移动网络代码
        public int psc;              // 主扰码(用于WCDMA)
        public int earfcn;           // 频点(用于LTE)
        public int pci;              // 物理小区标识(用于LTE和5G)
        public int tac;              // 跟踪区域码(用于LTE和5G)
        public int systemId;         // 系统ID(用于CDMA)
        public int signalStrength;   // 信号强度(dBm)
        public int asuLevel;         // 信号强度(asu)
        public boolean isRegistered; // 是否是当前注册的基站
        
        @Override
        public String toString() {
            return "类型: " + type + 
                   ", 基站ID: " + cellId +
                   ", 信号强度: " + signalStrength + "dBm" +
                   ", 是否注册: " + (isRegistered ? "是" : "否");
        }
    }
}

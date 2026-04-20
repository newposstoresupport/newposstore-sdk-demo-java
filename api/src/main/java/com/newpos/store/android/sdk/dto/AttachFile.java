package com.newpos.store.android.sdk.dto;

import androidx.annotation.NonNull;

/**
 * @ClassName : AttachFile
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/18-15:59
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public class AttachFile {
    /**
     * 补丁包类型 {@link PatchType}
     */
    public int patchType;
    /**
     * 文件HASH
     */
    public String patchHash;
    /**
     * 文件版本
     */
    public int patchVer;
    /**
     * 参数文件url
     */
    public String patchUrl;
    /**
     * 文件大小
     */
    public long fileSize;
    /**
     * 文件id
     */
    public int id;
    /**
     * 文件路径
     */
    public String filePath;
    /**
     * zip文件夹
     */
    public String fileDir;

    @NonNull
    @Override
    public String toString() {
        return "AttachFile{" +
                "patchType=" + patchType +
                ", patchHash='" + patchHash + '\'' +
                ", patchVer=" + patchVer +
                ", patchUrl='" + patchUrl + '\'' +
                ", fileSize=" + fileSize +
                ", id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileDir='" + fileDir + '\'' +
                '}';
    }
}

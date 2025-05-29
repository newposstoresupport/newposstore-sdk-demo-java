package com.newpos.store.android.sdk.dto;

/**
 * @ClassName : PatchType
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/12/18-16:07
 * @Version : 1.0
 * @Description :
 * @website : <a href="https://www.newpostech.com/">...</a>
 */
public enum PatchType {
    /**
     * 查询参数文件和热修包
     */
    ALL,

    /**
     * 查询参数文件
     */
    PARAMS,

    /**
     * 热修包
     */
    HOTFIX,
}

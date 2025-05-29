// IAppInquirer.aidl
package com.newpos.store.android.sdk;

// Declare any non-default types here with import statements

/**
 * English Application query interface description
 * Chinese 应用查询器接口描述
 * author: zhouqiang(1376359644@qq.com)
 * date: 2024/9/31 15:06
 * email: newpos@newpostech.com
 *
 * @author NEWPOS
 */
interface IAppInquirer {
   /*
   * Chinese 是否可以更新当前应用程序
   * English Is it possible to update the current application
   *
   * @return boolean Chinese 如果当前应用程序忙与处理，则应用返回false。如果空闲，则应用返回true
   *                 English If the current application is busy with processing, the application returns false. If it is idle, the application returns true
   *
   * @attention Chinese 建议处理策略，当交易时进行全局标记，交易结束将标记清除
   *            English Suggested processing strategy: global marking is performed when the transaction is started, and the mark is cleared when the transaction is completed
   *
   */
    boolean onReadyToUpdate();
}
// IStoreClient.aidl
package com.newpos.store.android.sdk;

import com.newpos.store.android.sdk.IAppInquirer;
import com.newpos.store.android.sdk.dto.TerminalInfo;
import com.newpos.store.android.sdk.dto.AuthenticationInfo;
import com.newpos.store.android.sdk.dto.AuthenticationRequest;
import com.newpos.store.android.sdk.dto.AppElements;

// Declare any non-default types here with import statements

/**
 * English Store client interface description
 * Chinese Store客户端接口描述
 * author: zhouqiang(1376359644@qq.com)
 * date: 2024/8/31 15:06
 * email: newpos@newpostech.com
 *
 * @author NEWPOS
 */
interface IStoreClient {
    /*
    * Chinese 获取终端信息
    * English Get terminal information
    *
    * @return TerminalInfo
    *
    * @deprecated Chinese 请使用AuthenticationInfo获取终端信息 @{@link AuthenticationInfo}
    *             English Please use AuthenticationInfo to obtain terminal information @{@link AuthenticationInfo}
    */
    TerminalInfo getBaseTerminalInfo();

    /*
    * Chinese 获取注册认证信息
    * English Get registration authentication information
    *
    * @params elements Chinese 应用三要素信息 {@link AppElements}
    *                  English Three elements of application @{@link AppElements}
    */
    AuthenticationInfo getAuthenticationInfo(in AppElements elements);

    /*
    * Chinese 获取注册认证信息
    * English Get registration authentication information
    *
    * @params elements Chinese 应用三要素信息 {@link AppElements}
    *                  English Three elements of application @{@link AppElements}
    *
    * @params request Chinese 请求信息 {@link AuthenticationRequest}
    *                 English Request Information @{@link AuthenticationRequest}
    */
    AuthenticationInfo getAuthenticationInfoEx(in AppElements elements, in AuthenticationRequest request);

    /*
    * Chinese 获取通道信息
    * English Get channel information
    *
    * @params json Chinese 请求参数
    *              English Request Parameters
    *
    * @return String Chinese 通道信息
    *                English channel information
    *
    * @deprecated Chinese 请使用兼容函数dynamicRequest获取通道信息
    *             English Please use the compatible function dynamicRequest to obtain channel information
    */
    String getTunnelInfo(String json);

    /*
    * Chinese 查询应用配置信息
    * English Query application configuration information
    *
    * @params json Chinese 请求参数
    *              English Request Parameters
    *
    * @return String Chinese 应用配置信息
    *                English application configuration information
    *
    * @deprecated Chinese 请使用兼容函数dynamicRequest查询应用配置信息
    *             English Please use the compatible function dynamicRequest to query application configuration information
    */
    String queryAppConfig(String json);

    /*
    * Chinese 注册应用查询器
    * English Register Application Inquirer
    *
    * @params pkgName Chinese 应用程序包名
    *                 English Application package name
    *
    * @params inquirer Chinese 应用程序查询器 @{@link IAppInquirer}
    *                  English Application Inquirer @{@link IAppInquirer}
    *
    * @return boolean Chinese 注册结果
    *                 English Registration Results
    *
    */
    boolean initAppInquirer(String pkgName, in IAppInquirer inquirer);

    /*
    * Chinese 动态请求服务器数据
    * English Dynamically request server data
    *
    * @params url Chinese 请求url
    *             English Request URL
    *
    * @params headers Chinese 增加的请求头信息
    *                 English Added request header information
    *
    * @params json Chinese 请求报文体
    *              English Request message body
    *
    * @return String Chinese 响应报文体
    *                English Response message body
    *
    */
    String dynamicRequest(String url, in Map<String, String> headers, String json);

    /*
    * Chinese 动态请求服务器数据
    * English Dynamically request server data
    *
    * @params fileUrl Chinese 文件下载url
    *                 English File download url
    *
    * @params filePath Chinese 文件保存路径
    *                  English File save path
    *
    * @return String Chinese 下载结果, SUCCESS 或者其它失败信息
    *                English Download Results, SUCCESS or other failure information
    *
    */
    //String downloadFile(String fileUrl, String filePath);
}
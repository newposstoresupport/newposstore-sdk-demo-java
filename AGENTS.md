# AGENTS.md

## 适用范围

本文件适用于整个仓库。

这是一个较老的 Android Java 版 NEWPOSSTORE SDK Demo 项目。后续开发时要把它当作“集成示例项目”处理：优先保持现有行为，除非用户明确要求改动业务逻辑。

## 表达规则

- 中文说明业务背景、风险、流程、结论和开发建议。
- 英文保留技术关键词、命令、路径、类名、方法名、模块名、Gradle task、package、permission、API name。
- 不要把技术关键词强行翻译成中文，例如保留 `StoreSdk`、`BaseApi`、`ViewModel`、`Fragment`、`WorkManager`、`dynamicRequest`。

## 禁止事项

- 不要在未被明确要求时修改业务代码。
- 不要顺手重构、格式化或重写无关文件。
- 不要修改 `build/`、`.gradle/`、`.idea/` 下的生成文件或 IDE 文件。
- 不要新增密钥、token、私有地址、机器本地路径或其他敏感信息。
- 不要在未确认方案前移除或替换现有 demo 凭据、签名配置和 `app/newstore.jks`。
- 不要把当前本地依赖的 `api`、`aidl` 模块改成 Maven 远程依赖，除非任务目标就是验证外部 SDK 集成。
- 不要绕过现有 AIDL、Store Core、RKI、WorkManager、FileDownloader 流程另起一套实现。

## 技术栈

- 语言：Android Java。
- 构建：Gradle Wrapper `8.7`，Android Gradle Plugin `8.5.1`。
- Android 配置：`compileSdk 33`、`targetSdk 33`、`minSdk 22`、Java 8。
- App namespace：`com.android.newpos.store.sdk.demo`。
- `api` 和 `aidl` namespace：`com.newpos.store.android.sdk`。
- Product flavors：`dft`、`LM`、`ZL`，flavor dimension 为 `env`。
- 主要依赖：
  - AndroidX AppCompat、Material、ConstraintLayout、Lifecycle、Navigation、Preference、Window。
  - RxJava2 / RxAndroid。
  - WorkManager。
  - MMKV。
  - OkHttp、logging-interceptor、Gson。
  - FileDownloader、filedownloader-okhttp3-connection。
  - 本地 AAR：`app/libs/android-baserecyle-master-v1.1.aar`。
  - 本地 JAR：`api/libs/rki_v1.2.250804.jar`。

## 目录结构

- `app/`：Demo APK。包含 UI、启动逻辑、云消息接收、参数下载、WorkManager 后台任务、文件下载辅助类。
- `api/`：SDK Java 封装层。`StoreSdk` 是外部入口，`BaseApi` 负责绑定 Store Core AIDL 服务和转发动态请求。
- `aidl/`：AIDL 接口和 Parcelable DTO，与 NewStore Core 通信。
- `docs/`：集成说明和图片/视频资源。部分文档目前为空或占位。
- `gradle/`：Gradle wrapper。

分析源码时忽略这些目录：`.gradle/`、`build/`、`.idea/`。

## 启动入口

- 进程入口：`app/src/main/java/com/android/newpos/store/sdk/demo/MainApplication.java`
  - 初始化 MMKV。
  - 初始化 `SPreference`。
  - 初始化 FileDownloader。
  - 后台调用 `initStoreSdk(AppUtils.getClientId(), null)`。
- Launcher Activity：`app/src/main/java/com/android/newpos/store/sdk/demo/MainActivity.java`
  - 使用 `ActivityMainBinding`。
  - 默认加载 `MainFragment`。
  - 设置 FileDownloader 默认保存路径。
  - 如果 Intent 中带有云消息参数任务 ID，则直接打开 `ParamFragment`。
- 主菜单：`app/src/main/java/com/android/newpos/store/sdk/demo/MainFragment.java`
  - 跳转到 Common、App Management、Registration、App Inquirer、LBS、Parameters、OTA、Cloud Message、RKI。
- 云消息入口：`app/src/main/java/com/android/newpos/store/sdk/demo/CloudMessageReceiver.java`
  - Manifest 静态注册。
  - 监听 `com.newstore.action.CLOUD_MESSAGE_ARRIVED` 和 `com.newstore.action.CLOUD_MESSAGE_CLICKED`。

## 核心业务流程

1. `MainApplication` 从 flavor 对应的 `BuildConfig.APPID`、`APPKEY`、`APPSECRET` 组装 `AppElements`。
2. `StoreSdk.init(...)` 校验应用三要素，并调用 `BaseApi.init(...)`。
3. `BaseApi` 绑定 Store Core 服务：
   - action：`com.newpos.store.android.app.ACTION_STORE`
   - package：`com.newpos.store.android.app`
4. Store Core 通过 AIDL 返回 `AuthenticationInfo`。
5. `StoreSdk` 根据返回的 `apiPaths` 初始化能力类：
   - `AppAbility`
   - `CloudMessageAbility`
   - `GoInsightAbility`
   - `LbsAbility`
   - `ParamAbility`
   - `ParamAbilityV2`
   - `UpgradeAbility`
   - `OtaAbility`
   - `RkiAbility`
6. 大部分能力通过 `BaseApi` 调用 `IStoreClient.dynamicRequest(...)`，接口名来自 `UrlConstant`。
7. RKI 额外绑定 `com.newpos.rki`，action 为 `com.newpos.rki.core.rkiservice`。

## 构建命令

在仓库根目录用 PowerShell 执行：

```powershell
.\gradlew.bat :app:assembleDftDebug
.\gradlew.bat :app:assembleLMDebug
.\gradlew.bat :app:assembleZLDebug
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:assembleRelease
```

注意：

- `:app:assembleDebug` 可能构建所有 debug flavor。
- release 构建使用 `app/build.gradle` 中的 `newstore` 签名配置。
- APK 文件名由脚本动态生成：`NewStoreSdkDemo-<flavor>-<versionName>-<buildType>.apk`。
- `versionName` 包含时间戳，每次构建 APK 文件名都会变化。

## 测试命令

单元测试：

```powershell
.\gradlew.bat test
.\gradlew.bat :app:testDftDebugUnitTest
.\gradlew.bat :api:testDebugUnitTest
.\gradlew.bat :aidl:testDebugUnitTest
```

仪器测试，需要 Android 真机或模拟器：

```powershell
.\gradlew.bat :app:connectedDftDebugAndroidTest
.\gradlew.bat :api:connectedDebugAndroidTest
.\gradlew.bat :aidl:connectedDebugAndroidTest
```

当前测试大多是模板测试。涉及 Store Core、云消息、RKI、参数下载的改动通常需要设备级联调。

## 运行命令

安装并启动默认 `dft` flavor：

```powershell
.\gradlew.bat :app:installDftDebug
adb shell am start -n com.android.newpos.store.sdk.demo/.MainActivity
```

各 flavor 包名：

```text
dft: com.android.newpos.store.sdk.demo
LM:  com.android.newpos.limiao.sdk.demo
ZL:  com.zhangle_sdk.test
```

其他 flavor 启动示例：

```powershell
adb shell am start -n com.android.newpos.limiao.sdk.demo/com.android.newpos.store.sdk.demo.MainActivity
adb shell am start -n com.zhangle_sdk.test/com.android.newpos.store.sdk.demo.MainActivity
```

运行前提：

- 本机 Android SDK 配置可用，或 `local.properties` 指向正确 SDK。
- 目标设备已安装 NewStore Core，否则 SDK 初始化会失败。
- RKI 流程需要设备已安装 RKI app。
- 参数下载、App 查询、RKI 查询等流程需要网络和平台侧配置。

## 代码分层规范

保持现有纵向分层：

- `app/.../demo/<feature>/`
  - `*Fragment`：只处理 ViewBinding、点击事件、弹窗、页面显示和导航。
  - `*ViewModel`：处理异步任务、loading 状态、LiveData 输出和 SDK 调用编排。
- `app/.../demo/base/`
  - 放 Demo 侧通用能力，如 `BaseFragment`、`BaseViewModel`、`AppUtils`、`DownloadFileManager`、`DownloadWorker`。
- `api/.../sdk/`
  - `StoreSdk` 是 SDK 单例入口。
  - `ability/` 放具体能力封装。
  - `dto/` 放请求/响应模型。
  - `listener/` 放回调接口。
  - `base/BaseApi` 放 Store Core AIDL 绑定和动态请求。
- `aidl/`
  - 只放 AIDL 接口和跨进程 DTO。

新增功能时按这个顺序思考：

1. API 合同需要变更时，先补 `api/.../dto`。
2. SDK 能力封装放到对应 `api/.../ability`。
3. Store Core 绑定、动态请求、兼容分支仍放在 `BaseApi`。
4. Demo UI 放在 `app/.../demo/<feature>`，保持 Fragment + ViewModel 分离。
5. 集成方式变化时同步更新 `docs/`。

## 风险点

### 凭据和签名

- `app/build.gradle` 硬编码了三套 `APPID`、`APPKEY`、`APPSECRET`。
- `app/build.gradle` 硬编码了签名密码。
- 仓库中存在 `app/newstore.jks`。
- 后续不要新增任何密钥。安全治理任务应优先迁移到本地 Gradle properties 或 CI secret 注入。

### 权限

Manifest 当前声明：

- `INTERNET`
- `ACCESS_NETWORK_STATE`
- `POST_NOTIFICATIONS`
- `SYSTEM_ALERT_WINDOW`
- `FOREGROUND_SERVICE`
- `store.permission.RECEIVE_CLOUD_MESSAGE`
- `store.permission.BIND_STORE_SERVICE`
- `rkiservice.permission.downloadkey`

涉及通知、广播、悬浮窗、前台服务、RKI 的改动，要在对应 Android 版本上验证权限行为，尤其是 Android 13+ 通知权限和高版本广播限制。

### 接口和跨进程服务

- Store Core 依赖包名 `com.newpos.store.android.app`。
- RKI 依赖包名 `com.newpos.rki`。
- `BaseApi` 通过 `IStoreClient.dynamicRequest(...)` 调用平台接口。
- 老版本 Store Core 兼容逻辑在 `BaseUtils.is0116NewStore(...)`。
- `StoreSdk.init(...)` 使用 `Semaphore` 控制初始化，存在并发初始化跳过 callback 的风险，改动时要谨慎。
- `BaseApi` 有 binder death 重连逻辑，但 `serviceConnection != null` 且 `storeClient == null` 的路径需要重点关注。

### 网络和证书

- `app/src/main/res/xml/network_security_config.xml` 允许明文流量。
- `DownloadFileManager` 的 OkHttp 开启了 BODY 级日志。
- `DownloadFileManager` 的 `hostnameVerifier` 永远返回 `true`。
- `MySafeTrustManager.checkClientTrusted(...)` 当前为空实现。
- 不要扩大不安全网络行为。凡是触碰下载、TLS、证书、日志，都要在交付说明中明确安全影响。

### 文件下载和参数替换

- 参数文件下载到 app external files download 目录。
- V2 参数流程会下载 zip、解压、替换 XML 模板变量，然后回传结果。
- `AppUtils.unzipFiles(...)` 需要注意 zip path traversal、嵌套目录、异常 zip。
- 模板替换需要注意正则特殊字符、XML 转义、加密字段解密失败。
- `ParamFragment` 注册了下载完成 receiver，改动时要检查生命周期注销。

### 数据库/本地存储

- 项目没有 Room/SQLite 数据库层。
- 本地轻量存储使用 MMKV 和 `SharedPreferences`：
  - `AppUtils` 使用 MMKV 保存 `clientId`。
  - `SPreference` 保存 App Inquirer 的 ready 状态。
- 不要把敏感凭据、密钥、证书私钥写入 MMKV 或 SharedPreferences。

### 编码

项目中已有部分中文注释和 README 出现乱码。修改旧文件时尽量保持原文件编码和最小改动。新建开发说明可以使用中文，但路径、命令、类名保持原样。

## 开发流程

1. 先读受影响模块和调用链，不要凭猜测改。
2. 先执行 `git status --short`，确认已有用户改动。
3. 只改任务相关文件，补丁越小越好。
4. 保持现有 Java 风格、包结构和命名方式。
5. UI 展示留在 Fragment，业务编排留在 ViewModel。
6. SDK facade 逻辑不要下沉到 `app`。
7. Demo 专用的下载/展示逻辑不要放进 `api`，除非它成为 SDK 合同的一部分。
8. 改完后运行最小必要 Gradle 命令。
9. 如果构建、测试、设备验证因为网络、设备或权限不可用而无法执行，要在最终回复中说明。

## 提交前检查

交付前确认：

- `git status --short` 只包含本次预期文件。
- 没有未被要求的业务代码改动。
- 没有新增生成文件、IDE 文件、本地路径、APK、jar、keystore 或密钥。
- 已运行相关构建命令，或说明未运行原因。
- 已运行相关测试命令，或说明未运行原因。
- 新代码符合 Fragment / ViewModel / SDK / DTO 分层。
- 涉及 Store Core 或 RKI 时，说明设备和服务依赖。
- 涉及安全敏感点时，说明凭据、权限、证书、明文流量、日志影响。

## 常用只读排查命令

```powershell
Get-ChildItem -Force
git status --short
Get-ChildItem app\src,api\src,aidl\src,docs -Recurse -File
Select-String -Path app\src\main\java\**\*.java -Pattern "setOnClickListener|observe|StoreSdk"
```

当前环境可能没有 `rg`。如果 `rg` 不可用，使用 PowerShell 的 `Get-ChildItem` 和 `Select-String`。

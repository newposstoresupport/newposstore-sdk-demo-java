# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- 云消息 `A010`、`A011`：收到商店转发后自己下载参数。`A010` 走 V1，`A011` 走 V2。对齐 Maven `api:1.0.5`，`aidl` 仍为 `1.2`。
- `TerminalLocationAbility` 与 Demo 单独一页：经商店 AIDL 取终端谷歌/百度定位（含地址）。Maven `api:1.0.4`，需 `aidl:1.2`。
- Firmware OTA demo：查询 / 下载固件（对齐 StoreSdk api `1.0.3`）
- `RomDeviceReflect`：ROM `DevConfig` / `SDKManager` 反射调用（不依赖公开 `sdk.jar`）
- `local.properties` 提供 demo 测试凭据（随仓库提交，供客户直接运行）
- `changelog.txt` 与 StoreSdk 发布说明对齐

### Changed
- README Maven 示例版本更新为 `api:1.0.5`，`aidl` 仍为 `1.2`。
- 凭据改为从 `local.properties` / 环境变量读取，不再硬编码在 `app/build.gradle`
- README Maven 示例版本更新为 `api:1.0.4` / `aidl:1.2`
- Param 相关 demo 与 StoreSdk 基准对齐（含 Param V2）
- 移除公开仓库中的 `app/libs/sdk.jar` 及 Gradle `compileOnly` 依赖

### Fixed
- OTA 页面此前仅为 stub，现可演示固件查询与下载流程

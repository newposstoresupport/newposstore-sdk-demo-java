# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Firmware OTA demo：查询 / 下载固件（对齐 StoreSdk api `1.0.3`）
- `app/libs/sdk.jar`（ROM `DevConfig` / `SDKManager`，`compileOnly`）
- `local.properties` 提供 demo 测试凭据（随仓库提交，供客户直接运行）
- `changelog.txt` 与 StoreSdk 发布说明对齐

### Changed
- 凭据改为从 `local.properties` / 环境变量读取，不再硬编码在 `app/build.gradle`
- README Maven 示例版本更新为 `api:1.0.3`
- Param 相关 demo 与 StoreSdk 基准对齐（含 Param V2）

### Fixed
- OTA 页面此前仅为 stub，现可演示固件查询与下载流程

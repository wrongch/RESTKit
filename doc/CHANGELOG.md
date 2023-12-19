# CHANGELOG

## 3.2.1

- Fix bugs: download file exception
- Fix bugs: config lost bug.

- 修复下载文件bug
- 修复配置丢失bug
- 非Windows用户可在 `registry` 中设置部分UI显示比例 `restkit.ui.scale`


## 3.2.0

- Optimize & Fix bugs.
- Change UI configs from project level to global level.
- Support exporting and importing parameters.
- Support displaying multi-level service tree and multiple request tabs with limit. All user can disable these in plugin setting.
- New project manager ui.

- 修复bug，优化细节
- 调整部分配置入口，UI配置升级为全局有效
- 新增支持导出导入请求参数
- 非订阅用户支持有限的多层级树节点展示和多请求面板，所有用户均可在设置中关闭
- 新增项目数据管理面板

其他说明：

1、扫描框架设置入口迁移到 Toolbar

![](zh_CN/快速入门/images/1691681334145.png)

2、请求参数导入导出入口：

![](zh_CN/核心功能/images/1691680441980.png)

3、项目数据管理面板

![](zh_CN/快速入门/images/1691681334146.png)

![](zh_CN/快速入门/images/1691681334147.png)

4、全局项目切换入口

![](zh_CN/快速入门/images/1691681334148.png)

5、关闭多层级树节点展示和多请求面板

![](zh_CN/快速入门/images/1691889605770.png)


## 3.1.1

- Fix compatibility issue with IDE 2023.2

## 3.1.0

- Fix bugs.
- Premium: Support displaying or sending multiple requests at the same time. [see](https://github.com/wrongch/RESTKit/blob/main/doc/zh_CN/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/%E7%AA%97%E5%8F%A3%E4%BB%8B%E7%BB%8D.md#request-client)

- 修复bug；
- 高级功能: 新增支持展示多个请求面板, 可同时发起多个请求；[详见](https://github.com/wrongch/RESTKit/blob/main/doc/zh_CN/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/%E7%AA%97%E5%8F%A3%E4%BB%8B%E7%BB%8D.md#request-client)

![](zh_CN/快速入门/images/169784614253607.png)

## 3.0.3

- Optimize high-resolution screen display.
- Optimize file settings.
- Support environment variable for request body.
- Support to minify json content.
- Support to set environment variables in pre-request and post-request script.
- Fix bugs (curl, parameter delete exception).

- 优化高分辨率屏幕显示;
- 优化全局文件目录设置;
- 请求body支持环境变量;
- 支持压缩JSON;
- 前置/后置脚本支持设置环境变量;
- 修复bug (curl复制、参数库删除异常).

## 3.0.2

- Optimize & Fix bugs.
- All tab support saving content.

- 修复bug.
- 全部Tab支持保存内容.

## 3.0.1

[3.0 Upgrade Guide - 3.0升级指南](https://github.com/wrongch/RESTKit/blob/main/doc/zh_CN/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/3.0%E5%8D%87%E7%BA%A7%E6%8C%87%E5%8D%97.md)

- Optimize & Fix bugs.
- Support parsing constants in springmvc mappings.
- Support MySQL datasource.
- Optimize document.

- 修复bug.
- 支持解析springmvc mapping中的常量.
- 支持MySQL数据源.
- 更新文档.

## 3.0.0

https://plugins.jetbrains.com/plugin/14723-restfulbox/versions/stable/277327

- Optimize & Fix bugs.
- New UI.
- Premium(paid): Support multi level service tree.
- Premium(paid): Support custom dataSource, view service items crossing projects.

- 细节优化，解决一些遗留问题.
- 新UI支持.
- 高级功能(付费): 支持多层级service tree.
- 高级功能(付费): 新增数据源，跨项目访问其他项目接口，提供扩展点对接三方平台/数据库.

_由于国内某些同类付费插件大量抄袭了本插件功能，且参差不齐，因此3.0之后的源码不再开放，如有插件开发的问题（不限于技术问题），可以在本GitHub地址提问。_

## 2.0.8

- Support ssl.
- Optimize http request, support upload/download file.
- Optimize curl.

- 支持SSL（单项认证和双向认证）
- 优化http请求，支持上传下载文件
- 优化复制curl


## 2.0.6

- fix bugs with 2022.2


## 2.0.5

- Add icon for springmvc mapping methods. Enable [Jump to tree using method line marker] in the setting.
- Remove jump to tree intention.

- 添加图标，支持springmvc方法快速生成请求，在设置中启用【Jump to tree using method line marker】
- 移除上下文菜单中的跳转到树窗口

![](https://s3.bmp.ovh/imgs/2022/04/10/3481cea48dae9d12.png)


## 2.0.4

- Optimize details.

- 优化细节

## 2.0.3

- support api group by file name.
- support rest client ep.
- support param filter for springmvc

- 扫描的API支持按文件名显示分组
- 发送请求提供扩展点，即将提供Dubbo服务支持.
- springmvc接口扫描支持param参数过滤

## 2.0.2

- more powerful api local store, support synchronization in different IDE.
- fix bugs.

- API本地存储升级，支持跨IDE同步


## 2.0.1

- support more jetbrains ides.
- support api local storage, export and import by default.
- most particular optimization.

- 支持更多的jetbrains产品
- 默认支持API本地存储/导出/导入
- 大量细节优化


## 2.0.0

- add extension for scanning restful services from other web framework.
- support some common config.
- support pre-request and post-request script.

- 提供扩展点，支持自定义web框架中的接口扫描与展示
- 增加一些通用配置
- 支持请求前置/后置脚本


## 1.0.8

- Integrate URL search into native Search Everywhere for idea 211.*.
- support scan library services.
- support environment migration.
- support restfull client parameter operation(Headers/Params/Body).

- 在Search Everywhere中集成URL搜索
- 支持扫描依赖包的接口
- 环境支持导出导入，一键迁移
- 支持restfull client参数操作(Headers/Params/Body)


## 1.0.7

- Integrate URL search into native Search Everywhere for idea 203.*.

- 在Search Everywhere中集成URL搜索 (idea 203.*)


## 1.0.6

- Integrate URL search into native Search Everywhere.

- 在Search Everywhere中集成URL搜索


## 1.0.5

- fix bugs.


## 1.0.4

- fix bugs.


## 1.0.3

- show http request info.
- update custom logging format.
- fix known issues and support 2021.2.

- 显示HTTP请求信息
- 更新日志打印格式
- 修复已知问题，兼容2021.2


## 1.0.2

- fix compatibility problems with 2020.

- 修复2020版本兼容性问题。


## 1.0.1

- print request logs to file (path: `$PROJECT_PATH$/.idea/restkit/*.log`);
- parse header from springmvc annotation.
- support copying current Environment(can use to rename if you want).

- 请求日志输出到文件（path: `$PROJECT_PATH$/.idea/restkit/*.log`）;
- 解析header (已验证java语言);
- 支持复制当前环境变量（可间接重命名）。


## 1.0.0

- support jump to api in tree window from Controller method ( use: ⌥ + ↵) ;
- more useful http client: supporting global request header/environment and request script, delete requests support body, param/body mocking more powerful, etc.
- fix bugs, remove useless function.

- 支持从方法跳转到tree窗口中对应的接口（在方法上按⌥ + ↵，如不存在需先刷新接口）;
- http工具更好用，支持全局请求头、环境变量和请求脚本，delete支持body参数，mock参数识别增强等等;
- 修复兼容性问题，去除了用处不大的功能（右键copy full url等）。


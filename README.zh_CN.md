<a href="https://plugins.jetbrains.com/plugin/14723-restkit">![bg](doc/en/images/bg2.png)</a>

## 一套功能强大的 Restful 服务开发辅助工具集


[英文](./README.md) | [Github](https://github.com/wrongch/RESTKit) | [Gitee](https://gitee.com/wrongch/RESTKit) | [Jetbrains](https://plugins.jetbrains.com/plugin/14723-restkit/reviews)

**RestfulBox**插件致力于提升开发效率，只有实用常用的功能。源于最初版本的RestfulToolkit，同时融入了Postman的常用功能，丰富且完善的功能能极大地提高了Idea开发的效率。曾用名：RESTKit。

## 特性 ([3.0升级指南](doc/zh_CN/快速入门/3.0升级指南.md))
- [x] 支持更多的jetbrains产品，不仅仅是idea
- [x] Restful服务自动扫描、展示、跳转和导入导出
  - 原生Search Everywhere支持restful URL搜索 (<kbd>Ctrl \\</kbd> or <kbd>Ctrl Alt N</kbd>)
  - 跨IDE跨项目浏览所有服务
  - 窗口显示多层级 Services tree
  - URL和Method相互跳转
- [x] 强大好用的请求工具：
  - 多标签：可同时展示、发起多个请求
  - 多协议：默认支持http，可扩展支持dubbo等
  - 自定义参数格式，支持占位符变量，JSON自动格式化
  - 环境变量：支持变量使用、管理及迁移，提供内置函数和脚本函数
  - 全局请求头：支持和环境变量一起使用，提供内置函数和脚本函数
  - 参数库：支持Headers、Params、Body参数展示、保存和删除
  - 请求脚本：支持前置/后置请求脚本
  - 请求响应信息展示报文格式，支持保存到日志文件
- [x] 语言和框架：
  - 默认支持存储服务到本地文件
  - idea默认支持 Spring 体系接口 (Spring MVC with Java or Kotlin)
- [x] 数据源: 支持多种数据源存储数据，更安全更可控，可本地可云端同步可定制
- [x] 插件扩展：提供多个扩展点，便于自定义需求的实现

## 生态

通过公开的扩展点，可以轻松实现一些自定义需求，具体参考生态章节。当前已支持：
- 扫描类型
  - [x] Spring MVC：默认支持，支持Java and Kotlin实现
  - [x] Jax-Rs：通过插件支持，见 [RESTKit-JAX-RS](https://github.com/huzunrong/RESTKit-JAX-RS)
  - [x] Dubbo：通过插件支持，见 [RESTKit-Dubbo](https://github.com/wrongch/RESTKit-Dubbo) ，支持扫描和发送请求
  - [x] Solon：通过插件支持，见 [RestfulBox-Solon](https://github.com/wrongch/RestfulBox-Solon) ，支持扫描和请求发送
- 存储类型（建议使用数据源）
  - [x] Redis：通过插件支持，见 [RESTKit-Redis](https://github.com/wrongch/RESTKit-Redis) ，支持存储API到redis和简单的redis命令发送
  - [x] Local Store：默认支持，支持存储API到本地文件
- 协议类型
  - [x] HTTP/HTTPS：默认支持
  - [x] DUBBO：同上
  - [x] Redis：同上
- 数据源（支持存储插件的所有数据到数据库）
  - [x] Sqlite数据源：默认3.0.0开始支持
  - [x] MySQL数据源：默认3.0.1开始支持

## 使用文档
- [中文文档-Github](https://github.com/wrongch/RESTKit/blob/main/doc/zh_CN/%E7%9B%AE%E5%BD%95.md)  [Gitee](https://gitee.com/wrongch/RESTKit/blob/main/doc/zh_CN/%E7%9B%AE%E5%BD%95.md)
- [English Document](doc/en/README.md)
- [CHANGELOG](doc/CHANGELOG.md)

## 关于插件
该插件的所有功能源于我本人日常开发中的点点滴滴，且一直以来只有我一人维护，真的是为爱发电了。虽然`3.0`开始新增了付费订阅方式，但仅针对新增的一些高级功能，对于历史版本已有功能以及未来可能会补充的基本功能仍然免费使用。如果有需要，不妨支持一下，点亮小星星，同时也欢迎提供宝贵的建议。:star2:

关于源码：由于国内某些同类付费插件大量抄袭了本插件功能（环境变量、前后置脚本等），且参差不齐，因此3.0之后的源码选择性更新，如有插件开发的问题，欢迎交流。

:fire: **_如果你想更加充分地体验订阅功能，作者免费赠送3个月使用。如有需要，可通过邮件联系作者，备注好需求和你的 [Jetbrains邮箱账号](https://account.jetbrains.com/profile-details) !!_**

[Issues](https://github.com/wrongch/RESTKit/issues) | [Email](mailto:huzunrong@foxmail.com) | [Jetbrains评分](https://plugins.jetbrains.com/plugin/14723-restkit/reviews)

> 注意  
> 反馈时请务必附上必要信息：Idea版本、插件版本、异常内容、复现方式、诉求等。


## 支持作者
你的支持是鼓励我前行的动力，非常感谢~

![pay](doc/en/images/pay.png)
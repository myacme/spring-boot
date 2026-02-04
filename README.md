# Spring Boot 项目简介

## 项目概述

这是一个基于 Spring Boot 2.5.0 构建的现代化 Java Web 应用程序。该项目集成了多种常用的技术组件和功能模块，包括多数据源支持、AOP 日志记录、JWT 认证、WebSocket 实时通信等功能。

## 技术栈

- **核心框架**: Spring Boot 2.5.0
- **编程语言**: Java 11
- **持久层**: MyBatis 2.2.0 + MyBatis Plus 动态数据源
- **数据库**: MySQL、PostgreSQL
- **安全认证**: JWT (JSON Web Token)
- **API 文档**: Knife4j + OpenAPI 3
- **加密算法**: AES、MD5、BCrypt
- **文件处理**: Excel 导入导出 (FastExcel、Apache POI)
- **数据传输**: SFTP 文件传输
- **实时通信**: WebSocket
- **SQL 解析**: JSqlParser

## 主要功能模块

### 1. 多数据源支持
- 支持 MySQL 和 PostgreSQL 双数据源
- 集成 `dynamic-datasource-spring-boot-starter` 实现动态数据源切换
- 支持主从数据库配置

### 2. 安全认证
- JWT Token 拦截器实现用户身份验证
- 自定义注解 `@Log` 实现操作日志记录
- AOP 切面编程实现统一的日志处理

### 3. API 文档
- 集成 Knife4j 实现 Swagger API 文档自动生成
- 支持在线 API 测试和文档浏览

### 4. Excel 处理
- 使用 FastExcel 和 Apache POI 实现 Excel 文件快速读写
- 支持 Excel 数据校验和监听器模式

### 5. 数据库工具
- SQL 解析工具，支持从 SQL 语句中提取表名
- 自定义 MyBatis 工具类

### 6. 加密解密
- AES 加密算法实现数据加密
- MD5 散列算法
- BCrypt 密码加密

### 7. 文件传输
- SFTP 工具类实现安全文件传输
- 支持远程服务器文件上传下载

### 8. WebSocket 实时通信
- 集成 Spring WebSocket 实现实时双向通信
- 自定义 WebSocket 处理器

## 项目结构

```
src/main/java/com/example/boot/
├── annotation/           # 注解定义
├── aspect/              # AOP 切面
├── bean/                # 实体类
├── config/              # 配置类
├── controller/          # 控制器层
├── exception/           # 异常处理
├── fliter/              # 过滤器
├── interceptor/         # 拦截器
├── mapper/              # MyBatis 映射接口
├── multdatasources/     # 多数据源配置
├── processor/           # 处理器
├── sevice/              # 服务层
├── util/                # 工具类
│   ├── excle/           # Excel 处理工具
│   ├── md5/             # 加密工具
│   ├── sql/             # SQL 工具
│   └── 添加配置/        # 配置刷新工具
└── BootApplication.java # 项目启动类
```

## 配置说明

### 端口与上下文
- 服务端口: 8081
- 上下文路径: /boot

### 数据源配置
项目配置了多个数据源：
- `mysql`: MySQL 数据库连接
- `postgres`: PostgreSQL 数据库连接
- `master`: 主数据库
- `slave0/slave1`: 从数据库

### API 文档访问地址
- Knife4j 地址: http://127.0.0.1:8081/boot/doc.html
- Swagger 地址: http://127.0.0.1:8081/boot/swagger-ui.html

## 开发环境要求

- Java 11+
- Maven 3.6+
- MySQL 5.7+ 或 PostgreSQL 9.6+
- Node.js (可选，用于前端开发)

## 启动说明

1. 确保数据库服务已启动并配置正确
2. 执行 Maven 编译命令：
   ```
   mvn clean install
   ```
3. 运行启动类 `BootApplication.java`
4. 访问应用: http://127.0.0.1:8081/boot/

## 特色功能

- **动态数据源切换**: 支持运行时根据需求切换不同数据库
- **线程安全的配置刷新**: 支持配置文件热更新
- **异步处理支持**: 配置了异步任务执行器
- **跨域处理**: 支持前后端分离架构的跨域请求
- **全局异常处理**: 统一的异常捕获和处理机制

## 项目特点

本项目是一个功能丰富的 Spring Boot 应用模板，适用于企业级应用开发。通过集成多种常用组件，开发者可以快速构建具备完整功能的 Web 应用。
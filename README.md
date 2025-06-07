# Kalista - 基于Spring Boot的后端基础框架

> **技术栈要求**
> - Spring Boot 3.x
> - JDK 21+

## 一、模块架构

### 1.1. 核心模块

| 模块名称           | 功能描述                                                                                        |
|----------------|---------------------------------------------------------------------------------------------|
| `kalista-base` | 基础公共模块，定义通用对象和接口规范                                                                          |
| `kalista-core` | 核心功能模块，提供：<br>• 序列化/反序列化<br>• 多级缓存体系<br>• 国际化支持<br>• AOP增强<br>• 反射工具集<br>• 加密算法库<br>• 通用工具类 |

### 1.2. 分布式能力

| 模块名称           | 功能特性                                                               |
|----------------|--------------------------------------------------------------------|
| `kalista-grpc` | gRPC服务支持：<br>• 基于gRPC-Java框架<br>• 支持Protobuf标准定义<br>• 扩展Java接口定义方式 |
| `kalista-lock` | 分布式锁实现：<br>• 基于Redisson的分布式锁解决方案                                   |

### 1.3. 数据持久化

| 模块名称              | 技术实现                                            |
|-------------------|-------------------------------------------------|
| `kalista-mybatis` | ORM增强：<br>• 基于MyBatis-Plus深度定制<br>• 开箱即用的CRUD操作 |
| `kalista-r2dbc`   | 响应式数据访问：<br>• Spring R2DBC增强封装                  |

### 1.4. 系统增强

| 模块名称              | 核心能力                                  |
|-------------------|---------------------------------------|
| `kalista-monitor` | 系统可观测性：<br>• 指标监控<br>• 告警机制<br>• 追踪能力 |
| `kalista-webflux` | 响应式Web支持：<br>• 基于Spring WebFlux深度定制   |

## 二、核心指南

### 2.1 BenchMarker - 全链路性能追踪工具

## 功能概述

BenchMarker 是一款用于全链路耗时分析的工具，支持追踪多种类型的请求处理过程：

- HTTP 请求
- gRPC 调用
- 消息消费
- 任务调度
- 其他自定义处理流程

## 核心特性

### 2.1.1 自动埋点

系统已在关键处理环节预置埋点，只需引入核心依赖即可自动完成：

```xml
<!-- 自动生效，无需配置 -->
<dependency>
    <groupId>com.reopenai.infras</groupId>
    <artifactId>kalista-core</artifactId>
</dependency>
```

### 2.1.2 手动埋点

获取实例后可进行自定义打点：

```java
BenchMarker.mark("startProcess");  // 开始标记
// ...业务逻辑...
BenchMarker.

mark("endProcess");    // 结束标记
```

重要规范：

- 打点必须成对出现
- 每次`mark()`只计算与前一个标记点的间隔耗时
- 标记名称应具有业务语义（如`"parseRequest"`）

### 2.1.3 响应式环境（WebFlux/R2DBC）

⚠️ 禁止以下操作：

```java
// 错误用法（响应式环境下无效）
// 请勿尝试通过任何ThreadLocal获取BenchMarker实例
BenchMarker.current();
```

✅正确的操作：
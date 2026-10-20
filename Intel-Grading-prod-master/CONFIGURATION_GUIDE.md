# 后端配置指南

本项目需要配置以下服务和参数才能正常运行：

## 必需的外部服务

### 1. MySQL 数据库
- 版本：8.0.33
- 需要创建数据库并导入SQL脚本
- 配置位置：`src/main/resources/application.yml`

配置项：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database_name?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_mysql_username
    password: your_mysql_password
```

### 2. Redis
- 版本：2.1.7.RELEASE
- 用于缓存和token刷新
- 配置位置：`src/main/resources/application.yml`

配置项：
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
```

### 3. RabbitMQ
- 版本：2.7.3
- 用于消息队列和异步处理
- 配置位置：`src/main/resources/application.yml`

配置项：
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: your_rabbitmq_username
    password: your_rabbitmq_password
```

## 可选的外部服务

### 4. 阿里云OSS
- 用于文件存储
- 配置位置：`src/main/resources/application.yml`

配置项：
```yaml
aliyun:
  oss:
    file:
      endpoint: your_oss_endpoint
      keyid: your_access_key_id
      keysecret: your_access_key_secret
      bucketname: your_bucket_name
```

### 5. 云端API接口
- 智能阅卷接口（OCR + 文心大模型）
- 多样评语接口
- 智能辅助接口
- 配置位置：`src/main/resources/application.yml`

配置项：
```yaml
api:
  grade: # 智能阅卷云端接口
    url: your_api_url
    request:
      header: your_request_header
      secret: your_secret
  comment: # 多样评语云端接口
    url: your_api_url
    request:
      header: your_request_header
      secret: your_secret
  extend: # 智能辅助云端接口
    url: your_api_url
    request:
      header: your_request_header
      secret: your_secret
```

## 启动步骤

1. 确保所有外部服务已安装并运行
2. 修改 `application.yml` 中的配置项
3. 导入数据库脚本（如果有）
4. 运行 Maven 构建：`mvn clean install`
5. 运行主类启动项目

## 默认配置

- 服务端口：8080
- 上下文路径：/grading
- 访问地址：http://localhost:8080/grading

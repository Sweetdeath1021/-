# 项目设置完成总结

## 已完成的任务

### 后端 (Intel-Grading-prod-master)
✅ 检查系统环境：Java 17, Maven 3.8.8
✅ 修复依赖兼容性问题：
  - Lombok: 1.18.10 → 1.18.30 (支持Java 17)
  - MapStruct: 1.3.0.Final → 1.5.5.Final
  - 修复 spring-boot-devtools 的 scope 配置
  - 配置注解处理器路径
  - 修复 ConverterUtil 注入问题
✅ Maven 构建成功
✅ 创建配置指南 (CONFIGURATION_GUIDE.md)

### 前端 (smart-master)
✅ 检查系统环境：Node.js v24.14.1, Yarn 1.22.22
✅ 安装依赖成功
✅ 更新代理配置指向本地后端
✅ 生产构建成功

## 启动前需要配置

### 后端配置文件
编辑 `Intel-Grading-prod-master/src/main/resources/application.yml`：

1. **MySQL 数据库** (必需)
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/your_database_name?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
       username: your_username
       password: your_password
   ```

2. **Redis** (必需)
   ```yaml
   spring:
     redis:
       host: localhost
       port: 6379
       password: your_password
   ```

3. **RabbitMQ** (必需)
   ```yaml
   spring:
     rabbitmq:
       host: localhost
       port: 5672
       username: your_username
       password: your_password
   ```

4. **阿里云OSS** (可选)
   ```yaml
   aliyun:
     oss:
       file:
         endpoint: your_endpoint
         keyid: your_keyid
         keysecret: your_keysecret
         bucketname: your_bucketname
   ```

5. **云端API接口** (可选 - 智能阅卷功能)
   ```yaml
   api:
     grade:
       url: your_api_url
       request:
         header: your_header
         secret: your_secret
     comment:
       url: your_api_url
       request:
         header: your_header
         secret: your_secret
     extend:
       url: your_api_url
       request:
         header: your_header
         secret: your_secret
   ```

## 启动步骤

### 1. 启动外部服务
- MySQL 数据库
- Redis
- RabbitMQ

### 2. 导入数据库
如果有 SQL 脚本，导入到 MySQL 数据库中

### 3. 启动后端
```bash
cd Intel-Grading-prod-master
mvn spring-boot:run
```
或运行主类：`org.grade.GradeApplication`

后端将在 http://localhost:8080/grading 启动

### 4. 启动前端
```bash
cd smart-master
yarn serve
```
前端将在 http://localhost:8080 启动（会自动打开浏览器）

## 端口说明
- 后端：8080 (上下文路径: /grading)
- 前端：8080 (开发服务器)

## 注意事项
1. 确保所有必需的外部服务（MySQL, Redis, RabbitMQ）已安装并运行
2. 如果不需要云端API功能，可以暂时不配置 api 部分
3. 前端已配置代理到本地后端，无需修改 API 调用代码

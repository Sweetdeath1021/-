# 本地服务安装指南

启动后端需要以下服务：

## 必需服务

### 1. MySQL 8.0

#### Windows安装
1. 下载MySQL Installer: https://dev.mysql.com/downloads/installer/
2. 选择"mysql-installer-community-8.0.xx.0.msi"
3. 安装时选择：
   - Developer Default（推荐）
   - 设置root密码（记住这个密码）
   - 配置端口：3306（默认）

#### 验证安装
```bash
mysql --version
```

#### 创建数据库
```bash
mysql -u root -p
# 输入密码后执行：
CREATE DATABASE grading CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

### 2. Redis

#### Windows安装
Redis官方不支持Windows，使用以下替代方案：

**方案A：使用Memurai（推荐）**
1. 下载: https://www.memurai.com/get-memurai
2. 安装后默认端口6379，无密码

**方案B：使用Docker**
```bash
docker run -d -p 6379:6379 redis:latest
```

**方案C：使用WSL2安装Redis**
```bash
# 在WSL2中
sudo apt update
sudo apt install redis-server
sudo service redis start
```

#### 验证安装
```bash
redis-cli ping
# 应返回 PONG
```

---

### 3. RabbitMQ

#### Windows安装
1. 安装Erlang（RabbitMQ依赖）: https://www.erlang.org/downloads
   - 下载OTP 25.x版本
2. 安装RabbitMQ: https://www.rabbitmq.com/download.html
   - 下载Windows版本
3. 启用管理插件：
```bash
cd "C:\Program Files\RabbitMQ Server\rabbitmq_server-3.12.x\sbin"
rabbitmq-plugins.bat enable rabbitmq_management
```

#### 默认配置
- 用户名: guest
- 密码: guest
- 端口: 5672（AMQP）、15672（管理界面）
- 管理界面: http://localhost:15672

#### 验证安装
访问 http://localhost:15672，使用guest/guest登录

---

## 配置后端

安装完成后，编辑 `Intel-Grading-prod-master/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/grading?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_mysql_password  # 替换为你的MySQL密码
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  redis:
    host: localhost
    port: 6379
    password:  # 如果Redis有密码则填写，否则留空
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: 100
    database: 0
```

---

## 快速启动方案（使用Docker）

如果已安装Docker，可以一键启动所有服务：

```bash
# 启动MySQL
docker run -d --name mysql-grading -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=grading -p 3306:3306 mysql:8.0

# 启动Redis
docker run -d --name redis-grading -p 6379:6379 redis:latest

# 启动RabbitMQ
docker run -d --name rabbitmq-grading -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Docker配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/grading?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  redis:
    host: localhost
    port: 6379
    password:
```

---

## 启动后端

配置完成后：
```bash
cd Intel-Grading-prod-master
mvn spring-boot:run
```

后端将在 http://localhost:8080/grading 启动

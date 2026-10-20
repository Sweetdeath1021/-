#!/usr/bin/env bash
# 智能阅卷平台 Linux（Ubuntu 22.04）一键环境安装 + 启动脚本
# 用法：bash setup_linux.sh        （在 pw 目录下执行）
set -e
ROOT="$(cd "$(dirname "$0")" && pwd)"
BACK="$ROOT/Intel-Grading-prod-master"
FRONT="$ROOT/smart-master"
DB_PASS=123456

echo "== 1. 系统包：JDK17 / Maven / zbar / python 别名 / Node 20"
sudo apt-get update -qq
sudo apt-get install -y -qq openjdk-17-jdk maven libzbar0 python3-pip python-is-python3 curl
if ! command -v node >/dev/null; then
  curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
  sudo apt-get install -y -qq nodejs
fi

echo "== 2. Python 脚本依赖（ReadQr.py / ImageSeg.py）"
pip3 install --user requests pillow numpy opencv-python-headless pyzbar

echo "== 3. Maven 镜像（避免 Maven Central 429 / 国内慢）"
mkdir -p ~/.m2
[ -f ~/.m2/settings.xml ] || cat > ~/.m2/settings.xml <<'XML'
<settings><mirrors><mirror>
  <id>aliyun</id><mirrorOf>central</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror></mirrors></settings>
XML

echo "== 4. MySQL 8 / Redis / RabbitMQ（Docker）"
REG=${REG:-mirror.gcr.io/library}   # Docker Hub 限流时用镜像；国内可改成自己的加速器
# 端口只绑定 127.0.0.1，避免本地数据库 / Redis 暴露到网络
docker start mysql-grading 2>/dev/null || docker run -d --name mysql-grading -p 127.0.0.1:3306:3306 \
  -e MYSQL_ROOT_PASSWORD=$DB_PASS $REG/mysql:8.0 \
  --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
docker start redis-grading 2>/dev/null || docker run -d --name redis-grading -p 127.0.0.1:6379:6379 $REG/redis:7
docker start rabbitmq-grading 2>/dev/null || docker run -d --name rabbitmq-grading \
  -p 127.0.0.1:5672:5672 -p 127.0.0.1:15672:15672 $REG/rabbitmq:3-management

echo "== 5. 导入数据库 + 结构补丁"
MYSQL="docker exec -i mysql-grading mysql -uroot -p$DB_PASS --default-character-set=utf8mb4"
until docker exec mysql-grading mysqladmin -uroot -p$DB_PASS ping 2>/dev/null | grep -q alive; do sleep 3; done
until $MYSQL -e "select 1" >/dev/null 2>&1; do sleep 3; done
$MYSQL -e "CREATE DATABASE IF NOT EXISTS intel_grading CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
if ! $MYSQL -N intel_grading -e "show tables like 'grd_user'" 2>/dev/null | grep -q grd_user; then
  $MYSQL intel_grading < "$BACK/src/main/resources/db/grade.sql"
fi
# 补丁可重复执行，每次都跑一遍以修复已有的旧库
$MYSQL intel_grading < "$BACK/src/main/resources/db/patch_schema.sql"

echo "== 6. 前端依赖"
(cd "$FRONT" && npm ci)

cat <<MSG

环境就绪。分别在两个终端启动：

[后端] http://localhost:8080/grading
cd "$BACK" && mvn spring-boot:run -Dspring-boot.run.profiles=local
#   或者: mvn -DskipTests package && java -jar target/Intel-Grading-1.0-SNAPSHOT.jar --spring.profiles.active=local

[前端] http://localhost:8081  （/grading 自动代理到后端 8080）
cd "$FRONT" && npm run serve
MSG

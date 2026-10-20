# 智能阅卷平台：在 Linux（Ubuntu 22.04）上运行需要的环境

已经在 Devin 机器上实测：后端能启动，前端能编译，登录→token→profile 整条链路走通，两个 Python 脚本也都跑通了。

## 1. 运行时 / 系统包
| 组件 | 版本 | 用途 | 安装 |
|---|---|---|---|
| JDK | 17（pom 编译目标是 8，用 17 运行也正常） | 后端 | `apt install openjdk-17-jdk` |
| Maven | 3.6+ | 构建 / 启动后端 | `apt install maven` |
| Node.js | 20 LTS（npm 10） | 前端 Vue CLI 5 | NodeSource 源 |
| Python | 3.8+，需要能直接用 `python` 命令 | 后端会调用 `python` 运行 ReadQr.py / ImageSeg.py | `apt install python-is-python3` |
| libzbar0 | - | pyzbar 识别条形码要用的系统库 | `apt install libzbar0` |
| Docker | - | 运行 MySQL / Redis / RabbitMQ | 机器上已有 |

## 2. 中间件（必需，缺任何一个后端都启动不了）
| 服务 | 版本 | 端口 | 说明 |
|---|---|---|---|
| MySQL | 8.0 | 3306 | 库名 `intel_grading`。先导入 `db/grade.sql`，再导入 `db/patch_schema.sql` |
| Redis | 6/7 | 6379 | 存登录 token |
| RabbitMQ | 3.x | 5672 / 15672 | 智能阅卷的异步队列，默认账号 guest/guest |

## 3. 依赖包
- 后端（Maven）：Spring Boot 2.7.3、MyBatis-Plus 3.4.2、Spring Security + jjwt、AMQP、WebSocket、aliyun-sdk-oss、hutool、zip4j / unrar / commons-compress、mapstruct、lombok
- 前端（npm）：vue 2.7、element-ui、axios、echarts、tui-image-editor、vue-router 3、vuex 3、sass。装法：`npm ci`
- Python：`pip install requests pillow numpy opencv-python-headless pyzbar`

## 4. 外部云服务（可选；不配置的话下面这些功能用不了）
- 阿里云 OSS（`aliyun.oss.file.*`）：注册时上传头像、上传答题卡/试卷压缩包、划分区域后存图都要用它。**没配 OSS，注册接口会报 server.error**，只能直接往库里插测试账号。
- 云端 AI 接口（`api.grade/comment/extend.*`）：智能评分、生成评语、知识延展。

## 5. 已修复的问题（本 PR）
1. **grade.sql 与代码实体不一致**（`grd_user.user_phone`→`user_account`；缺 `grd_region.region_content`、`grd_test_paper.paper_point`；缺 `grd_comment`、`grd_question` 表），不修登录直接 SQL 报错 → 新增 `db/patch_schema.sql`。
2. **Python 脚本路径写死为 Windows `D:\...`** → `PythonConstant` 改为默认 `src/main/resources/script`，可用 `-Dpython.script.dir` 或环境变量 `PYTHON_SCRIPT_DIR` 覆盖。
3. **前后端都默认 8080** → 前端开发服务器改为 8081。
4. **`mvn package` 产出的 jar 不可执行** → pom 增加 `repackage`，现在可以 `java -jar` 启动。
5. 新增 `application-local.yml`（本地 Docker 中间件配置），启动时加 `local` profile 即可，无需改 `application.yml`。

## 6. 仍需注意
- 阿里云 OSS 未配置时：注册（上传头像）、上传答题卡/试卷、划分区域不可用；可直接往库里插测试用户（密码为 BCrypt `$2a$` 哈希，并在 `grd_user_role` 中加角色）。
- Windows 上 `python` 命令需指向装好依赖的 Python。
- Maven Central / Docker Hub 可能限流或国内很慢：脚本默认配阿里云 Maven 镜像；Docker 可通过 `REG=...` 换镜像源。

## 7. 一键脚本
在仓库根目录执行 `bash setup_linux.sh`：安装上面所有依赖、启动 3 个容器、导入数据库并打补丁、安装前端依赖，最后打印前后端的启动命令。

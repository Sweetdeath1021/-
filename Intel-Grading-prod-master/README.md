# 智能阅卷平台

> 作者：**赛鸭脖团队**
>
> 前端开源链接：https://gitee.com/bchena/smart
>
> 项目演示视频：[赛博智评——智能阅卷平台](https://www.bilibili.com/video/BV1Lf42197CS/?buvid=XY25E7A20959E591B517FB95927E9B117C4C6&from_spmid=dt.space-dt.video.0&is_story_h5=false&mid=oBjYTFEP9aPMi2dJCdx2tH8FTQ%2FSZMtL1rElX6M3iMo%3D&p=1&plat_id=116&share_from=ugc&share_medium=android&share_plat=android&share_session_id=5fc4760d-73fd-4feb-8741-8a961681e8c7&share_source=QQ&share_tag=s_i&spmid=united.player-video-detail.0.0&timestamp=1716875764&unique_k=PjxrtOQ&up_id=3546691382807435)
> 
> 阅卷辅助平台应用：
>
> * [轻量版](https://aistudio.baidu.com/application/detail/24561)（由于云服算力限制原因，轻量版使用超轻量化OCR模型）
> * [标准版](https://aistudio.baidu.com/application/detail/24334)（标准版使用高精度模型，限时12:00~17:00开放）

## 📃项目介绍

本项目是基于 Spring Security + MQ + Redis 结合深度学习技术实现**智能化评阅**的阅卷平台。系统以智能评阅为核心，提供题组管理、任务分配、智能评分、个性评语、学情分析和知识延展等功能，实现评阅全流程智能化的解决方案。

主要包含三大角色：阅卷科目组长、阅卷老师、学生。

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404062243364.png)

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202405252340216.png)

## 💡亮点简介

### 前端

1. 基于Vue3 + Element-ui组件库，自主实现了用户登录、划分题目、任务分配、在线阅卷和学情分析等页面。
2. 使用Vue-CLI脚手架初始化项目，并自行开发了全局页面布局和通用前端项目模板，便于后续复用。
3. 为实现**全局导航**，基于Vue Router的路由配置文件，灵活地变换路由。
4. 为满足阅卷的多样化需求，通过集成tui-image-editor**图片编辑器**，提供了试卷评阅的丰富组件。
5. 引入echarts绘制多样化的图表，将学情数据进行更直观的展现。

### 后端

1. 通过重写 **Spring Security** 的过滤器链，实现了token令牌解析、用户名密码校验、安全异常处理等功能。此外，通过@ControllerAdvice注解实现了**全局异常处理器**，将异常信息通过资源包进行统一国际化处理。
2. 为满足阅卷场景的多样化用户属性需求，利用**静态工厂模式 + 模板方法模式**灵活选用对应用户的注册方法，注册对应角色的用户。
3. 利用RabbitMQ**异步通信**的特性，通过将阅卷任务存入消息队列，由阅卷服务使用 Hutool 工具类调用**云端接口**进行消费，单题响应时间在**2~3秒**，提高阅卷老师的评阅效率。
4. 利用RabbitMQ**流量削峰**的特性，在高峰期将阅卷请求暂存起来，按照模型的处理能力逐步释放请求，有效防止模型过载或崩溃
5. 考虑到上传题组的响应时间较长，基于**I/O密集型线程池**实现上传请求的并发执行和异步化，提交任务后即可响应前端，相比单线程传输，效率提升了53.6%。
6. 使用zip4j、unrar和commons-compress工具包，支持将zip、7z、rar等多种格式的答题卡压缩包解压，通过阿里云OSS服务存储文件。

### 算法

1. 为提升模型训练的质量，通过PPOCRLabel对12w数据集进行标注，再进行数据清洗、数据增强、划分训练集和测试集。这样在模型训练的时候提高数据集质量，使模型更好的**收敛**。此外添加了中科院将近33w高质量数据集，以提升模型训练质量。

2. 选择PPOCRv4-rec-server文本识别训练模型进行训练，选用配置文件hgent-fp32.yml。这个尽量选择更大的内存（批次大小为128或者256）和精度训练（fp32精度）这使得模型训练速度加快、各项指标提升

3. 选择优化器Adam和动态学习率CosineAnnealingIR （前期降低学习率，后期增加学习率，可以使模型更好的收敛、增加模型的泛化能力），并使用了l2正则化，防止了模型过拟合。

4. 选用SVTR算法和PP-HGnet-small 骨干结构，SVTR系列的预测精度和正确率效果达到了**82.5%**
5. 预测结果predict.py中加入半角、全角符号替换，进行字典错字替换来**提高结果正确率**。本项目中使用将近7000个错字替换，使得预测准确率至少提升2%

## 💾功能概要

### 阅卷科目组长

1. 上传题组：支持上传多格式的答题卡压缩包，文件格式支持：zip、7z、tar、rar、gz等。
2. 预览样卷：支持点击图片，开启图片放大预览功能进行快速浏览。
3. 划分题目：支持自定义阅卷任务，提供**图片编辑器**灵活设定阅卷区域。
4. 任务分配：支持通过全选按钮或框选功能**快速选择**阅卷老师；支持通过选择任务量规则**一键分配**阅卷任务。
5. 任务跟踪：可通过题组任务列表选择对应任务，分页表格可直观展示各阅卷老师任务完成进度。
6. 学情分析：提供大量图表，从考情总览、各班平均分、试题得分率等多个维度分析本科目考试结果。

### 阅卷老师

1. 线上阅卷：已阅的答题卡将高亮显示，自动跳转到尚未评阅的答题卡，可通过各**图片组件**对答题卡进行打分和标注。
2. 智能评阅：支持将学生答题卡和标准答案提交到**云端接口**，接口利用**PaddleOCR**将图片数据扫描为文本，**文心大模型**将结合评分标准自动为题目打分。
3. 批量评阅：阅卷老师可一键提交所有阅卷任务，实现便捷的智能化评阅。
4. 个性评语：支持**评语标签库**快速选择学生特点，或**自定义标签**上传给文心大模型，实现多样化评语。
5. 学情分析：提供大量图表，从成绩分布、学业等级、试题得分率等多个维度分析班级考试结果。

### 学生

1. 试卷查看：可查看历次考试及对应试卷得分情况，获取班级排名、教师评语等信息。
2. 知识延展：文心大模型可根据考生学情和教师评语，提供**更贴近**学生需求的知识延展和学科建议。

## 💻技术选型

### 前端

| 技术                       | 作用                                                         | 版本    |
| -------------------------- | ------------------------------------------------------------ | ------- |
| @toast-ui/vue-image-editor | 图片处理组件                                                 | 3.15.2  |
| axios                      | 发送ajax请求给后端进行交互                                   | 1.6.5   |
| jQuery                     | 提供了强大的功能函数                                         | 3.7.1   |
| core-js                    | 兼容性更强                                                   | 3.8.3   |
| default-passive-events     | Chrome增加了新的事件捕获机制Passive Event<br /> Listeners（被动事件监听器），让页面滑动更加流畅 | 2.0.0   |
| echarts                    | 支持多种类型和效果的数据可视化图表                           | 5.5.0   |
| gasp                       | 动画库                                                       | 3.12.4  |
| localforage                | 本地存储较大文件                                             | 1.10.0  |
| sass                       | css3的拓展，减少css重复的代码                                | 1.69.7  |
| vue-router                 | vue官方的路由管理器，使单页面应用实现变得更容易              | 3.0.1   |
| vuex                       | 状态管理                                                     | 3.0.1   |
| element-ui                 | 模块组件库，绘制界面                                         | 2.15.14 |
| vue                        | 提供前端交互                                                 | 2.7.16  |

### 后端

| 技术             | 作用                | 版本          |
| ---------------- | ------------------- | ------------- |
| JDK              | Java开发工具包      | 1.8           |
| SpringBoot       | 应用开发框架        | 2.7.3         |
| SpringSecurity   | Spring安全框架      | 2.7.3         |
| Mysql            | 提供后端数据库      | 8.0.33        |
| Redis            | 刷新令牌、缓存数据  | 2.1.7.RELEASE |
| Lombok           | 快速生成实体类方法  | 1.18.20       |
| MybatisPlus      | ORM框架，连接数据库 | 3.4.2         |
| Hutool           | 提供实用性工具类    | 5.7.17        |
| Jackson          | 提供JSON解析工具类  | 2.11.4        |
| Junit            | 提供单元测试功能    | 3.8.1         |
| jwt              | token工具包         | 0.9.0         |
| aliyun-sdk-oss   | 阿里云对象存储      | 2.8.3         |
| commons-compress | 支持多种解压方式    | 1.21          |
| RabbitMQ         | 提供消息队列服务    | 2.7.3         |

### 应用

| 技术       | 作用                                                         | 版本  |
| ---------- | ------------------------------------------------------------ | ----- |
| Gradio     | Gradio是一个用于快速创建可分享的机器学习模型的Web应用界面的库。 | 3.5.0 |
| FastDeploy | FastDeploy是一个用于部署机器学习和深度学习模型的工具。       | 1.0.7 |
| ErnieBot   | ErnieBot是一个预训练的自然语言处理模型，用于生成文本。       | 0.5.3 |

### 算法

| 技术                            | 作用                                                         | 版本                                                         |
| ------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| PPOCRLabel                      | 用于数据标注、文检测和识别，便于模型训练数据集               | PPOCRLabel  v2（版本）                                       |
| 卷积神经网络（CNN）             | 特征提取：CNN能够自动学习图像中的特征，有卷积层、池化层、全连接层等结构 | 模型的底层架构（详细逻辑结构如下表）                         |
| 数据增强                        | 增加数据多样性、实现数据量的扩大、减轻过拟合、提高模型的泛化能力。 | 包括了原始数据进行旋转、缩放、平移、翻转、裁剪等操作         |
| 文本识别算法（SVTR_HGNet）      | 高效识别速度、准确识别文本、适用多种场景、扩展性强           | 包括了下面的特征提取器（CNN）、Neck架构、Head架构、损失函数、SVTR模块 |
| 主干网络（PPHGNet_small）       | 进行模型创建、模型实例化、预训练模型加载、特征提取。         | 通常使用卷积神经网络（CNN）                                  |
| 多头部结构（MultiHead）         | 进行多任务处理、增加模型灵活性、共享底层特征提取器（卷积神经网络CNN）、方便模型训练和部署 | 包括CTCHead和NRTRHead，主要用到前向传播方法                  |
| 多损失函数（MultiLoss）         | 进行多任务学习、通过加权损失函数来加快模型训练、加速收敛、提高效率；多正则化、防止过拟合。 | 包括CTCLoss和NRTRLoss、这两种损失函数通过前向传播进行加权组合 |
| 模型后处理方法（PostProcess）   | 将模型输出的预测结果转换为文本标签，并进行解码和后处理，以便进一步评估模型的性能或生成最终的识别结果。 | 主要是CTCLabelDecode解码和整合。                             |
| 动态学习率（CosineAnnealingLR） | 通过动态调节学习率来增加模型对数据的适应能力、加快模型训练和收敛。 | 使用cosnie余弦退火周期、前期降低学习率，后期升高学习率。     |

## 🔨系统架构

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404062147185.svg)

## 📂功能模块

本系统分为三个角色：阅卷科目组长、学生和阅卷老师，各角色功能如下图：

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202405262050054.svg)

## 🚀业务流程

### 业务总体流程

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202405262047199.svg)

**阅卷组长：**

* 题组录入：上传试题和答题卡

* 划分题目：自主标记阅卷区域

* 分配任务：一键分配阅卷任务

**阅卷老师：**

* 智能评阅：AI批量评阅、评阅内容再编辑

* 多样评语：定义学生评价标签，生成个性化评语

**学生：**

* 查看结果：查阅评分细节、跟踪成绩情况

* 智能辅导：AI提供指导、进行知识回顾与拓展

**学情分析：**阅卷组长和老师可查看成绩分布、题目质量、试题得分率等可视化图表，多维度跟踪团体和个体的学业情况。

### 系统总体流程

当客户端发送请求时，首先由过滤器链（FilterChain）对于除用户登录注册外的请求进行过滤。对于用户进行登录操作时，由JWT进行token令牌签发，转发到客户端，当客户端后续访问服务端时需要携带token令牌，由JWT进行身份校验成功后方可通过请求。请求通过后，经由controller层进行请求转发并协调service层处理业务逻辑并返回数据到客户端，当请求需要使用模型时，service层将调用云端接口完成业务逻辑。Service层处理业务逻辑时，需要与数据层交互以从数据库中存储或取出数据，从而完成整个业务逻辑的流程。

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172329762.svg)

### 请求处理流程

当服务端接收到客户端的请求时，首先会判断该请求是否适合由Python来处理。如果Python不是必需的，服务端将直接与数据层交互，获取所需数据，并由服务层的业务逻辑进行相应处理。若请求确定需要Python参与，服务端会进一步判断是否需要调用Python模型。若无需调用模型，服务端会将请求数据设置为命令行参数，并调用本地的Python脚本来处理该请求。而若需调用Python模型，服务端则利用Hutool的HttpRequest类，通过封装formData数据发送请求，调用云端接口进行处理。在整个处理过程中，无论是本地处理还是云端处理，一旦处理成功，服务端将直接返回数据给客户端；若处理失败，则返回相应的错误信息。

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172330888.svg)

### 数据访问流程

当服务端需要检索数据时，服务层会与数据层进行交互以获取所需信息。在服务层提交查询请求后，系统会首先判断该请求是否为图片查询。若确定为图片查询请求，系统将转向阿里云OSS进行图片检索。若成功找到图片，则直接返回图片数据；若未找到，则返回相应的错误码。若查询请求非图片相关，系统首先会尝试从Redis缓存中检索数据。若缓存命中，则直接返回数据；若缓存未命中，则需进一步查询数据库。若数据库中仍无所需数据，则返回错误信息；若数据库中存在数据，则将该数据写入Redis缓存，并返回给服务层。

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172330991.svg)

## 📈界面展示

### 登录注册界面

**登录界面：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404091136816.png)

**注册界面：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404091137329.png)

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172316124.png)

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172316513.png)

**找回密码：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172316818.png)

### 个人信息界面

**基本资料：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404091138365.png)

**修改密码：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172316325.png)

### 题组管理界面

**上传题组：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404051650374.png)

**查看样卷：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404091140140.png)

### 划分题目界面

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404062246044.png)

### 任务分配界面

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172320872.png)

### 阅卷详情界面

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172321502.png)

### 学情分析界面

**考情总览：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404062249632.png)

**各班平均分：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172322395.png)

**成绩分布情况：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172323230.png)

**试卷质量：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172323467.png)

### 智能评阅界面

**题目打分：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172324744.png)

**上传答案：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172324470.png)

**AI评阅：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172324634.png)

**一键提交：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172325185.png)

### 多样评语界面

**选择标签：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172325515.jpg)

**评语参数：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172339850.png)

**生成评语：**

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172325947.jpg)

### 考试信息界面

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172327588.png)

### 知识延展界面

![](https://modox.oss-cn-hangzhou.aliyuncs.com/img/202404172327990.png)
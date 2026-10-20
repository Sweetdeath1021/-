# 智能阅卷平台 · 前端（smart-master）学习与面试准备

> 本文全部内容以 `smart-master/` 的实际代码为依据。
> 写着 **【已实现】** 的，代码里确实有，面试时可以放心讲；写着 **【改进方向】** 的，代码里**没有**，只能作为“如果继续优化我会怎么做”来回答。**千万别把改进方向说成已经做了**，面试官一追问细节就会露馅。
> 行号以本仓库当前版本为准，格式为 `文件:行号`。没写行号的，在文件里按函数名搜索即可。

---

## 0. 怎么用这份文档（建议学习顺序）

1. 先把项目跑起来，用三种角色（组长 roleId=1 / 老师 roleId=2 / 学生）各登录一次，把每个页面点一遍。
2. 按下面的顺序读代码，每读完一个文件，能用自己的话复述“这个页面做什么、调了哪些接口、数据怎么流动”：
   `package.json` → `vue.config.js` → `src/main.js` → `src/api/baseUrl.js` → `src/router/index.js` → `views/login.vue` → `views/home.vue` + `components/navcon.vue` → `views/examination/upload.vue` → `views/task/distribute.vue` → `views/task/list.vue` → `views/grading.vue` → `views/knowledgeExtension/index.vue` → `views/multipleRemark/index.vue` → `views/studyAnalysis/index.vue` → `components/Pagination.vue` → `views/personal/userAvatar.vue`
3. 把第 4 节的「难点与解决方案」每一条都对着代码看懂，能脱稿讲。
4. 最后背熟第 7 节的问答，再对着镜子讲一遍第 8 节的项目介绍。

---

## 1. 一句话介绍项目

> 智能阅卷平台是一个面向学校的 Web 系统：**组长**上传答题卡压缩包和样卷，在样卷上框选题目区域并把阅卷任务分给老师；**老师**在线逐张阅卷，可以手动打分，也可以调用 AI 评分、生成个性化评语；**学生**查看成绩、学情分析，并通过 AI 对错题做知识延展。我负责前端，用 Vue 2 + Element UI 实现了全部页面。

---

## 2. 技术栈（以及为什么用它）

| 技术 | 版本 | 在项目里干什么 | 证据 |
|---|---|---|---|
| Vue | 2.7.16 | 框架，Options API 写法 | `package.json` |
| Vue CLI | 5 | 脚手架、开发服务器、代理、打包 | `vue.config.js` |
| Vue Router | 3 | 页面路由，全部路由懒加载 | `src/router/index.js` |
| Element UI | 2.15 | 表单、表格、上传、弹窗、分页、消息提示 | `src/main.js` 全量 `Vue.use(ElementUI)` |
| jQuery | 3.7 | **大部分接口请求用 `this.$.ajax`**，也有少量 DOM 操作 | `src/main.js` 把 `$` 挂到 `Vue.prototype.$` |
| Axios | 1.6 | 挂在 `Vue.prototype.$http`，但业务里基本没用 | `src/main.js` |
| ECharts | 5.5 | 学情分析的饼图、柱状图、折线图，**按需引入** | `src/main.js:61` `echarts.use([...])` |
| Toast UI Image Editor | 3.15 | 在样卷上框选题目区域、阅卷时在图片上写分数 | `views/task/distribute.vue`、`views/grading.vue`、`components/photoEditor.vue` |
| vue-cropper | 0.6 | 头像裁剪 | `views/personal/userAvatar.vue` |
| typed.js | 2.1 | AI 回答的打字机效果 | `views/knowledgeExtension/index.vue` |
| vue-clipboard2 | 0.3 | 一键复制 AI 评语 | `src/main.js:37-38`、`views/multipleRemark/index.vue` |
| default-passive-events | 2 | 消除 Chrome 关于 passive 事件监听的警告 | `src/main.js:34` |
| Sass | 1.69 | 样式预处理 | 各 `.vue` 的 `<style lang="scss">` |

注意几个容易被问倒的事实：
- `package.json` 里有 **Vuex**，但 `main.js` 没有创建 store，**项目实际没用 Vuex**。用户信息放在 `localStorage`。
- `package.json` 里有 **Axios**，但实际请求几乎都是 **jQuery AJAX**。如果面试官问为什么，诚实回答：“项目初期团队对 jQuery AJAX 更熟，后来引入了 axios 但没来得及统一迁移，这是我认为最该重构的地方之一”（见 5.2）。
- 原 README 写的是 Vue 3，**实际是 Vue 2.7**，以 `package.json` 为准。

---

## 3. 项目结构与核心流程

### 3.1 目录

```
smart-master/
├── vue.config.js            # 开发端口 8081，/grading 代理到后端 8080
├── .env.development       # VUE_APP_ENV = serve，决定 baseUrl
└── src/
    ├── main.js              # 注册全局插件：ElementUI、$、bus、$http、baseUrl、$echarts、剪贴板
    ├── App.vue              # 只有 <router-view>，加全局样式
    ├── api/baseUrl.js       # 根据环境返回接口前缀
    ├── router/index.js      # 路由表（全部懒加载）
    ├── components/
    │   ├── navcon.vue       # 顶部导航，按角色显示不同菜单
    │   ├── Pagination.vue   # 封装 el-pagination
    │   └── photoEditor.vue  # 封装 Toast UI 图片编辑器
    └── views/
        ├── login.vue / register.vue
        ├── home.vue                 # 主布局：导航 + 子路由出口
        ├── personal/                # 个人中心、改密码、头像裁剪
        ├── examination/             # 组长：试卷列表、上传答题卡
        ├── task/                    # 组长：框选题目区域、分配阅卷任务
        ├── teacherMark/             # 老师：阅卷任务列表
        ├── grading.vue              # 老师：逐张阅卷（人工 + AI）
        ├── multipleRemark/          # 老师：AI 生成评语
        ├── studyAnalysis/           # 学情分析图表
        ├── studentTable/ studentLook/  # 学生成绩
        └── knowledgeExtension/      # 学生：AI 知识延展对话
```

### 3.2 三种角色、一条主线

```
组长(roleId=1)                        老师(roleId=2)                  学生
上传答题卡zip + 样卷                   查看分到的任务                   查看成绩
   │ upload.vue                          │ teacherMark                    │ studentLook
   ▼                                     ▼                               ▼
在样卷上框选每道题的区域  ──后端切图──▶  逐张阅卷 grading.vue           学情分析 / AI 知识延展
   │ task/distribute.vue                 │ 手动打分 / AI 打分
   ▼                                     ▼
点击题目区域，选老师分配任务             AI 生成个性化评语 multipleRemark
   │ task/list.vue
```

### 3.3 全局约定

- **接口地址**：`this.baseUrl + "/grading/xxx"`。开发时 `baseUrl` 为空字符串，请求发到 8081，由 `vue.config.js` 的 proxy 转发到 `http://localhost:8080`（后端 context-path 是 `/grading`）。见 `src/api/baseUrl.js`。
- **鉴权**：登录后把 `token` 存到 `localStorage`，每个请求在 header 里手动带上 `token`。
- **用户上下文**：`localStorage` 存 `token / school / userId / roleId`；`sessionStorage` 存当前导航 tab `activeName`。
- **组件通信**：父子组件用 `props / $emit`（如 `Pagination.vue` 通过 `$emit('callFather', pageparm)` 通知父组件翻页）；`main.js:17` 挂了一个全局事件总线 `Vue.prototype.bus = new Vue()`。

---

## 4. 核心模块逐个讲透

### 4.1 登录与角色导航

- `views/login.vue` 的 `loginSubmit()`：先 `this.$refs.form.validate()` 做表单校验，再 `$.ajax` POST `/grading/user/login`（`contentType: "application/json"`，`data: JSON.stringify(this.form)`）。成功后把 `token / school / userId / roleId` 写入 `localStorage`，`this.$router.push('/index')`。
- `components/navcon.vue`：从 `localStorage` 读 `roleId`，`tabClick()` 里按角色跳转不同路由：
  - 1 = 组长：试卷管理 / 阅卷管理 / 学情分析
  - 2 = 老师：老师阅卷 / 学情分析 / 多样评语
  - 其他 = 学生：成绩 / 知识延展
- 当前 tab 存在 `sessionStorage.activeName`，**刷新页面后导航高亮不丢**；关闭标签页自动清掉，下次登录从首页开始。
- 退出登录 `exitLogin()` 调 `/grading/user/logout`，清除 token 并跳回登录页（`navcon.vue:75-78`）。

### 4.2 路由

- `src/router/index.js`：全部用 `component: () => import('@/views/xxx.vue')` 懒加载，打包时每个页面单独一个 chunk，首屏只加载登录页。
- `/home` 是布局路由，业务页面都是它的 `children`，所以导航栏只渲染一次，切换页面只替换 `<router-view>`。
- 没有写 `mode`，默认是 **hash 模式**（URL 里带 `#`）。好处是部署时不需要服务端做 history fallback 配置。
- 每个路由的 `meta` 里写了 `requireAuth`，但**没有 `router.beforeEach` 守卫去读它**（见 5.1）。

### 4.3 上传答题卡（组长）

`views/examination/upload.vue`
- 用 `el-upload` 且 `auto-upload=false`，文件先放进 `answerFileList`（答题卡 zip）和 `sampleFileList`（样卷图片），点提交时一次性发出去。
- **命名校验**：zip 文件名必须是 `xx班_学科`，用正则 `/.+_.+/` 校验，不符合就 `this.$message.error('压缩包命名规范：xx班_学科')`。后端根据文件名识别班级和学科，所以前端先拦一道。
- **本地预览**：样卷用 `URL.createObjectURL(file)` 生成临时地址做预览，`beforeDestroy()` 里 `URL.revokeObjectURL()` 释放。
- **混合提交文件和 JSON**（重点，见 4 难点二）：
  ```js
  const formData = new FormData()
  formData.append('sheet', 答题卡zip)
  formData.append('paper', 样卷)
  formData.append('entity', new Blob([JSON.stringify(data)], { type: 'application/json' }))
  $.ajax({ url: '/grading/grade/upload', type: 'post', data: formData,
           contentType: false, processData: false, headers: { token } })
  ```

### 4.4 框选题目区域（组长）

`views/task/distribute.vue`（`components/photoEditor.vue` 是同类封装）
- 用 Toast UI Image Editor 加载样卷，组长用矩形工具框出每道题。
- 点“上传”时，遍历画布上的对象，`invoke('getObjectProperties', i, ['left','top','width','height','type'])` 读出每个矩形（`distribute.vue:403`），**把中心点坐标换算成左上角坐标**（`distribute.vue:408`），组成数组 `JSON.stringify` 后作为 `paperPoint` 提交到 `/grading/region/add`。
- 编辑器画布导出的是 base64 Data URL，用 `dataURLtoFile()` 转成 `File` 再放进 `FormData` 上传。
- 如果之前已经划分过，进入页面时请求旧坐标，**把左上角坐标再换算回中心点**（`distribute.vue:352`），在画布上把矩形重新画出来，组长可以接着修改。

### 4.5 分配阅卷任务（组长）

`views/task/list.vue`
- 请求 `/grading/region/point/get` 拿到样卷图和 `paperPoint`，`JSON.parse` 成区域数组。
- 样卷在页面上固定显示 850px 宽，原图可能是 2000+px，所以算一个缩放比 `scale = naturalWidth / 850`（`list.vue:587`），每个区域渲染成一个绝对定位的透明 div：`left: item.left/scale + 'px'` …… 点哪个 div 就给哪道题分配任务（`imgClick`，`list.vue:634`）。
- 选择老师后自动平均分配：答题卡数 ÷ 老师数，余数依次加给前几个老师（`list.vue:525` 附近的 `changeSelect`）。
- `surplusNum`（计算属性，`list.vue:645`）实时算出还剩多少份没分，`watch` 它（`list.vue:654`），分超了就提示。
- 提交到 `/grading/task/addList`。

### 4.6 逐张阅卷（老师）

`views/grading.vue`
- `/grading/region/imageList` 拿到这道题所有学生的切图，支持上一张/下一张/点缩略图切换。
- 手动打分：`/grading/region/putScore`；分数用编辑器的 `addText` 直接写在图片上，所见即所得。
- AI 打分：上传标准答案，把“学生答案图 + 标准答案”用 `FormData` 发到 `/grading/region/putIntelScore`；支持单张和**一键 AI 评阅**（`grading.vue:676` 遍历 `imgList` 逐张发请求，`onekeyNum` 计数，用 `$notify` 显示当前已阅数目，`grading.vue:654-658`）。
- AI 评语：`/grading/region/getContent`。
- **刷新不丢上下文**：进入页面时把路由参数存进 `localStorage.gradingMessage`，刷新后路由参数没了就从这里恢复（`grading.vue:717-722`，见难点四）。

### 4.7 多样评语（老师）

`views/multipleRemark/index.vue`
- 班级 → 学生列表 → 选择最多 10 个评价标签（超过 10 个提示）→ `/grading/comment/intelComment` 生成评语 → `vue-clipboard2` 的 `this.$copyText()` 一键复制 → `/grading/comment/put` 发布给学生。

### 4.8 知识延展（学生）

`views/knowledgeExtension/index.vue`
- 左侧试卷列表（带分页），右侧和 AI 的对话。
- `sendMessage()`：输入为空直接返回 → 输入框 disabled，防止重复发送 → 先在对话里插一条“思考中”的占位 → 调 `/grading/comment/extend` → 返回后用 `new Typed(...)` 逐字打出来 → 结束后恢复输入框，并调 `/grading/comment/change` 保存对话记录。
- 切换试卷时用 `/grading/comment/question` 加载这张卷子的历史对话。
- **防串台**：发送时记下 `sendId = paperId`，响应回来时对比当前 `paperId`，不一致说明用户已经切到别的卷子，直接丢弃这次结果（见难点五）。

### 4.9 学情分析

`views/studyAnalysis/index.vue`
- 用 `this.$echarts.init()` 画饼图（分数段分布）、柱状图（各题得分率）、折线图（历次成绩），用 `markLine` 画平均线，题目多时用 `dataZoom` 横向缩放。
- 左侧菜单和右侧内容联动：点菜单滚动到对应图表（`menuSelect`，`studyAnalysis/index.vue:635`）；滚动内容时监听 scroll 反向高亮菜单（`index.vue:1117`）。
- **如实说明**：这个页面目前很多图表数据是写死在前端的示例数据，部分用 `setTimeout` 模拟加载。面试时不要说“全部来自后端实时统计”。可以说：“图表和交互是我实现的，统计接口当时后端还在开发，先用 mock 数据联调样式。”

### 4.10 个人中心与头像裁剪

`views/personal/userAvatar.vue`
- `FileReader.readAsDataURL` 读本地图片 → `vue-cropper` 裁剪 → `getCropBlob()` 拿到 Blob → 包成 File 放进 FormData → PUT `/grading/user/avatar`。
- 同一个组件还能当子组件用：裁剪完通过 `$emit('save', { data, imageUrl, fileName })` 把结果交给父组件（例如注册页），**一个组件复用两种场景**。

---

## 5. 开发中遇到的难点与解决方案（面试重点）

> 每条按「问题 → 原因 → 解决 → 代码位置」组织，面试时用 STAR 讲：场景、问题、我怎么做、结果。

### 难点一：本地开发跨域 + 前后端端口冲突
- **问题**：前端 Vue CLI 默认 8080，后端 Spring Boot 也是 8080，同时启动会冲突；改了端口后浏览器又因为跨域拦截请求。
- **原因**：浏览器同源策略，协议/域名/端口任一不同就是跨域。
- **解决**：前端开发端口改为 8081；`vue.config.js` 配置 `devServer.proxy`，把 `/grading` 开头的请求转发到 `http://localhost:8080`，`changeOrigin: true`。接口前缀统一放在 `src/api/baseUrl.js`，开发环境（`.env.development` 里 `VUE_APP_ENV = serve`）为空字符串，走代理；代码里还预留了 `built` 分支指向 `http://localhost:8089/`，但仓库里没有对应的 `.env` 文件，打包时 `baseUrl` 仍是空字符串，需要部署时用 Nginx 把 `/grading` 转发到后端。
- **原理要讲清楚**：代理是 Node 开发服务器在服务端转发，**服务器之间没有同源限制**；浏览器始终只和 8081 通信，所以不跨域。生产环境用 Nginx 反向代理达到同样效果，或者后端配 CORS。

### 难点二：文件和 JSON 一起上传，后端收不到
- **问题**：上传答题卡时要同时传文件和一个 JSON 对象（考试名称、学科等），一开始用 jQuery 直接传，后端报错或拿不到数据。
- **原因**：① jQuery 默认会把 `data` 序列化成 `key=value` 字符串，并设置 `Content-Type: application/x-www-form-urlencoded`，`FormData` 会被破坏；② 后端用 `@RequestPart("entity")` 接收对象，要求这一段的 Content-Type 是 `application/json`，直接 `append` 字符串的话类型是 text/plain，反序列化失败。
- **解决**：
  - `processData: false`：不让 jQuery 序列化；
  - `contentType: false`：不手动设置，让浏览器自己生成带 `boundary` 的 `multipart/form-data`；**如果手写 `multipart/form-data` 反而缺 boundary，后端无法解析**；
  - JSON 用 `new Blob([JSON.stringify(data)], { type: 'application/json' })` 包一层，给这一段指定类型。
- **代码**：`views/examination/upload.vue` 提交函数、`views/task/distribute.vue:432`、`views/grading.vue` AI 评阅。

### 难点三：图片编辑器的坐标问题（原图 vs 画布 vs 页面）
- **问题 1**：框选的题目区域发给后端切图，切出来的位置是偏的。
- **原因**：Toast UI（底层 Fabric.js）返回的矩形 `left/top` 是**中心点**坐标，而后端裁剪（OpenCV/PIL）需要**左上角**坐标。
- **解决**：上传时 `left = e.left - e.width/2`、`top = e.top - e.height/2`（`distribute.vue:408`）；回显旧区域时反向加回去（`distribute.vue:352`）。坐标以**原图像素**为单位保存，和页面显示大小无关。
- **问题 2**：分配任务页样卷缩小显示，原坐标直接用会超出图片。
- **解决**：按 `scale = naturalWidth / 850` 换算（`list.vue:587`），坐标除以 scale 再渲染。
- **演进**：一开始用 HTML 的 `<map><area>` 做热区（注释代码还留在 `list.vue:87`），但 `area` 的 `coords` 是固定像素，图片缩放后就对不上，也不好加 hover 样式，所以改成绝对定位的 div 覆盖层。

### 难点四：刷新页面后阅卷上下文丢失
- **问题**：从任务列表通过 `this.$router.push({ name, params })` 进入阅卷页，用户按 F5 后试卷 ID、题号都没了，页面空白。
- **原因**：Vue Router 3 中用 `name + params` 传的参数（不出现在 URL 里）只存在内存中，刷新就丢。
- **解决**：进入 `grading.vue` 时如果有 `params`，就 `localStorage.setItem('gradingMessage', JSON.stringify(params))`；没有（说明是刷新）就从 `localStorage` 读回来。
- **还可以说**：更好的方式是把关键 ID 放进 `query` 或动态路由 `/grading/:paperId/:regionId`，URL 本身就是状态，还能分享链接。

### 难点五：AI 请求慢，导致重复提交和结果“串台”
- **问题**：AI 接口要几秒到十几秒。① 用户等不及会连点发送；② 在知识延展页，用户发完问题马上切换到另一张卷子，旧回答回来后显示在了新卷子的对话里。
- **解决**：
  - 发送后立即把输入框/按钮 `disabled`，并插入“思考中”占位，结束后恢复——防重复提交，也给用户反馈；
  - 发送时记录 `this.sendId = this.paperId`（`knowledgeExtension/index.vue:151`），回调里只有 `this.sendId == this.paperId` 才渲染结果（`index.vue:168`），**丢弃过期响应**；
  - 回答用 `typed.js` 逐字展示，体验接近 ChatGPT（注意：是收到完整结果后**前端模拟**的打字效果，不是流式传输）。
- **可延伸**：真正中止请求可以用 jQuery 的 `jqXHR.abort()` 或 axios 的 `AbortController`；想要真流式要后端支持 SSE，前端用 `fetch` + `ReadableStream` 读取。

### 难点六：大图预览导致内存上涨
- **问题**：样卷、答题卡图片很大，反复上传预览后页面越来越卡。
- **原因**：`URL.createObjectURL()` 生成的 blob URL 会一直引用文件，直到页面卸载或手动释放。
- **解决**：在 `upload.vue`、`grading.vue`、`userAvatar.vue` 的 `beforeDestroy()` 里调用 `URL.revokeObjectURL()`。
- **对比**：`FileReader.readAsDataURL` 生成 base64，体积比原文件大约 1/3，适合小图（头像裁剪）；`createObjectURL` 同步、不占额外内存，适合大图预览。项目两种都用了，按场景选。

### 难点七：Canvas 导出的图片怎么上传
- **问题**：Toast UI 编辑后的图片只能拿到 base64 Data URL，后端接口要的是文件。
- **解决**：`dataURLtoFile()`：按逗号拆出 MIME 和 base64，`atob` 解码，写进 `Uint8Array`，`new File([u8arr], filename, { type: mime })`，再 `append` 到 `FormData`。

### 难点八：第三方编辑器的定制和初始化时机
- **问题**：Toast UI 默认是英文，自带“Load/Download”按钮，和业务不符；另外刚进页面马上往画布里画旧矩形会报错或画不出来。
- **解决**：
  - 传入中文 `locale` 和自定义 `theme`，把默认 load/download 按钮隐藏；
  - 在编辑器头部插入自定义按钮，把文字改为“上传”（`distribute.vue:389-394`）；
  - 画布要等图片加载完成后才有尺寸，所以回显矩形放在 `$nextTick` / `setTimeout` 里执行（`distribute.vue:377-381`、`497`）。
- **诚实地说**：`setTimeout` 是经验延时，不够严谨；更好的做法是等 `loadImageFromURL()` 返回的 Promise resolve 后再画。

### 难点九：Vue 2 响应式的坑
- **问题**：老师列表里给每个老师加 `num` 字段（分配份数），改了数字但页面不更新。
- **原因**：Vue 2 用 `Object.defineProperty` 做响应式，**初始化之后新增的属性**不会被劫持；**通过下标给数组赋值**也不会触发更新。
- **解决**：`this.$set(this.datas[i], 'num', 0)`（`list.vue:624`）；需要独立副本时用 `JSON.parse(JSON.stringify(...))` 深拷贝（`list.vue:626`），避免两个列表共享引用、互相影响。
- **加分**：Vue 3 用 Proxy，能拦截新增属性和数组下标，不再需要 `$set`。

### 难点十：打包体积和首屏加载
- **已做**：
  - 路由全部懒加载，每个页面单独 chunk；
  - ECharts 按需引入：只 `echarts.use([BarChart, PieChart, LineChart, ..., CanvasRenderer])`（`main.js:61`），而不是 `import * as echarts from 'echarts'`。
- **现状数据**（本仓库 `npm run build` 实测）：`app.js` 约 25 KB；`chunk-vendors.js` 约 1.7 MB（gzip 后约 500 KB）。
- **还能做**（改进方向）：Element UI 改为按需引入（`babel-plugin-component`）；Toast UI 只在用到的页面异步加载；`webpack-bundle-analyzer` 分析；CDN 引入 Vue/Element；开启 gzip。

### 难点十一：联调时的环境问题（体现排查能力）
这些主要是后端/环境层面的，但前端联调时第一个看到，可以讲“怎么定位”：
- **登录接口报 500**：Network 面板看响应体，发现是 SQL 报错 `Unknown column 'user_account'`。数据库脚本 `grade.sql` 里字段叫 `user_phone`，代码里用的是 `user_account`，还缺两个字段和两张表。写了可重复执行的补丁 `patch_schema.sql` 修正表结构。
- **上传答题卡在 Linux 上失败**：后端调用 Python 切图脚本，路径写死成 Windows 的 `D:\...`。改为相对项目目录，并支持用环境变量 `PYTHON_SCRIPT_DIR` 配置。
- **ngrok 内网穿透时接口返回 HTML**：免费版 ngrok 会先返回一个警告页，AJAX 拿到的是 HTML 而不是 JSON。请求头加 `ngrok-skip-browser-warning: 'true'` 跳过（`navcon.vue:69`）。
- **排查思路**：先看 Network（状态码、请求头、请求体、响应体）→ 判断是前端传参问题还是后端问题 → 再看后端日志。

---

## 6. 项目现存的不足（主动讲，体现反思能力）

面试官很爱问“项目有什么不足？重新做会怎么改？”下面这些**都是真实存在的**，你可以主动说出来，比被面试官发现好得多。

### 6.1 没有路由守卫
- **现状**：`meta.requireAuth` 写了但没用，没登录直接输入 `/#/home` 也能进页面（只是接口会失败）。
- **改进**：
  ```js
  router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requireAuth && !token) return next('/login')
    // 再按 roleId 判断角色能不能访问 to
    next()
  })
  ```
  再配合后端返回的角色做页面级权限（例如学生不能进 `/task`）。

### 6.2 请求代码大量重复，没有统一封装
- **现状**：每个页面都手写 `$.ajax`、手动带 `token` 和 `ngrok` header、各自处理成功/失败；token 过期也没有统一跳登录。
- **改进**：建 `src/utils/request.js`：
  ```js
  const service = axios.create({ baseURL: baseUrl, timeout: 15000 })
  service.interceptors.request.use(cfg => {
    cfg.headers.token = localStorage.getItem('token')
    return cfg
  })
  service.interceptors.response.use(
    res => res.data.status === 200 ? res.data : Promise.reject(res.data),
    err => {
      if (err.response?.status === 401) { localStorage.clear(); router.push('/login') }
      Message.error(err.message); return Promise.reject(err)
    })
  ```
  再按模块拆 `api/user.js`、`api/grade.js`，页面只调用函数。好处：统一 token、统一错误提示、统一 401 处理、方便加 loading 和取消请求。

### 6.3 token 存在 localStorage
- **现状**：简单，但如果有 XSS，脚本可以直接读走 token。退出登录只清了 token 和 school，`userId`、`roleId` 没清（`navcon.vue:75-78`）。
- **改进**：后端设置 `HttpOnly + Secure + SameSite` Cookie，JS 读不到；或者短期 access token + refresh token；退出时 `localStorage.clear()`。同时项目里用 `v-html` 渲染 AI 评语（`grading.vue:129`、`studentLook/index.vue:34`），内容要做转义/过滤（如 DOMPurify）。

### 6.4 jQuery 和 Vue 混用
- **现状**：部分地方用 `$('.xxx').addClass()`、`document.querySelector()` 直接操作 DOM。
- **问题**：Vue 是数据驱动的，手动改 DOM 后 Vue 重新渲染会覆盖掉，状态也不好追踪。
- **改进**：用 `:class` / `v-show` 绑定数据；必须操作 DOM 时用 `ref`。

### 6.5 一键 AI 评阅没有并发控制
- **现状**：`imgList.forEach` 一次性发出所有请求。50 张卷子就是 50 个并发请求，浏览器同域名同时只会跑 6 个左右（HTTP/1.1），AI 接口也可能限流；某一张失败也没有汇总提示。另外用 `this.aiScore[index] = res.data.score`（`grading.vue:615`、`653`）给数组下标赋值，在 Vue 2 里不是响应式的（应 `this.$set`）。
- **改进**：写一个并发池（同时最多 3 个，完成一个补一个），用 `Promise.allSettled` 汇总成功/失败，失败的支持重试。
  ```js
  async function runPool(tasks, limit = 3) {
    const results = []; let i = 0
    const worker = async () => { while (i < tasks.length) { const cur = i++; results[cur] = await tasks[cur]().catch(e => e) } }
    await Promise.all(Array.from({ length: limit }, worker))
    return results
  }
  ```

### 6.6 平均分配的小 bug
- **现状**：`item.num = this.sheetNum / this.checkedBox.length`（`list.vue:525`）是普通除法，10 份分给 3 个老师得到 3.333…，再给前一个人 +1 就是 4.333…。
- **修复**：`Math.floor(this.sheetNum / n)`，再把余数逐个 +1，结果是 4、3、3。**建议你自己把这个改掉**，面试时就能说“我发现并修复了一个分配算法的 bug”。

### 6.7 ECharts 实例没有销毁、没有自适应
- **现状**：`echarts.init()` 后没有保存实例，组件销毁时没有 `dispose()`，窗口缩放时图表不跟着变。
- **改进**：
  ```js
  mounted() { this.chart = this.$echarts.init(this.$refs.pie); window.addEventListener('resize', this.onResize) }
  methods: { onResize() { this.chart && this.chart.resize() } }
  beforeDestroy() { window.removeEventListener('resize', this.onResize); this.chart.dispose() }
  ```
  另外 `$('.elContainer').on('scroll')`（`studyAnalysis/index.vue:1117`）在销毁时也要 `off`，否则会内存泄漏；滚动监听可以加节流。

### 6.8 其他
- Vuex 装了没用；学情分析多为 mock 数据；`default-passive-events` 会把所有监听强制设为 passive，导致某些组件里的 `preventDefault()` 失效（Chrome 会报错），只是“消警告”而不是解决问题。

---

## 7. 面试高频问答

### 项目类

**Q1：介绍一下你负责的部分。**
见第 8 节。重点讲三个最有难度的：图片区域标注 + 坐标换算、文件和 JSON 混合上传、AI 长耗时请求的交互（防重复、防串台、打字机效果）。

**Q2：权限是怎么做的？**
诚实回答：登录后后端返回 `roleId`，前端存到 `localStorage`，导航栏按角色渲染不同菜单并跳转不同页面（`navcon.vue`），接口层面由后端用 token 校验。**前端没有做路由守卫**，这是不足，我会用 `router.beforeEach` + 路由 `meta.roles` 补上（见 6.1）。真正的安全一定要靠后端校验，前端权限只是体验层。

**Q3：token 怎么存、怎么带、过期怎么办？**
存在 `localStorage`，每个请求手动在 header 里带 `token`。目前过期后接口返回错误，页面提示，但没有统一跳登录页。改进：axios 响应拦截器统一处理 401；或者 refresh token 无感刷新（多个请求同时 401 时，只发一次刷新请求，其余请求排队，刷新完成后重放）。

**Q4：文件上传怎么做的？大文件呢？**
`el-upload` 手动上传 + `FormData` + `processData:false` + `contentType:false`，JSON 用 Blob 包装成 `application/json`（难点二）。项目里的 zip 不算特别大，没做分片。大文件可以：`file.slice()` 切片 → 每片带序号和文件 hash（spark-md5，在 Web Worker 里算）→ 并发上传 → 后端合并；断点续传是先问后端哪些片已经上传过；秒传是 hash 已存在就直接返回。

**Q5：图片标注怎么实现的？坐标怎么换算？**
见 4.4 和难点三。关键点：Fabric 中心点 → 左上角；坐标存原图像素；显示时除以缩放比。

**Q6：AI 请求慢，你怎么优化体验？**
见难点五。loading 占位、禁用按钮、过期响应丢弃、打字机效果。强调打字机效果是前端模拟的，不是流式。

**Q7：一键评阅 100 张会怎样？**
主动说出现状的问题（6.5），然后讲并发池方案。

**Q8：遇到最难的问题是什么？**
推荐讲难点三（坐标）或难点二（multipart）。按“现象 → 我怎么排查（Network 看请求体/后端日志） → 找到原因 → 怎么改 → 结果”讲，2 分钟内讲完。

**Q9：项目有什么不足？**
挑 6.1、6.2、6.5 讲，每条都带上改进方案。

### Vue 基础类（结合项目回答）

**Q10：Vue 2 响应式原理？`$set` 用在哪？**
`Object.defineProperty` 劫持 getter/setter，getter 收集依赖（Watcher），setter 通知更新；新增属性、数组下标赋值检测不到。项目里给老师对象新增 `num` 用了 `$set`（`list.vue:624`）。Vue 3 改用 Proxy。

**Q11：computed 和 watch 区别？**
computed 有缓存，依赖不变不重新计算，适合派生值；watch 适合数据变化时执行副作用。项目中 `surplusNum` 是 computed（剩余份数），再 watch 它弹出“分配超出”提示（`list.vue:645`、`654`）。

**Q12：`$nextTick` 是什么？项目里哪里用了？**
Vue 的 DOM 更新是异步批量的，`$nextTick` 在 DOM 更新完成后执行回调（微任务优先：Promise → MutationObserver → setImmediate → setTimeout）。项目里数据变化后要操作编辑器/DOM 时用到（`distribute.vue:497`）。

**Q13：组件通信方式？**
props / $emit（`Pagination.vue` 的 `callFather`、`userAvatar.vue` 的 `save`）；`ref` 调子组件方法（`this.$refs.tuiImageEditor.invoke()`）；事件总线（`main.js:17`）；localStorage 跨页面共享用户信息。还有 provide/inject、Vuex，本项目没用。

**Q14：生命周期？你在哪些钩子里做了什么？**
`created` 发请求、从 localStorage 恢复参数；`mounted` 初始化 ECharts / 编辑器（需要 DOM）；`beforeDestroy` 释放 blob URL。父子顺序：父 beforeCreate → 父 created → 父 beforeMount → 子 beforeCreate…子 mounted → 父 mounted。

**Q15：路由懒加载原理？hash 和 history 区别？**
`() => import()` 是动态 import，webpack 把它拆成单独 chunk，路由匹配时才通过插入 `<script>` 加载。hash 用 `#` 和 `hashchange` 事件，不需要服务端配置；history 用 `pushState` + `popstate`，URL 好看，但刷新时服务端要把所有路径回退到 index.html。项目用的默认 hash。

**Q16：`v-if` 和 `v-show`？`key` 的作用？**
v-if 真正创建/销毁，v-show 只切 `display`，频繁切换用 v-show。key 帮助 diff 复用节点；用 index 做 key 在列表增删时会错位。

### 浏览器 / 工程化类

**Q17：跨域有哪些解决方式？项目用的哪种？**
开发：Vue CLI devServer proxy（项目用的）；生产：Nginx 反向代理或后端 CORS（`Access-Control-Allow-Origin` 等，带 token 自定义头会触发预检 OPTIONS 请求）；JSONP 只支持 GET，基本淘汰。

**Q18：localStorage、sessionStorage、cookie 区别？**
localStorage 永久、约 5MB、同源共享；sessionStorage 关闭标签页就清除、不跨标签页；cookie 约 4KB、每次请求自动携带、可设 HttpOnly。项目：token 和用户信息用 localStorage（记住登录），导航 tab 用 sessionStorage（只在本次会话保持）。

**Q19：Blob、File、Data URL、Object URL 区别？**
Blob 是二进制数据；File 继承 Blob，多了文件名等；Data URL 是 base64 字符串，可以直接放进 src，但体积约大 1/3；Object URL 是指向内存中 Blob 的短引用，需要手动 revoke。项目全都用到了（难点六、七）。

**Q20：打包优化做过哪些？**
见难点十：路由懒加载、ECharts 按需引入；还能做 Element 按需引入、CDN、gzip、bundle 分析。

**Q21：XSS 和 CSRF？**
XSS 是注入恶意脚本：不信任用户输入，`v-html` 内容要过滤，token 不放 localStorage 或用 HttpOnly cookie，CSP。CSRF 是借用浏览器自动带 cookie 伪造请求：本项目 token 放在自定义 header 里，天然不受 CSRF 影响；用 cookie 的话要配 SameSite 和 CSRF token。

---

## 8. 项目介绍模板（约 2 分钟，可以背）

> 这个项目是一个智能阅卷平台，分组长、老师、学生三种角色。我负责前端，技术栈是 Vue 2、Vue Router、Element UI，图表用 ECharts，图片处理用 Toast UI Image Editor。
>
> 核心流程是：组长上传答题卡压缩包和样卷，在样卷上框出每道题的区域，后端按区域把所有学生的答题卡切成小图，然后组长把每道题分给不同老师；老师在线逐张阅卷，可以手动打分，也可以调用 AI 打分和生成评语；学生能看成绩、学情分析图表，还能针对错题和 AI 对话做知识延展。
>
> 开发中我主要解决了几个问题：
> 第一是**图片区域标注**。编辑器返回的是矩形中心点坐标，而后端裁剪需要左上角坐标，一开始切出来的图是偏的，我做了坐标换算，并且统一用原图像素保存，显示时再按缩放比换算，保证不同屏幕上热区都能对准。
> 第二是**文件和 JSON 混合上传**。jQuery 默认会序列化数据，还会设置错误的 Content-Type，我通过关闭 processData、让浏览器自动生成 boundary，再把 JSON 包成 application/json 的 Blob，后端才能用 @RequestPart 正确解析。
> 第三是**AI 接口耗时长**。我做了按钮禁用防止重复提交，用请求发出时的试卷 ID 对比当前 ID，丢弃用户切换卷子后才返回的旧结果，再用 typed.js 做打字机效果提升体验。
> 另外还处理了刷新页面路由参数丢失、大图预览内存释放、开发环境跨域代理这些问题。
>
> 回头看，项目也有不足：请求没有统一封装、没有路由守卫、一键 AI 评阅没做并发控制。如果重构，我会用 axios 拦截器统一处理 token 和错误，加 beforeEach 权限守卫，并用并发池控制 AI 请求。

---

## 9. 自测清单（全部能脱稿回答再去面试）

- [ ] 说出 `main.js` 注册了哪些全局东西，各自做什么
- [ ] 画出三种角色的业务流程图
- [ ] 解释 proxy 为什么能解决跨域
- [ ] 解释 `processData:false` 和 `contentType:false` 各自的作用
- [ ] 手写 `dataURLtoFile`
- [ ] 说出中心点和左上角坐标的换算公式，以及 `scale` 的含义
- [ ] 解释刷新后 `params` 为什么会丢，两种解决方案
- [ ] 解释 `sendId` 防串台的原理
- [ ] 说出 `$set` 的使用场景和 Vue 3 为什么不需要
- [ ] 手写 axios 请求/响应拦截器
- [ ] 手写 `router.beforeEach` 登录守卫
- [ ] 手写并发池
- [ ] 说出 ECharts 销毁和 resize 的写法
- [ ] 说出至少 3 个项目不足和改进方案
- [ ] 把 `list.vue:525` 的分配 bug 自己修掉

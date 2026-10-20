# Vue2 到 Vue3 迁移方案

## 项目现状分析

### 技术栈
- **Vue**: 2.7.16 → 3.x
- **Vue Router**: 3.0.1 → 4.x
- **状态管理**: Vuex 3.0.1 → Pinia（实际项目中未使用Vuex，使用localStorage/sessionStorage）
- **UI框架**: Element UI 2.15.14 → Element Plus
- **构建工具**: Vue CLI 5.0 → Vite（推荐）

### 项目规模
- **Vue组件**: 28个
  - 公共组件: 3个 (Pagination, navcon, photoEditor)
  - 视图组件: 25个
- **路由配置**: 1个文件，约208行，包含嵌套路由
- **API调用**: 使用jQuery + axios
- **状态管理**: 使用localStorage/sessionStorage，无Vuex store

### 代码特点
- 使用选项式API (Options API)
- 大量使用jQuery进行DOM操作和AJAX请求
- 使用`this.$http`、`this.$`、`this.bus`等全局挂载
- 使用Element UI组件库
- 复杂的嵌套路由结构

---

## 迁移方案

### 阶段一：环境准备（1-2天）

#### 1.1 升级依赖包
```bash
# 卸载旧依赖
yarn remove vue vue-router vuex element-ui vue-template-compiler @vue/cli-service

# 安装新依赖
yarn add vue@^3.3.0 vue-router@^4.2.0 pinia@^2.1.0 element-plus@^2.4.0
yarn add -D @vitejs/plugin-vue vite@^5.0.0
```

#### 1.2 配置Vite
创建`vite.config.js`：
```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 8080,
    proxy: {
      "/grading": {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
```

#### 1.3 更新入口文件
将`main.js`迁移到Vue3语法：
- 使用`createApp`替代`new Vue`
- 使用`app.use()`注册插件
- 移除`Vue.prototype`，改用`app.config.globalProperties`

---

### 阶段二：路由迁移（1天）

#### 2.1 升级Vue Router
- `new Router()` → `createRouter()`
- `mode: 'history'` → `history: createWebHistory()`
- 路由配置基本保持不变

#### 2.2 迁移步骤
1. 更新`src/router/index.js`
2. 测试所有路由跳转功能

---

### 阶段三：组件迁移（核心工作，8-12天）

#### 3.1 迁移策略
按功能模块分批迁移，确保每个模块迁移后可独立运行：

**批次1：基础组件**（1-2天）
- App.vue
- Pagination.vue
- navcon.vue

**批次2：认证相关**（1天）
- login.vue
- register.vue

**批次3：个人中心**（1-2天）
- personal/index.vue
- personal/userInfo.vue
- personal/userAvatar.vue
- personal/resetPwd.vue

**批次4：试卷管理**（1-2天）
- examination/index.vue
- examination/upload.vue

**批次5：阅卷管理**（2-3天）
- task/index.vue
- task/list.vue
- task/distribute.vue
- task/details.vue
- teacherMark/index.vue
- teacherMark/startMark.vue
- grading.vue

**批次6：学情分析**（2-3天）
- studyAnalysis/index.vue（最复杂，1362行）

**批次7：学生相关**（1-2天）
- studentTable/index.vue
- studentTable/table.vue
- studentTable/synthesize.vue
- studentLook/index.vue
- knowledgeExtension/index.vue

**批次8：其他**（1天）
- multipleRemark/index.vue
- home.vue
- index.vue

#### 3.2 组件迁移模式

**Vue2 选项式API → Vue3 组合式API**

```vue
<!-- Vue2 -->
<script>
export default {
  data() {
    return {
      count: 0
    }
  },
  methods: {
    increment() {
      this.count++
    }
  },
  created() {
    console.log('created')
  }
}
</script>

<!-- Vue3 组合式API -->
<script setup>
import { ref, onMounted } from 'vue'

const count = ref(0)

const increment = () => {
  count.value++
}

onMounted(() => {
  console.log('mounted')
})
</script>
```

#### 3.3 关键迁移点

**1. 全局属性访问**
```javascript
// Vue2
this.$http
this.$
this.baseUrl
this.$message

// Vue3
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()
proxy.$http
proxy.$
proxy.baseUrl
proxy.$message
```

**2. 生命周期钩子**
```javascript
// Vue2 → Vue3
beforeCreate → setup()
created → setup()
beforeMount → onBeforeMount
mounted → onMounted
beforeUpdate → onBeforeUpdate
updated → onUpdated
beforeDestroy → onBeforeUnmount
destroyed → onUnmounted
```

**3. 组件通信**
```javascript
// Vue2 - bus
this.bus.$emit('event', data)
this.bus.$on('event', callback)

// Vue3 - 使用mitt或provide/inject
import mitt from 'mitt'
const emitter = mitt()
emitter.emit('event', data)
emitter.on('event', callback)
```

**4. Element UI → Element Plus**
- 组件名称基本保持不变
- 部分API有变化，需要查阅文档
- 图标系统变化较大，需要单独引入

#### 3.4 jQuery处理
建议逐步移除jQuery，使用Vue3的原生方式：
- DOM操作 → Vue ref
- AJAX请求 → axios
- 事件绑定 → Vue事件系统

---

### 阶段四：状态管理迁移（2-3天）

#### 4.1 现状
- 项目未使用Vuex
- 使用localStorage/sessionStorage存储token、roleId等
- 组件间通过bus通信

#### 4.2 Pinia迁移方案

**创建Pinia Stores**

```javascript
// stores/user.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const roleId = ref(localStorage.getItem('roleId') || '')
  const userInfo = ref({})

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setRoleId = (newRoleId) => {
    roleId.value = newRoleId
    localStorage.setItem('roleId', newRoleId)
  }

  const clearUser = () => {
    token.value = ''
    roleId.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('roleId')
  }

  return { token, roleId, userInfo, setToken, setRoleId, clearUser }
})
```

#### 4.3 在组件中使用
```javascript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
userStore.setToken('xxx')
```

---

### 阶段五：第三方库迁移（1-2天）

#### 5.1 Element UI → Element Plus
```javascript
// main.js
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

app.use(ElementPlus, { locale: zhCn })
```

#### 5.2 ECharts
- 基本保持不变
- 确保在Vue3中正确初始化

#### 5.3 其他库
- @toast-ui/vue-image-editor → 检查Vue3兼容性
- vue-clipboard2 → vue-clipboard3
- vue-cropper → 检查Vue3版本

---

### 阶段六：测试与优化（2-3天）

#### 6.1 功能测试
- 所有路由跳转
- 登录注册流程
- 各功能模块业务逻辑
- API请求

#### 6.2 性能优化
- 使用Vite的构建优化
- 组件懒加载
- 图片优化

#### 6.3 兼容性测试
- 不同浏览器测试
- 移动端适配

---

## 工作量评估

### 总工作量：15-22个工作日

| 阶段 | 工作量 | 说明 |
|------|--------|------|
| 环境准备 | 1-2天 | 依赖升级、Vite配置 |
| 路由迁移 | 1天 | Vue Router升级 |
| 组件迁移 | 8-12天 | 28个组件，按功能分批 |
| 状态管理 | 2-3天 | Pinia集成 |
| 第三方库 | 1-2天 | Element Plus等 |
| 测试优化 | 2-3天 | 功能测试、性能优化 |

### 风险点
1. **jQuery依赖**: 大量使用jQuery，移除工作量大
2. **复杂组件**: studyAnalysis/index.vue有1362行，逻辑复杂
3. **第三方库兼容性**: 部分库可能没有Vue3版本
4. **Element UI差异**: Element Plus API变化需要适配

### 建议
1. **渐进式迁移**: 不要一次性全部迁移，按功能模块逐步进行
2. **保留jQuery**: 初期可以保留jQuery，逐步替换
3. **充分测试**: 每个批次迁移后都要充分测试
4. **代码审查**: 迁移过程中注意代码质量

---

## 迁移检查清单

### 环境配置
- [ ] 升级package.json依赖
- [ ] 配置vite.config.js
- [ ] 更新main.js
- [ ] 更新index.html

### 路由
- [ ] 升级Vue Router到4.x
- [ ] 更新路由配置
- [ ] 测试所有路由

### 组件
- [ ] App.vue
- [ ] 公共组件（3个）
- [ ] 视图组件（25个）
- [ ] 移除Vue2特有语法
- [ ] 使用组合式API

### 状态管理
- [ ] 创建Pinia stores
- [ ] 迁移localStorage逻辑
- [ ] 更新组件中的状态访问

### 第三方库
- [ ] Element UI → Element Plus
- [ ] 更新其他依赖
- [ ] 测试第三方库功能

### 测试
- [ ] 功能测试
- [ ] 性能测试
- [ ] 兼容性测试

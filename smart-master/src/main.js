import Vue from 'vue'
import App from './App.vue'
// 引入element UI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 引入ant design
// import Antd from 'ant-design-vue';
// import 'ant-design-vue/dist/reset.css';
// Vue.use(Antd)

import router from './router';
// 引入jquery $
import $ from 'jquery'
Vue.prototype.$ = $

//bus非父子组件交流
Vue.prototype.bus = new Vue()

//引入axios
import axios from 'axios'
Vue.prototype.$http = axios

import '@/assets/iconfont/iconfont.js'
import './assets/iconfont/iconfont.css'

// 引入scss
import '@/assets/style/aaa.min.css'

//引入baseUrl
import baseUrl from '@/api/baseUrl.js'
Vue.prototype.baseUrl = baseUrl

//使用该特性来优化页面的滑动性能
import 'default-passive-events'

//一键复制
import VueClipboards from 'vue-clipboard2'
Vue.use(VueClipboards);

// 引入 echarts 核心模块，核心模块提供了 echarts 使用必须要的接口。
import * as echarts from 'echarts/core';
// 引入柱状图图表，图表后缀都为 Chart
import { BarChart, PieChart, LineChart } from 'echarts/charts';
// 引入提示框，标题，直角坐标系，数据集，内置数据转换器组件，组件后缀都为 Component
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  MarkLineComponent,
  LegendComponent,
  DataZoomComponent
} from 'echarts/components';
// 标签自动布局、全局过渡动画等特性
import { LabelLayout, UniversalTransition } from 'echarts/features';
// 引入 Canvas 渲染器，注意引入 CanvasRenderer 或者 SVGRenderer 是必须的一步
import { CanvasRenderer } from 'echarts/renderers';

// 注册必须的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  MarkLineComponent,
  LegendComponent,
  DataZoomComponent,
  BarChart,
  PieChart,
  LineChart,
  LabelLayout,
  UniversalTransition,
  CanvasRenderer
]);

Vue.prototype.$echarts = echarts

import '@/assets/style/background.css'
// 使用element UI
Vue.use(ElementUI);

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router
}).$mount('#app')

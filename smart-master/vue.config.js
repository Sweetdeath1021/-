const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: 'localhost',//本地地址
    port: '8080',//端口号
    hot: true,//热更新自动刷新
    open: true,//自动打开
    proxy: {
      "/grading": {
        target: 'http://localhost:8080',//代理到本地后端服务
        changeOrigin: true,//允许跨域请求
        secure: false,
      }
    },
    client: {
      overlay: false
    }
  }
})
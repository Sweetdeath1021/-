let baseUrl = ''

switch (process.env.VUE_APP_ENV) {
    case 'serve':
        //开发环境url - 使用相对路径，通过vue.config.js代理到本地后端
        baseUrl = ""  // 空字符串表示相对路径，会通过代理转发
        // 如果需要使用远程ngrok，取消下面注释（需要后端配置CORS）
        // baseUrl = "https://expert-lobster-rapidly.ngrok-free.app"
        break
    case 'built':
        baseUrl = "http://localhost:8089/"   //生产环境url
        break
}

export default baseUrl;
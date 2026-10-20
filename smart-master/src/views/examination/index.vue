<template>
    <el-container>
        <el-aside class="aside">
            <el-row class="tac">
      <el-col class="elCol">
        <!-- <div class="a"></div> -->
        <el-menu
          :default-active="defaultActive"
          class="el-menu-vertical-demo"
          @select="selectMenu"
          router>
            <el-menu-item index="/examination/upload" class="upload">
                <img src="../../assets/img/上传 (1).svg" alt="" class="icon">
                <span slot="title">上传试卷</span>
            </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
        </el-aside>
        <el-main>
            <el-container class="main">
                <el-header class="header border">
                    <el-breadcrumb separator="/" class="breadcrumb">
                      <el-breadcrumb-item><span style="font-weight: 700;color: black;"><img src="../../assets/img/018_主页.svg" class="breadHeader"/>试卷管理</span></el-breadcrumb-item>
                      <el-breadcrumb-item><span class="text">上传试卷</span></el-breadcrumb-item>
                    </el-breadcrumb>
                </el-header>
                <el-main class="elMain">
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: 'ExaminationSmart',
    data() {
        return {
            defaultActive:'/examination/upload'
        }
    },
    methods: {
        selectMenu(index) {
            if (index == '/examination/upload') {
                this.$('.text').text('上传试卷')
            }
        }
    },
    mounted() {
        this.$('.upload').click()
    }
}
</script>

<style scoped>
.el-main {
    flex: initial;
}
.breadHeader {
    margin-bottom: 3px;
    height: 32px;
    width: 32px;
    vertical-align: middle;
}
.icon2 {
    height: 32px;
    width: 32px;
    margin-bottom: 2px;
}
.border {
  border: 1px solid #86868659;
}
.main {
    display: flex;
    flex-direction: column;
    align-items: center;
}
.a {
    position: absolute;
    width: 250px;
    height: 250px;
    background: #88ecd0;
    border-radius: 50%;
    right: 5px;
    bottom: 50px;
}
.elCol {
    position: relative;
}
.aside {
    overflow: hidden;
    width: 150px!important;
    height: 91.8vh;
    border-top: 1px solid #f4f4f4;
    /* background: #fff; */
    position: sticky;
    top: 0;
    border-right: 1px solid #86868659;
}
.el-menu-vertical-demo{
    /* margin: 10px 10px; */
    border: none;
    /* border-radius: 10px; */
    padding-top: 20px;
    height: 91.8vh;
    background: #ffffff8c;
    backdrop-filter: blur(5px);
    -webkit-backdrop-filter: blur(5px);
    -moz-backdrop-filter: blur(5px);
    -ms-backdrop-filter: blur(5px);
}/* background: -webkit-linear-gradient(top,#a2f7b5,#f9fff9); */
.header {
    position: fixed;
    height: 50px!important;
    z-index: 5;
    margin: 10px 0;
    border-radius: 12px;
    width: 85%;
    background-color: #ffffffc2;
}
.breadcrumb {
    height: 50px;
    line-height: 50px;
}
.el-breadcrumb__item {
    height: 50px;
}
.el-menu-item {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin: 10px 23px;
    border-radius: 10px;
    height: 70px;
}
.el-menu-item i {
    margin-right: 0;
    margin-bottom: 2px;
}
.el-menu-item, .el-submenu__title {
    line-height: initial;
}
.el-menu-item.is-active {
    color: #00ce95;
}
.el-menu-item.is-active {
    background-color: #d4fff4;
    border: 1px solid #00ce95;
}
.el-menu-item:focus, .el-menu-item:hover {
    background-color: #d4fff4;
}
</style>
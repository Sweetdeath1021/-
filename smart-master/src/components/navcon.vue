<template>
    <div class="row">
        <div class="left">
            <router-link to="/index">
                <img src="../assets/img/未命名的设计 (4).svg" alt=""
                    style="background-color: transparent;position: absolute;bottom: 7px;width: 150px;height: 150px;"
                    @click="goIndex">
                <!-- <h1>智能阅卷</h1> -->
            </router-link>
        </div>
        <div class="middle">
            <el-tabs class="elTab" @tab-click="tabClick" v-model="activeName" v-if="roleId == 1">
                <el-tab-pane label="试卷管理" name="first"></el-tab-pane>
                <el-tab-pane label="阅卷管理" name="second"></el-tab-pane>
                <el-tab-pane label="学情分析" name="third"></el-tab-pane>
            </el-tabs>
            <el-tabs class="elTab" @tab-click="tabClick" v-model="activeName" v-if="roleId == 2">
                <el-tab-pane label="线上阅卷" name="first"></el-tab-pane>
                <el-tab-pane label="学情分析" name="second"></el-tab-pane>
                <el-tab-pane label="多样评语" name="third"></el-tab-pane>
            </el-tabs>
            <el-tabs class="elTab" @tab-click="tabClick" v-model="activeName" v-if="roleId == 3">
                <el-tab-pane label="考试信息" name="first"></el-tab-pane>
                <el-tab-pane label="知识延展" name="second"></el-tab-pane>
            </el-tabs>
        </div>
        <div class="right">
            <el-menu class="el-menu-demo" mode="horizontal">
                <el-submenu index="2" class="submenu">
                    <template slot="title">
                        <img :src="imgUrl" class="img" />
                    </template>
                    <template slot="title">{{ userName }}</template>
                    <el-menu-item index="2-1" @click="goPersonal">
                        <svg class="icon saiyabo" aria-hidden="true">
                            <use xlink:href="#icon-gerenzhongxin"></use>
                        </svg>个人中心
                    </el-menu-item>
                    <el-menu-item index="2-2" @click="exitLogin">
                        <svg class="icon saiyabo" aria-hidden="true">
                            <use xlink:href="#icon-tuichudenglu"></use>
                        </svg>退出登录
                    </el-menu-item>
                </el-submenu>
            </el-menu>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            imgUrl:require('../assets/img/默认头像.svg'),
            userName: '赛鸭脖',
            activeName: sessionStorage.getItem('activeName') || '',
            roleId:localStorage.getItem('roleId'),
        }
    },
    name: 'navconPage',
    methods: {
        exitLogin() {
            this.$.ajax({
                url: this.baseUrl + '/grading/user/logout',
                type: 'get',
                contentType: "application/json",
                headers: {
                    'token': localStorage.getItem('token'),
                    'ngrok-skip-browser-warning': 'true'
                },
                success: res => {
                    // console.log(res);
                    if (res.status == 200) {
                        this.$router.push('/login')
                        localStorage.removeItem('token')
                        localStorage.removeItem('school')
                        this.$message.success('注销成功')
                        sessionStorage.removeItem('activeName')
                    }
                    else {
                        this.$message.error('退出失败')
                    }
                },
                error: () => {
                    this.$message.error('退出失败，请稍后再试')
                }
            })
        },
        goPersonal() {
            sessionStorage.removeItem('activeName')
            this.activeName = ''
            this.$router.push('/personal')
        },
        tabClick(tab) {
            if (tab.index == 0) {
                if (sessionStorage.getItem('activeName') !== 'first') {
                    sessionStorage.setItem('activeName', 'first')
                    if (this.roleId == 1) {
                        this.$router.push('/examination')
                    } else if (this.roleId == 2) {
                        this.$router.push('/teacherMark')
                    } else {
                        this.$router.push('/studentTable')
                    }
                }
            } else if (tab.index == 1) {
                if (sessionStorage.getItem('activeName') !== 'second') {
                    sessionStorage.setItem('activeName', 'second')
                    if (this.roleId == 1) {
                        this.$router.push('/task')
                    } else if (this.roleId == 2) {
                        this.$router.push('/teacherAnalysis')
                    } else {
                        this.$router.push('/knowledgeExtension')
                    }
                }
            } else if (tab.index == 2) {
                if (sessionStorage.getItem('activeName') !== 'third') {
                    sessionStorage.setItem('activeName', 'third')
                    if (this.roleId == 1) {
                        this.$router.push('/manageAnalysis')
                    } else if (this.roleId == 2) {
                        this.$router.push('/multipleRemark')
                    }
                }
            }
            // console.log(this.activeName);
        // console.log(this.$router.currentRoute.fullPath);
        },
        getUser() {
            this.$.ajax({
                url: this.baseUrl + '/grading/user/info',
                type: 'get',
                headers: {
                    'token': localStorage.getItem('token'),
                    'ngrok-skip-browser-warning': 'true'
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    this.user = res.data
                    this.imgUrl = res.data.userImage
                    this.userName = res.data.userName
                },
                error: () => {
                    this.$message.error("连接出错，请检查网络连接或服务器状态。")
                }
            })
        },
        goIndex() {
            this.activeName = ''
            sessionStorage.removeItem('activeName')
        }
    },
    created() {
        this.getUser()
    }
}
</script>

<style>

.el-menu--popup {
    min-width: 110px;
    width: 127px;
    color:rgba(0,0,0,.85);
}
</style>
<style scoped>
.img {
    height: 36px;
    width: 36px;
    margin-right: 5px;
    border-radius: 50%;
}
.elTab /deep/ .el-tabs__item.is-active {
    color: #00ce95;
}
.elTab /deep/ .el-tabs__item:hover {
    color: #00ce95;
}
.elTab /deep/ .el-tabs__active-bar {
    background-color: #00ce95;
}
.row {
    display: flex;
    align-items: center;
    margin: 0 auto;
    padding-left: 8px;
    /* max-width: 1280px; */
    max-width: 86%;
    height: 100%;
    /* background-color: blue; */
}
.left a {
    width: 125px;
}
.left {
    position: relative;
    height: 64px;
    padding-left: 8px;
    min-width: 192px;
    /* background-color: brown; */
}
.left a {
    display: flex;
    height: 100%;
    align-items: center;
}
.left img {
    height: 45px;
    width: 45px;
}
.left h1 {
    display: inline-block;
    color: rgba(0,0,0,.85);
    font-size: 18px!important;
    font-weight: 500;
    margin: 0;
}
.middle {
    flex: 1;
    height: 64px;
}
.el-tab-pane {
    display: none;
}
.el-tabs {
    height: 100%;
}
.elTab /deep/ .el-tabs__item {
    height: 64px;
    line-height: 64px;
    padding-left: 20px!important;
    padding-top: 2px!important;
}

.elTab /deep/ .el-tabs__nav-wrap::after {
    height: 0;
}
.right {
    height: 63px;
    min-width: 242px;
    /* background-color: blueviolet; */
}
.dropdown /deep/ .popper__arrow {
    display: none;
}
.el-menu-demo {
    float: right;
}
.el-menu.el-menu--horizontal{
    border:none;
}
.submenu /deep/ .el-submenu__icon-arrow {
    display: none;
}
.submenu /deep/ .el-submenu__title:hover {
    background:rgba(0,0,0,.025);
}
.submenu /deep/ .el-submenu__title {
    height: 63px;
    border: none!important;
    padding-top: 2px;
    color: #4c4c4c;
}
.el-avatar {
    margin-right: 5px;
}
.saiyabo {
    height: 18px;
    width: 18px;
    margin: 0 8px 0 3px;
}
.el-menu-item:nth-child(1) {
    color: rgba(0, 0, 0, 0.553);
}
.el-menu-item:nth-child(2) {
    color: red;
}
.el-menu-item:hover {
    background: rgba(0,0,0,.025);
}
</style>
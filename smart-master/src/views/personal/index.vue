<template>
  <div class="app-container">
    <el-row>
      <div class="flexBox">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user=1 ref="userAvatar"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <i class="el-icon-user-solid"></i>用户名
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <i class="el-icon-phone"></i>手机号码
                <div class="pull-right">{{ user.userPhone }}</div>
              </li>
              <li class="list-group-item">
                <i class="el-icon-message"></i>邮箱
                <div class="pull-right">{{ user.userEmail }}</div>
              </li>
              <li class="list-group-item">
                <i class="el-icon-office-building"></i>学校
                <div class="pull-right">{{ user.userSchool }}</div>
              </li>
              <li class="list-group-item">
                <i class="el-icon-male" v-if="user.userSex == 1"></i><i class="el-icon-female" v-else></i>性别
                <div class="pull-right">{{ user.userSex == 1?'男': '女' }}</div>
              </li>
              <!-- <li class="list-group-item">
                <i class="el-icon-date"></i>创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li> -->
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo ref="userInfo"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
      </div>
    </el-row>
  </div>
</template>

<style scoped>
.app-container{
  margin-top: 20px;
}
.el-row {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 12px;
}
/* .el-card__body {
  height:388.4px;
} */
.flexBox {
  display: flex;
}
.el-col {
  padding: 0 12px;
}
.el-card {
  border-radius: 12px;
  box-shadow: none!important;
}
</style>

<script>
import $ from 'jquery';
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";

export default {
  name: "personalPage",
  components: { userAvatar, userInfo, resetPwd},
  data() {
    return {
      user: {
        // userName: '张三',
        // userPhone: '13546795826',
        // userEmail: '13135@qq.com',
        // userFirm: '禹乐强盗无能有限公司',
        // userGender: 0
      },
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
      user1: {},
    };
  },
  created() {
    this.getUser();
  },
  mounted() {
    
  },
  destroyed() {
  },
  methods: {
    getUser(){
      $.ajax({
        url: this.baseUrl + '/grading/user/info',
        type: 'get',
        headers: {
          'token': localStorage.getItem('token'),
          'ngrok-skip-browser-warning': 'true'
        },
        contentType: "application/json",
        success: res => {
          // console.log(res);
          this.user = res.data
          this.$refs.userAvatar.getImage(res.data.userImage)
          this.$refs.userInfo.getUser(JSON.parse(JSON.stringify(res.data)))
        },
        error: () => {
          this.$message.error("请求发送失败，请检查网络连接或服务器状态。")
        }
      })
    },
    //修改信息后重新获取用户信息
    getUserToo(){
      this.getUser()
    }
  }
};
</script>

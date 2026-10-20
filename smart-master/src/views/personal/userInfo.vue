<template>
  <el-form ref="form" :model="localUser" :rules="rules" label-width="80px">
    <el-form-item label="用户名" prop="userName">
      <el-input v-model="localUser.userName" />
    </el-form-item> 
    <el-form-item label="手机号码" prop="userPhone">
      <el-input v-model="localUser.userPhone" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="userEmail">
      <el-input v-model="localUser.userEmail" maxlength="50" />
    </el-form-item>
    <el-form-item label="学校" prop="userSchool">
        <el-input v-model="localUser.userSchool" maxlength="50" />
      </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="localUser.userSex">
        <el-radio :label='1'>男</el-radio>
        <el-radio :label='0'>女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import $ from 'jquery';

export default {
  created() {
    // console.log(this.localUser);
  },
  data() {
    return {
      localUser: {},
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        userEmailmail: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        userFirm: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
        ],
        userPhone: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // updateUserProfile(this.user).then(response => {
          //   if (response.code === 200) {
          //     this.msgSuccess("修改成功");
          //   }
          // });
          const data = {
            userId: localStorage.getItem('userId'),
            userName: this.user.userName,
            userEmail: this.user.userEmail,
            userPhone: this.user.userPhone,
            userGender: this.user.userGender,
            userFirm: this.user.userFirm
          }
          $.ajax({
            url: 'https://welcomed-readily-spaniel.ngrok-free.app/user/add',
            type: 'post',
            data: JSON.stringify(data),
            contentType: "application/json",
            success: () => {
              // console.log(res);
              this.$emit('save')
              this.$message({
                message: '保存成功',
                type: 'success'
              })
            },
            error: () => {
              alert("请求发送失败，请检查网络连接或服务器状态。")
            }
          })
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    },
    getUser(data) {
      this.localUser = data
      // console.log(this.localUser);
    }
  }
};
</script>
